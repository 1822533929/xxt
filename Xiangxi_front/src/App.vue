<template>
  <div>
<!--    <LoginForm />-->
    <AdminLayout></AdminLayout>
  </div>
</template>

<script>
// import LoginForm from '@/components/LoginForm.vue'
import AdminLayout from "@/views/admin/AdminLayout.vue";

export default {
  name: 'App',
  components: {
    AdminLayout
    // LoginForm
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
