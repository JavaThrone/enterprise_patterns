package it.discovery.order.controller;

import it.discovery.order.OrderApplication;
import it.discovery.order.dto.BookDTO;
import it.discovery.order.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringJUnitWebConfig(OrderApplication.class)
@AutoConfigureWebClient
@AutoConfigureJsonTesters
@TestPropertySource("classpath:application.properties")
public class OrderControllerTest {

    @Autowired
    RestTemplate bookRestTemplate;

    @Autowired
    OrderController orderController;

    @Autowired
    JacksonTester<BookDTO> bookTester;

    MockRestServiceServer bookMockServer;

    @Autowired
    Environment env;

    @BeforeEach
    void setup() {
        bookMockServer = MockRestServiceServer.createServer(bookRestTemplate);
    }

    @Test
    void makeOrder_validBookId_returnOrder() throws IOException {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(1);
        bookDTO.setPrice(200);

        bookMockServer.expect(requestTo(env.getProperty("book.service.url")
                + "/" + bookDTO.getId()))
                .andRespond(withSuccess(bookTester.write(bookDTO).getJson(),
                        MediaType.APPLICATION_JSON_UTF8));

        Order order = orderController.makeOrder(bookDTO.getId());

        bookMockServer.verify();
        assertEquals(bookDTO.getId(), order.getBookId());
        assertEquals(bookDTO.getPrice(), order.getPrice());
    }

}
