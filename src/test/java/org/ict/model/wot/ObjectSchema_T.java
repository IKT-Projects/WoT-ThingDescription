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

import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ict.gson.utils.AdapterFactory;
import org.ict.model.wot.constant.Op;
import org.ict.model.wot.core.ActionAffordance;
import org.ict.model.wot.core.InteractionAffordance;
import org.ict.model.wot.dataschema.DataSchema;
import org.ict.model.wot.dataschema.IntegerSchema;
import org.ict.model.wot.dataschema.ObjectSchema;
import org.ict.model.wot.dataschema.StringSchema;
import org.ict.model.wot.hypermedia.Form;
import com.github.jsonldjava.shaded.com.google.common.base.Optional;
import com.google.gson.Gson;

/**
 * @author F. Kohlmorgen
 */
public class ObjectSchema_T {
  private static Logger log = LogManager.getFormatterLogger(ObjectSchema_T.class);
  public static void main(String[] args) {
    try {
      Gson gson = AdapterFactory.getGsonWithDefaultWotTypeAdapters();
      IntegerSchema in = IntegerSchema.builder().title("In")
          .description("The id (integer) of a enum type").build();
      Map<String, DataSchema> properties = new HashMap<>();

      properties.put("id", IntegerSchema.builder().minimum(0).maximum(0).build());
      properties.put("name", StringSchema.builder().title("valuename").build());

      ObjectSchema out = ObjectSchema.builder().title("enumvalue")
          .description("The enum value and its mapped id").properties(properties).build();
      Optional<InteractionAffordance> action =
          Optional.of(ActionAffordance.builder().title("enumvalue").input(in).output(out)
              .forms(Arrays.asList(Form.builder().op(Arrays.asList(Op.invokeaction))
                  .href(URI.create("https://test.com")).contentType("application/json")
                  .subprotocol("https").build()))
              .build());

      log.info(gson.toJson(action.get()));
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }
}
