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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.ict.model.wot.core.MultiLanguage;
import org.ict.model.wot.dataschema.iface.DataSchemaIf;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author F. Kohlmorgen
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
public abstract class DataSchema implements DataSchemaIf {
  private static final List<URI> FILTER_TYPES =
      Arrays.asList(URI.create("https://www.w3.org/2019/wot/json-schema#ArraySchema"),
          URI.create("https://www.w3.org/2019/wot/json-schema#BooleanSchema"),
          URI.create("https://www.w3.org/2019/wot/json-schema#IntegerSchema"),
          URI.create("https://www.w3.org/2019/wot/json-schema#NullSchema"),
          URI.create("https://www.w3.org/2019/wot/json-schema#NumberSchema"),
          URI.create("https://www.w3.org/2019/wot/json-schema#ObjectSchema"),
          URI.create("https://www.w3.org/2019/wot/json-schema#StringSchema"),
          URI.create("https://www.w3.org/2019/wot/td#PropertyAffordance"),
          URI.create("https://www.w3.org/2019/wot/td#ActionAffordance"),
          URI.create("https://www.w3.org/2019/wot/td#EventAffordance"));

  @SerializedName("@id")
  private String id;
  // name is used for RDF serialization of map structures with name as key and a dataschema as value
  @Expose
  private transient String name;
  @SerializedName("instanceOf")
  private String instance;
  @SerializedName("@type")
  private List<URI> atType;
  private String title;
  private List<MultiLanguage> titles;
  private String description;
  private List<MultiLanguage> descriptions;
  @SerializedName("type")
  private String type;
  private Object constant;
  private String unit;
  private List<DataSchema> oneOf;
  private List<Object> enumeration;
  private Boolean readOnly;
  private Boolean writeOnly;
  private String format;

  /**
   * RDF Bean sets RDF type from the RDF Bean annotation. We use at type (also RDF type) as
   * additional type annotations and don't want the RDFBean annotations in the atType List. We can
   * detect the RDFBean type by class checking. The class is always URI. So every URI gets filtered.
   * 
   * @param types
   */
  public void setAtTypeRDF(List<URI> types) {
    List<URI> filtered =
        types.stream().filter(u -> !FILTER_TYPES.contains(u)).collect(Collectors.toList());
    if (filtered.isEmpty()) {
      this.atType = null;
    } else {
      this.atType = filtered;
    }
  }

  public List<URI> getAtTypeRDF() {
    return atType;
  }
}
