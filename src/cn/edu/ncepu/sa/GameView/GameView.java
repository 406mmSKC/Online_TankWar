package cn.edu.ncepu.sa.GameView;

import cn.edu.ncepu.sa.Model.*;
import cn.edu.ncepu.sa.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

/**
 * 游戏窗口
 */
public class GameView extends JFrame {
    /**
     * 数据区引用
     */
    WarData warData;

    /**
     * 游戏画板
     */
    GamePanel panel = new GamePanel();
    //显示游戏时间与杀敌数
   // JLabel statustimeLabel = new JLabel("游戏时间: 00:00");
   // JLabel statuskillLabel = new JLabel("杀敌数: 0");
    /**
     * 画布宽度
     */
    public int width = 1000;

    /**
     * 画布高度
     */
    public int height = 1000;

    /**
     * 初始化显示组件
     *
     * @param warData 数据区引用
     */
    public GameView(WarData warData) {
        this.warData = warData;
        panel.setWarData(warData);
        //Font font1 = new Font("Serif", Font.ITALIC, 24);
        //Font font2 = new Font("Serif", Font.ITALIC, 24);
        //statustimeLabel.setFont(font1);
       // statuskillLabel.setFont(font2);
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
       // statusPanel.add(statustimeLabel); // 将标签添加到状态面板的中心
      //  statusPanel.add(statuskillLabel); // 将标签添加到状态面板的中心
        // 窗体初始化和
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        //this.setResizable(false);
        this.setTitle("坦克大战 V1.0");
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 使用BorderLayout将游戏面板和状态面板分开
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER); // 游戏面板放在中间
        this.add(statusPanel, BorderLayout.SOUTH); // 状态面板放在右侧

