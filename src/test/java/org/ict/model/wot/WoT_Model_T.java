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
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ict.gson.GsonUtils;
import org.ict.gson.utils.AdapterFactory;
import org.ict.model.jsonld.context.Context;
import org.ict.model.wot.constant.Op;
import org.ict.model.wot.core.ActionAffordance;
import org.ict.model.wot.core.PropertyAffordance;
import org.ict.model.wot.core.Thing;
import org.ict.model.wot.dataschema.BooleanSchema;
import org.ict.model.wot.dataschema.NumberSchema;
import org.ict.model.wot.hypermedia.Form;
import org.ict.model.wot.security.BasicSecurityScheme;
import org.ict.model.wot.security.SecurityScheme;
import com.google.gson.Gson;

/**
 * @author F. Kohlmorgen
 */
public class WoT_Model_T {
  private static Logger log = LogManager.getFormatterLogger(WoT_Model_T.class);
  public static void main(String[] args) {
    try {
      Context[] contexts = new Context[] {
          Context.builder().prefix(null).namespace("https://www.w3.org/2019/wot/td/v1").build(),
          Context.builder().prefix("schema").namespace("http://schema.org/").build(),
          Context.builder().prefix("iot").namespace("http://iotschema.org/").build()};

      URI thingId = URI.create("https://ikt-systems.de:443/MyLamp1");
      String title = "My Lamp 1";
      Map<String, SecurityScheme> secDef = new HashMap<>();
      secDef.put("basic_sc", BasicSecurityScheme.builder().scheme(Basic).in(Header).build());
      List<String> security = new ArrayList<>();
      security.add("basic_sc");
      Map<String, PropertyAffordance> properties = new HashMap<>();

      PropertyAffordance level = PropertyAffordance.builder()
          .ds(NumberSchema.builder().minimum(0.0).maximum(100.0).title("CurrentDimmer")
              .description("Dimming value in percentage")
              .atType(Arrays.asList(URI.create("Property"), URI.create("iot:CurrentDimmer")))
              .unit("iot:Percent").build())
          .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.readproperty))
              .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b42ff"))
              .contentType("application/json").subprotocol("https").build()))
          .build();

      properties.put("level", level);

      Map<String, ActionAffordance> actions = new HashMap<>();
      ActionAffordance turnOn =
          ActionAffordance.builder().atType(Arrays.asList(URI.create("Action")))
              .output(BooleanSchema.builder().title("On").build())
              .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.invokeaction))
                  .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b42ff"))
                  .contentType("application/json").subprotocol("https").build()))
              .build();
      ActionAffordance turnOff =
          ActionAffordance.builder().atType(Arrays.asList(URI.create("Action")))
              .output(BooleanSchema.builder().title("Off").build())
              .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.invokeaction))
                  .href(URI.create("https://iktsystems.goip.de:443/parameters/106f4202b77b41ff"))
                  .contentType("application/json").subprotocol("https").build()))
              .build();
      actions.put("on", turnOn);
      actions.put("off", turnOff);
      Thing t = Thing.builder().contexts(contexts).id(thingId).title(title)
          .description("A web connected color light")
          .atType(Arrays.asList(URI.create("Thing"), URI.create("iot:Actuator")))
          .securityDefinitions(secDef).security(security).properties(properties).actions(actions)
          .build();

      // log.info(t);

      Gson gson = AdapterFactory.getGsonWithDefaultWotTypeAdapters();
      String jsonThing = gson.toJson(t);
      log.info(jsonThing);

    } catch (Throwable t) {
      t.printStackTrace();
    }
  }
}
