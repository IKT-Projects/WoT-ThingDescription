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
package org.ict.model.dao.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.ict.model.dao.Dao;
import org.ict.model.wot.core.Thing;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/**
 * @author F. Kohlmorgen
 */
public class ThingDao implements Dao<Thing> {
	private static final Logger log = LogManager.getFormatterLogger(ThingDao.class);
	private Repository db;
	private ValueFactory factory;
	private Gson gson;

	public ThingDao(Repository repo) {
		this.db = repo;
		this.factory = SimpleValueFactory.getInstance();
		this.gson = new GsonBuilder().create();
	}

	@Override
	public Optional<Thing> get(Resource id) {
		log.info("Get for id '%s'!", id);
		Model model = new TreeModel();
		// Open a connection to the database
		try (RepositoryConnection conn = db.getConnection()) {
			// let's get our data from the database
			try (RepositoryResult<Statement> result = conn.getStatements(id, null, null, true, (Resource) null);) {
				while (result.hasNext()) {
					model.add(result.next());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Rio.write(model, baos, RDFFormat.JSONLD);
		Object jsonObject = null;
		try {
			jsonObject = JsonUtils.fromInputStream(new ByteArrayInputStream(baos.toByteArray()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JsonLdOptions options = new JsonLdOptions();
		Map<?, ?> context = new HashMap<>();
		// Customise options...
		// Call whichever JSONLD function you want! (e.g. compact)
		Object compact = JsonLdProcessor.compact(jsonObject, context, options);
		try {
			log.info(JsonUtils.toPrettyString(compact));
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<Thing> getAll() {
		log.info("Get all!");
		// create a repo connection and get all thing
		Model model = new TreeModel();
		// Open a connection to the database
		try (RepositoryConnection conn = db.getConnection()) {
			// let's get our data from the database
			try (RepositoryResult<Statement> result = conn.getStatements(null, null, null, false, (Resource) null);) {
				while (result.hasNext()) {
					model.add(result.next());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Rio.write(model, baos, RDFFormat.JSONLD);
		Object jsonObject = null;
		try {
			jsonObject = JsonUtils.fromInputStream(new ByteArrayInputStream(baos.toByteArray()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JsonLdOptions options = new JsonLdOptions();
		Map<?, ?> context = new HashMap<>();
		// Customise options...
		// Call whichever JSONLD function you want! (e.g. compact)
		Object compact = JsonLdProcessor.compact(jsonObject, context, options);
		try {
			log.info(JsonUtils.toPrettyString(compact));
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void save(Thing t) {
		log.info("Save!");
		// save the given thing in the RDF repository
		try (RepositoryConnection conn = db.getConnection()) {
			String s = gson.toJson(t);
			log.info(s);
			InputStream is = new ByteArrayInputStream(s.getBytes());
			conn.add(is, null, RDFFormat.JSONLD, (Resource) null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Thing t, String[] params) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Thing t) {
		// TODO Auto-generated method stub

	}

}
