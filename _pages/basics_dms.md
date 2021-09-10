---
title: "DMS System"
layout: single
permalink: /basics/dms/
toc: true
toc_label: "Table of Contents"
toc_sticky: true
breadcrumbs: true
---

# Introduction
A **D**ocument **M**anagement **S**ystem (**DMS**) is a system that allows you to store files and keep track of the versions of those files. Ontimize Boot provides a DMS system that allows to store the files that are associated to the different records of a database table.

# Previous concepts
- **Document** (or **workspace**): It is superentity into which several files can be grouped. 
- File: The file is the generic representation of a file. A file groups several versions of itself.
- Version: Is the relationship to a physical file.
- **Category** (or **folder**): Is a way of grouping files within the document.

# Prerequisites
You can follow this tutorial using your own application, although for this example we will use an application created using the archetype that can be found [on this page](https://ontimize.github.io/ontimize-boot/getting_started/) and with a REST service. 

There are 2 options to follow this tutorial, clone the repository with the initial state and follow the tutorial step by step, or download the final example and see which files are new and which have been updated.

<div class="multiColumnRow multiColumnRowJustify">
<div class="multiColumn multiColumnGrow" >
  {{ "**Initial project**

    /$ git clone https://github.com/ontimize/ontimize-examples 
    /ontimize-examples$ cd ontimize-examples
    /ontimize-examples$ git checkout boot-dms-initial" 
    | markdownify }}
</div>
<div class="verticalDivider"></div>
<div class="multiColumn multiColumnGrow" >

  {{ "**Final example**

    /$ git clone https://github.com/ontimize/ontimize-examples 
    /ontimize-examples$ cd ontimize-examples
    /ontimize-examples$ git checkout boot-dms" 
    | markdownify }}

</div>
</div>

**Note:** To simplify the code being written, three dots (...) may appear in some parts of the code. This indicates that there may be previous code before and after those dots.
{: .notice--info}

# Steps
## Database
### DMS Tables

With the database started, we create the new tables that will store the DMS information.

{% highlight sql linenos %}
CREATE TABLE TDMS_DOC(ID_DMS_DOC INTEGER IDENTITY NOT NULL PRIMARY KEY,UPDATE_DATE TIMESTAMP,UPDATE_BY_ID INTEGER,DOC_NAME VARCHAR(255) NOT NULL,OWNER_ID INTEGER NOT NULL,DOC_DESCRIPTION CLOB(1G),DOC_KEYWORDS VARCHAR(255));
CREATE TABLE TDMS_DOC_FILE(ID_DMS_DOC_FILE INTEGER IDENTITY NOT NULL PRIMARY KEY,FILE_NAME VARCHAR(255) NOT NULL,ID_DMS_DOC INTEGER NOT NULL,FILE_TYPE VARCHAR(255),ID_DMS_DOC_CATEGORY INTEGER);
CREATE TABLE TDMS_DOC_FILE_VERSION(ID_DMS_DOC_FILE_VERSION INTEGER IDENTITY NOT NULL PRIMARY KEY,FILE_PATH VARCHAR(500),VERSION INTEGER NOT NULL,FILE_DESCRIPTION CLOB(1G),IS_ACTIVE CHARACTER(1) NOT NULL,FILE_ADDED_DATE TIMESTAMP NOT NULL,FILE_ADDED_USER_ID INTEGER NOT NULL,ID_DMS_DOC_FILE INTEGER NOT NULL,THUMBNAIL BLOB(1G),FILE_SIZE INTEGER);
CREATE TABLE TDMS_DOC_PROPERTY(ID_DMS_DOC_PROPERTY INTEGER IDENTITY NOT NULL PRIMARY KEY,DOC_PROPERTY_KEY VARCHAR(255) NOT NULL,DOC_PROPERTY_VALUE VARCHAR(255),ID_DMS_DOC INTEGER NOT NULL);
CREATE TABLE TDMS_RELATED_DOC(ID_DMS_RELATED_PROPERTY INTEGER IDENTITY NOT NULL PRIMARY KEY,ID_DMS_DOC_MASTER INTEGER NOT NULL,ID_DMS_DOC_CHILD INTEGER NOT NULL);
CREATE TABLE TDMS_DOC_CATEGORY(ID_DMS_DOC_CATEGORY INTEGER IDENTITY NOT NULL PRIMARY KEY,ID_DMS_DOC INTEGER NOT NULL,ID_DMS_DOC_CATEGORY_PARENT INTEGER,CATEGORY_NAME VARCHAR(255) NOT NULL);
{% endhighlight %}

Once the tables have been created, we add the foreign keys

{% highlight sql linenos %}
ALTER TABLE TDMS_DOC_FILE ADD CONSTRAINT TDMS_DOC_FILE_FK FOREIGN KEY(ID_DMS_DOC) REFERENCES TDMS_DOC(ID_DMS_DOC);
ALTER TABLE TDMS_DOC_FILE_VERSION ADD CONSTRAINT TDMS_DOC_FILE_VERSION_FK FOREIGN KEY(ID_DMS_DOC_FILE) REFERENCES TDMS_DOC_FILE(ID_DMS_DOC_FILE);
ALTER TABLE TDMS_DOC_PROPERTY ADD CONSTRAINT TDMS_DOC_PROPERTY_FK FOREIGN KEY(ID_DMS_DOC) REFERENCES TDMS_DOC(ID_DMS_DOC)
ALTER TABLE TDMS_RELATED_DOC ADD CONSTRAINT TDMS_RELATED_DOC_FK FOREIGN KEY(ID_DMS_DOC_MASTER) REFERENCES TDMS_DOC(ID_DMS_DOC);
ALTER TABLE TDMS_RELATED_DOC ADD CONSTRAINT TDMS_RELATED_DOC_FK_1 FOREIGN KEY(ID_DMS_DOC_CHILD) REFERENCES TDMS_DOC(ID_DMS_DOC);
ALTER TABLE TDMS_DOC_CATEGORY ADD CONSTRAINT TDMS_DOC_CATEGORY_FK FOREIGN KEY(ID_DMS_DOC) REFERENCES TDMS_DOC(ID_DMS_DOC);
ALTER TABLE TDMS_DOC_FILE ADD CONSTRAINT TDMS_DOC_FILE_FK_1 FOREIGN KEY(ID_DMS_DOC_CATEGORY) REFERENCES TDMS_DOC_CATEGORY(ID_DMS_DOC_CATEGORY);
{% endhighlight %}

### Link DMS table with entity table
In this example we want each new candidate added to the application to have its own space to store documents, so we will modify the `CANDIDATES` table to contain a column that stores the primary key of the **document** (or **workspace**) that will be associated with it.

{% highlight sql linenos %}
ALTER TABLE CANDIDATE ADD ID_DMS_DOC INTEGER;
{% endhighlight %}

{% highlight sql linenos %}
ALTER TABLE CANDIDATE ADD CONSTRAINT CANDIDATE_FK FOREIGN KEY(ID_DMS_DOC) REFERENCES TDMS_DOC(ID_DMS_DOC);
{% endhighlight %}

## Server
### Add DMS dependencies

<div class="multiColumnRow">
<div class="multiColumn jstreeloader">
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
      <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>pom.xml</li>
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
      <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-file"}'>.gitignore</li>
    <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    <li data-jstree='{"icon":"fas fa-file"}'>README.md</li>
  </ul>
  </li>
</ul>
</div>
<div class="multiColumn multiColumnGrow">
  {{ "**model/pom.xml**"| markdownify }}

{% highlight xml %}
...
<dependencies>
  ...
  <dependency>
    <groupId>com.ontimize.jee.dms</groupId>
    <artifactId>ontimize-jee-dms-server</artifactId>
  </dependency>

  <dependency>
    <groupId>com.ontimize.jee.dms</groupId>
    <artifactId>ontimize-jee-dms-common</artifactId>
  </dependency>
  ...
</dependencies>
...
{% endhighlight %}

  {{ "**ws/pom.xml**"| markdownify }}

{% highlight xml %}
...
<dependencies>
  ...
    <dependency>
        <groupId>com.ontimize.jee.dms</groupId>
        <artifactId>ontimize-jee-dms-rest</artifactId>
    </dependency>
  ...
</dependencies>
...
{% endhighlight %}
</div>
</div>

### Add DMS DAO and modify Candidate DAO
A specific DAO will be created for each table in the DMS system, and each of them will implement a different interface. In turn, the candidate DAO will be modified to reflect the new column it contains.

<div class="multiColumnRow">
<div class="multiColumn jstreeloader">
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
              <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>DMSCategoryDao.xml</li>
              <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>DMSDocumentDao.xml</li>
              <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>DMSDocumentFileDao.xml</li>
              <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>DMSDocumentFileVersionDao.xml</li>
              <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>DMSDocumentPropertyDao.xml</li>
              <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>DMSRelatedDocumentDao.xml</li>
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
    <li data-jstree='{"icon":"fas fa-file"}'>.gitignore</li>
    <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    <li data-jstree='{"icon":"fas fa-file"}'>README.md</li>
  </ul>
  </li>
</ul>

</div>
<div class="multiColumn multiColumnGrow">

{{ "**DMSCategoryDao.xml**" | markdownify}}
{% highlight xml linenos %}
<?xml version="1.0" encoding="UTF-8"?>
<JdbcEntitySetup
  xmlns="http://www.ontimize.com/schema/jdbc"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.ontimize.com/schema/jdbc http://www.ontimize.com/schema/jdbc/ontimize-jdbc-dao.xsd"
  catalog="" schema="${mainschema}" table="TDMS_DOC_CATEGORY"
  datasource="mainDataSource" sqlhandler="dbSQLStatementHandler">
  <DeleteKeys>
    <Column>ID_DMS_DOC_CATEGORY</Column>
  </DeleteKeys>
  <UpdateKeys>
    <Column>ID_DMS_DOC_CATEGORY</Column>
  </UpdateKeys>
  <GeneratedKey>ID_DMS_DOC_CATEGORY</GeneratedKey>
</JdbcEntitySetup>
{% endhighlight %}

{{ "**DMSDocumentDao.xml**" | markdownify}}
{% highlight xml linenos %}
<?xml version="1.0" encoding="UTF-8"?>
<JdbcEntitySetup
  xmlns="http://www.ontimize.com/schema/jdbc"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.ontimize.com/schema/jdbc http://www.ontimize.com/schema/jdbc/ontimize-jdbc-dao.xsd"
  catalog="" schema="${mainschema}" table="TDMS_DOC"
  datasource="mainDataSource" sqlhandler="dbSQLStatementHandler">
  <DeleteKeys>
    <Column>ID_DMS_DOC</Column>
  </DeleteKeys>
  <UpdateKeys>
    <Column>ID_DMS_DOC</Column>
  </UpdateKeys>
  <GeneratedKey>ID_DMS_DOC</GeneratedKey>
</JdbcEntitySetup>
{% endhighlight %}


{{ "**DMSDocumentFileDao.xml**" | markdownify}}
{% highlight xml linenos %}
<?xml version="1.0" encoding="UTF-8"?>
<JdbcEntitySetup
  xmlns="http://www.ontimize.com/schema/jdbc"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.ontimize.com/schema/jdbc http://www.ontimize.com/schema/jdbc/ontimize-jdbc-dao.xsd"
  catalog="" schema="${mainschema}" table="TDMS_DOC_FILE"
  datasource="mainDataSource" sqlhandler="dbSQLStatementHandler">
  <DeleteKeys>
    <Column>ID_DMS_DOC_FILE</Column>
  </DeleteKeys>
  <UpdateKeys>
    <Column>ID_DMS_DOC_FILE</Column>
  </UpdateKeys>
  <GeneratedKey>ID_DMS_DOC_FILE</GeneratedKey>
  <Queries>
    <Query id="default">
      <AmbiguousColumns>
        <AmbiguousColumn name="ID_DMS_DOC_FILE"
          prefix="tddf" />
        <AmbiguousColumn name="ID_DMS_DOC" prefix="tddf" />
      </AmbiguousColumns>
      <ValidColumns>
        <!-- TDMS_DOC_FILE -->
        <Column>ID_DMS_DOC_FILE</Column>
        <Column>FILE_NAME</Column>
        <Column>ID_DMS_DOC</Column>
        <Column>FILE_TYPE</Column>
        <Column>ID_DMS_DOC_CATEGORY</Column>
        <!-- TDMS_DOC -->
        <Column>ID_DMS_DOC</Column>
        <Column>UPDATE_DATE</Column>
        <Column>UPDATE_BY_ID</Column>
        <Column>DOC_NAME</Column>
        <Column>OWNER_ID</Column>
        <Column>DOC_DESCRIPTION</Column>
        <Column>DOC_KEYWORDS</Column>
        <!-- TDMS_DOC_FILE_VERSION -->
        <Column>ID_DMS_DOC_FILE_VERSION</Column>
        <Column>FILE_PATH</Column>
        <Column>VERSION</Column>
        <Column>FILE_DESCRIPTION</Column>
        <Column>IS_ACTIVE</Column>
        <Column>FILE_ADDED_DATE</Column>
        <Column>FILE_ADDED_USER_ID</Column>
        <Column>ID_DMS_DOC_FILE</Column>
        <Column>THUMBNAIL</Column>
        <Column>FILE_SIZE</Column>
      </ValidColumns>
      <Sentence>
        <![CDATA[
          SELECT
            #COLUMNS#
          FROM
            ${mainschema}.TDMS_DOC_FILE AS tddf
            JOIN ${mainschema}.TDMS_DOC AS tdd ON tddf.ID_DMS_DOC = tdd.ID_DMS_DOC
            LEFT JOIN ${mainschema}.TDMS_DOC_FILE_VERSION AS tddfv ON tddf.id_dms_doc_file = tddfv.id_dms_doc_file
          WHERE (tddfv.IS_ACTIVE = 'Y' OR tddfv.id_dms_doc_file_version IS NULL)
          #WHERE_CONCAT#
          #ORDER#
         ]]>
    </Sentence>
    </Query>
    <Query id="allfiles">
      <AmbiguousColumns>
        <AmbiguousColumn name="ID_DMS_DOC_FILE"
          prefix="tddf" />
      </AmbiguousColumns>
      <ValidColumns>
        <Column>ID_DMS_DOC_FILE</Column>
        <Column>FILE_NAME</Column>
        <Column>ID_DMS_DOC</Column>
        <Column>FILE_TYPE</Column>
        <Column>ID_DMS_DOC_CATEGORY</Column>
      </ValidColumns>
      <Sentence>
        <![CDATA[
          SELECT
            #COLUMNS#
          FROM
            ${mainschema}.TDMS_DOC_FILE AS tddf
          #WHERE#
          #ORDER#
         ]]>
    </Sentence>
    </Query>
  </Queries>
</JdbcEntitySetup>
{% endhighlight %}

{{ "**DMSDocumentFileVersionDao.xml**" | markdownify}}
{% highlight xml linenos %}
<?xml version="1.0" encoding="UTF-8"?>
<JdbcEntitySetup
  xmlns="http://www.ontimize.com/schema/jdbc"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.ontimize.com/schema/jdbc http://www.ontimize.com/schema/jdbc/ontimize-jdbc-dao.xsd"
  catalog="" schema="${mainschema}" table="TDMS_DOC_FILE_VERSION"
  datasource="mainDataSource" sqlhandler="dbSQLStatementHandler">
  <DeleteKeys>
    <Column>ID_DMS_DOC_FILE_VERSION</Column>
  </DeleteKeys>
  <UpdateKeys>
    <Column>ID_DMS_DOC_FILE_VERSION</Column>
  </UpdateKeys>
  <GeneratedKey>ID_DMS_DOC_FILE_VERSION</GeneratedKey>
  <Queries>
    <Query id="default">
      <AmbiguousColumns>
        <AmbiguousColumn name="ID_DMS_DOC_FILE"
          prefix="tddfv" />
      </AmbiguousColumns>
      <ValidColumns>
        <!-- TDMS_DOC_FILE_VERSION -->
        <Column>ID_DMS_DOC_FILE_VERSION</Column>
        <Column>FILE_PATH</Column>
        <Column>VERSION</Column>
        <Column>FILE_DESCRIPTION</Column>
        <Column>IS_ACTIVE</Column>
        <Column>FILE_ADDED_DATE</Column>
        <Column>FILE_ADDED_USER_ID</Column>
        <Column>ID_DMS_DOC_FILE</Column>
        <Column>THUMBNAIL</Column>
        <Column>FILE_SIZE</Column>
        <!-- TDMS_DOC_FILE -->
        <Column>ID_DMS_DOC_FILE</Column>
        <Column>FILE_NAME</Column>
        <Column>ID_DMS_DOC</Column>
        <Column>FILE_TYPE</Column>
        <Column>ID_DMS_DOC_CATEGORY</Column>
      </ValidColumns>
      <Sentence>
        <![CDATA[
           SELECT
             #COLUMNS#
        FROM
          ${mainschema}.TDMS_DOC_FILE_VERSION AS tddfv
          LEFT JOIN ${mainschema}.TDMS_DOC_FILE AS tddf ON tddfv.ID_DMS_DOC_FILE = tddf.ID_DMS_DOC_FILE
        #WHERE#
        #ORDER#
         ]]>
      </Sentence>
    </Query>
  </Queries>
</JdbcEntitySetup>
{% endhighlight %}

{{ "**DMSDocumentPropertyDao.xml**" | markdownify}}
{% highlight xml linenos %}
<?xml version="1.0" encoding="UTF-8"?>
<JdbcEntitySetup
  xmlns="http://www.ontimize.com/schema/jdbc"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.ontimize.com/schema/jdbc http://www.ontimize.com/schema/jdbc/ontimize-jdbc-dao.xsd"
  catalog="" schema="${mainschema}" table="TDMS_DOC_PROPERTY"
  datasource="mainDataSource" sqlhandler="dbSQLStatementHandler">
  <DeleteKeys>
    <Column>ID_DMS_DOC_PROPERTY</Column>
  </DeleteKeys>
  <UpdateKeys>
    <Column>ID_DMS_DOC_PROPERTY</Column>
  </UpdateKeys>
  <GeneratedKey>ID_DMS_DOC_PROPERTY</GeneratedKey>
</JdbcEntitySetup>
{% endhighlight %}

{{ "**DMSRelatedDocumentDao.xml**" | markdownify}}
{% highlight xml linenos %}
<?xml version="1.0" encoding="UTF-8"?>
<JdbcEntitySetup
  xmlns="http://www.ontimize.com/schema/jdbc"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.ontimize.com/schema/jdbc http://www.ontimize.com/schema/jdbc/ontimize-jdbc-dao.xsd"
  catalog="" schema="${mainschema}" table="TDMS_RELATED_DOC"
  datasource="mainDataSource" sqlhandler="dbSQLStatementHandler">
  <DeleteKeys>
    <Column>ID_DMS_RELATED_PROPERTY</Column>
  </DeleteKeys>
  <UpdateKeys>
    <Column>ID_DMS_RELATED_PROPERTY</Column>
  </UpdateKeys>
  <GeneratedKey>ID_DMS_RELATED_PROPERTY</GeneratedKey>
</JdbcEntitySetup>
{% endhighlight %}

</div>
</div>

<div class="multiColumnRow">
<div class="multiColumn jstreeloader">
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
                        <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>CandidateDao.java</li>
                        <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>DMSCategoryDao.java</li>
                        <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>DMSDocumentDao.java</li>
                        <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>DMSDocumentFileDao.java</li>
                        <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>DMSDocumentFileVersionDao.java</li>
                        <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>DMSDocumentPropertyDao.java</li>
                        <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>DMSRelatedDocumentDao.java</li>
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
              <li data-jstree='{"icon":"fas fa-file"}'>DMSCategoryDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentFileDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentFileVersionDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentPropertyDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>DMSRelatedDocumentDao.xml</li>
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
    <li data-jstree='{"icon":"fas fa-file"}'>.gitignore</li>
    <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    <li data-jstree='{"icon":"fas fa-file"}'>README.md</li>
  </ul>
  </li>
</ul>

</div>
<div class="multiColumn multiColumnGrow">

{{ "**CandidateDao.java**" | markdownify}}
{% highlight java%}
...
public class CandidateDao extends OntimizeJdbcDaoSupport {
  ...
  public static final String ATTR_ID_DMS_DOC = "ID_DMS_DOC";
}
{% endhighlight %}

{{ "**DMSCategoryDao.java**" | markdownify}}
{% highlight java linenos %}
package com.ontimize.projectwiki.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;
import com.ontimize.jee.server.services.dms.dao.IDMSCategoryDao;

@Repository("DMSCategoryDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/DMSCategoryDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class DMSCategoryDao extends OntimizeJdbcDaoSupport implements IDMSCategoryDao {

  public static final String ATTR_ID_DMS_DOC_CATEGORY = "ID_DMS_DOC_CATEGORY";
  public static final String ATTR_ID_DMS_DOC = "ID_DMS_DOC";
  public static final String ATTR_ID_DMS_DOC_CATEGORY_PARENT = "ID_DMS_DOC_CATEGORY_PARENT";
  public static final String ATTR_CATEGORY_NAME = "CATEGORY_NAME";

}
{% endhighlight %}

{{ "**DMSDocumentDao.java**" | markdownify}}
{% highlight java linenos %}
package com.ontimize.projectwiki.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;
import com.ontimize.jee.server.services.dms.dao.IDMSDocumentDao;

@Repository("DMSDocumentDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/DMSDocumentDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class DMSDocumentDao extends OntimizeJdbcDaoSupport implements IDMSDocumentDao {

  public static final String ATTR_ID_DMS_DOC = "ID_DMS_DOC";
  public static final String ATTR_UPDATE_DATE = "UPDATE_DATE";
  public static final String ATTR_UPDATE_BY_ID = "UPDATE_BY_ID";
  public static final String ATTR_DOC_NAME = "DOC_NAME";
  public static final String ATTR_OWNER_ID = "OWNER_ID";
  public static final String ATTR_DOC_DESCRIPTION = "DOC_DESCRIPTION";
  public static final String ATTR_DOC_KEYWORDS = "DOC_KEYWORDS";

}
{% endhighlight %}

{{ "**DMSDocumentFileDao.java**" | markdownify}}
{% highlight java linenos %}
package com.ontimize.projectwiki.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;
import com.ontimize.jee.server.services.dms.dao.IDMSDocumentFileDao;

@Repository("DMSDocumentFileDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/DMSDocumentFileDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class DMSDocumentFileDao extends OntimizeJdbcDaoSupport implements IDMSDocumentFileDao{

  public static final String ATTR_ID_DMS_DOC_FILE = "ID_DMS_DOC_FILE";         
  public static final String ATTR_FILE_NAME = "FILE_NAME";         
  public static final String ATTR_ID_DMS_DOC = "ID_DMS_DOC";         
  public static final String ATTR_FILE_TYPE = "FILE_TYPE";         
  public static final String ATTR_ID_DMS_DOC_CATEGORY = "ID_DMS_DOC_CATEGORY";         
    
}
{% endhighlight %}

{{ "**DMSDocumentFileVersionDao.java**" | markdownify}}
{% highlight java linenos %}
package com.ontimize.projectwiki.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;
import com.ontimize.jee.server.services.dms.dao.IDMSDocumentFileVersionDao;

@Repository("DMSDocumentFileVersionDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/DMSDocumentFileVersionDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class DMSDocumentFileVersionDao extends OntimizeJdbcDaoSupport implements IDMSDocumentFileVersionDao {

  public static final String ATT_ID_DMS_DOC_FILE_VERSION = "ID_DMS_DOC_FILE_VERSION";
  public static final String ATT_FILE_PATH = "FILE_PATH";
  public static final String ATT_VERSION = "VERSION";
  public static final String ATT_FILE_DESCRIPTION = "FILE_DESCRIPTION";
  public static final String ATT_IS_ACTIVE = "IS_ACTIVE";
  public static final String ATT_FILE_ADDED_DATE = "FILE_ADDED_DATE";
  public static final String ATT_FILE_ADDED_USER_ID = "FILE_ADDED_USER_ID";
  public static final String ATT_ID_DMS_DOC_FILE = "ID_DMS_DOC_FILE";
  public static final String ATT_THUMBNAIL = "THUMBNAIL";
  public static final String ATT_FILE_SIZE = "FILE_SIZE";

}
{% endhighlight %}

{{ "**DMSDocumentPropertyDao.java**" | markdownify}}
{% highlight java linenos %}
package com.ontimize.projectwiki.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;
import com.ontimize.jee.server.services.dms.dao.IDMSDocumentPropertyDao;

@Repository("DMSDocumentPropertyDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/DMSDocumentPropertyDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class DMSDocumentPropertyDao extends OntimizeJdbcDaoSupport implements IDMSDocumentPropertyDao {

  public static final String ATTR_ID_DMS_DOC_PROPERTY = "ID_DMS_DOC_PROPERTY";
  public static final String ATTR_DOC_PROPERTY_KEY = "DOC_PROPERTY_KEY";
  public static final String ATTR_DOC_PROPERTY_VALUE = "DOC_PROPERTY_VALUE";
  public static final String ATTR_ID_DMS_DOC = "ID_DMS_DOC";

}
{% endhighlight %}

{{ "**DMSRelatedDocumentDao.java**" | markdownify}}
{% highlight java linenos %}
package com.ontimize.projectwiki.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;
import com.ontimize.jee.server.services.dms.dao.IDMSRelatedDocumentDao;

@Repository("DMSRelatedDocumentDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/DMSRelatedDocumentDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class DMSRelatedDocumentDao extends OntimizeJdbcDaoSupport implements IDMSRelatedDocumentDao {

  public static final String ATTR_ID_DMS_RELATED_PROPERTY = "ID_DMS_RELATED_PROPERTY";
  public static final String ATTR_ID_DMS_DOC_MASTER = "ID_DMS_DOC_MASTER";
  public static final String ATTR_ID_DMS_DOC_CHILD = "ID_DMS_DOC_CHILD";

}
{% endhighlight %}
</div>
</div>

### Modify CandidateService insert method
The method of inserting new candidates will be modified so that, when inserting them, they will have a workspace to maintain the files to be uploaded associated with the inserted candidate.

<div class="multiColumnRow">
<div class="multiColumn jstreeloader">
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
                        <li data-jstree='{"icon":"fas fa-file"}'>DMSCategoryDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentFileDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentFileVersionDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentPropertyDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>DMSRelatedDocumentDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>UserRoleDao.java</li>
                      </ul>
                      </li>
                      <li data-jstree='{"icon":"fas fa-folder-open"}'>
                      service
                      <ul>
                        <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>CandidateService.java</li>
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
              <li data-jstree='{"icon":"fas fa-file"}'>DMSCategoryDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentFileDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentFileVersionDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentPropertyDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>DMSRelatedDocumentDao.xml</li>
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
    <li data-jstree='{"icon":"fas fa-file"}'>.gitignore</li>
    <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    <li data-jstree='{"icon":"fas fa-file"}'>README.md</li>
  </ul>
  </li>
</ul>
</div>
<div class="multiColumn multiColumnGrow">

{{"**CandidateService.java**" | markdownify }}
{% highlight java %}
...
import com.ontimize.jee.common.exceptions.DmsException;
import com.ontimize.jee.common.naming.DMSNaming;
import com.ontimize.jee.common.services.dms.DocumentIdentifier;
import com.ontimize.jee.server.services.dms.DMSCreationHelper;
...

@Service("CandidateService")
@Lazy
public class CandidateService implements ICandidateService {

  ...

  @Autowired
  private DMSCreationHelper dmsHelper;

  ...

  @Override
  public EntityResult candidateInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

    try {
    DocumentIdentifier docId = this.dmsHelper.createDocument((String) attrMap.get(CandidateDao.ATTR_DNI));
    attrMap.put(DMSNaming.DOCUMENT_ID_DMS_DOCUMENT, docId.getDocumentId());
    } catch (DmsException e) {
      throw new OntimizeJEERuntimeException("ERROR_CREATING_DMS_DOC", e);
    }

    return this.daoHelper.insert(this.candidateDao, attrMap);
  }

  ...
}
{% endhighlight %}
</div>
</div>

