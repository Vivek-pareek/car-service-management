package org.example;

public enum AppointmentStatus {
    BOOKED,
    CANCELLED,
    RESCHEDULED;

    public static boolean canBeBooked(AppointmentStatus status){
        return CANCELLED.equals(status) || RESCHEDULED.equals(status);
    }

}
