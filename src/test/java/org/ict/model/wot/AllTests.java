/*
 * Copyright © 2023 Institut fuer Kommunikationstechnik - FH-Dortmund (codebase.ikt@fh-dortmund.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ict.model.wot;

import static org.ict.model.wot.constant.In.Header;
import static org.ict.model.wot.constant.SecuritySchemaType.Basic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cyberborean.rdfbeans.RDFBeanManager;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.ict.gson.utils.AdapterFactory;
import org.ict.model.jsonld.context.Context;
import org.ict.model.wot.constant.Alg;
import org.ict.model.wot.constant.Format;
import org.ict.model.wot.constant.In;
import org.ict.model.wot.constant.Op;
import org.ict.model.wot.constant.SecuritySchemaType;
import org.ict.model.wot.core.ActionAffordance;
import org.ict.model.wot.core.PropertyAffordance;
import org.ict.model.wot.core.Thing;
import org.ict.model.wot.core.VersionInfo;
import org.ict.model.wot.dataschema.ArraySchema;
import org.ict.model.wot.dataschema.BooleanSchema;
import org.ict.model.wot.dataschema.DataSchema;
import org.ict.model.wot.dataschema.IntegerSchema;
import org.ict.model.wot.dataschema.NumberSchema;
import org.ict.model.wot.dataschema.ObjectSchema;
import org.ict.model.wot.dataschema.StringSchema;
import org.ict.model.wot.hypermedia.Form;
import org.ict.model.wot.security.BasicSecurityScheme;
import org.ict.model.wot.security.BearerSecurityScheme;
import org.ict.model.wot.security.SecurityScheme;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

/**
 * @author F. Kohlmorgen
 */
public class AllTests {
  private static Logger log = LogManager.getFormatterLogger(AllTests.class);
  private static Gson gson;
  private static RDFBeanManager manager;

  @BeforeAll
  static void initAll() {
    gson = AdapterFactory.getGsonWithDefaultWotTypeAdapters();
    Repository db = new SailRepository(new MemoryStore());
    // Repository db = new HTTPRepository("http://localhost:8080/rdf4j-server/", "1");
    db.init();
    RepositoryConnection con = db.getConnection();
    manager = new RDFBeanManager(con);
  }

  private List<URI> getFakeTypes() {
    return Arrays.asList(URI.create("ns:type1"), URI.create("ns:type2"), URI.create("ns:type3"));
  }

