package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.data.Answer;
import com.serverless.data.Question;
import com.serverless.db.DynamoDBAdapter;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.data.Question;
import com.serverless.db.DynamoDBAdapter;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetTransactionsResultsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = Logger.getLogger(GetTransactionsResultsHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("received: " + input);
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Powered-By", "AWS Lambda & serverless");
        headers.put("Access-Control-Allow-Origin", "*");


        List<Answer> tx;
        try {
            Map<String, String> pathParameters = (Map<String, String>) input.get("pathParameters");
            String user_id = pathParameters.get("user_id");
            LOG.info("Getting results for " + user_id);
            tx = DynamoDBAdapter.getInstance().getResult(user_id);
            LOG.debug("Question returns: " + tx);
        } catch (Exception e) {
            LOG.error(e, e);
            Response responseBody = new Response("Failure getting transactions", input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(headers)
                    .build();
        }
        int score = 0;
        for (Answer a : tx) {


            try {
                List<Question> transactions = DynamoDBAdapter.getInstance().getTransactions(a.getQuestion_id());
                if (a.getAnswer().equals(transactions.get(0).getRightAnswer())) {
                    score++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setObjectBody("{\"score\": "+score+"}")
                .setHeaders(headers)
                .build();
    }
}

