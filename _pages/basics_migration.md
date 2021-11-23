---
title: "Migration from Ontimize 1.x.x to 3.x.x"
layout: single
permalink: /basics/migration/
toc: true
toc_label: "Table of Contents"
toc_sticky: true
breadcrumbs: true
---

## Introduction

In this tutorial we are going to explain how to migrate a project from Ontimize version **1.x.x** that works with **JDK 8** to Ontimize version **3.x.x** that works with **JDK 11**. For this example we will download a project like the explained in this [training course](https://www.ontimize.com/xwiki/bin/view/Ontimize+Boot+Training/).

## Prerequisites
You can follow this tutorial using your own application, although for this example we will use an application created using the archetype that can be found [on this page](https://ontimize.github.io/ontimize-boot/getting_started/) and with a REST service. 

There are 2 options to follow this tutorial, clone the repository with the initial state and follow the tutorial step by step, or download the final example and see which files are new and which have been updated.

<div class="multiColumnRow multiColumnRowJustify">
<div class="multiColumn multiColumnGrow" >
  {{ "**Initial project**

    /$ git clone https://github.com/ontimize/ontimize-examples 
    /ontimize-examples$ cd ontimize-examples
    /ontimize-examples$ git checkout boot-migration-3.x.x-initial" 
    | markdownify }}
</div>
<div class="verticalDivider"></div>
<div class="multiColumn multiColumnGrow" >

  {{ "**Final example**

    /$ git clone https://github.com/ontimize/ontimize-examples 
    /ontimize-examples$ cd ontimize-examples
    /ontimize-examples$ git checkout boot-migration-3.x.x" 
    | markdownify }}

</div>
</div>

**Note:** To simplify the code being written, three dots (...) may appear in some parts of the code. This indicates that there may be previous code before and after those dots.
{: .notice--info}

## Steps
### Ontimize version
First of all we need to update the version of Ontimize (ontimize-boot-parent) located in the parent of the project.

<div class="multiColumnRow">
  <div class="multiColumn jstreeloader" >
<ul>
  <li data-jstree='{"opened":true, "icon":"fas fa-folder-open"}'>
  ontimize-examples
  <ul>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    hr-api
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                hr
                <ul>
                  <li data-jstree='{"icon":"fas fa-folder-open"}'>
                  api
                  <ul>
                    <li data-jstree='{"icon":"fas fa-folder-open"}'>
                    core
                    <ul>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      service
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>ICandidateService.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>IMasterService.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>IOfferService.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>IUserService.java</li>
                      </ul>
                      </li>
                    </ul>
                    </li>
                  </ul>
                  </li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    hr-boot
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                hr
                <ul>
                  <li data-jstree='{"icon":"fas fa-file"}'>ServerApplication.java</li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          resources
          <ul>
            <li data-jstree='{"icon":"fas fa-file"}'>application.yml</li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    hr-model
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          db
          <ul>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.properties</li>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.txt</li>
          </ul>
          </li>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                hr
                <ul>
                  <li data-jstree='{"icon":"fas fa-folder-open"}'>
                  model
                  <ul>
                    <li data-jstree='{"icon":"fas fa-folder-open"}'>
                    core
                    <ul>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      dao
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>CandidateDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>EducationDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>ExperienceLevelDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferCandidatesDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferCandidateStatusDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferStatusDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OriginDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>ProfileDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>StatusDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserRoleDao.java</li>
                      </ul>
                      </li>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      service
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>CandidateService.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>MasterService.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferService.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserService.java</li>
                      </ul>
                      </li>
                    </ul>
                    </li>
                  </ul>
                  </li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          resources
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            dao
            <ul>
              <li data-jstree='{"icon":"fas fa-file"}'>CandidateDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>EducationDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>ExperienceLevelDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>OfferCandidatesDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>OfferCandidateStatusDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>OfferDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>OfferStatusDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>OriginDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>placeholders.properties</li>
              <li data-jstree='{"icon":"fas fa-file"}'>ProfileDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>RoleDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>RoleServerPermissionDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>ServerPermissionDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>StatusDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>UserDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>UserRoleDao.xml</li>
            </ul>
            </li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    hr-ws
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                hr
                <ul>
                  <li data-jstree='{"icon":"fas fa-folder-open"}'>
                  ws
                  <ul>
                    <li data-jstree='{"icon":"fas fa-folder-open"}'>
                    core
                    <ul>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      rest
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>CandidateRestController.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>MainRestController.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>MasterRestController.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferRestController.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>TestRestController.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserRestController.java</li>
                      </ul>
                      </li>
                    </ul>
                    </li>
                  </ul>
                  </li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-file"}'>.gitignore</li>
    <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>pom.xml</li>
    <li data-jstree='{"icon":"fas fa-file"}'>README.md</li>
  </ul>
  </li>
</ul>
  </div>
  <div class="multiColumn" >

  {{"**pom.xml**" | markdownify}}

{%highlight xml %}
  <parent>
    <groupId>com.ontimize.boot</groupId>
    <artifactId>ontimize-boot-parent</artifactId>
    <version>3.6.0</version>
  </parent>
{% endhighlight %}

</div>
</div>

### Java version

In the project properties we will change the java version **from 1.8 to 11**, both for the main **pom.xml** and for the `hr-model` module **pom.xml**.

<div class="multiColumnRow">
  <div class="multiColumn jstreeloader" >
<ul>
  <li data-jstree='{"opened":true, "icon":"fas fa-folder-open"}'>
  ontimize-examples
  <ul>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    hr-api
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                hr
                <ul>
                  <li data-jstree='{"icon":"fas fa-folder-open"}'>
                  api
                  <ul>
                    <li data-jstree='{"icon":"fas fa-folder-open"}'>
                    core
                    <ul>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      service
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>ICandidateService.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>IMasterService.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>IOfferService.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>IUserService.java</li>
                      </ul>
                      </li>
                    </ul>
                    </li>
                  </ul>
                  </li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    hr-boot
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                hr
                <ul>
                  <li data-jstree='{"icon":"fas fa-file"}'>ServerApplication.java</li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          resources
          <ul>
            <li data-jstree='{"icon":"fas fa-file"}'>application.yml</li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    hr-model
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          db
          <ul>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.properties</li>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.txt</li>
          </ul>
          </li>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                hr
                <ul>
                  <li data-jstree='{"icon":"fas fa-folder-open"}'>
                  model
                  <ul>
                    <li data-jstree='{"icon":"fas fa-folder-open"}'>
                    core
                    <ul>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      dao
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>CandidateDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>EducationDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>ExperienceLevelDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferCandidatesDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferCandidateStatusDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferStatusDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OriginDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>ProfileDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>StatusDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserRoleDao.java</li>
                      </ul>
                      </li>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      service
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>CandidateService.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>MasterService.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferService.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserService.java</li>
                      </ul>
                      </li>
                    </ul>
                    </li>
                  </ul>
                  </li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          resources
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            dao
            <ul>
              <li data-jstree='{"icon":"fas fa-file"}'>CandidateDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>EducationDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>ExperienceLevelDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>OfferCandidatesDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>OfferCandidateStatusDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>OfferDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>OfferStatusDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>OriginDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>placeholders.properties</li>
              <li data-jstree='{"icon":"fas fa-file"}'>ProfileDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>RoleDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>RoleServerPermissionDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>ServerPermissionDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>StatusDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>UserDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>UserRoleDao.xml</li>
            </ul>
            </li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    hr-ws
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                hr
                <ul>
                  <li data-jstree='{"icon":"fas fa-folder-open"}'>
                  ws
                  <ul>
                    <li data-jstree='{"icon":"fas fa-folder-open"}'>
                    core
                    <ul>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      rest
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>CandidateRestController.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>MainRestController.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>MasterRestController.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferRestController.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>TestRestController.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserRestController.java</li>
                      </ul>
                      </li>
                    </ul>
                    </li>
                  </ul>
                  </li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-file"}'>.gitignore</li>
    <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>pom.xml</li>
    <li data-jstree='{"icon":"fas fa-file"}'>README.md</li>
  </ul>
  </li>
</ul>
  </div>
  <div class="multiColumn" >

  {{"**pom.xml**" | markdownify}}

{%highlight xml %}
  <properties>
    . . .
    <java.version>11</java.version>
    . . .
  </properties>
{% endhighlight %}

</div>
</div>

### Modifying application.yml

In the new version of Ontimize Boot, it has been decided to change the properties from **camelCase** to **kebab-case**, so you have to modify the `application.yml`.

```yaml
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
      ignore-paths: /app/**
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
   autoconfigure:
      exclude: |
         org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration, org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration, org.springframework.boot.autoconfigure.security.FallbackWebSecurityAutoConfiguration
```

### Renaming packages

With the new version, the **ontimize-core** library has been **merged** with **ontimize-jee**, and some classes have changed the name of their package, among them, the **EntityResult** Interface. To learn more about this class, check the following [link](https://www.ontimize.com/xwiki/bin/view/Ontimize+Boot+Training/Understanding+the+EntityResult). 

The package name of **EntityResult** has been renamed from `com.ontimize.db.EntityResult` to `com.ontimize.jee.common.dto.EntityResult`.

#### Api module

In the `hr-api` module we need to change the name of the package of this class in all the interfaces that are using it.

<div class="multiColumnRow">
  <div class="multiColumn jstreeloader" >
<ul>
  <li data-jstree='{"opened":true, "icon":"fas fa-folder-open"}'>
  ontimize-examples
  <ul>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    hr-api
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                hr
                <ul>
                  <li data-jstree='{"icon":"fas fa-folder-open"}'>
                  api
                  <ul>
                    <li data-jstree='{"icon":"fas fa-folder-open"}'>
                    core
                    <ul>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      service
                      <ul>
                        <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>ICandidateService.java</li>
                        <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>IMasterService.java</li>
                        <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>IOfferService.java</li>
                        <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>IUserService.java</li>
                      </ul>
                      </li>
                    </ul>
                    </li>
                  </ul>
                  </li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    hr-boot
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                hr
                <ul>
                  <li data-jstree='{"icon":"fas fa-file"}'>ServerApplication.java</li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          resources
          <ul>
            <li data-jstree='{"icon":"fas fa-file"}'>application.yml</li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    hr-model
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          db
          <ul>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.properties</li>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.txt</li>
          </ul>
          </li>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                hr
                <ul>
                  <li data-jstree='{"icon":"fas fa-folder-open"}'>
                  model
                  <ul>
                    <li data-jstree='{"icon":"fas fa-folder-open"}'>
                    core
                    <ul>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      dao
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>CandidateDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>EducationDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>ExperienceLevelDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferCandidatesDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferCandidateStatusDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferStatusDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OriginDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>ProfileDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>StatusDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserRoleDao.java</li>
                      </ul>
                      </li>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      service
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>CandidateService.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>MasterService.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferService.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserService.java</li>
                      </ul>
                      </li>
                    </ul>
                    </li>
                  </ul>
                  </li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          resources
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            dao
            <ul>
              <li data-jstree='{"icon":"fas fa-file"}'>CandidateDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>EducationDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>ExperienceLevelDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>OfferCandidatesDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>OfferCandidateStatusDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>OfferDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>OfferStatusDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>OriginDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>placeholders.properties</li>
              <li data-jstree='{"icon":"fas fa-file"}'>ProfileDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>RoleDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>RoleServerPermissionDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>ServerPermissionDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>StatusDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>UserDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>UserRoleDao.xml</li>
            </ul>
            </li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    hr-ws
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                hr
                <ul>
                  <li data-jstree='{"icon":"fas fa-folder-open"}'>
                  ws
                  <ul>
                    <li data-jstree='{"icon":"fas fa-folder-open"}'>
                    core
                    <ul>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      rest
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>CandidateRestController.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>MainRestController.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>MasterRestController.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferRestController.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>TestRestController.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserRestController.java</li>
                      </ul>
                      </li>
                    </ul>
                    </li>
                  </ul>
                  </li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-file"}'>.gitignore</li>
    <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    <li data-jstree='{"icon":"fas fa-file"}'>README.md</li>
  </ul>
  </li>
</ul>
  </div>
  <div class="multiColumn" >

  {{"**ICandidateService.java**" | markdownify}}

{%highlight java %}
package com.ontimize.hr.api.core.service;

. . .
import com.ontimize.jee.common.dto.EntityResult;
. . .

public interface ICandidateService {

 // CANDIDATE
 public EntityResult candidateQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
 public EntityResult candidateInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
 public EntityResult candidateUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
 public EntityResult candidateDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;

}
{% endhighlight %}

  {{"**IMasterService.java**" | markdownify}}

{%highlight java %}
package com.ontimize.hr.api.core.service;

. . .
import com.ontimize.jee.common.dto.EntityResult;
. . .

public interface IMasterService {

    . . .
	 // EDUCATION
	 public EntityResult educationQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	 public EntityResult educationInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
	 public EntityResult educationUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	 public EntityResult educationDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
     . . .
}
{% endhighlight %}

  {{"**IOfferService.java**" | markdownify}}

{%highlight java %}
package com.ontimize.hr.api.core.service;

. . .
import com.ontimize.jee.common.dto.EntityResult;
. . .

public interface IOfferService {

    . . .
	// OFFER
	public EntityResult offerQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	public EntityResult offerInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
	public EntityResult offerUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	public EntityResult offerDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
    . . .

}
{% endhighlight %}

  {{"**IUserService.java**" | markdownify}}

{%highlight java %}
package com.ontimize.hr.api.core.service;

. . .
import com.ontimize.jee.common.dto.EntityResult;
. . .

public interface IUserService {

	public EntityResult userQuery(Map<?, ?> keyMap, List<?> attrList);
	public EntityResult userInsert(Map<?, ?> attrMap);
	public EntityResult userUpdate(Map<?, ?> attrMap, Map<?, ?> keyMap);
	public EntityResult userDelete(Map<?, ?> keyMap);

}
{% endhighlight %}
</div>
</div>

#### Model module

Having changed the class in the interface, we also have to change it in the service. For this we go to the `hr-model` module and replace the import of all services.

<div class="multiColumnRow">
  <div class="multiColumn jstreeloader" >
<ul>
  <li data-jstree='{"opened":true, "icon":"fas fa-folder-open"}'>
  ontimize-examples
  <ul>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    hr-api
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                hr
                <ul>
                  <li data-jstree='{"icon":"fas fa-folder-open"}'>
                  api
                  <ul>
                    <li data-jstree='{"icon":"fas fa-folder-open"}'>
                    core
                    <ul>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      service
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>ICandidateService.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>IMasterService.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>IOfferService.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>IUserService.java</li>
                      </ul>
                      </li>
                    </ul>
                    </li>
                  </ul>
                  </li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    hr-boot
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                hr
                <ul>
                  <li data-jstree='{"icon":"fas fa-file"}'>ServerApplication.java</li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          resources
          <ul>
            <li data-jstree='{"icon":"fas fa-file"}'>application.yml</li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    hr-model
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          db
          <ul>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.properties</li>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.txt</li>
          </ul>
          </li>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                hr
                <ul>
                  <li data-jstree='{"icon":"fas fa-folder-open"}'>
                  model
                  <ul>
                    <li data-jstree='{"icon":"fas fa-folder-open"}'>
                    core
                    <ul>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      dao
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>CandidateDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>EducationDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>ExperienceLevelDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferCandidatesDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferCandidateStatusDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferStatusDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OriginDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>ProfileDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>StatusDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserRoleDao.java</li>
                      </ul>
                      </li>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      service
                      <ul>
                        <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>CandidateService.java</li>
                        <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>MasterService.java</li>
                        <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>OfferService.java</li>
                        <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>UserService.java</li>
                      </ul>
                      </li>
                    </ul>
                    </li>
                  </ul>
                  </li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          resources
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            dao
            <ul>
              <li data-jstree='{"icon":"fas fa-file"}'>CandidateDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>EducationDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>ExperienceLevelDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>OfferCandidatesDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>OfferCandidateStatusDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>OfferDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>OfferStatusDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>OriginDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>placeholders.properties</li>
              <li data-jstree='{"icon":"fas fa-file"}'>ProfileDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>RoleDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>RoleServerPermissionDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>ServerPermissionDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>StatusDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>UserDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>UserRoleDao.xml</li>
            </ul>
            </li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    hr-ws
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                hr
                <ul>
                  <li data-jstree='{"icon":"fas fa-folder-open"}'>
                  ws
                  <ul>
                    <li data-jstree='{"icon":"fas fa-folder-open"}'>
                    core
                    <ul>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      rest
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>CandidateRestController.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>MainRestController.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>MasterRestController.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferRestController.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>TestRestController.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserRestController.java</li>
                      </ul>
                      </li>
                    </ul>
                    </li>
                  </ul>
                  </li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-file"}'>.gitignore</li>
    <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    <li data-jstree='{"icon":"fas fa-file"}'>README.md</li>
  </ul>
  </li>
</ul>
  </div>
  <div class="multiColumn" >

  {{"**CandidateService.java**" | markdownify}}

{%highlight java %}
package com.ontimize.hr.model.core.service;

. . .
import com.ontimize.jee.common.dto.EntityResult;
. . .

@Service("CandidateService")
@Lazy
public class CandidateService implements ICandidateService {

	@Autowired
	private CandidateDao candidateDao;
    . . .
}
{% endhighlight %}

  {{"**MasterService.java**" | markdownify}}

{%highlight java %}
package com.ontimize.hr.model.core.service;

. . .
import com.ontimize.jee.common.dto.EntityResult;
. . .

@Service("MasterService")
@Lazy
public class MasterService implements MasterService {

	@Autowired
	private EducationDao educationDao;
    . . .
}
{% endhighlight %}

  {{"**OfferService.java**" | markdownify}}

{%highlight java %}
package com.ontimize.hr.model.core.service;

. . .
import com.ontimize.jee.common.dto.EntityResult;
. . .

@Service("OfferService")
@Lazy
public class OfferService implements IOfferService {

	@Autowired
	private OfferDao offerDao;
    . . .
}
{% endhighlight %}

  {{"**UserService.java**" | markdownify}}

{%highlight java %}
package com.ontimize.hr.model.core.service;

. . .
import com.ontimize.jee.common.dto.EntityResult;
. . .

@Lazy
@Service("UserService")
public class UserService implements IUserService {

	@Autowired
	private UserDao userDao;
    . . .
}
{% endhighlight %}
</div>
</div>

#### Ws module

In this case it is not just about changing the name of the package, it must also be taken into account that the `EntityResult` class is now an **Interface**, so an object cannot be instantiated from it. To do this, we will use the `EntityResultMapImpl` class. 

Go to the `hr-ws` module and replace the import of the EntityResult class in the rest controllers and make the following changes:

<div class="multiColumnRow">
  <div class="multiColumn jstreeloader" >
<ul>
  <li data-jstree='{"opened":true, "icon":"fas fa-folder-open"}'>
  ontimize-examples
  <ul>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    hr-api
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                hr
                <ul>
                  <li data-jstree='{"icon":"fas fa-folder-open"}'>
                  api
                  <ul>
                    <li data-jstree='{"icon":"fas fa-folder-open"}'>
                    core
                    <ul>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      service
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>ICandidateService.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>IMasterService.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>IOfferService.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>IUserService.java</li>
                      </ul>
                      </li>
                    </ul>
                    </li>
                  </ul>
                  </li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    hr-boot
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                hr
                <ul>
                  <li data-jstree='{"icon":"fas fa-file"}'>ServerApplication.java</li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          resources
          <ul>
            <li data-jstree='{"icon":"fas fa-file"}'>application.yml</li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    hr-model
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          db
          <ul>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.properties</li>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.txt</li>
          </ul>
          </li>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                hr
                <ul>
                  <li data-jstree='{"icon":"fas fa-folder-open"}'>
                  model
                  <ul>
                    <li data-jstree='{"icon":"fas fa-folder-open"}'>
                    core
                    <ul>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      dao
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>CandidateDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>EducationDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>ExperienceLevelDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferCandidatesDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferCandidateStatusDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferStatusDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OriginDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>ProfileDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>StatusDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserRoleDao.java</li>
                      </ul>
                      </li>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      service
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>CandidateService.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>MasterService.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>OfferService.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserService.java</li>
                      </ul>
                      </li>
                    </ul>
                    </li>
                  </ul>
                  </li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          resources
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            dao
            <ul>
              <li data-jstree='{"icon":"fas fa-file"}'>CandidateDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>EducationDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>ExperienceLevelDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>OfferCandidatesDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>OfferCandidateStatusDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>OfferDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>OfferStatusDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>OriginDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>placeholders.properties</li>
              <li data-jstree='{"icon":"fas fa-file"}'>ProfileDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>RoleDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>RoleServerPermissionDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>ServerPermissionDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>StatusDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>UserDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>UserRoleDao.xml</li>
            </ul>
            </li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    hr-ws
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                hr
                <ul>
                  <li data-jstree='{"icon":"fas fa-folder-open"}'>
                  ws
                  <ul>
                    <li data-jstree='{"icon":"fas fa-folder-open"}'>
                    core
                    <ul>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      rest
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>CandidateRestController.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>MainRestController.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>MasterRestController.java</li>
                        <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>OfferRestController.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>TestRestController.java</li>
                        <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>UserRestController.java</li>
                      </ul>
                      </li>
                    </ul>
                    </li>
                  </ul>
                  </li>
                </ul>
                </li>
              </ul>
              </li>
            </ul>
            </li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-file"}'>.gitignore</li>
    <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    <li data-jstree='{"icon":"fas fa-file"}'>README.md</li>
  </ul>
  </li>
</ul> 
  </div>
  <div class="multiColumn" >

  {{"**OfferRestController.java**" | markdownify}}

{%highlight java %}
package com.ontimize.hr.ws.core.rest;

. . .
import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicExpression;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicField;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicOperator;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
. . .

@RestController
@RequestMapping("/offers")
@ComponentScan(basePackageClasses = { com.ontimize.hr.api.core.service.IOfferService.class })
public class OfferRestController extends ORestController<IOfferService> {

	. . .

	@RequestMapping(value = "currentOffers/search", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult currentOffersSearch(@RequestBody Map<String, Object> req) {
		try {
			List<String> columns = (List<String>) req.get("columns");
			Map<String, Object> key = new HashMap<String, Object>();
			key.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY,
					searchBetween(OfferDao.ATTR_START_DATE));
			return offerService.offerQuery(key, columns);
		} catch (Exception e) {
			e.printStackTrace();
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			return res;
		}
	}

	@RequestMapping(value = "yearOffers/search", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult yearOffersSearch(@RequestBody Map<String, Object> req) {
		try {
			List<String> columns = (List<String>) req.get("columns");
			Map<String, Object> filter = (Map<String, Object>) req.get("filter");
			int year = (int) filter.get("YEAR");
			Map<String, Object> key = new HashMap<String, Object>();
			key.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY,
					searchBetweenWithYear(OfferDao.ATTR_START_DATE, year));
			return offerService.offerQuery(key, columns);
		} catch (Exception e) {
			e.printStackTrace();
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			return res;
		}
	}

    . . .

}
{% endhighlight %}

  {{"**UserRestController.java**" | markdownify}}

{%highlight java %}
package com.ontimize.hr.ws.core.rest;

. . .
import com.ontimize.jee.common.dto.EntityResult;
. . .

@RestController
@RequestMapping("/users")
@ComponentScan(basePackageClasses={com.ontimize.hr.api.core.service.IUserService.class})
public class UserRestController extends ORestController<IUserService> {

	. . .
	@RequestMapping(
		value = "/login",
		method = RequestMethod.POST,
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EntityResult> login() {
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
{% endhighlight %}
</div>
</div>
