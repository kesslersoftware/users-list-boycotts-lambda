package com.boycottpro.userboycotts;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import com.boycottpro.userboycotts.model.CompanySummary;
import com.boycottpro.utilities.JwtUtility;
import com.boycottpro.utilities.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

import java.util.*;

public class GetBoycottsPerUserHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final String TABLE_NAME = "";
    private final DynamoDbClient dynamoDb;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GetBoycottsPerUserHandler() {
        this.dynamoDb = DynamoDbClient.create();
    }

    public GetBoycottsPerUserHandler(DynamoDbClient dynamoDb) {
        this.dynamoDb = dynamoDb;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        String sub = null;
        int lineNum = 37;
        try {
            sub = JwtUtility.getSubFromRestEvent(event);
            if (sub == null) {
            Logger.error(40, sub, "user is Unauthorized");
            return response(401, Map.of("message", "Unauthorized"));
        }
            lineNum = 44;
            Set<CompanySummary> companies = getUserBoycottedCompanies(sub);
            lineNum = 46;
            return response(200,companies);
        } catch (Exception e) {
            Logger.error(lineNum, sub, e.getMessage());
            return response(500,Map.of("error", "Unexpected server error: " + e.getMessage()) );
        }
    }
    private APIGatewayProxyResponseEvent response(int status, Object body) {
        String responseBody = null;
        try {
            responseBody = objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(status)
                .withHeaders(Map.of("Content-Type", "application/json"))
                .withBody(responseBody);
    }
    public Set<CompanySummary> getUserBoycottedCompanies(String userId) {
        QueryRequest request = QueryRequest.builder()
                .tableName("user_boycotts")
                .keyConditionExpression("user_id = :uid")
                .expressionAttributeValues(Map.of(":uid", AttributeValue.fromS(userId)))
                .projectionExpression("company_id, company_name") // only fetch necessary fields
                .build();

        QueryResponse response = dynamoDb.query(request);

        Set<CompanySummary> uniqueCompanies = new HashSet<>();

        for (Map<String, AttributeValue> item : response.items()) {
            String companyId = item.get("company_id").s();
            String companyName = item.getOrDefault("company_name", AttributeValue.fromS("")).s();
            uniqueCompanies.add(new CompanySummary(companyId, companyName));
        }

        return uniqueCompanies;
    }

}