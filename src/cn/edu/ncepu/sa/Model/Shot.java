package cn.edu.ncepu.sa.Model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.awt.*;
import java.lang.reflect.Type;

/**
 * 子弹类
 */
public class Shot extends Element {
    /**
     * 移动方向
     */
    public double dir;

    /**
     * 移动速度（秒）
     */
    public double speed = 220.0;

    /**
     * 子弹伤害
     */
    public double damage = 60.0;
    /**
     * 打出这个子弹的坦克引用，用来敌我识别
     */

    public Tank tank;

    public Shot() {

    }

    /**
     * 构造函数
     *
     * @param tank  发射子弹的坦克
     * @param speed 子弹速度
     */
    public Shot(Tank tank, double speed) {
        this.tank = tank;
        this.x = tank.x+10*Math.sin(tank.turretDir);
        this.y = tank.y+10*Math.cos(tank.turretDir);;
        this.dir = tank.turretDir;
        this.speed = speed;
    }

    /**
     * 更新子弹位置；每一帧都会执行
     */
    public void update(double timeFlaps) {
        //计算新坐标
        double len = speed * timeFlaps;
        this.move(dir, len);

        //超出范围的无效子弹
        if (x < -100 || x > 1000 || y < -100 || y > 1000) {
            this.tank.cannonball++;
            this.destroy();
        }
    }

//    @Override
//    public void draw(Graphics2D g2) {
//        Graphics2D g = (Graphics2D) g2.create();//复制画笔
//        g.translate(x, y);
//        g.drawImage(ImageCache.get("shot"), -6, -6, null);
//    }
}
