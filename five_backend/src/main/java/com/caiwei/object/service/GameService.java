package com.caiwei.object.service;

import com.caiwei.object.entities.Message;
import com.caiwei.object.entities.enums.Player;
import com.caiwei.object.response.Result;
import com.caiwei.object.response.enums.ResultEnum;
import com.caiwei.object.utils.RedisUtil;
import com.caiwei.object.utils.ZhiPuUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class GameService {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private ZhiPuUtil zhiPuUtil;

    /**
     * 创建房间
     *
     * @return
     */
    public String generateRoom() {
        //创建房间号
        String uuid = UUID.randomUUID().toString();
        String roomId = uuid.substring(0, uuid.indexOf("-"));
        Integer memberCount = 0;
        //保存到redis key:value = 房间号:人数
        redisUtil.setCacheObject(roomId, memberCount, 15, TimeUnit.MINUTES);
        return roomId;
    }

    /**
     * 双人对战，房间人数不能超过2人
     *
     * @param roomId
     * @return
     */
    public Result joinRoom(String roomId) {
        //获取房间人数
        Integer memberCount = redisUtil.getCacheObject(roomId);
        //不存在
        if (memberCount == null) {
            return Result.failure(ResultEnum.ROOM_ID_ERROR);
        }
        if (memberCount >= 2) {
            return Result.failure(ResultEnum.ROOM_FULL);
        }
        //加入房间
        redisUtil.setCacheObject(roomId, ++memberCount);
        return Result.success(ResultEnum.OPERATE_SUCCESS);
    }

    /**
     * 确认玩家对应的棋子身份
     *
     * @param roomId
     * @return
     */
    public String generateIdentification(String roomId) {
        //return Player.BLACK.getPlayer();
        Integer memberCount = redisUtil.getCacheObject(roomId);
        log.info("当前房间{}的人数= {}", roomId, memberCount);
        return memberCount == null ? null : memberCount == 1 ?
                Player.BLACK.getPlayer() : Player.WHITE.getPlayer();
    }

    /**
     * ai落子
     *
     * @param message
     */
    public List<List<String>> aiBattle(Message message) {
        //落子的玩家
        String currentPlayer = message.getCurrentPlayer();
        //棋盘信息
        List<List<String>> board = (List<List<String>>) message.getBoard();
        String coordinate = zhiPuUtil.aiGame(board, Player.getPlayer(currentPlayer));
        String[] split = coordinate.replace("(", "")
                .replace(")", "")
                .replace(" ", "")
                .split(",");
        int i = Integer.parseInt(split[0]) - 1;
        int j = Integer.parseInt(split[1]) - 1;
        String chessColor = ZhiPuUtil.getAiColorChess(Player.getPlayer(currentPlayer));
        boolean pass = false;
        while (!pass) {
            if (board.get(i).get(j) == null) {
                pass = true;
            } else {
                //蠢ai会给出棋盘上存在棋子的位置
                Random random = new Random();
                if (i - 1 > 0 && j - 1 > 0 && i + 1 < board.size() && j + 1 < board.size()) {
                    //在ai给出的位置周围找空白处
                    int randomNumber = random.nextInt(8) + 1; // 生成0到7之间的数，然后加1变为1到8
                    switch (randomNumber) {
                        case 1:
                            if (board.get(i - 1).get(j - 1) == null) {
                                i = i - 1;
                                j = j - 1;
                                pass = true;
                            }
                            break;
                        case 2:
                            if (board.get(i - 1).get(j) == null) {
                                i = i - 1;
                                pass = true;
                            }
                            break;
                        case 3:
                            if (board.get(i - 1).get(j + 1) == null) {
                                i = i - 1;
                                j = j + 1;
                                pass = true;
                            }
                            break;
                        case 4:
                            if (board.get(i).get(j + 1) == null) {
                                j = j + 1;
                                pass = true;
                            }
                            break;
                        case 5:
                            if (board.get(i + 1).get(j + 1) == null) {
                                i = i + 1;
                                j = j + 1;
                                pass = true;
                            }
                            break;
                        case 6:
                            if (board.get(i + 1).get(j) == null) {
                                i = i + 1;
                                pass = true;
                            }
                            break;
                        case 7:
                            if (board.get(i + 1).get(j - 1) == null) {
                                i = i + 1;
                                j = j - 1;
                                pass = true;
                            }
                            break;
                        case 8:
                            if (board.get(i).get(j - 1) == null) {
                                j = j - 1;
                                pass = true;
                            }
                            break;
                    }
                } else {
                    i = random.nextInt(board.size());
                    j = random.nextInt(board.size());
                }
            }
        }
        board.get(i).set(j, chessColor);

        return board;
    }


}