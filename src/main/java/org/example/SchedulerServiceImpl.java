package org.example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SchedulerServiceImpl implements SchedulerService{
    @Autowired
    private ServiceOperatorRepository operatorRepository;
    //We are assuming there is only one customer, for multiple customers, we will always have customer id in request context
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    private static final String IST = "Asia/Kolkata";

    @Override
    public List<ServiceOperator> getAllOperators() {
        return operatorRepository.findAll();
    }

    @Override
    public String scheduleAppointmentWithOperator(Long operatorId, Integer slot) {
        List<Appointment> appointmentsForOperator = appointmentRepository.findByOperatorIdAndSlotAndCreatedDateAndStatus(
                operatorId, slot, LocalDate.now(ZoneId.of(IST)), AppointmentStatus.BOOKED
        );
        if(appointmentsForOperator.isEmpty()){
            Optional<ServiceOperator> operator = operatorRepository.findById(operatorId);
            if(operator.isPresent()){
                return saveAppointment(operator.get(), customerRepository.findAll().get(0), slot, AppointmentStatus.BOOKED);
            } else {
                throw new RuntimeException("No operator exists with operator id");
            }
        }
        throw new RuntimeException("Failed to schedule appointment. Please check the slot requested");
    }

    @Override
    public String scheduleAppointment(Integer slot) {
        List<Appointment> slotsBooked = appointmentRepository.findBySlotAndCreatedDateAndStatus(slot, LocalDate.now(ZoneId.of(IST)), AppointmentStatus.BOOKED);
        List<ServiceOperator> availableOperators = operatorRepository.findAll().stream().filter(operator -> slotsBooked.stream().noneMatch(appointment ->
                operator.getId().equals(appointment.getOperator().getId()))).collect(Collectors.toList());
        if(!availableOperators.isEmpty()){
            ServiceOperator serviceOperator = availableOperators.get(0);
            return saveAppointment(serviceOperator, customerRepository.findAll().get(0), slot, AppointmentStatus.BOOKED);
        }
        throw new RuntimeException("Failed to schedule appointment. Please check if appointments are available in given slot");
    }

    @Override
    public String rescheduleAppointment(String appointmentId, Integer slot, Boolean useSameOperator) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if(appointment.isPresent()){
            Appointment reschedulingAppointment = appointment.get();
            if(AppointmentStatus.BOOKED.equals(reschedulingAppointment.getStatus())) {
                reschedulingAppointment.setStatus(AppointmentStatus.RESCHEDULED);
                appointmentRepository.save(reschedulingAppointment);
                if (useSameOperator) {
                    return scheduleAppointmentWithOperator(reschedulingAppointment.getOperator().getId(), slot);
                } else {
                    return scheduleAppointment(slot);
                }
            }
        }
        throw new RuntimeException("Appointment id is invalid or appointment is not in booked state");
    }

    @Override
    public void cancelAppointment(String appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if(appointment.isPresent()){
            Appointment cancellingAppointment = appointment.get();
            cancellingAppointment.setStatus(AppointmentStatus.CANCELLED);
            appointmentRepository.save(cancellingAppointment);
            return;
        }
        throw new RuntimeException("Appointment id is invalid");
    }

    @Override
    public List<AppointmentResponseDTO> getBookedAppointmentsByOperator(Long operatorId) {
        return appointmentRepository.findByOperatorIdAndCreatedDateAndStatus(operatorId, LocalDate.now(ZoneId.of(IST)),
                AppointmentStatus.BOOKED).stream().map(AppointmentResponseDTO::new).collect(Collectors.toList());
    }

    @Override
    public Map<Long, List<AppointmentResponseDTO>> getBookedAppointments() {
        Map<Long, List<AppointmentResponseDTO>> allBookedAppointments = new HashMap<>();
        List<Long> operatorIds = operatorRepository.findAll().stream().map(ServiceOperator::getId).collect(Collectors.toList());
        appointmentRepository.findByCreatedDateAndStatus(LocalDate.now(ZoneId.of(IST)), AppointmentStatus.BOOKED).forEach(appointment -> {
            updateOrCreateAllAppointments(allBookedAppointments, appointment);
        });
        allBookedAppointments.forEach((operatorId, appointments) -> {
            appointments.sort(Comparator.comparing(AppointmentResponseDTO::getEndOfSlot));
        });
        operatorIds.forEach(operatorId -> {
            if(!allBookedAppointments.containsKey(operatorId)){
                allBookedAppointments.put(operatorId, new ArrayList<>());
            }
        });
        return allBookedAppointments;
    }


    @Override
    public List<OpenSlotResponseDTO> getOpenSlotsByOperator(Long operatorId) {
        Map<Long, List<OpenSlotResponseDTO>> openSlotsMap = getOpenSlots();
        if(openSlotsMap.containsKey(operatorId)){
            return openSlotsMap.get(operatorId);
        }
        return new ArrayList<>();
    }

    @Override
    public Map<Long, List<OpenSlotResponseDTO>> getOpenSlots(){
        Map<Long, List<AppointmentResponseDTO>> allBookedAppointments = getBookedAppointments();
        Map<Long, List<OpenSlotResponseDTO>> allOpenSlots = new HashMap<>();
        allBookedAppointments.forEach((operatorId, bookedAppointments) -> {
            if(bookedAppointments.size() > 0) {
                bookedAppointments.sort(Comparator.comparing(AppointmentResponseDTO::getEndOfSlot));
                for (int index = 0; index < bookedAppointments.size(); index++) {
                    AppointmentResponseDTO currentAppointment = bookedAppointments.get(index);
                    if(index + 1 < bookedAppointments.size()){
                        AppointmentResponseDTO nextAppointment = bookedAppointments.get(index + 1);
                        if(nextAppointment.getStartOfSlot().equals(currentAppointment.getEndOfSlot())) {
                            continue;
                        } else {
                            updateOrCreateAllOpenSlots(allOpenSlots, currentAppointment, currentAppointment.getEndOfSlot(), nextAppointment.getStartOfSlot());
                        }
                    } else {
                        if(currentAppointment.getEndOfSlot() != 24){
                            updateOrCreateAllOpenSlots(allOpenSlots, currentAppointment, currentAppointment.getEndOfSlot(), 24);
                        }
                    }
                }
            } else {
                AppointmentResponseDTO appointmentResponseDTO = new AppointmentResponseDTO();
                appointmentResponseDTO.setOperatorId(operatorId);
                updateOrCreateAllOpenSlots(allOpenSlots, appointmentResponseDTO, 0, 24);
            }
        });
        return allOpenSlots;
    }

    private String saveAppointment(ServiceOperator operator, Customer customer, Integer slot, AppointmentStatus status){
        Appointment appointment = new Appointment();
        String appointmentId = UUID.randomUUID().toString();
        appointment.setAppointmentId(appointmentId);
        appointment.setOperator(operator);
        appointment.setCustomer(customerRepository.findAll().get(0));
        appointment.setSlot(slot);
        appointment.setStatus(AppointmentStatus.BOOKED);
        appointment.setCreatedDate(LocalDate.now(ZoneId.of(IST)));
        appointmentRepository.save(appointment).getAppointmentId();
        return appointmentId;
    }

    private void updateOrCreateAllAppointments(Map<Long, List<AppointmentResponseDTO>> allBookedAppointments, Appointment appointment){
        Long operatorId = appointment.getOperator().getId();
        if(allBookedAppointments.containsKey(operatorId)){
            allBookedAppointments.get(operatorId).add(new AppointmentResponseDTO(appointment));
        } else {
            List<AppointmentResponseDTO> listOfAppointments = new ArrayList<>();
            listOfAppointments.add(new AppointmentResponseDTO(appointment));
            allBookedAppointments.put(operatorId, listOfAppointments);
        }
    }

    private void updateOrCreateAllOpenSlots(Map<Long, List<OpenSlotResponseDTO>> openSlots, AppointmentResponseDTO appointmentResponseDTO, Integer startOfSlot, Integer endOfSlot){
        Long operatorId = appointmentResponseDTO.getOperatorId();
        if(openSlots.containsKey(operatorId)){
            openSlots.get(operatorId).add(new OpenSlotResponseDTO(appointmentResponseDTO, startOfSlot, endOfSlot));
        } else {
            List<OpenSlotResponseDTO> openSlotsList = new ArrayList<>();
            openSlotsList.add(new OpenSlotResponseDTO(appointmentResponseDTO, startOfSlot, endOfSlot));
            openSlots.put(appointmentResponseDTO.getOperatorId(), openSlotsList);
        }
    }

}
