---
title: "Autoconfigurators"
layout: single
permalink: /basics/autoconfigurators/
toc: true
toc_label: "Table of Contents"
toc_sticky: true
breadcrumbs: true
---
Autoconfigurators are an easy way to indicate common elements that need minimal customization in applications, such as database connection. These configurations are used within the **application.yml** file located inside the **boot** folder.

## DMS
- **ontimize:dms:**

| Attribute | Values | Meaning |
|--|--|--|
| engine | *odms* | Indicates the engine that will be used for the DMS system. Ontimize has an implementation of an engine, whose value is *odms*. |
| base-path | *String* | The path where the DMS files will be stored |

The configuration of DMS system is done by setting up the necessary DAOs for that system. To see the configuration, see [this link](/ontimize-boot/v3/basics/dms/).

**Example**
```yaml
ontimize:
   dms:
      engine: odms
      base-path: file:/C:/applications/QSAllComponents_Jee/dms
```

## I18n

**ontimize:i18n:**

| Attribute | Values | Meaning |
|--|--|--|
|refBundleRepository| *String* | Name of the DAO containing information about the translation bundles |
|bundleKeyColumn| *String* | Column of the database table containing the translation bundle identifier |
|bundleClassNameColumn| *String* | Column of the database table containing the name of the translation bundle class |
|bundleDescriptionColumn| *String* | Column of the database table containing the description of the translation bundle |
|refBundleValueRepository| *String* | Name of the database table containing information about the translations of each translation bundle |
|bundleValueTextKeyColumn| *String* | Column of the database table containing the key of a translation |
|bundleValueKeyColumn| *String* | Column of the database table containing the key of a translation |
|engine| *default* | Property to enable the i18n system. Need to have any value, commonly, *default*.  |

<!--The configuration of the I18N system is done by setting up the necessary DAOs for that system. To see the configuration, see [this link](/ontimize-boot/basics/i18n/).-->

**Example**
```yaml
ontimize:
   i18n:
      refBundleRepository: OCDatabaseBundleDao 
      bundleKeyColumn: ID_I18N
      bundleClassNameColumn: CLASS_NAME
      bundleDescriptionColumn: I18N_DESCRIPTION
      refBundleValueRepository: OCDatabaseBundleValueDao
      bundleValueTextKeyColumn: KEY
      bundleValueKeyColumn: ID_I18N_VALUE
      engine: default    
```

## JDBC

- **ontimize:jdbc:**

| Attribute | Values | Meaning |
|--|--|--|
|name-convention|*upper*, *lower*, *database*| Indicate the nomenclature of the columns in the DB, in lower case, upper case or as it appears in the database |
|sqlhandler|*postgres*, *oracle*, *oracle12*, *sqlserver*, *hsqldb*| Indicates which SQL statement handler will be used to communicate with the database |

- **ontimize:jdbc:sql-condition-processor:**

| Attribute | Values | Meaning |
|--|--|--|
|upper-string|*true*, *false*| Use uppercase strings in WHERE conditions |
|upper-like|*true*, *false*| Use uppercase strings in LIKE conditions |

**Example**
```yaml
ontimize:
   jdbc:
      name-convention: upper
      sqlhandler: hsqldb
      sql-condition-processor:
         upper-string: true
         upper-like: true
```

## Mail

- **ontimize:mail:**

| Attribute | Values | Meaning |
|--|--|--|
| refRepository | *String* | Name of the DAO containing the configuration information required for system configuration |
| filterColumnName | *String* | Name of the column in the database table containing the keys |
| valueColumnName | *String* | Name of the database table column containing the values |
| queryId | *String* | Name of the DAO query to be executed. By default, is *default* |
| filterColumnValueEncoding | *String* | Key name of the row in the key column containing the value for mail encoding |
| filterColumnValueHost | *String* | Name of the key in the row of the key column containing the value for the host in the mail service |
| filterColumnValuePort | *String* | Name of the key in the row of the key column containing the value for the port in the mail service |
| filterColumnValueProtocol | *String* | Name of the key in the row of the key column containing the value for the protocol used in the mail service |
| filterColumnValueUser | *String* | Name of the key in the row of the key column containing the value for the user in the mail service |
| filterColumnValuePassword | *String* | Name of the key in the row of the key column containing the value for the user password in the mail service |
| filterColumnValueJavaMailProperties | *String* | Name of the key in the row of the key column containing the value for the mail propoerties in the mail service |
| engine | *String* | Enable or disable mail engine. To enable, have any value int this arribute. By default, use *default* value|

The configuration of the mail system is done by setting up the necessary DAOs for that system. To see the configuration, see [this link](/ontimize-boot/v3/basics/mail/).

**Example**
```yaml
ontimize:
   mail:
      refRepository: OCSettingsDao
      filterColumnName: SETTING_KEY
      valueColumnName: SETTING_VALUE
      queryId: default
      filterColumnValueEncoding: mail_encoding
      filterColumnValueHost: mail_host
      filterColumnValuePort: mail_port
      filterColumnValueProtocol: mail_protocol
      filterColumnValueUser: mail_user
      filterColumnValuePassword: mail_password
      filterColumnValueJavaMailProperties: mail_properties
      engine: default
```

