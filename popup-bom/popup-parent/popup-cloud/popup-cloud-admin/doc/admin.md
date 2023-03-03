# `Spring Boot Admin`

## 1.`Dependencies`

```xml
 <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-actuator</artifactId>
 </dependency>
 <dependency>
     <groupId>de.codecentric</groupId>
     <artifactId>spring-boot-admin-starter-server</artifactId>
 </dependency>
 <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
  </dependency>
```



## 2.`Configuration`

```yml
server:
  port: 9111
spring:
  boot:
    admin:
      ui:
        title: monitor
      client:
        instance:
          metadata:
            tags:
              environment: local
      probed-endpoints: health,env,metrics,httptrace:trace,threaddump:dump,jolokia,info,logfile,refresh,flyway,liquibase,heapdump,loggers,auditevents
      monitor:
        default-timeout: 20000
  security:
    user:
      name: admin
      password: admin
management:   
  trace:
    http:
      enabled: true
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: always
```



## 3.`Security`

### `2.x`

```java
@Configuration
@EnableWebSecurity
public class SecurityConfigure {

    private final String adminContextPath;

    public SecurityConfigure(AdminServerProperties adminServerProperties) {
        this.adminContextPath = adminServerProperties.getContextPath();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(adminContextPath + "/");

        http.authorizeRequests()
                .antMatchers(adminContextPath + "/assets/**").permitAll()
                .antMatchers(adminContextPath + "/login").permitAll()
                .antMatchers( "/actuator/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage(adminContextPath + "/login").successHandler(successHandler).and()
                .logout().logoutUrl(adminContextPath + "/logout").and()
                .httpBasic().and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringAntMatchers(
                        adminContextPath + "/instances",
                        adminContextPath + "/actuator/**"
                );

        return http.build();
    }
}
```



### `3.x`

```java
@Configuration
@EnableWebSecurity
public class SecurityConfigure {

    private final AdminServerProperties properties;

    public SecurityConfigure(AdminServerProperties serverProperties) {
        this.properties = serverProperties;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String adminContextPath = this.properties.getContextPath();

        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(adminContextPath + "/");

        // @formatter:off
        http.authorizeHttpRequests()
                .requestMatchers(adminContextPath + "/assets/**").permitAll()
                .requestMatchers(adminContextPath + "/login").permitAll()
                .requestMatchers( "/actuator/**").permitAll()
                .anyRequest().authenticated()
            .and()
                .formLogin()
                .loginPage(adminContextPath + "/login")
                .successHandler(successHandler)
            .and()
                .logout()
                .logoutUrl(adminContextPath + "/logout")
            .and()
                .httpBasic()
            .and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers(
                        adminContextPath + "/instances",
                        adminContextPath + "/actuator/**"
                );
        // @formatter:on

        return http.build();
    }
}
```

