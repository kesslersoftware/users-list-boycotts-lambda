package com.boycottpro.userboycotts;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.boycottpro.userboycotts.model.CompanySummary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetBoycottsPerUserHandlerTest {

    @Mock
    private DynamoDbClient dynamoDb;

    @InjectMocks
    private GetBoycottsPerUserHandler handler;

    @Test
    public void testValidUserIdReturnsCompanies() throws Exception {
        String userId = "test-user";
        Map<String, AttributeValue> item = Map.of(
                "company_id", AttributeValue.fromS("c1"),
                "company_name", AttributeValue.fromS("Apple")
        );

        QueryResponse response = QueryResponse.builder().items(List.of(item)).build();
        when(dynamoDb.query(any(QueryRequest.class))).thenReturn(response);

        APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent();
        event.setPathParameters(Map.of("user_id", userId));

        APIGatewayProxyResponseEvent result = handler.handleRequest(event, mock(Context.class));

        assertEquals(200, result.getStatusCode());
        assertTrue(result.getBody().contains("Apple"));
    }

    @Test
    public void testMissingUserIdReturns400() {
        APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent();
        event.setPathParameters(null);

        APIGatewayProxyResponseEvent result = handler.handleRequest(event, mock(Context.class));

        assertEquals(400, result.getStatusCode());
        assertTrue(result.getBody().contains("Missing user_id"));
    }
}