        this.setVisible(true);


    }

    // 私有数据区


    /**
     * 敌方坦克动起来，请尝试修改为每个坦克独立线程控制，自主活动
     */
    //以下代码最好放在别的层，而不是view层
    public void runEnemyTank(WarData warData,int viewWidth, int viewHeight) {
        HashSet<Element> elements = warData.elements;
       Tank userTank= warData.userTank;
        if (userTank.Destroyed) {
            return;
        }

        for (Element elemnet : elements) {
            // 找坦克
            if (elemnet instanceof Tank) {
                // 找敌方坦克
                if (((Tank) elemnet).team == TankTeam.BLUE.ordinal()) {
                    Tank t = (Tank) elemnet;
                    t.toLen=0X7f7f7f7f;
                    // 防止跑出地图
                    if (t.x < 0) {
                        t.dir = Directions.RIGHT.getAngleValue();
                    }
                    if (t.y < 0) {
                        t.dir = Directions.DOWN.getAngleValue();
                    }
                    if (t.x >= viewWidth) {
                        t.dir = Directions.LEFT.getAngleValue();
                    }
                    if (t.y >= viewHeight) {
                        t.dir = Directions.UP.getAngleValue();
                    }

                    // 运动几步随机开炮，50应该设置为参数或者常量
                    if (t.moveSteps > 50||t.colletion) {
                        // 方向随机
                        double random = Math.random() * 360;

                       while(t.colletion) {

                            if (!t.ColletionCheck(random)) {
                                random = Math.random() * 360;
                             }
                       }

                        t.dir = random;
                        t.turretDir = random;
                        t.moving = true;
                        t.moveSteps = 0;

                        // 如果我方坦克进入射程，800应该设置为常量，且有炮弹且可见
                        if (t.distance(userTank) < 800&&t.cannonball>0&& userTank.seeing&&t.moveSteps > 50) {
                            //打一发
                            t.cannonball--;
                            // 自动瞄准
                            t.turretDir = Utils.ppDir(t.x, t.y, userTank.x, userTank.y) + 90;

                            // 开炮
                            Shot shot = new Shot(t, 200);
                            elements.add(shot);
                            return;
                        }
                        else if(t.moveSteps > 50){
                            Shot shot = new Shot(t, 200);
                            elements.add(shot);
                            return;
                        }
                    }
                }
            }
        }
    }
    public void addEnemyTank(WarData warData,int viewWidth, int viewHeight) {
        HashSet<Element> elements = warData.elements;
        Tank userTank = warData.userTank;
        if (userTank.Destroyed) {
            return;
        }
        int num = 0;
        for (Element elemnet : elements) {
            // 找坦克
            if (elemnet instanceof Tank) {
                // 找敌方坦克
                if (((Tank) elemnet).team == TankTeam.BLUE.ordinal()) {
                    num++;
                }
            }
        }
        if (num < 3) {
            long seed = System.currentTimeMillis();
            Random random = new Random(seed);
            //如果少于三辆tank，生成0-3个
            int sum = random.nextInt(3);
            for (int i = 0; i < sum; i++) {
                int x = random.nextInt(1000); // 生成0到1000的随机整数（包括0，不包括1001）
                int y = random.nextInt(1000); // 同上
                // 假设其他参数是固定的，或者你也可以随机生成它们
                double hp = 200; // 示例生命值
                double dir = 0; // 示例角度
                TankTeam team = TankTeam.BLUE; // 示例团队
                Tank t = new Tank(x, y, 0, 200, 0.1, TankTeam.BLUE.ordinal());
                t.moving = true;    // 默认是移动状态
                warData.elements.add(t);
            }
        }
    }
    /**
     * 更新坦克的位置
     *
     * @param timeFlaps 运行时间间隔
     */
    public void updatePositions(WarData warData,double timeFlaps) {
        HashSet<Element> elements = warData.elements;
        // 所有元素依据流逝时间更新状态
        for (Element elemnet : elements) {
            elemnet.update(timeFlaps);
        }
    }

    /**
     * 碰撞检测ShotTank
     */
    public void CollisionDetectionShotTank(WarData warData) {
        HashSet<Element> elements = warData.elements;
        //遍历每一个子弹
        for (Element shot : elements) {
            if (shot instanceof Shot) {
                // 寻找每一辆坦克
                for (Element tank : elements) {
                    //进行敌我识别
                    if ((tank instanceof Tank) && (tank != ((Shot) shot).tank)) {
                        if (shot.distance(tank) < 20) {
                            ((Tank) tank).damage(((Shot) shot).damage); //使坦克受到伤害
                            if(tank.Destroyed) {
                                ((Shot)shot).tank.Killer++;
                            }
                            ((Shot) shot).tank.cannonball++;
                            shot.destroy(); //销毁当前子弹

                        }
                    }
                }
            }
        }
    }
    public void changetimeandkiller(WarData warData,double time) {
        warData.time+=time;
        // 将double类型的秒数转换为整数秒数（丢弃小数部分）
        int totalSeconds = (int) Math.floor(warData.time);

        // 计算分钟和秒
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        // 如果秒数只有一位，前面补0
        String formattedSeconds = String.format("%02d", seconds);

        // 格式化游戏时间为"MM:SS"格式
        String gameTime = minutes + ":" + formattedSeconds;

        // 更新statusLabel的文本
        //statustimeLabel.setText("游戏时间: " + gameTime);
        //statuskillLabel.setText( " 杀敌数: " + warData.userTank.Killer);
    }

    /**
     * 碰撞检测RiverTank
     */
    public void CollisionDetectionRiverTank(WarData warData) {
        HashSet<Element> elements = warData.elements;
        HashSet<Element> riverelements = warData.riverelements;
        for (Element river : riverelements) {
            if (river instanceof River) {
                // 寻找每一辆坦克
                for (Element tank : elements) {
                    //进行敌我识别
                    if ((tank instanceof Tank) ) {
                        if (((River) river).distanceCheck((Tank) tank)&&!((Tank) tank).colletion) {
                            //碰撞不能移动
                           ((Tank) tank).colletion=true;
                        }
                    }
                }
            }
        }
    }
    /**
     * 碰撞检测BushTank
     */
    public void CollisionDetectionBushTank(WarData warData) {
        HashSet<Element> elements = warData.elements;
        HashSet<Element> Bushelements = warData.bushelements;
        for (Element tank : elements) {
            if (tank instanceof Tank) {
                boolean flag=false;
                // 寻找每一辆坦克
                for (Element bush : Bushelements) {
                    //进行碰撞检测
                    if ((bush instanceof Grass) ) {

                        if (((Grass) bush).distanceCheck((Tank) tank)) {
                            //不可视
                            ((Tank) tank).seeing=false;

                            flag=true;
                            break;

                        }
                    }
                }
                if(!flag)
                {
                    ((Tank) tank).seeing=true;
                }
            }
        }
    }
    /**
     * 依据元素的状态，处理是否还保留在数据区中
     */

   synchronized public void updateDataSet(WarData warData) {
        HashSet<Element> elements = warData.elements;
        Tank userTank= warData.userTank;
        Iterator<Element> it = elements.iterator();
        while (it.hasNext()) {
            Element tmp = it.next();
            if (tmp.Destroyed) {
                if (tmp != userTank) {
                    it.remove();
                }
            }
        }
    }


    private int _frames = 0;//累计帧数，自主选择是否显示
    private float _dt = 0;//累计时长
    private float _frameRate = 0.0f;//帧率

    /**
     * 更新画板
     *
     * @param timeFlaps 流逝时间
     */
    public void update(double timeFlaps) {

        _frames++;
        _dt += timeFlaps;
        if (_dt >= 1.0f) {
            _frameRate = _frames / _dt;
            _frames = 0;
            _dt = 0f;
        }
        panel.setFrameRate(_frameRate);
        panel.repaint();
    }
}
