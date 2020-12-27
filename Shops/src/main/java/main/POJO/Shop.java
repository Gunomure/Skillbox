package main.POJO;

import java.util.ArrayList;
import java.util.List;

public class Shop {
    private String name;
    private List<Product> products;

    public Shop(String name) {
        this.name = name;
        products = new ArrayList<>();
    }

    public Shop() {
        products = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    @Override
    public String toString() {
        return "Shop{" +
                "name='" + name + '\'' +
                ", products=" + products +
                '}';
    }
}
