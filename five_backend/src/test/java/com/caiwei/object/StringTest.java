package com.caiwei.object;

import com.caiwei.object.entities.enums.Player;
import com.caiwei.object.utils.ZhiPuUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class StringTest {
    //region
    @Test
    void uuidTest() {
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
        int index = uuid.toString().indexOf("-");
        System.out.println(index);
        System.out.println(uuid.toString().substring(0, uuid.toString().indexOf("-")));
    }


    // 定义棋盘中每个位置可扩展的方向
    private static final int[][] DIRECTIONS = {
            {0, 1},   // 向右
            {1, 0},   // 向下
            {1, 1},   // 右下方
            {1, -1}   // 左下方
    };

    /**
     * 判断棋盘上的 'a' 是否满足胜利条件
     *
     * @param board 五子棋棋盘，List<List<String>>
     * @return 如果存在连续五个 'a' 返回 true，否则返回 false
     */
    public static boolean isWinningMove(List<List<String>> board) {
        if (board == null || board.isEmpty()) return false;

        int rows = board.size();
        int cols = board.get(0).size();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if ("a".equals(board.get(row).get(col))) {
                    for (int[] dir : DIRECTIONS) {
                        if (checkDirection(board, row, col, dir[0], dir[1])) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * 检查从起点 (row, col) 开始，在给定方向 (dRow, dCol) 上是否有连续五个 'a'
     */
    private static boolean checkDirection(List<List<String>> board, int row, int col, int dRow, int dCol) {
        int count = 1;
        int nRow = row + dRow;
        int nCol = col + dCol;

        while (nRow >= 0 && nRow < board.size() && nCol >= 0 && nCol < board.get(0).size()) {
            if ("a".equals(board.get(nRow).get(nCol))) {
                count++;
                if (count == 5) {
                    return true;
                }
                nRow += dRow;
                nCol += dCol;
            } else {
                break;
            }
        }

        // 检查反向
        nRow = row - dRow;
        nCol = col - dCol;
        while (nRow >= 0 && nRow < board.size() && nCol >= 0 && nCol < board.get(0).size()) {
            if ("a".equals(board.get(nRow).get(nCol))) {
                count++;
                if (count == 5) {
                    return true;
                }
                nRow -= dRow;
                nCol -= dCol;
            } else {
                break;
            }
        }

        return count >= 5;
    }

    public static void main(String[] args) {
        // 示例棋盘数据，可以根据需要调整
        // 使用 ArrayList 初始化棋盘数据
        List<List<String>> board = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList("black", null, null, null, null, null)),
                new ArrayList<>(Arrays.asList(null, null, "write", null, "black", null)),
                new ArrayList<>(Arrays.asList("black", null, "write", null, null, null)),
                new ArrayList<>(Arrays.asList("black", null, null, "write", null, null)),
                new ArrayList<>(Arrays.asList("black", null, null, null, "write", null)),
                new ArrayList<>(Arrays.asList("black", null, null, null, "write", null))
        ));

        //System.out.println(isWinningMove(board)); // 输出: true 或 false
    }

    //endregion
    @Resource
    private ZhiPuUtil zhiPuUtil;

    @Test
    void ai() {
        List<List<String>> board = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList("black", null, null, null, null, null)),
                new ArrayList<>(Arrays.asList(null, null, "write", null, "black", null)),
                new ArrayList<>(Arrays.asList("black", null, "write", null, null, null)),
                new ArrayList<>(Arrays.asList("black", null, null, "write", null, null)),
                new ArrayList<>(Arrays.asList("black", null, null, null, "write", null)),
                new ArrayList<>(Arrays.asList("black", null, null, null, "write", "write"))
        ));
        System.out.println(zhiPuUtil.aiGame(board, Player.BLACK));

    }

}
