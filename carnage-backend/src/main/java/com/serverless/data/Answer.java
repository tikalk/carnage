package com.serverless.data;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@DynamoDBTable(tableName = "answer_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Answer {

    @DynamoDBHashKey(attributeName = "answer_id")
    String answer_id;

    @DynamoDBRangeKey(attributeName = "transaction_date")
    Date transaction_date;

    @DynamoDBAttribute(attributeName = "question_id")
    String question_id;

    @DynamoDBAttribute(attributeName = "answer")
    String answer;

    @DynamoDBAttribute(attributeName = "user_name")
    String userName;

}

