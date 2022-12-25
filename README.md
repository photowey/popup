# `popup`

English | [中文](./README_CN.md) 

> `Popup` is a `Spring Cloud` `microservice` development scaffold based on `Spring Boot` `3.x`. It also supports `Spring Cloud Alibaba`.



## `Popctl`

> [`Popctl`](https://github.com/photowey/popctl) development scaffold cmd tools.



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
        
          - >  `Nacos` support
            >
            > Used to manage the version dependency between `Nacos ` client and starter
          
        - `popup-seata`
        
          - `Seata` support
          - `repeat`
      
    - `popup-component`
    
      - > `components`:
        
      - `Common`
      
      - `Core`
      
      - `...`
    
    - `popup-starter`
    
      - > `starters`
    
      - `Redis`
    
      - `MongoDB`
    
      - `Elasticsearch`
    
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