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
package org.ict.model.wot.add.location.iface;

import java.util.List;
import org.cyberborean.rdfbeans.annotations.RDF;
import org.cyberborean.rdfbeans.annotations.RDFBean;
import org.cyberborean.rdfbeans.annotations.RDFSubject;
import org.ict.model.wot.add.location.BuildingPart;
import org.ict.model.wot.add.location.GeoCoordinates;
import org.ict.model.wot.add.location.PostalAddress;

/**
 * @author F. Kohlmorgen
 */
@RDFBean("https://schema.org/Place")
public interface LocationIf {
  @RDFSubject
  String getId();

  @RDF("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")
  List<String> getAtType();

  @RDF("https://www.w3.org/2019/wot/td#title")
  String getTitle();

  @RDF("https://www.w3.org/2019/wot/td#description")
  String getDescription();

  @RDF("https://schema.org/geo")
  GeoCoordinates getGeo();

  @RDF("https://schema.org/address")
  PostalAddress getAddress();

  @RDF("https://www.ict.org/location#hasParts")
  List<BuildingPart> getParts();
}