  @Test
  void testInteractionAffordanceAtTypeWithBean() {

    try {
      List<URI> atTypes = getFakeTypes();
      BooleanSchema in = BooleanSchema.builder().title("Some boolean schema")
          .description("The state to be set (true/false)").build();
      ActionAffordance action =
          ActionAffordance.builder().title("Test action").atType(atTypes).input(in)
              .forms(Arrays.asList(
                  Form.builder().op(Arrays.asList(Op.invokeaction)).href(URI.create("http://test.uri"))
                      .contentType("application/json").subprotocol("https").build()))
              .build();
      // write to the RDF repo
      Resource res = manager.add(action);
      ActionAffordance newAction = manager.get(res, ActionAffordance.class);
      String json = gson.toJson(newAction);
      log.info(json);
      assertEquals(action, newAction);
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test
  void testUriVariables() {
    try {
      String expected = "{\n" + "  \"@context\": [\n"
          + "    \"https://www.w3.org/2019/wot/td/v1\",\n" + "    {\n"
          + "      \"saref\": \"http://ontology.tno.nl/saref#\",\n"
          + "      \"knx\": \"http://knx.org/ontology/\"\n" + "    }\n" + "  ],\n"
          + "  \"id\": \"id:test:uriVariables\",\n" + "  \"title\": \"Color Light\",\n"
          + "  \"security\": [\n" + "    \"basic_sc\"\n" + "  ],\n" + "  \"@type\": [\n"
          + "    \"iot:Actuator\"\n" + "  ],\n" + "  \"titles\": {\n"
          + "    \"de\": \"Meine 1. Lampe\",\n" + "    \"en\": \"My Light 1\"\n" + "  },\n"
          + "  \"descriptions\": {\n"
          + "    \"de\": \"Ein mit den Internet verbundenes dimmbares Licht\",\n"
          + "    \"en\": \"A web connected dimmable light\"\n" + "  },\n" + "  \"version\": {\n"
          + "    \"instance\": \"0.1\"\n" + "  },\n"
          + "  \"support\": \"https://www.fh-dortmund.de/de/fb/10/ikt/index.php\",\n"
          + "  \"base\": \"https://iktsystems.goip.de:443\",\n" + "  \"properties\": {\n"
          + "    \"intValue\": {\n" + "      \"observable\": false,\n" + "      \"minimum\": 0,\n"
          + "      \"maximum\": 100,\n" + "      \"forms\": [\n" + "        {\n"
          + "          \"op\": [\n" + "            \"readproperty\"\n" + "          ],\n"
          + "          \"href\": \"https://iktsystems.goip.de:443/parameters/106f4202b66b4211\",\n"
          + "          \"contentType\": \"application/json\",\n"
          + "          \"subprotocol\": \"https\",\n"
          + "          \"@type\": \"saref:GetCommand\"\n" + "        }\n" + "      ],\n"
          + "      \"uriVariables\": {\n" + "        \"var2\": {\n" + "          \"minimum\": 0,\n"
          + "          \"maximum\": 100,\n"
          + "          \"instanceOf\": \"org.ict.model.wot.dataschema.IntegerSchema\",\n"
          + "          \"@type\": [\n" + "            \"integer:type\"\n" + "          ],\n"
          + "          \"type\": \"integer\",\n" + "          \"readOnly\": false,\n"
          + "          \"writeOnly\": false\n" + "        },\n" + "        \"var1\": {\n"
          + "          \"instanceOf\": \"org.ict.model.wot.dataschema.BooleanSchema\",\n"
          + "          \"@type\": [\n" + "            \"boolean:type\"\n" + "          ],\n"
          + "          \"type\": \"boolean\",\n" + "          \"readOnly\": false,\n"
          + "          \"writeOnly\": false\n" + "        }\n" + "      },\n"
          + "      \"instanceOf\": \"org.ict.model.wot.dataschema.IntegerSchema\",\n"
          + "      \"@type\": [\n" + "        \"saref:SomeFunction\"\n" + "      ],\n"
          + "      \"title\": \"IntValue\",\n"
          + "      \"description\": \"Current value as integer\",\n"
          + "      \"type\": \"integer\",\n" + "      \"readOnly\": true,\n"
          + "      \"writeOnly\": false\n" + "    },\n" + "    \"status\": {\n"
          + "      \"observable\": false,\n" + "      \"forms\": [\n" + "        {\n"
          + "          \"op\": [\n" + "            \"readproperty\"\n" + "          ],\n"
          + "          \"href\": \"https://iktsystems.goip.de:443/parameters/106f4202b77b4211\",\n"
          + "          \"contentType\": \"application/json\",\n"
          + "          \"subprotocol\": \"https\",\n"
          + "          \"@type\": \"saref:GetCommand\"\n" + "        }\n" + "      ],\n"
          + "      \"uriVariables\": {\n" + "        \"var2\": {\n" + "          \"minimum\": 0,\n"
          + "          \"maximum\": 100,\n"
          + "          \"instanceOf\": \"org.ict.model.wot.dataschema.IntegerSchema\",\n"
          + "          \"@type\": [\n" + "            \"integer:type\"\n" + "          ],\n"
          + "          \"type\": \"integer\",\n" + "          \"readOnly\": false,\n"
          + "          \"writeOnly\": false\n" + "        },\n" + "        \"var1\": {\n"
          + "          \"instanceOf\": \"org.ict.model.wot.dataschema.BooleanSchema\",\n"
          + "          \"@type\": [\n" + "            \"boolean:type\"\n" + "          ],\n"
          + "          \"type\": \"boolean\",\n" + "          \"readOnly\": false,\n"
          + "          \"writeOnly\": false\n" + "        }\n" + "      },\n"
          + "      \"instanceOf\": \"org.ict.model.wot.dataschema.BooleanSchema\",\n"
          + "      \"@type\": [\n" + "        \"saref:OnOffFunction\"\n" + "      ],\n"
          + "      \"title\": \"SwitchStatus\",\n"
          + "      \"description\": \"Current value of On/Off status\",\n"
          + "      \"type\": \"boolean\",\n" + "      \"readOnly\": true,\n"
          + "      \"writeOnly\": false\n" + "    }\n" + "  },\n" + "  \"securityDefinitions\": {\n"
          + "    \"basic_sc\": {\n" + "      \"scheme\": \"basic\",\n"
          + "      \"instanceOf\": \"org.ict.model.wot.security.BasicSecurityScheme\",\n"
          + "      \"in\": \"header\"\n" + "    }\n" + "  }\n" + "}";

      Context[] contexts = new Context[] {
          Context.builder().prefix(null).namespace("https://www.w3.org/2019/wot/td/v1").build(),
          Context.builder().prefix("saref").namespace("http://ontology.tno.nl/saref#").build(),
          Context.builder().prefix("knx").namespace("http://knx.org/ontology/").build()};

      URI thingId = URI.create("id:test:uriVariables");
      String title = "Color Light";
      Map<String, SecurityScheme> secDef = new HashMap<>();
      secDef.put("basic_sc", BasicSecurityScheme.builder().scheme(Basic).in(Header).build());
      List<String> security = new ArrayList<>();
      security.add("basic_sc");

      // create properties
      Map<String, PropertyAffordance> properties = new HashMap<>();

      // create uriVariables
      Map<String, DataSchema> uriVars = new HashMap<>();
      uriVars.put("var1",
          BooleanSchema.builder().atType(Arrays.asList(URI.create("boolean:type"))).build());
      uriVars.put("var2", IntegerSchema.builder().atType(Arrays.asList(URI.create("integer:type")))
          .minimum(0).maximum(100).build());

      Map<String, JsonElement> additionalProperties = new HashMap<>();
      additionalProperties.put("@type", new JsonPrimitive("saref:GetCommand"));
      PropertyAffordance switchStatus = PropertyAffordance.builder().uriVariables(uriVars)
          .ds(BooleanSchema.builder().title("SwitchStatus")
              .description("Current value of On/Off status")
              .atType(Arrays.asList(URI.create("saref:OnOffFunction"))).readOnly(true).build())
          .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.readproperty))
              .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b4211"))
              .contentType("application/json").subprotocol("https")
              .additionalProperties(additionalProperties).build()))
          .build();