### Add File Manager Rest Controller

<div class="multiColumnRow">
<div class="multiColumn jstreeloader">
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
                        <li data-jstree='{"icon":"fas fa-file"}'>DMSCategoryDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentFileDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentFileVersionDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentPropertyDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>DMSRelatedDocumentDao.java</li>
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
              <li data-jstree='{"icon":"fas fa-file"}'>DMSCategoryDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentFileDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentFileVersionDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentPropertyDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>DMSRelatedDocumentDao.xml</li>
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
                        <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>DMSNameConverter.java</li>
                        <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>FileManagerRestController.java</li>
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
    <li data-jstree='{"icon":"fas fa-file"}'>.gitignore</li>
    <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    <li data-jstree='{"icon":"fas fa-file"}'>README.md</li>
  </ul>
  </li>
</ul>
</div>
<div class="multiColumn multiColumnGrow">

{{"**DMSNameConverter.java**" | markdownify }}
{% highlight java linenos %}
package com.ontimize.projectwiki.ws.core.rest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ontimize.jee.common.naming.DMSNaming;
import com.ontimize.jee.server.dms.model.OFile;
import com.ontimize.jee.server.dms.rest.IDMSNameConverter;

@Service("DMSNameConverter")
public class DMSNameConverter implements IDMSNameConverter {

