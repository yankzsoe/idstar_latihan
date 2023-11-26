package com.synrgy.commit.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class Response {

    public boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public Map ControllerSukses(Object objek){
        Map map = new HashMap();
        map.put("data", objek);

        return map;
    }

    public Map ControllerError(Object objek){
        Map map = new HashMap();
        map.put("message", objek);
        return map;
    }


    public Map Sukses(Object objek){
        Map map = new HashMap();
        map.put("message", "Success");
        map.put("status", "200");
        map.put("data", objek);

        return map;
    }

    public Map Error(Object objek){
        Map map = new HashMap();
        map.put("message", objek);
        map.put("status", "404");
        return map;
    }

    public boolean chekNull(Object obj){
        if(obj == null){
            return true;
        }
        return  false;
    }

    public Map notFound(Object objek){
        Map map = new HashMap();
        map.put("message", objek);
        map.put("status", "404");
        return map;
    }

    public boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }

    public boolean notSimbol(String password) {
        String passwordRegex = "^[a-zA-Z0-9_.-]*$";

        Pattern pat = Pattern.compile(passwordRegex);
        return pat.matcher(password).matches();
    }

    public boolean notNumber(String phonenumber) {
        String phoneRegex = "^0[0-9_.-]*$";

        Pattern pat = Pattern.compile(phoneRegex);

        return pat.matcher(phonenumber).matches();
    }

    public boolean nameNotSimbol(String nama){
        String nameRegex = "^[a-zA-Z\\s]*$";

        Pattern pat = Pattern.compile(nameRegex);

        return pat.matcher(nama).matches();
    }

    public Map isRequired(String obj)  {
        Map map = new HashMap();
        map.put("message", obj);
        map.put("status", "404");
        return map;
    }
}
