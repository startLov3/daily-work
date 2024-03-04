package com.common.tool.designpatterns.singletonpattern.hungrymode;

/*饿汉式*/
public class HungryClass {

    private static HungryClass instance = new HungryClass();

    private HungryClass() {
    }

    public static HungryClass getInstance() {
        return instance;
    }

}
