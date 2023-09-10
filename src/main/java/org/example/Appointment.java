package org.example;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "APPOINTMENT")
public class Appointment {

    @Id
    @Column(name = "APPOINTMENTID")
    private String appointmentId;

    @ManyToOne
    @JoinColumn(name = "OPERATOR_ID", referencedColumnName = "ID")
    private ServiceOperator operator;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID")
    private Customer customer;

    @Column(name = "SLOT")
    private Integer slot;

    @Column(name = "CREATED_DATE")
    private LocalDate createdDate;

    @Column(name = "STATUS")
    private AppointmentStatus status;

    public Appointment() {
        // Default constructor
    }

    public Appointment(String appointmentId, ServiceOperator operator, Customer customer, Integer slot, LocalDate createdDate, AppointmentStatus status) {
        this.appointmentId = appointmentId;
        this.operator = operator;
        this.customer = customer;
        this.slot = slot;
        this.createdDate = createdDate;
        this.status = status;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public ServiceOperator getOperator() {
        return operator;
    }

    public void setOperator(ServiceOperator operator) {
        this.operator = operator;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Integer getSlot() {
        return slot;
    }

    public void setSlot(Integer slot) {
        this.slot = slot;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
}
