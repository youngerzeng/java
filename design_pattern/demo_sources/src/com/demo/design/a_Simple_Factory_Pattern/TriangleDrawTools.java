package com.demo.design.a_Simple_Factory_Pattern;

/**
 * Author: zqy
 * Date: 2020/5/3 18:21
 * Description: 具体产品类
 * 三角形绘制工具
 */
public class TriangleDrawTools extends DrawTools{
    @Override
    public void draw() {
        System.out.println("绘制三角形");
    }

    @Override
    public void erase() {
        System.out.println("擦除三角形");
    }
}
