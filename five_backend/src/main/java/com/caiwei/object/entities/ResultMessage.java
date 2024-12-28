package com.caiwei.object.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回的消息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultMessage {

    private String currentPlayer;
    private Object board;
    private Integer ready;
    private String winner;

}
