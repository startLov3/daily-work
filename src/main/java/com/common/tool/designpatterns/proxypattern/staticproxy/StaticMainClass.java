package com.common.tool.designpatterns.proxypattern.staticproxy;

/*静态代理*/
public class StaticMainClass {

    public static void main(String[] args) {
        StaticProxyClass staticProxyClass1 = new StaticProxyClass(new StaticChildAClass());
        staticProxyClass1.exe();

        StaticProxyClass staticProxyClass2 = new StaticProxyClass(new StaticChildBClass());
        staticProxyClass2.exe();

    }
}
