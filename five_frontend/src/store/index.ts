import { createStore } from "vuex";
//import createPersistedState from 'vuex-persistedstate';

const boardSize = 15; // 棋盘大小
const store = createStore({
  state() {
    Array.from({ length: boardSize }, () => Array(boardSize).fill(null));
  },
  mutations: {
    SET_BOARD(state, payload) {
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-ignore
      state.board = payload;
    },
  },
  actions: {
    updatePlayer({ commit }, payload) {
      commit("SET_BOARD", payload);
    },
  },
  getters: {},
  /*  plugins: [
        createPersistedState({
          key: 'playerState', // 设置存储的键名
          storage: window.localStorage, // 使用 localStorage 进行持久化
          reducer(val) {
            return {
              player: val.player // 只持久化 player 状态
            };
          }
        })
      ]*/
});

export default store;
