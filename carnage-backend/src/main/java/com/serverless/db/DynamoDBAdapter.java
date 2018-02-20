package com.serverless.db;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.serverless.data.Answer;
import com.serverless.data.Question;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamoDBAdapter {

    private Logger logger = Logger.getLogger(this.getClass());

    private final static DynamoDBAdapter adapter = new DynamoDBAdapter();

    private final AmazonDynamoDB client;

    private DynamoDBAdapter() {
        client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration("https://dynamodb.us-east-1.amazonaws.com", "us-east-1"))
                .build();
        logger.info("Created DynamoDB client");
    }

    public static DynamoDBAdapter getInstance(){
        return adapter;
    }

    public List<Question> getTransactions(String questionId) throws IOException {
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        Map<String, AttributeValue> vals = new HashMap<>();
        vals.put(":val1",new AttributeValue().withS(questionId));
        DynamoDBQueryExpression<Question> queryExpression = new DynamoDBQueryExpression<Question>()
                .withKeyConditionExpression("question_id = :val1 ")
                .withExpressionAttributeValues(vals);
        return mapper.query(Question.class, queryExpression);
    }


    public void putTransaction(Answer answer) throws IOException{
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        mapper.save(answer);
    }

    void createTransationsTable() throws IOException{
        if(!client.describeTable("question_table").getTable().getTableStatus().equals("ACTIVE")) {
            DynamoDBMapper mapper = new DynamoDBMapper(client);
            CreateTableRequest req = mapper.generateCreateTableRequest(Question.class).withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            CreateTableResult result = client.createTable(req);
            logger.info("Table created " + result.getTableDescription());
        }
    }

}
