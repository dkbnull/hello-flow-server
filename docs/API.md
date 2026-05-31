# HelloFlow API 接口文档

## 通用说明

### 基础信息
- 基础路径：`/api`
- 认证方式：Bearer Token（JWT）
- 请求头：`Authorization: Bearer {accessToken}`

### 统一响应格式

所有接口统一返回 HTTP 200，通过业务码 `code` 区分成功与失败。

成功响应（code=0）：
```json
{
    "code": 0,
    "message": "操作成功",
    "data": {}
}
```

失败响应（code≠0）：
```json
{
    "code": 20004,
    "message": "用户名或密码错误",
    "data": null
}
```

### 分页响应格式

```json
{
    "code": 0,
    "message": "操作成功",
    "data": {
        "records": [],
        "total": 100,
        "size": 20,
        "current": 1,
        "pages": 5
    }
}
```

### 错误码

| 错误码 | 说明 |
|--------|------|
| 0 | 操作成功 |
| 10001 | 参数校验失败 |
| 20001 | 未认证 |
| 20002 | Token已过期 |
| 20003 | Token无效 |
| 20004 | 用户名或密码错误 |
| 20005 | 账号已被禁用 |
| 30001 | 权限不足 |
| 40001 | 资源不存在 |
| 40002 | 用户不存在 |
| 40003 | 项目不存在 |
| 40004 | 任务不存在 |
| 40005 | 迭代不存在 |
| 40006 | 角色不存在 |
| 50001 | 用户名已存在 |
| 50002 | 项目成员已存在 |
| 50003 | 任务关联已存在 |
| 50004 | 业务处理失败 |
| 50005 | 任务状态流转不合法 |
| 50006 | 不能评审自己的任务 |
| 50013 | 项目经理不能评审任务 |
| 50007 | 任务已取消 |
| 50008 | 项目已归档 |
| 50009 | 用户职位与项目角色不匹配 |
| 50010 | 用户不是项目成员 |
| 50011 | 项目编码已存在 |
| 90001 | 服务内部错误 |
| 90003 | 数据库操作失败 |

---

## 1. 认证管理

### 1.1 登录

- **请求方法**：POST
- **请求路径**：`/api/auth/login`
- **认证方式**：无需认证

**请求参数**（Body - JSON）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | string | 是 | 登录名 |
| password | string | 是 | 密码 |

**请求示例**：
```json
{
    "username": "admin",
    "password": "admin123"
}
```

**响应参数**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| code | int | 业务码，0表示成功 |
| data.accessToken | string | 访问令牌 |
| data.refreshToken | string | 刷新令牌 |
| data.expiresIn | long | 过期时间（毫秒） |
| data.user.id | long | 用户ID |
| data.user.username | string | 用户名 |
| data.user.nickname | string | 昵称 |
| data.user.avatar | string | 头像URL |
| data.user.positionCode | string | 职位编码（PM/DEV/QA） |
| data.user.roles | string[] | 角色编码列表 |

**响应示例**：
```json
{
    "code": 0,
    "message": "操作成功",
    "data": {
        "accessToken": "eyJhbGciOi...",
        "refreshToken": "eyJhbGciOi...",
        "expiresIn": 86400000,
        "user": {
            "id": 1,
            "username": "admin",
            "nickname": "管理员",
            "avatar": null,
            "positionCode": null,
            "roles": ["ADMIN"]
        }
    }
}
```

### 1.2 登出

- **请求方法**：POST
- **请求路径**：`/api/auth/logout`
- **认证方式**：Bearer Token

**响应参数**：无data

### 1.3 刷新Token

- **请求方法**：POST
- **请求路径**：`/api/auth/refresh`
- **认证方式**：无需认证

**请求参数**（Body - JSON）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| refreshToken | string | 是 | 刷新令牌 |

**响应参数**：同登录响应

---

## 2. 用户管理

### 2.1 用户列表

- **请求方法**：GET
- **请求路径**：`/api/users`
- **认证方式**：Bearer Token（需ADMIN角色）

**请求参数**（Query）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| keyword | string | 否 | 搜索关键词（用户名/昵称/邮箱） |
| status | int | 否 | 状态（0-禁用 1-启用） |
| positionId | long | 否 | 职位ID |
| page | int | 否 | 页码，默认1 |
| pageSize | int | 否 | 每页数量，默认20 |

