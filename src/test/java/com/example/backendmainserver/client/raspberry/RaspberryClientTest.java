package com.example.backendmainserver.client.raspberry;

import com.example.backendmainserver.client.raspberry.dto.request.BatterySwitchRequest;
import com.example.backendmainserver.client.raspberry.dto.request.PortAndSupplier;
import com.example.backendmainserver.client.raspberry.dto.response.BatterySwitchResponse;
import com.example.backendmainserver.client.raspberry.dto.response.PortAndResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class RaspberryClientTest {
    private MockWebServer mockWebServer;
    private RaspberryClient raspberryClient;

    @BeforeEach
    public void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        raspberryClient = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .contract(new SpringMvcContract())
                .target(RaspberryClient.class, mockWebServer.url("/").toString());
    }

    @AfterEach
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void testRequestPortBatterySwitch() throws Exception {
        // Prepare the expected response
        BatterySwitchResponse expectedResponse = new BatterySwitchResponse(
                Arrays.asList(new PortAndResult(1L, "success")));

        ObjectMapper objectMapper = new ObjectMapper();
        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(expectedResponse))
                .addHeader("Content-Type", "application/json"));

        // Create the request object
        BatterySwitchRequest request = new BatterySwitchRequest(Arrays.asList(new PortAndSupplier(1L, "BATTERY")));

        // Execute the client call
        BatterySwitchResponse actualResponse = raspberryClient.requestPortBatterySwitch(request);

        // Verify the response
        assertEquals(expectedResponse.portAndResults().get(0).portId(), actualResponse.portAndResults().get(0).portId());
        assertEquals(expectedResponse.portAndResults().get(0).result(), actualResponse.portAndResults().get(0).result());
    }
}