import { RouteRecordRaw } from "vue-router";
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import HomeView from "@/views/HomeView.vue";
import WebSocketComponent from "@/views/test/WebSocketComponent.vue";
import GomokuBoard from "@/views/game/GomokuBoard.vue";
import GameView from "@/views/test/GameView.vue";
import BoardView from "@/views/test/BoardView.vue";

export const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path: "/game/:roomId/:type",
    name: "game",
    component: GomokuBoard,
    props: true,
  },
  {
    path: "/chess",
    name: "chess",
    component: GameView,
  },
  {
    path: "/board",
    name: "board",
    component: BoardView,
  },
  {
    path: "/test",
    name: "test",
    component: WebSocketComponent,
  },
];
