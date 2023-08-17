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
package org.ict.model.wot.core;

import static org.ict.model.wot.constant.InteractionType.EventAffordance;
import java.net.URI;
import java.util.List;
import java.util.Map;
import org.ict.model.wot.core.iface.EventAffordanceIf;
import org.ict.model.wot.dataschema.DataSchema;
import org.ict.model.wot.hypermedia.Form;
import com.google.gson.annotations.Expose;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * @author F. Kohlmorgen
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EventAffordance extends InteractionAffordance implements EventAffordanceIf {
	@Expose
	private transient String name;
	private DataSchema subscription;
	private DataSchema data;
	private DataSchema cancellation;

	public EventAffordance() {
		super(null, null, null, null, null, null, null);
	}
	
	@Builder
	public EventAffordance(@NonNull List<Form> forms, List<URI> atType, String title, List<MultiLanguage> titles,
			String description, List<MultiLanguage> descriptions, Map<String, DataSchema> uriVariables,
			DataSchema subscription, DataSchema data, DataSchema cancellation) {
		super(forms, atType, title, titles, description, descriptions, uriVariables);
		this.subscription = subscription;
		this.data = data;
		this.cancellation = cancellation;
	}

	@Override
	public String getType() {
		return EventAffordance.type();
	}

	
	
}
