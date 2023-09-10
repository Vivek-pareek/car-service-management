package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppointmentRequest {
    private Integer slot;
    private Integer appointmentId;
    private AppointmentStatus appointmentStatus;

    public AppointmentRequest() {}

    public AppointmentRequest(Long operatorId, Integer slot, Integer appointmentId, AppointmentStatus appointmentStatus) {
        this.slot = slot;
        this.appointmentId = appointmentId;
        this.appointmentStatus = appointmentStatus;
    }


    // Getter for slot
    public Integer getSlot() {
        return slot;
    }

    // Setter for slot
    public void setSlot(Integer slot) {
        this.slot = slot;
    }

    // Getter for appointmentId
    public Integer getAppointmentId() {
        return appointmentId;
    }

    // Setter for appointmentId
    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    // Getter for appointmentStatus
    public AppointmentStatus getAppointmentStatus() {
        return appointmentStatus;
    }

    // Setter for appointmentStatus
    public void setAppointmentStatus(AppointmentStatus appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }
}
