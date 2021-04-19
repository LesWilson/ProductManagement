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

    private ResourceFormatter formatter;
    private Map<Product, List<Review>> products = new HashMap<>();
    private static Map<String, ResourceFormatter> formatters =
            Map.of("en-GB", new ResourceFormatter(Locale.UK),
                   "en-US", new ResourceFormatter(Locale.US),
                   "fr-FR", new ResourceFormatter(Locale.FRANCE),
                   "ru-RU", new ResourceFormatter(new Locale("ru", "RU")),
                   "zh-CN", new ResourceFormatter(Locale.CHINA));

    public ProductManager(Locale locale) {
        this(locale.toLanguageTag());
    }

    public ProductManager(String languageTag) {
        changeLocale(languageTag);
    }

    public void changeLocale(String languageTag) {
       formatter = formatters.getOrDefault(languageTag, formatters.get("en-GB"));
    }

    public static Set<String> getSupportedLocales() {
        return formatters.keySet();
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

    public Product reviewProduct(int id, Rating rating, String comments) {
        return reviewProduct(findProduct(id), rating, comments);
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

    public Product findProduct(int id) {
        Product result = null;
        for(Product product : products.keySet()) {
            if(product.getId() == id) {
                result = product;
                break;
            }
        }
        return result;
    }

    public void printProductReport(int id) {
        printProductReport(findProduct(id));
    }
    public void printProductReport(Product product) {
        List<Review> reviews = products.get(product);
        StringBuilder txt = new StringBuilder();
        txt.append(formatter.formatProduct(product));
        txt.append("\n");
        Collections.sort(reviews);
        for(Review review : reviews) {
            if (review == null) {
                break;
            }
            txt.append(formatter.formatReview(review));
            txt.append("\n");
        }
        if(reviews.isEmpty()) {
            txt.append(formatter.getText("no.reviews"));
            txt.append("\n");
        }
//        System.out.println(txt);
    }

    public void printProducts(Comparator<Product> sorter) {
        List<Product> productList = new ArrayList<>(products.keySet());
        productList.sort(sorter);
        StringBuilder txt = new StringBuilder();
        for(Product product : productList) {
            txt.append(formatter.formatProduct(product));
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

    private static class ResourceFormatter {
        private Locale locale;
        private ResourceBundle resources;
        private DateTimeFormatter dateFormat;
        private NumberFormat moneyFormat;

        private ResourceFormatter(Locale locale) {
            this.locale = locale;
            dateFormat = DateTimeFormatter.ofLocalizedDate(SHORT).localizedBy(locale);
            moneyFormat = NumberFormat.getCurrencyInstance(locale);
            resources = ResourceBundle.getBundle("labs.pm.data.resources", locale);
        }

        private String formatProduct(Product product) {
            return MessageFormat.format(getText("product"),
                    product.getName(),
                    moneyFormat.format(product.getPrice()),
                    product.getRating().getStars(),
                    dateFormat.format(product.getBestBefore()));
        }

        private String formatReview(Review review) {
            return MessageFormat.format(getText("review"),
                    review.getRating().getStars(),
                    review.getComments());
        }

        private String getText(String key) {
            return resources.getString(key);
        }
    }
}
