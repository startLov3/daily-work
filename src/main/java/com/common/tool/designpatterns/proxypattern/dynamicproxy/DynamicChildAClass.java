package com.common.tool.designpatterns.proxypattern.dynamicproxy;

public class DynamicChildAClass extends DynamicFatherClass {

    @Override
    public void exe() {
        System.out.println("DynamicChildAClass执行了");
    }
}
