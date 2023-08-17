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
import org.ict.model.wot.constant.In;
import org.ict.model.wot.constant.Qop;
import org.ict.model.wot.constant.SecuritySchemaType;
import org.ict.model.wot.security.iface.DigestSecuritySchemeIf;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author F. Kohlmorgen
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DigestSecurityScheme extends SecurityScheme implements DigestSecuritySchemeIf {
  private String qop = Qop.Auth.getType();
  private String in = In.Header.getType();
  private String name;

  public DigestSecurityScheme() {
    super(null, DigestSecurityScheme.class.getName(), SecuritySchemaType.Digest.getType(), null,
        null, null, null);
  }

  @Builder
  public DigestSecurityScheme(SecuritySchemaType scheme, List<String> atType, String description,
      Map<String, String> descriptions, URI proxy, Qop qop, In in, String name) {
    super(null, DigestSecurityScheme.class.getName(), scheme.getType(), atType, description,
        descriptions, proxy);
    this.qop = qop.getType();
    this.in = in.getType();
    this.name = name;
  }

}
