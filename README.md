## AI花卉识别APP-花语AI：设计与实现报告

---

[toc]

---

## 1. 项目概述

### 1.1 项目主题

通过手机摄像头和 AI 图像识别技术，当用户拍摄花朵照片后，APP 并能快速识别花名、分类、生长习性等信息，为用户提供浇水、施肥、光照等养护建议。
前后端分离，后端存储花卉养护知识数据，前端方便用户查看养护方案。花卉识别 AI 模型可以使用预训练好的模型。

### 1.2 项目目标

- **多模态交互**：支持**文本+图片的AI对话**模式
- **流式响应**：采用SSE技术实现实时对话体验
- **模块化设计**：前后端分离，后端采用多模块架构
- **现代化技术栈**：使用Spring AI、Android原生开发等前沿技术
- **完整的用户体系**：包含注册、登录、会话管理等完整功能

---

## 2. 技术栈与开发环境

### 2.1 前端技术栈

| 技术/框架 | 版本 | 说明 |
|----------|------|------|
| Android SDK | API 34 | Android应用开发平台 |
| Java | 17 | 主要开发语言 |
| Retrofit | 2.9.0 | HTTP客户端库 |
| OkHttp | 4.9.3 | 网络请求库 |
| Glide | 4.12.0 | 图片加载库 |
| Gson | 2.9.0 | JSON解析库 |
| Lombok | 1.18.30 | 代码简化工具 |

| 主要组件 | 说明 |
|----------|-----------|
| RecyclerView |  列表展示组件 |
| DrawerLayout |  侧滑菜单组件 |
| NavigationView | 导航菜单组件 |
| CardView |  卡片布局组件 |
| ConstraintLayout | 约束布局组件 |

### 2.2 后端技术栈

| 技术/框架 | 版本 | 说明 |
|----------|------|------|
| Spring Boot | 3.5.3 | 后端开发框架 |
| Spring AI | 1.0.0 稳定版 | AI集成框架 |
| MyBatis Plus | 3.5.10.1 | ORM框架 |
| MySQL | 8.0+ | 关系型数据库 |
| JWT | 0.12.5 | 身份认证 |
| Hutool | 5.7.17 | Java工具库 |
| Lombok | 1.18.30 | 代码简化工具 |
| **AI**大模型 | qwen-max、qwen-omni-turbo、deepseek-v3 | 百炼平台 |

### 2.3 开发环境

| 工具 | 版本 | 说明 |
|------|------|------|
| Android Studio | 2025.1.1 | Android开发IDE |
| IntelliJ IDEA | 2025.1.2 | Java后端开发IDE |
| Maven | 3.9.9 | 后端项目构建工具 |
| Gradle | 8.13 | 安卓项目构建工具 |
| Java | OpenJDK 17 | Java开发环境 |
| Apifox | 2.7.15 | 本地API测试工具 |
| Git | 2.46.0.windows.1 | 版本控制工具 |

---

## 3. 项目架构设计

### 3.1 整体架构

本项目采用前后端分离的架构设计，**前端为Android原生应用**，**后端为Spring Boot多模块项目**。两者通过RESTful API进行通信，支持**JSON**数据交换和**SSE流式**响应。

**整体架构图**（简化图，仅保留核定逻辑和关键处）

![img](assets/整体架构图.png)

### 3.2 前端架构

前端采用传统的MVC架构模式：

- **Model层**：数据模型类，包括User、Message、Conversation等
- **View层**：Activity、Fragment、Layout等UI组件
- **Controller层**：Activity中的业务逻辑处理
- **网络层**：Retrofit接口定义和API调用
- **工具层**：SharedPreferences管理、图片处理等工具类

**主要组件：**

- `MainActivity`：主界面，包含聊天功能和侧滑菜单
- `LoginActivity`：用户登录界面
- `RegisterActivity`：用户注册界面
- `ConversationManagementActivity`：会话管理界面
- `FlowerGalleryActivity`：花卉图库界面
- `ChatMessageAdapter`：聊天消息列表适配器
- `ApiService`：网络接口定义

### 3.3 后端架构

后端采用Spring Boot多模块架构：

- **aiserver-parent**：父模块，管理依赖版本
- **aiserver-common**：公共模块，包含实体类、VO、DTO、工具类
- **aiserver-core**：核心业务模块，包含Service、Mapper等
- **aiserver-web**：Web层模块，包含Controller和配置

**分层架构：**

- **Controller层**：处理HTTP请求，参数校验
- **Service层**：业务逻辑处理
- **Mapper层**：数据访问层
- **Entity层**：数据库实体类

### 3.4 数据库设计

数据库采用MySQL，包含以下主要表和关系：

<img src="assets/db概览.png" alt="image-20250906152121306" style="zoom: 67%;" />

1. `user`表和`conversation`表及其关系

<img src="assets/db1.png" alt="image-20250906151130997" style="zoom: 33%;" />

2. `conversation`表和`message`表及其关系：

   <img src="assets/db2.png" alt="image-20250906151351438" style="zoom: 33%;" />

3. 图库表`gallery`表：

   <img src="assets/db3.png" alt="image-20250906151434728" style="zoom: 50%;" />

