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

import java.util.List;
import org.cyberborean.rdfbeans.annotations.RDF;
import org.cyberborean.rdfbeans.annotations.RDFBean;
import org.ict.model.wot.dataschema.DataSchema;
import org.ict.model.wot.dataschema.Modified;

/**
 * @author F. Kohlmorgen
 */
@RDFBean("https://www.w3.org/2019/wot/json-schema#ObjectSchema")
public interface ObjectSchemaIf {
  @RDF("https://www.w3.org/2019/wot/json-schema#properties")
  List<DataSchema> getProperties();

  @RDF("https://www.w3.org/2019/wot/json-schema#required")
  List<String> getRequired();
  
  @RDF("https://schema.org/dateModified")
  DataSchema getModified();
  
  @RDF("https://schema.org/jsonDateModified")
  Modified getJsonModified();
}
