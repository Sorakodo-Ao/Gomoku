package com.caiwei.object.utils;

import com.caiwei.object.entities.enums.Player;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
@Slf4j
public class ZhiPuUtil {
    @Resource
    private ClientV4 client;


    //系统预设
    private static final String SYSTEM_PROMPT = "假设你是一个五子棋高手，现在正在进行一个五子棋比赛，" +
            "5个相同棋子横纵对角线连一线即为胜利，现在是你的回合。" +
            "棋盘表示为一个二维数组,行和列分别用i,j表示，都从1开始，最大为棋盘长宽，里面的null表示空白点，可以落子。" +
            "请根据给的棋盘信息和五子棋的规则，为了赢得比赛落子，并且阻止对手赢得胜利。" +
            "直接给出按照格式\"(i,j)\"一个坐标信息即可，不用说明原因";
    private static final ChatMessage systemChatMessage =
            new ChatMessage(ChatMessageRole.SYSTEM.value(), SYSTEM_PROMPT);


    public String aiGame(List<List<String>> board, Player player) {
        System.out.println(board);
        List<ChatMessage> messages = new ArrayList<>();
/*        String userMessage = "棋盘长宽为" + board.get(0).size() + ",棋盘信息为" + board + "," +
                "其中的棋盘里中的" + player.getPlayer() + "表示你的对手已经落完的棋子,而"
                + getAiColorChess(player) + "则是你之前已经落子的点,"
                + "你现在的棋子表示为" + getAiColorChess(player) + "请落一子到棋盘";*/
        String userMessage = "棋盘信息为" + board + "，其中" + player.getPlayer() +
                "是对手放入的棋子，你的棋子为" + getAiColorChess(player) + "，请落子";
        ChatMessage userChatMessage = new ChatMessage(ChatMessageRole.USER.value(),
                userMessage);
        messages.add(systemChatMessage);
        messages.add(userChatMessage);
        //业务id
        String requestId = UUID.randomUUID().toString();

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("glm-4-flash"/*Constants.ModelChatGLM4*/)
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .requestId(requestId)
                .temperature(0.1f)
                .build();
        ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);
        log.info("ai调用 = " + invokeModelApiResp);
        return (String) invokeModelApiResp.getData().getChoices().get(0).getMessage().getContent();
    }


    public void sandToAi() {
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(),
                "作为一名营销专家，请为智谱开放平台创作一个吸引人的slogan");
        messages.add(chatMessage);
        //业务id
        String requestId = UUID.randomUUID().toString();

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("glm-4-flash"/*Constants.ModelChatGLM4*/)
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .requestId(requestId)
                .build();
        ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);
        System.out.println(invokeModelApiResp);
    }

    public static String getAiColorChess(Player player) {
        return player == Player.BLACK ? Player.WHITE.getPlayer() : Player.BLACK.getPlayer();
    }


    private static String getBoardMessage(List<List<String>> board) {
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < board.size(); i++) {
            message.append("第").append(i).append("行信息 ：[");
            for (int i1 = 0; i1 < board.get(i).size(); i1++) {
                String chess = board.get(i).get(i1);
                if (chess == null) {
                    message.append("null");
                } else {
                    message.append(chess);
                }
                if (i1 != board.get(i).size()) {
                    message.append(",");
                }
            }
            message.append("],");
        }
        return message.toString();
    }

}
