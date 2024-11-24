package com.qjn.xiangxi_system.utils;

import lombok.Data;

@Data
public class Result {
        private int code;//200成功，其他失败
        private String message;//成功或失败
        private Long total;//总记录数
        private Object data;//数据
        public static Result result(int code, String message, Long total, Object data) {
            Result res= new Result();
            res.setCode(code);
            res.setMessage(message);
            res.setTotal(total);
            res.setData(data);
            return res;
        }
        public static Result fail(){//失败
            return result(400,"失败",0L,null);
        }
        public static Result suc(Object data) {//成功后传参
            return result(200,"成功",0L,data);
        }
        public static Result suc() {//成功不传参
             return result(200, "成功", 0L, null);
        }
        public static Result suc(Object data, Long total) {//成功后传参和total
            return result(200,"成功",total,data);
        }

}