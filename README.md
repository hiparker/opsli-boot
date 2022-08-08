# OPSLI 快速开发平台 (v2.0)
<div align="center">
 <img width="500" src="https://gitee.com/hiparker/opsli-ui/raw/master/repository-images/logo.png"/>
 <br/> <br/>

[![AUR](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg)](https://github.com/hiparker/opsli-boot/blob/master/LICENSE) [![spring-boot](https://img.shields.io/badge/spring--boot-2.3.3.RELEASE-green.svg)](http://spring.io/projects/spring-boot) [![mybatis-plus](https://img.shields.io/badge/mybatis--plus-3.4.0-blue.svg)](http://mp.baomidou.com) [![hutool](https://img.shields.io/badge/hutool-5.7.14-blue.svg)](https://www.hutool.cn) [![Stars](https://img.shields.io/github/stars/hiparker/opsli-boot?style=flat-square&label=Stars&logo=github)](https://github.com/hiparker/opsli-boot) [![Forks](https://img.shields.io/github/forks/hiparker/opsli-boot?style=flat-square&label=Forks&logo=github)](https://github.com/hiparker/opsli-boot)
</div>

## 关于

> OPSLI 是一款的低代码快速平台，零代码开发，致力于做更简洁的后台管理系统！

> OPSLI 快速开发平台基于springboot、vue、element-ui ，项目采用前后端分离架构，热插拔式业务模块与插件扩展性高 ,代码简洁，功能丰富，开箱即用，帮助Java项目解决70%的重复工作，让开发更关注业务逻辑，既能快速提高效率，节省研发成本，同时又不失灵活性！ 

## 地址

- 官方网站: <a href="https://opsli.com" target="_blank">https://opsli.com</a>
- 演示地址: <a href="https://demo.opsli.bedebug.com" target="_blank">https://demo.opsli.bedebug.com</a>
- 文档地址: <a href="https://wiki.opsli.bedebug.com" target="_blank">https://wiki.opsli.bedebug.com</a>
- 作者博客: <a href="https://www.bedebug.com" target="_blank">https://www.bedebug.com</a>
- 问题反馈: <a href="https://github.com/hiparker/opsli-boot/issues" target="_blank">https://github.com/hiparker/opsli-boot/issues</a>
- 交流Q群: 724850675 (1群)

## 技术选型

### 前端

- vue-admin-beautiful

### 后端版本

> 单机版

| 名称             | 版本号 |      | 名称           | 版本号          |
| ---------------- |---| ---- | -------------- |--------------|
| jdk版本          | 1.8 |      | springboot版本 | 2.5.6        |
| mybatis-plus版本 | 3.5.2 |      | pagehelper版本 | 1.3.0        |
| druid版本        | 1.1.17 |      | dynamic版本    | 2.5.4        |
| fastjson版本  | 1.2.83  |      | transmittable版本        | 2.12.5       |
| ehcache版本      | 3.9.0 |      | easyexcel版本  | 2.2.6        |
| captcha版本      | 1.6.2 |      | guava版本      | 30.0-android |
| enjoy版本        | 4.9.06 |      | hutool版本     | 5.7.14       |

## 在线演示

> - 地址：<a href="https://demo.opsli.bedebug.com" target="_blank">https://demo.opsli.bedebug.com</a>
> - 账号：demo
> - 密码：Aa123456

--------------------------------------------

> -  数据库监控/系统接口
> -  账号: admin
> -  密码: 123456

## 系统预览
![opsli-1](https://gitee.com/hiparker/opsli-ui/raw/master/repository-images/opsli-1.jpg)

![opsli-2](https://gitee.com/hiparker/opsli-ui/raw/master/repository-images/opsli-2.jpg)

![opsli-3](https://gitee.com/hiparker/opsli-ui/raw/master/repository-images/opsli-3.jpg)

![opsli-4](https://gitee.com/hiparker/opsli-ui/raw/master/repository-images/opsli-4.jpg)

![opsli-5](https://gitee.com/hiparker/opsli-ui/raw/master/repository-images/opsli-5.jpg)

![opsli-7](https://gitee.com/hiparker/opsli-ui/raw/master/repository-images/opsli-7.jpg)

![opsli-8](https://gitee.com/hiparker/opsli-ui/raw/master/repository-images/opsli-8.jpg)

![opsli-9](https://gitee.com/hiparker/opsli-ui/raw/master/repository-images/opsli-9.jpg)

## 代码结构
```
.
├── opsli-api                                               对外API 控制中心
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── org
│   │   │   │       └── opsli
│   │   │   │           └── api
│   │   │   │               ├── base                          API 基础
│   │   │   │               ├── web                           API Web层
│   │   │   │               └── warpper                       API 封装对象
│   │   │   │
├── opsli-base-support                                      基础模块
│   ├── opsli-common                                          基础 - 公共模块
│   │   ├── src
│   │   │   ├── main
│   │   │   │   ├── java
│   │   │   │   │   └── org
│   │   │   │   │       └── opsli
│   │   │   │   │           └── common
│   │   │   │   │               ├── annotation                  公共模块 - 注解类
│   │   │   │   │               ├── api                         公共模块 - Result内部文件(暂时无用)
│   │   │   │   │               ├── base                        公共模块 - 基础类
│   │   │   │   │               ├── constants                   公共模块 - 常量
│   │   │   │   │               ├── enums                       公共模块 - Enums
│   │   │   │   │               ├── exception                   公共模块 - 异常类
│   │   │   │   │               ├── msg                         公共模块 - 信息
│   │   │   │   │               ├── thread                      公共模块 - 线程相关
│   │   │   │   │               └── utils                       公共模块 - 工具类
│   │   │   │   │
│   └── opsli-core                                          基础 - 核心模块
│       ├── src
│       │   ├── main
│       │   │   ├── java
│       │   │   │   └── org
│       │   │   │       └── opsli
│       │   │   │           ├── core                        核心
│       │   │   │           │   ├── api                         核心模块 - API接口相关（登录Token缓存）
│       │   │   │           │   ├── autoconfigure               核心模块 - 自动配置
│       │   │   │           │   │   ├── conf                      自动装配
│       │   │   │           │   │   └── properties                配置文件注入
│       │   │   │           │   ├── base                        核心模块 - 基础类 Entity Service
│       │   │   │           │   ├── cache                       核心模块 - 缓存处理
│       │   │   │           │   ├── eventbus                    核心模块 - 消息事件
│       │   │   │           │   ├── filters                     核心模块 - 过滤器
│       │   │   │           │   │   ├── aspect                     AOP切面
│       │   │   │           │   │   └── interceptor                Spring拦截器
│       │   │   │           │   ├── general                     核心模块 - 其他处理器
│       │   │   │           │   ├── handler                     核心模块 - 异常拦截处理
│       │   │   │           │   ├── holder                      核心模块 - 上下文数据
│       │   │   │           │   ├── listener                    核心模块 - 系统监听器
│       │   │   │           │   ├── log                         核心模块 - 日志处理
│       │   │   │           │   ├── msg                         核心模块 - 信息
│       │   │   │           │   ├── options                     核心模块 - 系统参数
│       │   │   │           │   ├── persistence                 核心模块 - 查询条件构造器
│       │   │   │           │   │   └── querybuilder
│       │   │   │           │   │       └── chain               核心模块 - 查询条件构造器 - 责任链(例: 租户处理)
│       │   │   │           │   ├── security                    核心模块 - 权限验证
│       │   │   │           │   │   ├── filter                       拦截器
│       │   │   │           │   │   └── service                      获取用户的Service
│       │   │   │           │   └── utils                       核心模块 - 工具类
│       │   │   │           └── pligins                   插件
│       │   │   │               └── oss                         插件模块 - OSS文件存储
│       │   │   │
├── opsli-modulars                                        业务
│   ├── opsli-modulars-generator                              代码生成器
│   │   ├── src
│   │   │   ├── main
│   │   │   │   ├── java
│   │   │   │   │   └── org
│   │   │   │   │       └── opsli
│   │   │   │   │           ├──core                           代码生成器 - 核心包
│   │   │   │   │           │   └── generator
│   │   │   │   │           │       ├── enums                   代码生成器 - 枚举类
│   │   │   │   │           │       ├── exception               代码生成器 - 异常类
│   │   │   │   │           │       ├── msg                     代码生成器 - 信息类
│   │   │   │   │           │       ├── strategy                代码生成器 - 策略类
│   │   │   │   │           │       └── utils                   代码生成器 - 工具包
│   │   │   │   │           │
│   │   │   │   │           └── modulars                      代码生成器 - 业务包
│   │   │   │   │               └── generator
│   │   │   │   │                   ├── column                  代码生成器 - 表结构
│   │   │   │   │                   ├── createrlogs             代码生成器 - 生成记录
│   │   │   │   │                   ├── general                 代码生成器 - 同步数据库执行器
│   │   │   │   │                   ├── importable              代码生成器 - 数据库导入
│   │   │   │   │                   └── table                   代码生成器 - 表管理
│   │   │   │   │
│   └── opsli-modulars-system                               系统模块
│       ├── src
│       │   ├── main
│       │   │   ├── java
│       │   │   │   └── org
│       │   │   │       └── opsli
│       │   │   │           └── modulars
│       │   │   │               ├── system                    系统模块 - 系统配置
│       │   │   │               │   ├── area                    系统模块 - 地域
│       │   │   │               │   ├── dict                    系统模块 - 字典
│       │   │   │               │   ├── login                   系统模块 - 登录
│       │   │   │               │   │   ├── dto                   系统模块 - 登录 - DTO
│       │   │   │               │   │   ├── event                 系统模块 - 登录 - 消息事件
│       │   │   │               │   │   ├── handler               系统模块 - 登录 - 前置、成功、失败处理器
│       │   │   │               │   │   ├── vo                    系统模块 - 登录 - 返回数据
│       │   │   │               │   │   └──web                    系统模块 - 登录 - 接口控制器
│       │   │   │               │   ├── logs                    系统模块 - 日志
│       │   │   │               │   ├── menu                    系统模块 - 菜单
│       │   │   │               │   ├── monitor                 系统模块 - 系统监控
│       │   │   │               │   ├── options                 系统模块 - 参数配置
│       │   │   │               │   ├── org                     系统模块 - 组织机构
│       │   │   │               │   ├── role                    系统模块 - 角色
│       │   │   │               │   ├── tenant                  系统模块 - 租户
│       │   │   │               │   └── user                    系统模块 - 用户
│       │   │   │               │
│       │   │   │               └── tools                     工具包
│       │   │   │                   ├── api                     工具包 - 版本控制API测试类
│       │   │   │                   ├── common                  工具包 - 公共服务
│       │   │   │                   ├── email                   工具包 - 邮件包
│       │   │   │                   ├── oss                     工具包 - 文件存储
│       │   │   │                   └── searchhis               工具包 - 搜索历史
│       │   │   │
├── opsli-plugins                                  插件
│   ├── opsli-plugins-crypto                              加解密插件包
│   │   ├── src
│   │   │   ├── main
│   │   │   │   ├── java
│   │   │   │   │   └── org
│   │   │   │   │       └── opsli
│   │   │   │   │           └── plugins
│   │   │   │   │               └── crypto
│   │   │   │   │                   ├── enums                 加解密插件包 - 枚举
│   │   │   │   │                   ├── exception             加解密插件包 - 异常处理类
│   │   │   │   │                   ├── model                 加解密插件包 - 模型
│   │   │   │   │                   ├── msg                   加解密插件包 - 异常消息
│   │   │   │   │                   ├── spring                加解密插件包 - Spring集成相关内容
│   │   │   │   │                   └── strategy              加解密插件包 - 加解密策略 包含 对称、非对称等等
│   │   │   │   │
│   ├── opsli-plugins-ehcache                             Ehcache缓存插件 (二级缓存)
│   │   ├── src
│   │   │   ├── main
│   │   │   │   ├── java
│   │   │   │   │   └── org
│   │   │   │   │       └── opsli
│   │   │   │   │           └── plugins
│   │   │   │   │               └── cache
│   │   │   │   │                   ├── conf                  Ehcache缓存插件 - 自动装配
│   │   │   │   │                   ├── msg                   Ehcache缓存插件 - 信息
│   │   │   │   │                   └── service               Ehcache缓存插件 - 服务
│   │   │   │   │
│   ├── opsli-plugins-email                               邮件插件包
│   │   ├── src
│   │   │   ├── main
│   │   │   │   ├── java
│   │   │   │   │   └── org
│   │   │   │   │       └── opsli
│   │   │   │   │           └── plugins
│   │   │   │   │               └── email
│   │   │   │   │                   ├── conf                  邮件插件包 - 配置文件
│   │   │   │   │                   ├── exception             邮件插件包 - 异常处理类
│   │   │   │   │                   ├── msg                   邮件插件包 - 异常消息
│   │   │   │   │                   ├── service               邮件插件包 - 处理类
│   │   │   │   │                   └── wrapper               邮件插件包 - 包装器
│   │   │   │   │
│   ├── opsli-plugins-excel                               Excel插件
│   │   ├── src
│   │   │   ├── main
│   │   │   │   ├── java
│   │   │   │   │   └── org
│   │   │   │   │       └── opsli
│   │   │   │   │           └── plugins
│   │   │   │   │               └── excel
│   │   │   │   │                   ├── annotation            Excel插件 - 注解
│   │   │   │   │                   ├── exception             Excel插件 - 异常类
│   │   │   │   │                   ├── factory               Excel插件 - 工厂
│   │   │   │   │                   ├── listener              Excel插件 - 监听器
│   │   │   │   │                   └── msg                   Excel插件 - 信息
│   │   │   │   │
│   └── opsli-plugins-redis                               Redis缓存插件(一级缓存)
│   │   ├── src
│   │   │   ├── main
│   │   │   │   ├── java
│   │   │   │   │   └── org
│   │   │   │   │       └── opsli
│   │   │   │   │           └── plugins
│   │   │   │   │               └── redis
│   │   │   │   │                   ├── conf                  Redis缓存插件 - 自动装配
│   │   │   │   │                   ├── exception             Redis缓存插件 - 异常类
│   │   │   │   │                   ├── jsonserializer        Redis缓存插件 - json特殊处理器
│   │   │   │   │                   ├── lock                  Redis缓存插件 - 分布式锁
│   │   │   │   │                   ├── msg                   Redis缓存插件 - 信息
│   │   │   │   │                   ├── pushsub               Redis缓存插件 - 消息订阅
│   │   │   │   │                   └── scripts               Redis缓存插件 - 脚本处理
│   │   │   │   └── resources
│   │   │   │   │    └── lua                                  Redis缓存插件 - Lua脚本
│   │   │   │   │
│   └── opsli-plugins-redisson                            Redisson分布式锁
│   │   ├── src
│   │   │   ├── main
│   │   │   │   ├── java
│   │   │   │   │   └── org
│   │   │   │   │       └── opsli
│   │   │   │   │           └── plugins
│   │   │   │   │               └── redis
│   │   │   │   │                   ├── annotation            Redisson分布式锁 - 注解
│   │   │   │   │                   ├── conf                  Redisson分布式锁 - 自动装配
│   │   │   │   │                   ├── constant              Redisson分布式锁 - 常量
│   │   │   │   │                   ├── enums                 Redisson分布式锁 - 枚举类
│   │   │   │   │                   ├── properties            Redisson分布式锁 - 配置类
│   │   │   │   │                   └── strategy              Redisson分布式锁 - 策略
│   │   │   │   │
│   ├── opsli-plugins-security                            安全认证插件包
│   │   ├── src
│   │   │   ├── main
│   │   │   │   ├── java
│   │   │   │   │   └── org
│   │   │   │   │       └── opsli
│   │   │   │   │           └── plugins
│   │   │   │   │               └── security
│   │   │   │   │                   ├── authentication        安全认证插件包 - 识别器
│   │   │   │   │                   ├── checker               安全认证插件包 - 检查器
│   │   │   │   │                   ├── eventbus              安全认证插件包 - 消息事件
│   │   │   │   │                   ├── eventdto              安全认证插件包 - 消息事件DTO
│   │   │   │   │                   ├── exception             安全认证插件包 - 异常
│   │   │   │   │                   ├── handler               安全认证插件包 - 登陆处理器
│   │   │   │   │                   ├── properties            安全认证插件包 - 配置文件
│   │   │   │   │                   ├── provider              安全认证插件包 - Security认证器
│   │   │   │   │                   ├── service               安全认证插件包 - 加载用户信息抽象Service
│   │   │   │   │                   ├── utils                 安全认证插件包 - 工具包
│   │   │   │   │                   └── vo                    安全认证插件包 - VO
│   │   │   │   │
│   ├── opsli-plugins-sms                               短信插件包
│   │   ├── src
│   │   │   ├── main
│   │   │   │   ├── java
│   │   │   │   │   └── org
│   │   │   │   │       └── opsli
│   │   │   │   │           └── plugins
│   │   │   │   │               └── sms
│   │   │   │   │                   ├── enums                 短信插件包 - 配置文件
│   │   │   │   │                   ├── exception             短信插件包 - 异常处理类
│   │   │   │   │                   ├── model                 短信插件包 - 模型
│   │   │   │   │                   ├── msg                   短信插件包 - 异常消息
│   │   │   │   │                   └── service               短信插件包 - 服务处理
│   │   │   │   │
│   └── opsli-plugins-waf                                 Waf软防火墙
│       ├── src
│       │   ├── main
│       │   │   ├── java
│       │   │   │   └── org
│       │   │   │       └── opsli
│       │   │   │           └── plugins
│       │   │   │               └── redis
│       │   │   │                   ├── conf                  Waf软防火墙 - 自动装配
│       │   │   │                   ├── filter                Waf软防火墙 - 拦截器
│       │   │   │                   ├── msg                   Waf软防火墙 - 信息
│       │   │   │                   ├── properties            Waf软防火墙 - 配置类
│       │   │   │                   ├── servlet               Waf软防火墙 - Servlet处理器
│       │   │   │                   └── util                  Waf软防火墙 - 工具包
│       │   │   │
└── opsli-starter                                        启动类
    └── src
        └── main
            ├── java
            │   └── org
            │       └── opsli                              启动类 - 启动器
            └── resources                                  启动类 - 配置
                └── config                                 启动类 - 其他配置
```

## 作者寄语

感谢Star，感恩相遇，愿世间美好与我们环环相扣，加油！屏幕前的我们，打破桎梏，坚守初心。其实人生改变命运的机会并没有太多，我们并不是不优秀，我们也并不是一无是处，我们也希望驻足山巅被众人仰望，也许我们缺少的只是一个机会，缺少的只是生命中的导师，我希望这个框架帮助到更多的人，希望有一天，我们面试的时候不再胆怯，希望有一天别人看到的不仅仅是你的努力，还有你的功成名就，出人头地。

## 鸣谢

- <a href="https://www.jetbrains.com/?from=opsli-boot" rel="nofollow">感谢 JetBrains 提供的免费开源 License:</a>

    <a href="https://www.jetbrains.com/?from=opsli-boot" rel="nofollow"><img src="https://camo.githubusercontent.com/a4b533abbf1bd277a3943956fa8bed240b02184ccc6b5e9f751ae2f5afd7cfa6/687474703a2f2f7374617469632e786b636f64696e672e636f6d2f737072696e672d626f6f742d64656d6f2f3036343331322e6a7067" width="100px" alt="jetbrains"  style="max-width:100%;"></a>

## 贡献者列表

[![contributors](https://whnb.wang/contributors/hiparker/opsli-boot)](https://github.com/hiparker/opsli-boot)

## Stars 趋势

### Gitee
[![Stargazers over time](https://whnb.wang/stars/hiparker/opsli-boot)](https://github.com/hiparker/opsli-boot)

### Github
[![Stargazers over time](https://starchart.cc/hiparker/opsli-boot.svg)](https://github.com/hiparker/opsli-boot)


## 支持

> 谢谢您愿意支持开源

<div align="center">
<img width="200" src="https://gitee.com/hiparker/opsli-ui/raw/master/repository-images/vx.png"/>
<img width="200" src="https://gitee.com/hiparker/opsli-ui/raw/master/repository-images/zfb.png"/>
</div>

