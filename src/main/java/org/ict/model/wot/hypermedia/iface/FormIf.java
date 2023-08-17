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
package org.ict.model.wot.hypermedia.iface;

import java.net.URI;
import java.util.List;
import org.cyberborean.rdfbeans.annotations.RDF;
import org.cyberborean.rdfbeans.annotations.RDFBean;
import org.ict.model.wot.hypermedia.AdditionalProperty;
import org.ict.model.wot.hypermedia.ExpectedResponse;

/**
 * @author F. Kohlmorgen
 */
@RDFBean("https://www.w3.org/2019/wot/hypermedia#Form")
public interface FormIf {
  // mandatory
  @RDF("https://www.w3.org/2019/wot/hypermedia#hasOperationType")
  List<String> getOp();

  void setOp(List<String> ops);

  @RDF("https://www.w3.org/2019/wot/hypermedia#hasTarget")
  URI getHref();

  @RDF("https://www.w3.org/2019/wot/hypermedia#forContentType")
  String getContentType();

  // optional
  @RDF("https://www.w3.org/2019/wot/hypermedia#forContentCoding")
  String getContentCoding();

  @RDF("https://www.w3.org/2019/wot/hypermedia#forSubProtocol")
  String getSubprotocol();

  @RDF("https://www.w3.org/2019/wot/td#hasSecurityConfiguration")
  List<String> getSecurity();

  @RDF("https://www.w3.org/2019/wot/security#scopes")
  List<String> getScopes();

  @RDF("https://www.w3.org/2019/wot/hypermedia#returns")
  ExpectedResponse getResponse();

  // TODO this URL is not existing
  @RDF("https://www.w3.org/2019/wot/hypermedia#additionalProperties")
  List<AdditionalProperty> getAdditionalProperties();

}
