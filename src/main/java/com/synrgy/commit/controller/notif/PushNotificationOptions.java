package com.synrgy.commit.controller.notif;


import com.synrgy.commit.config.Config;
import com.synrgy.commit.service.oauth.TemplateCRUD;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Create by Weslei Dias.
 **/
public class PushNotificationOptions {
    static TemplateCRUD templateCRUD = new TemplateCRUD();

    static Config config = new Config();

    public final static String REST_API_KEY = "OTMzOTc0ZWItZDE2My00MzJlLWI0MzUtMzBhNzcwNTQwNmNi";
    public final static String APP_ID = "73a28f09-dac1-497d-bb9a-366729abe760";

//    public  void main(String[] args){
////        Map map =sendMessageToUserMap("riki test 1", "84b33dba-6937-440f-8504-60b6670d801f");
////        if (!map.get(config.getCode()).equals(config.code_sukses)) {
////            System.out.println("eror riki:\n" + map.get(config.getMessage()));
////        }else{
////            System.out.println("sukses riki:\n" + map.get("data"));
////        }
//
//        ReqPush obj =new ReqPush();
//        obj.setIdentifier("rikialdipari@gmail.com");
//        obj.setTimezone(-28800);
//        obj.setGameVersion("1.0");
//        obj.setDeviceOS("1");
//        obj.setDeviceType(11);
//        obj.setDeviceModel("vivo 1935");
//        obj.setIdentifierAuthHash("kode didpat dari email");
//        Map map =registerDevice(obj);
//        if (!map.get(config.getCode()).equals(config.code_sukses)) {
//            System.out.println("eror riki:\n" + map.get(config.getMessage()));
//        }else{
//            System.out.println("sukses riki:\n" + map.get("data"));
//        }
//    }


