# Online_TankWar
## 说明
1.该游戏由不愿透露姓名的董先生提供，我做了部分修改完成，相当一部分代码来自通义灵码的帮助。

2.代码最初由老师提供，其中sender类，reciever类，C2component几乎没有修改。

3.IP地址请自行修改，我不会提供老师的服务器IP，当然，你也可以找他本人索要。

## 代码说明
1.本项目在IDEA中直接运行main函数就可以了，位于src/cn/edu/ncepu/sa/Control，不知道为什么构建成jar之后运行就一直说缺JMS库，我至今没有解决。（2024-7-7添加：我建议你用Maven构建，一直到做完我才知道有这么个东西，省好多事）

2.本项目使用ActiveMQ5.9.1和FastJson1.2.7。

3.代码结构非常混乱，MVC架构已经面目全非了，大量使用public和引用传递，请体谅，你就算不想体谅我也懒得改了。

4.仅提供InterGame功能，且目前仅支持在一台电脑上开个双人对战，其他的会报错，我懒得完善了。

5.仍然存在不知道什么时候发作的线程同步问题，有时候犯病，有时候不会。

## 所以为什么会存在这个游戏？
因为这是软件体系结构课程的第二个小作业，大家看一乐吧。
