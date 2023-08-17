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

import java.net.URI;
import java.util.List;
import org.ict.model.wot.core.MultiLanguage;
import org.ict.model.wot.dataschema.iface.IntegerSchemaIf;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import static org.ict.model.wot.constant.DataSchemaType.IntegerSchema;

/**
 * @author F. Kohlmorgen
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class IntegerSchema extends DataSchema implements IntegerSchemaIf {
  private Number minimum = null;
  private Number maximum = null;
  private transient DataSchema modified;
  @SerializedName("schema:dateModified")
  private Modified jsonModified;

  public IntegerSchema() {
    super(null, null, IntegerSchema.class.getName(), null, null, null, null, null, null, null, null,
        null, null, null, null, null);
  }

  @Builder
  public IntegerSchema(String id, List<URI> atType, String title, List<MultiLanguage> titles,
      String description, List<MultiLanguage> descriptions, Object constant, String unit,
      List<DataSchema> oneOf, List<Object> enumeration, boolean readOnly, boolean writeOnly,
      String format, Integer minimum, Integer maximum, DataSchema modified) {
    super(id, null, IntegerSchema.class.getName(), atType, title, titles, description, descriptions,
        IntegerSchema.type(), constant, unit, oneOf, enumeration, readOnly, writeOnly, format);
    this.minimum = minimum;
    this.maximum = maximum;
    this.modified = modified;
    this.jsonModified = modified != null ? new Modified(modified.getId()) : null;
  }
}
