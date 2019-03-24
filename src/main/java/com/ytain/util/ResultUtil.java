package com.ytain.util;

import com.ytain.common.vo.Result;

/**
 * @Author: 胡国栋
 * @Description:
 * @Date:Create：in 2019/3/23 21:42
 * @Modified By：
 */
public class ResultUtil<E> {
    private Result<E> result;

    public ResultUtil(){
        result=new Result<>();
        result.setSuccess(true);
        result.setMessage("success");
        result.setCode(200);
    }

    public Result<E> setData(E t){
        this.result.setResult(t);
        this.result.setCode(200);
        return this.result;
    }

    public Result<E> setSuccessMsg(String msg){
        this.result.setSuccess(true);
        this.result.setMessage(msg);
        this.result.setCode(200);
        this.result.setResult(null);
        return this.result;
    }

    public Result<E> setData(E t, String msg){
        this.result.setResult(t);
        this.result.setCode(200);
        this.result.setMessage(msg);
        return this.result;
    }

    public Result<E> setErrorMsg(String msg){
        this.result.setSuccess(false);
        this.result.setMessage(msg);
        this.result.setCode(500);
        return this.result;
    }

    public Result<E> setErrorMsg(Integer code, String msg){
        this.result.setSuccess(false);
        this.result.setMessage(msg);
        this.result.setCode(code);
        return this.result;
    }
}
