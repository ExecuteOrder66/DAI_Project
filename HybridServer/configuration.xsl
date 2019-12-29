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
				<title>Configuration</title>
			</head>
			<body>
				<div id="container">
					<h1>Configuration</h1>
					<xsl:apply-templates select="c:configuration/c:connections"/>
					<xsl:apply-templates select="c:configuration/c:database"/>
					<xsl:apply-templates select="c:configuration/c:servers"/>
				</div>
			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="c:connections">
		<div class="connections">
			<h3><strong>Connections</strong></h3>
			<div class="http">
				<strong>HttpPort: </strong><xsl:value-of select="c:http"/> 
			</div>
			<div class="webService">
				<strong>WebService: </strong><xsl:value-of select="c:webservice"></xsl:value-of>
			</div>
			<div class="numClients">
				<strong>numClients: </strong><xsl:value-of select="c:numClients"></xsl:value-of>
			</div>	
		</div>
	</xsl:template>
	
	<xsl:template match="c:database">
		<div class="database">
			<h3><strong>Database</strong></h3>
			<div class="user">
				<strong>User: </strong><xsl:value-of select="c:user"></xsl:value-of>
			</div>
			<div class="password">
				<strong>Password: </strong><xsl:value-of select="c:password"></xsl:value-of>
			</div>
			<div class="url">
				<strong>Url: </strong><xsl:value-of select="c:url"></xsl:value-of>
			</div>
		</div>
	</xsl:template>
	
	<xsl:template match="c:servers">
		<div class="servers">
			<h3>Servers</h3>
				<xsl:if test="c:server">
					<xsl:for-each select="c:server">
						<h4><xsl:value-of select="@name"></xsl:value-of></h4>
						<ul>
							<li><strong>Wsdl: </strong><xsl:value-of select="@wsdl"></xsl:value-of></li>
							<li><strong>Namespace: </strong><xsl:value-of select="@namespace"></xsl:value-of></li>
							<li><strong>Service: </strong><xsl:value-of select="@service"></xsl:value-of></li>
							<li><strong>HttpAddress: </strong><xsl:value-of select="@httpAddress"></xsl:value-of></li>
						</ul>
					</xsl:for-each>
				</xsl:if>
		</div>
	</xsl:template>
	
</xsl:stylesheet>