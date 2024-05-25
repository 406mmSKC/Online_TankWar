package cn.edu.ncepu.sa.Model;

import cn.edu.ncepu.sa.utils.Utils;

public class Grass extends Element{
    public Grass(int x, int y)
    {
        this.x=x;
        this.y=y;
    }
    public Grass(int x, int y, int h, int w)
    {
        this.x=x;
        this.y=y;
        this.height =h;
        this.width =w;
    }
    public boolean distanceCheck(Tank tank) {
        double a = this.width/2-Math.abs(this.x - tank.x);
        double b = this.height/2-Math.abs(this.y - tank.y);
        double dir= Utils.ppDir(tank.x, tank.y, this.x,this.y) + 90;
        if((a>0)&&(b>0))
        {
            return true;
        }
        return false;
    }
}
