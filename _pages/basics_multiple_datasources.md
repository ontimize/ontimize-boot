---
title: "Multiple Datasources"
layout: single
permalink: /basics/multipledatasources/
toc: true
toc_label: "Table of Contents"
toc_sticky: true
breadcrumbs: true
sidebar:
  title: "Ontimize Basics"
  nav: sidebar-basics
---

**Important:** This module works only for Ontimize Boot version 3.9.0 or above. Actual release version: [![Ontimize Boot](https://img.shields.io/maven-central/v/com.ontimize.boot/ontimize-boot?label=Ontimize%20Boot&style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.ontimize.boot/ontimize-boot)
{: .notice--warning}

# Introduction

Ontimize Boot allows a single application to query multiple data sources, thus allowing you to separate the user and permission tables in a database and use one or more other data sources to query the rest of the information you want by configuring the data source you want to query in each DAO. 

# Prerequisites
**Note:** You can follow this tutorial using your own application, although for this example we will use an application created using the archetype that can be found [on this page](https://ontimize.github.io/ontimize-boot/getting_started/), with a REST service and two HSQLDB databases.
{: .notice--info}

There are 2 options to follow this tutorial, clone the repository with the initial state and follow the tutorial step by step, or download the final example and see which files are new and which have been updated.

<div class="multiColumnRow multiColumnRowJustify">
  <div class="multiColumn multiColumnGrow" >
  {{ "**Initial project**
 
    /$ git clone https://github.com/ontimize/ontimize-examples
    /ontimize-examples$ cd ontimize-examples
    /ontimize-examples$ git checkout boot-multidatasource-initial"
    | markdownify }}
   
</div>
<div class="verticalDivider"></div>
<div class="multiColumn multiColumnGrow">

  {{ "**Final example**
    
    /$ git clone https://github.com/ontimize/ontimize-examples
    /ontimize-examples$ cd ontimize-examples
    /ontimize-examples$ git checkout boot-multidatasource"
    | markdownify }}


  </div>
</div>

# Steps
## Modify application.yml file
Although you can keep the data source already indicated in the file, you can delete it without any problem. New data sources will be added in the *.yml under the `ontimize.datasources` properties, followed by a name for that data source, and then the same settings as required for the data source you normally use.


<div class="multiColumnRow">
  <div class="multiColumn jstreeloader" >
<ul>
  <li data-jstree='{"opened":true, "icon":"fas fa-folder-open"}'>
  ontimize-examples
  <ul>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    projectwiki-api
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
                projectwiki
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
          </ul></li>
        </ul>
        </li>
      </ul>
      </li>
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    projectwiki-boot
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
                projectwiki
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
            <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>application.yml</li>
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
    projectwiki-model
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
          db2
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
                projectwiki
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
                        <li data-jstree='{"icon":"fas fa-file"}'>UserDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserRoleDao.java</li>
                      </ul>
                      </li>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      service
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>CandidateService.java</li>
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
    projectwiki-ws
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
                projectwiki
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
    <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    <li data-jstree='{"icon":"fas fa-file"}'>README.md</li>
  </ul>
  </li>
</ul>
  </div>
  <div class="multiColumn multiColumnGrow" >
  {{ "**application.yml**"| markdownify }}

{% highlight yaml %}
...

ontimize:
   datasources:
      dbone:
         driver-class-name: org.hsqldb.jdbcDriver
         jdbc-url: jdbc:hsqldb:hsql://localhost:9013/templateDB
         username: SA
         password:
      dbtwo:
         driver-class-name: org.hsqldb.jdbcDriver
         jdbc-url: jdbc:hsqldb:hsql://localhost:9013/templateDB
         username: SA
         password:

...
{% endhighlight %}

  </div>
</div>

**Modify DAO's datasource**

In the DAOs the data source must be modified to indicate the correct data source, and the database table that belongs to that data source. In the case of this example, as indicated in the *.yml file, that would be `dbone` and `dbtwo`.

<div class="multiColumnRow">
  <div class="multiColumn jstreeloader" >
<ul>
  <li data-jstree='{"opened":true, "icon":"fas fa-folder-open"}'>
  ontimize-examples
  <ul>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    projectwiki-api
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
                projectwiki
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
    projectwiki-boot
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
                projectwiki
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
    projectwiki-model
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
          db2
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
                projectwiki
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
                        <li data-jstree='{"icon":"fas fa-file"}'>UserDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserRoleDao.java</li>
                      </ul>
                      </li>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      service
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>CandidateService.java</li>
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
              <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>CandidateDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>placeholders.properties</li>
              <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>RoleDao.xml</li>
              <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>RoleServerPermissionDao.xml</li>
              <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>ServerPermissionDao.xml</li>
              <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>UserDao.xml</li>
              <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>UserRoleDao.xml</li>
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
    projectwiki-ws
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
                projectwiki
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
    <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    <li data-jstree='{"icon":"fas fa-file"}'>README.md</li>
  </ul>
  </li>
</ul>
  </div>
  <div class="multiColumn multiColumnGrow" >

{{ "**RoleDao.xml**"| markdownify }}
{% highlight xml %}
<?xml version="1.0" encoding="UTF-8"?>
<JdbcEntitySetup
    xmlns="http://www.ontimize.com/schema/jdbc"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.ontimize.com/schema/jdbc http://www.ontimize.com/schema/jdbc/ontimize-jdbc-dao.xsd"
    catalog="" schema="${mainschema}" table="TROLE"
    datasource="dbone" sqlhandler="dbSQLStatementHandler">
    ...
{% endhighlight %}

{{ "**RoleServerPermissionDao.xml**"| markdownify }}
{% highlight xml %}
<?xml version="1.0" encoding="UTF-8"?>
<JdbcEntitySetup
    xmlns="http://www.ontimize.com/schema/jdbc"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.ontimize.com/schema/jdbc http://www.ontimize.com/schema/jdbc/ontimize-jdbc-dao.xsd"
    catalog="" schema="${mainschema}" table="TROLE_SERVER_PERMISSION"
    datasource="dbone" sqlhandler="dbSQLStatementHandler">
    ...
{% endhighlight %}

{{ "**ServerPermissionDao.xml**"| markdownify }}
{% highlight xml %}
<?xml version="1.0" encoding="UTF-8"?>
<JdbcEntitySetup
    xmlns="http://www.ontimize.com/schema/jdbc"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.ontimize.com/schema/jdbc http://www.ontimize.com/schema/jdbc/ontimize-jdbc-dao.xsd"
    catalog="" schema="${mainschema}" table="TSERVER_PERMISSION"
    datasource="dbone" sqlhandler="dbSQLStatementHandler">
    ...
{% endhighlight %}

{{ "**UserDao.xml**"| markdownify }}
{% highlight xml %}
<?xml version="1.0" encoding="UTF-8"?>
<JdbcEntitySetup
    xmlns="http://www.ontimize.com/schema/jdbc"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.ontimize.com/schema/jdbc http://www.ontimize.com/schema/jdbc/ontimize-jdbc-dao.xsd"
    catalog="" schema="${mainschema}" table="TUSER"
    datasource="dbone" sqlhandler="dbSQLStatementHandler">
    ...
{% endhighlight %}

{{ "**UserRoleDao.xml**"| markdownify }}
{% highlight xml %}
<?xml version="1.0" encoding="UTF-8"?>
<JdbcEntitySetup
	xmlns="http://www.ontimize.com/schema/jdbc"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.ontimize.com/schema/jdbc http://www.ontimize.com/schema/jdbc/ontimize-jdbc-dao.xsd"
	catalog="" schema="${mainschema}" table="TUSER_ROLE"
	datasource="dbone" sqlhandler="dbSQLStatementHandler">
  ...
{% endhighlight %}

{{ "**CandidateDao.xml**"| markdownify }}
{% highlight xml %}
<?xml version="1.0" encoding="UTF-8"?>
<JdbcEntitySetup
	xmlns="http://www.ontimize.com/schema/jdbc"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.ontimize.com/schema/jdbc http://www.ontimize.com/schema/jdbc/ontimize-jdbc-dao.xsd"
	catalog="" schema="${mainschema}" table="CANDIDATE"
	datasource="dbtwo" sqlhandler="dbSQLStatementHandler">
  ...
{% endhighlight %}
  </div>
</div>

## Modify SQL handler
At this point more data sources can be queried, as shown in the example, but they all have the same SQL handler. It is quite possible that, when multiple data sources are queried, unlike the example, they are databases of different technologies. To do this, in the same way as it is allowed to do with the data sources, the SQL handlers of each of the DAOs are changed. There is a SQL handler that is configured by the `ontimize.jdbc.sqlhandler` and `ontimize.jdbc.sql-condition-processor.*` properties, called by default **dbSQLStatementHandler**. From this version, it is no longer necessary to indicate which SQL handler we will use in the `ontimize.jdbc.sqlhandler` property, but we will indicate its name, listed below. By default, they already have some pre-established configurations, although they can be modified without any problem in the `application.yml` file.

<table>
<thead>
  <tr>
    <th>Handler</th>
    <th>Bean name</th>
    <th>Default configuration</th>
    <th rowspan="2">Property</th>
  </tr>
  <tr>
  </tr>
</thead>
<tbody>
  <tr>
    <td rowspan="2">default</td>
    <td rowspan="2">dbSQLStatementHandler</td>
    <td><strong>Upper string</strong></td>
    <td><em>false</em></td>
    <td>ontimize.jdbc.sql-condition-processor.upper-string</td>
  </tr>
  <tr>
    <td><strong>Upper like</strong></td>
    <td><em>true</em></td>
    <td>ontimize.jdbc.sql-condition-processor.upper-like</td>
  </tr>
  <tr>
    <td rowspan="2">postgres</td>
    <td rowspan="2">postgresSQLStatementHandler</td>
   <td><strong>Upper string</strong></td>
    <td><em>false</em></td>
    <td>ontimize.jdbc.postgres-sql-condition-processor.upper-string</td>
  </tr>
  <tr>
    <td><strong>Upper like</strong></td>
    <td><em>true</em></td>
    <td>ontimize.jdbc.postgres-sql-condition-processor.upper-like</td>
  </tr>
  <tr>
    <td rowspan="2">oracle</td>
    <td rowspan="2">oracleSQLStatementHandler</td>
   <td><strong>Upper string</strong></td>
    <td><em>false</em></td>
    <td>ontimize.jdbc.oracle-sql-condition-processor.upper-string</td>
  </tr>
  <tr>
    <td><strong>Upper like</strong></td>
    <td><em>true</em></td>
    <td>ontimize.jdbc.oracle-sql-condition-processor.upper-like</td>
  </tr>
  <tr>
    <td rowspan="2">oracle12</td>
    <td rowspan="2">oracle12SQLStatementHandler</td>
   <td><strong>Upper string</strong></td>
    <td><em>false</em></td>
    <td>ontimize.jdbc.oracle12-sql-condition-processor.upper-string</td>
  </tr>
  <tr>
    <td><strong>Upper like</strong></td>
    <td><em>true</em></td>
    <td>ontimize.jdbc.oracle12-sql-condition-processor.upper-like</td>
  </tr>
  <tr>
    <td rowspan="2">sqlserver</td>
    <td rowspan="2">sqlserverSQLStatementHandler</td>
   <td><strong>Upper string</strong></td>
    <td><em>false</em></td>
    <td>ontimize.jdbc.sqlserver-sql-condition-processor.upper-string</td>
  </tr>
  <tr>
    <td><strong>Upper like</strong></td>
    <td><em>true</em></td>
    <td>ontimize.jdbc.sqlserver-sql-condition-processor.upper-like</td>
  </tr>
  <tr>
    <td rowspan="2">hsqldb</td>
    <td rowspan="2">hsqldbSQLStatementHandler</td>
   <td><strong>Upper string</strong></td>
    <td><em>false</em></td>
    <td>ontimize.jdbc.hsqldb-sql-condition-processor.upper-string</td>
  </tr>
  <tr>
    <td><strong>Upper like</strong></td>
    <td><em>true</em></td>
    <td>ontimize.jdbc.hsqldb-sql-condition-processor.upper-like</td>
  </tr>
  <tr>
    <td rowspan="2">mysql</td>
    <td rowspan="2">mysqlSQLStatementHandler</td>
   <td><strong>Upper string</strong></td>
    <td><em>false</em></td>
    <td>ontimize.jdbc.mysql-sql-condition-processor.upper-string</td>
  </tr>
  <tr>
    <td><strong>Upper like</strong></td>
    <td><em>true</em></td>
    <td>ontimize.jdbc.mysql-sql-condition-processor.upper-like</td>
  </tr>
</tbody>
</table>

<div class="multiColumnRow">
  <div class="multiColumn jstreeloader" >
<ul>
  <li data-jstree='{"opened":true, "icon":"fas fa-folder-open"}'>
  ontimize-examples
  <ul>
    <li data-jstree='{"icon":"fas fa-folder-open"}'>
    projectwiki-api
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
                projectwiki
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
    projectwiki-boot
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
                projectwiki
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
            <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>application.yml</li>
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
    projectwiki-model
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
          db2
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
                projectwiki
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
                        <li data-jstree='{"icon":"fas fa-file"}'>UserDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserRoleDao.java</li>
                      </ul>
                      </li>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      service
                      <ul>
                        <li data-jstree='{"icon":"fas fa-file"}'>CandidateService.java</li>
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
              <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>CandidateDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>placeholders.properties</li>
              <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>RoleDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>RoleServerPermissionDao.xml</li>
              <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>ServerPermissionDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>UserDao.xml</li>
              <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>UserRoleDao.xml</li>
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
    projectwiki-ws
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
                projectwiki
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
    <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    <li data-jstree='{"icon":"fas fa-file"}'>README.md</li>
  </ul>
  </li>
</ul>
  </div>
  <div class="multiColumn multiColumnGrow" >

Modified application.yml (will use default values) and some SQL handlers

{{ "**aplication.yml**"| markdownify }}
{% highlight yml %}
...
ontimize:
   datasources:
      dbone:
         driver-class-name: org.hsqldb.jdbcDriver
         jdbc-url: jdbc:hsqldb:hsql://localhost:9013/templateDB
         username: SA
         password:
         initial-size: 10
         test-on-borrow: true
      dbtwo:
         driver-class-name: org.hsqldb.jdbcDriver
         jdbc-url: jdbc:hsqldb:hsql://localhost:9014/templateDB
         username: SA
         password:
         initial-size: 10
         test-on-borrow: true
   jdbc:
      name-convention: upper
...
{% endhighlight %}

{{ "**RoleDao.xml**"| markdownify }}
{% highlight xml %}
<?xml version="1.0" encoding="UTF-8"?>
<JdbcEntitySetup
    xmlns="http://www.ontimize.com/schema/jdbc"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.ontimize.com/schema/jdbc http://www.ontimize.com/schema/jdbc/ontimize-jdbc-dao.xsd"
    catalog="" schema="${mainschema}" table="TROLE"
    datasource="dbone" sqlhandler="hsqldbSQLStatementHandler">
    ...
{% endhighlight %}


{{ "**ServerPermissionDao.xml**"| markdownify }}
{% highlight xml %}
<?xml version="1.0" encoding="UTF-8"?>
<JdbcEntitySetup
    xmlns="http://www.ontimize.com/schema/jdbc"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.ontimize.com/schema/jdbc http://www.ontimize.com/schema/jdbc/ontimize-jdbc-dao.xsd"
    catalog="" schema="${mainschema}" table="TSERVER_PERMISSION"
    datasource="dbone" sqlhandler="hsqldbSQLStatementHandler">
    ...
{% endhighlight %}


{{ "**UserRoleDao.xml**"| markdownify }}
{% highlight xml %}
<?xml version="1.0" encoding="UTF-8"?>
<JdbcEntitySetup
	xmlns="http://www.ontimize.com/schema/jdbc"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.ontimize.com/schema/jdbc http://www.ontimize.com/schema/jdbc/ontimize-jdbc-dao.xsd"
	catalog="" schema="${mainschema}" table="TUSER_ROLE"
	datasource="dbone" sqlhandler="hsqldbSQLStatementHandler">
  ...
{% endhighlight %}

{{ "**CandidateDao.xml**"| markdownify }}
{% highlight xml %}
<?xml version="1.0" encoding="UTF-8"?>
<JdbcEntitySetup
	xmlns="http://www.ontimize.com/schema/jdbc"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.ontimize.com/schema/jdbc http://www.ontimize.com/schema/jdbc/ontimize-jdbc-dao.xsd"
	catalog="" schema="${mainschema}" table="CANDIDATE"
	datasource="dbtwo" sqlhandler="hsqldbSQLStatementHandler">
  ...
{% endhighlight %}
  </div>
</div>