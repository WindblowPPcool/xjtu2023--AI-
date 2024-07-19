# xjtu2023--AI-
本项目为XJTU2023暑期实训期间，本人参与的八人团队作品；只做学习用途，不涉及商业用途

项目名称：随心定义的AI虚拟角色--基于科大讯飞spark4.0Ultra

项目简介：实现了一个基于讯飞spark4.0Ultra的用户自定义性格、身份、关系等属性的虚拟角色对话功能，具有较为完善的账户、聊天、历史记录功能，集合了开源Live2d形象插件、语音播放对话的功能，具有一套完整的后台数据管理系统。

部署方法：

    1.clone仓库至本地
    
    2.下载nodejs配置好vue和npm
    
    3.管理员模式cmd进入front-end文件夹运行命令：npm install；
        若报错error：检查环境、是否为管理员模式；
        若警告warn：无影响
        
    4.下载MySQL，配置环境及账户，导入xadmin-master文件夹中的sql文件；
        推荐使用idea导入
        
    4.打开back-end文件夹，配置src/main/resources/application.xml中的数据库地址端口密码项，编译运行src中的java文件；
        推荐使用idea直接编译运行；
        也可使用命令行
        
    5.打开xadmin-master文件夹，配置src/main/resources/application.xml中的数据库地址端口密码项，编译运行，方法同4
    
    6.打开front-end文件夹中，编辑src/ts/AiChatWebSocket.ts文件中的api配置，保存后进入该目录下的cmd，执行命令 npm run serve；
        仅调试过讯飞spark4.0Ultra
        
    7.下载eSpeak软件https://espeak.sourceforge.net，在back-end文件夹中的src/main/java/service/impl/eSpeakServiceImpl.java中修改espeak.exe的地址，并按需设置音频文件输出地址；//若不配置语音软件，其他功能亦可正常运行
    
    8.执行完毕后，访问6中控制台提示的地址http://localhost:18000/#/即可运行,后台管理系统的地址默认为(http://localhost:9999/page/end/login.html)。