4. 最后是`spring_ai_chat_memory`表，该表并不是我们手动确定的，而是在SpringAI框架中，使用自动的jdbc持久化聊天记录特性，以及在配置了yml文件的数据库平台、初始化指定sql文件后**自动生成**的表（详见SpringAI官方文档：[JdbcChatMemoryRepository](https://docs.spring.io/spring-ai/reference/api/chat-memory.html#_jdbcchatmemoryrepository)）

   <img src="assets/db4.png" alt="image-20250906151629943" style="zoom: 50%;" />

> **表结构上述截图已经很清晰全面展示，这里不再粘贴文本**

---

## 4. 项目目录结构

### 4.1 前端目录结构

<img src="assets/前端项目结构截图.png" alt="image-20250906161934098" style="zoom: 50%;" />

```yaml
AiClient/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/xyz/qiquqiu/aiclient/
│   │   │   │   ├── MainActivity.java                    # 主界面Activity
│   │   │   │   ├── LoginActivity.java                   # 登录界面
│   │   │   │   ├── RegisterActivity.java                # 注册界面
│   │   │   │   ├── MyProfileActivity.java               # 个人资料界面
│   │   │   │   ├── ChangePasswordActivity.java          # 修改密码界面
│   │   │   │   ├── ConversationManagementActivity.java  # 会话管理界面
│   │   │   │   ├── FlowerGalleryActivity.java           # 花卉图库界面
│   │   │   │   ├── adapter/                             # 适配器包
│   │   │   │   │   ├── ChatMessageAdapter.java         # 聊天消息适配器
│   │   │   │   │   └── FlowerGalleryAdapter.java       # 花卉图库适配器
│   │   │   │   ├── api/                                 # API接口包
│   │   │   │   │   ├── ApiService.java                 # Retrofit接口定义
│   │   │   │   │   └── model/                          # 数据模型包
│   │   │   │   │       ├── BaseResult.java             # 通用响应基类
│   │   │   │   │       ├── Conversation.java           # 会话模型
│   │   │   │   │       ├── Message.java                # 消息模型
│   │   │   │   │       ├── FlowerImage.java            # 花卉图片模型
│   │   │   │   │       ├── LoginRequest.java           # 登录请求模型
│   │   │   │   │       ├── LoginResult.java            # 登录响应模型
│   │   │   │   │       └── ...
│   │   │   │   └── util/                               # 工具类包
│   │   │   │       ├── ApiClient.java                  # API客户端配置
│   │   │   │       └── SharedPreferencesManager.java  # 本地存储管理
│   │   │   ├── res/                                     # 资源文件
│   │   │   │   ├── layout/                             # 布局文件
│   │   │   │   ├── drawable/                           # 图片资源
│   │   │   │   ├── values/                             # 值资源
│   │   │   │   └── menu/                               # 菜单资源
│   │   │   └── AndroidManifest.xml                     # 应用清单文件
│   │   ├── androidTest/                                # Android测试
│   │   └── test/                                       # 单元测试
│   ├── build.gradle.kts                               # 应用构建脚本
│   └── proguard-rules.pro                             # 混淆规则
├── gradle/                                            # Gradle配置
├── build.gradle.kts                                   # 项目构建脚本
└── settings.gradle.kts                               # 项目设置
```

### 4.2 后端目录结构

```yaml
AiServer/
├── aiserver-parent/                                   # 父模块
│   └── pom.xml                                        # 父POM文件
├── aiserver-common/                                   # 公共模块
│   ├── src/main/java/xyz/qiquqiu/aiserver/common/
│   │   ├── po/                                        # 实体类
│   │   │   ├── User.java                              # 用户实体
│   │   │   ├── Conversation.java                      # 会话实体
│   │   │   ├── Message.java                           # 消息实体
│   │   │   └── Gallery.java                          # 图库实体
│   │   ├── vo/                                        # 视图对象
│   │   │   ├── UserInfoVO.java                        # 用户信息VO
│   │   │   ├── ConversationVO.java                    # 会话VO
│   │   │   ├── MessageVO.java                         # 消息VO
│   │   │   └── FlowerImageVO.java                     # 花卉图片VO
│   │   ├── dto/                                       # 数据传输对象
│   │   ├── constant/                                  # 常量类
│   │   ├── context/                                   # 上下文工具
│   │   └── util/                                      # 工具类
│   └── pom.xml
├── aiserver-core/                                     # 核心业务模块
│   ├── src/main/java/xyz/qiquqiu/aiserver/
│   │   ├── mapper/                                    # Mapper接口
│   │   │   ├── UserMapper.java                        # 用户Mapper
│   │   │   ├── ConversationMapper.java                # 会话Mapper
│   │   │   ├── MessageMapper.java                     # 消息Mapper
│   │   │   └── GalleryMapper.java                     # 图库Mapper
│   │   ├── service/                                   # 服务接口
│   │   │   ├── IUserService.java                      # 用户服务接口
│   │   │   ├── IConversationService.java              # 会话服务接口
│   │   │   ├── IMessageService.java                   # 消息服务接口
│   │   │   └── IGalleryService.java                   # 图库服务接口
│   │   ├── service/impl/                              # 服务实现
│   │   │   ├── UserServiceImpl.java                   # 用户服务实现
│   │   │   ├── ConversationServiceImpl.java           # 会话服务实现
│   │   │   ├── MessageServiceImpl.java                # 消息服务实现
│   │   │   └── GalleryServiceImpl.java                # 图库服务实现
│   │   ├── config/                                    # 配置类
│   │   └── properties/                                # 配置属性
│   ├── src/main/resources/
│   │   └── mapper/                                    # MyBatis映射文件
│   └── pom.xml
├── aiserver-web/                                      # Web层模块
│   ├── src/main/java/xyz/qiquqiu/aiserver/
│   │   ├── AiServerApplication.java                   # 启动类
│   │   ├── controller/                                # 控制器
│   │   │   ├── UserController.java                    # 用户控制器
│   │   │   ├── ChatController.java                    # 聊天控制器
│   │   │   └── GalleryController.java                 # 图库控制器
│   │   └── config/                                    # Web配置
│   ├── src/main/resources/
│   │   ├── application.yaml                          # 应用配置
│   │   ├── init-db.sql                               # 数据库初始化脚本
│   │   └── schema-mysql.sql                          # 初始化spring_ai_chat_memory
│   └── pom.xml
└── pom.xml                                           # 根POM文件
```

---

## 5. 核心功能模块实现

### 5.1 用户模块

用户模块负责用户的注册、登录、认证、信息管理等功能。

#### 5.1.1 Android端实现

**登录功能（LoginActivity）**

登录界面采用`ConstraintLayout`布局，包含用户名输入框、密码输入框和登录按钮。主要功能包括：

- 输入验证：检查用户名和密码是否为空
- 网络请求：调用后端登录API
- Token存储：登录成功后将JWT token存储到`SharedPreferences`
- 界面跳转：登录成功后跳转到主界面

<img src="assets/登录界面.jpg" alt="Screenshot_2025-09-06-15-26-15-142_xyz.qiquqiu.aiclient" style="zoom: 25%;" />

**核心方法：**

```java
// 登录逻辑
private void performLogin() {
    String username = etUsername.getText().toString().trim();
    String password = etPassword.getText().toString().trim();
    // 输入校验
    if (username.isEmpty() || password.isEmpty()) {
        Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
        return;
    }
    // 构建登录请求体
    LoginRequest loginRequest = new LoginRequest(username, password);
    // ★异步调用登录 API
    Call<LoginResult> call = apiService.login(loginRequest);
    call.enqueue(new Callback<LoginResult>() {
        @Override
        public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
            if (response.isSuccessful() && response.body() != null) {
                // 登录成功，响应体中传回token等信息
                LoginResult loginResponse = response.body();
                // 检查getData()是否为null，防止空指针异常
                if (loginResponse.getData() != null) {
                    String token = loginResponse.getData().getToken();
                    String userId = loginResponse.getData().getUserId();
                    String loggedInUsername = loginResponse.getData().getUsername();
                    // 保存 Token 和用户信息 (使用 SharedPreferences 本地保存)
                    SharedPreferencesManager.saveToken(LoginActivity.this, token);
                    SharedPreferencesManager.saveUserId(LoginActivity.this, userId);
                    SharedPreferencesManager.saveUsername(LoginActivity.this, loggedInUsername);
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    // 跳转到主界面
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // 关闭当前登录页面
                } else {
                    // getData()为null，说明登录失败
                    String errorMessage = "密码错误";
                    if (loginResponse.getMessage() != null) {
                        errorMessage = loginResponse.getMessage();
                    }
                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            } else {
                // 登录失败
                String errorMessage = "用户名或密码错误";
                if (response.errorBody() != null) {
                    try {
                        // 尝试解析错误信息 (取决于后端返回的错误格式)
                        // 需要根据实际后端错误格式进行解析
                        errorMessage = response.errorBody().string();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onFailure(Call<LoginResult> call, Throwable t) {
            // 网络请求失败
            Toast.makeText(LoginActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            t.printStackTrace();
        }
    });
}
```

**注册功能（RegisterActivity）**

注册界面提供用户名、密码、确认密码输入，实现新用户注册功能：

- 密码确认验证
- 用户名重复检查
- 注册成功后自动跳转登录

<img src="assets/注册界面演示.jpg" alt="Screenshot_2025-09-06-15-26-56-370_xyz.qiquqiu.aiclient" style="zoom: 25%;" />

**核心方法：**

```java
// 注册按钮点击事件
private void performRegistration() {
    String username = etUsername.getText().toString().trim();
    String password = etPassword.getText().toString().trim();
    String confirmPassword = etConfirmPassword.getText().toString().trim();
    // 输入验证
    if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
        Toast.makeText(this, "请填写所有字段", Toast.LENGTH_SHORT).show();
        return;
    }
    if (!password.equals(confirmPassword)) {
        Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
        return;
    }
    // 构建注册请求体
    RegisterRequest registerRequest = new RegisterRequest(username, password);
    // 调用注册 API
    Call<BaseResult> call = apiService.register(registerRequest);
    call.enqueue(new Callback<BaseResult>() {
        @Override
        public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
            if (response.isSuccessful()) {
                // 注册成功
                Toast.makeText(RegisterActivity.this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
                finish(); // 返回登录页面
            } else {
                // 注册失败
                String errorMessage = "注册失败";
                if (response.body() != null && response.body().getMessage() != null) {
                    errorMessage = response.body().getMessage();
                } else if (response.errorBody() != null) {
                    try {
                        errorMessage = response.errorBody().string();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onFailure(Call<BaseResult> call, Throwable t) {
            // 网络请求失败
            Toast.makeText(RegisterActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            t.printStackTrace();
        }
    });
}
```

**个人信息管理（MyProfileActivity）**

- 显示当前用户信息
- 支持修改密码功能
- 退出登录功能

<img src="assets/侧边栏展示.jpg" alt="Screenshot_2025-09-06-15-40-01-185_xyz.qiquqiu.aiclient" style="zoom:25%;" />	<img src="assets/我的信息界面.jpg" alt="Screenshot_2025-09-06-15-32-56-869_xyz.qiquqiu.aiclient" style="zoom:25%;" />

**本地数据管理（SharedPreferencesManager）**：基于`SharedPreferences`封装的工具类

```java
package xyz.qiquqiu.aiclient.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 基于 SharedPreferences 保存用户基本信息
 * @author lyh
 * @date 2025/6/28
 */
public class SharedPreferencesManager {
    private static final String PREF_NAME = "app_prefs";
    private static final String KEY_TOKEN = "auth_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void saveToken(Context context, String token) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public static String getToken(Context context) {
        return getSharedPreferences(context).getString(KEY_TOKEN, null);
    }

    public static void saveUserId(Context context, String userId) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    public static String getUserId(Context context) {
        return getSharedPreferences(context).getString(KEY_USER_ID, null);
    }

    public static void saveUsername(Context context, String username) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    public static String getUsername(Context context) {
        return getSharedPreferences(context).getString(KEY_USERNAME, null);
    }

    public static void clearUserData(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(KEY_TOKEN);
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_USERNAME);
        editor.apply();
    }
}
```

#### 5.1.2 后端实现

**用户实体类（User.java）**

```java
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true) // 链式setters方法的支持
@TableName("user")
public class User implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
```

**用户控制器（UserController.java）**

提供用户相关的RESTful API接口：

- `POST /user/register`：用户注册
- `POST /user/login`：用户登录
- `GET /user/me`：获取当前用户信息
- `POST /user/logout`：用户退出
- `PUT /user/me/password`：修改密码

**用户服务实现（UserServiceImpl.java）**

核心业务逻辑包括：

- 密码MD5加密存储（可以使用更安全的加密方式，如`hutool`包提供的若干方法）
- JWT token生成和验证
- 用户信息查询和更新

```java
// 用户登录
@Override
public BaseResult<LoginResultVO> login(LoginRequestDTO req) {
    // 1.判断存在并且校验
    User user = this.lambdaQuery().eq(User::getUsername, req.getUsername()).one();
    if (user == null) {
        return null;
    }
    boolean isMatch = MD5Util.match(req.getPassword(), user.getPassword());
    if (!isMatch) {
        return BaseResult.error("密码错误！");
    }
    long ttl = jwtProperties.getTtl();
    String s = String.valueOf(ttl);
    log.debug("{},{},{}", jwtProperties.getTokenName(), jwtProperties.getSecretKey(), s);
    // 2.存在则生成jwt返回
    Map<String, Object> claims = new HashMap<>(); // jwt的载荷
    claims.put("userId", user.getId());
    String token = JwtUtil.createJWT(jwtProperties.getSecretKey(), ttl, claims);
    log.debug("登录成功！");
    return BaseResult.success(new LoginResultVO(token, String.valueOf(user.getId()), user.getUsername()));
}
```

### 5.2 对话模块

**对话模块是本项目的核心功能**，**实现用户与AI的多模态交互**。

#### 5.2.1 Android端实现

**主界面设计（MainActivity）**

主界面采用DrawerLayout + NavigationView实现侧滑菜单，主要包含：

- 聊天消息列表（RecyclerView）
- 消息输入框（EditText）
- 图片选择按钮（ImageButton）
- 发送按钮（Button）
- 侧滑菜单（NavigationView）

<img src="assets/登录成功进入的主界面.jpg" alt="Screenshot_2025-09-06-15-32-40-407_xyz.qiquqiu.aiclient" style="zoom:25%;" />

**聊天消息适配器（ChatMessageAdapter）**

支持两种消息类型的展示：

1. **用户消息**：支持文本+图片的混合展示
2. **AI消息**：纯文本展示

```java
public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_AI_TEXT = 2;
    
    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        if ("USER".equalsIgnoreCase(message.getSender())) {
            return VIEW_TYPE_USER;
        } else {
            return VIEW_TYPE_AI_TEXT;
        }
    }
    // ViewHolder实现省略...
}
```

**流式消息处理**

使用**OkHttp**的**SSE**（Server-Sent Events）实现实时**流式**对话：

```java
private void sendMessage() {
    // 构建请求体
    SendMessageRequest requestPayload = new SendMessageRequest(
        currentConversationId, "USER", messageContent, imageBase64);
    RequestBody body = RequestBody.create(
        new Gson().toJson(requestPayload), 
        MediaType.get("application/json; charset=utf-8"));
    
    // 创建SSE请求
    Request sseRequest = new Request.Builder()
        .url(ApiClient.getClient().baseUrl() + "chat")
        .header("Authorization", token)
        .post(body)
        .build();
    
    // 处理流式响应
    EventSourceListener listener = new EventSourceListener() {
        @Override
        public void onEvent(EventSource eventSource, String id, String type, String data) {
            runOnUiThread(() -> {
                // 更新UI显示AI回复
                updateAiMessage(data);
            });
        }
    };
}
```

**会话管理（ConversationManagementActivity）**

提供会话的CRUD操作：

- 会话列表展示
- 会话重命名
- 会话删除
- 新建会话

**查询与展示：**
<img src="assets/侧边栏展示.jpg" alt="Screenshot_2025-09-06-15-40-01-185_xyz.qiquqiu.aiclient" style="zoom:25%;" /> <img src="assets/会话管理主界面.jpg" alt="Screenshot_2025-09-06-15-39-56-867_xyz.qiquqiu.aiclient" style="zoom:25%;" />

**删除和修改：**
<img src="assets/重命名会话.jpg" alt="Screenshot_2025-09-06-15-40-18-078_xyz.qiquqiu.aiclient" style="zoom:25%;" /> <img src="assets/删除会话.jpg" alt="Screenshot_2025-09-06-15-40-21-446_xyz.qiquqiu.aiclient" style="zoom:25%;" />

**批量操作：**
<img src="assets/批量操作.jpg" alt="Screenshot_2025-09-06-15-40-27-562_xyz.qiquqiu.aiclient" style="zoom:25%;" />

#### 5.2.2 后端实现

**会话实体类（Conversation.java）**

```java
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("conversation")
public class Conversation implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;          // UUID作为会话表主键
    private Long userId;        // 用户ID（逻辑外建）
    private String title;       // 会话标题
    private Integer messageCount; // 消息数量
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

**消息实体类（Message.java）**

```java
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("message")
public class Message implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String conversationId;  // 会话ID（逻辑外键）
    private Integer order;          // 消息顺序
    private String sender;          // USER/ASSISTANT
    private String content;         // 消息内容
    private String imageUrl;        // 图片URL
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

**聊天控制器（ChatController.java）**

提供对话相关的API接口：

- `POST /chat/conversations`：创建新会话
- `GET /chat/conversations`：获取用户会话列表
- `GET /chat/conversations/{id}/messages`：获取会话消息
- `POST /chat`：发送消息（流式响应）
- `POST /chat/conversations/{id}/save`：保存AI回复
- `GET /chat/conversations/{id}/title`：生成会话标题

**会话服务实现（ConversationServiceImpl.java）**

核心功能包括：

1. **多模态消息处理**

```java
@Override
@Transactional
public Flux<String> sendMessage(SendMessageDTO dto) {
    // 将图片上传到本地（或者云服务器、OSS等）
    String base64Image = dto.getImageFile();
    boolean hasImage = StrUtil.isNotBlank(base64Image);
    byte[] imageBytes = null;
    if (hasImage) {
        try {
            ImageUtil.ImageSaveResult result = ImageUtil.saveBase64Image(base64Image,
                    fileUploadProperties.getPath(),
                    fileUploadProperties.getUrlPrefix());
            dto.setImageFile(result.getUrl()); // 更新DTO中的URL
            imageBytes = result.getImageBytes();    // 将解码后的数据保存在局部变量中
        } catch (IOException e) {
            log.error("图片上传失败", e);
            throw new RuntimeException(e);
        }
    }
    // 保存用户消息，新增消息表消息数，统一执行更新（异步线程执行：线程池的应用）
    log.debug("【即将保存 SendMessageDTO = {}】", dto);
    // dto.setSender(Sender.USER);
    messageService.saveAndUpdate(dto);
    String prompt = dto.getContent();
    if (hasImage) {
        log.debug("处理多模态消息，prompt={}", prompt);
        return processMultiMedia(dto, imageBytes);
    }
    log.debug("处理纯文本消息，prompt={}", prompt);
    return processText(dto);
}
```

2. **Spring AI集成**

```yaml
# yml配置文件
spring:
  ai:
    openai:
      base-url: https://dashscope.aliyuncs.com/compatible-mode # 百炼平台的BaseUrl
      api-key: ${aiserver.ai.api-key} # 自己的API-Key
      chat:
        options:
          model: qwen3-max-preview # 默认自动装配的模型名
          temperature: 0.9 # 模型温度
```

```java
// 处理含有多模态（图片）的模型调用
private Flux<String> processMultiMedia(SendMessageDTO dto, byte[] imageBytes) {
    // 直接使用传递进来的 imageBytes
    Media media = ImageUtil.convertToMedia(imageBytes);
    if (media == null) {
        // 如果图片处理失败，可以降级为纯文本处理或返回错误
        throw new RuntimeException("图片处理失败");
        // return processText(dto);
    }
    String prompt = StrUtil.isNotBlank(dto.getContent()) ? dto.getContent() : "请描述这张图片";
    return multiChatClient.prompt()
            .user(u -> u.text(prompt).media(media)) // 构建多模态消息
            .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, dto.getConversationId()))
            .stream()
            .content();
}
```
> 其中：`multiChatClient` 是配置了 `qwen-omni-turbo` 的多模态大模型（必须支持多模态），配置代码如下：
>
> ```java
> @Bean
> public ChatClient multiChatClient(OpenAiChatModel model, ChatMemory chatMemory) {
>     log.debug("初始化 MultiChatClient...");
>     return ChatClient
>             .builder(model)
>             .defaultOptions(ChatOptions.builder().model("qwen-omni-turbo-2025-03-26").build())
>             .defaultSystem(SystemPrompt.DEFAULT_SYSTEM_PROMPT)
>             .defaultAdvisors(
>                     new SimpleLoggerAdvisor(),
>                     MessageChatMemoryAdvisor.builder(chatMemory).build()
>             )
>             .build();
> ```
>
3. **对话记忆管理**


使用Spring AI的`ChatMemory`+ `JdbcChatMemoryRepository` 组件实现对话上下文管理：

```java
@Bean
public ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository) {
    log.debug("初始化 ChatMemory...");
    return MessageWindowChatMemory.builder()
            .chatMemoryRepository(jdbcChatMemoryRepository)
            .maxMessages(20) // 最大消息数
            .build();
}
```

### 5.3 流式响应具体处理

#### 5.3.1 后端SSE实现

```java
// 处理纯文本的模型调用
private Flux<String> processText(SendMessageDTO dto) {
    String prompt = dto.getContent();
    return getOneModel().prompt()
            .user(prompt)
            .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, dto.getConversationId()))
            .stream() // 流式响应
            .content();
}

// 处理含有多模态（暂时是图片）的模型调用
private Flux<String> processMultiMedia(SendMessageDTO dto, byte[] imageBytes) {
    // 直接使用传递进来的 imageBytes
    Media media = ImageUtil.convertToMedia(imageBytes);
    if (media == null) {
        // 如果图片处理失败，可以降级为纯文本处理或返回错误
        throw new RuntimeException("图片处理失败");
        // return processText(dto);
    }
    String prompt = StrUtil.isNotBlank(dto.getContent()) ? dto.getContent() : "请描述这张图片";
    return multiChatClient.prompt()
            .user(u -> u.text(prompt).media(media)) // 构建多模态消息
            .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, dto.getConversationId()))
            .stream()
            .content();
}
```

#### 5.3.2 前端SSE处理

```java
// Android端使用OkHttp的EventSource处理SSE
// 实现EventSourceListener
EventSourceListener listener = new EventSourceListener() {
    private final StringBuilder aiResponseBuilder = new StringBuilder();
    @Override
    public void onOpen(@NonNull EventSource eventSource, @NonNull okhttp3.Response response) {
        Log.d("SSE", "Connection opened.");
    }
    @Override
    public void onEvent(@NonNull EventSource eventSource, @Nullable String id, @Nullable String type, @NonNull String data) {
        if ("[DONE]".equalsIgnoreCase(data)) {
            eventSource.cancel();
            return;
        }
        aiResponseBuilder.append(data);
        runOnUiThread(() -> {
            aiMessage.setContent(aiResponseBuilder.toString());
            chatMessageAdapter.notifyItemChanged(messageList.size() - 1);
        });
    }
    @Override
    public void onClosed(@NonNull EventSource eventSource) {
        Log.d("SSE", "连接关闭！");
        final String fullAiResponse = aiResponseBuilder.toString();
        runOnUiThread(() -> {
            // 重新启用相关组件
            setInteractionEnabled(true);
            // 调用新的处理流程
            handleStreamEnd(currentConversationId, fullAiResponse);
        });
    }
    @Override
    public void onFailure(@NonNull EventSource eventSource, @Nullable Throwable t, @Nullable okhttp3.Response response) {
        Log.e("SSE", "Connection failed.", t);
        runOnUiThread(() -> {
            // 发送失败后也重新启用
            setInteractionEnabled(true);
            String errorMsg = "AI响应出错了";
            if (t != null) {
                errorMsg += ": " + t.getMessage();
            }
            aiMessage.setContent(errorMsg);
            chatMessageAdapter.notifyItemChanged(messageList.size() - 1);
        });
    }
};
// 配置客户端
OkHttpClient client = ApiClient.getOkHttpClient();
currentEventSource = EventSources.createFactory(client).newEventSource(sseRequest, listener);
```

### 5.4 图库模块

图库模块提供花卉图片的浏览和选择功能。

#### 5.4.1 Android端实现

**花卉图库界面（FlowerGalleryActivity）**

采用RecyclerView + LinearLayoutManager实现图片列表展示：

- **分页加载**图片数据
- 无限**滚动加载**
- 图片**点击选择**功能
- 返回选中图片到主界面

<img src="assets/图库入口.png" alt="image-20250906160621032" style="zoom: 50%;" />**→**<img src="assets/图库内部1.jpg" alt="Screenshot_2025-09-06-15-36-42-134_xyz.qiquqiu.aiclient" style="zoom:25%;" /> <img src="assets/图库内部2.jpg" alt="Screenshot_2025-09-06-15-36-46-964_xyz.qiquqiu.aiclient" style="zoom:25%;" />

```java
public class FlowerGalleryActivity extends AppCompatActivity {
    private RecyclerView rvFlowerGallery;
    private FlowerGalleryAdapter adapter;
    private List<FlowerImage> flowerImageList = new ArrayList<>();
    private int currentPage = 1;
    private boolean isLoading = false;
    
    private void loadFlowerImages() {
        if (isLoading) return;
        isLoading = true;
        
        String token = SharedPreferencesManager.getToken(this);
        Call<BaseResult<List<FlowerImage>>> call = apiService.getFlowerImages(token, currentPage, 10);
        
        call.enqueue(new Callback<BaseResult<List<FlowerImage>>>() {
            @Override
            public void onResponse(Call<BaseResult<List<FlowerImage>>> call, Response<BaseResult<List<FlowerImage>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<FlowerImage> newImages = response.body().getData();
                    if (newImages != null && !newImages.isEmpty()) {
                        flowerImageList.addAll(newImages);
                        adapter.updateData(flowerImageList);
                        currentPage++;
                    }
                }
                isLoading = false;
            }
        });
    }
}
```

**花卉图库适配器（FlowerGalleryAdapter）**

```java
public class FlowerGalleryAdapter extends RecyclerView.Adapter<FlowerGalleryAdapter.ViewHolder> {
    private List<String> imageUrls;
    private OnItemClickListener onItemClickListener;
    
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        
        // 使用Glide加载图片
        Glide.with(holder.itemView.getContext())
            .load(ApiClient.BASE_URL + imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(holder.imageView);
            
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });
    }
}
```

#### 5.4.2 后端实现

**图库实体类（Gallery.java）**

```java
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("gallery")
public class Gallery implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String imageUrl;     // 图片路径
    private String name;         // 花卉名称
    private String description;  // 花卉描述
    private Integer star;        // 点赞数
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

**图库控制器（GalleryController.java）**

```java
package xyz.qiquqiu.aiserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.qiquqiu.aiserver.common.BaseResult;
import xyz.qiquqiu.aiserver.common.vo.FlowerImageVO;
import xyz.qiquqiu.aiserver.common.query.PageQuery;
import xyz.qiquqiu.aiserver.service.IGalleryService;

import java.util.List;

/**
 * <p>
 * 花卉图库 前端控制器
 * </p>
 *
 * @author lyh
 * @since 2025-07-07
 */
@Slf4j
@RestController
@RequestMapping("/gallery")
@RequiredArgsConstructor
public class GalleryController {

    private final IGalleryService galleryService;

    @GetMapping("/page")
    public BaseResult<List<FlowerImageVO>> pageQuery(PageQuery query) {
        log.debug("分页查询图库：【{}】", query);
        return galleryService.pageQuery(query);
    }

}
```

**图库服务实现（GalleryServiceImpl.java）**

```java
package xyz.qiquqiu.aiserver.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.qiquqiu.aiserver.common.BaseResult;
import xyz.qiquqiu.aiserver.common.vo.FlowerImageVO;
import xyz.qiquqiu.aiserver.common.po.Gallery;
import xyz.qiquqiu.aiserver.common.query.PageQuery;
import xyz.qiquqiu.aiserver.mapper.GalleryMapper;
import xyz.qiquqiu.aiserver.service.IGalleryService;

import java.util.List;

/**
 * <p>
 * 花卉图库 服务实现类
 * </p>
 *
 * @author lyh
 * @since 2025-07-07
 */
@Service
@Slf4j
public class GalleryServiceImpl extends ServiceImpl<GalleryMapper, Gallery> implements IGalleryService {

    @Override
    public BaseResult<List<FlowerImageVO>> pageQuery(PageQuery query) {
        Page<Gallery> page = this.lambdaQuery()
                .page(query.toMpPage("star", false));
        List<Gallery> list = page.getRecords();
        if (CollectionUtil.isEmpty(list)) {
            return BaseResult.error("暂无数据");
        }
        // PO集合转VO集合返回
        log.debug("查询完毕：{}", list);
        return BaseResult.success(list.stream().map(FlowerImageVO::of).toList());
    }
}
```

### 5.5 图片上传处理模块

图片处理模块负责图片的选择、拍照、上传和显示功能。

#### 5.5.1 Android端实现

**图片选择功能**

支持两种图片获取方式：

1. **相机拍照**：使用系统相机拍照
2. **图库选择**：从系统图库选择图片

**权限管理**

```java
public class MainActivity extends AppCompatActivity {
    private ActivityResultLauncher<String> pickImageLauncher;
    private ActivityResultLauncher<Uri> takePictureLauncher;
    private Uri selectedImageUri = null;
    private Uri cameraImageUri = null;
    
    // ... 其他代码略 ...
    
    // 初始化所有的 Launcher
    private void initializeLaunchers() {
        // 权限请求 Launcher
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                permissions -> {
                    boolean galleryGranted = permissions.getOrDefault(Manifest.permission.READ_MEDIA_IMAGES, false) ||
                            permissions.getOrDefault(Manifest.permission.READ_EXTERNAL_STORAGE, false);
                    boolean cameraGranted = permissions.getOrDefault(Manifest.permission.CAMERA, false);
                    if (permissions.containsKey("gallery") && galleryGranted) {
                        // 如果是请求相册权限成功
                        pickImageLauncher.launch("image/*");
                    } else if (permissions.containsKey("camera") && cameraGranted) {
                        // 如果是请求相机权限成功
                        cameraImageUri = createImageUri();
                        if (cameraImageUri != null) {
                            takePictureLauncher.launch(cameraImageUri);
                        }
                    } else {
                        Toast.makeText(this, "需要相关权限才能使用此功能", Toast.LENGTH_SHORT).show();
                    }
                });
        // 从相册选择图片 Launcher
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                // 这里使用申请请永久权限的方式实现非临时的uri访问（也可以复制文件到app私有目录）
                final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                try {
                    // 持久化这个URI的读取权限
                    getContentResolver().takePersistableUriPermission(uri, takeFlags);
                } catch (SecurityException e) {
                    e.printStackTrace();
                    // 如果 takePersistableUriPermission 失败，可能是因为用户选择的文件不支持
                }
                selectedImageUri = uri;
                showImagePreview(uri);
            }
        });
    }
}
```

**图片预览功能**

```java
// 显示图片预览：Glide
private void showImagePreview(Uri imageUri) {
    imagePreviewContainer.setVisibility(View.VISIBLE);
    Glide.with(this).load(imageUri).into(imagePreview);
}
```

**图片Base64编码**

```java
// 将图片Uri转换为Base64字符串
private String encodeImageToBase64(Uri imageUri) {
    try {
        Bitmap bitmap;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.getContentResolver(), imageUri));
        } else {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    } catch (IOException e) {
        e.printStackTrace();
        Toast.makeText(this, "图片编码失败", Toast.LENGTH_SHORT).show();
        return null;
    }
}
```

#### 5.5.2 后端实现

**图片上传处理**

后端接收Base64编码的图片数据，进行解码和存储

**保存时机：**POST `/chat` 接口调用，且**dto**参数中图片的base64编码不为空

```java
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageDTO {
    @NotNull(message = "会话id不能为空")
    private String conversationId;
    @NotNull(message = "消息内容不能为空")
    private String content;
    private String imageFile; // base64 string 如果不为空，就代表这是一条多模态消息（含有图片）
    private String sender;
}
```

**Controller：**

```java
@PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public Flux<String> sendMessage(@RequestBody @Valid SendMessageDTO dto) {
    return chatService.sendMessage(dto);
}
```

**Service：**

```java
// 发送消息
@Override
@Transactional
public Flux<String> sendMessage(SendMessageDTO dto) {
    // 将图片上传到本地（云服务器）
    String base64Image = dto.getImageFile();
    boolean hasImage = StrUtil.isNotBlank(base64Image);
    byte[] imageBytes = null;

    if (hasImage) {
        try {
            ImageUtil.ImageSaveResult result = ImageUtil.saveBase64Image(base64Image,
                    fileUploadProperties.getPath(),
                    fileUploadProperties.getUrlPrefix());
            dto.setImageFile(result.getUrl()); // 更新DTO中的URL
            imageBytes = result.getImageBytes();    // 将解码后的数据保存在局部变量中
        } catch (IOException e) {
            log.error("图片上传失败", e);
            throw new RuntimeException(e);
        }
    }

    // ...
}
```

**图片上传配置：**

```java
@Component
@Data
@ConfigurationProperties(prefix = "file.upload")
public class FileUploadProperties {
    private String path; // 上传路径
    private String urlPrefix; // 资源url前缀
}
```

**由工具类`ImageUtil`调用`saveBase64Image`方法保存图片到本地**

```java
package xyz.qiquqiu.aiserver.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.content.Media;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Slf4j
public class ImageUtil {

