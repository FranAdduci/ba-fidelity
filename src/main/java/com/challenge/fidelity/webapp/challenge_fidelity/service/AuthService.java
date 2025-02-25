package com.challenge.fidelity.webapp.challenge_fidelity.service;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Optional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@Service
public class AuthService {

    private final WebServiceTemplate webServiceTemplate;

    @Autowired
    public AuthService(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    public Optional<String> synchroAndLogin(String serialNumber, String username, String password, String foreignID) {
        try {
            String soapRequestXml =
                "<fid:SynchroAndLoginRequest xmlns:fid=\"http://FidelyNET3_WS_01_89_00.Local\">" +
                "<fid:serialNumber>" + serialNumber + "</fid:serialNumber>" +
                "<fid:username>" + username + "</fid:username>" +
                "<fid:password>" + password + "</fid:password>" +
                "<fid:foreignID>" + foreignID + "</fid:foreignID>" +
                "</fid:SynchroAndLoginRequest>";

            StringWriter writer = new StringWriter();
            webServiceTemplate.sendSourceAndReceiveToResult(
                "https://test113.fidely.net/fnet3web/interfaces",
                new StreamSource(new StringReader(soapRequestXml)),
                new SoapActionCallback("SynchroAndLogin"),
                new StreamResult(writer)
            );

            String responseContent = writer.toString();
            //System.out.println("SOAP Response (AuthService):\n" + responseContent);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(responseContent)));

            NodeList sessionIDNodes = doc.getElementsByTagNameNS("http://FidelyNET3_WS_01_89_00.Local", "sessionID");

            if (sessionIDNodes.getLength() > 0) {
                String sessionID = sessionIDNodes.item(0).getTextContent();
                //System.out.println("sessionID Capturado: " + sessionID);
                return Optional.of(sessionID);
            } else {
                System.err.println("No se encontró el sessionID en la respuesta.");
                return Optional.empty();
            }

        } catch (Exception e) {
            System.err.println("Error en AuthService: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
