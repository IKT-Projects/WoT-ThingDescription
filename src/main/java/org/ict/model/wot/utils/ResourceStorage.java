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
package org.ict.model.wot.utils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.ict.model.jsonld.context.Context;

/**
 * @author F. Kohlmorgen
 */
public class ResourceStorage {
  private URI base;
  private URI defaultNamespace;
  private Map<String, URI> contexts = new HashMap<>();
  private Context[] cont;

  public void setContext(Context[] contexts) {
    this.cont = contexts;
    for (int i = 0; i < contexts.length; i++) {
      String prefix = contexts[i].getPrefix();
      String namespace = contexts[i].getNamespace();
      if (prefix == null || prefix.equals("")) {
        this.defaultNamespace = URI.create(namespace);
      } else {
        this.contexts.put(prefix, URI.create(namespace));
      }
    }
  }

  public Context[] getContexts() {
    return this.cont;
  }

  public void setBaseURI(String baseURI) {
    this.base = URI.create(baseURI);
  }

  public URI getBase() {
    return this.base;
  }

  public String getBaseAsString() {
    return getBase().toString();
  }

  public URI getDefaultNamespace() {
    return defaultNamespace;
  }

  public String getDefaultNamespaceAsString() {
    return getDefaultNamespace().toString();
  }

  public URI getNamespace(String prefix) {
    if (contexts.containsKey(prefix)) {
      return contexts.get(prefix);
    } else {
      throw new IllegalArgumentException(String.format("Unknown prefix: %s", prefix));
    }
  }

  public String getNamespaceAsString(String prefix) {
    return getNamespace(prefix).toString();
  }

  public void addNamespace(String prefix, String namespace) {
    contexts.put(prefix, URI.create(namespace));
  }

  public URI createURI(String path) {
    return URI.create(this.base + path);
  }
}
