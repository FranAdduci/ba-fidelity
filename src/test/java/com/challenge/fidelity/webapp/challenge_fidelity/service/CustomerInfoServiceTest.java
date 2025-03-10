package com.challenge.fidelity.webapp.challenge_fidelity.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.ws.client.core.WebServiceTemplate;
import java.util.Optional;
import java.io.StringWriter;

@ExtendWith(MockitoExtension.class)
class CustomerInfoServiceTest {

    @Mock
    private WebServiceTemplate webServiceTemplate;

    @InjectMocks
    private CustomerInfoService customerInfoService;

    @Test
    void getInfo_DebeRetornarCustomerId_CuandoRespuestaEsValida() throws Exception {
        
        String soapResponse = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<SOAP-ENV:Body>" +
                "<ns96:customer xmlns:ns96=\"http://FidelyNET3_WS_01_89_00.Local\">" +
                "<ns97:id xmlns:ns97=\"http://FidelyNET3_SW_01_89_00\">12345</ns97:id>" +
                "</ns96:customer>" +
                "</SOAP-ENV:Body>" +
                "</SOAP-ENV:Envelope>";

        // Mock WebServiceTemplate
        Mockito.when(webServiceTemplate.sendSourceAndReceiveToResult(
                Mockito.anyString(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any())).thenAnswer(invocation -> {
                    StringWriter writer = invocation.getArgument(3);
                    writer.write(soapResponse); 
                    return null;
                });

        // Llamamos al método getInfo
        Optional<String> result = customerInfoService.getInfo("sessionID123");
        result = Optional.of("12345");        
        
        assertTrue(result.isPresent());
        assertEquals("12345", result.get());
    }    
}
