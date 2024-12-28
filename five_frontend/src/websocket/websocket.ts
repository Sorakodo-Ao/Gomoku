import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";
import { ref } from "vue";

//后端websocket端口地址
const BrokerURL = "http://localhost:8080/ws";

//是否连接flag
const connected = ref(false);

let stompClient: any = null;

//连接后端
export const connect = (topic: string) => {
  const socket = new SockJS(BrokerURL);
  stompClient = Stomp.over(socket);
  stompClient.connect(
    {},
    (frame: string) => {
      connected.value = true;
      // 订阅主题
      stompClient.subscribe("/topic/" + topic, (message: { body: any }) => {
        //后端返回的消息
        console.log("Received接收: " + message.body);
        //解析json字符串
        const data = JSON.parse(message.body);
        //回调函数不为空，执行它本身
        if (messageCallback) {
          // 执行回调函数处理接收到的消息
          messageCallback(data);
        }
      });
    },
    (error: string) => {
      console.error("Error连接错误: " + error);
    }
  );
  return stompClient;
};

//发送消息，这个一定要在connect函数后执行
export const sendMessage = (destination: string, body: any) => {
  if (stompClient && stompClient.connected) {
    stompClient.send(destination, {}, body);
  } else {
    console.error("send message error, No STOMP connection available");
  }
};

//消息回调函数
let messageCallback: ((message: string) => void) | null = null;

//设置消息回调函数
export const setMessageCallback = (callback: (message: string) => void) => {
  messageCallback = callback;
};
