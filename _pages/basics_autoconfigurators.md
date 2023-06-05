---
title: "Autoconfigurators"
layout: single
permalink: /basics/autoconfigurators/
toc: true
toc_label: "Table of Contents"
toc_sticky: true
breadcrumbs: true
sidebar:
  title: "Ontimize Basics"
  nav: sidebar-basics
---
Autoconfigurators are an easy way to indicate common elements that need minimal customization in applications, such as database connection. These configurations are used within the **application.yml** file located inside the **boot** folder.

## AsyncTask
- **ontimize:asynctask:**

| Attribute | Values | Meaning |
|--|--|--|
| enable | *true* | Module loading property |
| engine | *database* | Indicates the storage engine that will be used for the report system (*database* for database engine) |
| url | *String* | The URL base path where the asynchronous tasks service will be exposed |

The configuration of the decoupled tasks system is done by setting up the necessary DAO for that system and annotating the service controller method. To see the configuration, check [this link](/ontimize-boot/basics/asynctask/).

**Example**

```yaml
ontimize:
   asynctask:
      enable: true
      engine: database
      url: /tasks
```

## DMS
- **ontimize:dms:**

| Attribute | Values | Meaning |
|--|--|--|
| engine | *odms* | Indicates the engine that will be used for the DMS system. Ontimize has an implementation of an engine, whose value is *odms*. |
| base-path | *String* | The path where the DMS files will be stored |

The configuration of DMS system is done by setting up the necessary DAOs for that system. To see the configuration, check [this link](/ontimize-boot/basics/dms/).

**Example**
```yaml
ontimize:
   dms:
      engine: odms
      base-path: file:/C:/applications/projectwiki/dms
```

## SDMS

