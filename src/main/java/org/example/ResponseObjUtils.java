package org.example;

import org.springframework.http.ResponseEntity;

public class ResponseObjUtils {
    public static ResponseEntity<ResponseObj> responseObj(String message, Object data){
        return ResponseEntity.ok(new ResponseObj(200, true, message, data));
    }
}
