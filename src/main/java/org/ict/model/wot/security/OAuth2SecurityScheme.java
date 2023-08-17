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
import org.ict.model.wot.constant.Flow;
import org.ict.model.wot.constant.SecuritySchemaType;
import org.ict.model.wot.security.iface.OAuth2SecuritySchemeIf;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author F. Kohlmorgen
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OAuth2SecurityScheme extends SecurityScheme implements OAuth2SecuritySchemeIf {
  private URI authorization;
  private URI token;
  private URI refresh;
  private List<String> scopes;
  private String flow = Flow.Implicit.getType();

  public OAuth2SecurityScheme() {
    super(null, OAuth2SecurityScheme.class.getName(), SecuritySchemaType.Oauth2.getType(), null,
        null, null, null);
  }

  @Builder
  public OAuth2SecurityScheme(SecuritySchemaType scheme, List<String> atType, String description,
      Map<String, String> descriptions, URI proxy, URI authorization, URI token, URI refresh,
      List<String> scopes, Flow flow) {
    super(null, OAuth2SecurityScheme.class.getName(), scheme.getType(), atType, description,
        descriptions, proxy);
    this.authorization = authorization;
    this.token = token;
    this.refresh = refresh;
    this.scopes = scopes;
    this.flow = flow.getType();
  }
}
