package cn.edu.ncepu.sa.GameView;

import cn.edu.ncepu.sa.Model.*;

import javax.swing.*;
import java.awt.*;


public class Client_GameView extends JFrame{
    /**
     * 数据区引用
     */
    WarData warData;

    /**
     * 游戏画板
     */
    GamePanel panel = new GamePanel();
    //显示游戏时间与杀敌数
    //JLabel statustimeLabel = new JLabel("游戏时间: 00:00");
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
    public Client_GameView(WarData warData) {
        this.warData = warData;
        panel.setWarData(warData);
        Font font1 = new Font("Serif", Font.ITALIC, 24);
        Font font2 = new Font("Serif", Font.ITALIC, 24);
        //statustimeLabel.setFont(font1);
        //statuskillLabel.setFont(font2);
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        //statusPanel.add(statustimeLabel); // 将标签添加到状态面板的中心
        //statusPanel.add(statuskillLabel); // 将标签添加到状态面板的中心
        // 窗体初始化和
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        //this.setResizable(false);
        this.setTitle("坦克大战 客户端");
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 使用BorderLayout将游戏面板和状态面板分开
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER); // 游戏面板放在中间
        this.add(statusPanel, BorderLayout.SOUTH); // 状态面板放在右侧

        this.setVisible(true);
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
      /*  statustimeLabel.setText("游戏时间: " + gameTime);
        statuskillLabel.setText( " 杀敌数: " + warData.userTank.Killer);*/
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