**响应参数**（分页）：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| data.records[].id | long | 用户ID |
| data.records[].username | string | 登录名 |
| data.records[].nickname | string | 昵称 |
| data.records[].email | string | 邮箱 |
| data.records[].phone | string | 手机号 |
| data.records[].avatar | string | 头像URL |
| data.records[].positionId | long | 职位ID |
| data.records[].positionName | string | 职位名称 |
| data.records[].positionCode | string | 职位编码 |
| data.records[].status | int | 状态 |
| data.records[].roles | string[] | 角色编码列表 |
| data.records[].createdAt | datetime | 创建时间 |

### 2.2 创建用户

- **请求方法**：POST
- **请求路径**：`/api/users`
- **认证方式**：Bearer Token（需ADMIN角色）

**请求参数**（Body - JSON）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | string | 是 | 登录名 |
| password | string | 是 | 密码 |
| nickname | string | 否 | 昵称 |
| email | string | 否 | 邮箱 |
| phone | string | 否 | 手机号 |
| positionId | long | 否 | 职位ID |
| roleId | long | 是 | 角色ID |

**响应参数**：UserVO对象

### 2.3 更新用户

- **请求方法**：PUT
- **请求路径**：`/api/users/{id}`
- **认证方式**：Bearer Token（需ADMIN角色）

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 用户ID |

**请求参数**（Body - JSON）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| nickname | string | 否 | 昵称 |
| email | string | 否 | 邮箱 |
| phone | string | 否 | 手机号 |
| positionId | long | 否 | 职位ID |
| roleId | long | 否 | 角色ID |

### 2.4 启用/禁用用户

- **请求方法**：PUT
- **请求路径**：`/api/users/{id}/status`
- **认证方式**：Bearer Token（需ADMIN角色）

**请求参数**（Body - JSON）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | int | 是 | 0-禁用 1-启用 |

### 2.5 获取当前用户信息

- **请求方法**：GET
- **请求路径**：`/api/users/me`
- **认证方式**：Bearer Token

**响应参数**：UserVO对象

### 2.6 更新个人信息

- **请求方法**：PUT
- **请求路径**：`/api/users/me`
- **认证方式**：Bearer Token

**请求参数**（Body - JSON）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| nickname | string | 否 | 昵称 |
| email | string | 否 | 邮箱 |
| phone | string | 否 | 手机号 |
| avatar | string | 否 | 头像URL |

---

## 3. 角色管理

### 3.1 角色列表

- **请求方法**：GET
- **请求路径**：`/api/roles`
- **认证方式**：Bearer Token

**响应参数**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| data[].id | long | 角色ID |
| data[].name | string | 角色名称 |
| data[].code | string | 角色编码 |
| data[].description | string | 描述 |
| data[].status | int | 状态 |

### 3.2 创建角色

- **请求方法**：POST
- **请求路径**：`/api/roles`
- **认证方式**：Bearer Token（需ADMIN角色）

**请求参数**（Body - JSON）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| name | string | 是 | 角色名称 |
| code | string | 是 | 角色编码 |
| description | string | 否 | 描述 |

### 3.3 更新角色

- **请求方法**：PUT
- **请求路径**：`/api/roles/{id}`
- **认证方式**：Bearer Token（需ADMIN角色）

**请求参数**（Body - JSON）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| name | string | 否 | 角色名称 |
| description | string | 否 | 描述 |
| status | int | 否 | 状态 |

---

## 4. 字典管理

### 4.1 字典类型列表

- **请求方法**：GET
- **请求路径**：`/api/dict-types`
- **认证方式**：Bearer Token

**请求参数**（Query）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| keyword | string | 否 | 搜索关键词 |
| page | int | 否 | 页码，默认1 |
| pageSize | int | 否 | 每页数量，默认20 |

**响应参数**（分页）：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| data.records[].id | long | 字典类型ID |
| data.records[].name | string | 字典类型名称 |
| data.records[].code | string | 字典类型编码 |
| data.records[].status | int | 状态 |
| data.records[].remark | string | 备注 |
| data.records[].createdAt | datetime | 创建时间 |

### 4.2 创建字典类型

