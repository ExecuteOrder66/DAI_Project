<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:c="http://www.esei.uvigo.es/dai/hybridserver"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.esei.uvigo.es/dai/hybridserver ../configuration.xsd">
	
	<xsl:output method="html" encoding="utf8" indent="yes" />
	
	<xsl:template match="/">
		<html>
			<head>
				<title>Configuracion</title>
			</head>
			<body>
				<div id="container">
					
				</div>
			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="c:connections">
		<div class="connections">
			<h3><strong>Connections</strong></h3>
			<div class="http">
				<strong>HttpPort:</strong>&#160<xsl:value-of select="c:configuration:connections:http"/>
				<strong>WebService:</strong>&#160<xsl:value-of select="c:configuration:connections:webservice"></xsl:value-of>
				<strong>numClients:</strong>&#160<xsl:value-of select="c:configuration:connections:numClients"></xsl:value-of>
			</div>
		</div>
	</xsl:template>
	
	<xsl:template match="c:database">
		<div class="database">
			
		</div>
	</xsl:template>
	
		<xsl:template match="c:servers">
		<div class="servers">
			
		</div>
	</xsl:template>
	
</xsl:stylesheet>