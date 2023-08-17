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

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ict.gson.GsonUtils;
import org.ict.model.jsonld.context.Context;
import org.ict.model.wot.core.SystemProperty;
import org.ict.model.wot.dataschema.DataSchema;
import org.ict.model.wot.hypermedia.AdditionalProperty;
import org.ict.model.wot.hypermedia.ExpectedResponse;
import org.ict.model.wot.hypermedia.Form;
import org.ict.model.wot.security.SecurityScheme;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
/**
 * @author F. Kohlmorgen
 */
public class AdapterFactory {
  private static Logger LOG = LogManager.getFormatterLogger(AdapterFactory.class);

  public static <T> JsonDeserializerWithInheritance<T> getDeserializerWithInheritance(
      Class<T> clazz) {
    return new JsonDeserializerWithInheritance<T>();
  }

  public static GsonBuilder getBuilderWithDefaultWotTypeAdapters() {
    return new Gson().newBuilder().registerTypeAdapterFactory(numberAdapterFactory())
        .registerTypeAdapter(Context[].class, GsonUtils.getContextSerializer())
        .registerTypeAdapter(Context[].class, GsonUtils.getContextDeserializer())
        .registerTypeAdapter(Map.class, getMapDeserializer())
        .registerTypeAdapter(Form.class, getFormSerializer())
        .registerTypeAdapter(Number.class, getNumberDeserializer())
        .registerTypeAdapter(Form.class, getFormDeserializer())
        .registerTypeAdapter(SecurityScheme.class,
            new JsonDeserializerWithInheritance<SecurityScheme>())
        .registerTypeAdapter(DataSchema.class, new JsonDeserializerWithInheritance<DataSchema>())
        .registerTypeAdapter(SystemProperty.class, new JsonDeserializerWithInheritance<SystemProperty>())
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
  }

  //////////////////////////////////////////////////////////////////////////
  // TODO wtf is this?
  private static TypeAdapterFactory numberAdapterFactory() {

    // if (numberToNumberStrategy == ToNumberPolicy.LAZILY_PARSED_NUMBER) {
    // return TypeAdapters.NUMBER_FACTORY;
    // }

    return TypeAdapters.newFactory(Number.class, new TypeAdapter<Number>() {
      @Override
      public Number read(JsonReader in) throws IOException {
        LOG.debug("NumberTypeAdapterFactory: read methode called...");
        JsonToken jsonToken = in.peek();
        switch (jsonToken) {
          case NULL:
            in.nextNull();
            return null;
          case NUMBER:
          case STRING:
            String s = in.nextString();
            if (s.contains(".")) {
              return Double.parseDouble(s);
            }
            return Integer.parseInt(s);
          default:
            throw new JsonSyntaxException("Expecting number, got: " + jsonToken);
        }
      }

      @Override
      public void write(JsonWriter out, Number value) throws IOException {
        out.value(value);
      }
    });
  }
  //////////////////////////////////////////////////////////////////////////

  public static Gson getGsonWithDefaultWotTypeAdapters() {
    return getGsonWithDefaultWotTypeAdapters(false);
  }

  public static Gson getGsonWithDefaultWotTypeAdapters(boolean prettyPrint) {
    if (prettyPrint)
      return getBuilderWithDefaultWotTypeAdapters().setPrettyPrinting().create();
    else
      return getBuilderWithDefaultWotTypeAdapters().create();
  }

  // TODO wtf is this?
  public static JsonDeserializer<Number> getNumberDeserializer() {
    return (src, typeOfSrc, context) -> {

      Number json = 2;


      LOG.debug("Adapter for Number!");

      return json;
    };
  }

  public static JsonSerializer<Form> getFormSerializer() {
    return (src, typeOfSrc, context) -> {
      JsonObject json = new JsonObject();
      json.add("op",
          Optional.ofNullable(src.getOp()).isPresent() ? createArray(src.getOp()) : null);
      json.addProperty("href",
          Optional.ofNullable(src.getHref()).isPresent() ? src.getHref().toString() : null);
      json.addProperty("contentType", src.getContentType());
      json.addProperty("contentCoding", src.getContentCoding());
      json.addProperty("subprotocol", src.getSubprotocol());
      json.add("security",
          Optional.ofNullable(src.getSecurity()).isPresent() ? createArray(src.getSecurity())
              : null);
      json.add("scopes",
          Optional.ofNullable(src.getScopes()).isPresent() ? createArray(src.getScopes()) : null);
      JsonObject o = null;
      String exresContent = Optional.ofNullable(src).map(s -> s.getResponse())
          .map(r -> r.getContentType()).isPresent() ? src.getResponse().getContentType() : null;

      if (exresContent != null) {
        o = new JsonObject();
        o.addProperty("contentType", exresContent);
        json.add("response", o);
      }
      List<AdditionalProperty> properties = src.getAdditionalProperties();
      if (properties != null) {
        Map<String, JsonElement> m = new HashMap<>(properties.size());
        properties.stream().forEach(p -> m.put(p.getPname(), p.getElement()));
        // check for additional properties
        if (m == null || m.size() <= 0) {
          return json;
        }
        m.entrySet().stream().forEach(e -> json.add(e.getKey(), e.getValue()));
      }
      return json;
    };
  }

