package com.synrgy.commit.config;

import lombok.Data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class Config {
    String code = "status", message = "message";
    public String code_notFound ="404";

    public String codeRequired ="403";
    public String isRequired =" is Required";

    public String code_sukses = "200";
    public String code_server = "500";
    public String code_null = "1";
    public String message_sukses = "sukses";

    public String dateToString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String format = formatter.format(date);
        System.out.println(format);
       return  format;
    }

    public String convertDateToString(Date date) {

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String strDate = dateFormat.format(date);
//        System.out.println("Date: " + strDate);
        return strDate;
    }

    public String convertDateToStringDate(Date date) {

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String strDate = dateFormat.format(date);
//        System.out.println("Date: " + strDate);
        return strDate;
    }

    public Date convertStringToDateFrom(String date) throws ParseException {

        SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date6 =formatter6.parse(date+" 00:01:01");
        System.out.println("Date: " + date6);
        return date6;
    }

    public Date convertStringToDateTo(String date) throws ParseException {

        SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date6 =formatter6.parse(date+" 23:59:59");
        System.out.println("Date: " + date6);
        return date6;
    }



    public String convertDateToString_expired(Date date) {

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String strDate = dateFormat.format(date);
//        System.out.println("Date: " + strDate);
        return strDate;
    }



}

