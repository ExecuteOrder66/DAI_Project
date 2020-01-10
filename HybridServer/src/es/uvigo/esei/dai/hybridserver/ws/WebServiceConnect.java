package es.uvigo.esei.dai.hybridserver.ws;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import es.uvigo.esei.dai.hybridserver.Configuration;
import es.uvigo.esei.dai.hybridserver.ServerConfiguration;

public class WebServiceConnect {

	public WebServiceConnect(Configuration configuration) throws MalformedURLException {

		for (ServerConfiguration server : configuration.getServers()) {
			URL url = new URL(server.getWsdl());

			QName name = new QName(server.getNamespace(), server.getService());

			Service service = Service.create(url, name);
			
			HybridServerService hybridServer = service.getPort(HybridServerService.class);
		}
	}
}