- **请求方法**：POST
- **请求路径**：`/api/dict-types`
- **认证方式**：Bearer Token（需ADMIN角色）

**请求参数**（Body - JSON）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| name | string | 是 | 字典类型名称 |
| code | string | 是 | 字典类型编码 |
| remark | string | 否 | 备注 |

### 4.3 更新字典类型

- **请求方法**：PUT
- **请求路径**：`/api/dict-types/{id}`
- **认证方式**：Bearer Token（需ADMIN角色）

**请求参数**（Body - JSON）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| name | string | 否 | 字典类型名称 |
| remark | string | 否 | 备注 |
| status | int | 否 | 状态 |

### 4.4 字典数据列表

- **请求方法**：GET
- **请求路径**：`/api/dict-types/{typeId}/data`
- **认证方式**：Bearer Token

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| typeId | long | 是 | 字典类型ID |

**响应参数**（分页）：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| data.records[].id | long | 字典数据ID |
| data.records[].typeId | long | 字典类型ID |
| data.records[].label | string | 数据标签 |
| data.records[].value | string | 数据值 |
| data.records[].sort | int | 排序 |
| data.records[].status | int | 状态 |
| data.records[].remark | string | 备注 |

### 4.5 创建字典数据

- **请求方法**：POST
- **请求路径**：`/api/dict-types/data`
- **认证方式**：Bearer Token（需ADMIN角色）

**请求参数**（Body - JSON）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| typeId | long | 是 | 字典类型ID |
| label | string | 是 | 数据标签 |
| value | string | 是 | 数据值 |
| sort | int | 否 | 排序 |
| remark | string | 否 | 备注 |

### 4.6 更新字典数据

- **请求方法**：PUT
- **请求路径**：`/api/dict-types/data/{id}`
- **认证方式**：Bearer Token（需ADMIN角色）

**请求参数**（Body - JSON）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| label | string | 否 | 数据标签 |
| value | string | 否 | 数据值 |
| sort | int | 否 | 排序 |
| remark | string | 否 | 备注 |
| status | int | 否 | 状态 |

---

## 5. 项目管理

### 5.1 项目列表

- **请求方法**：GET
- **请求路径**：`/api/projects`
- **认证方式**：Bearer Token

**请求参数**（Query）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| keyword | string | 否 | 搜索关键词 |
| status | int | 否 | 状态（0-归档 1-进行中） |
| page | int | 否 | 页码，默认1 |
| pageSize | int | 否 | 每页数量，默认20 |

**响应参数**（分页）：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| data.records[].id | long | 项目ID |
| data.records[].code | string | 项目编码 |
| data.records[].name | string | 项目名称 |
| data.records[].description | string | 项目描述 |
| data.records[].pmId | long | 项目经理ID |
| data.records[].pmName | string | 项目经理名称 |
| data.records[].devLeadId | long | 开发主责ID |
| data.records[].devLeadName | string | 开发主责名称 |
| data.records[].testLeadId | long | 测试主责ID |
| data.records[].testLeadName | string | 测试主责名称 |
| data.records[].status | int | 状态 |
| data.records[].createdBy | long | 创建者ID |
| data.records[].createdAt | datetime | 创建时间 |

### 5.2 创建项目

- **请求方法**：POST
- **请求路径**：`/api/projects`
- **认证方式**：Bearer Token（需ADMIN角色）

**请求参数**（Body - JSON）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| code | string | 是 | 项目编码（唯一） |
| name | string | 是 | 项目名称 |
| description | string | 否 | 项目描述 |
| pmId | long | 否 | 项目经理ID（需为项目成员且职位为PM） |
| devLeadId | long | 否 | 开发主责ID（需为项目成员且职位为DEV） |
| testLeadId | long | 否 | 测试主责ID（需为项目成员且职位为QA） |

**校验规则**：
- 项目经理（pmId）对应用户的职位必须为 PM
- 开发主责（devLeadId）对应用户的职位必须为 DEV
- 测试主责（testLeadId）对应用户的职位必须为 QA
- 更新项目时，被选用户还必须是项目成员

### 5.3 项目详情

- **请求方法**：GET
- **请求路径**：`/api/projects/{id}`
- **认证方式**：Bearer Token

**响应参数**：ProjectVO对象（含成员列表）

