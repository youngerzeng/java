package com.demo.design.a_Simple_Factory_Pattern;

/**
 * Author: zqy
 * Date: 2020/5/3 18:24
 * Description: 具体产品类
 * 正方形绘制工具
 */
public class SquareDrawTools extends DrawTools{
    @Override
    public void draw() {
        System.out.println("绘制正方形");
    }

    @Override
    public void erase() {
        System.out.println("擦除正方形");
    }
}
