<template>
  <div class="gameView">
    <h1>GameView Page</h1>
    <button @click="sendMessageClick">发送消息</button>
    <div>
      {{ receiveMessage }}
    </div>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, onUnmounted, ref } from "vue";
import {
  connect,
  sendMessage,
  setMessageCallback,
} from "@/websocket/websocket";

const roomId = 1;
//后端返回的消息？？？？会不会存到store好点？？？？
const receiveMessage = ref("");
let client: any = null;
const sendMessageClick = () => {
  sendMessage("/greet/" + roomId, "hello,websocket");
  // 在适当的时机设置回调
  setMessageCallback(handleMessage);
};
onMounted(() => {
  client = connect("greet/" + roomId);
});

onUnmounted(() => {
  if (client) {
    client.disconnect();
  }
});

//后端返回消息处理
const handleMessage = (message: string) => {
  //获得返回的消息
  receiveMessage.value = message;
};
</script>
