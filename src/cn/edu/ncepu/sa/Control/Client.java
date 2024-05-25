package cn.edu.ncepu.sa.Control;
import cn.edu.ncepu.sa.Model.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;

import javax.jms.JMSException;
import javax.jms.TextMessage;

public class Client extends C2Component {
    /**
     * 工作任务列表
     */
    public java.util.Queue<String> workList = new LinkedList<String>();
    private ExecutorService executorService;
    public WarData warData;
    /**
     * 构造函数
     * @param host 主机名称
     * @param port
     * @param user
     * @param pwd
     */
    public Client(String host, int port, String user, String pwd, WarData warData) {
        super(host, port, user, pwd, "Internet");
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
    synchronized private void sendASubTask(String msg) {
        sender.SendAMsg("Master", msg, queue);
    }

    /**
     * 开始工作
     */
    @Override
    public void StartWork() {
        super.StartWork();
    }
    public void sendWork()
    {
        if (workList.size() > 0) {

                String next = workList.poll();
                System.out.println("开始子任务"+next);
                sendASubTask(next);
            }

    }
    public void reciverWork() throws JMSException {
        if (null != reciever) {
            // 接收器对了中是否消息系
            if (reciever.msgList.size() > 0) {
                // 取出一个消息
             //   System.out.println("开始接受子任务");
                TextMessage msg = reciever.msgList.poll();
                workMsg(msg.getText());
            }
        }
    }
   synchronized public void workMsg(String msg) {
        Server2Client send=new Server2Client();
        //System.out.println("Received message: " + msg);
        HashSet<Element>el=JSON.parseObject(msg, new TypeReference<HashSet<Element>>(){});
        //System.out.println("Parsed elements begin:");
        //for(Element e:el){
        //    System.out.println(e+" "+e.x+" "+e.y);
        //}
        //System.out.println("Parsed elements end");

        send.elements =el;

        warData.elements.clear();
        warData.elements.addAll(el);



    }
    @Override
    public void run() {
        while (toWork) {
            try {
                sendWork();
                reciverWork();
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
