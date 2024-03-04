package com.common.tool.designpatterns.singletonpattern.lazymode;

/*懒汉式*/
public class LazyClass {

    //private static LazyClass instance;

    private static volatile LazyClass instance;

    private LazyClass() {
    }

    /*public static synchronized LazyClass getInstance() {
        if (instance == null) {
            instance = new LazyClass();
        }
        return instance;
    }*/

    //双重检查锁定
    public static LazyClass getInstance() {
        if (instance == null) {
            synchronized (LazyClass.class) {
                if (instance == null) {
                    instance = new LazyClass();
                }
            }
        }
        return instance;
    }

}
