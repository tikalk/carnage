package com.serverless;

        import com.amazonaws.services.lambda.runtime.Context;
        import com.amazonaws.services.lambda.runtime.RequestHandler;
        import com.fasterxml.jackson.databind.JsonNode;
        import com.fasterxml.jackson.databind.ObjectMapper;
        import com.fasterxml.jackson.databind.node.ObjectNode;
        import com.serverless.data.Answer;
        import com.serverless.data.Question;
        import com.serverless.db.DynamoDBAdapter;
        import org.apache.log4j.Logger;

        import java.util.*;

public class PostQuestionTransactionsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = Logger.getLogger(PostQuestionTransactionsHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("received: " + input);
        //lets get our path parameter for account_id
        Map<String,String> headers = new HashMap<>();
        headers.put("X-Powered-By", "AWS Lambda & serverless");
        headers.put("Access-Control-Allow-Origin", "*");

        try{
            ObjectMapper mapper = new ObjectMapper();
            Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
            String question_id = pathParameters.get("question_id");

            LOG.info("Posting question id: " + question_id);
            Question tx = new Question()
                    .setQuestionId(question_id);
            String bodyText = (String) input.get("body");
			JsonNode body = mapper.readTree(bodyText);
            LOG.info("Posting question body " + bodyText);

            ObjectNode choices = (ObjectNode) body.get("choices");

            Map<String, String> choicesMap = new HashMap<>();

            Iterator<String> stringIterator = choices.fieldNames();

            while (stringIterator.hasNext()){
                String key = stringIterator.next();
                JsonNode next = choices.get(key);
                String value = next.textValue();
                choicesMap.put(key,value);
            }

            String right_answer = body.get("right_answer").asText();
            String question = body.get("question").asText();
			tx.setChoices(choicesMap).setQuestion(question).setRightAnswer(right_answer);
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