  @Override
  public Object getFileIdColumn() {
    return DMSNaming.DOCUMENT_FILE_ID_DMS_DOCUMENT_FILE;
  }

  @Override
  public Object getFileNameColumn() {
    return DMSNaming.DOCUMENT_FILE_NAME;
  }

  @Override
  public Object getFileSizeColumn() {
    return DMSNaming.DOCUMENT_FILE_VERSION_FILE_SIZE;
  }

  @Override
  public Object getCategoryIdColumn() {
    return DMSNaming.CATEGORY_ID_CATEGORY;
  }

  @Override
  public Object getCategoryNameColumn() {
    return DMSNaming.CATEGORY_CATEGORY_NAME;
  }

  @Override
  public OFile createOFile(Map<?, ?> params) {
    OFile file = new OFile();
    file.setId((Integer) params.get(DMSNaming.DOCUMENT_FILE_ID_DMS_DOCUMENT_FILE));
    file.setName((String) params.get(DMSNaming.DOCUMENT_FILE_NAME));
    file.setType((String) params.get(DMSNaming.DOCUMENT_FILE_TYPE));
    file.setSize((Integer) params.get(DMSNaming.DOCUMENT_FILE_VERSION_FILE_SIZE));
    file.setCreationDate(((Date) params.get(DMSNaming.DOCUMENT_FILE_VERSION_FILE_ADDED_DATE)).getTime());
    file.setDirectory(false);
    return file;
  }

