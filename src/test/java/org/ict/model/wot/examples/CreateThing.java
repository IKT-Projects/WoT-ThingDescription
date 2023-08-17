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
package org.ict.model.wot.examples;

import static org.ict.model.wot.constant.In.Header;
import static org.ict.model.wot.constant.SecuritySchemaType.Basic;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.cyberborean.rdfbeans.RDFBeanManager;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.ict.gson.utils.AdapterFactory;
import org.ict.model.jsonld.context.Context;
import org.ict.model.ssn.system.properties.Frequency;
import org.ict.model.wot.constant.Op;
import org.ict.model.wot.core.ActionAffordance;
import org.ict.model.wot.core.PropertyAffordance;
import org.ict.model.wot.core.SystemCapability;
import org.ict.model.wot.core.Thing;
import org.ict.model.wot.dataschema.BooleanSchema;
import org.ict.model.wot.dataschema.NumberSchema;
import org.ict.model.wot.dataschema.ObjectSchema;
import org.ict.model.wot.hypermedia.Form;
import org.ict.model.wot.security.BasicSecurityScheme;
import org.ict.model.wot.security.SecurityScheme;
import com.google.gson.Gson;

/**
 * @author F. Kohlmorgen
 */
public class CreateThing {

  public static void main(String[] args) throws MalformedURLException {
    // create a thing identifier
    URI thingId = URI.create("https://example.com:443/MyLamp1");
    // give the thing a title
    String title = "My Lamp 1";

    // create the context
    Context[] contexts = createContext();
    
    // define a security schema
    Map<String, SecurityScheme> secDef = createSecurityScheme();
    // add the name of the security definition to a list of available security options
    List<String> security = new ArrayList<>();
    security.add("basic_sc");

    // create some properties
    PropertyAffordance level = PropertyAffordance.builder()
        .ds(NumberSchema.builder().minimum(0.0).maximum(100.0).title("CurrentDimmer")
            .description("Dimming value in percentage")
            .atType(Arrays.asList(URI.create("https://www.w3.org/2019/wot/td#Property"),
                URI.create("http://iotschema.org/CurrentDimmer")))
            .unit("http://iotschema.org/Percent").build())
        .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.readproperty))
            .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b42ff"))
            .contentType("application/json").subprotocol("https").build()))
        .build();
    
    // add the properties to the properties map
    Map<String, PropertyAffordance> properties = new HashMap<>();
    properties.put("level", level);

    // create some actions
    ActionAffordance turnOn = ActionAffordance.builder()
        .atType(Arrays.asList(URI.create("https://www.w3.org/2019/wot/td#Action")))
        .output(BooleanSchema.builder().title("On").build())
        .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.invokeaction))
            .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b42ff"))
            .contentType("application/json").subprotocol("https").build()))
        //.systemCapability(SystemCapability.builder()
        //    .systemProperty(
        //        Frequency.builder().value(5.0f).unitCode("qudt-unit-1-1:Second").build())
        //    .build())
        .build();

    ActionAffordance turnOff = ActionAffordance.builder()
        .atType(Arrays.asList(URI.create("https://www.w3.org/2019/wot/td#Action")))
        .output(BooleanSchema.builder().title("Off").build())
        .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.invokeaction))
            .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b41ff"))
            .contentType("application/json").subprotocol("https").build()))
        //.systemCapability(SystemCapability.builder()
        //    .systemProperty(
        //        Frequency.builder().value(5.0f).unitCode("qudt-unit-1-1:Second").build())
        //    .build())
        .build();

    // add the actions to the actions map
    Map<String, ActionAffordance> actions = new HashMap<>();
    actions.put("on", turnOn);
    actions.put("off", turnOff);

    // now we have everything to create a simple thing description
    Thing t = Thing.builder().contexts(contexts).id(thingId).title(title)
        .description("A web connected color light")
        .atType(Arrays.asList(URI.create("http://ontology.tno.nl/saref#Device"),
            URI.create("http://ontology.tno.nl/saref#Light"),
            URI.create("http://iotschema.org/Actuator")))
        .securityDefinitions(secDef).security(security).properties(properties).actions(actions)
        .build();
 
    // get a preconfigured Gson instance with pretty printing option set to true
    Gson gson = AdapterFactory.getGsonWithDefaultWotTypeAdapters(true);
    // serialize the thing to json-ld
    String jsonThing = gson.toJson(t);
    // print the serialized version of the thing description
    System.out.println(jsonThing);

    //Repository db = new HTTPRepository("http://10.3.0.75:28008/rdf4j-server/", "1");
    Repository db = new HTTPRepository("http://localhost:8080/rdf4j-server/", "1");
    db.init();

    try (RepositoryConnection con = db.getConnection()) {
      contextToRDF4jContext(con, contexts);
      RDFBeanManager manager = new RDFBeanManager(con);
      Resource motionRes = manager.add(t);

      //Thing newTing = manager.get("https://zuse.icas.fh-dortmund.de:2443/ict-gw/v1/things/304ca384bb754548", Thing.class);
      Thing newTing = manager.get(motionRes, Thing.class);
      // serialize the thing to json-ld
      String newjsonThing = gson.toJson(newTing);
      // print the serialized version of the thing description
      System.out.println(newjsonThing);
    } catch (Throwable th) {
      th.printStackTrace();
    }

  }

  private static Map<String, SecurityScheme> createSecurityScheme() {
    Map<String, SecurityScheme> secDef = new HashMap<>();
    secDef.put("basic_sc", BasicSecurityScheme.builder().scheme(Basic).in(Header).build());
    return secDef;
  }

  private static Context[] createContext() {
    Context[] contexts = new Context[] {
        Context.builder().prefix(null).namespace("https://www.w3.org/2019/wot/td/v1").build(),
        Context.builder().prefix("iot").namespace("http://iotschema.org/").build(),
        Context.builder().prefix("saref").namespace("http://ontology.tno.nl/saref#").build(),
        Context.builder().prefix("knx").namespace("http://knx.org/ontology/").build(),
        Context.builder().prefix("sosa").namespace("http://www.w3.org/ns/sosa/").build(),
        Context.builder().prefix("ssn").namespace("http://www.w3.org/ns/ssn/").build(),
        Context.builder().prefix("ssn-system").namespace("http://www.w3.org/ns/ssn/systems/")
            .build(),
        Context.builder().prefix("schema").namespace("http://schema.org/").build(),
        Context.builder().prefix("qudt-unit-1-1").namespace("http://qudt.org/1.1/vocab/unit#")
            .build()};
    return contexts;
  }

  private static void contextToRDF4jContext(RepositoryConnection con, Context[] contexts) {
    for (int i = 0; i < contexts.length; i++) {
      Context c = contexts[i];
      String prefix = c.getPrefix() != null ? c.getPrefix() : "";
      con.setNamespace(prefix, c.getNamespace());
    }
  }
}
