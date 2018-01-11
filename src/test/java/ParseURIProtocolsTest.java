import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.Objects;

import org.apache.jena.iri.IRIFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.ResourceFactory.Interface;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDFS;
import org.junit.Test;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class ParseURIProtocolsTest {

	// https://www.w3.org/TR/app-uri/
	// Must match src/main/resources/app*z
	private static final String APP_BASE = "app://2dee5b0a-6100-470a-a67f-1399518cb470/";
	// http with a phony (but valid) hostname
	// Must match src/main/resources/http*
	private static final String HTTP_BASE = "http://2dee5b0a-6100-470a-a67f-1399518cb470/";
	// file with a phony (but valid) hostname
	// Must match src/main/resources/file*
	private static final String FILE_BASE = "file://2dee5b0a-6100-470a-a67f-1399518cb470/";
	// A made-up protocol that contains "-"
	private static final String X_MADEUP_BASE = "x-madeup://2dee5b0a-6100-470a-a67f-1399518cb470/";

	static boolean DEBUG = Boolean.getBoolean("debug");

	Model m = ModelFactory.createDefaultModel();
	IRIFactory iriFactory = IRIFactory.iriImplementation();
	Interface resourceFactory = ResourceFactory.getInstance();

	private InputStream load(String f) {
		return Objects.requireNonNull(getClass().getResourceAsStream(f));
	}

	private void dumpModelWithMessage(String msg) {
		if (!DEBUG) {
			return;
		}
		System.out.println(msg);
		RDFDataMgr.write(System.out, m, Lang.NTRIPLES);
	}

	private Statement expectedStatement(String base) {
		Resource s = resourceFactory.createResource(base + "nested/foo.txt");	
		Property p = RDFS.seeAlso;
		RDFNode o = resourceFactory.createResource(base + "bar.txt");
		return resourceFactory.createStatement(s, p, o);
	}

	@Test
	public void appTTL() throws Exception {
		RDFDataMgr.read(m, load("/app.ttl"), Lang.TURTLE);
		dumpModelWithMessage("appTTL");
		assertTrue("Can't find statement", m.contains(expectedStatement(APP_BASE)));		
	}

	@Test
	public void appRDF() throws Exception {
		RDFDataMgr.read(m, load("/app.rdf"), Lang.RDFXML);
		dumpModelWithMessage("appRDF");
		assertTrue("Can't find statement", m.contains(expectedStatement(APP_BASE)));
	}

	@Test
	public void appNT() throws Exception {
		RDFDataMgr.read(m, load("/app.nt"), Lang.NTRIPLES);
		dumpModelWithMessage("appNT");
		assertTrue("Can't find statement", m.contains(expectedStatement(APP_BASE)));
	}

	@Test
	public void appBaseTTL() throws Exception {
		RDFDataMgr.read(m, load("/app-base.ttl"), Lang.TURTLE);
		dumpModelWithMessage("appBaseTTL");
		assertTrue("Can't find statement", m.contains(expectedStatement(APP_BASE)));
	}

	@Test
	public void appBaseRDF() throws Exception {
		RDFDataMgr.read(m, load("/app-base.rdf"), Lang.RDFXML);
		dumpModelWithMessage("appBaseRDF");
		assertTrue("Can't find statement", m.contains(expectedStatement(APP_BASE)));
	}

	@Test
	public void appRelTTL() throws Exception {
		RDFDataMgr.read(m, load("/rel.ttl"), APP_BASE + "nested/", Lang.TURTLE);
		dumpModelWithMessage("appRelTTL");
		assertTrue("Can't find statement", m.contains(expectedStatement(APP_BASE)));
	}

	@Test
	public void appRelRDF() throws Exception {
		RDFDataMgr.read(m, load("/rel.rdf"), APP_BASE + "nested/", Lang.RDFXML);
		dumpModelWithMessage("appRelRDF");
		assertTrue("Can't find statement", m.contains(expectedStatement(APP_BASE)));
	}

	@Test
	public void fileTTL() throws Exception {
		RDFDataMgr.read(m, load("/file.ttl"), Lang.TURTLE);
		dumpModelWithMessage("fileTTL");
		assertTrue("Can't find statement", m.contains(expectedStatement(FILE_BASE)));
	}

	@Test
	public void fileRDF() throws Exception {
		RDFDataMgr.read(m, load("/file.rdf"), Lang.RDFXML);
		dumpModelWithMessage("fileRDF");
		assertTrue("Can't find statement", m.contains(expectedStatement(FILE_BASE)));
	}

	@Test
	public void fileNT() throws Exception {
		RDFDataMgr.read(m, load("/file.nt"), Lang.NTRIPLES);
		dumpModelWithMessage("fileNT");
		assertTrue("Can't find statement", m.contains(expectedStatement(FILE_BASE)));
	}

	@Test
	public void fileRelTTL() throws Exception {
		RDFDataMgr.read(m, load("/rel.ttl"), FILE_BASE + "nested/", Lang.TURTLE);
		dumpModelWithMessage("fileRelTTL");
		assertTrue("Can't find statement", m.contains(expectedStatement(FILE_BASE)));
	}

	@Test
	public void fileRelRDF() throws Exception {
		RDFDataMgr.read(m, load("/rel.rdf"), FILE_BASE + "nested/", Lang.RDFXML);
		dumpModelWithMessage("fileRelRDF");
		assertTrue("Can't find statement", m.contains(expectedStatement(FILE_BASE)));
	}
	
	@Test
	public void httpTTL() throws Exception {
		RDFDataMgr.read(m, load("/http.ttl"), Lang.TURTLE);
		dumpModelWithMessage("httpTTL");
		assertTrue("Can't find statement", m.contains(expectedStatement(HTTP_BASE)));
	}

	@Test
	public void httpRDF() throws Exception {
		RDFDataMgr.read(m, load("/http.rdf"), Lang.RDFXML);
		dumpModelWithMessage("httpRDF");
		assertTrue("Can't find statement", m.contains(expectedStatement(HTTP_BASE)));
	}

	@Test
	public void httpNT() throws Exception {
		RDFDataMgr.read(m, load("/http.nt"), Lang.NTRIPLES);
		dumpModelWithMessage("httpNT");
		assertTrue("Can't find statement", m.contains(expectedStatement(HTTP_BASE)));
	}

	@Test
	public void httpRelTTL() throws Exception {
		RDFDataMgr.read(m, load("/rel.ttl"), HTTP_BASE + "nested/", Lang.TURTLE);
		dumpModelWithMessage("httpRelTTL");
		assertTrue("Can't find statement", m.contains(expectedStatement(HTTP_BASE)));
	}

	@Test
	public void httpRelRDF() throws Exception {
		RDFDataMgr.read(m, load("/rel.rdf"), HTTP_BASE + "nested/", Lang.RDFXML);
		dumpModelWithMessage("httpRelRDF");
		assertTrue("Can't find statement", m.contains(expectedStatement(HTTP_BASE)));
	}
	

	@Test
	public void xMadeupRDF() throws Exception {
		RDFDataMgr.read(m, load("/x-madeup.rdf"), Lang.RDFXML);
		dumpModelWithMessage("xMadeupRDF");
		assertTrue("Can't find statement", m.contains(expectedStatement(X_MADEUP_BASE)));
	}

	@Test
	public void xMadeupNT() throws Exception {
		RDFDataMgr.read(m, load("/x-madeup.nt"), Lang.NTRIPLES);
		dumpModelWithMessage("xMadeupNT");
		assertTrue("Can't find statement", m.contains(expectedStatement(X_MADEUP_BASE)));
	}

	@Test
	public void xMadeupRelTTL() throws Exception {
		RDFDataMgr.read(m, load("/rel.ttl"), X_MADEUP_BASE + "nested/", Lang.TURTLE);
		dumpModelWithMessage("xMadeupRelTTL");
		assertTrue("Can't find statement", m.contains(expectedStatement(X_MADEUP_BASE)));
	}

	@Test
	public void xMadeupRelRDF() throws Exception {
		RDFDataMgr.read(m, load("/rel.rdf"), X_MADEUP_BASE + "nested/", Lang.RDFXML);
		dumpModelWithMessage("xMadeupRelRDF");
		assertTrue("Can't find statement", m.contains(expectedStatement(X_MADEUP_BASE)));
	}
	
	//
}
