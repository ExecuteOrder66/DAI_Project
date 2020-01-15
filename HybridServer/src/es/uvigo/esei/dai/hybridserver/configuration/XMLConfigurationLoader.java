/**
 *  HybridServer
 *  Copyright (C) 2017 Miguel Reboiro-Jato
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.uvigo.esei.dai.hybridserver.configuration;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.*;

import es.uvigo.esei.dai.hybridserver.SimpleErrorHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XMLConfigurationLoader {
	public Configuration load(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
		// Construcción del parser del documento activando validación
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		factory.setNamespaceAware(true);
		factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
				XMLConstants.W3C_XML_SCHEMA_NS_URI);

		// Al construir el parser hay que añadir un manejador de errores
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setErrorHandler(new SimpleErrorHandler());

		// Parsing y validación del documento
		Document document = builder.parse(xmlFile);

		// Analizar el documento y establecer los valores de la configuración
		int httpPort = 0;
		String webServiceURL = null;
		int numClients = 0;
		String dbUser = null;
		String dbPassword = null;
		String dbURL = null;
		List<ServerConfiguration> servers = new LinkedList<ServerConfiguration>();

		Element confElement = (Element) document.getElementsByTagName("configuration").item(0);
		NodeList confChildren = confElement.getChildNodes();

		for (int i = 0; i < confChildren.getLength(); i++) {
			Node confChild = confChildren.item(i);
			if ("connections".equals(confChild.getNodeName())) {
				NodeList connectionsChildren = confChild.getChildNodes();
				for (int j = 0; j < connectionsChildren.getLength(); j++) {
					Node connectionChild = connectionsChildren.item(j);

					if ("http".equals(connectionChild.getNodeName())) {
						httpPort = Integer.parseInt(connectionChild.getTextContent());
					} else if ("webservice".equals(connectionChild.getNodeName())) {
						webServiceURL = connectionChild.getTextContent();
					} else if ("numClients".equals(connectionChild.getNodeName())) {
						numClients = Integer.parseInt(connectionChild.getTextContent());
					}
				}
			}
			if ("database".equals(confChild.getNodeName())) {
				NodeList databaseChildren = confChild.getChildNodes();
				for (int j = 0; j < databaseChildren.getLength(); j++) {
					Node databaseChild = databaseChildren.item(j);

					if ("user".equals(databaseChild.getNodeName())) {
						dbUser = databaseChild.getTextContent();
					} else if ("password".equals(databaseChild.getNodeName())) {
						dbPassword = databaseChild.getTextContent();
					} else if ("url".equals(databaseChild.getNodeName())) {
						dbURL = databaseChild.getTextContent();
					}
				}
			}
			if ("servers".equals(confChild.getNodeName())) {
				NodeList serversChildren = confChild.getChildNodes();
				for (int j = 0; j < serversChildren.getLength(); j++) {
					Node serversChild = serversChildren.item(j);
					if ("server".equals(serversChild.getNodeName())) {
						NamedNodeMap atribs = serversChild.getAttributes();
						ServerConfiguration serverConf = new ServerConfiguration(
								atribs.getNamedItem("name").getNodeValue(), atribs.getNamedItem("wsdl").getNodeValue(),
								atribs.getNamedItem("namespace").getNodeValue(),
								atribs.getNamedItem("service").getNodeValue(),
								atribs.getNamedItem("httpAddress").getNodeValue());
						servers.add(serverConf);
					}
				}
			}
		}

		Configuration configuration = new Configuration(httpPort, numClients, webServiceURL, dbUser, dbPassword, dbURL,
				servers);

		return configuration;
	}

}
