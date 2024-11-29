package com.telecom.scheduler;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
public class RewardPointSchedulerTest {

    @Mock
    private MongoClient mongoClient;

    @Mock
    private MongoDatabase mongoDatabase;

    @Mock
    private MongoCollection<Document> retailOrderCollection;

    @Mock
    private MongoCollection<Document> orderRewardsCollection;

    @Mock
    private FindIterable<Document> findIterable;

    @Mock
    private MongoCursor<Document> mongoCursor;

    @Mock
    private Environment env;

    @InjectMocks
    private RewardPointScheduler rewardPointScheduler;

    @BeforeEach
    void setUp() 
    {
        when(env.getProperty("mongo.db.uri")).thenReturn("mongodb://localhost:27017");
        when(env.getProperty("mongo.db.name")).thenReturn("telecomDb");
        when(env.getProperty("mongo.db.retail.order.collection")).thenReturn("retailOrders");
        when(env.getProperty("mongo.db.order.reward.collection")).thenReturn("orderRewards");
        when(env.getProperty("retail.total.rewards.calculate.days")).thenReturn("30");
        when(mongoClient.getDatabase(anyString())).thenReturn(mongoDatabase);
        when(mongoDatabase.getCollection(anyString())).thenReturn(retailOrderCollection).thenReturn(orderRewardsCollection);
        when(retailOrderCollection.find(any(Document.class))).thenReturn(findIterable);
        when(findIterable.iterator()).thenReturn(mongoCursor);
        Document document = mock(Document.class);
        when(mongoCursor.hasNext()).thenReturn(true, false);  // simulate one document being returned
        when(mongoCursor.next()).thenReturn(document);
        JsonNode jsonNode = mock(JsonNode.class);
        when(document.toJson()).thenReturn("{\"retailerName\": \"RetailerA\", \"rewardPoints\": 100}");
        try 
        {
            when(new ObjectMapper().readTree(document.toJson())).thenReturn(jsonNode);
            when(jsonNode.get("retailerName")).thenReturn(mock(JsonNode.class));
            when(jsonNode.get("rewardPoints")).thenReturn(mock(JsonNode.class));
            when(jsonNode.get("retailerName").toString()).thenReturn("\"RetailerA\"");
            when(jsonNode.get("rewardPoints").toString()).thenReturn("100");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    @Test
    void testScheduleTask() 
    {
        DeleteResult deleteResult = mock(DeleteResult.class);
        when(deleteResult.getDeletedCount()).thenReturn(1L);
        when(orderRewardsCollection.deleteOne(any(BasicDBObject.class))).thenReturn(deleteResult);
        rewardPointScheduler.scheduleTask();
        verify(retailOrderCollection, times(1)).find(any(Document.class));
        verify(orderRewardsCollection, times(1)).insertOne(any(Document.class));
        ArgumentCaptor<Document> documentCaptor = ArgumentCaptor.forClass(Document.class);
        verify(orderRewardsCollection).insertOne(documentCaptor.capture());
        Document capturedDoc = documentCaptor.getValue();
        assertNotNull(capturedDoc);
        assertEquals("RetailerA", capturedDoc.get("retailerName"));
        assertEquals(100.0, capturedDoc.get("totalRewardPoints"));
    }

    @Test
    void testScheduleTask_NoOrdersFound() 
    {
        when(mongoCursor.hasNext()).thenReturn(false);
        rewardPointScheduler.scheduleTask();
        verify(orderRewardsCollection, never()).insertOne(any(Document.class));
    }

    @Test
    void testScheduleTask_WithMultipleOrdersForSameRetailer() 
    {
        Document document1 = mock(Document.class);
        Document document2 = mock(Document.class);
        when(mongoCursor.hasNext()).thenReturn(true, true, false);
        when(mongoCursor.next()).thenReturn(document1).thenReturn(document2);
        JsonNode jsonNode1 = mock(JsonNode.class);
        JsonNode jsonNode2 = mock(JsonNode.class);
        when(document1.toJson()).thenReturn("{\"retailerName\": \"RetailerA\", \"rewardPoints\": 50}");
        when(document2.toJson()).thenReturn("{\"retailerName\": \"RetailerA\", \"rewardPoints\": 70}");
        try 
        {
            when(new ObjectMapper().readTree(document1.toJson())).thenReturn(jsonNode1);
            when(new ObjectMapper().readTree(document2.toJson())).thenReturn(jsonNode2);
            when(jsonNode1.get("retailerName").toString()).thenReturn("\"RetailerA\"");
            when(jsonNode1.get("rewardPoints").toString()).thenReturn("50");
            when(jsonNode2.get("retailerName").toString()).thenReturn("\"RetailerA\"");
            when(jsonNode2.get("rewardPoints").toString()).thenReturn("70");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        rewardPointScheduler.scheduleTask();
        verify(orderRewardsCollection, times(1)).insertOne(any(Document.class));
        ArgumentCaptor<Document> documentCaptor = ArgumentCaptor.forClass(Document.class);
        verify(orderRewardsCollection).insertOne(documentCaptor.capture());
        Document capturedDoc = documentCaptor.getValue();
        assertNotNull(capturedDoc);
        assertEquals("RetailerA", capturedDoc.get("retailerName"));
        assertEquals(120.0, capturedDoc.get("totalRewardPoints"));  // 50 + 70 = 120
    }
}
