🎰 Roller: 抽奖系统测试的幕后剧本

一款集教学价值、系统设计与实际业务为一体的企业级抽奖系统。每一次点击“抽奖”，都在背后驱动一次数据库写入、一次消息投递、一次状态变更和一次设计模式的实践。

🌟 项目介绍
🎯 Roller 是一个以“抽奖”为核心场景的 Java 后端项目，模拟了企业中完整的业务开发流程，包括需求建模、模块划分、权限控制、状态流转、任务调度等内容。
项目适合作为：

🎓 后端开发练习项目
🧪 测试自动化模拟平台
🏗️ 架构教学展示样板

🏗 技术栈



层级
技术选型



开发框架
Spring Boot 3.x, MyBatis


数据存储
MySQL, Redis


消息中间件
RabbitMQ


安全机制
JWT, 手机号加密, 密码加盐哈希


通知服务
JavaMailSender 邮件发送


工具集成
Hutool, Lombok, Logback, JUnit 5


🚀 快速启动
✅ 环境要求

JDK 17+
Maven 3.6+
MySQL 8+
Redis 5+
RabbitMQ (开启默认端口)

✅ 克隆仓库
git clone https://github.com/your-username/roller-lottery.git
cd roller-lottery

✅ 初始化数据库
mysql -u root -p < lottery_system.sql

✅ 配置 application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/lottery_system?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_password

spring.redis.host=localhost
spring.redis.port=6379

spring.mail.host=smtp.qq.com
spring.mail.port=465
spring.mail.username=your_email@qq.com
spring.mail.password=your_authorization_code
spring.mail.protocol=smtp
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.ssl.enable=true

jwt.secret=your_jwt_secret_key

✅ 构建运行
mvn clean package
java -jar target/lottery-system-0.0.1-SNAPSHOT.jar

🔑 系统功能概览

👤 用户注册 / 登录 / 认证（JWT）
🎯 活动创建 / 发布 / 启动 / 结束
🎁 奖品配置 / 状态转换 / 自动库存控制
🎲 抽奖执行（状态更新 + 随机逻辑）
📬 邮件通知中奖者（支持异步发送）
📊 抽奖记录回溯与活动查看

🧩 模块设计亮点



模块
设计说明



状态流转
使用 责任链模式 保证“奖品 → 用户 → 活动”顺序处理；每一环再使用 策略模式 动态选择处理方式


抽奖执行
内置可扩展随机算法，支持避免重复中奖、奖品权重设定


通知系统
基于 RabbitMQ 异步投递中奖信息至邮箱发送模块


安全设计
JWT 身份认证、手机号加密、密码哈希防泄漏


异常处理
统一封装业务异常，配合全局异常处理器返回友好提示


测试设计
JUnit 单元测试、Mock 测试覆盖核心逻辑分支


🗂️ 项目结构
lottery-system/
├── controller/         # 接口层
├── service/            # 业务服务层
├── model/              # 实体类、DTO、VO
├── mapper/             # 数据访问接口
├── config/             # 安全、JWT、拦截器等配置类
├── common/             # 通用模块（统一响应、加密、异常等）
├── util/               # 工具类
├── resources/
│   ├── application.properties
│   └── mapper/         # MyBatis XML 映射文件

🧪 示例调用（接口文档）
可结合 Postman 或 Swagger 测试以下流程：

POST /user/register → 用户注册
POST /user/login → 登录获取 JWT Token
POST /activity/create → 创建抽奖活动
POST /activity/start → 启动活动
POST /lottery/draw → 执行抽奖
GET /record/list → 查看中奖记录

📬 邮件通知示意
中奖后，系统自动发送邮件至用户邮箱：
恭喜你中奖啦！
奖品：Switch 游戏机
活动：520 幸运大抽奖
时间：2025-06-01 20:30

📦 Todo（功能计划）

 前端抽奖动画页面（可选 Vue3）
 管理员后台管理系统
 抽奖算法支持权重 + 冷却机制
 抽奖限制：每日限抽、IP 限流
 Redis 限流 + 防刷机制

🙋‍♀️ 作者信息



作者
联系方式
说明



比特练习生
your_email@qq.com
用于学习、练习、教学


📄 License
MIT License © 本项目仅供学习交流，禁止用于商业用途。
🌈 彩蛋时间

你永远不知道谁会中奖，但你一定知道：学完这个项目，你的简历会被抽中。

🎉 欢迎 Star ⭐ | Fork 🍴 | PR 💡
