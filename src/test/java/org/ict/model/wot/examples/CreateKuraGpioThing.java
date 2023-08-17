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
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.ict.gson.utils.AdapterFactory;
import org.ict.model.jsonld.context.Context;
import org.ict.model.wot.constant.Op;
import org.ict.model.wot.core.ActionAffordance;
import org.ict.model.wot.core.PropertyAffordance;
import org.ict.model.wot.core.Thing;
import org.ict.model.wot.dataschema.BooleanSchema;
import org.ict.model.wot.dataschema.DataSchema;
import org.ict.model.wot.dataschema.ObjectSchema;
import org.ict.model.wot.hypermedia.Form;
import org.ict.model.wot.security.BasicSecurityScheme;
import org.ict.model.wot.security.SecurityScheme;
import com.google.gson.Gson;

/**
 * @author F. Kohlmorgen
 */
public class CreateKuraGpioThing {

  public static void main(String[] args) {
    try {
      final String tid = UUID.randomUUID().toString();
      // create a thing identifier
      URI thingId = URI.create("http://localhost:80/thing/" + tid);
      // give the thing a title
      String title = "RPI 1 GPIO";
      // description
      String desc = "A Eclipse Kura based GPIO implementation running on a Raspberry Pi";
      // create the context
      Context[] contexts = createContext();

      // define a security schema
      Map<String, SecurityScheme> secDef = createSecurityScheme();
      // add the name of the security definition to a list of available security options
      List<String> security = new ArrayList<>();
      security.add("basic_sc");

      // create some properties
      String led_prop_name = "led_status";
      PropertyAffordance led_status = PropertyAffordance.builder()
          .ds(BooleanSchema.builder().atType(Arrays.asList(URI.create("iot:SwitchStatus")))
              .description("Staus of the LED (true/false)").build())
          .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.readproperty))
              .href(URI.create("http://localhost:80/thing/" + tid + "/property/" + led_prop_name))
              .contentType("application/json").subprotocol("https").build()))
          .build();

      // add the properties to the properties map
      Map<String, PropertyAffordance> properties = new HashMap<>();
      properties.put(led_prop_name, led_status);

      final Map<String, DataSchema> objMap = new HashMap<>();
      // create boolean data schema
      objMap.put("on_off", BooleanSchema.builder().title("On/Off")
          .description("The state to be set (true/false)").build());
      final ObjectSchema os =
          ObjectSchema.builder().title("Input properties").properties(objMap).build();

      // create some actions
      ActionAffordance turnOn = ActionAffordance.builder()
          .atType(Arrays.asList(URI.create("Action"))).input(os)
          .output(BooleanSchema.builder().title("On/Off")
              .atType(Arrays.asList(URI.create("iot:TurnOn"), URI.create("iot:TurnOff"))).build())
          .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.invokeaction))
              .href(URI.create(
                  "http://localhost:80/thing/" + tid + "/property/" + led_prop_name + "/on"))
              .contentType("application/json").subprotocol("https").build()))
          .build();

      // add the actions to the actions map
      Map<String, ActionAffordance> actions = new HashMap<>();
      actions.put("on_off", turnOn);

      // now we have everything to create a simple thing description
      Thing t = Thing.builder().contexts(contexts).id(thingId).title(title).description(desc)
          .atType(Arrays.asList(URI.create("Thing"), URI.create("iot:Actuator"),
              URI.create("iot:BinarySwitchControl"), URI.create("iot:SwitchStatus")))
          .securityDefinitions(secDef).security(security).properties(properties).actions(actions)
          .build();

      // get a preconfigured Gson instance with pretty printing option set to true
      Gson gson = AdapterFactory.getGsonWithDefaultWotTypeAdapters(true);
      // serialize the thing to json-ld
      String jsonThing = gson.toJson(t);
      // print the serialized version of the thing description
      System.out.println(jsonThing);
    } catch (Throwable t) {
      t.printStackTrace();
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
        Context.builder().prefix("iot").namespace("http://iotschema.org/").build()};
    return contexts;
  }

}
