package com.challenge.fidelity.webapp.challenge_fidelity.service;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Optional;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

@Service
public class CustomerInfoService {

    private final WebServiceTemplate webServiceTemplate;

    @Autowired
    public CustomerInfoService(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    public Optional<String> getInfo(String sessionID) {
        try {
            String soapRequestXml = 
                "<fid:GetInfoRequest xmlns:fid=\"http://FidelyNET3_WS_01_89_00.Local\">" +
                "<fid:sessionID>" + sessionID + "</fid:sessionID>" +
                "<fid:customerID></fid:customerID>" +
                "<fid:card>100</fid:card>" +
                "<fid:mobile></fid:mobile>" +
                "<fid:email></fid:email>" +
                "<fid:identityCard></fid:identityCard>" +
                "<fid:foreignId></fid:foreignId>" +
                "</fid:GetInfoRequest>";

            StringWriter writer = new StringWriter();
            webServiceTemplate.sendSourceAndReceiveToResult(
                "https://test113.fidely.net/fnet3web/interfaces",
                new StreamSource(new StringReader(soapRequestXml)),
                new SoapActionCallback("GetInfo"),
                new StreamResult(writer)
            );

            String responseContent = writer.toString();
            //System.out.println("SOAP Response (CustomerInfoService):\n" + responseContent);

            // Extraer valores específicos de la respuesta SOAP
            String customerId = extractCustomerId(responseContent);

            // Mostrar el customerId extraído
            //System.out.println("Extracted Customer ID: " + customerId);

            // Devolver solo el customerID, no el AnswerCode
            return Optional.of(customerId);

        } catch (Exception e) {
            System.err.println("Error en CustomerInfoService: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private String extractCustomerId(String xmlResponse) throws Exception {
        Document doc = parseXml(xmlResponse);
        XPath xpath = XPathFactory.newInstance().newXPath();

        // Registrar los namespaces para ns96 y ns97
        xpath.setNamespaceContext(new NamespaceContext() {
            public String getNamespaceURI(String prefix) {
                if ("ns96".equals(prefix)) {
                    return "http://FidelyNET3_WS_01_89_00.Local"; // Namespace del elemento customer
                }
                if ("ns97".equals(prefix)) {
                    return "http://FidelyNET3_SW_01_89_00"; // Namespace del elemento id
                }
                return null;
            }

            public String getPrefix(String namespaceURI) {
                return null;
            }

            public Iterator<String> getPrefixes(String namespaceURI) {
                return null;
            }
        });

        // XPath para extraer el customerId
        XPathExpression expr = xpath.compile("//ns96:customer/ns97:id/text()");
        return (String) expr.evaluate(doc, XPathConstants.STRING);
    }

    private Document parseXml(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true); // Importante para que reconozca los namespaces
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xml)));
    }
}
