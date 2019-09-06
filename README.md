# Serverless Workflow API

Serverless Workflow Specification Version 0.1 - https://github.com/cncf/wg-serverless/blob/master/workflow/spec/spec.md

This project provides:

* JSON Schema (https://json-schema.org/) for the Serverless Workflow specification
* Java api generated from the Serverless Workflow JSON Schema
* Jackson Marshalling and Unmarshalling of the object model to/from JSON
* Workflow Validation (both schema validation and model validation rules)
* Event Expression evaluation
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
WorkflowController controller = new WorkflowController().forJson(json);
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
WorkflowController controller = new WorkflowController().forWorkflow(workflow);
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
    WorkflowController controller = new WorkflowController().forJson(json);
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


You can also enable strict validation (extra checks):
```java
    WorkflowController controller = new WorkflowController().forJson(json).withStrictValidation(true);
```
(strict validation is false by default)

You can also disable schema validation (only workflow based validation will be performed):
```java
    WorkflowController controller = new WorkflowController().forJson(json).withSchemaValidation(false);
```

Or you can disable validation completely:
```java
    WorkflowController controller = new WorkflowController().forJson(json).withValidationEnabled(false);
```
in this case validation errors will always be empty



#### Event Expression evaluation
According to the specification Event States wait for events to happen before triggering one or more functions.
Event states can have multiple events, and each event has an event-expression which defines which outside
events they should trigger upon.

This project provides two event expression evaluators. The default one is based on Apache Commons JEXL (http://commons.apache.org/proper/commons-jexl/).
Alternatively out of the box you can also use Spring Expression Language (SpEL) (https://docs.spring.io/spring/docs/5.2.0.RC1/spring-framework-reference/core.html#expressions)
expressions.

To use SpEL you need to pass it to the workflow controller:
```java
    WorkflowController controller = new WorkflowController().forJson("someJSONString");
    controller.setExpressionEvaluator(new SpelExpressionEvaluator());
    
```

If no expression evaluator is specified, the default one based on Apache Commons JEXL is used.
 
Implementors can also specify their own expression evaluator via the workflow controller, for example:


```java
    WorkflowController controller = new WorkflowController().forJson("someJSONString");
    controller.setExpressionEvaluator(myEvaluator);
    
```

If the default event expression evaluator is used, you can use full powers of JEXL to write your event expressions.
Here are two simple examples:

```json
...
"event-expression": "trigger.equals(\"testtrigger\")"
...
"event-expression": "trigger.equals(\"testtrigger\") or trigger.equals(\"testtrigger2\")",
...
```

For more information on JEXL language syntax, see here: https://commons.apache.org/proper/commons-jexl/reference/syntax.html


Similarly if you use SpEL, you can do for example:

```json
...
"event-expression" : "trigger != null && trigger.equals('testtrigger')",
...
"event-expression" : "trigger != null && (trigger.equals('testtrigger') || trigger.equals('testtrigger2'))",
...
```

### More to come soon!