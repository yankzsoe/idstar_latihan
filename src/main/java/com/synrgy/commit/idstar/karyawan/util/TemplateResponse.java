package com.synrgy.commit.idstar.karyawan.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TemplateResponse {
    public Map Success(Object data, String message, String status) {
        Map map = new HashMap();
        map.put("data",data);
        map.put("message",message);
        map.put("status",status);
        return map;
    }

    public Map Error(String message, String status) {
        Map map = new HashMap();
        map.put("message",message);
        map.put("status",status);
        return map;
    }
}
