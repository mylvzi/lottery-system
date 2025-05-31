package org.example.lottery_system.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActivityStatusEnum {

    RUNNING(1, "活动进行中"),

    COMPLETED(2, "活动已完成");


    private final Integer code;

    private final String message;

    public static ActivityStatusEnum forName(String name) {
        for (ActivityStatusEnum activityStatusEnum : ActivityStatusEnum.values()) {
            if (activityStatusEnum.name().equalsIgnoreCase(name)) {
                return activityStatusEnum;
            }
        }
        return null;
    }

}
