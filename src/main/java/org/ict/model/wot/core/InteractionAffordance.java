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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.ict.model.wot.core.iface.InteractionAffordanceIf;
import org.ict.model.wot.dataschema.DataSchema;
import org.ict.model.wot.hypermedia.Form;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author F. Kohlmorgen
 */
@Data
@AllArgsConstructor
public abstract class InteractionAffordance implements InteractionAffordanceIf {
  private static final List<URI> FILTER_TYPES =
      Arrays.asList(URI.create("https://www.w3.org/2019/wot/td#PropertyAffordance"),
          URI.create("https://www.w3.org/2019/wot/td#ActionAffordance"),
          URI.create("https://www.w3.org/2019/wot/td#EventAffordance"));
  // mandatory
  // @NonNull
  private List<Form> forms;
  // optional
  @SerializedName("@type")
  private List<URI> atType;
  private String title;
  private List<MultiLanguage> titles;
  private String description;
  private List<MultiLanguage> descriptions;
  private Map<String, DataSchema> uriVariables;

  public abstract String getType();

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
