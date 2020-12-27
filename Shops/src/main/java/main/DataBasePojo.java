package main;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import main.POJO.Product;
import main.POJO.Shop;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class DataBasePojo implements DataBase {

    private final static int DEFAULT_MONGO_PORT = 27017;
    private MongoDatabase database;
    private MongoCollection<Shop> shops;
    private MongoCollection<Product> products;

    public DataBasePojo() {
        init(DEFAULT_MONGO_PORT);

    }

    public DataBasePojo(int mongoPort) {
        init(mongoPort);
    }

    private void init(int mongoPort) {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));


        MongoClient mongoClient = new MongoClient("127.0.0.1:%d".formatted(mongoPort), MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
        database = mongoClient.getDatabase("local").withCodecRegistry(pojoCodecRegistry);
        shops = database.getCollection("shops", Shop.class).withCodecRegistry(pojoCodecRegistry);
        products = database.getCollection("products", Product.class).withCodecRegistry(pojoCodecRegistry);
        shops.drop();
        products.drop();
    }

    public void addShop(String name) {
        shops.insertOne(new Shop(name));
    }

    public void addProduct(String name, int price) {
        products.insertOne(new Product(name, price));
    }

    public void joinProductShop(String productName, String shopName) {
        Optional<Product> product = Optional.ofNullable(products.find(eq("name", productName)).first());
        System.out.printf("add product: %s%n", product.get().getName());
        Optional<Shop> shop = Optional.ofNullable(shops.find(eq("name", shopName)).first());
        if (shop.isPresent()) {
            shop.get().addProduct(product.get());
            shops.replaceOne(eq("name", shopName), shop.get());
        }
//        shops.aggregate(Arrays.asList(
//                Aggregates.lookup(productName, "name", "name", "products")
//        )).forEach((Consumer<? super Shop>) System.out::println);
    }

    public List<Document> getFullStatistics() {
        long count = products.countDocuments();
        String tmp = shops.aggregate(Arrays.asList(
//                unwind("$products", new UnwindOptions().preserveNullAndEmptyArrays(true))
                group("$products", avg("averageQuantity", "price"))

        ))
                .first().toString();
        System.out.println(tmp);
        return null;
    }

    public void printShops() {
        FindIterable<Shop> lshops = shops.find();
        for (Shop item : lshops) {
            System.out.println(item.toString());
        }
    }

    public void printProducts() {
        FindIterable<Product> lshops = products.find();
        for (Product item : lshops) {
            System.out.println(item.toString());
        }
    }

    public List<Document> getShops() {
        return new ArrayList<>();
    }

    public List<Document> getProducts() {
        return new ArrayList<>();
    }

    public List<Document> getStatisticsProducts() {
        return new ArrayList<>();
    }

    public List<Document> getProductsCount() {
        return new ArrayList<>();
    }

    public List<Document> getCountProductsLessVal(int value) {
        return new ArrayList<>();
    }
}