### 5.4 更新项目

- **请求方法**：PUT
- **请求路径**：`/api/projects/{id}`
- **认证方式**：Bearer Token

**请求参数**（Body - JSON）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| code | string | 否 | 项目编码（唯一） |
| name | string | 否 | 项目名称 |
| description | string | 否 | 项目描述 |
| pmId | long | 否 | 项目经理ID（需为项目成员且职位为PM） |
| devLeadId | long | 否 | 开发主责ID（需为项目成员且职位为DEV） |
| testLeadId | long | 否 | 测试主责ID（需为项目成员且职位为QA） |
| status | int | 否 | 状态（0-归档 1-进行中） |

### 5.5 项目成员列表

- **请求方法**：GET
- **请求路径**：`/api/projects/{id}/members`
- **认证方式**：Bearer Token

**请求参数**（Query）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| positionCode | string | 否 | 职位编码筛选（PM/DEV/QA） |

**响应参数**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| data[].userId | long | 用户ID |
| data[].username | string | 用户名 |
| data[].nickname | string | 昵称 |
| data[].positionCode | string | 职位编码 |
| data[].positionName | string | 职位名称 |
| data[].joinedAt | datetime | 加入时间 |

### 5.6 添加项目成员

- **请求方法**：POST
- **请求路径**：`/api/projects/{id}/members`
- **认证方式**：Bearer Token

**请求参数**（Query）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | long | 是 | 用户ID |

### 5.7 移除项目成员

- **请求方法**：DELETE
- **请求路径**：`/api/projects/{id}/members/{userId}`
- **认证方式**：Bearer Token

---

## 6. Sprint管理

### 6.1 Sprint列表

- **请求方法**：GET
- **请求路径**：`/api/projects/{projectId}/sprints`
- **认证方式**：Bearer Token

**响应参数**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| data[].id | long | Sprint ID |
| data[].projectId | long | 项目ID |
| data[].name | string | Sprint名称 |
| data[].goal | string | Sprint目标 |
| data[].status | int | 状态（1-计划中 2-进行中 3-已完成） |
| data[].startDate | date | 开始日期 |
| data[].endDate | date | 结束日期 |
| data[].createdBy | long | 创建者ID |
| data[].createdAt | datetime | 创建时间 |

### 6.2 创建Sprint

- **请求方法**：POST
- **请求路径**：`/api/projects/{projectId}/sprints`
- **认证方式**：Bearer Token

**请求参数**（Body - JSON）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| name | string | 是 | Sprint名称 |
| goal | string | 否 | Sprint目标 |
| startDate | string | 否 | 开始日期（yyyy-MM-dd） |
| endDate | string | 否 | 结束日期（yyyy-MM-dd） |

### 6.3 更新Sprint

- **请求方法**：PUT
- **请求路径**：`/api/sprints/{id}`
- **认证方式**：Bearer Token

**请求参数**（Body - JSON）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| name | string | 否 | Sprint名称 |
| goal | string | 否 | Sprint目标 |
| startDate | string | 否 | 开始日期 |
| endDate | string | 否 | 结束日期 |

### 6.4 开始Sprint

- **请求方法**：PUT
- **请求路径**：`/api/sprints/{id}/start`
- **认证方式**：Bearer Token

### 6.5 完成Sprint

- **请求方法**：PUT
- **请求路径**：`/api/sprints/{id}/complete`
- **认证方式**：Bearer Token

---

## 7. 任务管理

### 7.1 任务列表

- **请求方法**：GET
- **请求路径**：`/api/projects/{projectId}/tasks`
- **认证方式**：Bearer Token

**请求参数**（Query）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | int | 否 | 状态（1-未开始 2-进行中 3-待评审 4-待测试 5-已完成 6-已关闭 7-取消） |
| type | int | 否 | 类型（1-需求 2-完善 3-缺陷） |
| priority | int | 否 | 优先级（1-最低 2-低 3-中 4-高 5-最高） |
| assigneeId | long | 否 | 指派人ID |
| reporterId | long | 否 | 创建人ID |
| sprintId | long | 否 | Sprint ID |
| isDelayed | int | 否 | 延期标记（0-否 1-是） |
| dueDateStart | string | 否 | 到期日期起始（yyyy-MM-dd） |
| dueDateEnd | string | 否 | 到期日期结束（yyyy-MM-dd） |
| createdAtStart | string | 否 | 创建日期起始（yyyy-MM-dd） |
| createdAtEnd | string | 否 | 创建日期结束（yyyy-MM-dd） |
| page | int | 否 | 页码，默认1 |
| pageSize | int | 否 | 每页数量，默认20 |

