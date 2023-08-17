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
package org.ict.model.wot.security;

import java.net.URI;
import java.util.List;
import java.util.Map;
import org.ict.model.wot.constant.Alg;
import org.ict.model.wot.constant.Format;
import org.ict.model.wot.constant.In;
import org.ict.model.wot.constant.SecuritySchemaType;
import org.ict.model.wot.security.iface.BearerSecuritySchemeIf;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author F. Kohlmorgen
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BearerSecurityScheme extends SecurityScheme implements BearerSecuritySchemeIf {
  private URI authorization;
  private String alg = Alg.ES256.getType();
  private String format = Format.JWT.getType();
  private String in = In.Header.getType();
  private String name;

  public BearerSecurityScheme() {
    super(null, BearerSecurityScheme.class.getName(), SecuritySchemaType.Bearer.getType(), null,
        null, null, null);
  }

  @Builder
  public BearerSecurityScheme(SecuritySchemaType scheme, List<String> atType, String description,
      Map<String, String> descriptions, URI proxy, URI authorization, Alg alg, Format format, In in,
      String name) {
    super(null, BearerSecurityScheme.class.getName(), scheme.getType(), atType, description,
        descriptions, proxy);
    this.authorization = authorization;
    this.alg = alg.getType();
    this.format = format.getType();
    this.in = in.getType();
    this.name = name;
  }
}
