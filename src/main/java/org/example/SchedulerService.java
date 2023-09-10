package org.example;

import java.util.List;
import java.util.Map;

public interface SchedulerService {
    String scheduleAppointmentWithOperator(Long operatorId, Integer slot);
    String scheduleAppointment(Integer slot);
    String rescheduleAppointmentWithOperator(Long operatorId, String appointmentId, Integer slot);
    String rescheduleAppointment(String appointmentId, Integer slot);
    void cancelAppointment(String appointmentId);
    Map<String, List<AppointmentResponseDTO>> getAppointmentsByOperator(Long operatorId);
    Map<String, List<AppointmentResponseDTO>> getOpenSlotsByOperator(Long operatorId);

    Map<String, List<AppointmentResponseDTO>> getOpenSlots();
}