**响应参数**（分页）：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| data.records[].id | long | 任务ID |
| data.records[].projectId | long | 项目ID |
| data.records[].taskCode | string | 任务编码（项目编码-序号，如HELLO-0001） |
| data.records[].sprintId | long | Sprint ID |
| data.records[].parentId | long | 父任务ID |
| data.records[].type | int | 类型 |
| data.records[].title | string | 标题 |
| data.records[].description | string | 描述 |
| data.records[].status | int | 状态 |
| data.records[].priority | int | 优先级 |
| data.records[].assigneeId | long | 指派人ID |
| data.records[].assigneeName | string | 指派人名称 |
| data.records[].reporterId | long | 创建人ID |
| data.records[].reporterName | string | 创建人名称 |
| data.records[].developerId | long | 开发工程师ID |
| data.records[].developerName | string | 开发工程师名称 |
| data.records[].testerId | long | 测试工程师ID |
| data.records[].testerName | string | 测试工程师名称 |
| data.records[].dueDate | date | 到期时间 |
| data.records[].isDelayed | int | 延期标记 |
| data.records[].delayReason | string | 延期原因 |
| data.records[].createdAt | datetime | 创建时间 |

### 7.2 创建任务

- **请求方法**：POST
- **请求路径**：`/api/projects/{projectId}/tasks`
- **认证方式**：Bearer Token

**请求参数**（Body - JSON）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| title | string | 是 | 标题 |
| description | string | 否 | 描述（富文本） |
| type | int | 否 | 类型（1-需求 2-完善 3-缺陷），默认1 |
| priority | int | 否 | 优先级（1-5），默认3 |
| assigneeId | long | 否 | 指派人ID，未指定则自动指派开发主责 |
| developerId | long | 否 | 开发工程师ID |
| testerId | long | 否 | 测试工程师ID |
| dueDate | string | 否 | 到期时间（yyyy-MM-dd） |
| sprintId | long | 否 | Sprint ID |
| parentId | long | 否 | 父任务ID |
| relatedTaskId | long | 否 | 关联任务ID |
| relationType | int | 否 | 关联类型（1-关联 2-依赖 3-重复） |

**说明**：创建任务时，系统自动生成任务编码（格式：项目编码-序号，如 `HELLO-0001`），序号在项目内自增。

### 7.3 任务详情

- **请求方法**：GET
- **请求路径**：`/api/tasks/{id}`
- **认证方式**：Bearer Token

**响应参数**：TaskVO对象（含子任务列表和关联列表）

### 7.4 更新任务

- **请求方法**：PUT
- **请求路径**：`/api/tasks/{id}`
- **认证方式**：Bearer Token

**请求参数**（Body - JSON）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| title | string | 否 | 标题 |
| description | string | 否 | 描述 |
| type | int | 否 | 类型 |
| priority | int | 否 | 优先级 |
| dueDate | string | 否 | 到期时间 |
| planStartDate | string | 否 | 计划开始日期 |
| planEndDate | string | 否 | 计划结束日期 |
| sprintId | long | 否 | Sprint ID |

### 7.5 删除任务

- **请求方法**：DELETE
- **请求路径**：`/api/tasks/{id}`
- **认证方式**：Bearer Token（仅创建者可删除）

### 7.6 分配任务

- **请求方法**：POST
- **请求路径**：`/api/tasks/{id}/assign`
- **认证方式**：Bearer Token

**请求参数**（Body - JSON）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| assigneeId | long | 否 | 指派人ID |

### 7.7 开始开发

- **请求方法**：POST
- **请求路径**：`/api/tasks/{id}/start`
- **认证方式**：Bearer Token

**前置条件**：任务状态为「未开始」

### 7.8 开发完成

- **请求方法**：POST
- **请求路径**：`/api/tasks/{id}/complete-dev`
- **认证方式**：Bearer Token

**前置条件**：任务状态为「进行中」

