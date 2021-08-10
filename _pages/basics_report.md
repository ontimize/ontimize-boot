---
title: "Report Store"
layout: single
permalink: /basics/reports/
toc: true
toc_label: "Table of Contents"
toc_sticky: true
breadcrumbs: true
---

# Introduction
The **Report Store** system will allow you to store, manage and export all kinds of reports designed and implemented via the JasperReports API. This module will let you use your Ontimize application data as data sources for your reports, allowing you to fully customize its layout with tables, charts, graphs... and also visualize, export, print and download your reports.

# Previous concepts
- **Report**: It is the generic representation of a report. One report can have one or multiple custom report parameters, or have none.
- Main report file: The file containing all the information regarding the report layout, along with the data sources, connections and other configuration directives. Written in ***.jrxml*** format.
- **Report parameter**: It is the generic representation of a custom report parameter. They are used for defining filters, implementing pagination, or even specifying the report data source.

# Prerequisites
You can follow this tutorial using your own application, although for this example we will use an application created using the archetype that can be found [on this page](https://ontimize.github.io/ontimize-boot/v3/getting_started/) and with a REST service. 

There are 2 options to follow this tutorial, clone the repository with the initial state and follow the tutorial step by step, or download the final example and see which files are new and which have been updated.

<div class="multiColumnRow multiColumnRowJustify">
<div class="multiColumn multiColumnGrow" >
  {{ "**Initial project**

    /$ git clone https://github.com/ontimize/ontimize-examples 
    /ontimize-examples$ cd ontimize-examples
    /ontimize-examples$ git checkout boot-report-initial" 
    | markdownify }}
</div>
<div class="verticalDivider"></div>
<div class="multiColumn multiColumnGrow" >

  {{ "**Final example**

    /$ git clone https://github.com/ontimize/ontimize-examples 
    /ontimize-examples$ cd ontimize-examples
    /ontimize-examples$ git checkout boot-report" 
    | markdownify }}

</div>
</div>

**Note:** To simplify the code being written, three dots (...) may appear in some parts of the code. This indicates that there may be previous code before and after those dots.
{: .notice--info}

**Important:** In the first step we will learn how to configure the reports system with the ***database*** engine. If you want to use the *file system* engine you can jump to [this](#server) section.
{: .notice--warning}

# Steps
## Database
### Report Tables

With the database started, we create the new tables that will store the reports information. We're going to need to create two different tables, one for the report itself and one for the report custom parameters.

{% highlight sql linenos %}
CREATE TABLE REPORTS(ID INTEGER IDENTITY NOT NULL PRIMARY KEY, UUID VARCHAR(255) NOT NULL, NAME VARCHAR(255), DESCRIPTION VARCHAR(255), REPORT_TYPE VARCHAR(255), MAIN_REPORT_FILENAME VARCHAR(255) NOT NULL, ZIP VARBINARY(16777216), COMPILED VARBINARY(16777216));
CREATE TABLE REPORT_PARAMETERS(ID INTEGER IDENTITY NOT NULL PRIMARY KEY, REPORT_ID INTEGER NOT NULL, NAME VARCHAR(255), DESCRIPTION VARCHAR(255), NESTED_TYPE VARCHAR(255), VALUE_CLASS VARCHAR(255));
{% endhighlight %}

Once the tables have been created, we add the foreign key

{% highlight sql linenos %}
ALTER TABLE REPORT_PARAMETERS ADD CONSTRAINT REPORT_PARAMETERS_FK FOREIGN KEY(REPORT_ID) REFERENCES REPORTS(ID);
{% endhighlight %}

## Server
### Add Ontimize Report dependencies

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
	<artifactId>ontimize-boot-starter-report</artifactId>
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
    <groupId>com.ontimize.jee.report</groupId>
	<artifactId>ontimize-jee-report-server</artifactId>
  </dependency>
  ...
</dependencies>
...
{% endhighlight %}

</div>

</div>

### Add Report DAO
A specific DAO will be created for each of both tables in the reports system, and each of them will implement a different interface.

<div class="multiColumnRow">
<div class="multiColumn jstreeloader">

</div>
<div class="multiColumn multiColumnGrow">

{{ "**ReportDao.xml**" | markdownify}}
{% highlight xml linenos %}
<?xml version="1.0" encoding="UTF-8"?>
<JdbcEntitySetup xmlns="http://www.ontimize.com/schema/jdbc"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.ontimize.com/schema/jdbc http://www.ontimize.com/schema/jdbc/ontimize-jdbc-dao.xsd"
	table="REPORTS" datasource="mainDataSource" sqlhandler="dbSQLStatementHandler">
	<DeleteKeys>
		<Column>ID</Column>
	</DeleteKeys>
	<UpdateKeys>
		<Column>ID</Column>
	</UpdateKeys>
	<GeneratedKey>ID</GeneratedKey>
</JdbcEntitySetup>
{% endhighlight %}

{{ "**ReportParameterDao.xml**" | markdownify}}
{% highlight xml linenos %}
<?xml version="1.0" encoding="UTF-8"?>
<JdbcEntitySetup xmlns="http://www.ontimize.com/schema/jdbc"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.ontimize.com/schema/jdbc http://www.ontimize.com/schema/jdbc/ontimize-jdbc-dao.xsd"
	table="REPORT_PARAMETERS" datasource="mainDataSource" sqlhandler="dbSQLStatementHandler">
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

{{ "**ReportDao.java**" | markdownify}}
{% highlight java linenos %}
package com.imatia.qsallcomponents.model.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;
import com.ontimize.jee.server.services.reportstore.dao.IReportDao;

@Lazy
@Repository(value = "ReportDao")
@ConfigurationFile(configurationFile = "base-dao/ReportDao.xml", configurationFilePlaceholder = "base-dao/placeholders.properties")
public class ReportDao extends OntimizeJdbcDaoSupport implements IReportDao {

	public static final String	ATTR_ID					= "ID";
	public static final String	ATTR_NAME				= "NAME";
	public static final String	ATTR_DESCRIPTION			= "DESCRIPTION";
	public static final String	ATTR_REPORT_TYPE			= "REPORT_TYPE";
	public static final String	ATTR_MAIN_REPORT_FILENAME	        = "MAIN_REPORT_FILENAME";
	public static final String	ATTR_ZIP				= "ZIP";
	public static final String	ATTR_COMPILED				= "COMPILED";
	
	public ReportDao() {
		super();
	}
}

{% endhighlight %}

{{ "**ReportParameterDao.java**" | markdownify}}
{% highlight java linenos %}
package com.imatia.qsallcomponents.model.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;
import com.ontimize.jee.server.services.reportstore.dao.IReportParameterDao;

@Lazy
@Repository(value = "ReportParameterDao")
@ConfigurationFile(configurationFile = "base-dao/ReportParameterDao.xml", configurationFilePlaceholder = "base-dao/placeholders.properties")
public class ReportParameterDao extends OntimizeJdbcDaoSupport implements IReportParameterDao {
	
	public static final String	ATTR_ID					= "ID";
	public static final String	ATTR_REPORT_ID				= "REPORT_ID";
	public static final String	ATTR_NAME				= "NAME";
	public static final String	ATTR_DESCRIPTION			= "DESCRIPTION";
	public static final String	ATTR_NESTED_TYPE			= "NESTED_TYPE";
	public static final String	ATTR_VALUE_CLASS			= "VALUE_CLASS";

	public ReportParameterDao() {
		super();
	}
}

{% endhighlight %}

</div>
</div>

### Modify application.yml

The **application.yml** file will be modified to indicate the report engine type it will use and, if needed, the path where the report files will be stored. [In this link](/ontimize-boot/v3/basics/autoconfigurators/#report) you have information about the configuration of the reports system in the **application.yml** file.

**Note:** The engine type specified in the *engine* variable must exist before the server is started.
{: .notice--info}

**Important:** You can only choose **ONE** of the two options listed below.
{: .notice--warning}

<div class="multiColumnRow">
<div class="multiColumn jstreeloader">

</div>
<div class="multiColumn multiColumnGrow">

{{"**application.yml**" | markdownify}}
{{"For *database* engine" | markdownify}}
{% highlight yaml%}
ontimize:
   report:
      engine: database
{% endhighlight %}


{{"For *file system* engine" | markdownify}}
{% highlight yaml%}
ontimize:
   report:
      engine: file
      base-path: file:/C:/applications/projectwiki/reports
{% endhighlight %}
</div>
</div>