/**
 * token本存储
 */

import { LOCAL_TOKEN } from "./constants.js"
/**
 * 获取本地存储的token
 */
export const getLocalToken = () => {
  const token = localStorage.getItem(LOCAL_TOKEN);
  if (!token) return '';
  
  try {
    // 尝试解析存储的内容
    const parsedToken = JSON.parse(token);
    // 如果解析成功，说明存储的是对象，尝试获取 data 字段（后端返回的格式）
    if (typeof parsedToken === 'object') {
      if (parsedToken.data) {
        // 确保返回的是有效的 JWT token
        const jwtToken = parsedToken.data.trim();
        // 验证是否是有效的 JWT 格式（包含两个点，分为三部分）
        if (jwtToken.split('.').length === 3) {
          return jwtToken;
        }
        return '';
      }
    }
    // 如果是直接存储的 JWT token
    const directToken = token.trim();
    if (directToken.split('.').length === 3) {
      return directToken;
    }
    return '';
  } catch (e) {
    // 如果解析失败，检查是否是直接存储的 JWT token
    const directToken = token.trim();
    if (directToken.split('.').length === 3) {
      return directToken;
    }
    return '';
  }
};
/**
 * 将token存储于本地
 */
export const setLocalToken = (token) => {
  if (!token) return;
  
  // 添加日志
  console.log('Setting token:', token);
  
  try {
    // 如果是字符串，直接存储
    if (typeof token === 'string') {
      const cleanToken = token.trim();
      if (cleanToken) {
        localStorage.setItem(LOCAL_TOKEN, cleanToken);
        console.log('Stored token as string:', cleanToken);
      }
      return;
    }
    
    // 如果是对象，尝试获取 token 字段
    if (typeof token === 'object') {
      if (token.data) {
        const cleanToken = token.data.trim();
        if (cleanToken) {
          localStorage.setItem(LOCAL_TOKEN, cleanToken);
          console.log('Stored token from object:', cleanToken);
        }
        return;
      }
    }
    
    console.warn('Invalid token format:', token);
  } catch (e) {
    console.error('Error storing token:', e);
  }
};
/**
 * 删除本地token
 */
export const removeLocalToken = () => localStorage.removeItem(LOCAL_TOKEN);