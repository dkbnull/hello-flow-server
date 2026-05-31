<p align="center">
  <img src="https://github.com/dkbnull/hello-flow-web/src/assets/logo.png" alt="HelloFlow" width="120">
</p>

<h1 align="center">Hello Flow</h1>

<p align="center"><strong>软件研发项目管理工具 - 服务端</strong></p>

<p align="center">
  <a href="https://github.com/dkbnull/hello-flow-server">
    <img src="https://img.shields.io/badge/GitHub-服务端-blue?logo=github">
  </a>
  <a href="https://gitee.com/dkbnull/hello-flow-server">
    <img src="https://img.shields.io/badge/Gitee-服务端-red?logo=gitee">
  </a>
  <a href="https://github.com/dkbnull/hello-flow-web">
    <img src="https://img.shields.io/badge/GitHub-WEB 端-blue?logo=github">
  </a>
  <a href="https://gitee.com/dkbnull/hello-flow-web">
    <img src="https://img.shields.io/badge/Gitee-WEB 端-red?logo=gitee">
  </a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/JDK-17-green?logo=java">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.5.14-brightgreen?logo=springboot">
  <img src="https://img.shields.io/badge/MySQL-8.0+-orange?logo=mysql">
  <img src="https://img.shields.io/badge/License-Apache%202.0-blue">
</p>

---

## 项目简介

Hello Flow 是一款面向软件研发团队的项目管理工具，支持需求管理、任务流转、缺陷跟踪、迭代管理，提供看板视图和统计报表。

本项目为 **服务端**，提供 RESTful API 接口。Web 前端项目请访问 [hello-flow-web](https://github.com/dkbnull/hello-flow-web)。

## 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17 | 运行环境 |
| Spring Boot | 3.5.14 | 应用框架 |
| Spring Security | - | 认证与权限控制 |
| MyBatis Plus | 3.5.12 | ORM 框架 |
| MySQL | 8.0+ | 数据库 |
| JWT (jjwt) | 0.12.6 | Token 认证 |
| MapStruct | 1.6.3 | 对象映射 |
| SpringDoc | 2.8.8 | API 文档 |
| Lombok | - | 代码简化 |

## 用户角色与权限

系统采用 **RBAC + 职位** 双重权限模型，角色控制系统能力，职位决定业务操作范围。

| 角色 | 编码 | 说明 |
|------|------|------|
| 管理员 | ADMIN | 系统级角色，拥有所有权限，可管理用户 |
| 普通用户 | USER | 业务角色，权限由职位决定 |

| 职位 | 编码 | 说明 |
|------|------|------|
| 项目经理 | PM | 管理自己负责的项目，创建任务 |
| 开发工程师 | DEV | 开发任务，可在项目内担任开发主责，可评审他人任务 |
| 测试工程师 | QA | 测试任务，可在项目内担任测试主责，提交缺陷 |

每个项目设有 **开发主责** 和 **测试主责**，分别负责开发侧评审和测试侧验收，实现职责清晰的任务流转。

## 核心功能

### 用户管理

- 管理员创建账号（用户名、密码、昵称、邮箱、手机号、职位、系统角色）
- 启用/禁用用户，不开放自助注册
- 用户可修改个人信息（昵称、邮箱、手机号、头像）

### 项目管理

- 管理员创建项目（名称、描述、项目经理、开发主责、测试主责）
- 项目经理/管理员添加项目成员（开发工程师、测试工程师）
- 项目状态：进行中、归档

### 任务管理

任务管理是 HelloFlow 的核心，覆盖从创建到关闭的完整生命周期。

- **任务类型**：需求、完善、缺陷（仅分类标签，工作流一致）
- **任务优先级**：最低、低、中（默认）、高、最高
- **创建任务**：填写标题、描述（富文本）、可指定开发者、到期时间、优先级，可关联其他任务；未指定开发者时自动指派给项目开发主责
- **子任务**：支持单层子任务，任何任务下均可创建；缺陷关联作为子任务；子任务全部完成后父任务状态自动更新
- **任务关联**：支持关联、依赖、重复三种关联类型
- **延期标记**：开发工程师可在进行中状态标记延期并填写延期原因
- **自动指派**：评审通过后自动指派项目测试主责，重新打开后自动指派开发工程师

### Sprint/迭代管理

- 项目经理创建 Sprint（名称、目标、开始日期、结束日期）
- Sprint 状态：计划中 → 进行中 → 已完成
- 任务可关联 Sprint，支持 Sprint 级看板和统计

### 任务检索与过滤器

- **多维度检索**：项目、状态、类型、优先级、指派人、创建人、Sprint、创建时间范围、到期时间范围、延期标记
- **我的任务**：快速查看当前用户相关的任务
- **保存过滤器**：将常用查询条件保存为过滤器，默认私有

### 通知系统

- **站内信**：必选，所有通知场景均推送站内信
- **邮件通知**：用户可自主开关
- **通知场景**：任务分配、状态变更、评审打回、重新打开、@提及等

### 操作历史

- 记录任务所有字段变更（状态、指派人、优先级等）
- 记录操作人、操作时间、变更前后值，支持完整追溯

### 评论

- 任务支持评论，方便团队沟通协作

### 看板与统计

- **看板视图**：按状态列展示任务卡片，支持拖拽变更状态，支持项目级和 Sprint 级看板
- **项目概览**：各状态任务数量、完成趋势
- **Sprint 燃尽图**：迭代进度可视化
- **成员工作量统计**：团队成员任务分布
- **缺陷统计**：缺陷数量与解决情况

### 字典管理

- 管理任务类型、优先级、状态等字典数据
- 支持字典类型和字典数据的增删改查，灵活扩展

## 任务工作流

```
                    ┌──────────────────────────────────────────┐
                    │              重新打开                      │
                    │  (从已完成/已关闭 → 未开始，自动指派开发工程师)  │
                    ▼                                          │
  [创建] → 未开始 ──→ 进行中 ──→ 待评审 ──→ 待测试 ──→ 已完成 ──→ 已关闭
            │          │          │                    │
            │          │          │ 评审不通过          │ 提交缺陷子任务
            │          │          │ (打回进行中)         │ (父任务保持待测试)
            │          │          ◄────────────────────┘
            │          │
            │          ├─→ 标记延期(进行中+is_delayed=true)
            │          │
            ▼          ▼
           取消        取消
```

## 快速开始

### 环境要求

- JDK 17+
- MySQL 8.0+
- Maven 3.6+

### 安装与配置

1. **克隆项目**

```bash
git clone https://github.com/dkbnull/hello-flow-server.git
cd hello-flow-server
```

2. **初始化数据库**

执行 [init.sql](docs/init.sql) 创建数据库和表结构：

```bash
mysql -u root -p < docs/init.sql
```

3. **修改配置**

编辑 `hello-flow-app/src/main/resources/application.yaml`，配置数据库连接和 JWT 密钥：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hello_flow?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:root}

