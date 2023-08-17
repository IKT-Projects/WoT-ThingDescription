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
package org.ict.model.wot.hypermedia;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.ict.model.wot.constant.Op;
import org.ict.model.wot.hypermedia.iface.FormIf;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author F. Kohlmorgen
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Form implements FormIf {
  // mandatory
  @NonNull
  private List<Op> op;
  @NonNull
  private URI href;
  @NonNull
  @Builder.Default  
  private String contentType = "application/json";

  // optional
  private String contentCoding;
  private String subprotocol;
  private List<String> security;
  private List<String> scopes;
  private ExpectedResponse response;

  @SerializedName("@type")
  private List<String> atType;

  private Map<String, JsonElement> additionalProperties;

  public List<String> getOp() {
    return this.op.stream().map(e -> e.name()).collect(Collectors.toList());
  }

  public void setOp(List<String> op) {
    this.op = op.stream().map(e -> Op.valueOf(e)).collect(Collectors.toList());
  }

  /**
   * RDF Bean sets RDF type from the RDF Bean annotation. We use at type (also RDF type) as
   * additional type annotations and don't want the RDFBean annotations in the atType List. We can
   * detect the RDFBean type by class checking. The class is always URI. So every URI gets filtered.
   * 
   * @param types
   */
  public void setAtType(List<String> types) {
    List<String> newTypes = new ArrayList<>(types.size() - 1);
    Object[] ot = types.toArray(new Object[0]);
    for (int i = 0; i < ot.length; i++) {
      Object o = ot[i];
      if (o instanceof String) {
        newTypes.add((String) o);
      }
    }
    this.atType = newTypes;
  }

  public List<AdditionalProperty> getAdditionalProperties() {
    if (this.additionalProperties == null || this.additionalProperties.isEmpty()) {
      return null;
    }
    return this.additionalProperties.entrySet().stream().map(e -> {
      AdditionalProperty p = new AdditionalProperty();
      p.setElement(e.getValue());
      p.setPname(e.getKey());
      return p;
    }).collect(Collectors.toList());
  }

  public void setAdditionalProperties(List<AdditionalProperty> properties) {
    Map<String, JsonElement> m = new HashMap<>(properties.size());
    properties.stream().forEach(p -> m.put(p.getPname(), p.getElement()));
    this.additionalProperties = m;
  }
}