**Important:** This module works only for Ontimize Boot version 3.11.0 or above. Actual release version: [![Ontimize Boot](https://img.shields.io/maven-central/v/com.ontimize.boot/ontimize-boot?label=Ontimize%20boot&style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.ontimize.boot/ontimize-boot)
{: .notice--warning}

- **ontimize:sdms:**

| Attribute | Values | Meaning |
|--|--|--|
| engine |s3 | Indicates that the implementation that handles documents via the Amazon AWS S3 service API will be used. |

**Example**
```yaml
ontimize:
   dms:
      engine: s3
```
### SDMS - S3 Engine
- **ontimize:sdms:s3:**

| Attribute | Values | Meaning |
|--|--|--|
| access-key | String | Indicates the `access-key` parámeter required to authenticate to the Amazon AWS S3 service API. |
| secret-key | String | Indicates the `secret-key` parámeter required to authenticate to the Amazon AWS S3 service API. |
| bucket | String | Indicates the `bucket` parámeter required to establish the S3 bucket into the SDMS. |
| region | String | Indicates the `region` parámeter required to establish the region where the S3 bucket is located. |

**Example**
```yaml
ontimize:
   dms:
     access-key: s3
   s3:
     access-key: ${S3_ACCESS_KEY}
     secret-key: ${S3_SECRET_KEY}
     bucket: ${S3_BUCKET}
     region: ${S3_REGION}
```

## Export
**Important:** This module works only for Ontimize Boot version 3.7.0 or above. Actual release version: [![Ontimize Boot](https://img.shields.io/maven-central/v/com.ontimize.boot/ontimize-boot?label=Ontimize%20boot&style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.ontimize.boot/ontimize-boot)
{: .notice--warning}
- **ontimize:export:**

| Attribute | Values | Meaning |
|--|--|--|
| url | *String* | Specifies the path to use the export system. |
| extension | *String* | Specifies the file extension to use. This *String* will append to the *url* path as a additional path.  |

The configuration of exporting system is done through autoconfigurators and dependencies. To see how to use and configure, check [this link](/ontimize-boot/basics/excelexport).

**Example**
```yaml
ontimize:
   export:
      url: /export
      extension: xlsx
```


## I18n

**ontimize:i18n:**

| Attribute | Values | Meaning |
|--|--|--|
|ref-bundle-repository| *String* | Name of the DAO containing information about the translation bundles |
|bundle-key-column| *String* | Column of the database table containing the translation bundle identifier |
|bundle-class-name-column| *String* | Column of the database table containing the name of the translation bundle class |
|bundle-description-column| *String* | Column of the database table containing the description of the translation bundle |
|ref-bundle-value-repository| *String* | Name of the database table containing information about the translations of each translation bundle |
|bundle-value-text-key-column| *String* | Column of the database table containing the key of a translation |
|bundle-value-key-column| *String* | Column of the database table containing the key of a translation |
|engine| *default* | Property to enable the i18n system. Need to have any value, commonly, *default*.  |

The configuration of the I18N system is done by setting up the necessary DAOs for that system. To see the configuration, check [this link](/ontimize-boot/basics/i18n/).

**Example**
```yaml
ontimize:
   i18n:
      ref-bundle-repository: OCDatabaseBundleDao 
      bundle-key-column: ID_I18N
      bundle-class-name-column: CLASS_NAME
      bundle-description-column: I18N_DESCRIPTION
      ref-bundle-value-repository: OCDatabaseBundleValueDao
      bundle-value-text-key-column: KEY
      bundle-value-key-column: ID_I18N_VALUE
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

## LDAP

- **ontimize:security:**

| Attribute | Value | Meaning |
|--|--|--|
| mode | *ldap* | Change the system security from *default* to *ldap* |

- **ldap:**

| Attribute | Values | Meaning |
|--|--|--|
| active | *true, false* | Enable or disable ldap security |
| host | *IP* | Ip host for ldap security |
| port | *Number* | Port of the host for ldap security |
| login-type | *DN, simple* | The login type indicates whether a full LDAP string with *DN* value or will be used or if the username will simply be provided with *simple* value |
| bind.dn | *String* | File to populate the LDAP server using a *.ldif* file |
| base.dn | *String* | List of base DNs. |
| domain | *String* | The domain name |

The LDAP security configuration is done through autoconfigurators. To see the settings, check [this link](/ontimize-boot/basics/ldap).

**Example**
```yaml
ontimize:
   security:
   mode: ldap
ldap: 
   active: true 
   host: 10.0.0.1
   port: 389
   login-type: simple
   bind.dn: 
   base.dn: 
   domain: yourdomain.com
```

## Mail

- **ontimize:mail:**

| Attribute | Values | Meaning |
|--|--|--|
| ref-repository | *String* | Name of the DAO containing the configuration information required for system configuration |
| filter-column-name | *String* | Name of the column in the database table containing the keys |
| value-column-name | *String* | Name of the database table column containing the values |
| query-id | *String* | Name of the DAO query to be executed. By default, is *default* |
| filter-column-value-encoding | *String* | Key name of the row in the key column containing the value for mail encoding |
| filter-column-value-host | *String* | Name of the key in the row of the key column containing the value for the host in the mail service |
| filter-column-value-port | *String* | Name of the key in the row of the key column containing the value for the port in the mail service |
| filter-column-value-protocol | *String* | Name of the key in the row of the key column containing the value for the protocol used in the mail service |
| filter-column-value-user | *String* | Name of the key in the row of the key column containing the value for the user in the mail service |
| filter-column-value-password | *String* | Name of the key in the row of the key column containing the value for the user password in the mail service |
| filter-column-value-java-mail-properties | *String* | Name of the key in the row of the key column containing the value for the mail propoerties in the mail service |
| engine | *String* | Enable or disable mail engine. To enable, have any value int this arribute. By default, use *default* value|

The configuration of the mail system is done by setting up the necessary DAOs for that system. To see the configuration, check [this link](/ontimize-boot/basics/mail/).

**Example**
```yaml
ontimize:
   mail:
      ref-repository: OCSettingsDao
      filter-column-name: SETTING_KEY
      value-column-name: SETTING_VALUE
      query-id: default
      filter-column-value-encoding: mail_encoding
      filter-column-value-host: mail_host
      filter-column-value-port: mail_port
      filter-column-value-protocol: mail_protocol
      filter-column-value-user: mail_user
      filter-column-value-password: mail_password
      filter-column-value-java-mail-properties: mail_properties
      engine: default
```

## Report

**Important:** This module works only for Ontimize Boot version 3.7.0 or above. Actual release version: [![Ontimize Boot](https://img.shields.io/maven-central/v/com.ontimize.boot/ontimize-boot?label=Ontimize%20boot&style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.ontimize.boot/ontimize-boot)
{: .notice--warning}

- **ontimize:report:**

| Attribute | Values | Meaning |
|--|--|--|
| enable | *true* | Module loading property |
| engine | *database*, *file* | Indicates the engine that will be used for the report system (*file* for file system engine or *database* for database engine) |
| base-path | *String* | The path where the report files will be stored (file system engine only) |


The configuration of the reports system is done by setting up the necessary DAOs for that system. To see the configuration, check [this link](/ontimize-boot/basics/reports/).


**Example**

*Database*
```yaml
ontimize:
   report:
      enable: true
      engine: database
```

*File system*
```yaml
ontimize:
   report:
      enable: true
      engine: file
      base-path: C:/applications/projectwiki/reports
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
| expiration-time | *Long* | JWT expiration time|
| refresh-token | *true*, *false* | Set *true* to refresh JWT, *false* otherwise|

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

The configuration of the rest of the Security System is done by setting up the necessary DAOs for that system. To see the configuration, check [this link](/ontimize-boot/basics/security/).

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
## TaskExecutor
- **ontimize:threadpool:**

| Attribute | Values | Meaning |
|--|--|--|
| coresize | *Integer* | The number of threads to keep in the pool, evenif they are idle |
| maxsize | *Integer* | The maximum number of threads to allow in the pool |
| keepalive | *Long* | When the number of threads is greater than the core, the maximum time that excess idle threads will wait for new tasks before terminating (in milliseconds) |
| timeout | *true*, *false* | Allow core threads to time out |

**Example**

```yaml
ontimize:
   threadpool:
      coresize: 1
      maxsize: 2147483647
      keepalive: 1000
      timeout: true
```
## Preferences
**Important:** This module works only for Ontimize Boot version 3.9.0 or above. Actual release version: [![Ontimize Boot](https://img.shields.io/maven-central/v/com.ontimize.boot/ontimize-boot?label=Ontimize%20Boot&style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.ontimize.boot/ontimize-boot)
{: .notice--warning}
- **ontimize:save-config:**

| Attribute | Values | Meaning |
|--|--|--|
| save-config | *true*, *false* | Allows save preferences in database |

- **ontimize:save-config-dao:**

| Attribute | Values | Meaning |
|--|--|--|
| save-config-dao | *String* | The name of the DAO for save preferences |

The configuration of the rest of the Preferences System is done by setting up the necessary DAOs for that system. To see the configuration, check [this link](/ontimize-boot/basics/preferences/).

**Example**

```yaml
ontimize:
   save-config: true
   save-config-dao: ConfigsDao
``` 