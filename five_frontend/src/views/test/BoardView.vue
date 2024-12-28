<template>
  <el-container>
    <el-header>五子棋游戏</el-header>
    <el-main>
      <div class="board">
        <div v-for="(row, rowIndex) in board" :key="rowIndex" class="board-row">
          <div
            v-for="(cell, colIndex) in row"
            :key="colIndex"
            :class="getCellClass(rowIndex, colIndex)"
            @click="placeStone(rowIndex, colIndex)"
          >
            <el-tag v-if="cell === 'black'" type="success" class="stone"
              >●
            </el-tag>
            <el-tag v-else-if="cell === 'white'" type="warning" class="stone"
              >x
            </el-tag>
          </div>
        </div>
      </div>
      <div class="info">
        <p>当前玩家: {{ currentPlayer }}</p>
        <p>记录:</p>
        <pre>{{ moveHistory }}</pre>
      </div>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { ref } from "vue";

const boardSize = 15;
const board = ref(
  Array.from({ length: boardSize }, () => Array(boardSize).fill(null))
);
const currentPlayer = ref("black");
const moveHistory = ref([]);

const getCellClass = (rowIndex, colIndex) => {
  const cell = board.value[rowIndex][colIndex];
  return {
    cell: true,
    "cell-active": cell !== null,
  };
};

const placeStone = (rowIndex, colIndex) => {
  if (board.value[rowIndex][colIndex] !== null) return;

  board.value[rowIndex][colIndex] = currentPlayer.value;
  moveHistory.value.push({
    row: rowIndex,
    col: colIndex,
    player: currentPlayer.value,
  });

  currentPlayer.value = currentPlayer.value === "black" ? "white" : "black";
};
</script>

<style scoped>
.board {
  display: grid;
  grid-template-columns: repeat(15, 40px);
  grid-template-rows: repeat(15, 40px);
  gap: 2px;
}

.board-row {
  display: contents;
}

.cell {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f0f0f0;
  border: 1px solid #ccc;
  cursor: pointer;
}

.cell-active {
  background-color: #e0e0e0;
}

.stone {
  font-size: 24px;
  margin: 0;
}

.info {
  margin-top: 20px;
}

pre {
  background-color: #f5f5f5;
  padding: 10px;
  border: 1px solid #ddd;
  white-space: pre-wrap;
  word-wrap: break-word;
}
</style>
