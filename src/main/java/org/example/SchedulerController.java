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

    @GetMapping("/operators")
    public ResponseEntity<ResponseObj> getAllOperators() {
        return ResponseObjUtils.responseObj("List of operators", schedulerService.getOpenSlots());
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
        String appointmentId = schedulerService.scheduleAppointmentWithOperator(operatorId, appointmentRequest.getSlot());
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("appointmentId", appointmentId);
        return ResponseObjUtils.responseObj("Appointment Scheduled successfuly with appointment Id : " + appointmentId, responseData);
    }

    @PostMapping("/appointment")
    public ResponseEntity<ResponseObj> scheduleAppointment(@RequestBody AppointmentRequest appointmentRequest){
        String appointmentId = schedulerService.scheduleAppointment(appointmentRequest.getSlot());
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("appointmentId", appointmentId);
        return ResponseObjUtils.responseObj("Appointment Scheduled successfuly with appointment Id : " + appointmentId, responseData);
    }

    @DeleteMapping("/appointment")
    public ResponseEntity<ResponseObj> deleteAppointment(@RequestBody AppointmentRequest appointmentRequest){
        schedulerService.cancelAppointment(appointmentRequest.getAppointmentId());
        return ResponseObjUtils.responseObj("Appointment cancelled successfuly for appointment Id: " + appointmentRequest.getAppointmentId(), null);
    }

}
