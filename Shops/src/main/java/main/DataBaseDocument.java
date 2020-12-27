package main;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;


public class DataBaseDocument implements DataBase {
    private final static int DEFAULT_MONGO_PORT = 27017;
    private MongoDatabase database;
    private MongoCollection<Document> shops;
    private MongoCollection<Document> products;

    public DataBaseDocument() {
        init(DEFAULT_MONGO_PORT);
    }

    public DataBaseDocument(int mongoPort) {
        init(mongoPort);
    }

    private void init(int mongoPort) {
        MongoClient mongoClient = new MongoClient(String.format("127.0.0.1:%d", mongoPort));
        database = mongoClient.getDatabase("local");
        shops = database.getCollection("shops");
        products = database.getCollection("products");
        shops.drop();
        products.drop();
    }

    public void addShop(String name) {
        List<Document> products = new ArrayList<>();
        Document shop = new Document()
                .append("name", name)
                .append("products", products);
        shops.insertOne(shop);
    }

    public void addProduct(String name, int price) {
        products.insertOne(new Document()
                .append("name", name)
                .append("price", price));
    }

    public void joinProductShop(String productName, String shopName) {
        Optional<Document> product = Optional.ofNullable(products.find(eq("name", productName)).first());
        product.ifPresent(document -> shops.findOneAndUpdate(eq("name", shopName),
                Updates.pushEach("products", Collections.singletonList(document))));
    }

    public List<Document> getFullStatistics() {
        Bson unwind = Aggregates.unwind("$products");
        Bson sort = Aggregates.sort(Sorts.ascending("products.price"));
        Bson addFields = Aggregates.addFields(new Field("n",
                new Document("$cond",
                        new Document("if", new Document("$gte", Arrays.asList("$products.price", 100L)))
                                .append("then", 0L)
                                .append("else", 1L))));
        Bson group = Aggregates.group(
                "$name",
                Accumulators.avg("average_price", "$products.price"),
                Accumulators.sum("count_products", 1L),
                Accumulators.sum("count_products_less_100", "$n"),
                Accumulators.first("min_price", "$products"),
                Accumulators.last("max_price", "$products"));

        return mapToList(shops
                .aggregate(Arrays.asList(unwind, sort, addFields, group))
                .into(new ArrayList<>()));
    }

    public void printShops() {
        FindIterable<Document> lshops = shops.find();
        lshops.forEach((Consumer<Document>) System.out::println);
    }

    public List<Document> getShops() {
        return mapToList(shops.find());
    }

    public void printProducts() {
        FindIterable<Document> lproducts = this.products.find();
        lproducts.forEach((Consumer<Document>) System.out::println);
    }

    public List<Document> getProducts() {
        return mapToList(this.products.find());
    }

    private List<Document> mapToList(Iterable<Document> it) {
        List<Document> res = new ArrayList<>();
        it.iterator().forEachRemaining(res::add);
        return res;
    }
}
