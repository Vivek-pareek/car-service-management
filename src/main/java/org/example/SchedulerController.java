package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/schedule")
public class SchedulerController {

    @Autowired
    private SchedulerService schedulerService;

    @GetMapping("/operators")
    public ResponseEntity<ResponseObj> getAllOperators() {
        return ResponseObjUtils.responseObj("List of operators", schedulerService.getOpenSlots());
    }

    @PostMapping("/appointment/{operatorId}")
    public ResponseEntity<ResponseObj> scheduleAppointment(@PathParam("operatorId") Long operatorId, @RequestBody AppointmentRequest appointmentRequest){
        return ResponseObjUtils.responseObj("Appointment Scheduled successfuly with appointment Id : " + schedulerService.scheduleAppointmentWithOperator(operatorId, appointmentRequest.getSlot()), null);
    }
}
