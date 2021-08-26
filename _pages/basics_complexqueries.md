---
title: "Using complex queries"
layout: single
permalink: /basics/complexqueries/
toc: true
toc_label: "Table of Contents"
toc_sticky: true
breadcrumbs: true
---

## Introduction

In this example the DAO *.xml files will be modified to make complex queries against other database tables.

## Database structure

We will update the OfferCandidatesDao DAO to add new information in the *.xml. In this case, you will try to add a new query that reflects the other tables that are used from this DAO to relate the offers, candidates, and status. As this table only contains identifiers, our new query will allow us to know all the data and not only its identifiers. 

![database](/../assets/images/databasecmplx.png)

## Modify the DAO to add a complex query

In the *.xml, we will add a new `<Queries>` tag in which we will add the new queries using the `<Query>` tag. In this query, we can indicate the query to be executed through the `<Sentence>` tag.
As we want to use the columns and conditions that you indicate through the request, we will use the following markers:

### Query markers

| Marker | Meaning |
|--|--|
| #COLUMNS# | Columns to be queried |
| #WHERE# | Columns to filter the query |
| #WHERE_CONCAT# | Adds more columns to filter the query with the **AND** sql operator |
| #ORDER# | Column to order the queried values with the **ORDER BY** sql operator |
| #ORDER_CONCAT# | Adds more columns to order the queried values with a **comma (,)** |
| #SCHEMA# | Gets the database schema |

**Example**

```xml
<Queries>
    <Query>
        <Sentence>
            <![CDATA[
                SELECT
                #COLUMNS#
                FROM
                #SCHEMA#.OFFER_CANDIDATES
                #WHERE#
            ]]>
        </Sentence>
    </Query>
</Queries>
```

This will be the query to execute:

    SELECT ID, OFFER_ID, CANDIDATE_ID, OFFER_CANDIDATE_STATUS FROM PUBLIC.OFFER_CANDIDATES WHERE ID = 200


There are two ways to order the queries values, with the #ORDER# marker or with a `<OrderColumn>` tag

**Example with #ORDER#**
```xml
<Queries>
    <Query>
        <Sentence>
            <![CDATA[
                SELECT
                #COLUMNS#
                FROM
                OFFER_CANDIDATES
                #ORDER#
            ]]>
        </Sentence>
    </Query>
</Queries>
```

This will be the query to execute:

    SELECT ID, OFFER_ID, CANDIDATE_ID, OFFER_CANDIDATE_STATUS FROM PUBLIC.OFFER_CANDIDATES ORDER BY ID

**Example with `<OrderColumn>`**

```xml
<Queries>
    <Query>
        <OrderColumns>
            <OrderColumn name="CANDIDATE_ID" type="ASC"/>
        </OrderColumns>
        <Sentence>
            <![CDATA[
                SELECT
                #COLUMNS#
                FROM
                OFFER_CANDIDATES
            ]]>
        </Sentence>
    </Query>
</Queries>
```
This will be the query to execute:

    SELECT ID, OFFER_ID, CANDIDATE_ID, OFFER_CANDIDATE_STATUS FROM PUBLIC.OFFER_CANDIDATES ORDER BY CANDIDATE_ID

There are two types of OrderColumn, **ASC** (Ascendent) or **DESC** (Descendent).


### Ambiguous columns

The use of the `<AmbiguousColumn>` tag will indicate which are the ambiguous columns (in this case, the DESCRIPTION column of OFFER_CANDIDATE_STATUS and OFFER, using the name of DESC_STATUS for OFFER_CANDIDATE_STATUS). Otherwise in case we want to modify the query that is performed by default, the identifier of the tag would be default `<Query id = "default">`. As we want to keep the query by default, we will put another identifier.

**Example**

