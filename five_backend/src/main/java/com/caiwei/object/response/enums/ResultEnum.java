package com.caiwei.object.response.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultEnum {

    ROOM_FULL(201, "房间已满"),

    ROOM_ID_ERROR(501, "房间号错误"),

    OPERATE_SUCCESS(200, "操作成功"),

    OPERATE_FAILURE(500, "操作失败");

    private Integer code;
    private String message;
}
