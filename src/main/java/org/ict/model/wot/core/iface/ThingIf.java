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
package org.ict.model.wot.core.iface;

import java.net.URI;
import java.util.Date;
import java.util.List;
import org.cyberborean.rdfbeans.annotations.RDF;
import org.cyberborean.rdfbeans.annotations.RDFBean;
import org.cyberborean.rdfbeans.annotations.RDFSubject;
import org.ict.model.jsonld.context.Context;
import org.ict.model.wot.add.hardware.HardwareRef;
import org.ict.model.wot.add.location.LocationRef;
import org.ict.model.wot.core.ActionAffordance;
import org.ict.model.wot.core.EventAffordance;
import org.ict.model.wot.core.MultiLanguage;
import org.ict.model.wot.core.PropertyAffordance;
import org.ict.model.wot.core.VersionInfo;
import org.ict.model.wot.hypermedia.AdditionalProperty;
import org.ict.model.wot.hypermedia.Form;
import org.ict.model.wot.hypermedia.Link;
import org.ict.model.wot.security.SecurityScheme;

/**
 * @author F. Kohlmorgen
 */
@RDFBean("https://www.w3.org/2019/wot/td#Thing")
public interface ThingIf {

  @RDFSubject
  String getAtIdRDF();

  @RDF("https://www.w3.org/2019/wot/td#hasContexts")
  Context[] getContexts();

  @RDF("https://www.w3.org/2019/wot/td#id")
  String getIdRDF();

  @RDF("http://purl.org/dc/terms/title")
  String getTitle();

  @RDF("https://www.w3.org/2019/wot/td#hasSecurityConfiguration")
  List<String> getSecurity();

  @RDF("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")
  List<URI> getAtTypeRDF();

  void setAtTypeRDF(List<URI> types);

  // TODO URLs are not correct
  @RDF("http://purl.org/dc/terms/titles")
  List<MultiLanguage> getTitlesRDF();

  @RDF("http://purl.org/dc/terms/description")
  String getDescription();
  
  //TODO URLs are not correct
  @RDF("http://purl.org/dc/terms/descriptions")
  List<MultiLanguage> getDescriptionsRDF();

  @RDF("https://www.w3.org/2019/wot/td#versionInfo")
  VersionInfo getVersion();

  @RDF("https://www.w3.org/2019/wot/td#created")
  Date getCreated();

  @RDF("https://www.w3.org/2019/wot/td#modified")
  Date getModified();

  @RDF("https://www.w3.org/2019/wot/td#supportContact")
  URI getSupport();

  @RDF("https://www.w3.org/2019/wot/td#baseUri")
  URI getBase();

  @RDF("https://www.w3.org/2019/wot/td#hasPropertyAffordance")
  List<PropertyAffordance> getPropertiesRDF();

  @RDF("https://www.w3.org/2019/wot/td#hasActionAffordance")
  List<ActionAffordance> getActionsRDF();

  @RDF("https://www.w3.org/2019/wot/td#hasEventAffordance")
  List<EventAffordance> getEventsRDF();

  @RDF("https://www.w3.org/2019/wot/td#hasLink")
  List<Link> getLinks();

  @RDF("https://www.w3.org/2019/wot/td#hasForm")
  List<Form> getForms();

  @RDF("https://www.w3.org/2019/wot/td#securityDefinitions")
  List<SecurityScheme> getSecurityDefinitionsRDF();

  @RDF("https://www.ict.org/common#hasLocation")
  LocationRef getLocation();

  @RDF("https://www.ict.org/common#hasHardware")
  HardwareRef getHardware();

  @RDF("https://www.w3.org/2019/wot/td#name")
  String getName();

  @RDF("https://www.w3.org/2019/wot/td#referenceId")
  String getReferenceId();

  @RDF("http://schema.org/manufacturer")
  String getManufacturer();
  // @RDF("https://www.w3.org/2019/wot/hypermedia#additionalProperties")
  // List<AdditionalProperty> getAdditionalProperties();
}
