---
title: "Reports"
layout: single
permalink: /basics/reports/
toc: true
toc_label: "Table of Contents"
toc_sticky: true
breadcrumbs: true
sidebar:
  title: "Ontimize Basics"
  nav: sidebar-basics
---

**Important:** This module works only for Ontimize Boot version 3.7.0 or above. Actual release version: [![Ontimize Boot](https://img.shields.io/maven-central/v/com.ontimize.boot/ontimize-boot?label=Ontimize%20boot&style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.ontimize.boot/ontimize-boot)
{: .notice--warning}

# Introduction
The module **Ontimize Jee Report** is a set of reporting tools based in [OntimizeBoot](https://github.com/ontimize/ontimize-boot){:target="_blank"}.


**Reports** implements reporting with:

# Report **store**
The Report Store system will allow you to store, manage and export all kinds of reports designed and implemented via the JasperReports API. This module will let you use your Ontimize application data as data sources for your reports, allowing you to fully customize its layout with tables, charts, graphs… and also visualize, export, print and download your reports. You can find more information at this [link]({{ base_path }}/ontimize-boot/basics/reports/report-store)

# Report **on-demand**
Allow the final users of the applications developed with Ontimize to define, view and store reports from any table available in the application.
This visual tool will allow users to define parameters of the report such as **title, subtitle, columns to display, differents styles options, sorting, grouping and data aggregate functions** of the report. With these parameters and the data of the table, this component will dynamically generate the report and present it to the user. In addition, all these settings will can be stored in a system of preferences. You can find more information at this [link]({{ base_path }}/ontimize-boot/basics/reports/report-on-demand)