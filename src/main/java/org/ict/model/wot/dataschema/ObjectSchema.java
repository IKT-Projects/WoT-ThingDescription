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
package org.ict.model.wot.dataschema;

import static org.ict.model.wot.constant.DataSchemaType.ObjectSchema;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.ict.model.wot.core.MultiLanguage;
import org.ict.model.wot.dataschema.iface.ObjectSchemaIf;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author F. Kohlmorgen
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ObjectSchema extends DataSchema implements ObjectSchemaIf {
  private Map<String, DataSchema> properties;
  private List<String> required;
  private transient DataSchema modified;
  @SerializedName("schema:dateModified")
  private Modified jsonModified;
  
  public ObjectSchema() {
    super(null, null, ObjectSchema.class.getName(), null, null, null, null, null, null, null, null,
        null, null, null, null, null);
  }

  @Builder
  public ObjectSchema(String id, List<URI> atType, String title, List<MultiLanguage> titles,
      String description, List<MultiLanguage> descriptions, Object constant, String unit,
      List<DataSchema> oneOf, List<Object> enumeration, boolean readOnly, boolean writeOnly,
      String format, Map<String, DataSchema> properties, List<String> required, DataSchema modified) {
    super(id, null, ObjectSchema.class.getName(), atType, title, titles, description, descriptions,
        ObjectSchema.type(), constant, unit, oneOf, enumeration, readOnly, writeOnly, format);
    this.properties = properties;
    this.required = required;
    this.modified = modified;
    this.jsonModified = modified != null ? new Modified(modified.getId()) : null;
  }

  public List<DataSchema> getProperties() {
    if (this.properties == null) {
      return null;
    }
    return this.properties.entrySet().stream().map(e -> {
      DataSchema p = e.getValue();
      p.setName(e.getKey());
      return p;
    }).collect(Collectors.toList());
  }

  public void setProperties(List<DataSchema> properties) {
    Map<String, DataSchema> m = new HashMap<>(properties.size());
    properties.stream().forEach(p -> m.put(p.getName(), p));
    this.properties = m;
  }

  public Map<String, DataSchema> getPropertiesMap() {
    return this.properties;
  }
}
