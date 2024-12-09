// 确保 token 的存储和获取方法正确
export const setLocalToken = (token) => {
  if (token) {
    localStorage.setItem('token', token);
  }
};

export const getLocalToken = () => {
  return localStorage.getItem('token');
}; 