package org.example;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenSlotResponseDTO {
    private String operatorName;
    private Long operatorId;
    @JsonIgnore
    private Integer startOfSlot;
    @JsonIgnore
    private Integer endOfSlot;
    private String slot;
    private LocalDate date;

    public OpenSlotResponseDTO() {
        // Default constructor
    }

    public OpenSlotResponseDTO(AppointmentResponseDTO appointmentResponseDTO, Integer startOfSlot, Integer endOfSlot) {
        this.operatorName = appointmentResponseDTO.getOperatorName();
        this.operatorId = appointmentResponseDTO.getOperatorId();
        this.startOfSlot = startOfSlot;
        this.endOfSlot = endOfSlot;
        StringBuilder slotBuilder = new StringBuilder();
        this.slot = slotBuilder.append(startOfSlot).append(" - ").append(endOfSlot).toString();
        this.date = appointmentResponseDTO.getDate();
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
