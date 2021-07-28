---
title: "Understanding application.yml"
layout: single
permalink: /basics/applicationyml/
toc: true
toc_label: "Table of Contents"
toc_sticky: true
breadcrumbs: true
---

## Introduction

A [**YAML**](https://en.wikipedia.org/wiki/YAML) file, with extension ***.yml** or **.yaml** , is a human readable file in which we can write data pairs in a suitable way by combinations of lists, maps and simple data. Another of the most important features of these files is the indentation. It is important to write these indented elements correctly, since if they are badly indented, they cannot be parsed correctly.

## Application.yml file description


### Endpoints

They allow you to monitor and interact with your application. Integrated endpoints are available, but adding custom endpoints is also allowed.  

- **endpoints: api:**

| Attribute | Values | Meaning |
|--|--|--|
| enabled | true, false | Active Spring Boot endpoints. |

**Example**
```yaml
endpoints:
   api:
      enabled: true
```


### Logging

- **logging: level:**

| Attribute | Values | Meaning |
|--|--|--|
| root | String | Default server log level set to INFO level |


**Example**
```yaml
logging:
   level:
      root: info
```


### CORS

Mechanism that allows restricted resources to be requested between domains.

- **ontimize:corsfilter:**

| Attribute | Values | Meaning |
|--|--|--|
| enabled | true, false | CORS filter enabled. |


- **ontimize:globalcors:**

CORS global configuration


| Attribute | Values | Meaning |
|--|--|--|
| cors-configurations | String | Configuration for this entrypoint |

- **ontimize:globalcors:cors-configurations:**

| Attribute | Values | Meaning |
|--|--|--|
| allowed-origins | String | Allow different origins |
| allowed-headers | String | Allow headers |
| exposed-headers | ["X-Auth-Token","Content-disposition","X-Requested-With"] | Exposed headers |
| allowed-methods | GET, POST, PUT, DELETE, OPTIONS | Allow HTTP methods |

**Example**
```yaml
ontimize:
    corsfilter:
      enabled: true
    globalcors:
      cors-configurations:
         '[/**]':
            allowed-origins: "*"
            allowed-headers: "*"
            exposed-headers: ["X-Auth-Token","Content-disposition","X-Requested-With"]       
            allowed-methods:
            - GET
            - POST
            - PUT
            - OPTIONS
            - DELETE
```

### JDBC 

- **ontimize:jdbc:**

| Attribute | Values | Meaning |
|--|--|--|
| name-convention | upper, lower, database | Convention of data columns name |
| sqlhandler | postgres, oracle, oracle12, sqlserver, hsqldb | SQL handler |


- **ontimize:jdbc:sql-condition-processor:**

SQL handler

| Attribute | Values | Meaning |
|--|--|--|
| upper-string | true, false | Use of uppercase |
| upper-like | true, false | String comparision using LIKE in uppercase |

**Example**
```yaml
jdbc:
      name-convention: upper
      sqlhandler: hsqldb
      sql-condition-processor:
         uppper-string: true
         upper-like: true
```


### Security

| Attribute | Values | Meaning |
|--|--|--|
| mode | default | Default security mode |

- **ontimize:security:role-information-service:**

Configure columns for user roles

| Attribute | Values | Meaning |
|--|--|--|
| role-repository | String | Repository which store the user role |
| role-name-column | String | Name of the column which store the role name |
| server-permission-query-id | String | Query ID to identify the query of server permissions |
| server-permission-name-column | String | Name of the columns which contains the name of the server permission |
| client-permission-query-id | String | Query ID for  client permissions |
| client-permission-column | String | Name of the columns which contains the name of the client permission |

- **ontimize:security:user-information-service:**

Information about the user

| Attribute | Values | Meaning |
|--|--|--|
| user-repository | String | Repository which stores the users of the application |
| user-login-column | String | Name of the column with the username |
| user-password-column | String | Name of the column with the password |
| query-id | String | Query identififer for login |
| other-data | List | Other query columns |

- **ontimize:security:user-role-information-service:**

Repository information about linking users and roles

| Attribute | Values | Meaning |
|--|--|--|
| user-role-repository | String | Repository name |
| query-id | String | Query identifier |
| role-login-column | String | Column which stores the user |
| role-name-column | String | Column which stores the role name |


**Example**
```yaml
security:
      mode: default
      role-information-iervice:
         role-repository: UserRoleDao
         role-name-column: ROLENAME
         server-permission-query-id: serverPermissions
         server-permission-name-column: PERMISSION_NAME
         client-permission-query-id: clientPermissions
         client-ermission-column: XMLCLIENTPERMISSION
      user-information-service:
         user-repository: UserDao
         user-login-column: USER_
         user-password-column: PASSWORD
         query-id: login
         other-data:
            - NAME
            - SURNAME
            - EMAIL
            - NIF
            - USERBLOCKED
            - LASTPASSWORDUPDATE
            - FIRSTLOGIN
      user-role-information-service:
         user-role-repository: UserRoleDao
         query-id: userRole
         role-login-column: USER_
         role-name-column: ROLENAME
```


### Server 

- **ontimize:server:**

| Attribute | Values | Meaning |
|--|--|--|
| port | int | Server port |

-**ontimize:server:servlet:**

| Attribute | Values | Meaning |
|--|--|--|
| context-path| String | customized servlet path |

- **ontimize:server:tomcat:**

| Attribute | Values | Meaning |
|--|--|--|
| uri-encoding | String | URI encoding |

- **ontimize:server:compression:**

| Attribute | Values | Meaning |
|--|--|--|
| enabled | true, false | Enable data compression |
| mime-types | String | Mime types |

**Example**
```yaml
server:
   servlet:
      context-path: /custom/path
   port: 33333
   tomcat:
      uri-encoding: UTF-8
   compression:
      enabled: true
      mime-types: application/json, application/xml
```

### Spring

- **ontimize:spring:datasource:**

Data source (DB connection)

| Attribute | Values | Meaning |
|--|--|--|
| driver-class-name | String | JDBC driver |
| jdbc-url | String | Connection URl |
| username | String | DB username |
| password | String | DB username password |
| initial-size | int | Initial size |
| test-on-borrow | true, false | Validation |

- **ontimize:spring:main:**

| Attribute | Values | Meaning |
|--|--|--|
| banner-mode | on, off | Remove the startup banner in the console |

- **ontimize:spring:session:**

| Attribute | Values | Meaning |
|--|--|--|
| store-type | String | Save the session in spring |

- **ontimize:spring:resources:**

| Attribute | Values | Meaning |
|--|--|--|
|static-locations | String | Path for satic resources |

- **ontimize:spring:autoconfigure:**

| Attribute | Values | Meaning |
|--|--|--|
| exclude | String | Delete Spring Boot auto-configuration files |


**Example**
```yaml
spring:
   datasource:
      driver-class-name: org.hsqldb.jdbcDriver
      jdbc-url: jdbc:hsqldb:hsql://localhost:9013/templateDB
      username: SA
      password:
      initial-size: 10
      test-on-borrow: true
   main:
      banner-mode: 'off'
   session:
      store-type: none
   resources:
      static-locations: classpath:/ngx/dist/
   autoconfigure:
      exclude: 
         org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration, org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration
```

## Complete Example
{% highlight yaml linenos %}
endpoints:
   api:
      enabled: true
logging:
   level:
      root: info
ontimize:
   corsfilter:
      enabled: true
   globalcors:
      cors-configurations:
         '[/**]':
            allowed-origins: "*"
            allowed-headers: "*"
            exposed-headers: ["X-Auth-Token","Content-disposition","X-Requested-With"]
            allowed-methods:
            - GET
            - POST
            - PUT
            - OPTIONS
            - DELETE
   jdbc:
      name-convention: upper
      sqlhandler: hsqldb
      sql-condition-processor:
         uppper-string: true
         upper-like: true
   security:
      mode: default
      role-information-service:
         role-repository: UserRoleDao
         role-name-column: ROLENAME
         server-permission-query-id: serverPermissions
         server-permission-name-column: PERMISSION_NAME
         client-permission-query-id: clientPermissions
         client-permission-column: XMLCLIENTPERMISSION
      user-information-service:
         user-repository: UserDao
         user-login-column: USER_
         user-password-column: PASSWORD
         query-id: login
         other-data:
            - NAME
            - SURNAME
            - EMAIL
            - NIF
            - USERBLOCKED
            - LASTPASSWORDUPDATE
            - FIRSTLOGIN
      user-role-information-service:
         user-role-repository: UserRoleDao
         query-id: userRole
         role-login-column: USER_
         role-name-column: ROLENAME
server:
   port: 33333
   tomcat:
      uri-encoding: UTF-8
   compression:
      enabled: true
      mime-types: application/json, application/xml
spring:
   datasource:
      driver-class-name: org.hsqldb.jdbcDriver
      jdbc-url: jdbc:hsqldb:hsql://localhost:9013/templateDB
      username: SA
      password:
      initial-size: 10
      test-on-borrow: true
   main:
      banner-mode: 'off'
   session:
      store-type: none
   resources:
      static-locations: classpath:/ngx/dist/
   autoconfigure:
      exclude: |
         org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration, org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration, org.springframework.boot.autoconfigure.security.FallbackWebSecurityAutoConfiguration
{% endhighlight %}