    @Data
    @AllArgsConstructor
    public static class ImageSaveResult {
        private String url;
        private byte[] imageBytes;
    }

    /**
     * 将Base64编码的图片字符串保存到本地，并返回可访问的URL
     * @param imageBase64 Base64编码的图片字符串
     * @return 可通过网络访问的图片URL
     * @throws IOException 文件写入异常
     */
    public static ImageSaveResult saveBase64Image(String imageBase64, String uploadPath, String urlPrefix) throws IOException {
        // log.debug("原始base64图片字符串: {}", imageBase64);

        // 1.解码
        // Base64字符串可能包含MIME头，如 "data:image/jpeg;base64,"，需要去掉
        if (imageBase64.contains(",")) {
            imageBase64 = imageBase64.split(",")[1];
        }
        byte[] imageBytes = Base64.getDecoder().decode(imageBase64);

        // 2.生成唯一文件名
        // 这里我们默认为.jpg，因为Base64本身不包含文件类型信息
        String fileName = UUID.randomUUID() + ".jpg";

        // 3,4.确定存储路径并保存文件
        // 使用 Paths.get() 来创建路径
        Path directoryPath = Paths.get(uploadPath);

        // 确保目录存在，如果不存在则创建
        if (!Files.exists(directoryPath)) {
            log.debug("资源上传目录不存在，创建目录: {}", directoryPath);
            Files.createDirectories(directoryPath);
        }

        Path filePath = directoryPath.resolve(fileName);

        // 将解码图片后的字节数组写入文件
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            log.debug("正在写入文件: {}", filePath);
            fos.write(imageBytes);
        }

