package com.caiwei.object.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 客户端传来的消息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String currentPlayer;
    private Object board;
}
