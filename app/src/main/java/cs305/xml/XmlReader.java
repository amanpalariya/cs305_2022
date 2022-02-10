package cs305.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XmlReader {
    private Document document;

    public XmlReader(String filepath) throws IOException, XmlParsingException {
        try {
            File f = new File(filepath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            this.document = dBuilder.parse(f);
            this.document.getDocumentElement().normalize();
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new XmlParsingException();
        }
    }

    public SqlTag getTagWithId(String queryId) throws NoSqlTagWithGivenIdException {
        NodeList nl;
        try {
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();
            XPathExpression expr = xPath.compile("//sql[@id=\"" + queryId + "\"]");
            nl = (NodeList) expr.evaluate(this.document, XPathConstants.NODESET);
        } catch (Exception e) {
            throw new NoSqlTagWithGivenIdException(queryId);
        }

        if (nl.getLength() != 0) {
            Element sqlQueryElement = (Element) nl.item(0);
            String paramType = sqlQueryElement.getAttribute("paramType");
            String queryString = sqlQueryElement.getTextContent().trim();
            return new SqlTag(queryString, paramType);
        } else {
            throw new NoSqlTagWithGivenIdException(queryId);
        }
    }
}
