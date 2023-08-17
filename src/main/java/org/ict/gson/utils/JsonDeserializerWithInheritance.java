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
package org.ict.gson.utils;

import java.lang.reflect.Type;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ict.model.wot.security.APIKeySecurityScheme;
import org.ict.model.wot.security.BasicSecurityScheme;
import org.ict.model.wot.security.BearerSecurityScheme;
import org.ict.model.wot.security.CertSecurityScheme;
import org.ict.model.wot.security.DigestSecurityScheme;
import org.ict.model.wot.security.NoSecurityScheme;
import org.ict.model.wot.security.OAuth2SecurityScheme;
import org.ict.model.wot.security.PSKSecurityScheme;
import org.ict.model.wot.security.PoPSecurityScheme;
import org.ict.model.wot.security.PublicSecurityScheme;
import org.ict.model.wot.security.SecurityScheme;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
/**
 * @author F. Kohlmorgen
 */
public class JsonDeserializerWithInheritance<T> implements JsonDeserializer<T>, JsonSerializer<T> {
  private static Logger LOG = LogManager.getFormatterLogger(JsonDeserializerWithInheritance.class);

  @Override
  public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    LOG.trace("JSON: %s\n", json);
    LOG.trace("Type: %s\n", typeOfT.toString());
    LOG.trace("Context: %s\n", context.toString());


    JsonObject jsonObject = json.getAsJsonObject();
    JsonElement classNamePrimitive = (JsonElement) jsonObject.get("instanceOf");
    LOG.trace(classNamePrimitive);

    String className = classNamePrimitive.getAsString();

    Class<?> clazz;
    try {
      clazz = Class.forName(className);
    } catch (ClassNotFoundException e) {
      throw new JsonParseException(e.getMessage());
    }
    return context.deserialize(jsonObject, clazz);
  }

  @Override
  public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
    if (!(src instanceof SecurityScheme)) {
      return context.serialize(src);
    }
    SecurityScheme s = (SecurityScheme) src;
    JsonObject json = new JsonObject();
    json.addProperty("scheme", s.getScheme());
    json.addProperty("instanceOf", s.getInstance());
    if (src instanceof BasicSecurityScheme) {
      BasicSecurityScheme basic = (BasicSecurityScheme) src;
      json.addProperty("in", basic.getIn());
      json.addProperty("name", basic.getName());
    } else if (src instanceof APIKeySecurityScheme) {
      APIKeySecurityScheme apikey = (APIKeySecurityScheme) src;
      json.addProperty("in", apikey.getIn());
      json.addProperty("name", apikey.getName());
    } else if (src instanceof BearerSecurityScheme) {
      BearerSecurityScheme bearer = (BearerSecurityScheme) src;
      json.addProperty("in", bearer.getIn());
      json.addProperty("name", bearer.getName());
      json.addProperty("alg", bearer.getAlg());
      json.addProperty("format", bearer.getFormat());
      json.addProperty("authorization", bearer.getAuthorization().toString());
    } else if (src instanceof CertSecurityScheme) {
      CertSecurityScheme cert = (CertSecurityScheme) src;
      json.addProperty("identity", cert.getIdentity());
    } else if (src instanceof DigestSecurityScheme) {
      DigestSecurityScheme digest = (DigestSecurityScheme) src;
      json.addProperty("in", digest.getIn());
      json.addProperty("name", digest.getName());
      json.addProperty("qop", digest.getQop());
    } else if (src instanceof NoSecurityScheme) {
      // no special fields -> just do nothing
    } else if (src instanceof OAuth2SecurityScheme) {
      OAuth2SecurityScheme oauth = (OAuth2SecurityScheme) src;
      json.addProperty("authorization", oauth.getAuthorization().toString());
      json.addProperty("token", oauth.getToken().toString());
      json.addProperty("refresh", oauth.getRefresh().toString());
      JsonArray scopes = new JsonArray();
      oauth.getScopes().forEach(scopes::add);
      json.add("scopes", scopes);
      json.addProperty("flow", oauth.getFlow());
    } else if (src instanceof PoPSecurityScheme) {
      PoPSecurityScheme pop = (PoPSecurityScheme) src;
      json.addProperty("in", pop.getIn());
      json.addProperty("name", pop.getName());
      json.addProperty("alg", pop.getAlg());
      json.addProperty("format", pop.getFormat());
      json.addProperty("authorization", pop.getAuthorization().toString());
    } else if (src instanceof PSKSecurityScheme) {
      PSKSecurityScheme psk = (PSKSecurityScheme) src;
      json.addProperty("identity", psk.getIdentity());
    } else if (src instanceof PublicSecurityScheme) {
      PublicSecurityScheme pss = (PublicSecurityScheme) src;
      json.addProperty("identity", pss.getIdentity());
    } else {
      throw new IllegalArgumentException(
          String.format("Security Scheme '%s' not implemented!", s.getInstance()));
    }
    return json;
  }
}
