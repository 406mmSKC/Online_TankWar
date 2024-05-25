package cn.edu.ncepu.sa.Model;


import java.util.HashSet;
import java.util.Random;

public class StaticMap {
    public HashSet<Element> elements = new HashSet<>();
    public HashSet<Element> bushelements = new HashSet<>();
    public HashSet<Element> riverelements = new HashSet<>();
    public Tank userTank = new Tank(600, 300, 0, 110, 0.1, TankTeam.RED.ordinal());

    public StaticMap() {
        // 水
        int riverWidth = 50;
        int riverGap = 32;
        for (int i = 200; i <= 800; i += riverGap) {
            River boundry1 = new River(200, i, riverWidth, 50);
            riverelements.add(boundry1);
            River boundry2 = new River(800, i, riverWidth, 50);
            riverelements.add(boundry2);
        }
        for (int i = 200; i <= 800; i += riverGap) {
            River boundry1 = new River(i, 200, riverWidth, 50);
            riverelements.add(boundry1);
            River boundry2 = new River(i, 800, riverWidth, 50);
            riverelements.add(boundry2);
        }

        // 连接河流
        River cornerRiver1 = new River(200, 200, 600, 50); // 左上角
        riverelements.add(cornerRiver1);
        River cornerRiver2 = new River(800, 200, 600, 50); // 右上角
        riverelements.add(cornerRiver2);
        River cornerRiver3 = new River(800, 800, 600, 50); // 右下角
        riverelements.add(cornerRiver3);
        River cornerRiver4 = new River(200, 800, 600, 50); // 左下角
        riverelements.add(cornerRiver4);

        int bushSize = 50;
        int hexagonSideLength = 300; // 适当调整以适应地图大小
        int hexagonRadius =(int) (hexagonSideLength / (2 * Math.sqrt(3)));
        int centerX = 500;
        int centerY = 500;

        for (int i = 0; i < 6; i++) {
            double angle = i * Math.PI / 3; // 每个顶点的角度
            int x = (int) (centerX + hexagonRadius * Math.cos(angle));
            int y = (int) (centerY + hexagonRadius * Math.sin(angle));
            for (int j = 0; j < hexagonSideLength / bushSize; j++) {
                int offsetX =(int)( j * bushSize * Math.cos(angle + Math.PI / 2)); // 偏移量计算考虑角度
                int offsetY =(int) (j * bushSize * Math.sin(angle + Math.PI / 2));
                Grass grass = new Grass(x + offsetX, y + offsetY, bushSize, bushSize);
                bushelements.add(grass);
            }
        }
    }

    /*public StaticMap()
    {

        //水
        for(int i=200;i<=360;i+=32)
        {
            River Boundry1=new River(200,i,50,50);
            riverelements .add( Boundry1);
            River Boundry2=new River(760,i,50,50);
            riverelements .add( Boundry2);
        }
        for(int i=600;i<=760;i+=32)
        {
            River Boundry1=new River(200,i,50,50);
            riverelements .add( Boundry1);
            River Boundry2=new River(760,i,50,50);
            riverelements .add( Boundry2);
        }
        for(int i=200;i<=360;i+=32)
        {
            River Boundry1=new River(i,200,50,50);
            riverelements .add( Boundry1);
            River Boundry2=new River(i,760,50,50);
            riverelements .add( Boundry2);
        }
        for(int i=600;i<=760;i+=32)
        {
            River Boundry1=new River(i,200,50,50);
            riverelements .add( Boundry1);
            River Boundry2=new River(i,760,50,50);
            riverelements .add( Boundry2);
        }
        //草
        for(int i=460;i<=522;i+=32)
        {
            for(int j=168;j<=808;j+=32)
            {
                Grass Boundry1=new Grass(j,i,50,50);
                bushelements.add( Boundry1);
                Grass Boundry2=new Grass(i,j,50,50);
                bushelements.add( Boundry2);
            }

        }
    }*/

    /**
     * 增加一辆敌方坦克
     *
     * @param x                   x
     * @param y                   y
     * @param dir                 方向
     * @param hp                  初始血量
     * @param hp_recovery_per_sec 修复血量
     * @param team                分组
     */
    public void AddAEnemyTank(int x, int y, double dir, double hp, double hp_recovery_per_sec, int team) {
        Tank t = new Tank(x, y, dir, hp, hp_recovery_per_sec, team);
        t.moving = true;    // 默认是移动状态
        elements.add(t);
    }

    /**
     * 构造敌方坦克，之后要依据配置/地图来构造tank
     */
    public void AddSomeEnemyTanks(int num) {
        // 使用当前时间的毫秒数作为随机种子
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        for (int i = 0; i < num; i++) {
            int x = random.nextInt(1000); // 生成0到1000的随机整数（包括0，不包括1001）
            int y = random.nextInt(1000); // 同上
            // 假设其他参数是固定的，或者你也可以随机生成它们
            double hp = 200; // 示例生命值
            double dir = 0; // 示例角度
            TankTeam team = TankTeam.BLUE; // 示例团队
            AddAEnemyTank(x, y, 0, 0, 0.1, TankTeam.BLUE.ordinal());
        }

    }
}










