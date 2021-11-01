<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="text" omit-xml-declaration="yes"/>

  <xsl:template match="/">
    Coding Style Check Results
    --------------------------
    Total files checked:
    <xsl:apply-templates/>
    Files with errors:
    <xsl:number level="any" value="count(descendant::file[error])"/>
    Total errors:
    <xsl:number level="any" value="count(descendant::error)"/>
    Errors per file:
    <xsl:number level="any" value="count(descendant::error) div count(descendant::file)"/>
    <xsl:number level="any" value="count(descendant::file)"/>
  </xsl:template>

  <xsl:template match="file[error]">
    no.unit.nva.file.model.File:
    <xsl:apply-templates select="error"/>
    <xsl:text>
</xsl:text>
    <xsl:value-of select="@name"/>
  </xsl:template>

  <xsl:template match="error">
    <xsl:text> - </xsl:text>:<xsl:text>
</xsl:text><xsl:value-of select="@line"/><xsl:value-of
    select="@message"/><xsl:value-of select="@column"/>
  </xsl:template>

</xsl:stylesheet>