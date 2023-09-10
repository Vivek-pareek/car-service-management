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
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;

    private static final String IST = "Asia/Kolkata";

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
            reschedulingAppointment.setStatus(AppointmentStatus.RESCHEDULED);
            appointmentRepository.save(reschedulingAppointment);
            if(useSameOperator){
                return scheduleAppointmentWithOperator(reschedulingAppointment.getOperator().getId(), slot);
            } else {
                return scheduleAppointment(slot);
            }
        }
        throw new RuntimeException("Appointment id is invalid");
    }

    @Override
    public void cancelAppointment(String appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if(appointment.isPresent()){
            Appointment cancellingAppointment = appointment.get();
            cancellingAppointment.setStatus(AppointmentStatus.CANCELLED);
            appointmentRepository.save(cancellingAppointment);
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
        appointmentRepository.findByCreatedDateAndStatus(LocalDate.now(ZoneId.of(IST)), AppointmentStatus.BOOKED).forEach(appointment -> {
            updateOrCreateAllAppointments(allBookedAppointments, appointment);
        });
        return allBookedAppointments;
    }


    @Override
    public Map<String, List<AppointmentResponseDTO>> getOpenSlotsByOperator(Long operatorId) {
        return null;
    }

    @Override
    public Map<String, List<AppointmentResponseDTO>> getOpenSlots(){
        return null;
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

}
