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

public class ParseURISchemeTest {

	// Must match src/main/resources/{scheme}*.[rdf|ttl|nt]

	// Tests for various schemes from https://www.iana.org/assignments
	// according to https://tools.ietf.org/html/rfc3986#section-3.1
	// and registered according to https://tools.ietf.org/html/bcp35
	//
	// URI schemes can be classified as:
	//
	// permanent https://tools.ietf.org/html/bcp35#section-3
	// provisional https://tools.ietf.org/html/bcp35#section-4
	// historical https://tools.ietf.org/html/bcp35#section-5
	// private (not listed) https://tools.ietf.org/html/bcp35#section-6
	//
	// Note that all of the above are syntactically equal

	// private
	// https://www.w3.org/TR/app-uri/
	private static final String APP_BASE = "app://2dee5b0a-6100-470a-a67f-1399518cb470/";
	static boolean DEBUG = Boolean.getBoolean("debug");
	// Permanent https://tools.ietf.org/html/rfc7595#section-8
	private static final String EXAMPLE_BASE = "example://2dee5b0a-6100-470a-a67f-1399518cb470/";
	// permanent file with a real hostname
	// Must match src/main/resources/file*
	private static final String FILE_BASE = "file://example.com/";
	// permanent http with a phoney (but syntactically valid) UUID hostname
	private static final String HTTP_BASE = "http://2dee5b0a-6100-470a-a67f-1399518cb470/";
	// private scheme according to https://tools.ietf.org/html/bcp35#section-3.8
	private static final String JENA_BASE = "org.apache.jena.test://foo/";
	// Provisional https://www.iana.org/assignments/uri-schemes/prov/ssh
	private static final String SSH_BASE = "ssh://example.com/";

	// private made-up scheme that contains "-"
	private static final String X_MADEUP_BASE = "x-madeup://2dee5b0a-6100-470a-a67f-1399518cb470/";

	IRIFactory iriFactory = IRIFactory.iriImplementation();
	Model m = ModelFactory.createDefaultModel();
	Interface resourceFactory = ResourceFactory.getInstance();

	@Test
	public void appBaseRDF() throws Exception {
		RDFDataMgr.read(m, load("app-base.rdf"), Lang.RDFXML);
		dumpModelWithMessage("appBaseRDF");
		assertTrue("Can't find statement", m.contains(expectedStatement(APP_BASE)));
	}

	@Test
	public void appBaseTTL() throws Exception {
		RDFDataMgr.read(m, load("app-base.ttl"), Lang.TURTLE);
		dumpModelWithMessage("appBaseTTL");
		assertTrue("Can't find statement", m.contains(expectedStatement(APP_BASE)));
	}

	@Test
	public void appNT() throws Exception {
		RDFDataMgr.read(m, load("app.nt"), Lang.NTRIPLES);
		dumpModelWithMessage("appNT");
		assertTrue("Can't find statement", m.contains(expectedStatement(APP_BASE)));
	}

	@Test
	public void appRDF() throws Exception {
		RDFDataMgr.read(m, load("app.rdf"), Lang.RDFXML);
		dumpModelWithMessage("appRDF");
		assertTrue("Can't find statement", m.contains(expectedStatement(APP_BASE)));
	}

	@Test
	public void appRelRDF() throws Exception {
		RDFDataMgr.read(m, load("rel.rdf"), APP_BASE + "nested/", Lang.RDFXML);
		dumpModelWithMessage("appRelRDF");
		assertTrue("Can't find statement", m.contains(expectedStatement(APP_BASE)));
	}

	@Test
	public void appRelTTL() throws Exception {
		RDFDataMgr.read(m, load("rel.ttl"), APP_BASE + "nested/", Lang.TURTLE);
		dumpModelWithMessage("appRelTTL");
		assertTrue("Can't find statement", m.contains(expectedStatement(APP_BASE)));
	}

	@Test
	public void appTTL() throws Exception {
		RDFDataMgr.read(m, load("app.ttl"), Lang.TURTLE);
		dumpModelWithMessage("appTTL");
		assertTrue("Can't find statement", m.contains(expectedStatement(APP_BASE)));
	}

	private void dumpModelWithMessage(String msg) {
		if (!DEBUG) {
			return;
		}
		System.out.println(msg);
		RDFDataMgr.write(System.out, m, Lang.NTRIPLES);
	}

	@Test
	public void exampleNT() throws Exception {
		RDFDataMgr.read(m, load("example.nt"), Lang.NTRIPLES);
		dumpModelWithMessage("exampleNT");
		assertTrue("Can't find statement", m.contains(expectedStatement(EXAMPLE_BASE)));
	}

	@Test
	public void exampleRDF() throws Exception {
		RDFDataMgr.read(m, load("example.rdf"), Lang.RDFXML);
		dumpModelWithMessage("exampleRDF");
		assertTrue("Can't find statement", m.contains(expectedStatement(EXAMPLE_BASE)));
	}

