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
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ict.gson.utils.AdapterFactory;
import org.ict.model.jsonld.context.Context;
import org.ict.model.wot.add.hardware.Hardware;
import org.ict.model.wot.add.hardware.HardwareRef;
import org.ict.model.wot.add.location.BuildingPart;
import org.ict.model.wot.add.location.Elevation;
import org.ict.model.wot.add.location.GeoCoordinates;
import org.ict.model.wot.add.location.Location;
import org.ict.model.wot.add.location.LocationRef;
import org.ict.model.wot.constant.Op;
import org.ict.model.wot.core.PropertyAffordance;
import org.ict.model.wot.core.Thing;
import org.ict.model.wot.core.VersionInfo;
import org.ict.model.wot.dataschema.BooleanSchema;
import org.ict.model.wot.hypermedia.Form;
import org.ict.model.wot.hypermedia.Link;
import org.ict.model.wot.security.BasicSecurityScheme;
import org.ict.model.wot.security.SecurityScheme;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * @author F. Kohlmorgen
 */
public class WoT_SAREF_T {
  private static Logger log = LogManager.getFormatterLogger(WoT_SAREF_T.class);
  public static void main(String... args) {
    try {
      Gson gson = AdapterFactory.getGsonWithDefaultWotTypeAdapters(true);

      Thing light = createLightThing("https://example.com:443/iot/saref/LightExmple1");

      // create IDs for locations and hardware
      String hwid = UUID.randomUUID().toString();
      String locid = UUID.randomUUID().toString();
      String local = UUID.randomUUID().toString();

      generateHardware(gson, hwid);
      light = setHardwareRef(light, hwid);

      generateLocation(gson, locid, local);
      light = setLocationRef(light, locid, local);

      String json = gson.toJson(light);
      log.info(json);

      Thing l = gson.fromJson(json, Thing.class);
      log.info(l);

      String json2 = gson.toJson(l);
      log.info(json.equals(json2));



    } catch (Throwable t) {
      t.printStackTrace();
    }

  }

  private static Thing setLocationRef(Thing light, String locid, String local) throws MalformedURLException {
    light.setLocation(LocationRef.builder()
        .base(Form.builder().op(Arrays.asList(Op.readproperty))
            .href(URI.create("https://example.com:443/location/" + locid))
            .contentType("application/json").subprotocol("https").build())
        .href(Form.builder().op(Arrays.asList(Op.readproperty))
            .href(URI.create("https://example.com:443/location/" + local))
            .contentType("application/json").subprotocol("https").build())
        .build());
    return light;
  }

  private static void generateLocation(Gson gson, String locid, String local) {
    log.info("------------------------- Location -------------------------");
    List<BuildingPart> rooms = new ArrayList<>();
    BuildingPart room514 = BuildingPart.builder().id(URI.create("/location/" + local))
        .atType(Arrays.asList("knx:BuildingPart", "knx:Room")).title("A 514")
        .description("Bestes Labor aller zeiten").build();
    BuildingPart room516 = BuildingPart.builder().id(URI.create("/location/" + UUID.randomUUID()))
        .atType(Arrays.asList("knx:BuildingPart", "knx:Room", "knx:MeetingRoom")).title("A 516")
        .description("Lecker Kaffee").build();
    BuildingPart room512 = BuildingPart.builder().id(URI.create("/location/" + UUID.randomUUID()))
        .atType(Arrays.asList("knx:BuildingPart", "knx:Room")).title("A 512").description("Studis")
        .build();

    rooms.add(room514);
    rooms.add(room516);
    rooms.add(room512);

    List<BuildingPart> floors = new ArrayList<>();
    BuildingPart etage5 = BuildingPart.builder().id(URI.create("/location/" + UUID.randomUUID()))
        .title("Etage 5").description("Hier wohnt das IKT :D").build();
    etage5.setParts(rooms);
    floors.add(etage5);

    Location loc =
        Location.builder().id(URI.create("/location/" + locid))
            .atType(Arrays.asList("knx:Building", "knx:Campus")).title("FH Dortmund")
            .description("Gebauede der Fachhochschule Dortmund")
            .geo(GeoCoordinates.builder()
                .elevation(Elevation.builder().elevation(116).unit("m").build())
                .latitude(51.4999153f).longitude(7.4296393f).build())
            .parts(floors).build();
    String location = gson.toJson(loc);
    log.info(location);
    log.info("------------------------------------------------------------");
  }

