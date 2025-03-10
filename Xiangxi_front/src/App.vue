<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script setup>
</script>

<style>
html, body {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
  box-sizing: border-box;
}

* {
  box-sizing: inherit;
}

body {
  display: flex;
  flex-direction: column;
}

#app {
  width: 100%;
  height: 100%;
}

</style>