	@Test
	public void exampleRelRDF() throws Exception {
		RDFDataMgr.read(m, load("rel.rdf"), EXAMPLE_BASE + "nested/", Lang.RDFXML);
		dumpModelWithMessage("exampleRelRDF");
		assertTrue("Can't find statement", m.contains(expectedStatement(EXAMPLE_BASE)));
	}

	@Test
	public void exampleRelTTL() throws Exception {
		RDFDataMgr.read(m, load("rel.ttl"), EXAMPLE_BASE + "nested/", Lang.TURTLE);
		dumpModelWithMessage("exampleRelTTL");
		assertTrue("Can't find statement", m.contains(expectedStatement(EXAMPLE_BASE)));
	}

	@Test
	public void exampleTTL() throws Exception {
		RDFDataMgr.read(m, load("example.ttl"), Lang.TURTLE);
		dumpModelWithMessage("exampleTTL");
		assertTrue("Can't find statement", m.contains(expectedStatement(EXAMPLE_BASE)));
	}

	private Statement expectedStatement(String base) {
		Resource s = resourceFactory.createResource(base + "nested/foo.txt");
		Property p = RDFS.seeAlso;
		RDFNode o = resourceFactory.createResource(base + "bar.txt");
		return resourceFactory.createStatement(s, p, o);
	}

	@Test
	public void fileNT() throws Exception {
		RDFDataMgr.read(m, load("file.nt"), Lang.NTRIPLES);
		dumpModelWithMessage("fileNT");
		assertTrue("Can't find statement", m.contains(expectedStatement(FILE_BASE)));
	}

	@Test
	public void fileRDF() throws Exception {
		RDFDataMgr.read(m, load("file.rdf"), Lang.RDFXML);
		dumpModelWithMessage("fileRDF");
		assertTrue("Can't find statement", m.contains(expectedStatement(FILE_BASE)));
	}

	@Test
	public void fileRelRDF() throws Exception {
		RDFDataMgr.read(m, load("rel.rdf"), FILE_BASE + "nested/", Lang.RDFXML);
		dumpModelWithMessage("fileRelRDF");
		assertTrue("Can't find statement", m.contains(expectedStatement(FILE_BASE)));
	}

	@Test
	public void fileRelTTL() throws Exception {
		RDFDataMgr.read(m, load("rel.ttl"), FILE_BASE + "nested/", Lang.TURTLE);
		dumpModelWithMessage("fileRelTTL");
		assertTrue("Can't find statement", m.contains(expectedStatement(FILE_BASE)));
	}

	@Test
	public void fileTTL() throws Exception {
		RDFDataMgr.read(m, load("file.ttl"), Lang.TURTLE);
		dumpModelWithMessage("fileTTL");
		assertTrue("Can't find statement", m.contains(expectedStatement(FILE_BASE)));
	}

	@Test
	public void httpNT() throws Exception {
		RDFDataMgr.read(m, load("http.nt"), Lang.NTRIPLES);
		dumpModelWithMessage("httpNT");
		assertTrue("Can't find statement", m.contains(expectedStatement(HTTP_BASE)));
	}

	@Test
	public void httpRDF() throws Exception {
		RDFDataMgr.read(m, load("http.rdf"), Lang.RDFXML);
		dumpModelWithMessage("httpRDF");
		assertTrue("Can't find statement", m.contains(expectedStatement(HTTP_BASE)));
	}

	@Test
	public void httpRelRDF() throws Exception {
		RDFDataMgr.read(m, load("rel.rdf"), HTTP_BASE + "nested/", Lang.RDFXML);
		dumpModelWithMessage("httpRelRDF");
		assertTrue("Can't find statement", m.contains(expectedStatement(HTTP_BASE)));
	}

	@Test
	public void httpRelTTL() throws Exception {
		RDFDataMgr.read(m, load("rel.ttl"), HTTP_BASE + "nested/", Lang.TURTLE);
		dumpModelWithMessage("httpRelTTL");
		assertTrue("Can't find statement", m.contains(expectedStatement(HTTP_BASE)));
	}

	@Test
	public void httpTTL() throws Exception {
		RDFDataMgr.read(m, load("http.ttl"), Lang.TURTLE);
		dumpModelWithMessage("httpTTL");
		assertTrue("Can't find statement", m.contains(expectedStatement(HTTP_BASE)));
	}

	@Test
	public void jenaNT() throws Exception {
		RDFDataMgr.read(m, load("jena.nt"), Lang.NTRIPLES);
		dumpModelWithMessage("jenaNT");
		assertTrue("Can't find statement", m.contains(expectedStatement(JENA_BASE)));
	}

	@Test
	public void jenaRDF() throws Exception {
		RDFDataMgr.read(m, load("jena.rdf"), Lang.RDFXML);
		dumpModelWithMessage("jenaRDF");
		assertTrue("Can't find statement", m.contains(expectedStatement(JENA_BASE)));
	}

