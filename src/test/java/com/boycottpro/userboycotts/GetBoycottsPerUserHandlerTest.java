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
        Map<String, AttributeValue> item = Map.of(
                "company_id", AttributeValue.fromS("c1"),
                "company_name", AttributeValue.fromS("Apple")
        );

        QueryResponse response = QueryResponse.builder().items(List.of(item)).build();
        when(dynamoDb.query(any(QueryRequest.class))).thenReturn(response);

        APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent();
        Map<String, String> claims = Map.of("sub", "11111111-2222-3333-4444-555555555555");
        Map<String, Object> authorizer = new HashMap<>();
        authorizer.put("claims", claims);

        APIGatewayProxyRequestEvent.ProxyRequestContext rc = new APIGatewayProxyRequestEvent.ProxyRequestContext();
        rc.setAuthorizer(authorizer);
        event.setRequestContext(rc);

        // Path param "s" since client calls /users/s
        event.setPathParameters(Map.of("user_id", "s"));

        APIGatewayProxyResponseEvent result = handler.handleRequest(event, mock(Context.class));

        assertEquals(200, result.getStatusCode());
        assertTrue(result.getBody().contains("Apple"));
    }

    @Test
    public void testMissingUserIdReturns400() {
        APIGatewayProxyRequestEvent event = null;

        APIGatewayProxyResponseEvent result = handler.handleRequest(event, mock(Context.class));

        assertEquals(401, result.getStatusCode());
        assertTrue(result.getBody().contains("Unauthorized"));
    }
}