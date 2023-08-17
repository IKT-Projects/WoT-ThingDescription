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
package org.ict.model.wot.add.location;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.ict.model.wot.add.location.iface.BuildingPartIf;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * @author F. Kohlmorgen
 */
@Data
@Builder
public class BuildingPart implements BuildingPartIf {
  @NonNull
  private URI id;
  @SerializedName("@type")
  private List<String> atType;

  @NonNull
  private String title;
  private String description;

  private List<BuildingPart> parts;

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

  public String getId() {
    return id.toString();
  }

  public void setId(String id) {
    this.id = URI.create(id);
  }
}
