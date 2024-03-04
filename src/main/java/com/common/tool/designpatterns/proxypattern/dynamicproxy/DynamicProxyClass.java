package com.common.tool.designpatterns.proxypattern.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DynamicProxyClass implements InvocationHandler {

    private Object obj;

    public DynamicProxyClass(Object obj) {
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //在调用目标对象之前，记录开始时间
        long startTime = System.currentTimeMillis();
        System.out.println("开始执行" + method.getName() + "方法...");
        //调用目标对象的方法，并获取返回值
        Object result = method.invoke(obj, args);
        //在调用目标对象之后，记录结束时间和耗时
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("结束执行" + method.getName() + "方法，耗时" + duration + "毫秒");
        // 返回结果
        return result;
    }
}
