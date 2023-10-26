package org.example.warehouse;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Category {
    // Category class holds the categories in a hashmap to make sure that there is only one of each category
    private static final Map<String, Category> categories = new HashMap<>();
    // Category name
    private final String name;

    // Private constructor called only in the static method of()
    private Category(String name) {
        this.name = name;
    }

    // Static method for creating new categories
    public static Category of(String name) {
        // Null check
        if (name == null) throw new IllegalArgumentException("Category name can't be null");

        // Capitalize the first letter
        String capitalized = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();

        // Check if the category already exists, otherwise create it. If its already in the map just return it
        return categories.computeIfAbsent(capitalized, Category::new);
    }

    // Getter for the name
    public String getName() {
        return name;
    }
}
