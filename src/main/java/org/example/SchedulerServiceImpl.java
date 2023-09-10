package org.example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class SchedulerServiceImpl implements SchedulerService{
    @Autowired
    private ServiceOperatorRepository operatorRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public String scheduleAppointmentWithOperator(Long operatorId, Integer slot) {
        List<Appointment> appointmentsForOperator = appointmentRepository.findByOperatorIdAndSlotAndCreatedDateAndStatus(
                operatorId, slot, LocalDate.now(ZoneId.of("Asia/Kolkata")), AppointmentStatus.BOOKED
        );
        if(appointmentsForOperator == null || appointmentsForOperator.isEmpty()){
            Appointment appointment = new Appointment();
            String appointmentId = UUID.randomUUID().toString();
            appointment.setAppointmentId(appointmentId);
            appointment.setOperator(operatorRepository.findById(operatorId).get());
            appointment.setCustomer(customerRepository.findAll().get(0));
            appointment.setSlot(slot);
            appointment.setStatus(AppointmentStatus.BOOKED);
            appointment.setCreatedDate(LocalDate.now(ZoneId.of("Asia/Kolkata")));
            appointmentRepository.save(appointment).getAppointmentId();
            return appointmentId;
        }
        throw new RuntimeException("Failed to schedule appointment. Please check the slot requested");
    }

    @Override
    public String scheduleAppointment(Integer slot) {
        /*List<Appointment> slotsBooked = appointmentRepository.findBySlotAndStatus(slot, AppointmentStatus.BOOKED);
        operatorRepository.findAll().stream().filter(operator -> {
            slotsBooked.stream().map(appointment -> {
                appointment.getOperator().getId()
            })
        })*/
        return null;
    }

    @Override
    public String rescheduleAppointmentWithOperator(Long operatorId, String appointmentId, Integer slot) {
        return null;
    }

    @Override
    public String rescheduleAppointment(String appointmentId, Integer slot) {
        return null;
    }

    @Override
    public void cancelAppointment(String appointmentId) {
        return;
    }

    @Override
    public Map<String, List<AppointmentResponseDTO>> getAppointmentsByOperator(Long operatorId) {
        return null;
    }

    @Override
    public Map<String, List<AppointmentResponseDTO>> getOpenSlotsByOperator(Long operatorId) {
        return null;
    }

    @Override
    public Map<String, List<AppointmentResponseDTO>> getOpenSlots(){
        return null;
    }

}
