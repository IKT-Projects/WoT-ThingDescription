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
package org.ict.model.wot.examples;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Collectors;
import org.ict.gson.utils.AdapterFactory;
import org.ict.model.wot.core.Thing;
import com.google.gson.Gson;

/**
 * @author F. Kohlmorgen
 */
public class ReadComplexTD {
  public static void main(String[] args) {
    try {
      Gson gson = AdapterFactory.getGsonWithDefaultWotTypeAdapters();
      File f = new File("./src/main/resources/TDwithSystemCapability.json");
      String input = Files.readAllLines(f.toPath(), StandardCharsets.UTF_8).stream()
          .collect(Collectors.joining()).trim();
      Thing thing = gson.fromJson(input, Thing.class);
      System.out.println(thing);
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }
}
