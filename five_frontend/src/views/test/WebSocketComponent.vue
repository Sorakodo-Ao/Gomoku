<template>
  <div>
    <h1>WebSocket 示例</h1>
    <button @click="sendMessage">发送消息</button>
    <div>
      {{ messages }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { onBeforeUnmount, onMounted, ref } from "vue";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";

const messages = ref();
let stompClient: any = null;

const connect = () => {
  const socket = new SockJS("http://localhost:8080/ws");
  stompClient = Stomp.over(socket);
  stompClient.connect(
    {},
    (frame: string) => {
      console.log("Connected: " + frame);
      stompClient.subscribe("/topic/greetings", (message: { body: string }) => {
        console.log("Received: " + message.body);
        messages.value = message.body;
        //messages.value.push(message.body);
      });
    },
    (error: string) => {
      console.error("Error: " + error);
    }
  );
};

const sendMessage = () => {
  if (stompClient && stompClient.connected) {
    stompClient.send("/app/hello", {}, "hello, world");
  } else {
    console.error("No STOMP connection available");
  }
};

onMounted(() => {
  connect();
});

onBeforeUnmount(() => {
  if (stompClient) {
    stompClient.disconnect();
  }
});
</script>

<style scoped>
/* 添加你的样式 */
</style>
