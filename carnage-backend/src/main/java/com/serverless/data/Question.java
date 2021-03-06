package com.serverless.data;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@DynamoDBTable(tableName = "question_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Question {

    @DynamoDBHashKey(attributeName = "question_id")
    String questionId;

    @DynamoDBAttribute(attributeName = "question")
    String question;

    @DynamoDBAttribute(attributeName = "choices")
    Map<String, String> choices;

    @DynamoDBAttribute(attributeName = "right_answer")
    String rightAnswer;
}
