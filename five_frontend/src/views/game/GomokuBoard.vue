<template>
  <el-container>
    <el-header>
      <h1 class="info">
        五子棋游戏 &nbsp;&nbsp; 你的棋子: {{ playerStone }}&nbsp;&nbsp;
        当前玩家落子: {{ currentPlayer }}&nbsp;&nbsp;当前房间: {{ roomId }}
      </h1>
    </el-header>

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
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, ref } from "vue";
import {
  connect,
  sendMessage,
  setMessageCallback,
} from "@/websocket/websocket";
import { useRoute } from "vue-router";
import axiosInstance from "@/request/axiosInstance";
import { ElMessage } from "element-plus";
import { Type } from "@/enum/type";
import router from "@/router";

const route = useRoute();
//获得home页面传来的参数roomId
const roomId = route.params.roomId;

//获得home页面传来的参数type,双人对战还是ai对战
const type = route.params.type as unknown as number;
console.log("type参数 = ", type);
console.log("type = ", typeof type);

//websocket链接
const wsDestinationUrl = ref("");

//订阅主题链接
const topicUrl = ref("");

// 初始化WebSocket连接
let client: any = null;
//棋盘大小
const boardSize = 15;
//棋盘映射二维数组
const board = ref(
  Array.from({ length: boardSize }, () => Array(boardSize).fill(null))
);
//当前落子的玩家
const currentPlayer = ref("black");
//玩家自己代表的棋子
const playerStone = ref("black");
//移动记录
//const moveHistory = ref([]);

/*  moveHistory.value.push({
    row: rowIndex,
    col: colIndex,
    player: currentPlayer.value,
  });*/

//落子
const placeStone = (rowIndex, colIndex) => {
  //该点已经落子
  if (board.value[rowIndex][colIndex] !== null) return;

  //不是自己的回合
  if (currentPlayer.value != playerStone.value) {
    ElMessage.error("当前不是你的回合");
    return;
  }
  board.value[rowIndex][colIndex] = playerStone.value;

  // 你已经落子，切换玩家
  currentPlayer.value = currentPlayer.value === "black" ? "white" : "black";

  //访问连接错误
  if (wsDestinationUrl.value == null || wsDestinationUrl.value == "") {
    ElMessage.error("服务器错误");
    router.push("/");
    return;
  }
  // 发送websocket消息
  sendMessage(
    wsDestinationUrl.value,
    JSON.stringify({
      board: board.value,
      currentPlayer: playerStone.value,
    })
  );
};

//页面初始化
onMounted(() => {
  if (type === Type.NORMAL) {
    wsDestinationUrl.value = "/app/game/" + roomId;
    topicUrl.value = roomId as any;

    //获得玩家表示的棋子
    axiosInstance
      .get("/get/Identification/" + roomId)
      .then((res) => {
        console.log("获得自己棋子颜色 = ", res.data);
        if (!res.data) {
          ElMessage.error("身份信息获取失败");
          return;
        }
        //本页面玩家的身份棋子
        playerStone.value = res.data.data;
        console.log("玩家身份棋子 = ", playerStone.value);
      })
      .catch((error) => {
        console.error("Error fetching data:", error);
      });
  }
  if (type === Type.AI) {
    wsDestinationUrl.value = "/app/game/ai/" + roomId;
    topicUrl.value = "ai/" + roomId;

    //玩家的身份棋子
    playerStone.value = "black";
  }
  //websocket连接
  client = connect(topicUrl.value);
  setMessageCallback(handleMessage);
});

//页面关闭，websocket断开连接
onUnmounted(() => {
  if (client) {
    client.disconnect();
  }
});

const receiveMessage = ref<any>(null);
//后端返回消息处理
const handleMessage1 = (message: string) => {
  //获得返回的消息
  receiveMessage.value = message;
};
const handleMessage = (data: string) => {
  console.log("回调 = ", data);
  if (!data) return;
  // 游戏结束，显示获胜信息
  if (data.winner) {
    if (playerStone.value === data.currentPlayer) {
      ElMessage.success("获得胜利");
    } else {
      ElMessage.error("你失败了");
    }
    //棋盘重置
    reset();
    return;
  }
  //广播返回来的自己的消息
  if (type === Type.NORMAL) {
    if (playerStone.value === data.currentPlayer) return;
  }
  //对方的消息
  //获得返回的消息
  console.log("data.board = ", data.board);
  board.value = data.board;
  // 切换玩家
  currentPlayer.value = currentPlayer.value === "black" ? "white" : "black";
};

const reset = () => {
  ElMessage.info("3秒后将重置棋盘");
  // 创建一个定时器，在3秒后将 player 设置为 null
  setTimeout(() => {
    board.value = Array.from({ length: boardSize }, () =>
      Array(boardSize).fill(null)
    );
    if (type === Type.AI) {
      playerStone.value = "black";
    }
    currentPlayer.value = "black";
  }, 3000);
};

//css样式
const getCellClass = (rowIndex, colIndex) => {
  const cell = board.value[rowIndex][colIndex];
  return {
    cell: true,
    "cell-active": cell !== null,
  };
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