### 7.9 评审通过

- **请求方法**：POST
- **请求路径**：`/api/tasks/{id}/review-pass`
- **认证方式**：Bearer Token

**前置条件**：任务状态为「待评审」，项目经理不允许评审，开发工程师不能评审自己的任务（项目中只有自己一个开发工程师时除外）

### 7.10 评审不通过

- **请求方法**：POST
- **请求路径**：`/api/tasks/{id}/review-reject`
- **认证方式**：Bearer Token

**前置条件**：任务状态为「待评审」，项目经理不允许评审，开发工程师不能评审自己的任务（项目中只有自己一个开发工程师时除外）

### 7.11 测试通过

- **请求方法**：POST
- **请求路径**：`/api/tasks/{id}/test-pass`
- **认证方式**：Bearer Token

**前置条件**：任务状态为「待测试」

### 7.12 测试不通过

- **请求方法**：POST
- **请求路径**：`/api/tasks/{id}/test-reject`
- **认证方式**：Bearer Token

**前置条件**：任务状态为「待测试」

**说明**：测试不通过后，任务状态回退为「进行中」，指派回开发工程师，并通知开发工程师修改。

### 7.13 重新打开

- **请求方法**：POST
- **请求路径**：`/api/tasks/{id}/reopen`
- **认证方式**：Bearer Token

**前置条件**：任务状态为「已完成」或「已关闭」

### 7.14 关闭任务

- **请求方法**：POST
- **请求路径**：`/api/tasks/{id}/close`
- **认证方式**：Bearer Token

**前置条件**：任务状态为「已完成」

### 7.15 取消任务

- **请求方法**：POST
- **请求路径**：`/api/tasks/{id}/cancel`
- **认证方式**：Bearer Token

**前置条件**：任务状态为「未开始」或「进行中」

**请求参数**（Body - JSON）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| cancelReason | string | 否 | 取消原因 |

### 7.16 标记延期

- **请求方法**：POST
- **请求路径**：`/api/tasks/{id}/delay`
- **认证方式**：Bearer Token

**前置条件**：任务状态为「进行中」

**请求参数**（Body - JSON）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| delayReason | string | 否 | 延期原因 |

### 7.17 子任务列表

- **请求方法**：GET
- **请求路径**：`/api/tasks/{id}/subtasks`
- **认证方式**：Bearer Token

### 7.18 创建子任务

- **请求方法**：POST
- **请求路径**：`/api/tasks/{id}/subtasks`
- **认证方式**：Bearer Token

**请求参数**：同创建任务

### 7.19 任务关联列表

- **请求方法**：GET
- **请求路径**：`/api/tasks/{id}/relations`
- **认证方式**：Bearer Token

**响应参数**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| data[].id | long | 关联ID |
| data[].relatedTaskId | long | 关联任务ID |
| data[].relatedTaskTitle | string | 关联任务标题 |
| data[].relationType | int | 关联类型（1-关联 2-依赖 3-重复） |

### 7.20 添加任务关联

- **请求方法**：POST
- **请求路径**：`/api/tasks/{id}/relations`
- **认证方式**：Bearer Token

**请求参数**（Body - JSON）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| relatedTaskId | long | 是 | 关联任务ID |
| relationType | int | 是 | 关联类型（1-关联 2-依赖 3-重复） |

### 7.21 删除任务关联

- **请求方法**：DELETE
- **请求路径**：`/api/tasks/{taskId}/relations/{relationId}`
- **认证方式**：Bearer Token

### 7.22 我的任务

- **请求方法**：GET
- **请求路径**：`/api/tasks/mine`
- **认证方式**：Bearer Token

**请求参数**（Query）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | int | 否 | 状态（1-未开始 2-进行中 3-待评审 4-待测试 5-已完成 6-已关闭 7-取消） |
| type | int | 否 | 类型（1-需求 2-Bug 3-改进 4-任务） |
| priority | int | 否 | 优先级（1-低 2-中 3-高 4-紧急） |
| keyword | string | 否 | 关键词（模糊搜索标题） |
| page | int | 否 | 页码，默认1 |
| pageSize | int | 否 | 每页数量，默认20 |

### 7.23 我创建的任务