```xml
 <Queries>
  <Query id="details">
   <AmbiguousColumns>
    <AmbiguousColumn name="DESC_STATUS" prefix="OS"
     databaseName="DESCRIPTION" />
    <AmbiguousColumn name="OFF_STATUS" prefix="OCS"
     databaseName="DESCRIPTION" />
   </AmbiguousColumns>
   <Sentence>
   <![CDATA[
     SELECT
      #COLUMNS#
     FROM
      PUBLIC.OFFER_CANDIDATES OC
     INNER JOIN PUBLIC.OFFER O ON
      OC.OFFER_ID = O.ID
     INNER JOIN PUBLIC.CANDIDATE C ON
      OC.CANDIDATE_ID = C.ID
     INNER JOIN PUBLIC.OFFER_STATUS OS ON
      O.OFFER_STATUS = OS.ID
     INNER JOIN PUBLIC.OFFER_CANDIDATE_STATUS OCS ON
      O.OFFER_STATUS = OCS.ID
     #WHERE#
   ]]>
   </Sentence>
  </Query>
 </Queries>
 ```

### Complete example

<div class="multiColumnRow">
<div class="multiColumn jstreeloader">
<ul>
  <li data-jstree='{"opened":true, "icon":"fas fa-folder-open"}'>
  ontimize-boot-tutorial
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
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
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
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
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
    frontend
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          ngx
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            aot-config
            <ul>
              <li data-jstree='{"icon":"fas fa-file"}'>helpers.js</li>
              <li data-jstree='{"icon":"fas fa-file"}'>index.ejs</li>
              <li data-jstree='{"icon":"fas fa-file"}'>vendor-aot.ts</li>
              <li data-jstree='{"icon":"fas fa-file"}'>webpack-aot.config.js</li>
            </ul>
            </li>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            src
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              app
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                login
                <ul>
                  <li data-jstree='{"icon":"fas fa-file"}'>login-routing.module.ts</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>login.component.html</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>login.component.scss</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>login.component.ts</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>login.module.ts</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>login.theme.scss</li>
                </ul>
                </li>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                main
                <ul>
                  <li data-jstree='{"icon":"fas fa-folder-open"}'>
                  home
                  <ul>
                    <li data-jstree='{"icon":"fas fa-file"}'>home-routing.module.ts</li>
                    <li data-jstree='{"icon":"fas fa-file"}'>home.component.html</li>
                    <li data-jstree='{"icon":"fas fa-file"}'>home.component.scss</li>
                    <li data-jstree='{"icon":"fas fa-file"}'>home.component.ts</li>
                    <li data-jstree='{"icon":"fas fa-file"}'>home.module.ts</li>
                  </ul>
                  </li>
                  <li data-jstree='{"icon":"fas fa-file"}'>main-routing.module.ts</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>main.component.html</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>main.component.scss</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>main.component.ts</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>main.module.ts</li>
                </ul>
                </li>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                shared
                <ul>
                  <li data-jstree='{"icon":"fas fa-file"}'>app.menu.config.ts</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>app.services.config.ts</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>shared.module.ts</li>
                </ul>
                </li>
                <li data-jstree='{"icon":"fas fa-file"}'>app-routing.module.ts</li>
                <li data-jstree='{"icon":"fas fa-file"}'>app.component.html</li>
                <li data-jstree='{"icon":"fas fa-file"}'>app.component.scss</li>
                <li data-jstree='{"icon":"fas fa-file"}'>app.component.ts</li>
                <li data-jstree='{"icon":"fas fa-file"}'>app.config.ts</li>
                <li data-jstree='{"icon":"fas fa-file"}'>app.module.ts</li>
              </ul>
              </li>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              assets
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                css
                <ul>
                  <li data-jstree='{"icon":"fas fa-file"}'>app.scss</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>loader.css</li>
                </ul>
                </li>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                i18n
                <ul>
                  <li data-jstree='{"icon":"fas fa-file"}'>en.json</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>es.json</li>
                </ul>
                </li>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                images
                <ul>
                  <li data-jstree='{"icon":"fas fa-file"}'>no-image.png</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>ontimize.png</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>ontimize_web_log.png</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>sidenav-closed.png</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>sidenav-opened.png</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>user_profile.png</li>
                </ul>
                </li>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                js
                <ul>
                  <li data-jstree='{"icon":"fas fa-file"}'>domchange.js</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>keyboard.js</li>
                </ul>
                </li>
              </ul>
              </li>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              environments
              <ul>
                <li data-jstree='{"icon":"fas fa-file"}'>environment.prod.ts</li>
                <li data-jstree='{"icon":"fas fa-file"}'>environment.ts</li>
              </ul>
              </li>
              <li data-jstree='{"icon":"fas fa-file"}'>favicon.ico</li>
              <li data-jstree='{"icon":"fas fa-file"}'>index.html</li>
              <li data-jstree='{"icon":"fas fa-file"}'>main.ts</li>
              <li data-jstree='{"icon":"fas fa-file"}'>polyfills.ts</li>
              <li data-jstree='{"icon":"fas fa-file"}'>styles.scss</li>
              <li data-jstree='{"icon":"fas fa-file"}'>test.ts</li>
              <li data-jstree='{"icon":"fas fa-file"}'>tsconfig.app.json</li>
              <li data-jstree='{"icon":"fas fa-file"}'>tsconfig.spec.json</li>
            </ul>
            </li>
            <li data-jstree='{"icon":"fas fa-file"}'>angular.json</li>
            <li data-jstree='{"icon":"fas fa-file"}'>package-lock.json</li>
            <li data-jstree='{"icon":"fas fa-file"}'>package.json</li>
            <li data-jstree='{"icon":"fas fa-file"}'>README.md</li>
            <li data-jstree='{"icon":"fas fa-file"}'>tsconfig.aot.json</li>
            <li data-jstree='{"icon":"fas fa-file"}'>tsconfig.json</li>
            <li data-jstree='{"icon":"fas fa-file"}'>tslint.json</li>
          </ul>
          </li>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          resources
          <ul>
            <li data-jstree='{"icon":"fas fa-file"}'>application.properties</li>
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
                      <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>OfferCandidatesDao.java</li>
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
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          resources
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            dao
            <ul>
              <li data-jstree='{"icon":"fas fa-file"}'>CandidateDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>EducationDao.xml</li>
              <li data-jstree='{"icon":"fas fa-file"}'>ExperienceLevelDao.xml</li>
              <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>OfferCandidatesDao.xml</li>
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
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
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
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    <li data-jstree='{"icon":"fas fa-file"}'>README.md</li>
  </ul>
  </li>
