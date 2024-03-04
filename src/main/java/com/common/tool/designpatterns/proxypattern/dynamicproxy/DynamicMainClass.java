package com.common.tool.designpatterns.proxypattern.dynamicproxy;

import java.lang.reflect.Proxy;

/*动态代理*/
public class DynamicMainClass {

    public static void main(String[] args) {
        DynamicFatherClass dynamicChildAClass = new DynamicChildAClass();
        DynamicProxyClass dynamicProxyClass = new DynamicProxyClass(dynamicChildAClass);
        DynamicFatherInterface dynamicFatherInterface = (DynamicFatherInterface) Proxy.newProxyInstance(
                dynamicChildAClass.getClass().getClassLoader(),
                new Class[]{DynamicFatherInterface.class},
                dynamicProxyClass
        );
        dynamicFatherInterface.exe();
    }
}
