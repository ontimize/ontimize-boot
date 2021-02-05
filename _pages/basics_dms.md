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