	@Test
	public void jenaRelRDF() throws Exception {
		RDFDataMgr.read(m, load("rel.rdf"), JENA_BASE + "nested/", Lang.RDFXML);
		dumpModelWithMessage("jenaRelRDF");
		assertTrue("Can't find statement", m.contains(expectedStatement(JENA_BASE)));
	}

	@Test
	public void jenaRelTTL() throws Exception {
		RDFDataMgr.read(m, load("rel.ttl"), JENA_BASE + "nested/", Lang.TURTLE);
		dumpModelWithMessage("jenaRelTTL");
		assertTrue("Can't find statement", m.contains(expectedStatement(JENA_BASE)));
	}

	@Test
	public void jenaTTL() throws Exception {
		RDFDataMgr.read(m, load("jena.ttl"), Lang.TURTLE);
		dumpModelWithMessage("jenaTTL");
		assertTrue("Can't find statement", m.contains(expectedStatement(JENA_BASE)));
	}

	private InputStream load(String f) {
		return Objects.requireNonNull(getClass().getResourceAsStream(f));
	}

	@Test
	public void sshBaseRDF() throws Exception {
		RDFDataMgr.read(m, load("ssh-base.rdf"), Lang.RDFXML);
		dumpModelWithMessage("sshRDF");
		assertTrue("Can't find statement", m.contains(expectedStatement(SSH_BASE)));
	}

	@Test
	public void sshBaseTTL() throws Exception {
		RDFDataMgr.read(m, load("ssh-base.ttl"), Lang.TURTLE);
		dumpModelWithMessage("sshNT");
		assertTrue("Can't find statement", m.contains(expectedStatement(SSH_BASE)));
	}

	@Test
	public void sshNT() throws Exception {
		RDFDataMgr.read(m, load("ssh.nt"), Lang.NTRIPLES);
		dumpModelWithMessage("sshNT");
		assertTrue("Can't find statement", m.contains(expectedStatement(SSH_BASE)));
	}

	@Test
	public void sshRDF() throws Exception {
		RDFDataMgr.read(m, load("ssh.rdf"), Lang.RDFXML);
		dumpModelWithMessage("sshRDF");
		assertTrue("Can't find statement", m.contains(expectedStatement(SSH_BASE)));
	}

	@Test
	public void sshRelRDF() throws Exception {
		RDFDataMgr.read(m, load("rel.rdf"), SSH_BASE + "nested/", Lang.RDFXML);
		dumpModelWithMessage("sshRelRDF");
		assertTrue("Can't find statement", m.contains(expectedStatement(SSH_BASE)));
	}

	@Test
	public void sshRelTTL() throws Exception {
		RDFDataMgr.read(m, load("rel.ttl"), SSH_BASE + "nested/", Lang.TURTLE);
		dumpModelWithMessage("sshRelTTL");
		assertTrue("Can't find statement", m.contains(expectedStatement(SSH_BASE)));
	}

	@Test
	public void sshTTL() throws Exception {
		RDFDataMgr.read(m, load("ssh.ttl"), Lang.TURTLE);
		dumpModelWithMessage("sshTTL");
		assertTrue("Can't find statement", m.contains(expectedStatement(SSH_BASE)));
	}

	@Test
	public void xMadeupNT() throws Exception {
		RDFDataMgr.read(m, load("x-madeup.nt"), Lang.NTRIPLES);
		dumpModelWithMessage("xMadeupNT");
		assertTrue("Can't find statement", m.contains(expectedStatement(X_MADEUP_BASE)));
	}

	@Test
	public void xMadeupRDF() throws Exception {
		RDFDataMgr.read(m, load("x-madeup.rdf"), Lang.RDFXML);
		dumpModelWithMessage("xMadeupRDF");
		assertTrue("Can't find statement", m.contains(expectedStatement(X_MADEUP_BASE)));
	}

	@Test
	public void xMadeupRelRDF() throws Exception {
		RDFDataMgr.read(m, load("rel.rdf"), X_MADEUP_BASE + "nested/", Lang.RDFXML);
		dumpModelWithMessage("xMadeupRelRDF");
		assertTrue("Can't find statement", m.contains(expectedStatement(X_MADEUP_BASE)));
	}

	@Test
	public void xMadeupRelTTL() throws Exception {
		RDFDataMgr.read(m, load("rel.ttl"), X_MADEUP_BASE + "nested/", Lang.TURTLE);
		dumpModelWithMessage("xMadeupRelTTL");
		assertTrue("Can't find statement", m.contains(expectedStatement(X_MADEUP_BASE)));
	}

	@Test
	public void xMadeupTTL() throws Exception {
		RDFDataMgr.read(m, load("x-madeup.ttl"), Lang.TURTLE);
		dumpModelWithMessage("xMadeupTTL");
		assertTrue("Can't find statement", m.contains(expectedStatement(X_MADEUP_BASE)));
	}

	//
}
