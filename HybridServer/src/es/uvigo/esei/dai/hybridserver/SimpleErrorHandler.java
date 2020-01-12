package es.uvigo.esei.dai.hybridserver;

import org.xml.sax.*;

public class SimpleErrorHandler implements ErrorHandler {
	@Override
	public void warning (SAXParseException exception) throws SAXException {
		exception.printStackTrace();
	}

	@Override
	public void fatalError (SAXParseException exception) throws SAXException {
		throw exception;
	}

	@Override
	public void error (SAXParseException exception)throws SAXException {
		throw exception;
	}
}