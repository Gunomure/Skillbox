package main;

import org.bson.Document;

import java.util.List;

public interface DataBase {
    public void addShop(String name);

    public void addProduct(String name, int price);

    public void joinProductShop(String productName, String shopName);

    public List<Document> getFullStatistics();

    public void printShops();

    public void printProducts();

    public List<Document> getShops();

    public List<Document> getProducts();
}