## REST

- **ontimize:corsfilter:**

| Attribute | Values | Meaning |
|--|--|--|
| enabled | *true*, *false* | Enable or disable CORS filter |

- **ontimize:globalcors:cors-configurations**  
Indicates the entrypoint to be configured, with the properties for each one. In general, the entrypoint [/**] is configured entirely.

| Attribute | Values | Meaning |
|--|--|--|
| allowed-origins | *\** | Set the origins to allow, the special value \* allows all domains. By default this is not set|
| allowed-headers | *\** | Set the list of headers that a pre-flight request can list as allowed for use during an actual request. The special value \* allows actual requests to send any header. A header name is not required to be listed if it is one of: *Cache-Control*, *Content-Language*, *Expires*, *Last-Modified* or *Pragma*). By default this is not set.|
| exposed-headers |  | Set the list of response headers other than simple headers (i.e. *Cache-Control*, *Content-Language*, *Content-Type*, *Expires*, *Last-Modified* or *Pragma* that an actual response might have and can be exposed. Note that \* is not a valid exposed header value. By default this is not set. |
| allowed-methods | *List* | Set the HTTP methods to allow, e.g. *GET*, *POST*, *PUT*, etc. The special value \* allows all methods. If not set, only *GET* and *HEAD* are allowed. By default this is not set. Note: CORS checks use values from "Forwarded" [RFC7239](http://tools.ietf.org/html/rfc7239), *X-Forwarded-Host*, *X-Forwarded-Port*, and *X-Forwarded-Proto* headers, if present, in order to reflect the client-originated address. Consider using the *ForwardedHeaderFilter* in order to choose from a central place whether to extract and use, or to discard such headers. See the Spring Framework reference for more on this filter. |
| maxAge | *Number* | Configure how long, in seconds, the response from a pre-flight request can be cached by clients. By default this is not set. |
| allow-credentials | *-* | Whether user credentials are supported. By default this is not set (i.e. user credentials are not supported). |

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
            allowed-hethods:
            - GET
            - POST
            - PUT
            - OPTIONS
            - DELETE
```

## Security

- **ontimize:security:**

| Attribute | Values | Meaning |
|--|--|--|
| mode | *default* | Use *default* to enable the security mode for Ontimize Boot |
| ignore-paths | *String* | Paths in server thant will not be securized |
| service-path | *String* | Establish the service path. By default, */\*\** |

- **ontimize:security:jwt:** *Not required, enabled by default*

| Attribute | Values | Meaning |
|--|--|--|
| password | *String* | JWT password |
| expirationTime | *Long* | JWT expiration time|
| refreshToken | *true*, *false* | Set *true* to refresh JWT, *false* otherwise|

- **ontimize:security:user-information-service:**

| Attribute | Values | Meaning |
|--|--|--|
| query-id | *String* | Name of the DAO query identifier. |
| user-login-column | *String* | Database column that stores the username |
| user-password-column | *String* | Database column that stores the password |
| user-need-check-pass-column | *String* | Database column that stores whether the password requires updating at the next use |
| user-repository | *String* | Name of the DAO containing information about users |
| other-data | *List* | Extra data to store from the user logged |

- **ontimize:security:role-information-service:**

| Attribute | Values | Meaning |
|--|--|--|
| role-repository | *String* | Name of the DAO containing information about users |
| role-name-column | *String* | Database column that stores the role name |
| server-permission-query-id | *String* | Name of the DAO query identifier for server permissions |
| server-permission-name-column | *String* | Database column that stores the server permissions |
| client-permission-query-id | *String* | Name of the DAO query identifier for client permissions |
| client-permission-column | *List* | Database column that stores the client permissions |

- **ontimize:security:user-role-information-service:**

| Attribute | Values | Meaning |
|--|--|--|
| user-role-repository | *String* | Name of the DAO containing relation between users and its profiles |
| query-id | *String* | Name of the DAO query identifier |
| role-login-column | *String* | Database column that stores the username |
| role-name-column | *String* | Database column that stores the role name |

The configuration of the rest of the I18N system is done by setting up the necessary DAOs for that system. To see the configuration, see [this link](/ontimize-boot/v3/basics/security/).

**Example**
```yaml
ontimize:
  security:
    mode: default
    ignore-paths: /news/**, /products/**
    user-information-service:
      user-repository: OCLoginProfilesDao
      query-id: login
      user-login-column: USER_
      user-password-column: PASSWORD
      other-data:
        - NAME
        - SURNAME
        - EMAIL
        - NIF
        - USERBLOCKED
        - LASTPASSWORDUPDATE
        - FIRSTLOGIN
    user-role-information-service:
      user-role-repository: OCLoginProfilesDao
      query-id: userRole
      role-login-column: USER_
      role-name-column: ROLENAME
    role-information-service:
      role-repository: OCLoginProfilesDao
      role-name-column: ROLENAME
      server-permission-query-id: serverPermissions
      server-permission-name-column: PERMISSION_NAME
      client-permission-query-id: clientPermissions
      client-permission-column: XMLCLIENTPERMISSION
```
