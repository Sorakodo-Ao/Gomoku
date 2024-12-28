<template>
  <div id="home">
    <div class="centered-div">
      <h1>欢迎来到五子棋游戏</h1>
      <el-tabs
        v-model="activeName"
        type="card"
        class="demo-tabs"
        stretch="true"
        @tab-click="handleClick"
      >
        <el-tab-pane label="创建游戏" name="first">
          <el-button
            :type="buttonType as any"
            size="large"
            @click="buttonClick"
            style="margin-top: 10px"
            >{{ buttonContent }}
          </el-button>
          <h2 v-if="showTitle">房间号: {{ roomId }}</h2>
        </el-tab-pane>
        <el-tab-pane label="加入游戏" name="second">
          <span>
            房间号:
            <el-input
              v-model="roomId"
              style="width: 240px"
              placeholder="请输入房间号"
            />
          </span>
          <el-button
            type="success"
            size="large"
            @click="joinGameClick"
            style="margin-top: 10px"
            >点击加入游戏
          </el-button>
        </el-tab-pane>
        <el-tab-pane label="AI对战" name="third">
          <el-button
            type="success"
            size="large"
            @click="BattleAiClick"
            style="margin-top: 10px"
            >点击对战AI
          </el-button>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onUnmounted, ref } from "vue";
import type { TabsPaneContext } from "element-plus";
import { ElMessage } from "element-plus";
import axiosInstance from "@/request/axiosInstance";
import router from "@/router";
import { Type } from "@/enum/type";

//按钮类型
const buttonType = ref("primary");

//展示房间号
const showTitle = ref(false);

//定位标签页
const activeName = ref("first");

//房间号
const roomId = ref(null);

//按钮内容
const buttonContent = ref("点击创建游戏");

const handleClick = (tab: TabsPaneContext, event: Event) => {
  console.log(tab, event);
  console.log(activeName.value);
};

//创建游戏房间
const buttonClick = () => {
  //创建游戏房间
  if (!showTitle.value) {
    createRoom();
    showTitle.value = true;
    buttonType.value = "success";
    buttonContent.value = "点击加入游戏";
  } else {
    //加入游戏房间
    joinGameClick();
  }
};
//加入游戏
const joinGameClick = () => {
  console.log("加入游戏");
  axiosInstance
    .post("/join/" + roomId.value)
    .then((res) => {
      console.log("加入游戏 = ", res.data);
      if (res.data.code == 200) {
        ElMessage.success("加入游戏成功");
        //跳转
        router.push({
          name: "game",
          params: {
            roomId: roomId.value,
            type: Type.NORMAL,
          },
        });
      } else {
        ElMessage.error(res.data.message);
      }
    })
    .catch((err) => {
      console.log("加入游戏失败 = ", err);
      ElMessage.error("加入游戏失败");
    });
};

//ai对战
const BattleAiClick = () => {
  console.log("AI对战");
  createRoom();
  ElMessage.success("即将开始AI对战");
  setTimeout(() => {
    router.push({
      name: "game",
      params: { roomId: roomId.value, type: Type.AI },
    });
  }, 1000);
};

//创建房间
const createRoom = () => {
  //发送请求创建房间
  axiosInstance
    .get("/createRoom")
    .then((res) => {
      console.log("创建房间 = ", res.data);
      if (res.data.code == 200) {
        console.log("房间号 = ", res.data.data);
        roomId.value = res.data.data;
        ElMessage.success("创建房间成功");
      }
    })
    .catch((err) => {
      ElMessage.error("创建房间失败");
    });
};

onUnmounted(() => {
  console.log("页面关闭");
  //还原
  buttonType.value = "primary";
  showTitle.value = false;
  activeName.value = "first";
  roomId.value = null;
  buttonContent.value = "点击创建游戏";
});
</script>

<style>
#home {
  width: 100vw;
  height: 100vh;
  background-image: url("@/assets/backgroud.png"); /* 替换为你的图片路径 */
  background-size: cover;
  background-position: center;
  display: flex;
  justify-content: center;
  align-items: center;
}

.centered-div {
  background-color: rgba(255, 255, 255, 0.8); /* 半透明白色背景 */
  padding: 20px;
  border-radius: 10px;
  text-align: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  width: 400px;
  height: 500px;
}
</style>
