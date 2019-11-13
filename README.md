# Serverless Workflow API

Provides the API/SPI for the
Serverless Workflow Specification Version 0.1 - https://github.com/cncf/wg-serverless/blob/master/workflow/spec/spec.md

It is the base for implementors of the Serverless Workflow Specification.

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

