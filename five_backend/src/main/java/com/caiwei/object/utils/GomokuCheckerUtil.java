package com.caiwei.object.utils;

import java.util.List;

/**
 * 胜利条件
 */
public class GomokuCheckerUtil {

    // 定义棋盘中每个位置可扩展的方向
    private static final int[][] DIRECTIONS = {
            {0, 1},   // 向右
            {1, 0},   // 向下
            {1, 1},   // 右下方
            {1, -1}   // 左下方
    };

    /**
     * 判断棋盘上的 param 是否满足胜利条件
     *
     * @param board 五子棋棋盘，List<List<String>>
     * @param param 检查的元素
     * @return 如果存在连续五个 param 返回 true，否则返回 false
     */
    public static boolean isWinningMove(List<List<String>> board, String param) {
        if (board == null || board.isEmpty()) return false;

        int rows = board.size();
        int cols = board.get(0).size();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (param.equals(board.get(row).get(col))) {
                    for (int[] dir : DIRECTIONS) {
                        if (checkDirection(board, param, row, col, dir[0], dir[1])) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * 检查从起点 (row, col) 开始，在给定方向 (dRow, dCol) 上是否有连续五个 param
     */
    private static boolean checkDirection(List<List<String>> board, String param,
                                          int row, int col, int dRow, int dCol) {
        int count = 1;
        int nRow = row + dRow;
        int nCol = col + dCol;

        while (nRow >= 0 && nRow < board.size() && nCol >= 0 && nCol < board.get(0).size()) {
            if (param.equals(board.get(nRow).get(nCol))) {
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
            if (param.equals(board.get(nRow).get(nCol))) {
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

}