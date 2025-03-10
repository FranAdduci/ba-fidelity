package com.challenge.fidelity.webapp.challenge_fidelity.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

class AuthServiceTest {

    private AuthService authService;
    private WebServiceTemplate webServiceTemplate;

    @BeforeEach
    void setUp() {
        webServiceTemplate = mock(WebServiceTemplate.class);
        authService = new AuthService(webServiceTemplate);
    }

    @Test
    void synchroAndLogin_DebeDevolverSessionID() throws Exception {
        // 🔹 Simulamos una respuesta SOAP con un sessionID
        String soapResponse = "<fid:SynchroAndLoginResponse xmlns:fid=\"http://FidelyNET3_WS_01_89_00.Local\">" +
                "<fid:sessionID>12345</fid:sessionID>" +
                "</fid:SynchroAndLoginResponse>";

        // 🔹 Simulamos la escritura del resultado en el StreamResult
        doAnswer(invocation -> {
            StreamResult result = invocation.getArgument(3);
            result.getWriter().write(soapResponse);
            return null;
        }).when(webServiceTemplate).sendSourceAndReceiveToResult(
                anyString(),
                any(StreamSource.class),
                any(SoapActionCallback.class),
                any(StreamResult.class));

        // 🔹 Llamamos al método y verificamos el resultado
        Optional<String> sessionId = authService.synchroAndLogin("serial123", "user123", "pass123", "foreignID123");

        assertTrue(sessionId.isPresent(), "El sessionID no debería estar vacío.");
        assertEquals("12345", sessionId.get(), "El sessionID devuelto no es el esperado.");
    }
}
