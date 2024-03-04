package com.common.tool.designpatterns.proxypattern.staticproxy;

public class StaticProxyClass implements StaticFatherInterface {

    private StaticFatherClass staticFatherClass;

    public StaticProxyClass(StaticFatherClass staticFatherClass) {
        this.staticFatherClass = staticFatherClass;
    }

    @Override
    public void exe() {
        System.out.println("核心逻辑处理前...");

        staticFatherClass.exe();

        System.out.println("核心逻辑处理后...");
    }
}
