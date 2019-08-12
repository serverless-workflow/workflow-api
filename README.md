# Serverless Workflow API

Serverless Workflow Specification Version 0.1 - https://github.com/cncf/wg-serverless/blob/master/workflow/spec/spec.md

This project provides:

* JSON Schema (https://json-schema.org/) for the Serverless Workflow specification
* Java api generated from the Serverless Workflow JSON Schema
* Jackson Marshalling and Unmarshalling of the object model to/from JSON
* Workflow Jackson ObjectMapper with custom serializers and deserializers for the Workflow spec


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

### API Examples

#### JSON To Object Model
Given serverless workflow JSON which represents a workflow with a single Event State, for example:

```json
{
  "id" : "testid",
  "name" : "testname",
  "version" : "testversion",
  "description" : "testdescription",
  "owner" : "testOwner",
  "states" : [ {
    "events" : [ {
      "event-expression" : "testEventExpression",
      "timeout" : "testTimeout",
      "action-mode" : "SEQUENTIAL",
      "actions" : [ {
        "function" : "testFunction",
        "timeout" : 5,
        "retry" : {
          "match" : "testMatch",
          "retry-interval" : 2,
          "max-retry" : 10,
          "next-state" : "testNextRetryState"
        }
      } ],
      "next-state" : "testNextState"
    } ],
    "name" : "eventstate",
    "type" : "EVENT",
    "start" : true
  } ]
}

```

You can use this project to read it into the workflow api:

```java
ObjectMapper objectMapper = new WorkflowObjectMapper();
Workflow workflow = objectMapper.readValue(json,Workflow.class);

...

EventState eventState = (EventState) workflow.getStates().get(0);
assertEquals("testEventExpression", event.getEventExpression());

...

Action action = eventState.getActions().get(0);
assertEquals("testFunction", action.getFunction());

...

```

#### Object model to JSON

You can create the Workflow programatically, for example:

```java
Workflow workflow = new Workflow().withStates(new ArrayList<State>() {{
    add(
            new SwitchState().withDefault("defaultteststate").withStart(false).withChoices(
                    new ArrayList<Choice>() {{
                        add(
                                new AndChoice().withNextState("testnextstate").withAnd(
                                        Arrays.asList(
                                                new DefaultChoice().withNextState("testnextstate")
                                                        .withOperator(DefaultChoice.Operator.EQ)
                                                        .withPath("testpath")
                                                        .withValue("testvalue")
                                        )
                                )
                        );
                    }}
            )
    );
}});

ObjectMapper objectMapper = new WorkflowObjectMapper();
String workflowStr = objectMapper.writeValueAsString(workflow);

```
This will produce a workflow JSON with a single Switch State:

```json
{
  "states" : [ {
    "choices" : [ {
      "And" : [ {
        "path" : "testpath",
        "value" : "testvalue",
        "operator" : "EQ",
        "next-state" : "testnextstate"
      } ],
      "next-state" : "testnextstate"
    } ],
    "default" : "defaultteststate",
    "name" : "switchstate",
    "type" : "SWITCH",
    "start" : false
  } ]
}
```

### More to come soon!