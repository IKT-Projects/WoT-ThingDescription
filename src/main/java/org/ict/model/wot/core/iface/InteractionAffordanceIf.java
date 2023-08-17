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
import java.util.List;
import java.util.Map;
import org.cyberborean.rdfbeans.annotations.RDF;
import org.cyberborean.rdfbeans.annotations.RDFBean;
import org.ict.model.wot.core.MultiLanguage;
import org.ict.model.wot.dataschema.DataSchema;
import org.ict.model.wot.hypermedia.Form;

/**
 * @author F. Kohlmorgen
 */
@RDFBean("https://www.w3.org/2019/wot/td#InteractionAffordance")
public interface InteractionAffordanceIf {
  // mandatory
  @RDF("https://www.w3.org/2019/wot/td#hasForm")
  List<Form> getForms();

  // optional
  @RDF("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")
  List<URI> getAtTypeRDF();

  @RDF("http://purl.org/dc/terms/title")
  String getTitle();

  // TODO fix URLs here
  @RDF("http://purl.org/dc/terms/titles")
  List<MultiLanguage> getTitles();

  @RDF("http://purl.org/dc/terms/description")
  String getDescription();

  // TODO fix URLs here
  @RDF("http://purl.org/dc/terms/descriptions")
  List<MultiLanguage> getDescriptions();

  @RDF("https://www.w3.org/2019/wot/td#hasUriTemplateSchema")
  Map<String, DataSchema> getUriVariables();
}
