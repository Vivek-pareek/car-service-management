package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppointmentResponseDTO {
    private String operatorName;
    private Long operatorId;
    private String customerName;
    private Long customerId;
    private String appointmentId;
    private String slot;
    private LocalDate date;

    public AppointmentResponseDTO() {
    }

    public AppointmentResponseDTO(Appointment appointment) {
        this.operatorId = appointment.getOperator().getId();
        this.operatorName = appointment.getOperator().getName();
        this.customerName = appointment.getCustomer().getName();
        this.customerId = appointment.getCustomer().getId();
        this.appointmentId = appointment.getAppointmentId();
        StringBuilder slotBuilder = new StringBuilder();
        slotBuilder.append(appointment.getSlot() - 1).append(" - ").append(appointment.getSlot());
        this.slot = slotBuilder.toString();
        this.date = appointment.getCreatedDate();
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
