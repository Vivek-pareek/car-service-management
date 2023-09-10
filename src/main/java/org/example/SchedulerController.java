package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/schedule")
public class SchedulerController {

    @Autowired
    private SchedulerService schedulerService;

    @GetMapping("/operatorsList")
    public ResponseEntity<ResponseObj> getAllOperators() {
        return ResponseObjUtils.responseObj("List of operators", schedulerService.getAllOperators());
    }

    @GetMapping("/bookedAppointments/{operatorId}")
    public ResponseEntity<ResponseObj> getAllBookedAppointmentsByOperator(@PathVariable("operatorId") Long operatorId){
        return ResponseObjUtils.responseObj("Booked appointments for the requested operator", schedulerService.getBookedAppointmentsByOperator(operatorId));
    }

    @GetMapping("/bookedAppointments")
    public ResponseEntity<ResponseObj> getAllBookedAppointments(){
        return ResponseObjUtils.responseObj("Booked appointments for all operators.", schedulerService.getBookedAppointments());
    }

    @PostMapping("/appointment/{operatorId}")
    public ResponseEntity<ResponseObj> scheduleAppointmentWithOperator(@PathVariable("operatorId") Long operatorId, @RequestBody AppointmentRequest appointmentRequest){
        validateAppointmentRequest(appointmentRequest);
        String appointmentId = schedulerService.scheduleAppointmentWithOperator(operatorId, appointmentRequest.getSlot());
        return ResponseObjUtils.responseObj("Appointment Scheduled successfuly with appointment Id : " + appointmentId, appointmentIdResponseDataMap(appointmentId));
    }

    @PostMapping("/appointment")
    public ResponseEntity<ResponseObj> scheduleAppointment(@RequestBody AppointmentRequest appointmentRequest){
        validateAppointmentRequest(appointmentRequest);
        String appointmentId = schedulerService.scheduleAppointment(appointmentRequest.getSlot());
        return ResponseObjUtils.responseObj("Appointment Scheduled successfuly with appointment Id : " + appointmentId, appointmentIdResponseDataMap(appointmentId));
    }

    @PostMapping("/updateAppointment")
    public ResponseEntity<ResponseObj> updateAppointment(@RequestBody AppointmentRequest appointmentRequest){
        //Here, we are using the same api for cancelling and rescheduling the appointment
        if(AppointmentStatus.CANCELLED.equals(appointmentRequest.getAppointmentStatus())){
            schedulerService.cancelAppointment(appointmentRequest.getAppointmentId());
            return ResponseObjUtils.responseObj("Appointment successfully cancelled", null);
        }
        else if(AppointmentStatus.RESCHEDULED.equals(appointmentRequest.getAppointmentStatus())){
            String appointmentId;
            validateAppointmentRequest(appointmentRequest);
            if(appointmentRequest.isUseSameOperator() != null && appointmentRequest.isUseSameOperator()){
                appointmentId = schedulerService.rescheduleAppointment(appointmentRequest.getAppointmentId(), appointmentRequest.getSlot(), true);
            } else {
                appointmentId = schedulerService.rescheduleAppointment(appointmentRequest.getAppointmentId(), appointmentRequest.getSlot(), false);
            }
            return ResponseObjUtils.responseObj("Successfully updated appointment" + appointmentId, appointmentIdResponseDataMap(appointmentId));
        }
        else {
            return ResponseObjUtils.responseObj("Appointment update status is invalid", null);
        }
    }

    @GetMapping("/getOpenSlots")
    public ResponseEntity<ResponseObj> getOpenSlots(){
        return ResponseObjUtils.responseObj("Open slots details", schedulerService.getOpenSlots());
    }

    @GetMapping("/getOpenSlots/{operatorId}")
    public ResponseEntity<ResponseObj> getOpenSlotsForOperator(@PathVariable Long operatorId){
        return ResponseObjUtils.responseObj("Open slots for requested operator", schedulerService.getOpenSlotsByOperator(operatorId));
    }

    private Map<String, String> appointmentIdResponseDataMap(String appointmentId){
        Map<String, String> responseData = new HashMap<>();
        responseData.put("appointmentId", appointmentId);
        return responseData;
    }

    private void validateAppointmentRequest(AppointmentRequest appointmentRequest){
        if(appointmentRequest.getSlot() != null && (appointmentRequest.getSlot() < 0 || appointmentRequest.getSlot() > 24)){
            throw new RuntimeException("Slot value cannot be below 0 or above 24");
        }
    }
}
