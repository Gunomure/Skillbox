package main;

import org.bson.Document;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    private static DataBase db;

    public static void main(String[] args) {
        db = new DataBaseDocument();
        Scanner scan = new Scanner(System.in);
        System.out.println("Write requests");

        while (scan.hasNext()) {
            String request = scan.next().toLowerCase(Locale.ROOT);

            if (request.contains("add_shop")) {
                db.addShop(scan.next());
                System.out.println("The shop successfully added");
            } else if (request.contains("add_product")) {
                db.addProduct(scan.next(), Integer.parseInt(scan.next()));
                System.out.println("The product successfully added");
            } else if (request.contains("submit_product")) {
                db.joinProductShop(scan.next(), scan.next());
                System.out.println("The product successfully submitted in the shop");
            } else if (request.contains("product_statistics")) {
                printStatistics();
            }
        }
    }

    private static void printStatistics() {
        List<Document> stat = db.getFullStatistics();
        for (Document item : stat) {
            String info = String.format(
                    "Shop name - %s\n" +
                            "Average price of a product - %.2f\n" +
                            "Number of products at store, total - %d\n" +
                            "Number of products at store, price<100 - %d\n" +
                            "Min price product - %d\n" +
                            "Max price product - %d",
                    item.get("_id"),
                    item.getDouble("average_price"),
                    item.getLong("count_products"),
                    item.getLong("count_products_less_100"),
                    ((Document) item.get("min_price")).getInteger("price"),
                    ((Document) item.get("max_price")).getInteger("price"));
            System.out.println(info);
        }
    }
}
