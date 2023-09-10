package org.example;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppointmentResponseDTO {
    private String operatorName;
    private Long operatorId;
    private String customerName;
    private Long customerId;
    private String appointmentId;
    @JsonIgnore
    private Integer startOfSlot;
    @JsonIgnore
    private Integer endOfSlot;
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
        this.startOfSlot = appointment.getSlot() - 1;
        this.endOfSlot = appointment.getSlot();
        StringBuilder slotBuilder = new StringBuilder();
        slotBuilder.append(appointment.getSlot() - 1).append(" - ").append(appointment.getSlot());
        this.slot = slotBuilder.toString();
        this.date = appointment.getCreatedDate();
    }

    public AppointmentResponseDTO(Appointment appointment, Integer startOfSlot, Integer endOfSlot){
        this(appointment);
        StringBuilder slotBuilder = new StringBuilder();
        this.slot = slotBuilder.append(startOfSlot).append(" - ").append(endOfSlot).toString();
        this.customerId = null;
        this.customerName = null;
        this.appointmentId = null;
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

    public Integer getStartOfSlot() {
        return startOfSlot;
    }

    public void setStartOfSlot(Integer startOfSlot) {
        this.startOfSlot = startOfSlot;
    }

    public Integer getEndOfSlot() {
        return endOfSlot;
    }

    public void setEndOfSlot(Integer endOfSlot) {
        this.endOfSlot = endOfSlot;
    }
}