  @Override
  public List<?> getFileColumns(List<?> columns) {
    return Arrays.asList(DMSNaming.DOCUMENT_FILE_ID_DMS_DOCUMENT_FILE, DMSNaming.DOCUMENT_FILE_NAME,
        DMSNaming.DOCUMENT_FILE_TYPE, DMSNaming.DOCUMENT_FILE_VERSION_FILE_SIZE,
        DMSNaming.DOCUMENT_FILE_VERSION_FILE_ADDED_DATE);
  }

  @Override
  public List<?> getCategoryColumns(List<?> columns) {
    return Arrays.asList(DMSNaming.CATEGORY_ID_CATEGORY, DMSNaming.CATEGORY_CATEGORY_NAME,
        DMSNaming.CATEGORY_ID_CATEGORY_PARENT);
  }

}

{%endhighlight%}

{{"**FileManagerRestController.java**" | markdownify }}
{% highlight java linenos %}
package com.ontimize.projectwiki.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.jee.common.services.dms.IDMSService;
import com.ontimize.jee.server.dms.rest.DMSRestController;
import com.ontimize.jee.server.dms.rest.IDMSNameConverter;

@RestController
@RequestMapping("/filemanager")
@ComponentScan(basePackageClasses = { com.ontimize.jee.common.services.dms.IDMSService.class,
    com.ontimize.jee.server.dms.rest.IDMSNameConverter.class })