</ul>
</div>
<div class="multiColumn multiColumnGrow">
  {{ "**OfferCandidatesDao.xml**"| markdownify }}

{% highlight xml %}
<?xml version="1.0" encoding="UTF-8"?>
<JdbcEntitySetup
 xmlns="http://www.ontimize.com/schema/jdbc"
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xsi:schemaLocation="http://www.ontimize.com/schema/jdbc http://www.ontimize.com/schema/jdbc/ontimize-jdbc-dao.xsd"
 catalog="" schema="${mainschema}" table="OFFER_CANDIDATES"
 datasource="mainDataSource" sqlhandler="dbSQLStatementHandler">
 <DeleteKeys>
  <Column>ID</Column>
 </DeleteKeys>
 <UpdateKeys>
  <Column>ID</Column>
 </UpdateKeys>
 <GeneratedKey>ID</GeneratedKey>

 <Queries>
  <Query id="details">
   <AmbiguousColumns>
    <AmbiguousColumn name="DESC_STATUS" prefix="OS"
     databaseName="DESCRIPTION" />
    <AmbiguousColumn name="OFF_STATUS" prefix="OCS"
     databaseName="DESCRIPTION" />
   </AmbiguousColumns>
   <Sentence>
   <![CDATA[
     SELECT
      #COLUMNS#
     FROM
      PUBLIC.OFFER_CANDIDATES OC
     INNER JOIN PUBLIC.OFFER O ON
      OC.OFFER_ID = O.ID
     INNER JOIN PUBLIC.CANDIDATE C ON
      OC.CANDIDATE_ID = C.ID
     INNER JOIN PUBLIC.OFFER_STATUS OS ON
      O.OFFER_STATUS = OS.ID
     INNER JOIN PUBLIC.OFFER_CANDIDATE_STATUS OCS ON
      O.OFFER_STATUS = OCS.ID
     #WHERE#
   ]]>
   </Sentence>
  </Query>
 </Queries>
