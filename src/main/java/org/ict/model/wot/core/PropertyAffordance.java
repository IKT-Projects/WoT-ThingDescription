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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.ict.model.wot.core.iface.PropertyAffordanceIf;
import org.ict.model.wot.dataschema.ArraySchema;
import org.ict.model.wot.dataschema.BooleanSchema;
import org.ict.model.wot.dataschema.DataSchema;
import org.ict.model.wot.dataschema.IntegerSchema;
import org.ict.model.wot.dataschema.Modified;
import org.ict.model.wot.dataschema.NullSchema;
import org.ict.model.wot.dataschema.NumberSchema;
import org.ict.model.wot.dataschema.ObjectSchema;
import org.ict.model.wot.dataschema.StringSchema;
import org.ict.model.wot.dataschema.iface.ArraySchemaIf;
import org.ict.model.wot.dataschema.iface.BooleanSchemaIf;
import org.ict.model.wot.dataschema.iface.IntegerSchemaIf;
import org.ict.model.wot.dataschema.iface.NumberSchemaIf;
import org.ict.model.wot.dataschema.iface.ObjectSchemaIf;
import org.ict.model.wot.hypermedia.Form;
import com.google.gson.annotations.Expose;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author F. Kohlmorgen
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PropertyAffordance extends InteractionAffordanceDataSchema
    implements PropertyAffordanceIf, NumberSchemaIf, IntegerSchemaIf, ArraySchemaIf,
    BooleanSchemaIf, ObjectSchemaIf {
  @Expose
  private transient String name;
  private Boolean observable;
  // added for conectd
  private Boolean writeable;

  // fields from Number and Integer DataSchema
  private Number minimum = null;
  private Number maximum = null;
  private DataSchema modified = null;
  private Modified jsonModified = null;

  private List<DataSchema> items = null;
  private Long minItems = null;
  private Long maxItems = null;

  private Map<String, DataSchema> properties;
  private List<String> required;

  @Builder
  public PropertyAffordance(DataSchema ds, List<Form> forms, Map<String, DataSchema> uriVariables,
      boolean observable) {
    super(ds.getAtType(), ds.getInstance(), ds.getTitle(), ds.getTitles(), ds.getDescription(),
        ds.getDescriptions(), ds.getType(), ds.getConstant(), ds.getUnit(), ds.getOneOf(),
        ds.getEnumeration(), ds.getReadOnly(), ds.getWriteOnly(), ds.getFormat(), forms,
        uriVariables);

    if (ds instanceof NumberSchema) {
      NumberSchema n = (NumberSchema) ds;
      super.setType(n.getType());
      this.minimum = n.getMinimum();
      this.maximum = n.getMaximum();
      this.modified = n.getModified();
      this.jsonModified = modified != null ? new Modified(modified.getId()) : null;
    } else if (ds instanceof BooleanSchema) {
      BooleanSchema b = (BooleanSchema) ds;
      this.modified = b.getModified();
      this.jsonModified = modified != null ? new Modified(modified.getId()) : null;
      super.setType(b.getType());
    } else if (ds instanceof IntegerSchema) {
      IntegerSchema i = (IntegerSchema) ds;
      super.setType(i.getType());
      this.minimum = i.getMinimum();
      this.maximum = i.getMaximum();
      this.modified = i.getModified();
      this.jsonModified = modified != null ? new Modified(modified.getId()) : null;
    } else if (ds instanceof ArraySchema) {
      ArraySchema as = (ArraySchema) ds;
      super.setType(as.getType());
      this.items = as.getItems();
      this.minItems = as.getMinItems();
      this.maxItems = as.getMaxItems();
      this.modified = as.getModified();
      this.jsonModified = modified != null ? new Modified(modified.getId()) : null;
    } else if (ds instanceof StringSchema) {
      StringSchema s = (StringSchema) ds;
      this.modified = s.getModified();
      this.jsonModified = modified != null ? new Modified(modified.getId()) : null;
      super.setType(s.getType());
    } else if (ds instanceof ObjectSchema) {
      ObjectSchema o = (ObjectSchema) ds;
      this.properties = o.getPropertiesMap();
      super.setType(o.getType());
      this.modified = o.getModified();
      this.jsonModified = modified != null ? new Modified(modified.getId()) : null;
      this.required = o.getRequired();
    } else if (ds instanceof NullSchema) {
      NullSchema n = (NullSchema) ds;
      super.setType(n.getType());
      this.modified = n.getModified();
      this.jsonModified = modified != null ? new Modified(modified.getId()) : null;
    } else {
      throw new IllegalArgumentException(
          String.format("Implementation of type '%s' not supported!", ds.getType()));
    }
    this.observable = observable;
  }

  public List<DataSchema> getProperties() {
    if (this.properties == null) {
      return null;
    }
    return this.properties.entrySet().stream().map(e -> {
      DataSchema ds = e.getValue();
      ds.setName(e.getKey());
      return ds;
    }).collect(Collectors.toList());
  }

  public void setProperties(List<DataSchema> properties) {
    Map<String, DataSchema> m = new HashMap<>(properties.size());
    properties.stream().forEach(p -> m.put(p.getName(), p));
    this.properties = m;
  }
}
