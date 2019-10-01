package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;


public class ServiceThread implements Runnable{
	
	private final Socket socket;
	
	public ServiceThread(Socket client) throws IOException{
		this.socket=client;
	}
	
	@Override
	public void run() {
		
		try(Socket clientSocket = this.socket){
			// Responder al cliente
			String html = "<html><head></head><body>Hybrid Server</body></html>";
			OutputStream clientOutput = clientSocket.getOutputStream();
			//No hace falta hacer try con OutputStream porque se cerrara cuando se cierre el hilo
			
			clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
			clientOutput.write(("Content-Length: " + html.length() + "\r\n\r\n").getBytes());
			clientOutput.write(html.getBytes());
			
			clientOutput.flush();	
		}catch(IOException e) {
			e.getLocalizedMessage();
		}
	}

}
