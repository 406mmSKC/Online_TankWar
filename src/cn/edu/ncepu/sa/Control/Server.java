package cn.edu.ncepu.sa.Control;
import cn.edu.ncepu.sa.Model.*;
import cn.edu.ncepu.sa.utils.Utils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;

import javax.jms.JMSException;
import javax.jms.TextMessage;

public class Server extends C2Component {
    /**
     * 工作任务列表
     */
    java.util.Queue<String> workList = new LinkedList<String>();
    private  ExecutorService executorService; // 假设有5个线程
    WarData warData;
    /**
     * 构造函数
     * @param host 主机名称
     * @param port
     * @param user
     * @param pwd
     */
    public Server(String host, int port, String user, String pwd, WarData warData) {
        super(host, port, user, pwd, "Master");
        this.warData=warData;
        // TODO Auto-generated constructor stub
    }

    /**
     * 增加一个工作项
     * @param work
     */
    public void AddWorkItem(String work) {

        workList.offer(work);
    }

    /**
     * 发送一个子任务
     * @param msg
     */
    private void sendASubTask(String msg) {

        sender.SendAMsg("Internet", msg, queue);

    }

    /**
     * 开始工作
     */
    @Override
    public void StartWork() {
        super.StartWork();
    }
    synchronized public void sendWork() {

        Server2Client send=new Server2Client();
        send.elements=warData.elements;
       // System.out.println("the element will be sent");
        //for (Element shot : send.elements) {
        //    System.out.println(shot+" "+shot.x+" "+shot.y);
       // }
//        for (Element t : send.elements) {
//            if (t instanceof Shot) {
//                System.out.println(((Shot)t).x);
//                System.out.println(((Shot)t).y);
//            }
//            }
        //发送坦克与子弹
        String msg = JSON.toJSONString(send.elements, SerializerFeature.WriteClassName);
       // System.out.println("开始发送主任务"+msg);
        //System.out.println(("Send message begin"));
        //System.out.println(msg);
       // System.out.println(("Send message end"));
        sendASubTask(msg);
       sendASubTask(msg);
    }
    public void reciverWork() throws JMSException {

        if (null != reciever) {
            // 接收器对了中是否消息系
            if (reciever.msgList.size() > 0) {
            //    System.out.println("开始接受主任务");
                // 取出一个消息
                TextMessage msg = reciever.msgList.poll();
                System.out.println(msg.getText());
                workMsg(msg.getText());
            }
        }
    }
    synchronized public void workMsg(String msg) {
        String[] tmp = msg.split(",");
        double x,y;
        int team;
        HashSet<Element> elements;

        switch (tmp[0]) {
            // String msg ="move"+","+String.valueOf(mouseX)+","+String.valueOf(mouseY)+","
            //                            +String.valueOf(tank.team);
            case "move":
                System.out.println("move");
                 x = Double.parseDouble(tmp[1]);
                 y= Double.parseDouble(tmp[2]);
                 team=Integer.parseInt(tmp[3]);
                elements = warData.elements;
                //遍历每一个
                for (Element tank : elements) {
                    if (tank instanceof Tank) {
                        if (((Tank) tank).team==team)
                        {
                            ((Tank) tank).turretDir = Utils.ppDir(tank.x, tank.y, x, y) + 90;
                            if(((Tank) tank).colletion) {
                                if (((Tank) tank).ColletionCheck(((Tank) tank).turretDir)) {
                                    ((Tank) tank).dir = ((Tank) tank).turretDir;
                                    ((Tank) tank).toLen = tank.distance(x, y);
                                    ((Tank) tank).moving = true;
                                }
                            }
                            else {
                                ((Tank) tank).dir = ((Tank) tank).turretDir;
                                ((Tank) tank).toLen = tank.distance(x, y);
                                ((Tank) tank).moving = true;
                            }
                        }
                    }
                }
                break;
                case "shot":
                    System.out.println("shot");
                    x = Double.parseDouble(tmp[1]);
                    y= Double.parseDouble(tmp[2]);
                    team=Integer.parseInt(tmp[3]);
                    elements = warData.elements;
                    // String msg ="move"+","+String.valueOf(mouseX)+","+String.valueOf(mouseY)+","
                    //                            +String.valueOf(tank.team);
                    Shot shot = null;
                    for (Element tank : elements) {
                        if (tank instanceof Tank) {
                            if (((Tank) tank).team == team) {
                                if(((Tank) tank).cannonball>0)
                                {

                                    ((Tank) tank).turretDir = Utils.ppDir(tank.x, tank.y, x, y) + 90;
                                    ((Tank) tank).cannonball--;
                                    shot = new Shot(((Tank)tank), 500);
                                   
                                }
                            }
                        }
                    }
                    if(shot!=null)
                    elements.add(shot);
                break;
            default:
                Tank tank = JSON.parseObject(msg, Tank.class);
                warData.elements.add(tank);
                break;
        }
}
    @Override
        public void run() {
        while (toWork) {
            try {
                reciverWork();
                sendWork();

            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
            // 休眠0.1秒
            try {
                sleep(5);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }
//    public void run() {
//        executorService = Executors.newFixedThreadPool(2); // 创建一个固定大小的线程池
//
//        // 提交任务到线程池
//        executorService.submit(this::executeSendWork);
//        executorService.submit(this::executeReceiveWork);
//
//        // 如果需要等待所有任务完成再结束线程，可以使用以下代码
//        // executorService.shutdown();
//        // try {
//        //     executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//        // } catch (InterruptedException e) {
//        //     Thread.currentThread().interrupt();
//        // }
//    }
    private void executeSendWork() {
        while (toWork) {
            try {
                sendWork();
            } catch (Exception e) {
                // 异常处理
                e.printStackTrace();
            }
            try {
                Thread.sleep(10); // 休眠100毫秒
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
    private void executeReceiveWork() {
        while (toWork) {
            try {
                reciverWork();
            } catch (Exception e) {
                // 异常处理
                e.printStackTrace();
            }
            try {
                Thread.sleep(10); // 休眠100毫秒
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
    public void StopWork() {
        super.StopWork();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
        }
    }

    @Override
    String getResult(String msg) {
        // TODO Auto-generated method stub
        return null;
    }

}
