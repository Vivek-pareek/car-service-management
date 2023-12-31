package org.example;

import java.util.List;
import java.util.Map;

public interface SchedulerService {
    List<ServiceOperator> getAllOperators();
    String scheduleAppointmentWithOperator(Long operatorId, Integer slot);
    String scheduleAppointment(Integer slot);
    String rescheduleAppointment(String appointmentId, Integer slot, Boolean useSameOperator);
    void cancelAppointment(String appointmentId);
    List<AppointmentResponseDTO> getBookedAppointmentsByOperator(Long operatorId);
    Map<Long, List<AppointmentResponseDTO>> getBookedAppointments();

    List<OpenSlotResponseDTO> getOpenSlotsByOperator(Long operatorId);

    Map<Long, List<OpenSlotResponseDTO>> getOpenSlots();
}
