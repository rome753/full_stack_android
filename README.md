# full_stack_chat_android
这是一个开发中的全栈式即时聊天工具，包括python后台、Android客户端和html网页聊天室，后台搭建在自己的个人服务器(http://rome753.cc)上。目前已实现基本的登录注册和聊天功能。
只要你会Android/ios/python/js/html中的一种，就可以参加这个项目（ios本人不熟，欢迎开发ios客户端），欢迎提交Pull Request，有bug或者功能上的建议可以创建Issue，也可以直接到聊天室吼一声~

* 网页聊天室地址 <http://rome753.cc/chatroom>
* python服务端项目地址 <https://github.com/rome753/full_stack_chat>

## 架构
使用一些通用、高效的框架
- [okhttp]：com.squareup.okhttp3:okhttp:3.4.2
- [okhttp-ws]：com.squareup.okhttp3:okhttp-ws:3.4.2
- [eventbus]：org.greenrobot:eventbus:3.0.0
- [gson]：com.google.code.gson:gson:2.8.0
- [butterknife]:

## 功能（~~已实现标记~~）
- ~~注册~~
> 检测邮箱和用户名是否已存在

- ~~登录~~
> 验证用户名和密码，登录成功后保存用户名

- ~~即时聊天~~
> 使用okhttp-ws的websocket长连接实现。

- 邮箱验证码
- 数据库
- 通知栏消息
- **两人聊天**
- **个人信息页面（头像）**
- **获取历史聊天记录**
- **发送图片**
- 第三方登录