</JdbcEntitySetup>
{% endhighlight %}
<br>
In the java file, we will add a new constant, which will have the same value as the name of the identifier that we have established for the query. 
<br><br>

{{"**OfferCandidatesDao.java**" | markdownify}}

{% highlight java %}
package com.ontimize.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("OfferCandidatesDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/OfferCandidatesDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class OfferCandidatesDao extends OntimizeJdbcDaoSupport {

 public static final String ATTR_ID ="ID";
 public static final String ATTR_OFFER_ID ="OFFER_ID";
 public static final String ATTR_CANDIDATE_ID ="CANDIDATE_ID";
 public static final String ATTR_OFFER_CANDIDATE_STATUS ="OFFER_CANDIDATE_STATUS";
 public static final String QUERY_OFFER_DETAILS = "details";
  
}
{% endhighlight %}
</div>
</div>


## Modify the interface and the service to add the new method

We will update the IOfferService interface to add the new method that will perform the query. In case of being the default query, it would not be necessary to do any of the steps indicated below.

<div class="multiColumnRow">
<div class="multiColumn jstreeloader">
<ul>
  <li data-jstree='{"opened":true, "icon":"fas fa-folder-open"}'>
  ontimize-boot-tutorial
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
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
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
                      <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>IOfferService.java</li>
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
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
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
    frontend
    <ul>
      <li data-jstree='{"icon":"fas fa-folder-open"}'>
      src
      <ul>
        <li data-jstree='{"icon":"fas fa-folder-open"}'>
        main
        <ul>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          ngx
          <ul>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            aot-config
            <ul>
              <li data-jstree='{"icon":"fas fa-file"}'>helpers.js</li>
              <li data-jstree='{"icon":"fas fa-file"}'>index.ejs</li>
              <li data-jstree='{"icon":"fas fa-file"}'>vendor-aot.ts</li>
              <li data-jstree='{"icon":"fas fa-file"}'>webpack-aot.config.js</li>
            </ul>
            </li>
            <li data-jstree='{"icon":"fas fa-folder-open"}'>
            src
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              app
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                login
                <ul>
                  <li data-jstree='{"icon":"fas fa-file"}'>login-routing.module.ts</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>login.component.html</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>login.component.scss</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>login.component.ts</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>login.module.ts</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>login.theme.scss</li>
                </ul>
                </li>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                main
                <ul>
                  <li data-jstree='{"icon":"fas fa-folder-open"}'>
                  home
                  <ul>
                    <li data-jstree='{"icon":"fas fa-file"}'>home-routing.module.ts</li>
                    <li data-jstree='{"icon":"fas fa-file"}'>home.component.html</li>
                    <li data-jstree='{"icon":"fas fa-file"}'>home.component.scss</li>
                    <li data-jstree='{"icon":"fas fa-file"}'>home.component.ts</li>
                    <li data-jstree='{"icon":"fas fa-file"}'>home.module.ts</li>
                  </ul>
                  </li>
                  <li data-jstree='{"icon":"fas fa-file"}'>main-routing.module.ts</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>main.component.html</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>main.component.scss</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>main.component.ts</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>main.module.ts</li>
                </ul>
                </li>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                shared
                <ul>
                  <li data-jstree='{"icon":"fas fa-file"}'>app.menu.config.ts</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>app.services.config.ts</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>shared.module.ts</li>
                </ul>
                </li>
                <li data-jstree='{"icon":"fas fa-file"}'>app-routing.module.ts</li>
                <li data-jstree='{"icon":"fas fa-file"}'>app.component.html</li>
                <li data-jstree='{"icon":"fas fa-file"}'>app.component.scss</li>
                <li data-jstree='{"icon":"fas fa-file"}'>app.component.ts</li>
                <li data-jstree='{"icon":"fas fa-file"}'>app.config.ts</li>
                <li data-jstree='{"icon":"fas fa-file"}'>app.module.ts</li>
              </ul>
              </li>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              assets
              <ul>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                css
                <ul>
                  <li data-jstree='{"icon":"fas fa-file"}'>app.scss</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>loader.css</li>
                </ul>
                </li>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                i18n
                <ul>
                  <li data-jstree='{"icon":"fas fa-file"}'>en.json</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>es.json</li>
                </ul>
                </li>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                images
                <ul>
                  <li data-jstree='{"icon":"fas fa-file"}'>no-image.png</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>ontimize.png</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>ontimize_web_log.png</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>sidenav-closed.png</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>sidenav-opened.png</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>user_profile.png</li>
                </ul>
                </li>
                <li data-jstree='{"icon":"fas fa-folder-open"}'>
                js
                <ul>
                  <li data-jstree='{"icon":"fas fa-file"}'>domchange.js</li>
                  <li data-jstree='{"icon":"fas fa-file"}'>keyboard.js</li>
                </ul>
                </li>
              </ul>
              </li>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              environments
              <ul>
                <li data-jstree='{"icon":"fas fa-file"}'>environment.prod.ts</li>
                <li data-jstree='{"icon":"fas fa-file"}'>environment.ts</li>
              </ul>
              </li>
              <li data-jstree='{"icon":"fas fa-file"}'>favicon.ico</li>
              <li data-jstree='{"icon":"fas fa-file"}'>index.html</li>
              <li data-jstree='{"icon":"fas fa-file"}'>main.ts</li>
              <li data-jstree='{"icon":"fas fa-file"}'>polyfills.ts</li>
              <li data-jstree='{"icon":"fas fa-file"}'>styles.scss</li>
              <li data-jstree='{"icon":"fas fa-file"}'>test.ts</li>
              <li data-jstree='{"icon":"fas fa-file"}'>tsconfig.app.json</li>
              <li data-jstree='{"icon":"fas fa-file"}'>tsconfig.spec.json</li>
            </ul>
            </li>
            <li data-jstree='{"icon":"fas fa-file"}'>angular.json</li>
            <li data-jstree='{"icon":"fas fa-file"}'>package-lock.json</li>
            <li data-jstree='{"icon":"fas fa-file"}'>package.json</li>
            <li data-jstree='{"icon":"fas fa-file"}'>README.md</li>
            <li data-jstree='{"icon":"fas fa-file"}'>tsconfig.aot.json</li>
            <li data-jstree='{"icon":"fas fa-file"}'>tsconfig.json</li>
            <li data-jstree='{"icon":"fas fa-file"}'>tslint.json</li>
          </ul>
          </li>
          <li data-jstree='{"icon":"fas fa-folder-open"}'>
          resources
          <ul>
            <li data-jstree='{"icon":"fas fa-file"}'>application.properties</li>
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
                      <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>OfferService.java</li>
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
            com
            <ul>
              <li data-jstree='{"icon":"fas fa-folder-open"}'>
              ontimize
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
      <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    </ul>
    </li>
    <li data-jstree='{"icon":"fas fa-file"}'>pom.xml</li>
    <li data-jstree='{"icon":"fas fa-file"}'>README.md</li>
  </ul>
  </li>
</ul>
</div>
<div class="multiColumn multiColumnGrow">
  {{ "**IOfferService.java**"| markdownify }}

{% highlight java %}
public interface IOfferService {

        ...
 // OFFER CANDIDATES
 ...
 public EntityResult offerCandidateDetailsQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;

        ...
 // OFFER CANDIDATES STATUS
        ...
}
{% endhighlight %}
<br>
In the service, we implement the new interface method, using the daoHelper to create the query, but adding a new parameter after the list of columns to query, which will be the identifier of the query that we have created in the DAO (and that we have associated with a constant in the corresponding java file). 
<br><br>

{{"**OfferService.java**" | markdownify}}

{% highlight java %}
@Service("OfferService")
@Lazy
public class OfferService implements IOfferService {

 ...

 @Override
 public EntityResult offerCandidateDetailsQuery(Map<String, Object> keyMap, List<String> attrList)
   throws OntimizeJEERuntimeException {
  return this.daoHelper.query(this.offerCandidatesDao, keyMap, attrList, OfferCandidatesDao.QUERY_OFFER_DETAILS);
 }

 ...
}
{% endhighlight %}
</div>
</div>