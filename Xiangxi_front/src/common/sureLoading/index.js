/**
 * 加载提示
 */

import { reactive } from 'vue';

export const sureLoading = reactive({
  loading: false,
  tip: '',
  show(tip) {
    if (tip) this.tip = tip;
    this.loading = true;
  },
  close() {
    this.loading = false;
    this.tip = '';
  }
});
export default {
  install(app) {
    app.config.globalProperties.$loading = sureLoading;
  }
};
