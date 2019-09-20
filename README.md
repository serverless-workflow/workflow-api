# Serverless Workflow API

[![CircleCI](https://circleci.com/gh/serverless-workflow/workflow-api.svg?style=svg)](https://circleci.com/gh/serverless-workflow/workflow-api)

Provides the API/SPI for the
Serverless Workflow Specification Version 0.1 - https://github.com/cncf/wg-serverless/blob/master/workflow/spec/spec.md

It is the base for implementors of the Serverless Workflow Specification.

### Getting Started

To build project and run tets:

```
mvn clean install
```

To use this project add the following dependency into your project pom.xml:

```xml
<dependency>
    <groupId>org.servlerless</groupId>
    <artifactId>workflow-api</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
### For implementors
There are three interfaces which must be implemented:
* WorflowManager - the main manager for wireless workflow implementations
* WorkflowValidator - implementations define validation (both schema and workflow) for serverless workflow JSON
* ExpressionEvaluator - implementations define expression evaluation capabilities for the serverless workflow

This api also provides the Service Providers for the three interfaces, namely:
 * WorkflowManagerProvider
 * WorkflowValidatorProvider
 * ExpressionEvaluatorProvider
 
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
