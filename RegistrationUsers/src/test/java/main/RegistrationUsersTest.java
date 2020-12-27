package main;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class RegistrationUsersTest {

    //    private static final int MONGO_PORT = 27017;
////    final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("scalar4eg/skill-mongo-with-hacker:latest").asCompatibleSubstituteFor("mongo"));
//
//    @ClassRule
//    public static GenericContainer<?> mongoDBContainer = new GenericContainer<>("scalar4eg/skill-mongo-with-hacker:latest")
//            .withExposedPorts(MONGO_PORT);
//
//    @Test
//    public void testIsRunning() {
////        assertFalse("Container is not started and not running", mongoDBContainer.isRunning());
////        mongoDBContainer.start();
//        assertTrue("Container is started and running", mongoDBContainer.isRunning());
////        mongoDBContainer.close();
//    }
//
//    @Test
//    public void simpleMongoDbTest() {
//        MongoClient mongoClient = new MongoClient(mongoDBContainer.getHost(), mongoDBContainer.getMappedPort(MONGO_PORT));
//        MongoDatabase database = mongoClient.getDatabase("test");
//        MongoCollection<Document> collection = database.getCollection("testCollection");
//
//        Document doc = new Document("name", "foo")
//                .append("value", 1);
//        collection.insertOne(doc);
//
//        Document doc2 = collection.find(new Document("name", "foo")).first();
//        assertEquals("A record can be inserted into and retrieved from MongoDB", 1, doc2.get("value"));
//    }
    private RedisStorage redisStorage;
    private static final int REDIS_PORT = 6379;

    @ClassRule
    public static GenericContainer<?> redisContainer = new GenericContainer<>("redis:latest");

    @Before
    public void setUp() throws Exception {
        System.out.println("start");
        redisStorage = new RedisStorage();
        redisStorage.init(redisContainer.getMappedPort(REDIS_PORT));
    }

    @Test
    public void isRunningTest() {
        assertTrue("Container is started and running", redisContainer.isRunning());
    }

    @Test
    public void addUserTest() {
        redisStorage.registerUser(0);
        redisStorage.registerUser(1);
        redisStorage.registerUser(2);
        redisStorage.registerUser(3);
        String user = redisStorage.showUser();
        assertEquals("0", user);
    }

    @Test
    public void showRandomUser() {
        redisStorage.registerUser(0);
        redisStorage.registerUser(1);
        redisStorage.registerUser(2);
        redisStorage.registerUser(3);
        String user = redisStorage.showUser("2");
        assertEquals("2", user);
        assertEquals("0", redisStorage.showUser());
    }

    @Test
    public void moveUserToBeginTest() {
        redisStorage.registerUser(0);
        redisStorage.registerUser(1);
        redisStorage.registerUser(2);
        redisStorage.registerUser(3);
        redisStorage.moveUserToBegin("3");
        assertEquals("3", redisStorage.showUser());
    }

    @Test
    public void moveUserToEndTest() {
        redisStorage.registerUser(0);
        redisStorage.registerUser(1);
        redisStorage.registerUser(2);
        redisStorage.registerUser(3);
        redisStorage.moveUserToEnd("0");
        redisStorage.printAll();
        assertEquals("1", redisStorage.showUser());
    }

    @After
    public void endTest() {
        System.out.println("end");
        redisStorage.shutdown();
    }
}
