package cn.edu.ncepu.sa.GameView;

import cn.edu.ncepu.sa.Control.Client_WarContol;
import cn.edu.ncepu.sa.Control.Server_WarControl;
import cn.edu.ncepu.sa.Control.WarControl;
import cn.edu.ncepu.sa.Model.*;
import cn.edu.ncepu.sa.Model.WarData;


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashSet;

public class StartView extends JFrame {
    // ... 现有代码保持不变 ...  

    // 添加两个按钮  
    private JButton startGameButton;
    private JButton loadGameButton;
    private JButton reGameButton;
    private JButton saveGameButton;
    private JButton InternetGameButton;
    private JButton joinGameButton;
    private WarData wardata;
    /**
     * 画布宽度
     */
    public int width = 400;

    /**
     * 画布高度
     */
    public int height = 400;
    public StartView() {
        // 设置 Nimbus Look and Feel 以美化界面
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        loadStartview();
        //reGameButton.setEnabled(false);
        //saveGameButton.setEnabled(false);
    }
    public StartView(WarData wardata) {
        this.wardata = wardata;
        loadStartview();
    }
    // 构造函数中初始化按钮并添加到窗体中  
    private void loadStartview() {
        // 窗体初始化
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        this.setTitle("坦克大战 V1.0");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 设置内容面板以应用边距
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        // 使用GridBagLayout来灵活布局按钮
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0); // 上下间距10，左右无间距
        gbc.gridy = 0; // 初始化行索引
        Font buttonFont = new Font("Arial", Font.BOLD, 18);
        Color buttonColor = new Color(0, 128, 96);
        Border buttonBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(buttonColor, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // 添加按钮到面板，并应用样式
        addButtonToPanel(buttonPanel, gbc, "StartGame", buttonFont, buttonColor, buttonBorder, startGameAction());
        gbc.gridy++;
        addButtonToPanel(buttonPanel, gbc, "Continue", buttonFont, buttonColor, buttonBorder, reGameAction());
        gbc.gridy++;
        addButtonToPanel(buttonPanel, gbc, "InterGame", buttonFont, buttonColor, buttonBorder, internetGameAction());
        gbc.gridy++;
        addButtonToPanel(buttonPanel, gbc, "JoinGame", buttonFont, buttonColor, buttonBorder, joinGameAction());
        gbc.gridy++;
        addButtonToPanel(buttonPanel, gbc, "Save", buttonFont, buttonColor, buttonBorder, saveGameAction());
        gbc.gridy++;
        addButtonToPanel(buttonPanel, gbc, "Load", buttonFont, buttonColor, buttonBorder, loadGameAction());
        JLabel tankGameLabel = new JLabel("TankWar", SwingConstants.CENTER);
        tankGameLabel.setFont(buttonFont.deriveFont(Font.PLAIN, 24));
        contentPane.add(tankGameLabel, BorderLayout.NORTH);

        // 将按钮面板添加到中心
        contentPane.add(buttonPanel, BorderLayout.CENTER);

        // 使窗口可见
        this.pack(); // 自动调整大小以适应内容
        this.setVisible(true);
    }

    // 辅助方法用于简化按钮添加过程
    private void addButtonToPanel(JPanel panel, GridBagConstraints gbc, String text, Font font, Color color, Border border, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(border);
        button.addActionListener(actionListener);
        panel.add(button, gbc);
    }
        // 实现开始游戏的方法
    private void startGame() {
        StaticMap map=new StaticMap();
        WarData warData = new WarData();
        warData.bushelements=new HashSet<>(map.bushelements);
        warData.riverelements=new HashSet<>(map.riverelements);
        warData.elements=new HashSet<>(map.elements);
        warData.userTank=new Tank(map.userTank);
        warData.elements.add(warData.userTank);
        // 您的游戏开始逻辑
        this.dispose();
        //另外一个JFrame
        GameView win = new GameView(warData);
        // 构造控制器组件
        WarControl warControl = new WarControl();
        // 依据用户输入刷新显示，关联View层和数据层
          warControl.StartWar(win, warData);
    }
    // 实现继续游戏的方法
    private void restartGame(WarData wardata) {
        StaticMap map=new StaticMap();
        // 您的游戏开始逻辑
        this.dispose();
        //另外一个JFrame
        GameView win = new GameView(wardata);
       // win.warData=wardata;
        win.changetimeandkiller(wardata,0);
        // 构造控制器组件
        WarControl warControl = new WarControl();
        // 依据用户输入刷新显示，关联View层和数据层
        warControl.StartWar(win, wardata);
    }
    // 实现存储游戏的方法
    private void saveGame() throws IOException {

        SaveLoadView saveLoadView = new SaveLoadView(wardata);
        saveLoadView.save("F:/yiyong/java_tankTESTideasavegame.json");

    }
    // 实现载入存档的方法  
    private void loadGame() throws IOException {
        // 您的载入存档逻辑  

        SaveLoadView saveWardata=new SaveLoadView();
        WarData warData = new WarData();
        saveWardata.load();
        wardata= saveWardata.wardata;
        this.dispose();
        //另外一个JFrame
        GameView win = new GameView(wardata);
        // 构造控制器组件
        win.changetimeandkiller(wardata,0);
        WarControl warControl = new WarControl();
        // 依据用户输入刷新显示，关联View层和数据层
        warControl.StartWar(win, wardata);
        // ... 您的代码 ...  
    }
    // 实现网络游戏的方法
    private void InternetGame() {
        StaticMap map=new StaticMap();
        WarData warData = new WarData();
        warData.bushelements=new HashSet<>(map.bushelements);
        warData.riverelements=new HashSet<>(map.riverelements);
        warData.elements=new HashSet<>();
        warData.userTank=new Tank(map.userTank);
        warData.elements.add(warData.userTank);
        // 您的游戏开始逻辑

        //另外一个JFrame
        Server_GameView win = new Server_GameView(warData);
        // 构造控制器组件
        Server_WarControl warControl = new Server_WarControl();
        warControl.StartWar(win,warData);
        joinGame();
        this.dispose();
    }
    // 实现加入游戏的方法
    private void joinGame() {
        StaticMap map=new StaticMap();
        WarData warData = new WarData();
        warData.bushelements=new HashSet<>(map.bushelements);
        warData.riverelements=new HashSet<>(map.riverelements);
        warData.elements=new HashSet<>();
        warData.userTank=new Tank(map.userTank);
        warData.userTank.team=TankTeam.BLUE.ordinal();
        warData.elements.add(warData.userTank);
        // 您的游戏开始逻辑
        this.dispose();
        //另外一个JFrame
        Client_GameView win = new Client_GameView(warData);
        // 构造控制器组件
        Client_WarContol warcontrolinternet= new Client_WarContol();
        warcontrolinternet.StartWar(win,warData);
    }
    private ActionListener startGameAction() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        };
    }
    private ActionListener reGameAction() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame(wardata);
            }
        };
    }

    private ActionListener internetGameAction() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InternetGame();
            }
        };
    }
    private ActionListener joinGameAction() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                joinGame();
            }
        };
    }

    private ActionListener saveGameAction() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    saveGame();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
    }
    private ActionListener loadGameAction() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loadGame();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
    }
}
