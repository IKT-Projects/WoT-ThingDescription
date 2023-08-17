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
package org.ict.model.wot.add.location;

import org.ict.model.wot.add.location.iface.PostalAddressIf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author F. Kohlmorgen
 */
@Data
@Builder
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor
public class PostalAddress implements PostalAddressIf {
	private String addressCountry;
	private String addressLocality;
	private String addressRegion;
	private String postOfficeBoxNumber;
	private String streetAddress;
	private String postalCode;
}
