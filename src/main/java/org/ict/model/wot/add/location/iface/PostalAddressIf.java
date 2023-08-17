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
package org.ict.model.wot.add.location.iface;

import org.cyberborean.rdfbeans.annotations.RDF;
import org.cyberborean.rdfbeans.annotations.RDFBean;

/**
 * @author F. Kohlmorgen
 */
@RDFBean("https://schema.org/PostalAddress")
public interface PostalAddressIf {
	@RDF("https://schema.org/addressCountry")
	String getAddressCountry();

	@RDF("https://schema.org/addressLocality")
	String getAddressLocality();

	@RDF("https://schema.org/addressRegion")
	String getAddressRegion();

	@RDF("https://schema.org/postOfficeBoxNumber")
	String getPostOfficeBoxNumber();

	@RDF("https://schema.org/postalCode")
	String getPostalCode();

	@RDF("https://schema.org/streetAddress")
	String getStreetAddress();
}
