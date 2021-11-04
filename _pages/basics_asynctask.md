---
title: "Async Tasks"
layout: single
permalink: /basics/asynctask/
toc: true
toc_label: "Table of Contents"
toc_sticky: true
breadcrumbs: true
---

# Introduction
The **Async Task** system will allow you to run decoupled, asynchronous tasks. This module will let you run any service method in a separate, newly created thread, by simply adding an annotation to its controller method.

# Previous concepts
- **Task**: It is the generic representation of a decoupled task. It stores information such as its UUID, its current status and the result of the execution.
- **Aspect**: It is a modularization of a concern that cuts across multiple classes. It allows us to intercept the execution of any given method or class and implement some alternative or extra behaviour for it.

# Prerequisites
You can follow this tutorial using your own application, although for this example we will use an application created using the archetype that can be found [on this page](https://ontimize.github.io/ontimize-boot/v3/getting_started/) and with a REST service. 

There are 2 options to follow this tutorial, clone the repository with the initial state and follow the tutorial step by step, or download the final example and see which files are new and which have been updated.

<div class="multiColumnRow multiColumnRowJustify">
<div class="multiColumn multiColumnGrow" >
  {{ "**Initial project**

    /$ git clone https://github.com/ontimize/ontimize-examples 
    /ontimize-examples$ cd ontimize-examples
    /ontimize-examples$ git checkout boot-async-task-initial" 
    | markdownify }}
</div>
<div class="verticalDivider"></div>
<div class="multiColumn multiColumnGrow" >

  {{ "**Final example**

    /$ git clone https://github.com/ontimize/ontimize-examples 
    /ontimize-examples$ cd ontimize-examples
    /ontimize-examples$ git checkout boot-async-task" 
    | markdownify }}

</div>
</div>

**Note:** To simplify the code being written, three dots (...) may appear in some parts of the code. This indicates that there may be previous code before and after those dots.
{: .notice--info}

# Steps
## Database
### Tasks Table

With the database started, we create the new table that will store the tasks information.

{% highlight sql linenos %}
CREATE TABLE TASKS(ID INTEGER IDENTITY NOT NULL PRIMARY KEY, UUID VARCHAR(255) NOT NULL, STATUS VARCHAR(255), RESULT VARBINARY(16777216));
{% endhighlight %}

## Server
### Add Ontimize AsyncTask dependencies

**Note:** The decoupled tasks system is integrated in the **Ontimize Core** module, so we need to declare it as a project dependency.
{: .notice--info}

<div class="multiColumnRow">
<div class="multiColumn jstreeloader">

</div>
<div class="multiColumn multiColumnGrow">
  {{ "**boot/pom.xml**"| markdownify }}

{% highlight xml %}
...
<dependencies>
  ...
  <dependency>
    <groupId>com.ontimize.boot</groupId>
	<artifactId>ontimize-boot-starter-core</artifactId>
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
    <groupId>com.ontimize.boot</groupId>
	<artifactId>ontimize-boot-core</artifactId>
  </dependency>
  ...
</dependencies>
...
{% endhighlight %}

</div>

</div>

### Add Task DAO
A specific DAO will be created for the tasks table, and it will implement the DAO interface in the tasks module.

<div class="multiColumnRow">
<div class="multiColumn jstreeloader">

</div>
<div class="multiColumn multiColumnGrow">

{{ "**TaskDao.xml**" | markdownify}}
{% highlight xml linenos %}
<?xml version="1.0" encoding="UTF-8"?>
<JdbcEntitySetup xmlns="http://www.ontimize.com/schema/jdbc"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.ontimize.com/schema/jdbc http://www.ontimize.com/schema/jdbc/ontimize-jdbc-dao.xsd"
	table="TASKS" datasource="mainDataSource" sqlhandler="dbSQLStatementHandler">
	<DeleteKeys>
		<Column>ID</Column>
	</DeleteKeys>
	<UpdateKeys>
		<Column>ID</Column>
	</UpdateKeys>
	<GeneratedKey>ID</GeneratedKey>
</JdbcEntitySetup>
{% endhighlight %}

</div>
</div>

<div class="multiColumnRow">
<div class="multiColumn jstreeloader">

</div>
<div class="multiColumn multiColumnGrow">

{{ "**TaskDao.java**" | markdownify}}
{% highlight java linenos %}
package com.imatia.qsallcomponents.model.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.boot.core.asynctask.IAsyncTaskDao;
import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Lazy
@Repository(value = "TaskDao")
@ConfigurationFile(configurationFile = "base-dao/TaskDao.xml", configurationFilePlaceholder = "base-dao/placeholders.properties")
public class TaskDao extends OntimizeJdbcDaoSupport implements IAsyncTaskDao {
	
	public static final String	ATTR_ID				= "ID";
	public static final String	ATTR_UUID			= "UUID";
	public static final String	ATTR_STATUS			= "STATUS";
	public static final String	ATTR_RESULT			= "RESULT";
	
	public TaskDao() {
		super();
	}

}

{% endhighlight %}

</div>
</div>

### Annotate controller method

In order to run some service method asynchronously, we need to annotate its respective REST controller method with **@OAsyncTask**. This way, a new thread will be created in order to handle the method's execution, and we will recieve an instant response with the URL where we can check the execution status and retrieve its result when it's finished.

**Important:** The service's method **MUST** return a *serializable* object with getters and setters, as well as the controller's method must return a *ResponseEntity* object.
{: .notice--warning}

<div class="multiColumnRow">
<div class="multiColumn jstreeloader">

</div>
<div class="multiColumn multiColumnGrow">

{{ "**Customer.java**" | markdownify}}
{% highlight java linenos %}
package com.imatia.qsallcomponents.api.constants.entities;

import java.io.Serializable;

public class Customer implements Serializable {
	
    private static final long serialVersionUID = 1L;

    private String	id;
    private String	name;
    private String	email;
    ...
	
    public Customer() {}

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    ...

}

{% endhighlight %}

</div>
</div>

<div class="multiColumnRow">
<div class="multiColumn jstreeloader">

</div>
<div class="multiColumn multiColumnGrow">

{{ "**CustomerService.java**" | markdownify}}
{% highlight java linenos %}
package com.imatia.qsallcomponents.model.service;

import org.springframework.stereotype.Service;

import com.imatia.qsallcomponents.api.constants.entities.Customer;
import com.imatia.qsallcomponents.api.services.ICustomerService;

import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

...

@Service("CustomerService")
public class CustomerService implements ICustomerService {

    ...

    @Override
    public Customer customerQuery() throws OntimizeJEERuntimeException {
		
        ...
        
        return <Customer>;
    }

    ...

}

{% endhighlight %}

</div>
</div>

<div class="multiColumnRow">
<div class="multiColumn jstreeloader">

</div>
<div class="multiColumn multiColumnGrow">

{{ "**CustomerRestController.java**" | markdownify}}
{% highlight java linenos %}
package com.imatia.qsallcomponents.ws.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imatia.qsallcomponents.api.constants.entities.Customer;
import com.imatia.qsallcomponents.api.services.ICustomerService;
import com.imatia.qsallcomponents.openapi.ICustomersApi;
import com.ontimize.boot.core.asynctask.OAsyncTask;
import com.ontimize.jee.server.rest.ORestController;

...

@RestController
@RequestMapping("/customers")
public class CustomerRestController extends ORestController<ICustomerService> implements ICustomersApi {

    @Autowired
    private ICustomerService customerService;

    @Override
    public ICustomerService getService() {
        return this.customerService;
    }

    ...
	
    @OAsyncTask
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<Customer> getCustomer() {
        return ResponseEntity.ok().body(this.customerService.customerQuery());
        
    }
    
    ...

}

{% endhighlight %}

</div>
</div>

### Modify application.yml

The **application.yml** file will be modified to enable the decoupled tasks module, indicate the storage engine it will use, the URL base path for the service, and its thread pool configuration. [In this link](/ontimize-boot/v3/basics/autoconfigurators/#asynctask) you have information about the configuration of the asynchronous tasks system in the **application.yml** file.

**Note:** The *enable* property must be set to ***true*** and the storage engine type must be specified in the *engine* property before the server is started.
{: .notice--info}

**Important:** The asynchronous tasks system requires the **Ontimize** ***TaskExecutor*** to be configured, see [this link](/ontimize-boot/v3/basics/autoconfigurators/#taskexecutor).
{: .notice--warning}

<div class="multiColumnRow">
<div class="multiColumn jstreeloader">

</div>
<div class="multiColumn multiColumnGrow">

{{"**application.yml**" | markdownify}}
{{"For *database* storage" | markdownify}}
{% highlight yaml%}
ontimize:
  asynctask:
    enable: true
    engine: database
    url: /tasks
{% endhighlight %}

</div>
</div>