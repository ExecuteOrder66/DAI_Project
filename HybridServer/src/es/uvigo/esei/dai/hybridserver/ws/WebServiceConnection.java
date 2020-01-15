package es.uvigo.esei.dai.hybridserver.ws;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;

import es.uvigo.esei.dai.hybridserver.configuration.ServerConfiguration;

public class WebServiceConnection {

	List<HybridServerService> hybridServerServiceList = new LinkedList<HybridServerService>();

	public WebServiceConnection(List<ServerConfiguration> serverConfList) throws MalformedURLException {

		for (ServerConfiguration server : serverConfList) {
			URL url = new URL(server.getWsdl());
			QName name = new QName(server.getNamespace(), server.getService());

			try {
				Service service = Service.create(url, name);
				hybridServerServiceList.add(service.getPort(HybridServerService.class));
			} catch (WebServiceException e) {
				System.err.println(server.getName() + " is down");
				e.printStackTrace();
			}
		}
	}

	public List<HybridServerService> getServers() {
		return hybridServerServiceList;
	}
}
