package cn.edu.ncepu.sa.Model;

import cn.edu.ncepu.sa.utils.Utils;

import java.awt.*;

public class River extends Element{
    public River(int x,int y)
    {
        this.x=x;
        this.y=y;
    }
    public River(int x,int y,int h,int w)
    {
        this.x=x;
        this.y=y;
        this.height =h;
        this.width =w;
    }
    public boolean distanceCheck(Tank tank) {
        // this,target

        double a = this.width/2-Math.abs(this.x - tank.x);
        double b = this.height/2-Math.abs(this.y - tank.y);
        double dir=Utils.ppDir(tank.x, tank.y, this.x,this.y) + 90;
        //方向冲river且到了块内
        if((a>0)&&(b>0))
        {
            tank.colletiondir= Utils.ppDir(tank.x, tank.y, this.x,this.y) + 90;
            //移出块外
            double lx = tank.x - 10 * Math.cos((tank.dir - 90) * Math.PI / 180);
            double ly = tank.y - 10 * Math.sin((tank.dir - 90) * Math.PI / 180);
            tank.x=lx;
            tank.y=ly;
            return true;
        }

        return false;

    }
}