public class FileManagerRestController extends DMSRestController<IDMSService, IDMSNameConverter> {

  @Autowired
  private IDMSService dmsService;

  @Override
  public IDMSService getService() {
    return this.dmsService;
  }

}
{%endhighlight%}
</div>
</div>

### Modify application.yml

The **application.yml** file will be modified to indicate the path where the dms files will be stored and the engine it will use. [In this link](https://ontimize.github.io/ontimize-boot/basics/autoconfigurators/#dms) you have information about the configuration of the DMS system in the **application.yml** file.

**Note:** The path specified in the *basePath* variable must exist before the server is started.
{: .notice--info}

<div class="multiColumnRow">
<div class="multiColumn jstreeloader">
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
            <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>application.yml</li>
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
                        <li data-jstree='{"icon":"fas fa-file"}'>DMSCategoryDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentFileDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentFileVersionDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentPropertyDao.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>DMSRelatedDocumentDao.java</li>
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
              <li data-jstree='{"icon":"fas fa-file"}'>DMSCategoryDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentFileDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentFileVersionDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>DMSDocumentPropertyDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>DMSRelatedDocumentDao.xml</li>
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
                        <li data-jstree='{"icon":"fas fa-file"}'>DMSNameConverter.java</li>
                        <li data-jstree='{"icon":"fas fa-file"}'>FileManagerRestController.java</li>
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
    <li data-jstree='{"icon":"fas fa-file"}'>.gitignore</li>
    <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    <li data-jstree='{"icon":"fas fa-file"}'>README.md</li>
  </ul>
  </li>
</ul>
</div>
<div class="multiColumn multiColumnGrow">

{{"**application.yml**" | markdownify}}
{% highlight yaml%}
ontimize:
   dms:
      engine: odms
      basePath: file:/C:/applications/projectwiki/dms
{% endhighlight %}
</div>
</div>

### Add permissions

It is necessary to add the permissions required for the role associated with the user to be able to execute REST requests, which are secured. For the example, we will add all the methods and give access to the *demo* user role.

{%highlight sql linenos%}
INSERT INTO TSERVER_PERMISSION (PERMISSION_NAME) VALUES('com.ontimize.jee.server.services.dms.DMSServiceImpl/fileGetContentOfVersion');
INSERT INTO TSERVER_PERMISSION (PERMISSION_NAME) VALUES('com.ontimize.jee.server.services.dms.DMSServiceImpl/documentGetProperty');
INSERT INTO TSERVER_PERMISSION (PERMISSION_NAME) VALUES('com.ontimize.jee.server.services.dms.DMSServiceImpl/fileRecoverPreviousVersion');
INSERT INTO TSERVER_PERMISSION (PERMISSION_NAME) VALUES('com.ontimize.jee.server.services.dms.DMSServiceImpl/documentDeleteProperties');
INSERT INTO TSERVER_PERMISSION (PERMISSION_NAME) VALUES('com.ontimize.jee.server.services.dms.DMSServiceImpl/documentGetProperties');
INSERT INTO TSERVER_PERMISSION (PERMISSION_NAME) VALUES('com.ontimize.jee.server.services.dms.DMSServiceImpl/documentGetAllFiles');
INSERT INTO TSERVER_PERMISSION (PERMISSION_NAME) VALUES('com.ontimize.jee.server.services.dms.DMSServiceImpl/setRelatedDocuments');
INSERT INTO TSERVER_PERMISSION (PERMISSION_NAME) VALUES('com.ontimize.jee.server.services.dms.DMSServiceImpl/documentAddProperties');
INSERT INTO TSERVER_PERMISSION (PERMISSION_NAME) VALUES('com.ontimize.jee.server.services.dms.DMSServiceImpl/getRelatedDocument');
INSERT INTO TSERVER_PERMISSION (PERMISSION_NAME) VALUES('com.ontimize.jee.server.services.dms.DMSServiceImpl/categoryGetForDocument');
INSERT INTO TSERVER_PERMISSION (PERMISSION_NAME) VALUES('com.ontimize.jee.server.services.dms.DMSServiceImpl/moveFilesToCategory');
INSERT INTO TSERVER_PERMISSION (PERMISSION_NAME) VALUES('com.ontimize.jee.server.services.dms.DMSServiceImpl/fileVersionQuery');
INSERT INTO TSERVER_PERMISSION (PERMISSION_NAME) VALUES('com.ontimize.jee.server.services.dms.DMSServiceImpl/documentQuery');
INSERT INTO TSERVER_PERMISSION (PERMISSION_NAME) VALUES('com.ontimize.jee.server.services.dms.DMSServiceImpl/documentInsert');
INSERT INTO TSERVER_PERMISSION (PERMISSION_NAME) VALUES('com.ontimize.jee.server.services.dms.DMSServiceImpl/documentUpdate');
INSERT INTO TSERVER_PERMISSION (PERMISSION_NAME) VALUES('com.ontimize.jee.server.services.dms.DMSServiceImpl/documentGetFiles');
INSERT INTO TSERVER_PERMISSION (PERMISSION_NAME) VALUES('com.ontimize.jee.server.services.dms.DMSServiceImpl/categoryInsert');
INSERT INTO TSERVER_PERMISSION (PERMISSION_NAME) VALUES('com.ontimize.jee.server.services.dms.DMSServiceImpl/fileInsert');
INSERT INTO TSERVER_PERMISSION (PERMISSION_NAME) VALUES('com.ontimize.jee.server.services.dms.DMSServiceImpl/categoryUpdate');
INSERT INTO TSERVER_PERMISSION (PERMISSION_NAME) VALUES('com.ontimize.jee.server.services.dms.DMSServiceImpl/categoryDelete');
INSERT INTO TSERVER_PERMISSION (PERMISSION_NAME) VALUES('com.ontimize.jee.server.services.dms.DMSServiceImpl/fileDelete');
INSERT INTO TSERVER_PERMISSION (PERMISSION_NAME) VALUES('com.ontimize.jee.server.services.dms.DMSServiceImpl/fileGetVersions');
INSERT INTO TSERVER_PERMISSION (PERMISSION_NAME) VALUES('com.ontimize.jee.server.services.dms.DMSServiceImpl/fileGetContent');
INSERT INTO TSERVER_PERMISSION (PERMISSION_NAME) VALUES('com.ontimize.jee.server.services.dms.DMSServiceImpl/fileUpdate');
INSERT INTO TSERVER_PERMISSION (PERMISSION_NAME) VALUES('com.ontimize.jee.server.services.dms.DMSServiceImpl/documentDelete');
INSERT INTO TSERVER_PERMISSION (PERMISSION_NAME) VALUES('com.ontimize.jee.server.services.dms.DMSServiceImpl/fileQuery');
{%endhighlight%}

Add all permissions to the user role *demo*.

{%highlight sql linenos%}
INSERT
	INTO
	PUBLIC.PUBLIC.TROLE_SERVER_PERMISSION tsp (ID_SERVER_PERMISSION, ID_ROLENAME)
SELECT
	ID_SERVER_PERMISSION,
	(
	SELECT
		ID_ROLENAME
	FROM
		TUSER_ROLE
	WHERE
		USER_ = 'demo') AS ID_ROLENAME
FROM
	TSERVER_PERMISSION tp
LEFT JOIN TROLE_SERVER_PERMISSION tsp ON
	tp.ID_SERVER_PERMISSION = tsp.ID_SERVER_PERMISSION
WHERE
	tsp.ID_SERVER_PERMISSION IS NULL
{%endhighlight%}