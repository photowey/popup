# `popup`

 [English](./README.md)  | 中文

> `Popup`  是基于 `Spring Boot` `3.x`. 的 `Spring Cloud` 微服务开发脚手架，同时也支持 `Spring Cloud Alibaba`。



## `Popctl`

> [`Popctl`](https://github.com/photowey/popctl) 开发脚手架的命令行工具.



## 本地运行

> 添加一个指向 `maven` 本地仓库的环境变量 `MAVEN_REPO`
>
> 比如: 
>
> `MAVEN_REPO` = `D:\software\maven\repo\repository`
>
> `MAVEN_REPO` = `/Users/photowey/xxx/maven/repo/repository`



## 扩展项目

> [mybatis-plus-ext](https://github.com/photowey/mybatis-plus-ext) 是 `Mybatis-plus` 的扩展项目, 本项目将使用它来增强 `Repository`（`Mapper`），从而更大化简化开发。



## 一、结构

- `popup-bom`
  - `popup-dependency`
  - `popup-parent`
    - `popup-app`
      - `popup-app`
        - `popup-app-client`
        
        - `popup-app-core`
        
        - `popup-app-repository`
        
        - `popup-app-service`
        
        - `popup-app-web`
        
        - --
        
        - `popup-app-database`
        
          - 数据库统一依赖
          - 比如: `Mybatis-Plus`
        
        - `popup-app-database-mysql`
        
          - `MySQL` 驱动
        
        - `popup-app-database-pgsql`
        
          - `PG` 驱动
        
        - `popup-app-persistence`
        
          - > `JAR`
            >
            > 数据库相关的自动配置
        
          - `database`
        
          - `cache`
        
          - ...
        
        - --
        
        - `popup-nacos`
        
          - > `Nacos` 支持
            >
            > 通常情况: `Nacos` starter 和其 `Client` 的版本可能不是咱们想要的适配版本。
            >
            > 故: 通过从 `starter` 中排除 `client` 依赖然后再手动引入的方式来实现版本的升级和管理。

        - `popup-seata`

          - `Seata` 支持
          - 同 `Nacos`
          -

    - `popup-cloud`

      - `popup-app-starting-listener`
      - `popup-app-starting-printer`
      - `popup-cloud-admin`
      - `popup-cloud-core`
      - `popup-cloud-gateway`
      - `popup-cloud-uaa`

    - `popup-cloud-alibaba`

      - `popup-cloud-alibaba-nacos`
      - `popup-cloud-alibaba-seata`
      - `popup-cloud-alibaba-sentinel`

    - `popup-component`

      - > `components`:

      - `common`

      - `core`

      - `crypto`

      - `eventbus`

      - `queue`

      - `sync`

      - `test`

      - `...`
    
    - `popup-starter`

      - > 各种 `starter`

      - `Autoconfigure`

      - `Redis`

      - `MongoDB`

      - `Elasticsearch`

      - `Logging`

      - `Classpath reader`

      - `Entity validator`

      - `...`



## 二、应用程序骨架

> 假定
>
> 项目名称:  `组织架构`
>
> 项目编码: `organization`

- `organization-client`

  - 提供远程调用能力, 供外部应用使用
  - `FeignClient`

- `organization-core`

  - `项目核心数据模型`
    - `dto`
    - `entity`
    - `payload`
    - `query`
    - `util`

- `organization-repository`

  - 项目仓储
  - `XxxRepository` | `XxxMapper`
  - `XxxMapper.xml`

- `organization-service`

  - 项目 `Assembler`

  - 项目服务

    - `XxxService` | `XxxServiceImpl`
      - `XxxRepository` `

  - 项目引擎  `Organization` `engine`

    - > 对外统一提供如下基础能力, 解决各个服务相互依赖注入从而引起代码混乱问题。

    -  `Assembler`, `Service`, `Repository`

    - `Beanfactroy`

    - `ApplocationContext`

    - `Environment`

- `organization-web`

  - 服务对外的 `API` 接口

