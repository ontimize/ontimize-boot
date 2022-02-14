---
title: "Security System"
layout: single
permalink: /basics/security/
toc: true
toc_label: "Table of Contents"
toc_sticky: true
breadcrumbs: true
sidebar:
  title: "Ontimize Basics"
  nav: sidebar-basics
---

# Introduction
Ontimize security allows you to set permissions to each role that you assign to a specific user, so that different users can access (or not) the project services and methods.

# Prerequisites
You can follow this tutorial using your own application, although for this example we will use an application created using the archetype that can be found [on this page](https://ontimize.github.io/ontimize-boot/getting_started/) and with a REST service. 

There are 2 options to follow this tutorial, clone the repository with the initial state and follow the tutorial step by step, or download the final example and see which files are new and which have been updated.

<div class="multiColumnRow multiColumnRowJustify">
<div class="multiColumn multiColumnGrow" >
  {{ "**Initial project**

    /$ git clone https://github.com/ontimize/ontimize-examples 
    /ontimize-examples$ cd ontimize-examples
    /ontimize-examples$ git checkout boot-security-initial" 
    | markdownify }}
</div>
<div class="verticalDivider"></div>
<div class="multiColumn multiColumnGrow" >

  {{ "**Final example**

    /$ git clone https://github.com/ontimize/ontimize-examples 
    /ontimize-examples$ cd ontimize-examples
    /ontimize-examples$ git checkout boot-security" 
    | markdownify }}

</div>
</div>

**Note:** To simplify the code being written, three dots (...) may appear in some parts of the code. This indicates that there may be previous code before and after those dots.
{: .notice--info}

# Steps

## Add new profile and user

We will add the following elements to the database:

- New user
{% highlight sql linenos %}
INSERT INTO TUSER VALUES('candidate','candidate','candidate','candidate',NULL,'99999999R',NULL,'2020-07-03 11:50:40.665000',NULL)
{% endhighlight %}

- New user role
{% highlight sql linenos %}
INSERT INTO TROLE VALUES(1,'candidate','<?xml version="1.0" encoding="UTF-8"?><security></security>')
{% endhighlight %}

- Relationship between the new user and the new role
{% highlight sql linenos %}
INSERT INTO TUSER_ROLE VALUES(1,1,'candidate')
{% endhighlight %}

## Modify the service to add security

In the *projectwiki-boot* module we will add the annotation `@EnableAspectJAutoProxy(proxyTargetClass = false)` to the **ServerApplication.java** class and to each service method the `@Secured({ PermissionsProviderSecured.SECURED })` annotation.


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
                  <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>ServerApplication.java</li>
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
                        <li data-jstree='{"selected": true, "icon":"fas fa-file"}'>CandidateService.java</li>
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
<div class="multiColumn multiColumnGrow">
  {{ "**ServerApplication.java**"| markdownify }}

{% highlight java %}
package com.ontimize.projectwiki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = false)
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
{% endhighlight %}


  {{ "**CustomerService.java**"| markdownify }}

{% highlight java %}
package com.ontimize.projectwiki.model.core.service;

. . .
import org.springframework.security.access.annotation.Secured;


. . . 
import com.ontimize.jee.common.security.PermissionsProviderSecured;

. . .

public class CandidateService implements ICandidateService {

. . .

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult candidateQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.candidateDao, keyMap, attrList);
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	@Transactional(rollbackFor = Exception.class)
	public EntityResult candidateInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.insert(this.candidateDao, attrMap);
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	@Transactional(rollbackFor = Exception.class)
	public EntityResult candidateUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.update(this.candidateDao, attrMap, keyMap);
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult candidateDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.candidateDao, keyMap);
	}
	
}
{% endhighlight %}

</div>
</div>

## Add permissions for methods

Once the methods have been noted, it is necessary to add the service and the method that we have noted to the table of the DB that stores those elements and to the table that indicates the methods for which the role has permission.

{% highlight sql lineos%}
INSERT INTO TSERVER_PERMISSION VALUES('com.ontimize.projectwiki.model.core.service.ICandidateService/candidateQuery')
INSERT INTO TSERVER_PERMISSION VALUES('com.ontimize.projectwiki.model.core.service.ICandidateService/candidateInsert')
INSERT INTO TSERVER_PERMISSION VALUES('com.ontimize.projectwiki.model.core.service.ICandidateService/candidateUpdate')
INSERT INTO TSERVER_PERMISSION VALUES('com.ontimize.projectwiki.model.core.service.ICandidateService/candidateDelete')

INSERT INTO TROLE_SERVER_PERMISSION VALUES(0,1)
INSERT INTO TROLE_SERVER_PERMISSION VALUES(0,2)
INSERT INTO TROLE_SERVER_PERMISSION VALUES(0,3)
INSERT INTO TROLE_SERVER_PERMISSION VALUES(0,4)
{% endhighlight %}


If we try to make requests through [Postman](https://www.postman.com/) to the service of CandidateService, changing the authentication by the user candidate and password candidate, it will give an error, since the user candidate does not belong to the role admin which is the only role that has permissions to make these queries.