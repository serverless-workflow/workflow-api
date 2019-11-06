# Serverless Workflow API

[![CircleCI](https://circleci.com/gh/serverless-workflow/workflow-api.svg?style=svg)](https://circleci.com/gh/serverless-workflow/workflow-api) 

[![Release](https://jitpack.io/v/serverless-workflow/workflow-api.svg)](https://jitpack.io/#serverless-workflow/workflow-api)

Provides the API/SPI for Serverless Workflow capabilities for Kogito

### Getting Started

#### Building locally
To build project and run tets locally:

```
git clone https://github.com/serverless-workflow/workflow-api.git
cd workflow-api
mvn clean install
```

Then to use it in your project pom.xml add:

```xml
<dependency>
    <groupId>org.servlerless</groupId>
    <artifactId>workflow-api</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

#### Using JitPack repository
Add the JitPack repository and the dependency to your pom.xml:

```xml
<dependency>
    <groupId>com.github.serverless-workflow</groupId>
    <artifactId>workflow-api</artifactId>
    <version>Tag</version>
</dependency>
...
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

You can find the list of Tags and future releases here: https://jitpack.io/#serverless-workflow/workflow-api

### For implementors
There are four interfaces which must be implemented:
* WorflowManager - the main manager for wireless workflow implementations
* WorkflowValidator - implementations define validation (both schema and workflow) for serverless workflow JSON
* ExpressionEvaluator - implementations define expression evaluation capabilities for the serverless workflow
* WorkflowPropertySource - implementation of property source  that can be used to initialize workflow values

This api also provides the Service Providers for the three interfaces, namely:
 * WorkflowManagerProvider
 * WorkflowValidatorProvider
 * ExpressionEvaluatorProvider
 * WorkflowPropertySourceProvider
 
 To use these in your implementation, or your app, you can for example do:
 
```java
WorkflowManager manager = WorkflowManagerProvider.getInstance().get();
...
```

### Markup object mapping
This module provides object mappers for both Json and Yaml. 
* To define serverless workflow markup in Json use JsonObjectMapper
* To define servless workflwo markup in Yaml use YamlObjectMapper

### More to come soon!

