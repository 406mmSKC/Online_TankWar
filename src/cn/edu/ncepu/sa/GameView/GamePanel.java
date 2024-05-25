package cn.edu.ncepu.sa.GameView;

import cn.edu.ncepu.sa.Model.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

/**
 * 游戏画板
 */
public class GamePanel extends JPanel {
    /**
     * 数据区引用,
     * 放到参数区也可以
     */
    private WarData warData;

    /**
     * 游戏帧率
     */
    private double frameRate = 0.0;
    //
    public GamePanel() {
        setDoubleBuffered(true); // 开启双缓冲
    }

    /**
     * 初始化数据引用
     *
     * @param warData 注意是引用传递
     */
    public void setWarData(WarData warData) {
        this.warData = warData;
    }

    public void setFrameRate(double frameRate) {
        this.frameRate = frameRate;
    }

    private void draw(Element element,Graphics2D g2) {
        float alpha=1.0f;
        if (element instanceof Shot) {
            Graphics2D g = (Graphics2D) g2.create();//复制画笔
            g.translate(element.x, element.y);
            g.drawImage(ImageCache.get("shot"), -6, -6, null);
        }
        if (element instanceof River) {
            Graphics2D g = (Graphics2D) g2.create();//复制画笔
            g.translate(element.x, element.y);
            g.drawImage(ImageCache.get("river"), -16, -16, null);
        }
        if (element instanceof Grass) {
            Graphics2D g = (Graphics2D) g2.create();//复制画笔
            g.translate(element.x, element.y);
            g.drawImage(ImageCache.get("bush"), -16, -16, null);
        }
        if (element instanceof Tank){
            if(((Tank) element).hp<=0){
                return;
            }
            if(((Tank) element).seeing){
                alpha=1.0f;
            //System.out.println("画:"+x+","+y);
                 Image img1 = null;
                 Image img2 = null;
                 if (((Tank) element).team == TankTeam.RED.ordinal()) {
                    img1 = ImageCache.get("tank_red");
                    img2 = ImageCache.get("turret_red");
                }
                if (((Tank) element).team == TankTeam.BLUE.ordinal()) {
                    img1 = ImageCache.get("tank_blue");
                    img2 = ImageCache.get("turret_blue");
                }
            Graphics2D g = (Graphics2D) g2.create();//复制画笔
            g.translate(element.x, element.y);
            //绘制坦克身体
            g.rotate(Math.toRadians(((Tank) element).dir));
            g.drawImage(img1, -18, -19, null);
            g.rotate(Math.toRadians(-((Tank) element).dir));

            // 绘制血条
            g.drawRect(-22, -34, 44, 8);
            g.setColor(Color.RED);
            int whp = (int) (43.08 * (((Tank) element).hp / ((Tank) element).hpmax));
            if(((Tank) element).hp<=0 ){
                whp=0;
            }
            g.fillRect(-21, -33, whp, 7);
            g.rotate(Math.toRadians(((Tank) element).turretDir));
            g.drawImage(img2, -32, -32, null);
        }
            else if(warData.userTank==((Tank)element)){
                //如果是玩家操作的坦克，就半透明显示，其实这里有BUG，因为usertank貌似从始至终就是其中一个
                //的坦克，因为当时加入element集合的时候就是加入的先操作的那一方坦克，如果先点服务器端，那么自始至终就
                //只有服务器的坦克可以半透明，先点客户端，就会两个坦克在服务器端都半透明，很怪。
                // 你如果有更好的办法就把下面这块注释掉，我的想法是给每个人分配一个ID，但是我懒得改了。
                alpha = 0.3f;
                AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
                g2.setComposite(ac);
                //System.out.println("半透明画坦克:"+element.x+","+element.y);
                Image img1 = null;
                Image img2 = null;
                if (((Tank) element).team == TankTeam.RED.ordinal()) {
                    img1 = ImageCache.get("tank_red");
                    img2 = ImageCache.get("turret_red");
                }
                if (((Tank) element).team == TankTeam.BLUE.ordinal()) {
                    img1 = ImageCache.get("tank_blue");
                    img2 = ImageCache.get("turret_blue");
                }
                Graphics2D g = (Graphics2D) g2.create();//复制画笔
                g.translate(element.x, element.y);
                //绘制坦克身体
                g.rotate(Math.toRadians(((Tank) element).dir));
                g.drawImage(img1, -18, -19, null);
                g.rotate(Math.toRadians(-((Tank) element).dir));

                alpha=1.0f;
                // 绘制血条
                g.drawRect(-22, -34, 44, 8);
                g.setColor(Color.RED);
                int whp = (int) (43.08 * (((Tank) element).hp / ((Tank) element).hpmax));
                if(((Tank) element).hp<=0 ){
                    whp=0;
                }
                g.fillRect(-21, -33, whp, 7);
                g.rotate(Math.toRadians(((Tank) element).turretDir));
                g.drawImage(img2, -32, -32, null);

            }

        }
    }
    public void paint(Graphics g) {
        super.paint(g);//保留原来的paint，g相当于画笔
        Graphics2D g2 = (Graphics2D) g;
       /* g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());*/
        // 绘制每一个游戏元素
        if (warData != null && (warData.elements.size() > 0||warData.riverelements.size()>0||warData.bushelements.size()>0)) {
            HashSet<Element> elements = new HashSet<>();
            HashSet<Element> bushelements = new HashSet<>();
            HashSet<Element> riverelements = new HashSet<>();
            elements=new HashSet<>(warData.elements);
            riverelements=new HashSet<>(warData.riverelements);
            bushelements=new HashSet<>(warData.bushelements);

            for (Element element : riverelements) {
                draw(element,g2);//让每个节点都自我绘制
            }
            for (Element element : bushelements) {
                draw(element,g2);//让每个节点都自我绘制
            }
            for (Element element : elements) {
                draw(element,g2);//让每个节点都自我绘制
            }
        }
    }
}