        // 5.生成可访问的URL
        // 例如：/resources/images/ + 123e4567-e89b-12d3-a456-426614174000.jpg
        String url = urlPrefix + fileName;
        return new ImageSaveResult(url, imageBytes);
    }

    public static Media convertToMedia(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length == 0) {
            return null;
        }
        ByteArrayResource resource = new ByteArrayResource(imageBytes);
        return new Media(MediaType.IMAGE_JPEG, resource);
    }
}
```

**静态资源映射配置**（由于**Windows与Linux路径模式不一致**，所以使用 dev/prod 两套不同环境变量保存）

```yaml
# application.yaml
file:
  upload:
    path: ${aiserver.file.upload.path}
    url-prefix: ${aiserver.file.upload.url-prefix}
  gallery:
    path: ${aiserver.file.gallery.path}
    url-prefix: ${aiserver.file.gallery.url-prefix}
```

### 5.6 JWT鉴权模块

#### 5.6.1 JWT配置

```yaml
aiserver:
  jwt:
    secret-key: 自定义一个复杂的密钥
    ttl: 259200000 # 过期时间：3 days
    token-name: Authorization # token作为请求头的名称
```

#### 5.6.2 JWT工具类

```java
package xyz.qiquqiu.aiserver.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    /**
     * 生成jwt
     * 使用HS256算法, 私钥使用固定秘钥
     *
     * @param secretKey jwt秘钥
     * @param ttlMillis jwt过期时间(毫秒)
     * @param claims    设置的信息
     * @return 生成的JWT字符串
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        // 将秘钥转换为SecretKey对象
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // 生成JWT的时间
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        // 设置jwt的body
        JwtBuilder builder = Jwts.builder()
                // 设置私有声明
                .setClaims(claims)
                // 设置签名使用的签名算法和签名秘钥
                .signWith(key, Jwts.SIG.HS256)
                // 设置过期时间
                .setExpiration(exp);

        return builder.compact();
    }

    /**
     * Token解密
     *
     * @param secretKey jwt秘钥
     * @param token     加密后的token
     * @return 解析后的Claims对象
     */
    public static Claims parseJWT(String secretKey, String token) {
        // 将秘钥转换为SecretKey对象
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // 解析JWT
        return Jwts.parser()
                .verifyWith(key) // 设置验证秘钥
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
```

#### 5.6.3 拦截器实现

```java
package xyz.qiquqiu.aiserver.interceptor;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import xyz.qiquqiu.aiserver.context.BaseContext;
import xyz.qiquqiu.aiserver.properties.JwtProperties;
import xyz.qiquqiu.aiserver.util.JwtUtil;

/**
 * jwt令牌校验拦截器
 * @author lyh
 * @date 2025/6/28 14:33
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenInterceptor implements HandlerInterceptor {

    // 注入 jwt 配置
    private final JwtProperties jwtProperties;

    /**
     * 校验jwt
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果是OPTIONS请求，则直接放行，这是CORS预检请求
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            log.info("OPTIONS请求，直接放行");
            return true;
        }

        // 1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getTokenName());

        // 2、校验令牌
        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
            Long userId = Long.valueOf(claims.get("userId").toString());
            log.info("jwt校验通过,当前用户id:{}", userId);
            // 3、通过
            // BaseContext维护一个ThreadLocal<Long>，即用户id
            BaseContext.setCurrentId(userId);
            return true;
        } catch (Exception ex) {
            // 4、不通过，响应401状态码
            log.info("jwt校验不通过！");
            response.setStatus(401);
            return false;
        }
    }
}
```

---

## 6. 系统测试与效果展示

### 6.1 功能测试

#### 6.1.1 用户模块测试

**测试用例1：用户注册、用户登录**

- 预期结果：注册成功，跳转到登录页面

  <img src="assets/登录成功进入的主界面.jpg" alt="登录成功进入的主界面" style="zoom:25%;" />

**测试用例2：JWT认证与token本地保存**

- 测试步骤：使用有效token访问需要认证的API

- 预期结果：正常访问API，token保存到本地

  **IDEA控制台日志：**

  ```java
  2025-09-06 15:43:09.138 [http-nio-8080-exec-6] INFO  x.q.a.i.JwtTokenInterceptor - jwt校验:eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjIxNDc0ODM2NDcsImV4cCI6MTc1NzQwMzE1OX0.sagmz1tub-l-2VhXslG37zjsyTsLqSLGuSGtPFFn9Jk
  2025-09-06 15:43:09.138 [http-nio-8080-exec-6] INFO  x.q.a.i.JwtTokenInterceptor - jwt校验通过,当前用户id:2147483647
  ```

  **手机本地文件：**`/data/data/xyz.qiquqiu.aiclient/shared_prefs/app.prefs.xml`

  ```xml
  <?xml version='1.0' encoding='utf-8' standalone='yes' ?>
    <map>
        <string name="auth_token">eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjIxNDc0ODM2NDcsImV4cCI6MTc1NzQwMzE1OX0.sagmz1tub-l-2VhXslG37zjsyTsLqSLGuSGtPFFn9Jk</string>
        <string name="user_id">2147483647</string>
        <string name="username">liyihan</string>
    </map>
  ```

#### 6.1.2 对话模块测试

**测试用例4：文本对话**

- 预期结果：AI正常回复

  <img src="assets/普通文本测试.jpg" alt="fb8a5bf52e24a385a86b2f3977c486c4_720" style="zoom:25%;" />

**测试用例5：图片对话、流式响应**

- 预期结果：AI识别花卉并提供相关信息、AI回复以流式方式逐步显示

  <img src="assets/多模态回答演示1.jpg" alt="Screenshot_2025-09-06-15-42-45-580_xyz.qiquqiu.aiclient" style="zoom:25%;" /> **→**  <img src="assets/多模态回答演示2.jpg" alt="Screenshot_2025-09-06-15-42-53-503_xyz.qiquqiu.aiclient" style="zoom:25%;" />

#### 6.1.3 图库模块测试

**测试用例7：图库浏览、图片选择：**上述图片就是通过图库选择，所以跳过本测试

> **获取拍照、相册权限后，可以进行拍照或上传相册图片进行对话**
>
> <img src="assets/app请求获取权限.jpg" alt="Screenshot_2025-09-06-15-43-05-424_xyz.qiquqiu.aiclient" style="zoom:25%;" />

---

## 7. 项目总结

### 7.1 完成情况

本项目成功实现了预期的所有功能目标：

✅ **用户管理系统**：完成用户注册、登录、认证、信息管理等功能  
✅ **多模态AI对话**：实现文本+图片的AI交互功能  
✅ **流式响应**：采用SSE技术实现实时对话体验  
✅ **花卉图库**：提供花卉图片浏览和选择功能  
✅ **会话管理**：支持多会话创建、切换、重命名、删除  
✅ **图片处理**：支持拍照、图库选择、压缩、上传功能  
✅ **前后端分离**：Android客户端 + Spring Boot后端服务  
✅ **数据持久化**：MySQL数据库存储用户和对话数据

**项目规模统计：**

- 前端代码：约3000行Java代码 + XML布局文件
- 后端代码：约2500行Java代码
- 数据库表：4个核心业务表
- API接口：15个RESTful接口
- 功能模块：4个主要功能模块

### 7.2 技术收获

通过本项目的开发，我在以下技术领域获得了深入的学习和实践：

**Android开发技术：**

- Android原生开发的核心组件使用
- RecyclerView、DrawerLayout等高级UI组件
- 使用Retrofit进行网络请求（声明式API）
- 图片处理和Base64编码技术
- SSE流式数据处理
- Android权限管理和生命周期

**后端开发技术：**
- Spring Boot框架的基本使用
- Spring AI框架集成大模型技术
    - 集成第三方AI大模型API（百炼平台，deepseek开发平台等）
    - 多模态数据处理技术（多模态大模型的调用）
    - 流式响应的实现原理
    - 对话上下文管理及对话持久化方案

- MyBatis Plus简化Mybatis进行数据库操作
- 无状态的JWT认证机制的实现
- SSE服务端推送技术（Flux）

### 7.3 TODO-List

1. **性能优化**：
    - 图片加载可以增加更多缓存策略
    - 长列表滚动可以优化内存使用
    - 网络请求可以增加重试机制（及优化失败处理）
2. **用户体验**：
    - 可以增加更多的加载动画和反馈
    - 可以增加夜间模式支持
3. **功能扩展**：
    - 可以增加语音输入功能
    - 可以支持更多文件格式
    - 可以增加分享功能（基于后端服务器创建公共链接）

---

## 8. 参考资料

### 8.1 官方文档

1. [Android Developer Documentation](https://developer.android.com/docs)
2. [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
3. [Multimodality API :: Spring AI Reference](https://docs.spring.io/spring-ai/reference/api/multimodality.html)

### 8.2 技术博客与教程

1. [Retrofit官方教程](https://square.github.io/retrofit/)
2. [OkHttp使用指南](https://square.github.io/okhttp/)
3. [Glide图片加载库](https://bumptech.github.io/glide/)
4. [SSE服务端推送技术](https://developer.mozilla.org/en-US/docs/Web/API/Server-sent_events)
5. [【SpringAI篇01】：5分钟教会你使用SpringAI （1.0.0稳定版）_spring ai使用-CSDN博客](https://blog.csdn.net/lyh2004_08/article/details/148925217)
2. [【SpringAI篇02】：实现连续对话（上下文记忆功能+会话隔离）_spring ai 记忆-CSDN博客](https://blog.csdn.net/lyh2004_08/article/details/148928827)
3. [【SpringAI篇03】：聊天记录持久化（仍保留上下文）.-CSDN博客](https://blog.csdn.net/lyh2004_08/article/details/148951699)

---

## 附录

### 附录A：后端代码开源地址

- **Github**：https://github.com/qiquqiu/CommonAiServer
- **Gitee**：https://gitee.com/liyihan11/common-ai-server

### 附录B：项目部署说明

**环境要求：**

- JDK 17+
- MySQL 8.0+
- Android SDK API 34+
- Maven 3.8+

**其他要求：**

必须要一个**API_KEY**，可以是DeepSeek、ChatGPT等平台的，只要符合OpenAI接口即可通用（无需更改代码）。

**部署步骤：**

1. **数据库准备**

   ```bash
   # 创建数据库
   CREATE DATABASE ai_application DEFAULT CHARACTER SET utf8mb4;
   
   # 执行建表语句
   source init-db.sql;
   ```

2. **后端部署**
   ```bash
   # 编译打包
   mvn clean package -DskipTests
   
   # 运行应用
   java -jar aiserver-web/target/aiserver-web-1.0.0.jar
   ```

3. **Android应用安装**

   ```bash
   # 编译APK
   ./gradlew assembleDebug
   
   # 安装到设备
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

### 附录C：build.gradle.kts

```json
plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "xyz.qiquqiu.aiclient"
    compileSdk = 34

    defaultConfig {
        applicationId = "xyz.qiquqiu.aiclient"
        minSdk = 29
        targetSdk = 34
        versionCode = 3
        versionName = "1.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // AndroidX UI
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // http请求库 Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Gson: 用于JSON解析
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    // SSE 模块，用于处理服务器推送的事件
    implementation("com.squareup.okhttp3:okhttp-sse:4.9.3")

    // 图片加载库 - Glide (用于显示用户上传图片预览)
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    // 侧边栏 (DrawerLayout) 和 NavigationView
    implementation("androidx.drawerlayout:drawerlayout:1.1.1")

    // RecyclerView (用于展示聊天消息和历史会话列表)
    implementation("androidx.recyclerview:recyclerview:1.2.1")

    // lombok
    implementation("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    // 其他必要的依赖
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
```