      PropertyAffordance intValue = PropertyAffordance.builder().uriVariables(uriVars)
          .ds(IntegerSchema.builder().title("IntValue").description("Current value as integer")
              .atType(Arrays.asList(URI.create("saref:SomeFunction"))).minimum(0).maximum(100)
              .readOnly(true).build())
          .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.readproperty))
              .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b66b4211"))
              .contentType("application/json").subprotocol("https")
              .additionalProperties(additionalProperties).build()))
          .build();

      properties.put("status", switchStatus);
      properties.put("intValue", intValue);

      Map<String, String> descriptions = new HashMap<>();
      descriptions.put("en", "A web connected dimmable light");
      descriptions.put("de", "Ein mit den Internet verbundenes dimmbares Licht");

      Map<String, String> titles = new HashMap<>();
      titles.put("en", "My Light 1");
      titles.put("de", "Meine 1. Lampe");

      Thing t = Thing.builder().contexts(contexts).id(thingId)
          .atType(Arrays.asList(URI.create("saref:Actuator"), URI.create("saref:LightSwitch")))
          .title(title).titles(titles).descriptions(descriptions)
          .version(VersionInfo.builder().instance("0.1").build())
          .support(URI.create("https://www.fh-dortmund.de/de/fb/10/ikt/index.php"))
          .base(URI.create("https://iktsystems.goip.de:443"))
          .atType(Arrays.asList(URI.create("iot:Actuator"))).securityDefinitions(secDef)
          .security(security).properties(properties).build();
      JsonParser parser = new JsonParser();
      assertEquals(parser.parse(expected), parser.parse(gson.toJson(t)));
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test
  void testArraySchema() {
    List<DataSchema> items = new ArrayList<>(10);
    for (int i = 0; i < 3; i++) {
      Map<String, DataSchema> properties = new HashMap<>(2);
      properties.put("id", IntegerSchema.builder().minimum(i).maximum(i)
          .description("The id of the enum element").build());
      properties.put("name", StringSchema.builder().title(String.format("Element %d", i))
          .description("The name of the enum element").build());
      ObjectSchema os = ObjectSchema.builder().properties(properties).build();
      items.add(os);
    }
    ArraySchema out =
        ArraySchema.builder().title("Some title").description("The enum value and its mapped id")
            .minItems(3L).maxItems(3L).items(items).build();
    String json = gson.toJson(out);
    log.info(json);
    assertEquals(json,
        "{\"items\":[{\"properties\":{\"name\":{\"instanceOf\":\"org.ict.model.wot.dataschema.StringSchema\",\"title\":\"Element 0\",\"description\":\"The name of the enum element\",\"type\":\"string\",\"readOnly\":false,\"writeOnly\":false},\"id\":{\"minimum\":0,\"maximum\":0,\"instanceOf\":\"org.ict.model.wot.dataschema.IntegerSchema\",\"description\":\"The id of the enum element\",\"type\":\"integer\",\"readOnly\":false,\"writeOnly\":false}},\"instanceOf\":\"org.ict.model.wot.dataschema.ObjectSchema\",\"type\":\"object\",\"readOnly\":false,\"writeOnly\":false},{\"properties\":{\"name\":{\"instanceOf\":\"org.ict.model.wot.dataschema.StringSchema\",\"title\":\"Element 1\",\"description\":\"The name of the enum element\",\"type\":\"string\",\"readOnly\":false,\"writeOnly\":false},\"id\":{\"minimum\":1,\"maximum\":1,\"instanceOf\":\"org.ict.model.wot.dataschema.IntegerSchema\",\"description\":\"The id of the enum element\",\"type\":\"integer\",\"readOnly\":false,\"writeOnly\":false}},\"instanceOf\":\"org.ict.model.wot.dataschema.ObjectSchema\",\"type\":\"object\",\"readOnly\":false,\"writeOnly\":false},{\"properties\":{\"name\":{\"instanceOf\":\"org.ict.model.wot.dataschema.StringSchema\",\"title\":\"Element 2\",\"description\":\"The name of the enum element\",\"type\":\"string\",\"readOnly\":false,\"writeOnly\":false},\"id\":{\"minimum\":2,\"maximum\":2,\"instanceOf\":\"org.ict.model.wot.dataschema.IntegerSchema\",\"description\":\"The id of the enum element\",\"type\":\"integer\",\"readOnly\":false,\"writeOnly\":false}},\"instanceOf\":\"org.ict.model.wot.dataschema.ObjectSchema\",\"type\":\"object\",\"readOnly\":false,\"writeOnly\":false}],\"minItems\":3,\"maxItems\":3,\"instanceOf\":\"org.ict.model.wot.dataschema.ArraySchema\",\"title\":\"Some title\",\"description\":\"The enum value and its mapped id\",\"type\":\"array\",\"readOnly\":false,\"writeOnly\":false}");
  }

  @Test
  void testBooleanSchemaAtTypeWithBean() {
    BooleanSchema bs = BooleanSchema.builder().atType(getFakeTypes()).build();

    try {
      Resource res = manager.add(bs);
      BooleanSchema newbs = manager.get(res, BooleanSchema.class);
      assertEquals(bs, newbs);
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
    String json = gson.toJson(bs);
    log.info(json);
    // write to the RDF repo
    assertEquals(json,
        "{\"instanceOf\":\"org.ict.model.wot.dataschema.BooleanSchema\",\"@type\":[\"ns:type1\",\"ns:type2\",\"ns:type3\"],\"type\":\"boolean\",\"readOnly\":false,\"writeOnly\":false}");
  }

  @Test
  void testBearerSecuritySchema() {
    BearerSecurityScheme bearer = BearerSecurityScheme.builder().scheme(SecuritySchemaType.Bearer)
        .authorization(URI.create("https://authorization.com")).name("bearer_sc").format(Format.JWT)
        .alg(Alg.ES256).in(In.Header).build();
    String json = gson.toJson(bearer);
    log.info(json);
    assertEquals(json,
        "{\"authorization\":\"https://authorization.com\",\"alg\":\"ES256\",\"format\":\"jwt\",\"in\":\"header\",\"name\":\"bearer_sc\",\"instanceOf\":\"org.ict.model.wot.security.BearerSecurityScheme\",\"scheme\":\"bearer\"}");
  }

  @Test
  void testObjectSchemaWithBean() {
    Map<String, DataSchema> properties = new HashMap<>();
    properties.put("first", IntegerSchema.builder().atType(getFakeTypes()).minimum(0).maximum(100)
        .title("first property with type integer").build());
    properties.put("second", BooleanSchema.builder().atType(getFakeTypes())
        .title("second property with type boolean").build());

    ObjectSchema os = ObjectSchema.builder().atType(getFakeTypes()).title("Object Schema test")
        .properties(properties).build();
    // write to the RDF repo
    try {
      Resource res = manager.add(os);
      ObjectSchema newos = manager.get(res, ObjectSchema.class);
      assertEquals(os, newos);
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test
  void testThingWithBean() {
    try {
      Thing t = createLightThing("https://ikt-systems.de:443/iot/saref/LightExmple1");
      Resource res = manager.update(t);
      Thing newt = manager.get(res, Thing.class);
      assertEquals(t, newt);
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test
  void testThingToAndFromJson() {
    try {
      Thing t = createLightThing("https://ikt-systems.de:443/iot/saref/LightExmple1");
      String json = gson.toJson(t);
      log.info(json);
      Thing newt = gson.fromJson(json, Thing.class);
      String json2 = gson.toJson(newt);
      log.info(json2);
      assertEquals(t, newt);
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test
  void testThingFromJson() {
    try {
      String testThing = readLineByLineJava8("Example.json");
      Thing t = gson.fromJson(testThing, Thing.class);
      log.info(t);
      assertNotNull(t);
    } catch (Throwable t) {
      t.printStackTrace();
      fail(t.getMessage());
    }
  }

  @Test
  void testFormWithBean() {
    try {
      final Map<String, JsonElement> add_props = new HashMap<>();

      add_props.put("http:fieldName:1", new JsonPrimitive("x-api-key"));
      add_props.put("http:fieldValue:1", new JsonPrimitive("text/plain"));
      add_props.put("http:fieldName:2", new JsonPrimitive("Authorization"));
      add_props.put("http:fieldValue:2", new JsonPrimitive("text/plain"));
      add_props.put("http:fieldName:3", new JsonPrimitive("X-Host-Override"));
      add_props.put("http:fieldValue:3", new JsonPrimitive("text/plain"));
      add_props.put("http:fieldName:4", new JsonPrimitive("Content-Type"));
      add_props.put("http:fieldValue:4", new JsonPrimitive("application/x-www-form-urlencoded"));


      Form f = Form.builder().op(Arrays.asList(Op.readproperty))
          .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b4211"))
          .contentType("application/json").subprotocol("https").additionalProperties(add_props)
          .build();
      Resource res = manager.update(f);
      Form newf = manager.get(res, Form.class);
      String json = gson.toJson(newf);
      log.info(json);
      assertEquals(f, newf);
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  /**
   * We have to test with a minimal thing definition here, because RDFBean does not support HashMap
   * serialization.
   */
  @Test
  void testSecuritySchemaWithBean() {
    try {

      Map<String, SecurityScheme> s = new HashMap<>();
      s.put("basic_sc", BasicSecurityScheme.builder().scheme(Basic).in(Header).build());
      List<String> security = new ArrayList<>();
      security.add("basic_sc");

      Context[] contexts = new Context[] {
          Context.builder().prefix(null).namespace("https://www.w3.org/2019/wot/td/v1").build()};

      Thing t = Thing.builder().contexts(contexts).id(URI.create("urn:id")).title("some title")
          .description("some description").securityDefinitions(s).security(security).build();
      Resource res = manager.update(t);
      Thing newt = manager.get(res, Thing.class);

      assertEquals(t, newt);
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  // @Test
  // void testContextWithBean() {
  // try {
  // List<Object> contexts = new ArrayList<>();
  // contexts.add("https://www.w3.org/2019/wot/td/v1");
  //
  // Resource res = manager.update(contexts);
  // @SuppressWarnings("unchecked")
  // List<Object> newcontexts = (List<Object>) manager.get(res, List.class);
  //
  // assertEquals(contexts, newcontexts);
  // } catch (Exception e) {
  // e.printStackTrace();
  // fail(e.getMessage());
  // }
  // }

  @Test
  void testPropertyAffordanceWithObjectSchemaAndBean() {
    try {
      Map<String, DataSchema> props = new HashMap<>(2);
      // always add the time property
      props.put("time",
          StringSchema.builder().title("Date time").description("The date time in ISO 8601 format")
              .atType(Arrays.asList(URI.create("schema:DateTime"))).build());

      BooleanSchema out = BooleanSchema.builder().title("value")
          .description("The current boolean state (true/false)").unit("some unit")
          .atType(Arrays.asList(URI.create("iot:Temperature"))).build();

      props.put("value", out);
      ObjectSchema obj =
          ObjectSchema.builder().title("Current temperature").properties(props).build();

      PropertyAffordance p = PropertyAffordance.builder().ds(obj)
          .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.readproperty))
              .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b4211"))
              .contentType("application/json").subprotocol("https").build()))
          .build();
      Resource res = manager.update(p);
      PropertyAffordance newp = manager.get(res, PropertyAffordance.class);
      assertEquals(p, newp);
      String json = gson.toJson(newp);
      log.info(json);
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test
  void testPropertyAffordanceWithObjectSchemaAndBeanDateTime() {
    try {
      String uuid = UUID.randomUUID().toString();
      Map<String, DataSchema> props = new HashMap<>(2);

      StringSchema time = StringSchema.builder().id("urn:" + uuid).title("Date time")
          .description("The date time in ISO 8601 format")
          .atType(Arrays.asList(URI.create("schema:DateTime"))).build();

      NumberSchema value = NumberSchema.builder().minimum(-30.0).maximum(60.0)
          .atType(Arrays.asList(URI.create("mozilla:TemperatureProperty")))
          .unit("mozilla:degree celsius").description("The current temperature in °C")
          .modified(time).build();

      // always add the time property
      props.put("time", time);
      props.put("value", value);
      ObjectSchema obj =
          ObjectSchema.builder().atType(Arrays.asList(URI.create("iot:CurrentTemperatur")))
              .title("Current temperature").properties(props).build();

      PropertyAffordance p = PropertyAffordance.builder().ds(obj)
          .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.readproperty))
              .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b4211"))
              .contentType("application/json").subprotocol("https").build()))
          .build();
      String json = gson.toJson(p);
      log.info(json);
      Resource res = manager.update(p);
      PropertyAffordance newp = manager.get(res, PropertyAffordance.class);
      assertEquals(p, newp);
      json = gson.toJson(newp);
      log.info(json);
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test
  void testPropertyAffordanceWithObjectSchemaAndBeanDateTimeInt() {
    try {
      String uuid = UUID.randomUUID().toString();
      Map<String, DataSchema> props = new HashMap<>(2);

      StringSchema time = StringSchema.builder().id("urn:" + uuid).title("Date time")
          .description("The date time in ISO 8601 format")
          .atType(Arrays.asList(URI.create("schema:DateTime"))).build();

      IntegerSchema value = IntegerSchema.builder().minimum(-30).maximum(60)
          .atType(Arrays.asList(URI.create("mozilla:TemperatureProperty")))
          .unit("mozilla:degree celsius").description("The current temperature in °C")
          .modified(time).build();

      // always add the time property
      props.put("time", time);
      props.put("value", value);
      ObjectSchema obj =
          ObjectSchema.builder().title("Current temperature").properties(props).build();

      PropertyAffordance p = PropertyAffordance.builder().ds(obj)
          .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.readproperty))
              .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b4211"))
              .contentType("application/json").subprotocol("https").build()))
          .build();
      String json = gson.toJson(p);
      log.info(json);
      Resource res = manager.update(p);
      PropertyAffordance newp = manager.get(res, PropertyAffordance.class);
      assertEquals(p, newp);
      json = gson.toJson(newp);
      log.info(json);
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test
  void testActionAffordanceWithObjectSchemaAndBeanDateTimeInt() {
    try {
      String uuid = UUID.randomUUID().toString();
      Map<String, DataSchema> props = new HashMap<>(2);

      StringSchema time = StringSchema.builder().id("urn:" + uuid).title("Date time")
          .description("The date time in ISO 8601 format")
          .atType(Arrays.asList(URI.create("schema:DateTime"))).build();

      IntegerSchema value = IntegerSchema.builder().minimum(-30).maximum(60)
          .atType(Arrays.asList(URI.create("mozilla:TemperatureProperty")))
          .unit("mozilla:degree celsius").description("The current temperature in °C")
          .modified(time).build();

      // always add the time property
      props.put("time", time);
      props.put("value", value);
      ObjectSchema obj = ObjectSchema.builder().atType(Arrays.asList(URI.create("iot:Temperature")))
          .title("Current temperature").properties(props).build();

      ActionAffordance a = ActionAffordance.builder().output(obj).input(obj)
          .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.readproperty))
              .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b4211"))
              .contentType("application/json").subprotocol("https").build()))
          .build();
      String json = gson.toJson(a);
      log.info(json);
      Resource res = manager.update(a);
      ActionAffordance newp = manager.get(res, ActionAffordance.class);
      assertEquals(a, newp);
      json = gson.toJson(newp);
      log.info(json);
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test
  void testPropertyAffordanceWithBean() {
    try {
      Map<String, JsonElement> additionalProp = new HashMap<>();
      additionalProp.put("@type", new JsonPrimitive("saref:GetCommand"));
      PropertyAffordance p = PropertyAffordance.builder()
          .ds(BooleanSchema.builder().title("SwitchStatus")
              .description("Current value of On/Off status")
              .atType(Arrays.asList(URI.create("saref:OnOffFunction"))).readOnly(true).build())
          .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.readproperty))
              .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b4211"))
              .contentType("application/json").subprotocol("https")
              .additionalProperties(additionalProp).build()))
          .build();
      Resource res = manager.update(p);
      PropertyAffordance newp = manager.get(res, PropertyAffordance.class);
      assertEquals(p, newp);
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test
  void testPropertyAffordanceAddPropBean() {
    try {
      // form 1
      Map<String, JsonElement> additionalProp = new HashMap<>();
      additionalProp.put("http:methodName", new JsonPrimitive("GET"));
      additionalProp.put("http:header:1:fieldName", new JsonPrimitive("x-api-key"));
      additionalProp.put("http:header:1:fieldValue", new JsonPrimitive("text/plain"));
      additionalProp.put("http:header:2:fieldName", new JsonPrimitive("Authorization"));
      additionalProp.put("http:header:2:fieldValue", new JsonPrimitive("text/plain"));
      additionalProp.put("http:header:3:fieldName", new JsonPrimitive("X-Host-Override"));
      additionalProp.put("http:header:3:fieldValue", new JsonPrimitive("text/plain"));

      // form 2
      Map<String, JsonElement> additionalProp2 = new HashMap<>();
      additionalProp2.put("http:methodName", new JsonPrimitive("POST"));
      additionalProp2.put("http:header:1:fieldName", new JsonPrimitive("x-api-key"));
      additionalProp2.put("http:header:1:fieldValue", new JsonPrimitive("text/plain"));
      additionalProp2.put("http:header:2:fieldName", new JsonPrimitive("Authorization"));
      additionalProp2.put("http:header:2:fieldValue", new JsonPrimitive("text/plain"));
      additionalProp2.put("http:header:3:fieldName", new JsonPrimitive("X-Host-Override"));
      additionalProp2.put("http:header:3:fieldValue", new JsonPrimitive("text/plain"));
      additionalProp2.put("http:header:4:fieldName", new JsonPrimitive("Content-Type"));
      additionalProp2.put("http:header:4:fieldValue", new JsonPrimitive("application/json"));

      PropertyAffordance p =
          PropertyAffordance
              .builder().ds(BooleanSchema
                  .builder().title("SwitchStatus").description("Current value of On/Off status")
                  .atType(Arrays.asList(URI.create("saref:OnOffFunction"))).readOnly(true).build())
              .forms(
                  Arrays
                      .asList(
                          Form.builder().op(Arrays.asList(Op.readproperty))
                              .href(URI.create(
                                  "https://iktsystems.goip.de:443/parameters/106f4202b77b4211"))
                              .contentType("application/json").subprotocol("https")
                              .additionalProperties(additionalProp).build(),
                          Form.builder().op(Arrays.asList(Op.writeproperty))
                              .href(URI.create(
                                  "https://iktsystems.goip.de:443/parameters/106f4202b77b4211"))
                              .contentType("application/json").subprotocol("https")
                              .additionalProperties(additionalProp).build()))
              .build();
      Resource res = manager.update(p);
      PropertyAffordance newp = manager.get(res, PropertyAffordance.class);
      assertEquals(p, newp);
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @AfterAll
  static void tearDownAll() {
    manager.getRepositoryConnection().close();
    manager = null;
  }

  private static Thing createLightThing(String id) throws MalformedURLException {
    Context[] contexts = new Context[] {
        Context.builder().prefix(null).namespace("https://www.w3.org/2019/wot/td/v1").build(),
        Context.builder().prefix("saref").namespace("http://ontology.tno.nl/saref#").build(),
        Context.builder().prefix("knx").namespace("http://knx.org/ontology/").build()};

    URI thingId = URI.create(id);
    String title = "Color Light";
    Map<String, SecurityScheme> secDef = new HashMap<>();
    secDef.put("basic_sc", BasicSecurityScheme.builder().scheme(Basic).in(Header).build());
    List<String> security = new ArrayList<>();
    security.add("basic_sc");

    // create properties
    Map<String, PropertyAffordance> properties = new HashMap<>();

    Map<String, JsonElement> additionalProperties = new HashMap<>();
    additionalProperties.put("@type", new JsonPrimitive("saref:GetCommand"));
    PropertyAffordance switchStatus = PropertyAffordance.builder()
        .ds(BooleanSchema.builder().title("SwitchStatus")
            .description("Current value of On/Off status")
            .atType(Arrays.asList(URI.create("saref:OnOffFunction"))).readOnly(true).build())
        .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.readproperty))
            .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b4211"))
            .contentType("application/json").subprotocol("https")
            .additionalProperties(additionalProperties).build()))
        .build();

    PropertyAffordance intValue = PropertyAffordance.builder()
        .ds(IntegerSchema.builder().title("IntValue").description("Current value as integer")
            .atType(Arrays.asList(URI.create("saref:SomeFunction"))).minimum(0).maximum(100)
            .readOnly(true).build())
        .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.readproperty))
            .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b66b4211"))
            .contentType("application/json").subprotocol("https")
            .additionalProperties(additionalProperties).build()))
        .build();

    properties.put("status", switchStatus);
    properties.put("intValue", intValue);

    Map<String, String> descriptions = new HashMap<>();
    descriptions.put("en", "A web connected dimmable light");
    descriptions.put("de", "Ein mit den Internet verbundenes dimmbares Licht");

    Map<String, String> titles = new HashMap<>();
    titles.put("en", "My Light 1");
    titles.put("de", "Meine 1. Lampe");

    return Thing.builder().contexts(contexts).id(thingId)
        .atType(Arrays.asList(URI.create("saref:Actuator"), URI.create("saref:LightSwitch")))
        .title(title).titles(titles).descriptions(descriptions)
        .version(VersionInfo.builder().instance("0.1").build()).created(new Date())
        .modified(new Date())
        .support(URI.create("https://www.fh-dortmund.de/de/fb/10/ikt/index.php"))
        .base(URI.create("https://iktsystems.goip.de:443"))
        .atType(Arrays.asList(URI.create("iot:Actuator"))).securityDefinitions(secDef)
        .security(security).properties(properties).build();
  }

  @SuppressWarnings("unused")
  private static <T extends List<String>> JsonArray createArray(T t) {
    JsonArray ops = new JsonArray(t.size());

    for (String e : t) {
      ops.add(e);
    }
    return ops;
  }

  private static String readLineByLineJava8(String filePath) {
    StringBuilder contentBuilder = new StringBuilder();
    try (Stream<String> stream =
        Files.readLines(new File(filePath), StandardCharsets.UTF_8).stream()) {
      stream.forEach(s -> contentBuilder.append(s).append("\n"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return contentBuilder.toString();
  }
}
