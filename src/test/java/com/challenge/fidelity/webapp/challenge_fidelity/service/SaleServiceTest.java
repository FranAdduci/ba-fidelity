// package com.challenge.fidelity.webapp.challenge_fidelity.service;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.Mockito.*;

// import java.io.StringWriter;

// import javax.xml.transform.stream.StreamResult;
// import javax.xml.transform.stream.StreamSource;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.ws.client.core.WebServiceTemplate;
// import org.springframework.ws.soap.client.core.SoapActionCallback;

// class SaleServiceTest {

//     @Mock
//     private WebServiceTemplate webServiceTemplate;

//     @InjectMocks
//     private SaleService saleService;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//     }

//     @Test
//     void testSaleByCard_Success() throws Exception {
//         String sessionID = "28d87f5d-61e2-4e59-b8c4-a4b5071c03c5";
//         long customerID = 1871859;
//         double totalMoney = 100;
//         String notes = "Compra de prueba";

//         String soapResponse = """
//             <fid:SaleByCardResponse xmlns:fid="http://FidelyNET3_WS_01_89_00.Local">
//                 <fid:result>SUCCESS</fid:result>
//             </fid:SaleByCardResponse>
//         """;

//         doAnswer(invocation -> {
//             StreamResult result = invocation.getArgument(2);
//             StringWriter writer = (StringWriter) result.getWriter();
//             writer.write(soapResponse);
//             return null;
//         }).when(webServiceTemplate).sendSourceAndReceiveToResult(
//             anyString(),
//             any(StreamSource.class),
//             any(SoapActionCallback.class),
//             any(StreamResult.class)
//         );

//         String response = saleService.saleByCard(sessionID, customerID, totalMoney, notes);
//         assertEquals("SUCCESS", response);
//     }

//     @Test
//     void testSaleByCard_Failure() {
//         String sessionID = "123456";
//         long customerID = 7890;
//         double totalMoney = 100.0;
//         String notes = "Compra de prueba";

//         doThrow(new RuntimeException("SOAP request failed"))
//             .when(webServiceTemplate).sendSourceAndReceiveToResult(
//                 anyString(),
//                 any(StreamSource.class),
//                 any(SoapActionCallback.class),
//                 any(StreamResult.class)
//             );

//         String response = saleService.saleByCard(sessionID, customerID, totalMoney, notes);
//         assertEquals("Error al realizar la venta", response);
//     }
// }
