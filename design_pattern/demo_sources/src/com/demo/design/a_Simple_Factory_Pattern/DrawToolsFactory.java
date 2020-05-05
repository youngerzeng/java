package com.demo.design.a_Simple_Factory_Pattern;

/**
 * Author: zqy
 * Date: 2020/5/3 18:25
 * Description: 工厂类
 * 绘制工具工厂类
 */
public class DrawToolsFactory {

    /**
     * 根据参数获取不同的产品
     * @param args
     * @return
     */
    public static DrawTools getDrawTools(String args){
        DrawTools drawTools = null;
        if(args.equalsIgnoreCase("triangle")){
            drawTools = new TriangleDrawTools();
        }else if(args.equalsIgnoreCase("square")){
            drawTools = new SquareDrawTools();
        }
        return drawTools;
    }
}
