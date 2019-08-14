# Serverless Workflow API

Serverless Workflow Specification Version 0.1 - https://github.com/cncf/wg-serverless/blob/master/workflow/spec/spec.md

This project provides:

* JSON Schema (https://json-schema.org/) for the Serverless Workflow specification
* Java api generated from the Serverless Workflow JSON Schema
* Jackson Marshalling and Unmarshalling of the object model to/from JSON
* Workflow Validation (both schema validation and model validation rules)
* WorkflowController class for convenience


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
WorkflowController controller = new WorkflowController(json);
assertTrue(controller.isValid());

...

Workflow workflow = controller.getWorkflow();

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

// you can use the workflow objectmapper:
ObjectMapper objectMapper = new WorkflowObjectMapper();
String workflowStr = objectMapper.writeValueAsString(workflow);

// or you can use the workflow controller too:
WorkflowController controller = new WorkflowController(workflow);
assertTrue(controller.isValid());

String jsonString = controller.toJsonString();

JsonNode jsonNode = controller.toJson();

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

#### Workflow Validation
Workflow controller can help you get both JSON schema and workflow model validation errors. 
For example if we have a bare valid workflow definition without any states:

```json
{
  "states" : []
}
```

we can get validation errors:

```java
    WorkflowController controller = new WorkflowController(json);
    assertTrue(controller.isValid());
    
    List<ValidationError> validationErrors = controller.getValidationErrors();
    
    assertEqual(2, validationErrors.size());
```

the two validation errors should be "No states found" and "No start state found"

If we give invalid workflow json, for example just:

```json
{
  
}
```
We should get the json schema validation error: "#: required key [states] not found"
as the workflow json schema defines the states node as required. 

### More to come soon!