    public void sendMessageToAllUsers(String message) {
        try {
            String jsonResponse;

            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization",
                    "Basic " + REST_API_KEY);//REST API
            con.setRequestMethod("POST");

            String strJsonBody = "{"
                    + "\"app_id\": \"" + APP_ID + "\","
                    + "\"included_segments\": [\"All\"],"
                    + "\"data\": {\"foo\": \"bar\"},"
                    + "\"contents\": {\"en\": \"" + message + "\"}"
                    + "}";


            System.out.println("strJsonBody:\n" + strJsonBody);

            byte[] sendBytes = strJsonBody.getBytes("UTF-8");
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();
            System.out.println("httpResponse: " + httpResponse);

            jsonResponse = mountResponseRequest(con, httpResponse);
            System.out.println("jsonResponse:\n" + jsonResponse);

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static String mountResponseRequest(HttpURLConnection con, int httpResponse) throws IOException {
        String jsonResponse;
        if (httpResponse >= HttpURLConnection.HTTP_OK
                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            scanner.close();
        } else {
            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            scanner.close();
        }
        return jsonResponse;
    }

    public void sendMessageToUser(
            String message, String userId) {
        try {
            String jsonResponse;

            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", REST_API_KEY);
            con.setRequestMethod("POST");

            String strJsonBody = "{"
                    + "\"app_id\": \"" + APP_ID + "\","
                    + "\"include_player_ids\": [\"" + userId + "\"],"
                    + "\"data\": {\"foo\": \"bar\"},"
                    + "\"contents\": {\"en\": \"" + message + "\"}"
                    + "}";


            System.out.println("strJsonBody:\n" + strJsonBody);

            byte[] sendBytes = strJsonBody.getBytes("UTF-8");
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();
            System.out.println("httpResponse: " + httpResponse);

            jsonResponse = mountResponseRequest(con, httpResponse);
            System.out.println("jsonResponse:\n" + jsonResponse);

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static Map sendMessageToUserMap(
            String message, String userId, Boolean isSendNotif) {
        if (isSendNotif) {
            Map map = new HashMap<>();
            try {
                String jsonResponse;

                URL url = new URL("https://onesignal.com/api/v1/notifications");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setUseCaches(false);
                con.setDoOutput(true);
                con.setDoInput(true);

                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestProperty("Authorization", REST_API_KEY);
                con.setRequestMethod("POST");

                String strJsonBody = "{"
                        + "\"app_id\": \"" + APP_ID + "\","
                        + "\"include_player_ids\": [\"" + userId + "\"],"
                        + "\"data\": {\"foo\": \"bar\"},"
                        + "\"contents\": {\"en\": \"" + message + "\"}"
                        + "}";

                byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                con.setFixedLengthStreamingMode(sendBytes.length);

                OutputStream outputStream = con.getOutputStream();
                outputStream.write(sendBytes);

                int httpResponse = con.getResponseCode();

                jsonResponse = mountResponseRequest(con, httpResponse);
                if (httpResponse == 200) {
                    return templateCRUD.template1(jsonResponse);
                } else {
                    return templateCRUD.eror(jsonResponse);
                }


            } catch (Throwable t) {
                t.printStackTrace();
                return templateCRUD.eror(t);
            }
        }

        return templateCRUD.template1("not send notif");
    }

    public static Map sendMessageToUserMapKhususProduct(
            String message, String userId, Long productId, Boolean isSendNotif) {
        if (isSendNotif) {


            Map map = new HashMap<>();
            try {
                String jsonResponse;

                URL url = new URL("https://onesignal.com/api/v1/notifications");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setUseCaches(false);
                con.setDoOutput(true);
                con.setDoInput(true);

                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestProperty("Authorization", REST_API_KEY);
                con.setRequestMethod("POST");

                String app = "chute://product/" + productId;
                String strJsonBody = "{"
                        + "\"app_id\": \"" + APP_ID + "\","
                        + "\"app_url\": \"" + app + "\","
                        + "\"include_player_ids\": [\"" + userId + "\"],"
                        + "\"data\": {\"foo\": \"bar\"},"
                        + "\"contents\": {\"en\": \"" + message + "\"}"
                        + "}";


                byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                con.setFixedLengthStreamingMode(sendBytes.length);

                OutputStream outputStream = con.getOutputStream();
                outputStream.write(sendBytes);

                int httpResponse = con.getResponseCode();
                jsonResponse = mountResponseRequest(con, httpResponse);
                if (httpResponse == 200) {
                    return templateCRUD.template1(jsonResponse);
                } else {
                    return templateCRUD.eror(jsonResponse);
                }


            } catch (Throwable t) {
                t.printStackTrace();
                return templateCRUD.eror(t);
            }
        }
        return templateCRUD.template1("not send notif");

    }

    //    https://documentation.onesignal.com/reference/add-a-device
    public static Map registerDevice(
            ReqPush obj) {
        Map map = new HashMap<>();
        try {
            String jsonResponse;

            URL url = new URL("https://onesignal.com/api/v1/players");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", REST_API_KEY);
            con.setRequestMethod("POST");

            obj.setLanguage("en");
            obj.setTimezone(-28800);

            String strJsonBody;

            if (obj.getDeviceOS() == null) {
                strJsonBody = "{"
                        + "\"app_id\": \"" + APP_ID + "\","
                        + "\"identifier\": \"" + obj.getIdentifier() + "\","
                        + "\"language\": \"" + obj.getLanguage() + "\","
                        + "\"timezone\": \"" + obj.getTimezone() + "\","
                        + "\"game_version\": \"" + obj.getGameVersion() + "\","
                        + "\"device_type\": \"" + obj.getDeviceType() + "\","
                        + "\"device_model\": \"" + obj.getDeviceModel() + "\","
                        + "\"notification_types\": \"" + obj.getNotificationTypes() + "\","
                        + "\"identifier_auth_hash\": \"" + obj.getIdentifierAuthHash() + "\","//jika pake email dan SMS
                        + "\"tags\": {\"message\": \"" + obj.getMessage() + "\"}"
                        + "}";
            } else {
                strJsonBody = "{"
                        + "\"app_id\": \"" + APP_ID + "\","
                        + "\"identifier\": \"" + obj.getIdentifier() + "\","
                        + "\"language\": \"" + obj.getLanguage() + "\","
                        + "\"timezone\": \"" + obj.getTimezone() + "\","
                        + "\"game_version\": \"" + obj.getGameVersion() + "\","
                        + "\"device_os\": \"" + obj.getDeviceOS() + "\","
                        + "\"device_type\": \"" + obj.getDeviceType() + "\","
                        + "\"device_model\": \"" + obj.getDeviceModel() + "\","
                        + "\"notification_types\": \"" + obj.getNotificationTypes() + "\","
                        + "\"identifier_auth_hash\": \"" + obj.getIdentifierAuthHash() + "\"," //jika pake email dan SMS
                        + "\"tags\": {\"message\": \"" + obj.getMessage() + "\"}"
                        + "}";
            }


            byte[] sendBytes = strJsonBody.getBytes("UTF-8");
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();


            jsonResponse = mountResponseRequest(con, httpResponse);

            if (httpResponse == 200) {
                return templateCRUD.template1(jsonResponse);
            } else {
                return templateCRUD.eror(jsonResponse);
            }


        } catch (Throwable t) {
            t.printStackTrace();
            return templateCRUD.eror(t);
        }

    }

    public static void main(String[] args) {

    }
}