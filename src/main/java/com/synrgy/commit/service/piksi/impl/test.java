package com.synrgy.commit.service.piksi.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

public class test {
    public static void main(String[] args){
        String a = "check_oust";
        if(a.equals("check_in") || a.equals("check_out")){
            System.out.println("masuk ");
        }else{
            System.out.println("kelaur ");
        }

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String format = formatter.format(date);
        System.out.println(format);
    }
}