- **请求方法**：GET
- **请求路径**：`/api/tasks/reported`
- **认证方式**：Bearer Token

**请求参数**：同我的任务

### 7.24 与我相关的任务

- **请求方法**：GET
- **请求路径**：`/api/tasks/related`
- **认证方式**：Bearer Token

**请求参数**：同我的任务

**说明**：返回当前用户作为负责人（assigneeId）、创建人（reporterId）、开发工程师（developerId）、测试工程师（testerId）中任一角色的任务列表。

**响应参数**：同任务列表（分页）

---

## 8. 评论

### 8.1 评论列表

- **请求方法**：GET
- **请求路径**：`/api/tasks/{id}/comments`
- **认证方式**：Bearer Token

**响应参数**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| data[].id | long | 评论ID |
| data[].taskId | long | 任务ID |
| data[].userId | long | 评论人ID |
| data[].username | string | 评论人用户名 |
| data[].nickname | string | 评论人昵称 |
| data[].avatar | string | 头像URL |
| data[].content | string | 评论内容 |
| data[].createdAt | datetime | 创建时间 |

### 8.2 添加评论

- **请求方法**：POST
- **请求路径**：`/api/tasks/{id}/comments`
- **认证方式**：Bearer Token

**请求参数**（Body - JSON）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| content | string | 是 | 评论内容 |

---

## 9. 操作历史

### 9.1 操作历史列表

- **请求方法**：GET
- **请求路径**：`/api/tasks/{id}/activities`
- **认证方式**：Bearer Token

**响应参数**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| data[].id | long | 历史ID |
| data[].taskId | long | 任务ID |
| data[].userId | long | 操作人ID |
| data[].username | string | 操作人名称 |
| data[].action | string | 操作类型中文名称（创建/分配/状态变更/优先级变更/更新/删除/标记延期/取消/重新打开/关闭/测试不通过） |
| data[].field | string | 变更字段 |
| data[].oldValue | string | 旧值 |
| data[].newValue | string | 新值 |
| data[].description | string | 操作描述（如"状态：进行中 → 待评审"） |
| data[].createdAt | datetime | 操作时间 |

---

## 10. 看板与统计

### 10.1 项目看板

- **请求方法**：GET
- **请求路径**：`/api/projects/{projectId}/board`
- **认证方式**：Bearer Token

**响应参数**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| data.columns[].status | int | 状态码 |
| data.columns[].statusName | string | 状态名称 |
| data.columns[].tasks[].id | long | 任务ID |
| data.columns[].tasks[].title | string | 标题 |
| data.columns[].tasks[].type | int | 类型 |
| data.columns[].tasks[].priority | int | 优先级 |
| data.columns[].tasks[].isDelayed | int | 延期标记 |
| data.columns[].tasks[].assigneeId | long | 指派人ID |
| data.columns[].tasks[].dueDate | string | 到期时间 |

### 10.2 Sprint看板

- **请求方法**：GET
- **请求路径**：`/api/sprints/{sprintId}/board`
- **认证方式**：Bearer Token

**响应参数**：同项目看板

### 10.3 项目概览统计

- **请求方法**：GET
- **请求路径**：`/api/projects/{projectId}/stats/overview`
- **认证方式**：Bearer Token

**响应参数**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| data.totalTasks | long | 任务总数 |
| data.completedTasks | long | 已完成数 |
| data.completionRate | double | 完成率（%） |
| data.statusDistribution | map | 各状态任务数量 |

### 10.4 燃尽图数据

- **请求方法**：GET
- **请求路径**：`/api/projects/{projectId}/stats/burndown`
- **认证方式**：Bearer Token

**请求参数**（Query）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sprintId | long | 是 | Sprint ID |

**响应参数**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| data.sprintName | string | Sprint名称 |
| data.startDate | string | 开始日期 |
| data.endDate | string | 结束日期 |
| data.totalPoints | int | 总任务数 |
| data.actual | array | 实际燃尽线 |
| data.ideal | array | 理想燃尽线 |

### 10.5 成员工作量统计

- **请求方法**：GET
- **请求路径**：`/api/projects/{projectId}/stats/members`
- **认证方式**：Bearer Token

