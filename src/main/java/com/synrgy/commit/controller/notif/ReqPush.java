package com.synrgy.commit.controller.notif;


import lombok.Data;

@Data
public class ReqPush {
    Integer timezone;
    Integer deviceType;
    String identifier;
    String language;
    String deviceModel = "default OS";
    String message;
    String gameVersion;
    String deviceOS;
    String messages;
    String identifierAuthHash;
    Integer  notificationTypes;
    String idPushNotif;

}
