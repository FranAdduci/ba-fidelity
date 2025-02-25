package com.challenge.fidelity.webapp.challenge_fidelity.service;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

@Service
public class SaleService {

    @Autowired
    private WebServiceTemplate webServiceTemplate;

    public String saleByCard(String sessionID, long customerID, double totalMoney, String notes) {
        try {
            
            String soapRequestXml = "<fid:SaleByCardRequest xmlns:fid=\"http://FidelyNET3_WS_01_89_00.Local\">" +
                    "<fid:sessionID>" + sessionID + "</fid:sessionID>" +
                    "<fid:customerID>" + customerID + "</fid:customerID>" +
                    "<fid:totalMoney>" + totalMoney + "</fid:totalMoney>" +
                    "<fid:notes>" + notes + "</fid:notes>" +
                    "<fid:notReward>false</fid:notReward>" +
                    "</fid:SaleByCardRequest>";
            
            StringWriter writer = new StringWriter();
            webServiceTemplate.sendSourceAndReceiveToResult(
                    "https://test113.fidely.net/fnet3web/interfaces",
                    new StreamSource(new StringReader(soapRequestXml)),
                    new SoapActionCallback("SaleByCard"),
                    new StreamResult(writer));

            String responseContent = writer.toString();

            // Convertir XML a JSON
            JSONObject jsonResponse = convertXmlToJson(responseContent);

            // Devolver JSON en formato string
            return jsonResponse.toString(4); // 4 es la indentación para que se vea bonito

        } catch (Exception e) {
            System.err.println("Error en SaleByCardRequest: " + e.getMessage());
            e.printStackTrace();
            return "{\"error\": \"Error al realizar la venta\"}";
        }
    }

    // Método para convertir XML a JSON
    private JSONObject convertXmlToJson(String xml) {
        try {
            return XML.toJSONObject(xml);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }
}