jwt:
  secret: ${JWT_SECRET:your-secret-key-at-least-256-bits-long}
```

也可通过环境变量 `DB_USERNAME`、`DB_PASSWORD`、`JWT_SECRET` 配置。

4. **构建项目**

```bash
./mvnw clean package -DskipTests
```

5. **运行服务**

```bash
java -jar hello-flow-app/target/hello-flow-app-0.0.1-SNAPSHOT.jar
```

或使用 Maven 直接运行：

```bash
./mvnw spring-boot:run -pl hello-flow-app
```

服务启动后访问：
- 应用地址：`http://localhost:8080`
- API 文档：`http://localhost:8080/swagger-ui.html`

### 默认账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | admin123 | 管理员 |

> 首次部署需将数据库中 admin 用户的密码替换为 BCrypt 加密值。

## API 概览

| 模块 | 路径前缀 | 说明 |
|------|---------|------|
| 认证 | `/api/auth` | 登录、登出、刷新 Token |
| 用户 | `/api/users` | 用户 CRUD、角色分配 |
| 角色 | `/api/roles` | 角色查询 |
| 项目 | `/api/projects` | 项目 CRUD、成员管理 |
| 任务 | `/api/tasks` | 任务 CRUD、状态流转、评论、历史 |
| 迭代 | `/api/sprints` | Sprint CRUD |
| 过滤器 | `/api/filters` | 保存/查询过滤器 |
| 通知 | `/api/notifications` | 通知查询、已读标记 |
| 统计 | `/api/board-stats` | 看板统计 |
| 字典 | `/api/dicts` | 字典类型与数据管理 |

认证方式：`Authorization: Bearer {accessToken}`

详细接口文档参见 [API.md](docs/API.md)。

## 许可证

[Apache License 2.0](LICENSE)
