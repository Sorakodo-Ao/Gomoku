package com.caiwei.object.controller;

import com.caiwei.object.entities.Message;
import com.caiwei.object.entities.ResultMessage;
import com.caiwei.object.response.Result;
import com.caiwei.object.response.enums.ResultEnum;
import com.caiwei.object.service.GameService;
import com.caiwei.object.utils.GomokuCheckerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
public class GameController {

    @Resource
    private GameService gameService;


    /**
     * 加入房间
     *
     * @param roomId
     * @return
     */
    @PostMapping("/join/{roomId}")
    public Result joinRoom(@PathVariable("roomId") String roomId) {
        return gameService.joinRoom(roomId);
    }


    /**
     * 创建并获取房间号
     *
     * @return
     */
    @GetMapping("/createRoom")
    public Result generateRoom() {
        String roomId = gameService.generateRoom();
        return Result.success(ResultEnum.OPERATE_SUCCESS, roomId);
    }

    /**
     * 确认玩家对应的棋子身份
     *
     * @param roomId
     * @return
     */
    @GetMapping("/get/Identification/{roomId}")
    public Result getIdentification(@PathVariable("roomId") String roomId) {
        String player = gameService.generateIdentification(roomId);
        return player == null ?
                Result.failure(ResultEnum.OPERATE_FAILURE) :
                Result.success(ResultEnum.OPERATE_SUCCESS, player);
    }

    /**
     * 落子
     * @param message
     * @param roomId
     * @return
     */
    @MessageMapping("/game/{roomId}")
    @SendTo("/topic/{roomId}")
    public ResultMessage game(@RequestBody Message message,
                              @DestinationVariable String roomId) {
        log.info("前端传来的棋盘信息 = " + message);
        //落子的玩家
        String currentPlayer = message.getCurrentPlayer();
        //棋盘信息
        List<List<String>> board = (List<List<String>>) message.getBoard();
        log.info("{}玩家落子，棋盘信息{}", currentPlayer, board);
        boolean isWinner = GomokuCheckerUtil.isWinningMove(board, currentPlayer);
        ResultMessage resultMessage = new ResultMessage();
        resultMessage.setCurrentPlayer(currentPlayer);
        resultMessage.setBoard(board);
        resultMessage.setWinner(isWinner ? currentPlayer : null);
        return resultMessage;
    }

    /**
     * ai对战
     *
     * @param message
     * @param roomId
     * @return
     */
    @MessageMapping("/game/ai/{roomId}")
    @SendTo("/topic/ai/{roomId}")
    public ResultMessage aiBattle(@RequestBody Message message,
                                  @DestinationVariable String roomId) {
        log.info("前端传来的棋盘信息 = " + message);
        //落子的玩家
        String currentPlayer = message.getCurrentPlayer();
        //棋盘信息
        List<List<String>> board = (List<List<String>>) message.getBoard();
        log.info("{}玩家落子，棋盘信息{}", currentPlayer, board);
        board = gameService.aiBattle(message);
        boolean isWinner = GomokuCheckerUtil.isWinningMove(board, currentPlayer);
        ResultMessage resultMessage = new ResultMessage();
        resultMessage.setCurrentPlayer(currentPlayer);
        resultMessage.setBoard(board);
        resultMessage.setWinner(isWinner ? currentPlayer : null);
        return resultMessage;
    }
}