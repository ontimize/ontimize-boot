---
title: "Export data to xlsx"
layout: single
permalink: /basics/export/exportdata/export-data-to-xlsx
toc: true
toc_label: "Table of Contents"
toc_sticky: true
breadcrumbs: true
sidebar:
  title: "Ontimize Basics"
  nav: sidebar-basics
---

**Important:** This module works only for Ontimize Boot version 3.9.0-SNAPSHOT or above.
{: .notice--warning}

# Introduction
Ontimize provides a system to export the DAO data of a service and dump it directly to an *.xlsx file. This system uses a JSON template where all the necessary parameters are indicated to use in the body of the request to obtain the file.

# Prerequisites
**Note:** You can follow this tutorial using your own application, although for this example we will use an application created using the archetype that can be found [on this page](https://ontimize.github.io/ontimize-boot/getting_started/) and with a REST service.
{: .notice--info}

There are 2 options to follow this tutorial, clone the repository with the initial state and follow the tutorial step by step, or download the final example and see which files are new and which have been updated.

<div class="multiColumnRow multiColumnRowJustify">
  <div class="multiColumn multiColumnGrow" >
  {{ "**Initial project**

    /$ git clone https://github.com/ontimize/ontimize-examples
    /ontimize-examples$ cd ontimize-examples
    /ontimize-examples$ git checkout boot-export-initial"
    | markdownify }}

</div>
<div class="verticalDivider"></div>
<div class="multiColumn multiColumnGrow">

  {{ "**Final example**

    /$ git clone https://github.com/ontimize/ontimize-examples
    /ontimize-examples$ cd ontimize-examples
    /ontimize-examples$ git checkout boot-export"
    | markdownify }}


  </div>
</div>

# Steps

**Note:** To simplify the code being written, three dots (...) may appear in some parts of the code. This indicates that there may be previous code before and after those dots.
{: .notice--info}

## Add dependencies

Two dependencies need to be added, one to the ws module containing the controllers (so that it can respond to the export request) and one to the boot module to be able to load the autoconfigurator in the application.yml file for export.

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
      <li data-jstree='{"icon":"fas fa-file"}'>.gitignore</li>
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
      <li data-jstree='{"icon":"fas fa-file"}'>.gitignore</li>
      <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>pom.xml</li>
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
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.lck</li>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.properties</li>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.script</li>
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
      <li data-jstree='{"icon":"fas fa-file"}'>.gitignore</li>
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
      <li data-jstree='{"icon":"fas fa-file"}'>.gitignore</li>
      <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-file"}'>.gitignore</li>
    <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    <li data-jstree='{"icon":"fas fa-file"}'>README.md</li>
  </ul>
  </li>
</ul>
  </div>
  <div class="multiColumn multiColumnGrow" >

{{ "**projectwiki-boot/pom.xml**" | markdownify }}
{% highlight xml %}
    <dependencies>
        ...
        <dependency>
            <groupId>com.ontimize.boot</groupId>
            <artifactId>ontimize-boot-starter-webaddons</artifactId>
        </dependency>
        ...
    </dependencies>
{% endhighlight %}

{{ "**projectwiki-ws/pom.xml**" | markdownify }}
{% highlight xml %}
    <dependencies>
        ...
        <dependency>
            <groupId>com.ontimize.jee</groupId>
            <artifactId>ontimize-jee-webclient-addons</artifactId>
        </dependency>
        ...
    </dependencies>
{% endhighlight %}

  </div>
</div>

## Add export url to application.yml
In the application.yml file, a configuration will be added to allow indicating the export URL and the extension used.

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
      <li data-jstree='{"icon":"fas fa-file"}'>.gitignore</li>
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
      <li data-jstree='{"icon":"fas fa-file"}'>.gitignore</li>
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
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.lck</li>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.properties</li>
            <li data-jstree='{"icon":"fas fa-file"}'>templateDB.script</li>
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
      <li data-jstree='{"icon":"fas fa-file"}'>.gitignore</li>
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
      <li data-jstree='{"icon":"fas fa-file"}'>.gitignore</li>
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
  <div class="multiColumn multiColumnGrow" >

{{ "**ontimize:export:**

| Attribute | Values | Meaning |
|--|--|--|
| url | *String* | Specifies the path to use the export system. |
| enable | *Boolean* | Indicates when export is enabled.  |

**Example**
```yaml
...
ontimize:
...
   export:
      url: /export
      enable: true
...
```
"| markdownify }}
  </div>
</div>

# Check the export system

An application such as **Postman** will be used to execute the REST export request for our project. A **POST** request will be made to the previously configured url using *\*.json* as the body of the request containing all the necessary information for the export.

- **URL**: http://localhost:33333/export/xlsx
- **HTTP Method**: POST
- **Authorization**: *User:* demo, *Password*: demouser
- **Body**: JSON
{% highlight json %}
{
    "queryParam": {
        "columns": [
            "SURNAME",
            "PHONE",
            "ID",
            "EMAIL",
            "WAGE_LEVEL",
            "COMMENT",
            "BIRTHDAY",
            "DNI",
            "SPECIALTIES",
            "NAME"
        ],
        "sqltypes": {
            "SURNAME": 12,
            "PHONE": 12,
            "ID": 4,
            "EMAIL": 12,
            "WAGE_LEVEL": 2,
            "COMMENT": 12,
            "BIRTHDAY": 91,
            "DNI": 12,
            "SPECIALTIES": 12,
            "NAME": 12
        }
        "offset": -1,
        "pageSize": 25
    },
    "service": "CandidateService",
    "dao": "candidate",
    "path": "/candidates",
    "advQuery": false,
    "columns": {
        "ID": {},
        "DNI": {},
        "NAME": {},
        "SURNAME": {},
        "EMAIL": {},
        "PHONE": {},
        "BIRTHDAY": {},
        "SPECIALTIES": {},
        "WAGE_LEVEL": {},
        "COMMENT": {}
    },
    "columnTitles": {
        "SURNAME": "Surname",
        "PHONE": "Phone",
        "ID": "Id.",
        "EMAIL": "Email",
        "WAGE_LEVEL": "Wage level",
        "COMMENT": "Comment",
        "BIRTHDAY": "Birthday",
        "DNI": "DNI",
        "SPECIALTIES": "Speciality",
        "NAME": "Name"
    },
    "columnTypes": {
        "SURNAME": "java.lang.String",
        "PHONE": "java.lang.String",
        "ID": "java.lang.Integer",
        "EMAIL": "java.lang.String",
        "WAGE_LEVEL": "java.lang.Integer",
        "COMMENT": "java.lang.String",
        "BIRTHDAY": "java.sql.Date",
        "DNI": "java.lang.String",
        "SPECIALTIES": "java.lang.String",
        "NAME": "java.lang.String"
    },
    "styles": {
        "greenBG": {
            "fillBackgroundColor": "GREEN"
        },
        "redBG": {
            "fillBackgroundColor": "RED"
        },
        "blueBG": {
            "fillBackgroundColor": "BLUE"
        }
    },
    "columnStyles": {
        "NAME": "greenBG"
    },
    "rowStyles": {
        "1": "blueBG"
    },
    "cellStyles": {
        "7,7": "greenBG",
        "2,2,5,4": "redBG"
    }
}
{% endhighlight %}

After click in *Send* button, click in *Save Response ^* and save it to a file. Then, open the *\*.xlsx* file in your editor.

**Postman**
![postman](/../../assets/images/export_postman_query.png)

**Excel**
![export](/../../assets/images/excelexport.png)

# Creating the JSON for exporting data

This is the list of values accepted by the JSON request to generate the export file.

| Attribute | Values | Meaning |
|--|--|--|
| queryParam | *JSON Object* | A JSON object defining the *columns* and *sqltypes* elements. {::nomarkdown}<table style="font-size: 1em"><thead><tr><td>Attribute</td><td>Values</td><td>Meaning</td></tr></thead><tbody><tr><td>columns</td><td>JSON Array</td><td>An array that indicates which columns to query in the database.</td></tr><tr><td>sqltypes</td><td>JSON Object</td><td>An object containing the key-value pairs for the data type contained in the database. As a key, the column name and as a value, the integer corresponding to the database data type, which can be found at this {:/}[link](https://docs.oracle.com/en/java/javase/11/docs/api/java.sql/java/sql/Types.html).{::nomarkdown}</td></tr><tr><td>offset</td><td> *Integer* </td><td>Integer to especify a page to query. -1 if don't want it</td><tr><td>pageSize</td><td> *Integer* </td><td>The size of the page for advanced query. Useless with *advQuery: false*</td></tbody></table>{:/} |
| service | *String* |This is the bean name of the service you want to query. (The name that appears inside the @Service() annotation, e.g.: *@Service("SERVICE_NAME")* = **SERVICE_NAME**)|
| dao | *String* |Name of the query method of the service to be queried **without** the suffix *Query* or *PaginationQuery*, e.g.: *customerQuery* = **customer**|
| path | *String* |Name of the path to be queried|
| advQuery | *Boolean* | Determines whether the DAO query method is *Query* or *PaginationQuery*. |
| columns | *JSON Object* |Determines the order of the columns in the export|
| columnTitles | *JSON Object* |Translates the name of the column to be exported, replacing it with the value of the key.|
| columnTypes | *JSON Object* |Key-value pairs that indicate how the data will be treated within the cell, e.g.: String, Date, Integer, etc. These data types are those corresponding to the database data type, which can be found at this [link](https://docs.oracle.com/en/java/javase/11/docs/api/java.sql/java/sql/Types.html)|
| styles | *JSON Object* |JSON objects used to indicate the styles that the cell will have, grouped under the same name. Only the following styles are supported: {::nomarkdown}<table style="font-size: 1em"><thead><tr><td>Attribute</td><td>Values</td><td>Meaning</td></tr></thead><tbody><tr><td>dataFormatString</td><td>*String*</td><td>Format string for some formatter, like decimals, e.g.: #,##0.00</td></tr><tr><td>alignment</td><td>*String*</td><td>These are the possible values for the horizontal alignment: GENERAL, LEFT, CENTER, RIGHT, FILL, JUSTIFY, CENTER_SELECTION, DISTRIBUTED</td></tr><tr><td>verticalAlignment</td><td>*String*</td><td>These are the possible values for the horizontal alignment: TOP, CENTER, BOTTOM, JUSTIFY, DISTRIBUTED</td></tr><tr><td>fillBackgroundColor</td><td>*String*</td><td>These are the possible values for the background colors: BLACK1, WHITE1, RED1, BRIGHT_GREEN1, BLUE1, YELLOW1, PINK1, TURQUOISE1, BLACK, WHITE, RED, BRIGHT_GREEN, BLUE, YELLOW, PINK, TURQUOISE, DARK_RED, GREEN, DARK_BLUE, DARK_YELLOW, VIOLET, TEAL, GREY_25_PERCENT, GREY_50_PERCENT, CORNFLOWER_BLUE, MAROON, LEMON_CHIFFON, LIGHT_TURQUOISE1, ORCHID, CORAL, ROYAL_BLUE, LIGHT_CORNFLOWER_BLUE, SKY_BLUE, LIGHT_TURQUOISE, LIGHT_GREEN, LIGHT_YELLOW, PALE_BLUE, ROSE, LAVENDER, TAN, LIGHT_BLUE, AQUA, LIME, GOLD, LIGHT_ORANGE, ORANGE, BLUE_GREY, GREY_40_PERCENT, DARK_TEAL, SEA_GREEN, DARK_GREEN, OLIVE_GREEN, BROWN, PLUM</td></tr></tbody></table>{:/} |
| columnStyles | *JSON Object* | Key-value pairs, where the key is the column name and the value is the name of the style defined in the *styles* section. This value has priority 2 (the lower value, the higher priority). |
| rowStyles | *JSON Object* | Key-value pairs, where the key is the row index and the value is the name of the style defined in the *styles* section. This value has priority 3 (the lower value, the higher priority).|
| cellStyles | *JSON Object* |Key-value pairs, where the key is the cell to be selected (row, column) or the range of cells (start row, start column, end row, end column) and the value is the name of the style defined in the *styles* section. This value has priority 1 (the lower the value, the higher the priority).|
