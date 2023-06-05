---
title: "SDMS System"
layout: single
permalink: /basics/sdms/
toc: true
toc_label: "Table of Contents"
toc_sticky: true
breadcrumbs: true
sidebar:
  title: "Ontimize Basics"
  nav: sidebar-basics
---

**Important:** This module works only for Ontimize Boot version 3.11.0 or above. Actual release version: [![Ontimize Boot](https://img.shields.io/maven-central/v/com.ontimize.boot/ontimize-boot?label=Ontimize%20boot&style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.ontimize.boot/ontimize-boot)
{: .notice--warning}

# Introduction
The **S**torage **D**ocument **M**anagement **S**ystem (**SDMS**) is a system that allows you to manage documents related to an entity in your project. Ontimize Boot provides a DMS that allows to store files in external services according to the engine set in its configuration.

# Previous concepts
- **Workspace**: Represents the base folder where the documents for an entity will be stored.

# Prerequisites
**Note:** You can follow this tutorial using your own application, although for this example we will use an application created using the archetype that can be found [on this page](https://ontimize.github.io/ontimize-boot/getting_started/) and with a REST service.
{: .notice--info}

There are 2 options to follow this tutorial, clone the repository with the initial state and follow the tutorial step by step, or download the final example and see which files are new and which have been updated.

<div class="multiColumnRow multiColumnRowJustify">
<div class="multiColumn multiColumnGrow" >
  {{ "**Initial project**

    /$ git clone https://github.com/ontimize/ontimize-examples 
    /ontimize-examples$ cd ontimize-examples
    /ontimize-examples$ git checkout boot-sdms-initial" 
    | markdownify }}
</div>
<div class="verticalDivider"></div>
<div class="multiColumn multiColumnGrow" >

  {{ "**Final example**

    /$ git clone https://github.com/ontimize/ontimize-examples 
    /ontimize-examples$ cd ontimize-examples
    /ontimize-examples$ git checkout boot-sdms" 
    | markdownify }}

</div>
</div>

**Note:** To simplify the code being written, three dots (...) may appear in some parts of the code. This indicates that there may be previous code before and after those dots.
{: .notice--info}

# Steps
## Server
### Add SDMS dependencies

<div class="multiColumnRow">
<div class="multiColumn jstreeloader">
<ul class="sticky">
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
      <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>pom.xml</li>
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
    <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>pom.xml</li>
    <li data-jstree='{"icon":"fas fa-file"}'>README.md</li>
  </ul>
  </li>
</ul>
</div>
<div class="multiColumn multiColumnGrow">
  {{ "**/pom.xml**"| markdownify }}

{% highlight xml %}
...
<dependencies>
  ...
  <dependency>
    <groupId>com.ontimize.jee.sdms</groupId>
    <artifactId>ontimize-jee-sdms-common</artifactId>
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
        <groupId>com.ontimize.jee.sdms</groupId>
        <artifactId>ontimize-jee-sdms-rest</artifactId>
    </dependency>
  ...
</dependencies>
...
{% endhighlight %}

  {{ "**model/pom.xml**"| markdownify }}

{% highlight xml %}
...
<dependencies>
...
  <dependency>
    <groupId>com.ontimize.jee.sdms</groupId>
    <artifactId>ontimize-jee-sdms-server</artifactId>
  </dependency>

  <dependency>
    <groupId>com.ontimize.jee.sdms</groupId>
    <artifactId>ontimize-jee-sdms-event</artifactId>
  </dependency>
...
</dependencies>
...
{% endhighlight %}

  {{ "**boot/pom.xml**"| markdownify }}

{% highlight xml %}
...
<dependencies>
...
  <dependency>
    <groupId>com.ontimize.boot</groupId>
    <artifactId>ontimize-boot-starter-sdms</artifactId>
  </dependency>
...
</dependencies>
...
{% endhighlight %}
</div>
</div>

### Modify application.yml

The **application.yml** file will be modified the engine to be used by the SDMS, for this example we will set up the **S3** engine. Information on the configuration of the SDMS system in the **application.yml** file can be found at [this link](https://ontimize.github.io/ontimize-boot/basics/autoconfigurators/#sdms).

**Note:** Currently only the S3 engine using the Amazon AWS S3 service API is available.
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
  sdms:
    engine: s3
    s3:
      access-key: ${S3_ACCESS_KEY}
      secret-key: ${S3_SECRET_KEY}
      bucket: ${S3_BUCKET}
      region: ${S3_REGION}
{% endhighlight %}
</div>
</div>

### Modify the entity service to add the methods of the SDMS service

All methods available by the SDMS service will be added to the service interface of our entity.
Our method names are constructed with the name of the entity followed by the SDMS method name.  
The SDMS methods available are:

- **SdmsFindById**: _It allows us to retrieve the information of an SDMS element by its ID (Base64 encrypted id)._
- **SdmsFind**: _It allows us to retrieve the information of several elements of the SDMS from a filter._
- **SdmsDownloadById**: _It allows us to download an SDMS document by its ID (Base64 encrypted id)_
- **SdmsDownload**: _It allows us to download several SDMS documents from a filter._
- **SdmsUpload**: _It allows us to upload a document to the SDMS._
- **SdmsCreate**: _It allows us to create an SDMS element in the system._
- **SdmsUpdate**: _It allows us to update an SDMS element in the system._
- **SdmsCopy**: _It allows us to copy an SDMS element in the system to another space in the SDMS._
- **SdmsMove**: _It allows us to move an SDMS element in the system to another space in the SDMS._
- **SdmsDeleteById**: _It allows us to delete an SDMS element in the system by its ID (Base64 encrypted id)._
- **SdmsDelete**: _It allows us to delete several SDMS elements in the system from a filter._

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
                        <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>ICandidateService.java</li>
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

{{"**ICandidateService.java**" | markdownify }}
{% highlight java %}
...
import com.ontimize.jee.sdms.common.dto.OSdmsRestDataDto;
import org.springframework.web.multipart.MultipartFile;
import java.io.Serializable;
...

public interface ICandidateService {

...

EntityResult candidateSdmsFindById( Serializable id, OSdmsRestDataDto data );
EntityResult candidateSdmsFind( OSdmsRestDataDto data );
EntityResult candidateSdmsDownloadById( Serializable id, OSdmsRestDataDto data );
EntityResult candidateSdmsDownload( OSdmsRestDataDto data );
EntityResult candidateSdmsUpload(OSdmsRestDataDto data, MultipartFile file );
EntityResult candidateSdmsCreate( OSdmsRestDataDto data );
EntityResult candidateSdmsUpdate( OSdmsRestDataDto data );
EntityResult candidateSdmsCopy( OSdmsRestDataDto data );
EntityResult candidateSdmsMove( OSdmsRestDataDto data );
EntityResult candidateSdmsDeleteById( Serializable id, OSdmsRestDataDto data );
EntityResult candidateSdmsDelete( OSdmsRestDataDto data );

...
}
{% endhighlight %}
</div>
</div>

Now in the implementation of the service we **implement the methods** with the help of the `IOSdmsService` component of the SDMS system and by calling the corresponding method.  
We will also **establish the workspace** where the entity will store and manage the files. We will do this via the `OSdmsWorkspace` annotation which admits 2 parameters:
- `name`: Sets the name of the workspace. If this parameter is not set, the value will be `default`.
- `value`: Sets the path to the workspace, and can be set to variable between braces.

The annotation can be set at **class level** by enabling the workspace for all SDMS methods, and/or at **method level** by setting its scope to the annotated method. It can also be set as many workspaces as required.

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

import com.ontimize.jee.sdms.common.dto.OSdmsRestDataDto;
import com.ontimize.jee.sdms.common.workspace.annotation.OSdmsWorkspace;
import com.ontimize.jee.sdms.server.service.IOSdmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

...

@OSdmsWorkspace( "candidate/doc/{id}" )
@OSdmsWorkspace( name = "all", value = "candidate/doc" )
...
public class CandidateService implements ICandidateService {

  ...

  @Autowired
  private IOSdmsService oSdmsService;

  ...

  @Override
  public EntityResult candidateSdmsFind( final OSdmsRestDataDto data ) {
  	return this.oSdmsService.find( data );
  }

@OSdmsWorkspace( name = "images", value = "candidate/img/{id}" )
  @Override
  public EntityResult candidateSdmsDownloadById( final Serializable id, final OSdmsRestDataDto data ) {
  	return this.oSdmsService.downloadById( id, data );
  }

  @Override
  public EntityResult candidateSdmsDownload( final OSdmsRestDataDto data ) {
  	return this.oSdmsService.download( data );
  }

  @Override
  public EntityResult candidateSdmsUpload(final OSdmsRestDataDto data, final MultipartFile file ) {
  	return this.oSdmsService.upload( data, file );
  }

  @Override
  public EntityResult candidateSdmsCreate( final OSdmsRestDataDto data ) {
  	return this.oSdmsService.create( data );
  }

  @Override
  public EntityResult candidateSdmsUpdate( final OSdmsRestDataDto data ) {
  	return this.oSdmsService.update( data );
  }

  @Override
  public EntityResult candidateSdmsCopy( final OSdmsRestDataDto data ) {
  	return this.oSdmsService.copy( data );
  }

  @Override
  public EntityResult candidateSdmsMove( final OSdmsRestDataDto data ) {
  	return this.oSdmsService.move( data );
  }

  @Override
  public EntityResult candidateSdmsDeleteById( final Serializable id, final OSdmsRestDataDto data ) {
  	return this.oSdmsService.deleteById( id, data );
  }

  @Override
  public EntityResult candidateSdmsDelete( final OSdmsRestDataDto data ) {
  	return this.oSdmsService.delete( data );
  }

  ...

}
{% endhighlight %}
</div>
</div>

### Modify the entity Rest controller

We modify the Rest controller of our entity so that instead of inheriting from the `ORestController` class, it inherits from the `OSdmsRestController` class. This class adds all the endpoints of the `ORestController` class and the SDMS endpoints, linking them with the corresponding SDMS methods that we have established in the service of our entity.

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
                        <li data-jstree='{"selected":true,"icon":"fas fa-file"}'>CandidateRestController.java</li>
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

{{"**ICandidateService.java**" | markdownify }}
{% highlight java %}
...

import com.imatia.platform.hr.api.core.service.ICandidateService;
import com.ontimize.jee.sdms.rest.controller.OSdmsRestController;

...

public class CandidateRestController extends OSdmsRestController<ICandidateService> {

...
}
{% endhighlight %}
</div>
</div>

# Endpoints

The endpoints set by the `OSdmsRestController` are the following:  

**Note:** If the workspace is not sent in an http request, the SDMS will set the `default` workspace as the active workspace. But if the default workspace has variables, you will need to pass it the workspace with the variable values to access the workspace.
{: .notice--info}

{{"**Find by ID**" | markdownify }}

This endpoint maps the request to the `SdmsFindById` method of the SDMS service.

The id of the requested document is passed in Base64 encrypted in the url and the data parameter is added with the workspace information in JSON format.

**Note:** The data value must be encoded with [percent-encoding](https://en.wikipedia.org/wiki/URL_encoding) to be read correctly.
{: .notice--info}

{% highlight properties %}
GET: /candidates/candidate/sdms/find/id/Y2FuZGlkYXRlLzEvZmlsZS0wOC50eHQ=
Content-Type: multipart/form-data
Content-Disposition: form-data; name="data"; {"filter": { "workspace": "default", "data": {"id": 1}}}
{% endhighlight %}

{{"**Find**" | markdownify }}

This endpoint maps the request to the `SdmsFind` method of the SDMS service.

The id of the requested document is passed in Base64 encrypted in the url and the data parameter is added with the workspace information in JSON format.

{% highlight properties %}
POST: /candidates/candidate/sdms/find/id
Content-Type: application/json
Body: {"filter": { "workspace": "default", "data": {"id": 1}, "id": ["Y2FuZGlkYXRlLzEvZmlsZS0wMS50eHQ=", "Y2FuZGlkYXRlLzEvZmlsZS0wMi50eHQ="]}}
{% endhighlight %}

{{"**Download by ID**" | markdownify }}

This endpoint maps the request to the `SdmsDownloadById` method of the SDMS service.  

The id of the requested document is passed in Base64 encrypted in the url and the data parameter is added with the workspace information in JSON format.

**Note:** The data value must be encoded with [percent-encoding](https://en.wikipedia.org/wiki/URL_encoding) to be read correctly.
{: .notice--info}

{% highlight properties %}
GET: /candidates/candidate/sdms/download/id/Y2FuZGlkYXRlLzEvZmlsZS0wOC50eHQ=
Content-Type: multipart/form-data
Content-Disposition: form-data; name="data"; {"filter": { "workspace": "default", "data": {"id": 1}}}
{% endhighlight %}

{{"**Download**" | markdownify }}

This endpoint maps the request to the `SdmsDownload` method of the SDMS service.  

The id of the requested document is passed in Base64 encrypted in the url and the data parameter is added with the workspace information in JSON format.

{% highlight properties %}
POST: /candidates/candidate/sdms/download/id
Content-Type: application/json
Body: {"filter": { "workspace": "default", "data": {"id": 1}, "id": ["Y2FuZGlkYXRlLzEvZmlsZS0wMS50eHQ=", "Y2FuZGlkYXRlLzEvZmlsZS0wMi50eHQ="]}}
{% endhighlight %}

{{"**Upload**" | markdownify }}

This endpoint maps the request to the `SdmsUpload` method of the SDMS service.  

The data parameter is added with the workspace information in JSON format.

**Note:** The data value must be encoded with [percent-encoding](https://en.wikipedia.org/wiki/URL_encoding) to be read correctly.
{: .notice--info}

{% highlight properties %}
POST: /candidates/candidate/sdms/upload
Content-Type: multipart/form-data
Content-Disposition: form-data; name="file"; filename="/C:/cv.pdf"
Content-Disposition: form-data; name="data"; {"filter": { "workspace": "default", "data": {"id": 1}}, "data": {"id": "Y2FuZGlkYXRlLzEvZmlsZS0wMS50eHQ"}}
{% endhighlight %}

{{"**Create**" | markdownify }}

This endpoint maps the request to the `SdmsCreate` method of the SDMS service.

{% highlight properties %}
POST: /candidates/candidate/sdms/create
Content-Type: application/json
Body: {"filter": { "workspace": "default", "data": {"id": 1}}, "data": {"id": "Y2FuZGlkYXRlLzEvZmlsZS0wMi50eHQ="}}
{% endhighlight %}

{{"**Update**" | markdownify }}

This endpoint maps the request to the `SdmsUpdate` method of the SDMS service.  

{% highlight properties %}
PUT: /candidates/candidate/sdms/update
Content-Type: application/json
Body: {"filter": { "workspace": "default", "data": {"id": 1}, "id": "Y2FuZGlkYXRlLzEvZmlsZS0wMS50eHQ="}, "data": {"id": "Y2FuZGlkYXRlLzEvZmlsZS0wMi50eHQ="}}
{% endhighlight %}

{{"**Copy**" | markdownify }}

This endpoint maps the request to the `SdmsCopy` method of the SDMS service.  

{% highlight properties %}
PUT: /candidates/candidate/sdms/copy
Content-Type: application/json
Body: {"filter": { "workspace": "default", "data": {"id": 1}, "id": "Y2FuZGlkYXRlLzEvZmlsZS0wMS50eHQ="}, "data": {"id": "Y2FuZGlkYXRlLzEvZmlsZS0wMi50eHQ="}}
{% endhighlight %}

{{"**Move**" | markdownify }}

This endpoint maps the request to the `SdmsMove` method of the SDMS service.  

{% highlight properties %}
PUT: /candidates/candidate/sdms/move
Content-Type: application/json
Body: {"filter": { "workspace": "default", "data": {"id": 1}, "id": "Y2FuZGlkYXRlLzEvZmlsZS0wMS50eHQ="}, "data": {"id": "Y2FuZGlkYXRlLzEvZmlsZS0wMi50eHQ="}}
{% endhighlight %}

{{"**Delete by ID**" | markdownify }}

This endpoint maps the request to the `SdmsDeleteById` method of the SDMS service.  

The id of the requested document is passed in Base64 encrypted in the url and the data parameter is added with the workspace information in JSON format.

**Note:** The data value must be encoded with [percent-encoding](https://en.wikipedia.org/wiki/URL_encoding) to be read correctly.
{: .notice--info}

{% highlight properties %}
DELETE: /candidates/candidate/sdms/delete/id/Y2FuZGlkYXRlLzEvZmlsZS0wOC50eHQ=
Content-Type: multipart/form-data
Content-Disposition: form-data; name="data"; {"filter": { "workspace": "default", "data": {"id": 1}}}
{% endhighlight %}

{{"**Delete**" | markdownify }}

This endpoint maps the request to the `SdmsDelete` method of the SDMS service.  

The id of the requested document is passed in Base64 encrypted in the url and the data parameter is added with the workspace information in JSON format.

{% highlight properties %}
DELETE: /candidates/candidate/sdms/delete/id
Content-Type: application/json
Body: {"filter": { "workspace": "default", "data": {"id": 1}, "id": ["Y2FuZGlkYXRlLzEvZmlsZS0wMS50eHQ=", "Y2FuZGlkYXRlLzEvZmlsZS0wMi50eHQ="]}}
{% endhighlight %}

## Request Parameters

The data parameter of the SDMS system always goes to the "filter" and "data" sections.

The "filter" section contains the parameters related to the selection of elements of the SDMS. Here you will always find the information related to the workspace you want to use.

In the "data" section are the parameters related to the information to be sent to the SDMS.

The rest of the parameters that can be sent in each of the sections will depend on the engine used.

### Possible parameters in the S3 engine

- **Filter**
  - `workspace`: _The name of the workspace to use._  
    _example:_ `"workspace": "default"`
  
  - `data`: _The variables values of the workspace._  
    _example:_ `"data": "{"id": "ID-1"}"` | `"data": "{"id": 1}"` | `"data": "{"id": [1, 2, 3]}"`
  
  - `id`: _The S3 key of the document encoded in Base64 (several can be in an array)._  
    _example:_ `"id": "Y2FuZGlkYXRlLzEvZmlsZS0wMS50eHQ="` | `"id": ["Y2FuZGlkYXRlLzEvZmlsZS0wMS50eHQ=", "Y2FuZGlkYXRlLzEvZmlsZS0wMi50eHQ="]`
  
  - `key`: _The S3 key of the document (several can be in an array)._  
    _example:_ `"key": "candidate/1/file-01.txt"` | `"key": ["candidate/1/file-01.txt", "candidate/1/file-02.txt"]`
  
  - `prefix`: _The S3 prefix of the document (several can be in an array)._  
    _example:_ `"prefix": "candidate/1/folder-1"` | `"prefix": ["candidate/1/folder-1", "candidate/1/folder-2"]`
  
  - `fileName`: _The S3 name of the document (several can be in an array)._  
    _example:_ `"fileName": "file-01.txt"` | `"fileName": ["file-01.txt", "file-02.txt"]`
  
  - `maxKeys`: _The maximum number of S3 documents to return._  
    _example:_ `"maxKeys": 10`
  
  - `delimiter`: _The delimiter to use in the S3 documents search._  
    _example:_ `"delimiter": "/"`
  
  - `marker`: _The marker to use in the S3 documents search._  
    _example:_ `"marker": "candidate/1/file-01.txt"`

- **Data**
  - `id`: _The S3 key of the document encoded in Base64._  
    _example:_ `"id": "Y2FuZGlkYXRlLzEvZmlsZS0wMS50eHQ="`
  
  - `key`: _The S3 key of the document._  
    _example:_ `"key": "candidate/1/file-01.txt"`
  
  - `prefix`: _The S3 prefix of the document._  
    _example:_ `"prefix": "/folder-1"`
  
  - `fileName`: _The S3 name of the document._  
    _example:_ `"fileName": "file-01.txt"`
  
  - `currentPrefix`: _The current S3 prefix of the document._  
    _example:_ `"currentPrefix": "candidate/1/folder-1"`
