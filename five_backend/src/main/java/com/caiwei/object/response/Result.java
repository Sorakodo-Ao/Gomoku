package com.caiwei.object.response;

import com.caiwei.object.response.enums.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> Result<T> success(ResultEnum resultEnum){
        return new Result(resultEnum.getCode(),resultEnum.getMessage(),null);
    }

    public static <T> Result<T> success(ResultEnum resultEnum,T data){
        return new Result(resultEnum.getCode(),resultEnum.getMessage(),data);
    }

    public static <T> Result<T> failure(ResultEnum resultEnum){
        return new Result(resultEnum.getCode(),resultEnum.getMessage(),null);
    }

    public static <T> Result<T> failure(ResultEnum resultEnum,T data){
        return new Result(resultEnum.getCode(),resultEnum.getMessage(),data);
    }
}
