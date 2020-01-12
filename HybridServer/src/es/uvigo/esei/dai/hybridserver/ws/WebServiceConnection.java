package es.uvigo.esei.dai.hybridserver.ws;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import es.uvigo.esei.dai.hybridserver.ServerConfiguration;

public class WebServiceConnection {
	
	List<HybridServerService> hybridServerServiceList = new LinkedList<HybridServerService>();

	public WebServiceConnection(List<ServerConfiguration> serverConfList) throws MalformedURLException {
		
		for (ServerConfiguration server : serverConfList) {
			URL url = new URL(server.getWsdl());

			QName name = new QName(server.getNamespace(), server.getService());

			Service service = Service.create(url, name);
			
			hybridServerServiceList.add(service.getPort(HybridServerService.class));
		}
	}
	
	public List<HybridServerService> getServers() {
		return  hybridServerServiceList;
	}
	
	/*public String getHTMLuuid(String htmlUuid) {
		for (HybridServerService server : hybridServerServiceList) {
			List<String> uuidList = server.getHTMLuuids();
			for(String uuid : uuidList) {
				if(uuid.equals(htmlUuid)) {
					return uuid;
				}
			}
		}
		return null;
	}*/
}
