---
title: "Getting started"
layout: single
permalink: /getting_started/
toc: true
toc_label: "Table of Contents"
toc_sticky: true
---

This page allows you to know how to start developing an application with Ontimize Boot in a simple way. This project includes an HSQLDB database already prepared to start the application. It can be replaced.

## Prerequisites

 - [Java JDK 8](https://adoptopenjdk.net/?variant=openjdk8&jvmVariant=hotspot)
 - [Maven](https://maven.apache.org/download.cgi)
 - Any IDE that supports Spring Boot ([Eclipse](https://www.eclipse.org/downloads/packages/release/2020-09/r/eclipse-ide-enterprise-java-developers), [IntelliJ](https://www.jetbrains.com/es-es/idea/download/#section=windows)...)
 - [Git](https://git-scm.com/) (Optional)

## Create application from archetype

Open a console and type the next command:

    mvn org.apache.maven.plugins:maven-archetype-plugin:2.4:generate -DgroupId=YOUR_GROUP_ID -DartifactId=YOUR_ARTIFACT_ID -Dversion=YOUR_VERSION -Dpackage=YOUR.GROUPID.ARTIFACTID -DarchetypeGroupId=com.ontimize -DarchetypeArtifactId=ontimize-boot-archetype -DarchetypeVersion=1.0.0 -DinteractiveMode=false -DarchetypeCatalog=https://artifactory.imatia.com/public-artifactory/ontimize-archetypes/archetype-catalog.xml

### Command explanation

| Argument | Meaning |
|--|--|
| mvn | Maven CLI |
| org.apache.maven.plugins:maven-archetype-plugin:2.4:generate | Use the Maven Archetype Plugin (v2.4) for create a new project from an archetype|
|-DgroupId=YOUR_GROUP_ID|Your project **groupId**|
|-DartifactId=YOUR_ARTIFACT_ID|Your project **artifactId**|
|-Dversion=YOUR_VERSION|Your project **version**|
|-Dpackage=YOUR.GROUPID.ARTIFACTID | Sets the package on which the project will be based (e.g.: In our examples, this will be *com.ontimize.projectwiki*)|
|-DarchetypeGroupId=com.ontimize|**groupId** of the Ontimize Boot archetype|
|-DarchetypeArtifactId=ontimize-boot-archetype|**artifactId** of the Ontimize Boot archetype|
|-DarchetypeVersion=1.0.0|**version** of the Ontimize Boot archetype|
|-DinteractiveMode=false| Forced to **skip interactive mode** and use the paramaters in the command|
|-DarchetypeCatalog=https://artifactory.imatia.com/public-artifactory/ontimize-archetypes/archetype-catalog.xml| URL where you can find the archetype catalog  |

## Start the project

<div class="multiColumnRow">
  <div class="multiColumn jstreeloader" >
    <ul>
  <li data-jstree='{"selected": true, "opened":true, "icon":"fas fa-folder-open"}'>
  app
  <ul>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    api
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
            api
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              core
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                service
                <ul>
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
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    boot
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
            boot
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              core
              <ul>
                <li data-jstree='{"icon":"fas fa-file"}'>ServerApplication.java</li>
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
    model
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
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.lck</li>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.log</li>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.properties</li>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.script</li>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.txt</li>
          </ul>
          </li>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
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
                  <li data-jstree='{"icon":"fas fa-file"}'>UserDao.java</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>UserRoleDao.java</li>
                </ul>
                </li>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                service
                <ul>
                  <li data-jstree='{"icon":"fas fa-file"}'>UserService.java</li>
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
              <li data-jstree='{"icon":"fas fa-file"}'>placeholders.properties</li>
              <li data-jstree='{"icon":"fas fa-file"}'>RoleDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>RoleServerPermissionDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>ServerPermissionDao.xml</li>
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
    ws
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
            ws
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              core
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                rest
                <ul>
                  <li data-jstree='{"icon":"fas fa-file"}'>MainRestController.java</li>
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
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    <li data-jstree='{"icon":"fas fa-file"}'>README.md</li>
  </ul>
  </li>
</ul>
  </div>
  <div class="multiColumn">
{{ "To start the project, it is necessary to start both the database and the server.
The first thing to do is to execute the `mvn install` command inside the project's root folder

    $ cd app
    /app$ mvn install
" | markdownify }}
  </div>
</div>

### Start the database

<div class="multiColumnRow">
  <div class="multiColumn jstreeloader" >
    <ul>
  <li data-jstree='{"opened":true, "icon":"fas fa-folder-open"}'>
  app
  <ul>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    api
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
            api
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              core
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                service
                <ul>
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
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    boot
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
            boot
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              core
              <ul>
                <li data-jstree='{"icon":"fas fa-file"}'>ServerApplication.java</li>
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
    <li data-jstree='{ "selected": true, "icon":"fas fa-folder-open"}'>
    model
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
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.lck</li>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.log</li>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.properties</li>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.script</li>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.txt</li>
          </ul>
          </li>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
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
                  <li data-jstree='{"icon":"fas fa-file"}'>UserDao.java</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>UserRoleDao.java</li>
                </ul>
                </li>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                service
                <ul>
                  <li data-jstree='{"icon":"fas fa-file"}'>UserService.java</li>
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
              <li data-jstree='{"icon":"fas fa-file"}'>placeholders.properties</li>
              <li data-jstree='{"icon":"fas fa-file"}'>RoleDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>RoleServerPermissionDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>ServerPermissionDao.xml</li>
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
    ws
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
            ws
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              core
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                rest
                <ul>
                  <li data-jstree='{"icon":"fas fa-file"}'>MainRestController.java</li>
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
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    <li data-jstree='{"icon":"fas fa-file"}'>README.md</li>
  </ul>
  </li>
</ul>
  </div>
  <div class="multiColumn">
  {{ "Next, we navigate to the `model` folder to start the HSQLDB database

    /app$ cd model
    /app/model$ mvn exec:java -Prun_database
" | markdownify }}
  </div>
</div>

### Start the server

<div class="multiColumnRow">
  <div class="multiColumn jstreeloader" >
    <ul>
  <li data-jstree='{"opened":true, "icon":"fas fa-folder-open"}'>
  app
  <ul>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    api
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
            api
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              core
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                service
                <ul>
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
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"selected": true, "icon":"fas fa-folder-open"}'>
    boot
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
            boot
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              core
              <ul>
                <li data-jstree='{"icon":"fas fa-file"}'>ServerApplication.java</li>
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
    model
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
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.lck</li>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.log</li>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.properties</li>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.script</li>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.txt</li>
          </ul>
          </li>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          java
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
                  <li data-jstree='{"icon":"fas fa-file"}'>UserDao.java</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>UserRoleDao.java</li>
                </ul>
                </li>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                service
                <ul>
                  <li data-jstree='{"icon":"fas fa-file"}'>UserService.java</li>
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
              <li data-jstree='{"icon":"fas fa-file"}'>placeholders.properties</li>
              <li data-jstree='{"icon":"fas fa-file"}'>RoleDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>RoleServerPermissionDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>ServerPermissionDao.xml</li>
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
    ws
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
            ws
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              core
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                rest
                <ul>
                  <li data-jstree='{"icon":"fas fa-file"}'>MainRestController.java</li>
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
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    <li data-jstree='{"icon":"fas fa-file"}'>README.md</li>
  </ul>
  </li>
</ul>
  </div>
  <div class="multiColumn">
  {{ "To start the server, open a new console in the root folder of the project, navigate to `boot` folder and type the following command

    /app$ cd boot
    /app/boot$ mvn spring-boot:run
" | markdownify }}
  </div>
</div>

## Test the application

You can check if the application is working by making a request, for example, to the following address:

    http://localhost:33333/users/user?columns=USER_

Through applications such as Postman or from the browser

In both cases, the access must be done with a user and password example:

        User: demo
    Password: demouser