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
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cyberborean.rdfbeans.RDFBeanManager;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.ict.gson.utils.AdapterFactory;
import org.ict.model.jsonld.context.Context;
import org.ict.model.wot.add.hardware.HardwareRef;
import org.ict.model.wot.add.location.LocationRef;
import org.ict.model.wot.constant.Op;
import org.ict.model.wot.core.ActionAffordance;
import org.ict.model.wot.core.EventAffordance;
import org.ict.model.wot.core.PropertyAffordance;
import org.ict.model.wot.core.Thing;
import org.ict.model.wot.core.VersionInfo;
import org.ict.model.wot.dataschema.ArraySchema;
import org.ict.model.wot.dataschema.BooleanSchema;
import org.ict.model.wot.dataschema.DataSchema;
import org.ict.model.wot.dataschema.IntegerSchema;
import org.ict.model.wot.dataschema.StringSchema;
import org.ict.model.wot.hypermedia.Form;
import org.ict.model.wot.security.BasicSecurityScheme;
import org.ict.model.wot.security.SecurityScheme;
import com.google.gson.Gson;

/**
 * @author F. Kohlmorgen
 */
public class RDFBeans_T {
  private static Logger log = LogManager.getFormatterLogger(RDFBeans_T.class);
  public static void main(String[] args) {
    try {
      Gson gson = AdapterFactory.getGsonWithDefaultWotTypeAdapters();
      Repository db = new HTTPRepository("http://localhost:8080/rdf4j-server/", "wot-repo1");
      db.init();

      try (RepositoryConnection con = db.getConnection()) {
        ValueFactory vf = con.getValueFactory();
        con.setNamespace("", "https://www.w3.org/2019/wot/td/v1#");
        con.setNamespace("iot", "http://iotschema.org/");
        con.setNamespace("schema", "http://schema.org/");

        RDFBeanManager manager = new RDFBeanManager(con);

        IRI modec = vf.createIRI("https;//example.com:443/MotionDetector1");
        IRI li = vf.createIRI("https;//example.com:443/HueLight1");
        IRI temp = vf.createIRI("https;//example.com:443/Temperature1");

        Thing motionDetector = createMotionDetectorThing(modec.stringValue());
        Thing hueLight = createLightThing(li.stringValue());
        Thing temperature = createTemperatureThing(temp.stringValue());
        log.info(gson.toJson(motionDetector));

        Resource motionRes = manager.add(motionDetector);
        Resource lightRes = manager.add(hueLight);
        Resource tempRes = manager.add(temperature);

        log.info("Stored resources: '%s', '%s', '%s'\n", motionRes, lightRes, tempRes);
        log.info("Default namespace: '%s'", con.getNamespace(""));

        motionDetector = (Thing) manager.get(motionRes);
        hueLight = manager.get(lightRes, Thing.class);
        temperature = (Thing) manager.get(tempRes);

        log.info(motionDetector);
        log.info("Light: " + hueLight);
        log.info(temperature);


        String jsonMontion = gson.toJson(motionDetector);
        String jsonLight = gson.toJson(hueLight);
        String jsonTemp = gson.toJson(temperature);

        log.info(
            "-------------------------------------------------------------------------------");
        log.info(jsonMontion);
        log.info(
            "-------------------------------------------------------------------------------");
        log.info(jsonLight);
        log.info(
            "-------------------------------------------------------------------------------");
        log.info(jsonTemp);
      }
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  private static Thing createLightThing(String id) throws MalformedURLException {
    Context[] contexts = new Context[] {
        Context.builder().prefix(null).namespace("https://www.w3.org/2019/wot/td/v1").build(),
        Context.builder().prefix("schema").namespace("http://schema.org/").build(),
        Context.builder().prefix("iot").namespace("http://iotschema.org/").build()};

    URI thingId = URI.create(id);
    String title = "Hue Color Light";
    Map<String, SecurityScheme> secDef = new HashMap<>();
    secDef.put("basic_sc", BasicSecurityScheme.builder().scheme(Basic).in(Header).build());
    List<String> security = new ArrayList<>();
    security.add("basic_sc");

    // create properties
    Map<String, PropertyAffordance> properties = new HashMap<>();

    PropertyAffordance switchStatus = PropertyAffordance.builder()
        .ds(BooleanSchema.builder().title("SwitchStatus")
            .description("Current value of On/Off status")
            .atType(Arrays.asList(URI.create("iot:SwitchStatus"))).readOnly(true).build())
        .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.readproperty))
            .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b4211"))
            .contentType("application/json").subprotocol("https").build()))
        .build();

    PropertyAffordance level = PropertyAffordance.builder()
        .ds(IntegerSchema.builder().minimum(0).maximum(100).title("CurrentDimmer")
            .description("Dimming value in percentage")
            .atType(Arrays.asList(URI.create("iot:DimmerData"))).unit("iot:Percent").build())
        .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.readproperty))
            .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b4212"))
            .contentType("application/json").subprotocol("https").build()))
        .build();

    PropertyAffordance ramp = PropertyAffordance.builder()
        .ds(IntegerSchema.builder().minimum(0).maximum(60).title("RampTime")
            .description("Dimming ramp time in seconds")
            .atType(Arrays.asList(URI.create("iot:RampTimeData"))).unit("iot:Second").build())
        .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.writeproperty))
            .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b4213"))
            .contentType("application/json").subprotocol("https").build()))
        .build();

    List<DataSchema> colors = new ArrayList<>();
    IntegerSchema red =
        IntegerSchema.builder().title("Red").description("Red channel of RGB colour")
            .atType(Arrays.asList(URI.create("iot:RColourData"))).minimum(0).maximum(255).build();
    IntegerSchema green =
        IntegerSchema.builder().title("Green").description("Green channel of RGB colour")
            .atType(Arrays.asList(URI.create("iot:GColourData"))).minimum(0).maximum(255).build();
    IntegerSchema blue =
        IntegerSchema.builder().title("Blue").description("Blue channel of RGB colour")
            .atType(Arrays.asList(URI.create("iot:BColourData"))).minimum(0).maximum(255).build();
    colors.add(red);
    colors.add(green);
    colors.add(blue);

    PropertyAffordance colour = PropertyAffordance.builder()
        .ds(ArraySchema.builder().title("CurrentColour").description("Color value in RGB format")
            .atType(Arrays.asList(URI.create("iot:CurrentColour"))).minItems(3L).maxItems(3L)
            .items(colors).build())
        .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.writeproperty))
            .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b4213"))
            .contentType("application/json").subprotocol("https").build()))
        .build();

    properties.put("status", switchStatus);
    properties.put("level", level);
    properties.put("ramp", ramp);
    properties.put("colour", colour);

    // create actions
    Map<String, ActionAffordance> actions = new HashMap<>();
    ActionAffordance toggle = ActionAffordance.builder().title("Toggle action")
        .description("A action that toogles the light")
        .atType(Arrays.asList(URI.create("iot:ToggleAction")))
        .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.invokeaction))
            .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b42ff/toggle"))
            .contentType("application/json").subprotocol("https").build()))
        .build();

    ActionAffordance on = ActionAffordance.builder().title("TurnOn").description("Turn On")
        .atType(Arrays.asList(URI.create("iot:TurnOn")))
        .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.invokeaction))
            .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b42ff/on"))
            .contentType("application/json").subprotocol("https").build()))
        .build();

    ActionAffordance off = ActionAffordance.builder().title("TurnOff").description("Turn Off")
        .atType(Arrays.asList(URI.create("iot:TurnOff")))
        .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.invokeaction))
            .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b42ff/off"))
            .contentType("application/json").subprotocol("https").build()))
        .build();

    ActionAffordance setdim = ActionAffordance.builder().title("SetDimmer")
        .description(
            "Set dimming value in percent. The action takes <ramptime> seconds to complete ")
        .atType(Arrays.asList(URI.create("iot:SetDimmer")))
        .input(IntegerSchema.builder().maximum(0).maximum(100).build())
        .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.invokeaction))
            .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b42ff/dimmer"))
            .contentType("application/json").subprotocol("https").build()))
        .build();

    ActionAffordance setColour = ActionAffordance.builder().title("SetColour")
        .description("Control color value in RGB format")
        .atType(Arrays.asList(URI.create("iot:SetColour")))
        .input(ArraySchema.builder().minItems(3L).maxItems(3L).items(colors).build())
        .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.invokeaction))
            .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b42ff/colour"))
            .contentType("application/json").subprotocol("https").build()))
        .build();

    actions.put("toggle", toggle);
    actions.put("on", on);
    actions.put("off", off);
    actions.put("setdimmer", setdim);
    actions.put("setcolour", setColour);

    Map<String, String> descriptions = new HashMap<>();
    descriptions.put("en", "A web connected dimmable light");
    descriptions.put("de", "Ein mit den Internet verbundenes dimmbares Licht");

    Map<String, String> titles = new HashMap<>();
    titles.put("en", "My Light 1");
    titles.put("de", "Meine 1. Lampe");

    return Thing.builder().contexts(contexts).atId(thingId).id(thingId).title(title).titles(titles)
        .descriptions(descriptions).version(VersionInfo.builder().instance("0.1").build())
        .created(new Date()).modified(new Date())
        .support(URI.create("https://www.fh-dortmund.de/de/fb/10/ikt/index.php"))
        .base(URI.create("https://iktsystems.goip.de:443"))
        .atType(Arrays.asList(URI.create("iot:Actuator"))).securityDefinitions(secDef)
        .security(security).properties(properties).actions(actions).build();
  }

  private static Thing createMotionDetectorThing(String id) throws MalformedURLException {
    Context[] contexts = new Context[] {
        Context.builder().prefix(null).namespace("https://www.w3.org/2019/wot/td/v1").build(),
        Context.builder().prefix("schema").namespace("http://schema.org/").build(),
        Context.builder().prefix("iot").namespace("http://iotschema.org/").build()};

    URI thingId = URI.create(id);
    String title = "Motion Detector";
    Map<String, SecurityScheme> secDef = new HashMap<>();
    secDef.put("basic_sc", BasicSecurityScheme.builder().scheme(Basic).in(Header).build());
    List<String> security = new ArrayList<>();
    security.add("basic_sc");

    // create properties
    Map<String, PropertyAffordance> properties = new HashMap<>();
    PropertyAffordance state = PropertyAffordance.builder()
        .ds(BooleanSchema.builder().title("Montion detected state")
            .description("Presence (motion) sensing state (True=Motion detected, False=No motion)")
            .atType(Arrays.asList(URI.create("iot:StateData"))).readOnly(true).build())
        .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.readproperty))
            .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b42f1"))
            .contentType("application/json").subprotocol("https").build()))
        .build();

    properties.put("state", state);

    // create event
    Map<String, EventAffordance> events = new HashMap<>();
    List<DataSchema> schemas = new ArrayList<>(2);
    schemas.add(BooleanSchema.builder().title("Montion detected state")
        .description("Presence (motion) sensing state (True=Motion detected, False=No motion)")
        .readOnly(true).build());
    schemas.add(StringSchema.builder().title("Motion sensor type")
        .description("The type of the motion sensor").readOnly(true).build());
    EventAffordance motion = EventAffordance.builder().title("Motion detected event")
        .description("Indicates a detected motion")
        .data(ArraySchema.builder().minItems(2L).maxItems(2L).items(schemas).readOnly(true).build())
        .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.subscribeevent))
            .href(URI.create("wss://iktsystems.goip.de:80/parameters/106f4202b77b42f2"))
            .contentType("application/json").subprotocol("wss").build()))
        .build();
    events.put("motion", motion);

    // create location reference
    LocationRef locRef = LocationRef.builder()
        .base(Form.builder().op(Arrays.asList(Op.readproperty))
            .href(URI.create("https://iktsystems.goip.de:443/location/206f5202b77b42f1"))
            .contentType("application/json").subprotocol("https").build())
        .href(Form.builder().op(Arrays.asList(Op.readproperty))
            .href(URI.create("https://iktsystems.goip.de:443/location/47353645990409"))
            .contentType("application/json").subprotocol("https").build())
        .build();

    // create hardware reference
    HardwareRef hwRef = HardwareRef.builder()
        .href(Form.builder().op(Arrays.asList(Op.readproperty))
            .href(URI.create("https://iktsystems.goip.de:443/hardware/113f4202b24b82e3"))
            .contentType("application/json").subprotocol("https").build())
        .build();

    Map<String, String> descriptions = new HashMap<>();
    descriptions.put("en", "A web connected presence/motion sensing device");
    descriptions.put("de", "Ein mit den Internet verbundener Präsens-/Bewegungsmelder");

    Map<String, String> titles = new HashMap<>();
    titles.put("en", title);
    titles.put("de", "Bewegungsmelder");

    return Thing.builder().contexts(contexts).atId(thingId).id(thingId).title(title).titles(titles)
        .descriptions(descriptions).location(locRef).hardware(hwRef)
        .version(VersionInfo.builder().instance("0.1").build()).created(new Date())
        .modified(new Date())
        .support(URI.create("https://www.fh-dortmund.de/de/fb/10/ikt/index.php"))
        .base(URI.create("https://iktsystems.goip.de:443"))
        .atType(Arrays.asList(URI.create("iot:Sensor"))).securityDefinitions(secDef)
        .security(security).properties(properties).events(events).build();
  }

  private static Thing createTemperatureThing(String id) throws MalformedURLException {
    Context[] contexts = new Context[] {
        Context.builder().prefix(null).namespace("https://www.w3.org/2019/wot/td/v1").build(),
        Context.builder().prefix("schema").namespace("http://schema.org/").build(),
        Context.builder().prefix("iot").namespace("http://iotschema.org/").build()};

    URI thingId = URI.create(id);
    String title = "Temperature Sensor";
    Map<String, SecurityScheme> secDef = new HashMap<>();
    secDef.put("basic_sc", BasicSecurityScheme.builder().scheme(Basic).in(Header).build());
    List<String> security = new ArrayList<>();
    security.add("basic_sc");

    // create properties
    Map<String, PropertyAffordance> properties = new HashMap<>();
    PropertyAffordance temp = PropertyAffordance.builder()
        .ds(IntegerSchema.builder().minimum(-30).maximum(60).title("CurrentTemperature")
            .description("Current temperature in °C")
            .atType(Arrays.asList(URI.create("iot:Temperature"), URI.create("iot:TemperatureData")))
            .build())
        .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.readproperty))
            .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b42ff"))
            .contentType("application/json").subprotocol("https").build()))
        .build();

    properties.put("temperature", temp);

    Map<String, String> descriptions = new HashMap<>();
    descriptions.put("en", "A web connected temperature sensor");
    descriptions.put("de", "Ein mit den Internet verbundener Temperatursensor");

    Map<String, String> titles = new HashMap<>();
    titles.put("en", title);
    titles.put("de", "Temperatursensor");
    Thing thing =
        Thing.builder().contexts(contexts).atId(thingId).id(thingId).title(title).titles(titles)
            .descriptions(descriptions).version(VersionInfo.builder().instance("0.1").build())
            .created(new Date()).modified(new Date())
            .support(URI.create("https://www.fh-dortmund.de/de/fb/10/ikt/index.php"))
            .base(URI.create("https://iktsystems.goip.de:443"))
            .atType(Arrays.asList(URI.create("iot:Sensor"))).securityDefinitions(secDef)
            .security(security).properties(properties).build();
    return thing;
  }
 
  @SuppressWarnings("unused")
  private static Thing createThing(String id) throws MalformedURLException {
    Context[] contexts = new Context[] {
        Context.builder().prefix(null)
            .namespace(
                "file:/./home/kohlmorgen/git/WoT-Framework/wot/src/main/resources/td_v1.json")
            .build(),
        Context.builder().prefix("schema").namespace("http://schema.org/").build(),
        Context.builder().prefix("iot").namespace("http://iotschema.org/").build()};

    URI thingId = URI.create(id);
    String title = "My Lamp 1";
    Map<String, SecurityScheme> secDef = new HashMap<>();
    secDef.put("basic_sc", BasicSecurityScheme.builder().scheme(Basic).in(Header).build());
    List<String> security = new ArrayList<>();
    security.add("basic_sc");

    // create properties
    Map<String, PropertyAffordance> properties = new HashMap<>();
    PropertyAffordance level = PropertyAffordance.builder()
        .ds(IntegerSchema.builder().minimum(0).maximum(100).title("CurrentDimmer")
            .description("Dimming value in percentage")
            .atType(Arrays.asList(URI.create("iot:CurrentDimmer"))).unit("iot:Percent").build())
        .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.readproperty))
            .href(URI.create("/parameters/106f4202b77b42ff")).contentType("application/json")
            .subprotocol("https").build()))
        .build();

    properties.put("level", level);

    // create actions
    Map<String, ActionAffordance> actions = new HashMap<>();
    ActionAffordance toggle = ActionAffordance.builder().title("Toggle action")
        .description("A action that toogles the light")
        .atType(Arrays.asList(URI.create("iot:ToggleAction")))
        .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.invokeaction))
            .href(URI.create("href://parameters/106f4202b77b42ff/toggle"))
            .contentType("application/json").subprotocol("https").build()))
        .build();

    actions.put("toggle", toggle);

    Map<String, String> descriptions = new HashMap<>();
    descriptions.put("en", "A web connected dimmable light");
    descriptions.put("de", "Ein mit den Internet verbundenes dimmbares Licht");

    Map<String, String> titles = new HashMap<>();
    titles.put("en", "My Light 1");
    titles.put("de", "Meine 1. Lampe");
    return Thing.builder().contexts(contexts).atId(thingId).id(thingId).title(title).titles(titles)
        .descriptions(descriptions).version(VersionInfo.builder().instance("0.1").build())
        .created(new Date()).modified(new Date())
        .support(URI.create("https://www.fh-dortmund.de/de/fb/10/ikt/index.php"))
        .base(URI.create("https://iktsystems.goip.de:443"))
        .atType(Arrays.asList(URI.create("iot:Actuator"))).securityDefinitions(secDef)
        .security(security).properties(properties).actions(actions).build();
  }
}
