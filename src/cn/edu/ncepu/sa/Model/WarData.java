package cn.edu.ncepu.sa.Model;

import cn.edu.ncepu.sa.utils.Utils;

import java.util.HashSet;
import java.util.Iterator;

/**
 * 数据组件，除了引用传递还可以使用单例类,地图类
 */
public class WarData {

    public HashSet<Element> elements = new HashSet<>();
    public HashSet<Element> bushelements = new HashSet<>();
    public HashSet<Element> riverelements = new HashSet<>();
    public Tank userTank ;
    public double time=0 ;
   public WarData() {

    }
    public WarData(WarData wardata) {
        this.elements=new HashSet<>(wardata.elements);
        this.bushelements=new HashSet<>(wardata.bushelements);
        this.riverelements=new HashSet<>(wardata.riverelements);
        this.userTank=new Tank(wardata.userTank);
    }

}
