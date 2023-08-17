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
package org.ict.model.wot.add.hardware.iface;

import java.net.URI;
import java.util.List;

import org.cyberborean.rdfbeans.annotations.RDF;
import org.cyberborean.rdfbeans.annotations.RDFBean;
import org.cyberborean.rdfbeans.annotations.RDFSubject;
import org.ict.model.wot.hypermedia.Link;

/**
 * @author F. Kohlmorgen
 */
@RDFBean("https://www.ict.org/hardware#Hardware")
public interface HardwareIf {
	@RDFSubject
	String getId();

	@RDF("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")
	List<String> getAtType();

	@RDF("https://schema.org/image")
	URI getDeviceImg();

	@RDF("https://www.ict.org/hardware#ean")
	String getEAN();

	@RDF("https://schema.org/name")
	String getDeviceName();

	@RDF("https://schema.org/manufacturer")
	String getManufacturer();

	@RDF("https://www.w3.org/2019/wot/td#hasLink")
	Link getManufacturerLink();

	@RDF("https://schema.org/serialNumber")
	String getSerialNumber();

	@RDF("https://schema.org/productID")
	String getProductId();

	@RDF("https://www.ict.org/hardware#deviceType")
	String getDeviceType();

	@RDF("https://schema.org/softwareVersion")
	String getSoftwareVersion();

	@RDF("https://schema.org/version")
	String getHardwareVersion();

	@RDF("https://www.ict.org/hardware#externalInformation")
	Link getExternalInformation();
}
