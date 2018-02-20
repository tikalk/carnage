package com.serverless.db;

import com.serverless.data.Answer;
import com.serverless.data.Question;
import junit.framework.TestCase;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by mpandit on 4/13/17.
 */
public class DynamoDBAdapterTest extends TestCase {

    private final Logger logger = Logger.getLogger(this.getClass());

    public void testCRUD(){
        try {
            DynamoDBAdapter adapter = DynamoDBAdapter.getInstance();
            adapter.createTransationsTable();
            for (int i = 0; i < 10; i++) {
                adapter.putTransaction(new Answer().setAnswer_id("123"));
            }
            //now we read
            List<Question> results = adapter.getTransactions("1234");
            logger.info("Got " + results.size() + " transactions!");
            assertTrue(results.size()>0);
        }catch(Exception e){
            fail();
        }
    }

}
