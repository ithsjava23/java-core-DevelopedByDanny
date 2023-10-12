package org.example.warehouse;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {
    // Fields
    private final String name;
    private final List<ProductRecord> products;
    private final List<ProductRecord> changedProducts;

    // Private constructor
    private Warehouse(String name) {
        this.name = name;
        this.products = new ArrayList<>();
        this.changedProducts = new ArrayList<>();
    }

    // If you run getInstance without parameter you will get a Warehouse named "Warehouse"
    public static Warehouse getInstance() {
        return getInstance("Warehouse");
    }

    // getInstance runs the Constructor with name passed in as parameter
    public static Warehouse getInstance(String name) {
        return new Warehouse(name);
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    // Returning an unmodifiable list of products
    public List<ProductRecord> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public ProductRecord addProduct(UUID id, String name, Category category, BigDecimal price) {

        // Checking for errors
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Product name can't be null or empty.");
        if (category == null) throw new IllegalArgumentException("Category can't be null.");
        if (price == null) price = BigDecimal.ZERO;
        if (id == null) id = UUID.randomUUID();
        if (productExists(id)) throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");

        // Create new product
        var product = new ProductRecord(id, name, category, price);

        // Add product to the list of products
        products.add(product);

        // Return the added ProductRecord
        return product;
    }

    // Check if product Id is already present in the list
    private boolean productExists(UUID id) {
        return products.stream()
                .anyMatch(productRecord -> productRecord.uuid() == id);
    }

    // Update product price
    public void updateProductPrice(UUID id, BigDecimal newPrice) {
        for (int i = 0; i < products.size(); i++)
            if (products.get(i).uuid().equals(id)) {
                // Create new product
                var changedProduct = new ProductRecord(
                        products.get(i).uuid()
                        , products.get(i).name()
                        , products.get(i).category()
                        , newPrice);

                // Replace the old product with set method, witch returns the old product and that gets added to the changedProductslist
                changedProducts.add(products.set(i, changedProduct));
                return;
            }
        throw new IllegalArgumentException("Product with that id doesn't exist.");
    }

    public Optional<ProductRecord> getProductById(UUID uuid) {
        return products.stream()
                .filter(p -> p.uuid().equals(uuid))
                .findFirst();
    }

    // Sort by Category
    public Map<Category, List<ProductRecord>> getProductsGroupedByCategories() {
        return products.stream()
                .collect(Collectors.groupingBy(ProductRecord::category));
    }

    // Find products with a specific category
    public List<ProductRecord> getProductsBy(Category category) {
        return products.stream()
                .filter(product -> product.category().equals(category))
                .collect(Collectors.toList());
    }

    // Gets the list of the products that has been changed
    public List<ProductRecord> getChangedProducts() {
        return changedProducts;
    }

}

