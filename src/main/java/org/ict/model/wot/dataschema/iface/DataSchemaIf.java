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
package org.ict.model.wot.dataschema.iface;

import java.net.URI;
import java.util.List;
import org.cyberborean.rdfbeans.annotations.RDF;
import org.cyberborean.rdfbeans.annotations.RDFBean;
import org.cyberborean.rdfbeans.annotations.RDFSubject;
import org.ict.model.wot.core.MultiLanguage;
import org.ict.model.wot.dataschema.DataSchema;

/**
 * @author F. Kohlmorgen
 */
@RDFBean("https://www.w3.org/2019/wot/json-schema#DataSchema")
public interface DataSchemaIf {
  @RDFSubject
  String getId();
  
  @RDF("https://www.ict.org/common#propertyName")
  String getName();

  @RDF("https://www.ict.org/common#intsanceOf")
  String getInstance();
  
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

  // TODO has to be RDF type but it collides with atType
  @RDF("https://www.w3.org/2019/wot/json-schema#type")
  String getType();

  @RDF("https://www.w3.org/2019/wot/json-schema#const")
  Object getConstant();

  @RDF("https://www.w3.org/2019/wot/json-schema#unitCode")
  String getUnit();

  @RDF("https://www.w3.org/2019/wot/json-schema#oneOf")
  List<DataSchema> getOneOf();

  @RDF("https://www.w3.org/2019/wot/json-schema#enum")
  List<Object> getEnumeration();

  @RDF("https://www.w3.org/2019/wot/json-schema#readOnly")
  Boolean getReadOnly();

  @RDF("https://www.w3.org/2019/wot/json-schema#writeOnly")
  Boolean getWriteOnly();

  @RDF("https://www.w3.org/2019/wot/json-schema#format")
  String getFormat();
}
