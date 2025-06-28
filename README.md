# Waxberry ServiceHub

Waxberry ServiceHub 是一个基于 Spring Boot 3.x 和 Spring Cloud 2024 构建的现代微服务架构项目。该项目采用模块化设计，提供完整的微服务解决方案，包括认证授权、API网关、文件服务等核心功能。

## 功能特性

- 🚀 基于 Spring Boot 3.x 和 Spring Cloud 2024
- 🔐 完整的认证授权系统
- 🌐 API 网关服务
- 📁 分布式文件服务
- 🛠 统一服务管理
- 📚 完善的文档
- 🔄 数据库版本控制
- 🧪 完整的测试覆盖

## 技术栈

- Java 17
- Spring Boot 3.4.6
- Spring Cloud 2024.0.1
- Spring Security
- MySQL 8.0
- MinIO (对象存储)
- Flyway (数据库版本控制)
- Lombok
- Hutool
- SpringDoc (API 文档)

## 项目结构

```
waxberry-servicehub/
├── auth/           # 认证授权服务
├── boot/           # 启动模块
├── common/         # 公共模块
├── fileserver/     # 文件服务
├── gateway/        # API 网关
└── manager/        # 服务管理
```

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- MinIO 服务器

### 构建项目

```bash
# 克隆仓库
git clone https://gitee.com/jiangxue-waxberry/waxberry-servicehub.git

# 进入项目目录
cd waxberry-servicehub

# 构建项目
mvn clean package
```

### 运行项目

项目支持多种环境配置（local、dev、test、prod），默认为 dev 环境：

```bash
# 在开发环境中运行
mvn spring-boot:run -Pdev

# 在生产环境中运行
mvn spring-boot:run -Pprod
```

## 配置

主要配置文件位于每个模块的 `src/main/resources` 目录中：

- `application.yml`: 基础配置
- `application-dev.yml`: 开发环境配置
- `application-prod.yml`: 生产环境配置

## 文档

- API 文档：启动服务后访问 `http://localhost:8080/swagger-ui.html`

## 参与贡献

1. Fork 本仓库
2. 创建您的功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交您的更改 (`git commit -m '添加一些 AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 许可证

本项目采用 Apache License 2.0 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。


## 致谢

感谢所有为本项目做出贡献的开发者！
