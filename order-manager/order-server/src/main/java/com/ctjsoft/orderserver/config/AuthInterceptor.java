/*
package com.ctjsoft.orderserver.config;

import com.alibaba.fastjson.JSON;
import com.ctjsoft.orderserver.utils.response.Result;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public abstract class AuthInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        if (handler instanceof HandlerMethod)
        {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            if (annotation != null)
            {
                if (this.isRepeatSubmit(request))
                {
                    Result result = new Result();
                    result.setSuccess(false);
                    result.setMsg("不允许重复提交，请稍后再试");
                    result.setCode(500);
                    response.setHeader("Content-type","application/json;charset=UTF-8");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(JSON.toJSONString(result));
                    return false;
                }
            }
            return true;
        }
        else
        {
            return this.preHandle(request, response, handler);
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }



    public abstract boolean isRepeatSubmit(HttpServletRequest request);


}
*/
