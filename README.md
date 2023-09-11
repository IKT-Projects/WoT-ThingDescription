[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/built-by-developers.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)

# An implementation of the Web of Things (WoT) Thing Description (W3C Candidate Recommendation 16 May 2019)

### Introduction
This project offers a Java class based implementation of the W3C Thing Descriptions.
Thing Descriptions are created with corresponding class builders. The resulting thing-class can be serialized to a JSON-LD document or stored
in a RDF4j TripleStore.

### Notice
This project was created in the research project SENSE and contains experimental implementations and therefore does not claim to be complete 
or bug-free. The use is at your own risk. Any legal claim is excluded. 

### Basic usage example

Create the basic thing properties:

```java
 // create a thing identifier
 URI thingId = URI.create("https://ikt-systems.de:443/MyLamp1");
 // give the thing a title
 String title = "My Lamp 1";
```
Create the context:

```java
List<Context> contexts = Arrays.asList(
        Context.builder().prefix(null).namespace("https://www.w3.org/2019/wot/td/v1").build(),
        Context.builder().prefix("schema").namespace("http://schema.org/").build(),
        Context.builder().prefix("iot").namespace("http://iotschema.org/").build());
```
Create a security definition and add it as security schema:

```java
 Map<String, SecurityScheme> secDef = new HashMap<>();
 secDef.put("basic_sc", BasicSecurityScheme.builder().scheme(Basic).in(Header).build());
 
 List<String> security = new ArrayList<>();
 security.add("basic_sc");
```
Create properties, actions, events:

```java
PropertyAffordance level = PropertyAffordance.builder()
        .ds(NumberSchema.builder().minimum(0.0f).maximum(100.0f).title("CurrentDimmer")
            .description("Dimming value in percentage")
            .atType(Arrays.asList("Property", "iot:CurrentDimmer")).unit("iot:Percent").build())
        .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.readproperty))
            .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b42ff"))
            .contentType("application/json").subprotocol("https").build()))
        .build();

// add the properties to the properties map
Map<String, PropertyAffordance> properties = new HashMap<>();
properties.put("level", level);

ActionAffordance turnOn = ActionAffordance.builder().atType(Arrays.asList("Action"))
        .output(BooleanSchema.builder().title("On").build())
        .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.invokeaction))
            .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b42ff"))
            .contentType("application/json").subprotocol("https").build()))
        .build();
        
// add the actions to the actions map
Map<String, ActionAffordance> actions = new HashMap<>();
actions.put("on", turnOn);
    
```
Finally we can create the full thing description:

```java
Thing t = Thing.builder().context(contexts).id(thingId).title(title)
        .description("A web connected color light").atType(Arrays.asList("Thing", "iot:Actuator"))
        .securityDefinitions(secDef).security(security).properties(properties).actions(actions)
        .build();
```
Additionally we can create a preconfigured Gson instance and serialize the thing to JSON-LD:

```java
// get a preconfigured Gson instance with pretty printing option set to true
Gson gson = AdapterFactory.getGsonWithDefaultWotTypeAdapters(true);
// serialize the thing to json-ld
String jsonThing = gson.toJson(t);
// print the serialized version of the thing description
System.out.println(jsonThing);
```


## License
Apache License

_Version 2.0, January 2004_  
http://www.apache.org/licenses/