  public static JsonDeserializer<Form> getFormDeserializer() {
    return (json, typeOfT, context) -> {
      JsonObject jsonObject = json.getAsJsonObject();
      List<String> ops = new ArrayList<>();
      if (jsonObject.get("op").isJsonArray()) {
        jsonObject.get("op").getAsJsonArray().forEach(e -> ops.add(e.getAsString()));
      } else if (jsonObject.get("op").isJsonPrimitive()
          && jsonObject.get("op").getAsJsonPrimitive().isString()) {
        ops.add(jsonObject.get("op").getAsString());
      } else {
        // TODO add logger with warning
      }
      String href = jsonObject.get("href").getAsString();
      String content = jsonObject.get("contentType").getAsString();
      String coding = null;
      Optional<JsonElement> ocoding = Optional.ofNullable(jsonObject.get("contentCoding"));
      if (ocoding.isPresent()) {
        coding = ocoding.get().getAsString();
      }
      String subprotocol = null;
      Optional<JsonElement> osub = Optional.ofNullable(jsonObject.get("subprotocol"));
      if (osub.isPresent()) {
        subprotocol = osub.get().getAsString();
      }
      List<String> secs = null;
      Optional<JsonElement> osecs = Optional.ofNullable(jsonObject.get("security"));
      if (osecs.isPresent()) {
        secs = new ArrayList<>();
        JsonArray jsecs = osecs.get().getAsJsonArray();
        for (int i = 0; i < jsecs.size(); i++) {
          secs.add(jsecs.get(i).getAsString());
        }
      }
      List<String> scopes = null;
      Optional<JsonElement> oscopes = Optional.ofNullable(jsonObject.get("scopes"));
      if (osecs.isPresent()) {
        scopes = new ArrayList<>();
        JsonArray jscopes = oscopes.get().getAsJsonArray();
        for (int i = 0; i < jscopes.size(); i++) {
          scopes.add(jscopes.get(i).getAsString());
        }
      }

      Optional<JsonElement> oresponse = Optional.ofNullable(jsonObject.get("response"));

      Form f = new Form();
      f.setOp(ops);
      f.setHref(URI.create(href));
      f.setContentType(content);
      f.setContentCoding(coding);
      f.setSubprotocol(subprotocol);
      f.setSecurity(secs);
      f.setScopes(scopes);
      if (oresponse.isPresent()) {
        JsonObject o = oresponse.get().getAsJsonObject();
        String exresContent = o.get("contentType").getAsString();
        f.setResponse(ExpectedResponse.builder().contentType(exresContent).build());
      }

      String[] knownKeys = {"op", "href", "contentType", "contentCoding", "subprotocol", "security",
          "scopes", "response"};
      List<String> known = Arrays.asList(knownKeys);

      Map<String, JsonElement> filtered =
          jsonObject.entrySet().stream().filter(e -> !known.contains(e.getKey()))
              .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

      List<AdditionalProperty> l = filtered.entrySet().stream().map(e -> {
        AdditionalProperty p = new AdditionalProperty();
        p.setPname(e.getKey());
        p.setElement(e.getValue());
        return p;
      }).collect(Collectors.toList());
      f.setAdditionalProperties(l);

      return f;
    };
  }

  @SuppressWarnings("unchecked")
  public static <T> JsonDeserializer<Map<String, T>> getMapDeserializer() {
    Gson gson = new Gson().newBuilder().registerTypeAdapterFactory(numberAdapterFactory())
        .registerTypeAdapter(Form.class, getFormSerializer())
        .registerTypeAdapter(Form.class, getFormDeserializer())
        .registerTypeAdapter(SecurityScheme.class,
            new JsonDeserializerWithInheritance<SecurityScheme>())
        .registerTypeAdapter(DataSchema.class, new JsonDeserializerWithInheritance<DataSchema>())
        .registerTypeAdapter(SystemProperty.class, new JsonDeserializerWithInheritance<SystemProperty>())
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

    return (json, typeOfT, context) -> {
      LOG.debug("TypeOfT: %s", typeOfT);
      String type = typeOfT.getTypeName();
      int startIndex = type.indexOf(",") + 2;
      int endIndex = type.length() - 1;
      type = type.substring(startIndex, endIndex);
      Class<?> clazz = String.class;
      try {
        clazz = Class.forName(type);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
      LOG.debug("Map value generic type: '%s'", type);
      JsonObject jsonObj = json.getAsJsonObject();
      Set<String> keys = jsonObj.keySet();
      Map<String, T> map = new HashMap<>(keys.size());
      for (String key : keys) {
        map.put(key, (T) gson.fromJson(jsonObj.get(key).toString(), clazz));
      }
      return map;
    };
  }

  private static <T extends List<String>> JsonArray createArray(T t) {
    JsonArray ops = new JsonArray(t.size());

    for (String e : t) {
      ops.add(e);
    }
    return ops;
  }
}

