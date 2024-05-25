package cn.edu.ncepu.sa.Model;

import java.awt.*;

/**
 * 坦克类
 */
public class Tank extends Element {
    /**
     * 坦克方向
     */
    public double dir = 90;

    /**
     * 炮筒方向
     */
    public double turretDir;
    /**
     * 杀敌数
     */
    public int Killer=0;

    /**
     * 是否在移动
     */
    public boolean moving = false;

    /**
     * 是否碰撞障碍物*/
    public boolean colletion = false;
    /**
     * 碰撞夹角*/
    public double  colletiondir;
    /**
     * 移动目标距离
     */
    public double toLen=0x7f7f7f;
    /**
     * 可视-草丛*/
    public boolean seeing=true;


    /**
     * 移动步数
     */
    public long moveSteps = 0;

    /**
     * 每秒移动速度,注意要比子弹慢一些
     */
    public double speed = 200;

    /**
     * 生命数，装甲,炮弹数目
     */
    public double hp = 600;
    public double hpmax = 200;
    public int cannonball=3;
    /**
     * 每秒回复生命
     */
    public double hp_recovery_per_sec = 0.1;

    /**
     * 队伍，1红，2蓝
     */
    public int team = 1;


    public Tank() {

    }
    public Tank(Tank t) {
        this.x = t.x;
        this.y = t.y;

        this.dir = t.dir;
        this.speed = t.speed;
        this.hp = t.hp;
        this.hp_recovery_per_sec = t.hp_recovery_per_sec;
        this.team = t.team;
    }
    /**
     * 构造坦克
     *
     * @param x                   x坐标
     * @param y                   y坐标
     * @param dir                 方向
     * @param hp                  初始血量
     * @param hp_recovery_per_sec 每秒恢复血量
     * @param team                组别
     */
    public Tank(int x, int y, double dir, double hp, double hp_recovery_per_sec, int team) {
        this.x = x;
        this.y = y;

        this.dir = dir;
        this.speed = speed;
        this.hp = hp;
        this.hp_recovery_per_sec = hp_recovery_per_sec;
        this.team = team;
    }

    /**
     * 此坦克受到伤害
     */
    public void damage(double val) {
        this.hp -= val;
        if (this.hp <= 0) {

            System.out.println("坦克销毁");
            this.destroy();
        }
    }

    /**
     * 更新坦克位置
     *
     * @param timeFlaps 流逝时间间隔
     */
    public void update(double timeFlaps) {
        // 若已死亡，则不再动作
        if (Destroyed) {
            return;
        }

        //生命回复
        recoverLife();

        //更新坦克位置
        if (moving&&toLen>0&&!colletion) {
            double len = speed * timeFlaps;

            //判断预计移动位置是否达到
            if(len-toLen>0)
            {
                len=toLen;
            }
            toLen-=len;
            moveSteps++;
            this.move(dir, len);
        }
    }
    public void move(double dir, double len) {

       double lx = x + len * Math.cos((dir - 90) * Math.PI / 180);
       double ly = y + len * Math.sin((dir - 90) * Math.PI / 180);
        if(lx<0||lx>1000||ly<0||ly>1000)
        {
            return;
        }
        x=lx;
        y=ly;

    }
    /**
     * 定时自动回血
     */
    public void recoverLife() {
        hp += hp_recovery_per_sec;
        if (hp > hpmax) {
            hp = hpmax;
        }
    }
    /**
     * 障碍物碰撞更新,可以移动return true,turretDir当前方向，colletiondir与碰撞的障碍物夹角
     */
    public boolean ColletionCheck(double turretDir) {
         if(90<Math.abs(turretDir-colletiondir))
         {

             colletion=false;
             return true;
         }
         return false;
    }


//    @Override
//    public void draw(Graphics2D g2) {
//        //System.out.println("画:"+x+","+y);
//        Image img1 = null;
//        Image img2 = null;
//        if (team == TankTeam.RED.ordinal()) {
//            img1 = ImageCache.get("tank_red");
//            img2 = ImageCache.get("turret_red");
//        }
//        if (team == TankTeam.BLUE.ordinal()) {
//            img1 = ImageCache.get("tank_blue");
//            img2 = ImageCache.get("turret_blue");
//        }
//        Graphics2D g = (Graphics2D) g2.create();//复制画笔
//        g.translate(x, y);
//        //绘制坦克身体
//        g.rotate(Math.toRadians(dir));
//        g.drawImage(img1, -18, -19, null);
//        g.rotate(Math.toRadians(-dir));
//
//        // 绘制血条
//        g.drawRect(-22, -34, 44, 8);
//        g.setColor(Color.RED);
//        int whp = (int) (43.08 * (hp / hpmax));
//        g.fillRect(-21, -33, whp, 7);
//        g.rotate(Math.toRadians(this.turretDir));
//        g.drawImage(img2, -32, -32, null);
//    }
}
