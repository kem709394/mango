 

### 项目说明
- 轻量级的系统开发框架
- 前后分离系统的权限控制解决方案
- 一套使用简单，扩展性强，安全可靠的权限系统
- 框架将会长期维护，不断优化和增加各种必备的功能

### 项目特点
- 针对前后分离的系统结构，设计更加符合实际的权限控制方案
- 完善的XSS防范及脚本过滤，彻底杜绝XSS攻击（自创的XSS防范方式，网上找不到的）
- 使用Token的方式授权登录用户，实现无状态的请求
- 支持分布式部署，Token可存储在数据库或redis中
- 友好的代码结构及注释，便于阅读及二次开发
- 引入quartz定时任务，可动态完成任务的添加、修改、删除、暂停、恢复及日志查看等功能
- 页面交互使用Vue2.x，极大的提高了开发效率
- 集成Druid数据库监控器，方便了解数据库情况
- 引入swagger文档支持，方便编写API接口文档

### 权限设计
![权限设计图](https://images.gitee.com/uploads/images/2019/0815/161903_99c7228f_547845.png "QQ浏览器截图20190815161821.png")
整个权限控制模块，划分为前端组件权限和数据接口权限两部分。前端组件权限，主要控制前端UI层的组件是否可见，如菜单、按钮、链接等UI组件；数据接口权限，主要控制网络请求的url访问权限，同时也支持对请求返回数据的过滤。

### 代码结构
mango
│  
├─doc     相关文档  
│   
├─src      管理后台  
│  │   
│  ├─authorize  权限模块  
│  │   
│  ├─common  公共模块  
│  │   
│  ├─config  配置模块  
│  │   
│  ├─controller  基础接口  
│  │   
│  ├─filter  过滤器&拦截器  
│  │   
│  ├─handler  处理转换  
│  │   
│  ├─sys  系统模块  
│  │  ├─controller 系统接口  
│  │  ├─entity 数据实体  
│  │  ├─mapper dao层  
│  │  └─service service层  
│  │   
│  ├─task  任务模块  
│  │   
│  └─resources   
│        ├─statics  静态资源  
│        ├─templates 页面模板  
│        └─application.yml   全局配置文件  


### 技术选型
- 核心框架：Spring Boot 2.1
- 安全框架：Apache Shiro 1.4
- 视图框架：Spring MVC 5.0
- 持久层框架：MyBatis 3.5
- 定时器：Quartz 2.3
- 数据库连接池：Druid 1.1
- 日志管理：SLF4J 1.7、Log4j
- 页面交互：Vue2.x

### 软件需求
- JDK1.8
- MySQL5.7.8+
- Maven3.0+

### 本地部署
- 通过git下载源码
- 创建数据库mango，数据库编码为utf8mb4
- 执行doc/db/mysql.sql文件，初始化数据【按需导入表结构及数据】
- 用IDEA打开本地已下载好的代码
- 由于entity使用lombok模式，IDEA需要安装lombok插件（否则找不到entity的get set方法）
- 修改application-dev.yml文件，更新MySQL账号和密码
- 运行BaseApplication.java，即可启动项目
- 默认不使用Redis,不需要安装Redis
- 部署前端(查看项目mango-admin-vue)
- 浏览器访问路径：http://localhost:8080
- 账号密码：admin/123456

### 项目演示
- 系统演示地址：http://kem.gitee.io/mango-admin-vue
- 测试账号密码：admin/123456
![登录页面](https://images.gitee.com/uploads/images/2019/0815/170700_4055bf04_547845.jpeg "QQ浏览器截图20190815150158(1).jpg")
![系统首页](https://images.gitee.com/uploads/images/2019/0815/170758_732bcce6_547845.png "QQ浏览器截图20190815150316.png")
![用户管理](https://images.gitee.com/uploads/images/2019/0815/170858_f2d57160_547845.png "QQ浏览器截图20190815150334.png")
![角色管理](https://images.gitee.com/uploads/images/2019/0815/170932_c73243ee_547845.png "QQ浏览器截图20190815150342.png")
![角色菜单](https://images.gitee.com/uploads/images/2019/0815/171012_b7cda093_547845.png "QQ浏览器截图20190815150450.png")
![角色资源](https://images.gitee.com/uploads/images/2019/0815/171035_0c73972b_547845.png "QQ浏览器截图20190815150505.png")
![参数设置](https://images.gitee.com/uploads/images/2019/0815/171107_42935684_547845.png "QQ浏览器截图20190815150517.png")
![字典管理](https://images.gitee.com/uploads/images/2019/0815/171145_20767632_547845.png "QQ浏览器截图20190815150528.png")
![菜单管理](https://images.gitee.com/uploads/images/2019/0815/171220_ef9150a1_547845.png "QQ浏览器截图20190815150538.png")
![资源管理](https://images.gitee.com/uploads/images/2019/0815/171242_b609a2a9_547845.png "QQ浏览器截图20190815150546.png")
![定时任务](https://images.gitee.com/uploads/images/2019/0815/171319_7890ccab_547845.png "QQ浏览器截图20190815150553.png")
![文件管理](https://images.gitee.com/uploads/images/2019/0815/171403_14e6b631_547845.png "QQ浏览器截图20190815150637.png")
![druid](https://images.gitee.com/uploads/images/2019/0815/171422_d1e4f5be_547845.png "QQ浏览器截图20190815150703.png")
![swagger](https://images.gitee.com/uploads/images/2019/0815/171456_5e29e755_547845.png "QQ浏览器截图20190815150709.png")

### 技术支持
- 联系QQ:172221929
![赞赏图片](https://images.gitee.com/uploads/images/2019/0815/174146_fb2d5301_547845.jpeg "未命名_副本.jpg")
