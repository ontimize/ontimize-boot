---
title: "Understanding the application.yml"
layout: single
permalink: /basics/application/
toc: true
toc_label: "Table of Contents"
toc_sticky: true
breadcrumbs: true
---

## Introduction

A YAML file, with extension *.yml or *.yaml, is a human readable file in which we can write data pairs in a suitable way by combinations of lists, maps and simple data. Another of the most important features of these files is the indentation. It is important to write these indented elements correctly, since if they are badly indented, they cannot be parsed correctly.

## Application.yml file description


### Endpoints

Permiten monitorear e interactuar con su aplicación. Existen endpoints integrados, pero también se permite agregar endpoints personalizados.  

- **endpoints: api:**

| Attribute | Values | Meaning |
|--|--|--|
| enabled | true, false | Endpoints de Spring Boot activos. |

**Example**
```yaml
endpoints:
   api:
      enabled: true
```


### Logging

Nivel de log por defecto del servidor establecido al nivel de INFO

- **logging: level:**

**Example**
```yaml
logging:
   level:
      root: info
```

### Corsfilter

| Attribute | Values | Meaning |
|--|--|--|
| enabled | true, false | Habilitando los filtros de CORS. |


**Example**
```yaml
ontimize:
   corsfilter:
      enabled: true
```