**响应参数**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| data[].userId | long | 用户ID |
| data[].username | string | 用户名 |
| data[].nickname | string | 昵称 |
| data[].totalTasks | long | 任务总数 |
| data[].completedTasks | long | 已完成数 |
| data[].inProgressTasks | long | 进行中数 |
| data[].todoTasks | long | 未开始数 |

### 10.6 缺陷统计

- **请求方法**：GET
- **请求路径**：`/api/projects/{projectId}/stats/defects`
- **认证方式**：Bearer Token

**响应参数**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| data.totalDefects | long | 缺陷总数 |
| data.openDefects | long | 未解决缺陷数 |
| data.closedDefects | long | 已解决缺陷数 |
| data.fixRate | double | 修复率（%） |

---

## 11. 通知管理

### 11.1 通知列表

- **请求方法**：GET
- **请求路径**：`/api/notifications`
- **认证方式**：Bearer Token

**请求参数**（Query）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| isRead | int | 否 | 已读状态（0-未读 1-已读） |
| page | int | 否 | 页码，默认1 |
| pageSize | int | 否 | 每页数量，默认20 |

**响应参数**（分页）：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| data.records[].id | long | 通知ID |
| data.records[].userId | long | 接收人ID |
| data.records[].title | string | 通知标题 |
| data.records[].content | string | 通知内容 |
| data.records[].type | int | 类型（1-任务分配 2-状态变更 3-重新打开 4-评论 5-系统通知） |
| data.records[].relatedId | long | 关联业务ID |
| data.records[].isRead | int | 已读状态 |
| data.records[].createdAt | datetime | 创建时间 |

### 11.2 未读数量

- **请求方法**：GET
- **请求路径**：`/api/notifications/unread-count`
- **认证方式**：Bearer Token

**响应参数**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| data | long | 未读数量 |

### 11.3 标记已读

- **请求方法**：PUT
- **请求路径**：`/api/notifications/{id}/read`
- **认证方式**：Bearer Token

### 11.4 全部已读

- **请求方法**：PUT
- **请求路径**：`/api/notifications/read-all`
- **认证方式**：Bearer Token

### 11.5 获取通知设置

- **请求方法**：GET
- **请求路径**：`/api/notification-settings`
- **认证方式**：Bearer Token

**响应参数**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| data.userId | long | 用户ID |
| data.emailEnabled | int | 邮件通知开关（0-关闭 1-开启） |

### 11.6 更新通知设置

- **请求方法**：PUT
- **请求路径**：`/api/notification-settings`
- **认证方式**：Bearer Token

**请求参数**（Body - JSON）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| emailEnabled | int | 否 | 邮件通知开关（0-关闭 1-开启） |

---

## 12. 过滤器管理

### 12.1 过滤器列表

- **请求方法**：GET
- **请求路径**：`/api/filters`
- **认证方式**：Bearer Token

**响应参数**：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| data[].id | long | 过滤器ID |
| data[].userId | long | 所属用户ID |
| data[].name | string | 过滤器名称 |
| data[].conditions | string | 查询条件（JSON格式） |
| data[].isDefault | int | 是否默认 |
| data[].createdAt | datetime | 创建时间 |

### 12.2 创建过滤器

- **请求方法**：POST
- **请求路径**：`/api/filters`
- **认证方式**：Bearer Token

**请求参数**（Body - JSON）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| name | string | 是 | 过滤器名称 |
| conditions | string | 否 | 查询条件（JSON格式） |
| isDefault | int | 否 | 是否默认，默认0 |

**conditions JSON示例**：
```json
{
    "projectId": 1,
    "sprintId": 3,
    "status": [1, 2],
    "type": [1, 3],
    "priority": [4, 5],
    "assigneeId": 10,
    "reporterId": 10,
    "isDelayed": false,
    "dueDateStart": "2026-01-01",
    "dueDateEnd": "2026-12-31"
}
```

### 12.3 更新过滤器

- **请求方法**：PUT
- **请求路径**：`/api/filters/{id}`
- **认证方式**：Bearer Token

**请求参数**（Body - JSON）：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| name | string | 否 | 过滤器名称 |
| conditions | string | 否 | 查询条件 |
| isDefault | int | 否 | 是否默认 |

### 12.4 删除过滤器

- **请求方法**：DELETE
- **请求路径**：`/api/filters/{id}`
- **认证方式**：Bearer Token
