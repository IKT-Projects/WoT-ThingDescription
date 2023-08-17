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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.ict.model.jsonld.context.Context;
import org.ict.model.wot.add.hardware.HardwareRef;
import org.ict.model.wot.add.location.LocationRef;
import org.ict.model.wot.core.iface.ThingIf;
import org.ict.model.wot.hypermedia.AdditionalProperty;
import org.ict.model.wot.hypermedia.Form;
import org.ict.model.wot.hypermedia.Link;
import org.ict.model.wot.security.SecurityScheme;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author F. Kohlmorgen
 */
@Data
@EqualsAndHashCode
@SuperBuilder
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor
@NoArgsConstructor
public class Thing implements ThingIf {
  // mandatory
  @NonNull
  @SerializedName("@context")
  private Context[] contexts;
  private transient URI atId;
  @NonNull
  private URI id;
  @NonNull
  private String title;
  @NonNull
  private List<String> security;

  // optional
  @SerializedName("@type")
  private List<URI> atType;
  private Map<String, String> titles;
  private String description;
  private Map<String, String> descriptions;
  private VersionInfo version;
  private Date created;
  private Date modified;
  private URI support;
  private URI base;
  private Map<String, PropertyAffordance> properties;
  private Map<String, ActionAffordance> actions;
  private Map<String, EventAffordance> events;
  private List<Link> links;
  private List<Form> forms;
  private Map<String, SecurityScheme> securityDefinitions;

  // added for SENSE project
  private LocationRef location;
  private HardwareRef hardware;

  // added for connectd
  private String name;
  private String referenceId;
  @SerializedName("schema:manufacturer")
  private String manufacturer;
  // for unknown properties(TODO not working)
  private Map<String, Object> additionalProperties;

  public String getAtIdRDF() {
    if (atId == null) {
      return null;
    }
    return atId.toString();
  }

  public void setAtIdRDF(String id) {
    this.atId = URI.create(id);
  }

  public String getIdRDF() {
    return id.toString();
  }

  public void setIdRDF(String id) {
    this.id = URI.create(id);
  }

  public List<PropertyAffordance> getPropertiesRDF() {
    if (this.properties == null) {
      return null;
    }
    return this.properties.entrySet().stream().map(e -> {
      PropertyAffordance p = e.getValue();
      p.setName(e.getKey());
      return p;
    }).collect(Collectors.toList());
  }

  public void setPropertiesRDF(List<PropertyAffordance> properties) {
    Map<String, PropertyAffordance> m = new HashMap<>(properties.size());
    properties.stream().forEach(p -> m.put(p.getName(), p));
    this.properties = m;
  }

  public List<ActionAffordance> getActionsRDF() {
    if (this.actions == null) {
      return null;
    }
    return this.actions.entrySet().stream().map(e -> {
      ActionAffordance p = e.getValue();
      p.setName(e.getKey());
      return p;
    }).collect(Collectors.toList());
  }

  public void setActionsRDF(List<ActionAffordance> actions) {
    Map<String, ActionAffordance> m = new HashMap<>(actions.size());
    actions.stream().forEach(p -> m.put(p.getName(), p));
    this.actions = m;
  }

  public List<EventAffordance> getEventsRDF() {
    if (this.events == null) {
      return null;
    }
    return this.events.entrySet().stream().map(e -> {
      EventAffordance p = e.getValue();
      p.setName(e.getKey());
      return p;
    }).collect(Collectors.toList());
  }

  public void setEventsRDF(List<EventAffordance> events) {
    Map<String, EventAffordance> m = new HashMap<>(events.size());
    events.stream().forEach(p -> m.put(p.getName(), p));
    this.events = m;
  }

  public List<MultiLanguage> getDescriptionsRDF() {
    if (this.descriptions == null) {
      return null;
    }
    return this.descriptions.entrySet().stream().map(e -> {
      MultiLanguage ml = MultiLanguage.builder().lang(e.getKey()).text(e.getValue()).build();
      return ml;
    }).collect(Collectors.toList());
  }

  public void setDescriptionsRDF(List<MultiLanguage> ml) {
    Map<String, String> m = new HashMap<>(ml.size());
    ml.stream().forEach(v -> m.put(v.getLang(), v.getText()));
    this.descriptions = m;
  }

  public List<MultiLanguage> getTitlesRDF() {
    if (this.titles == null) {
      return null;
    }
    return this.titles.entrySet().stream().map(e -> {
      MultiLanguage ml = MultiLanguage.builder().lang(e.getKey()).text(e.getValue()).build();
      return ml;
    }).collect(Collectors.toList());
  }

  public void setTitlesRDF(List<MultiLanguage> ml) {
    Map<String, String> m = new HashMap<>(ml.size());
    ml.stream().forEach(v -> m.put(v.getLang(), v.getText()));
    this.titles = m;
  }

  // @TODO Implement the security schemes. Fix bug (missing head)
  public List<SecurityScheme> getSecurityDefinitionsRDF() {
    if (this.securityDefinitions == null) {
      return null;
    }
    return this.securityDefinitions.entrySet().stream().map(e -> {
      SecurityScheme s = e.getValue();
      s.setPname(e.getKey());
      return s;
    }).collect(Collectors.toList());
  }

  public void setSecurityDefinitionsRDF(List<SecurityScheme> s) {
    Map<String, SecurityScheme> m = new HashMap<>(s.size());
    s.stream().forEach(v -> m.put(v.getPname(), v));
    this.securityDefinitions = m;
  }

  /**
   * RDF Bean sets RDF type from the RDF Bean annotation. We use at type (also RDF type) as
   * additional type annotations and don't want the RDFBean annotations in the atType List. We can
   * detect the RDFBean type by class checking. The class is always URI. So every URI gets filtered.
   * 
   * @param types
   */
  public void setAtTypeRDF(List<URI> types) {
    List<URI> filtered =
        types.stream().filter(u -> !u.equals(URI.create("https://www.w3.org/2019/wot/td#Thing")))
            .collect(Collectors.toList());
    if (filtered.isEmpty()) {
      this.atType = null;
    } else {
      this.atType = filtered;
    }
  }

  public List<URI> getAtTypeRDF() {
    return atType;
  }

  @Override
  public String toString() {
    return "Thing [context=" + Arrays.toString(contexts) + ", atId=" + atId + ", id=" + id
        + ", title=" + title + ", security=" + security + ", atType=" + atType + ", titles="
        + titles + ", description=" + description + ", descriptions=" + descriptions + ", version="
        + version + ", created=" + created + ", modified=" + modified + ", support=" + support
        + ", base=" + base + ", properties=" + properties + ", actions=" + actions + ", events="
        + events + ", links=" + links + ", forms=" + forms + ", securityDefinitions="
        + securityDefinitions + ", location=" + location + ", hardware=" + hardware + "]";
  }
}
