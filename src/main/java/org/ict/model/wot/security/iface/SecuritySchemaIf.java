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
package org.ict.model.wot.security.iface;

import java.util.List;
import org.cyberborean.rdfbeans.annotations.RDF;
import org.cyberborean.rdfbeans.annotations.RDFBean;
import org.ict.model.wot.core.MultiLanguage;

/**
 * @author F. Kohlmorgen
 */
// this URL is not present in the TD doc
@RDFBean("https://www.w3.org/2019/wot/security#SecurityScheme")
public interface SecuritySchemaIf {
  @RDF("https://www.ict.org/common#propertyName")
  String getPname();

  @RDF("https://www.ict.org/common#intsanceOf")
  String getInstance();

  // TODO this should be RDF type ?
  @RDF("https://www.w3.org/2019/wot/td#scheme")
  String getScheme();

  @RDF("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")
  List<String> getAtType();

  @RDF("http://purl.org/dc/terms/description")
  String getDescription();

  // TODO fix URLs here
  @RDF("http://purl.org/dc/terms/descriptions")
  List<MultiLanguage> getDescriptions();

  @RDF("https://www.w3.org/2019/wot/security#proxy")
  String getProxy();
}
