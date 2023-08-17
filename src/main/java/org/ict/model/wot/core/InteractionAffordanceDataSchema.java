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
package org.ict.model.wot.core;

import java.net.URI;
import java.util.List;
import java.util.Map;
import org.ict.model.wot.core.iface.InteractionAffordanceIf;
import org.ict.model.wot.dataschema.DataSchema;
import org.ict.model.wot.hypermedia.Form;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * @author F. Kohlmorgen
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InteractionAffordanceDataSchema extends DataSchema implements InteractionAffordanceIf {
  // mandatory
  @NonNull
  private List<Form> forms;
  // optional
  private Map<String, DataSchema> uriVariables;

  public InteractionAffordanceDataSchema() {
    super(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
        null);
  }

  public InteractionAffordanceDataSchema(List<URI> atType, String instance, String title,
      List<MultiLanguage> titles, String description, List<MultiLanguage> descriptions, String type,
      Object constant, String unit, List<DataSchema> oneOf, List<Object> enumeration,
      Boolean readOnly, Boolean writeOnly, String format, List<Form> forms,
      Map<String, DataSchema> uriVariables) {
    super(null, null, instance, atType, title, titles, description, descriptions, type, constant,
        unit, oneOf, enumeration, readOnly, writeOnly, format);
    this.forms = forms;
    this.uriVariables = uriVariables;
  }
}
