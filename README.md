# `popup`

English | [中文](./README_CN.md) 

> `Popup` is a `Spring Cloud` `microservice` development scaffold based on `Spring Boot` `3.x`. It also supports `Spring Cloud Alibaba`.



## `Popctl`

> [`Popctl`](https://github.com/photowey/popctl) development scaffold `cmd` tools.



## `Run local`

> Add `maven` `MAVEN_REPO` `env`
>
>
> `E.g.`: 
>
> `MAVEN_REPO` = `D:\software\maven\nexus-maven\repo\repository`
>
> `MAVEN_REPO` = `/Users/photowey/xxx/maven/repo/repository`



## `Ext project`

> [mybatis-plus-ext](https://github.com/photowey/mybatis-plus-ext) is an extension project of `Mybatis-plus`, this project will use it to enhance `Repository` (`Mapper`), This greatly simplifies development.



## 一、`Structure`

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
        
          - Database dependencies
          -  `Mybatis-Plus`
          - `...`
        
        - `popup-app-database-mysql`
        
          - `MySQL` driver
        
        - `popup-app-database-pgsql`
        
          - `PG` driver
        
        - `popup-app-persistence`
        
          - > packaging `JAR`
            >
            > Database auto-configuration
        
          - `database`
        
          - `cache`
        
          - ...
        
        - --
        
        - `popup-nacos`

          - > `Nacos` support
            >
            > Used to manage the version dependency between `Nacos` client and
            starter

        - `popup-seata`

          - `Seata` support
          - `repeat`

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

      - `Common`

      - `Core`

      - `Crypto`

      - `Eventbus`

      - `Queue`

      - `Sync`

      - `Test`

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

## 二、`Application structure`

> `Such as`:
>
> project code: `organization`

- `organization-client`

  - Provides `RPC` access capability
  - `FeignClient`

- `organization-core`

  - `Core data-model`
    - `dto`
    - `entity`
    - `payload`
    - `query`
    - `util`

- `organization-repository`

  - Repository
  - `XxxRepository` | `XxxMapper`
  - `XxxMapper.xml`

- `organization-service`

  -  `Assembler`
- Service
  
  - `XxxService` | `XxxServiceImpl`
      - `XxxRepository` `
  -   `Organization` `engine`
- Provides management and centralized access to each component of the project
    - `Assembler`, `Service`, `Repository`

    - `Beanfactroy`

    - `ApplocationContext`

    - `Environment`

- `organization-web`

  - Expose web `API`