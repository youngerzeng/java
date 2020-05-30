package com.demo.design.a_Simple_Factory_Pattern;

/**
 * Author: zqy
 * Date: 2020/5/3 18:16
 * Description: 抽象产品类
 * 绘制工具
 */
abstract class DrawTools {
    private String id;
    /**
     * 绘制
     */
    abstract void draw();

    /**
     * 擦除
     */
    abstract void erase();
}
