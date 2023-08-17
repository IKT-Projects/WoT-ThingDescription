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
package org.ict.model.wot.security;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.ict.model.wot.core.MultiLanguage;
import org.ict.model.wot.security.iface.SecuritySchemaIf;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author F. Kohlmorgen
 */
@Data
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor
public class SecurityScheme implements SecuritySchemaIf {
  @Expose
  private transient String pname;
  @SerializedName("instanceOf")
  private String instance;
  // mandatory
  @NonNull
  private String scheme;

  // optional
  @SerializedName("@type")
  private List<String> atType;
  private String description;
  private Map<String, String> descriptions;
  private URI proxy;

  public String getProxy() {
    if (this.proxy == null) {
      return null;
    }
    return this.proxy.toString();
  }

  public void setProxy(String proxy) {
    this.proxy = URI.create(proxy);
  }

  public List<MultiLanguage> getDescriptions() {
    if (this.descriptions == null) {
      return new ArrayList<MultiLanguage>();
    }
    return this.descriptions.entrySet().stream().map(e -> {
      MultiLanguage ml = MultiLanguage.builder().lang(e.getKey()).text(e.getValue()).build();
      return ml;
    }).collect(Collectors.toList());
  }

  public void setDescriptions(List<MultiLanguage> ml) {
    Map<String, String> m = new HashMap<>(ml.size());
    ml.stream().forEach(v -> m.put(v.getLang(), v.getText()));
    this.descriptions = m;
  }

  /**
   * RDF Bean sets RDF type from the RDF Bean annotation. We use at type (also RDF type) as
   * additional type annotations and don't want the RDFBean annotations in the atType List. We can
   * detect the RDFBean type by class checking. The class is always URI. So every URI gets filtered.
   * 
   * @param types
   */
  public void setAtType(List<String> types) {
    if(types.size() <= 1) {
      this.atType = null;
      return;
    }
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
}
