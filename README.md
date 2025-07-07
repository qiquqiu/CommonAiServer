# AiServer - 多模块AI应用后端

`AiServer` 是一个基于 Spring Boot 3 和 Spring AI 构建的企业级多模块AI应用后端项目。它采用了标准的父子模块化架构，旨在提供一个清晰、可扩展、易于维护的开发框架。

## ✨ 项目特性

- **模块化设计**：项目被拆分为四个核心模块，实现了高度的内聚和低耦合。
- **统一依赖管理**：通过 `aiserver-parent` 统一管理所有模块的依赖版本，避免版本冲突。
- **清晰分层**：业务逻辑、数据访问和Web层完全分离，职责明确。
- **Spring AI 集成**：深度集成 Spring AI，轻松实现与大语言模型的交互。
- **企业级实践**：包含统一异常处理、JWT认证、MyBatis Plus数据持久化等企业级开发实践。

## 📁 模块结构

项目采用标准的Maven父子模块结构，具体如下：

```
aiserver-parent
├── aiserver-common  (公共模块)
├── aiserver-core    (核心模块)
└── aiserver-web     (Web模块)
```

### 🏗️ aiserver-parent
- **类型**：`pom`
- **作用**：作为项目的父模块，不包含任何业务代码。它主要负责：
  - **统一依赖管理**：在 `<dependencyManagement>` 中定义所有依赖的版本。
  - **模块聚合**：在 `<modules>` 中声明所有子模块。
  - **插件管理**：在 `<pluginManagement>` 中统一管理Maven插件。

### 📦 aiserver-common
- **类型**：`jar`
- **作用**：公共模块，存放整个项目通用的工具类、常量、DTO、VO等。
- **核心依赖**：`spring-boot-starter`, `spring-ai-starter-model-openai`, `jjwt`, `hutool-all`, `lombok`。

### 🔧 aiserver-core
- **类型**：`jar`
- **作用**：核心业务模块，包含项目的核心业务逻辑。
- **包含内容**：
  - **Entity**：数据库实体类。
  - **Mapper/DAO**：数据访问层接口。
  - **Service**：业务逻辑层。
  - **Config**：核心配置文件，如 `MybatisConfig`。
  - **Properties**：配置属性类。
- **依赖**：`aiserver-common`, `mybatis-plus`, `mysql-connector-j`。

### 🌐 aiserver-web
- **类型**：`jar`
- **作用**：Web层模块，负责处理HTTP请求和响应，是项目的启动模块。
- **包含内容**：
  - **Controller**：API控制器。
  - **Interceptor**：请求拦截器，如JWT令牌验证。
  - **Handler**：全局异常处理器。
  - **Config**：Web相关配置，如 `WebMvcConfig`。
  - **Application**：`AiServerApplication` 启动类。
  - **Resources**：`application.yaml` 等配置文件。
- **依赖**：`aiserver-core`, `spring-boot-starter-web`。

## 🛠️ 技术栈

- **核心框架**：Spring Boot 3.x
- **AI框架**：Spring AI 1.0.0
- **数据持久化**：MyBatis Plus
- **数据库**：MySQL
- **认证授权**：JWT (JSON Web Token)
- **工具库**：Hutool, Lombok
- **构建工具**：Maven

## 🚀 如何运行

1.  **克隆项目**：
    ```bash
    git clone https://github.com/qiquqiu/CommonAiServer.git
    cd AiServer
    ```

2.  **配置环境**：
    - 修改 `aiserver-web/src/main/resources/application-dev.yaml` 中的数据库连接信息。
    - 在 `application.yaml` 或环境变量中配置你的AI模型API Key (`aiserver.ai.api-key`)。

3.  **构建项目**：
    在项目根目录（`aiserver-parent`所在目录）下执行Maven命令：
    ```bash
    mvn clean install
    ```

4.  **运行项目**：
    可以直接在IDE中运行 `aiserver-web` 模块的 `AiServerApplication` 类，或者通过以下命令启动：
    ```bash
    java -jar aiserver-web/target/aiserver-web-0.0.1-SNAPSHOT.jar
    ```

## ✨ 重构优势

- **清晰分层**：按功能职责分离代码，使项目结构更加清晰。
- **依赖管理**：父模块统一管理版本，避免依赖冲突，简化升级。
- **模块复用**：`aiserver-common` 和 `aiserver-core` 模块可以被其他项目复用。
- **构建灵活**：可以独立构建和测试各个模块，提高开发效率。
- **维护性强**：代码结构清晰，便于团队协作和长期维护。
