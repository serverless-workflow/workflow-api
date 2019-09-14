# Serverless Workflow API

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
### Support for workflow initializing context
Sometimes you don't want to hard-code all values in your
workflow json/yaml but want to use existing properties or object values
for some defaults, or often used settings.
This can promote reusability and ease maintenance of multiple workflows.

This project provides support for initializing your workflow values from properties. 
These properties can come from different sources such as properties file, map, string, or inputstream.

You can use these properties in your workflow json/yaml for string, enum values (number and boolean not support atm).
To use them your value must start with a "$$" and specify a json path expression.

For example if we had an application.properties file in our app with value:

```
my.workflow.default.name=defaultName
```
you can access this value in your workflow json with:

```json
{
    "name": "$$.my.workflow.default.name",
    "states": []
}
```

Implementors must assure that the InitializingContext class provided here is set up 
and passed to the WorkflowObjectMapper in their implementations for
this feature to be enabled.

### More to come soon!