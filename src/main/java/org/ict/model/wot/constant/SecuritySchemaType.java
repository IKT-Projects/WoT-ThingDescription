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
package org.ict.model.wot.constant;

/**
 * @author F. Kohlmorgen
 */
public enum SecuritySchemaType {
	Nosec("nosec"), Basic("basic"), Cert("cert"), Digest("digest"), Bearer("bearer"), Pop("pop"), Psk("psk"),
	Public("public"), Oauth2("oauth2"), Apikey("apikey");

	private String type;

	private SecuritySchemaType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
