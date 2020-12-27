package main;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class ShopsTest {
    private static final int MONGO_PORT = 27017;
    @ClassRule
    public static GenericContainer<?> mongoDBContainer = new GenericContainer<>("scalar4eg/skill-mongo-with-hacker:latest");

    private DataBase db;

    @Before
    public void setUp() throws Exception {
        db = new DataBaseDocument(mongoDBContainer.getMappedPort(MONGO_PORT));
    }

    @Test
    public void addShopTest() {
        db.addShop("shop1");

        Document doc = new Document("name", "shop1")
                .append("products", new ArrayList<>());
        List<Document> expectedList = new ArrayList<>();
        expectedList.add(doc);

        assertEquals(expectedList, clearId(db.getShops()));
    }

    @Test
    public void addShopWithProductTest() {
        db.addShop("shop1");
        db.addProduct("product1", 100);
        db.joinProductShop("product1", "shop1");

        Document doc = new Document("name", "shop1")
                .append("products", makeList(
                        new Document("name", "product1")
                                .append("price", 100)
                ));
        List<Document> expectedList = new ArrayList<>();
        expectedList.add(doc);

        assertEquals(expectedList, clearId(db.getShops()));
    }

    @Test
    public void addProductTest() {
        db.addProduct("product1", 100);
        Document doc = new Document("name", "product1")
                .append("price", 100);
        assertEquals(makeList(doc), clearId(db.getProducts()));
    }

    @Test
    public void getFullStatisticsTest() {
        db.addShop("shop1");
        db.addProduct("product1", 110);
        db.addProduct("product2", 20);
        db.addProduct("product3", 30);

        db.addShop("shop2");
        db.addProduct("product4", 300);
        db.addProduct("product5", 20);

        db.joinProductShop("product1", "shop1");
        db.joinProductShop("product2", "shop1");
        db.joinProductShop("product3", "shop1");

        db.joinProductShop("product4", "shop2");
        db.joinProductShop("product5", "shop2");

        List<Document> expectedList = new ArrayList<>();
        expectedList.addAll(
                makeList(
                        new Document("_id", "shop1")
                                .append("average_price", 53.333333333333336)
                                .append("count_products", 3)
                                .append("count_products_less_100", 2)
                                .append("min_price", new Document("name", "product2").append("price", 20))
                                .append("max_price", new Document("name", "product1").append("price", 110)),
                        new Document("_id", "shop2")
                                .append("average_price", 160.0)
                                .append("count_products", 2)
                                .append("count_products_less_100", 1)
                                .append("min_price", new Document("name", "product5").append("price", 20))
                                .append("max_price", new Document("name", "product4").append("price", 300))
                )
        );
        
        for (Document item : clearId(db.getFullStatistics())) {
            if (item.get("_id").equals("shop1")) {
                assertEquals(Optional.of(53.333333333333336), Optional.ofNullable(item.getDouble("average_price")));
                assertEquals(Optional.of(3L), Optional.ofNullable(item.getLong("count_products")));
                assertEquals(Optional.of(2L), Optional.ofNullable(item.getLong("count_products_less_100")));
                assertEquals(new Document("name", "product2").append("price", 20), item.get("min_price"));
                assertEquals(new Document("name", "product1").append("price", 110), item.get("max_price"));
            }
            if (item.get("_id").equals("shop2")) {
                assertEquals(Optional.of(160.0), Optional.ofNullable(item.getDouble("average_price")));
                assertEquals(Optional.of(2L), Optional.ofNullable(item.getLong("count_products")));
                assertEquals(Optional.of(1L), Optional.ofNullable(item.getLong("count_products_less_100")));
                assertEquals(new Document("name", "product5").append("price", 20), item.get("min_price"));
                assertEquals(new Document("name", "product4").append("price", 300), item.get("max_price"));
            }
        }
    }

    private List<Document> clearId(List<Document> list) {
        for (Document doc : list) {
            clearId(doc);
        }
        return list;
    }

    private Document clearId(Document item) {
        for (String key : item.keySet()) {
            if (item.get(key) instanceof ObjectId) {
                // иначе ломается итератор
                item.remove(key);
                clearId(item);
                break;
            } else if (item.get(key) instanceof Document) {
                clearId((Document) item.get(key));
            } else if (item.get(key) instanceof ArrayList) {
                clearId((ArrayList) item.get(key));
            }
        }
        return item;
    }

    private List<Document> makeList(Document... args) {
        return new ArrayList<>(Arrays.asList(args));
    }
}
