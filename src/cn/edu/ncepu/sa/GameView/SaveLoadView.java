package cn.edu.ncepu.sa.GameView;

import cn.edu.ncepu.sa.Model.WarData;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.alibaba.fastjson.JSON;

public class SaveLoadView {
    // 例如，如果Element类没有无参构造函数或包含不能序列化的字段，你可能需要添加@JsonIgnore或其他注解
    //@JsonIgnore
//    public HashSet<Element> elements = new HashSet<>();
//   // @JsonIgnore
//    public HashSet<Element> bushelements = new HashSet<>();
//   // @JsonIgnore
//    public HashSet<Element> riverelements = new HashSet<>();
//  //  @JsonIgnore
//    public Tank userTank ;
    public WarData wardata;
    public SaveLoadView()
    {

    }
    public SaveLoadView(WarData wardata)
    {
//       bushelements=wardata.bushelements;
//       riverelements=wardata.riverelements;
//       elements=wardata.elements;
//       userTank=wardata.userTank;
        this.wardata=wardata;
    }
    public void save(String fileName) throws IOException {
        // 将对象序列化为JSON字符串
        String jsonString = JSON.toJSONString(this, SerializerFeature.WriteClassName);
        System.out.println(jsonString);
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(jsonString);
        }
    }
    public static SaveLoadView loadFromFile(String fileName) throws IOException {
        // 从文件中读取JSON字符串
        String jsonString;
        try (FileReader fileReader = new FileReader(fileName)) {
            int ch;
            StringBuilder sb = new StringBuilder();
            while ((ch = fileReader.read()) != -1) {
                sb.append((char) ch);
            }
            jsonString = sb.toString();
        }

        // 使用包含类名的特性来反序列化JSON字符串
        SaveLoadView loadedView = JSON.parseObject(jsonString, SaveLoadView.class);

        // 如果需要，可以在这里处理反序列化后的对象，比如检查字段的有效性等
        return loadedView;
    }
    public void load() throws IOException {
//        this.elements=SaveLoadView.loadFromFile("F:/yiyong/java_tankTESTideasavegame.json").elements;
//        System.out.println("载入存档！");
//        this.bushelements=SaveLoadView.loadFromFile("F:/yiyong/java_tankTESTideasavegame.json").bushelements;
//        this.riverelements=SaveLoadView.loadFromFile("F:/yiyong/java_tankTESTideasavegame.json").riverelements;
//        this.userTank=SaveLoadView.loadFromFile("F:/yiyong/java_tankTESTideasavegame.json").userTank;
        this.wardata= SaveLoadView.loadFromFile("F:/yiyong/java_tankTESTideasavegame.json").wardata;
        return ;
    }
}
