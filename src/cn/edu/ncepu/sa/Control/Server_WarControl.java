package cn.edu.ncepu.sa.Control;

import cn.edu.ncepu.sa.GameView.Server_GameView;
import cn.edu.ncepu.sa.GameView.StartView;
import cn.edu.ncepu.sa.Model.Shot;
import cn.edu.ncepu.sa.Model.Tank;
import cn.edu.ncepu.sa.Model.WarData;
import cn.edu.ncepu.sa.utils.Utils;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.jms.JMSException;
import javax.jms.TextMessage;

public class Server_WarControl extends Thread{
    /**
     * 显示组件引用
     */
    Server_GameView win;
    Server c2Master;
    /**
     * 数据组件引用
     */
    WarData warData;
    private volatile boolean isRunning = true; // 使用volatile确保跨线程可见性
    private volatile boolean Run= true; // 使用volatile确保跨线程可见性
    private final ExecutorService executorService = Executors.newFixedThreadPool(5); // 假设有5个线程
    /**
     * 默认构造函数
     */
    public Server_WarControl() {

    }
    /**
     * 初始化控制器
     *
     * @param win     显示组件引用
     * @param warData 数据组件引用
     */
    public void StartWar(Server_GameView win, WarData warData) {

        c2Master = new Server("127.0.0.1", 61616, "admin", "admin",warData);
        c2Master.StartWork();
        this.win = win;
        this.warData = warData;
        Tank tank = warData.userTank;
        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (warData.userTank.Destroyed) {
                    return;
                }
//                System.out.println(e.getX()+","+e.getY());
                double x = e.getX() - 9;
                double y = e.getY() - 38;
                tank.turretDir = Utils.ppDir(tank.x, tank.y, x, y) + 90;
            }
            @Override
            public void mousePressed(MouseEvent e) {
                if (tank.Destroyed) {
                    tank.moving = false;
                    return;
                }
                if (e.getButton() == MouseEvent.BUTTON3&&tank.cannonball>0) { // 鼠标右键被按下
                    tank.cannonball--;
                    Shot shot = new Shot(tank, 500);
                    warData.elements.add(shot);
                } else if (e.getButton() == MouseEvent.BUTTON1) { // 鼠标左键被按下
                    System.out.println("Hello world!");
                    // 处理鼠标右键按下的逻辑，比如显示一个菜单或执行其他操作
                    //如果在障碍物边上
                    if(tank.colletion) {
                        if (tank.ColletionCheck(tank.turretDir)) {
                            tank.dir = tank.turretDir;
                            double x = e.getX() - 9;
                            double y = e.getY() - 38;
                            tank.toLen = tank.distance(x, y);
                            tank.moving = true;
                        }
                    }
                    else {
                        tank.dir =  tank.turretDir;
                        double x = e.getX() - 9;
                        double y = e.getY() - 38;
                        tank.toLen = tank.distance(x,y);
                        tank.moving = true;
                    }
                }
            }
        };
        win.addMouseListener(adapter); //点击事件
        win.addMouseMotionListener(adapter); //移动事件
        win.addMouseWheelListener(adapter); //滚轮事件

        win.addKeyListener(new KeyAdapter() {
            int release = 0;
            @Override //键盘按下
            public void keyPressed(KeyEvent e) {
//                System.out.println(e.getKeyChar());
                //抬起判断
                if (tank.Destroyed) {
                    tank.moving = false;
                    return;
                }
                switch (e.getKeyChar()) {
                    case ' ':
                        Shot shot = new Shot(tank, 500);
                        //有子弹且按一次打一发
                        if (release == 0 && tank.cannonball > 0) {
                            tank.cannonball--;
                            warData.elements.add(shot);
                            release = 1;
                        }
                        break;
                }


            }

            @Override //键盘抬起
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case ' ':
                        release=0;
                        break;
                }
            }
        });

        this.start();
    }

    private void changeIsRunning()
    {
        isRunning=!isRunning;
    }
    private void stopGame(Server_GameView win, WarData warData)
    {
        Run=false;
        win.dispose();
        StartView startview=new StartView(warData);
    }

    public void run() {
        super.run();

        long lastUpdate = System.currentTimeMillis();//当前系统时间
        int fps = 60;//理论帧数
        while (Run) {
            long interval = 1000 / fps;//理论间隔
            long curr = System.currentTimeMillis();
            long _time = curr - lastUpdate;
            if (_time < interval||!isRunning) {
                // 不到刷新时间，休眠
                try {
                    Thread.sleep(1);
                    if(!isRunning)
                        lastUpdate = curr;
                } catch (Exception e) {

                }

            } else {
                // 更新游戏状态

                lastUpdate = curr;

                // 流逝时间
                float dt = _time * 0.001f;
                warData.time+=dt;
                executorService.submit(() -> {

                    SwingUtilities.invokeLater(() -> {


                        win.updatePositions(warData, dt);
                        win.CollisionDetectionShotTank(warData);
                        win.CollisionDetectionRiverTank(warData);
                        win.CollisionDetectionBushTank(warData);
                        win.updateDataSet(warData);
                        win.update(dt);
                    });
                });

            }

        }
    }
}
