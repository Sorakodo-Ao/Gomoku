package com.caiwei.object.controller;

import com.caiwei.object.entities.Message;
import com.caiwei.object.entities.ResultMessage;
import com.caiwei.object.utils.ZhiPuUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class HealthController {

    @Resource
    private ZhiPuUtil zhiPuUtil;


    @GetMapping("/health")
    public String health() {
        return "hello,springboot";
    }

    @GetMapping("/zhipu")
    public String ai() {
        zhiPuUtil.sandToAi();
        return "ai";
    }

    /**
     * 对方已加入游戏
     *
     * @param message
     * @return
     */
    @MessageMapping("/greet/{roomId}")
    @SendTo("/topic/greet/{roomId}")
    public ResultMessage greeting(@RequestBody Message message) {
        ResultMessage resultMessage = new ResultMessage();
        resultMessage.setCurrentPlayer(message.getCurrentPlayer());
        resultMessage.setReady(200);
        return resultMessage;
    }


}