  private static Thing setHardwareRef(Thing light, String hwid) throws MalformedURLException {
    light.setHardware(HardwareRef.builder()
        .href(Form.builder().op(Arrays.asList(Op.readproperty))
            .href(URI.create("https://example.com:443/hardware/" + hwid))
            .contentType("application/json").subprotocol("https").build())
        .build());
    return light;
  }

  private static void generateHardware(Gson gson, String hwid) throws MalformedURLException {
    log.info("------------------------- Hardware -------------------------");
    Hardware hw = Hardware.builder().id(URI.create("id:d34a4c6e-d5a1-4d64-b6dc-a4f7d4565899"))
        .location(LocationRef.builder()
            .base(Form.builder().op(Arrays.asList(Op.readproperty))
                .href(URI.create("https://example.com:443/location/Fh-Dortmund"))
                .contentType("application/json").subprotocol("https").build())
            .href(Form.builder().op(Arrays.asList(Op.readproperty))
                .href(URI.create("/floor/5/room/A524")).contentType("application/json")
                .subprotocol("https").build())
            .build())
        .deviceImg(URI.create("https://www.hager.de/ecatimages/pdf/TXE530.jpg"))
        .deviceName("Hager TXE531").deviceType("Weatherstation").EAN("3250616046016")
        .manufacturer("Hager")
        .manufacturerLink(Link.builder().href(URI.create("https://www.hager.de/")).build())
        .serialNumber("436589063894534").productId("TXE531")
        .externalInformation(Link.builder().href(URI.create(
            "https://www.hager.de/knx-gebaeudesystemtechnik/knx-easy/sensoren-wetterstationen/txe531/929503.htm"))
            .build())
        .hardwareVersion("v1.03").softwareVersion("v2.3")
        .atType(Arrays.asList("http://elite.polito.it/ontologies/eupont.owl#WeatherStation"))
        .build();

    String hwJson = gson.toJson(hw);
    log.info(hwJson);
    log.info("------------------------------------------------------------");
  }

  private static <T extends List<String>> JsonArray createArray(T t) {
    JsonArray ops = new JsonArray(t.size());

    for (String e : t) {
      ops.add(e);
    }
    return ops;
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

    Map<String, JsonElement> additionalProp = new HashMap<>();
    additionalProp.put("@type", new JsonPrimitive("saref:GetCommand"));
    PropertyAffordance switchStatus = PropertyAffordance.builder()
        .ds(BooleanSchema.builder().title("SwitchStatus")
            .description("Current value of On/Off status")
            .atType(Arrays.asList(URI.create("saref:OnOffFunction"))).readOnly(true).build())
        .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.readproperty))
            .href(URI.create("https://example.com:443/parameters/106f4202b77b4211"))
            .contentType("application/json").subprotocol("https")
            .additionalProperties(additionalProp).build()))
        .build();

    properties.put("status", switchStatus);
    Map<String, String> descriptions = new HashMap<>();
    descriptions.put("en", "A web connected dimmable light");
    descriptions.put("de", "Ein mit den Internet verbundenes dimmbares Licht");

    Map<String, String> titles = new HashMap<>();
    titles.put("en", "My Light 1");
    titles.put("de", "Meine 1. Lampe");

    return Thing.builder().contexts(contexts).atId(thingId).id(thingId)
        .atType(Arrays.asList(URI.create("saref:Actuator"), URI.create("saref:LightSwitch")))
        .title(title).titles(titles).descriptions(descriptions)
        .version(VersionInfo.builder().instance("0.1").build()).created(new Date())
        .modified(new Date())
        .support(URI.create("https://www.fh-dortmund.de/de/fb/10/ikt/index.php"))
        .base(URI.create("https://example.com:443"))
        .atType(Arrays.asList(URI.create("iot:Actuator"))).securityDefinitions(secDef)
        .security(security).properties(properties).build();
  }
}
