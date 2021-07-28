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
| corsConfigurations | String | Configuration for this entrypoint |

- **ontimize:globalcors:corsConfigurations:**

| Attribute | Values | Meaning |
|--|--|--|
| allowedOrigins | String | Allow different origins |
| allowedHeaders | String | Allow headers |
| exposedHeaders | ["X-Auth-Token","Content-disposition","X-Requested-With"] | Exposed headers |
| allowedMethods | GET, POST, PUT, DELETE, OPTIONS | Allow HTTP methods |

**Example**
```yaml
ontimize:
    corsfilter:
      enabled: true
    globalcors:
      corsConfigurations:
         '[/**]':
            allowedOrigins: "*"
            allowedHeaders: "*"
            exposedHeaders: ["X-Auth-Token","Content-disposition","X-Requested-With"]       
            allowedMethods:
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
| nameConvention | upper, lower, database | Convention of data columns name |
| sqlhandler | postgres, oracle, oracle12, sqlserver, hsqldb | SQL handler |


- **ontimize:jdbc:sqlConditionProcessor:**

SQL handler

| Attribute | Values | Meaning |
|--|--|--|
| upperString | true, false | Use of uppercase |
| upperLike | true, false | String comparision using LIKE in uppercase |

**Example**
```yaml
jdbc:
      nameConvention: upper
      sqlhandler: hslqdb
      sqlConditionProcessor:
         upperString: true
         upperLike: true
```


### Security

| Attribute | Values | Meaning |
|--|--|--|
| mode | default | Default security mode |

- **ontimize:security:roleInformationService:**

Configure columns for user roles

| Attribute | Values | Meaning |
|--|--|--|
| roleRepository | String | Repository which store the user role |
| roleNameColumn | String | Name of the column which store the role name |
| serverPermissionQueryId | String | Query ID to identify the query of server permissions |
| serverPermissionNameColumn | String | Name of the columns which contains the name of the server permission |
| clientPermissionQueryId | String | Query ID for  client permissions |
| clientPermissionColumn | String | Name of the columns which contains the name of the client permission |

- **ontimize:security:userInformationService:**

Information about the user

| Attribute | Values | Meaning |
|--|--|--|
| userRepository | String | Repository which stores the users of the application |
| userLoginColumn | String | Name of the column with the username |
| userPasswordColumn | String | Name of the column with the password |
| queryId | String | Query identififer for login |
| otherData | List | Other query columns |

- **ontimize:security:userRoleInformationService:**

Repository information about linking users and roles

| Attribute | Values | Meaning |
|--|--|--|
| userRoleRepository | String | Repository name |
| queryId | String | Query identifier |
| roleLoginColumn | String | Column which stores the user |
| roleNameColumn | String | Column which stores the role name |


**Example**
```yaml
security:
      mode: default
      roleInformationService:
         roleRepository: UserRoleDao
         roleNameColumn: ROLENAME
         serverPermissionQueryId: serverPermissions
         serverPermissionNameColumn: PERMISSION_NAME
         clientPermissionQueryId: clientPermissions
         clientPermissionColumn: XMLCLIENTPERMISSION
      userInformationService:
         userRepository: UserDao
         userLoginColumn: USER_
         userPasswordColumn: PASSWORD
         queryId: login
         otherData:
            - NAME
            - SURNAME
            - EMAIL
            - NIF
            - USERBLOCKED
            - LASTPASSWORDUPDATE
            - FIRSTLOGIN
      userRoleInformationService:
         userRoleRepository: UserRoleDao
         queryId: userRole
         roleLoginColumn: USER_
         roleNameColumn: ROLENAME
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
| jdbcUrl | String | Connection URl |
| username | String | DB username |
| password | String | DB username password |
| initialSize | int | Initial size |
| testOnBorrow | true, false | Validation |

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
      jdbcUrl: jdbc:hsqldb:hsql://localhost:9013/templateDB
      username: SA
      password:
      initialSize: 10
      testOnBorrow: true
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
      corsConfigurations:
         '[/**]':
            allowedOrigins: "*"
            allowedHeaders: "*"
            exposedHeaders: ["X-Auth-Token","Content-disposition","X-Requested-With"]
            allowedMethods:
            - GET
            - POST
            - PUT
            - OPTIONS
            - DELETE
   jdbc:
      nameConvention: upper
      sqlhandler: hsqldb
      sqlConditionProcessor:
         upperString: true
         upperLike: true
   security:
      mode: default
      roleInformationService:
         roleRepository: UserRoleDao
         roleNameColumn: ROLENAME
         serverPermissionQueryId: serverPermissions
         serverPermissionNameColumn: PERMISSION_NAME
         clientPermissionQueryId: clientPermissions
         clientPermissionColumn: XMLCLIENTPERMISSION
      userInformationService:
         userRepository: UserDao
         userLoginColumn: USER_
         userPasswordColumn: PASSWORD
         queryId: login
         otherData:
            - NAME
            - SURNAME
            - EMAIL
            - NIF
            - USERBLOCKED
            - LASTPASSWORDUPDATE
            - FIRSTLOGIN
      userRoleInformationService:
         userRoleRepository: UserRoleDao
         queryId: userRole
         roleLoginColumn: USER_
         roleNameColumn: ROLENAME
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
      jdbcUrl: jdbc:hsqldb:hsql://localhost:9013/templateDB
      username: SA
      password:
      initialSize: 10
      testOnBorrow: true
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
