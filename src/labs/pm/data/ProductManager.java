/*
 * Copyright (C) 2020 Oracle
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the license, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 */
package labs.pm.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.time.format.FormatStyle.SHORT;

/**
 * Factory Class that creates either Food or drink instances
 */
public class ProductManager {

    private Locale locale;
    private ResourceBundle resources;
    private DateTimeFormatter dateFormat;
    private NumberFormat moneyFormat;

    private Map<Product, List<Review>> products = new HashMap<>();

    public ProductManager(Locale locale) {
        this.locale = locale;
        dateFormat = DateTimeFormatter.ofLocalizedDate(SHORT).localizedBy(locale);
        moneyFormat = NumberFormat.getCurrencyInstance(locale);
        resources = ResourceBundle.getBundle("labs.pm.data.resources", locale);
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
        Product product = new Food(id, name, price, rating, bestBefore);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
        Product product = new Drink(id, name, price, rating);
        products.putIfAbsent(product, new ArrayList<>());
        // comment
        return product;
    }

    public Product reviewProduct(Product product, Rating rating, String comments) {
        List<Review> reviews = products.get(product);
        products.remove(product, reviews);
        reviews.add(new Review(rating, comments));
        int sum = 0;
        for (Review review : reviews) {
            sum += review.getRating().ordinal();
        }
        product = product.applyRating(Rateable.convert(Math.round((float)sum / reviews.size())));
        products.put(product, reviews);
        return product;
    }

    public void printProductReport(Product product) {
        List<Review> reviews = products.get(product);
        StringBuilder txt = new StringBuilder();
        txt.append(MessageFormat.format(resources.getString("product"),
                product.getName(),
                moneyFormat.format(product.getPrice()),
                product.getRating().getStars(),
                dateFormat.format(product.getBestBefore())));
        txt.append("\n");
        for(Review review : reviews) {
            if (review == null) {
                break;
            }
            txt.append(MessageFormat.format(resources.getString("review"),
                    review.getRating().getStars(),
                    review.getComments()));
            txt.append("\n");
        }
        if(reviews.isEmpty()) {
            txt.append(resources.getString("no.reviews"));
            txt.append("\n");
        }
        System.out.println(txt);
    }

    /**
     * Example of using keySet to loop through products
     */
    public void printAllProducts() {
        System.out.println("**** All Products start");
        for(Product product : products.keySet()) {
            printProductReport(product);
        }
        System.out.println("**** All Products end");
    }
}
