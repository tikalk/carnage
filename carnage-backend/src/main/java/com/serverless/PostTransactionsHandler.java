package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.data.Answer;
import com.serverless.db.DynamoDBAdapter;
import org.apache.log4j.Logger;

import java.util.*;

public class PostTransactionsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = Logger.getLogger(PostTransactionsHandler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		LOG.info("received: " + input);
		Map<String,String> headers = new HashMap<>();
		headers.put("X-Powered-By", "AWS Lambda & serverless");
		headers.put("Access-Control-Allow-Origin", "*");

		//lets get our path parameter for account_id
		try{
			ObjectMapper mapper = new ObjectMapper();
			Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
			String question_id = pathParameters.get("question_id");
			String answer = pathParameters.get("answer");
			String user = pathParameters.get("user");
			LOG.info("Posting answer for question " + question_id);
			Answer tx = new Answer()
					.setAnswer_id(UUID.randomUUID().toString())
					.setQuestion_id(question_id)
					.setTransaction_date(new Date())
					.setAnswer(answer)
					.setUserName(user);
//			JsonNode body = mapper.readTree((String) input.get("body"));
//			float amount = (float) body.get("amount").asDouble();
//			String txId = body.get("transaction_id").asText();
//			tx.setAmount(amount);
//			tx.setTransaction_date(new Date(System.currentTimeMillis()));
//			tx.setTransaction_id(txId);
			DynamoDBAdapter.getInstance().putTransaction(tx);
		}catch(Exception e){
			LOG.error(e,e);
			Response responseBody = new Response("Failure putting transaction", input);
			return ApiGatewayResponse.builder()
					.setStatusCode(500)
					.setObjectBody(responseBody)
					.setHeaders(headers)
					.build();
		}

		Response responseBody = new Response("Transaction added successfully!", input);
		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setObjectBody(responseBody)
				.setHeaders(headers)
				.build();
	}
}
