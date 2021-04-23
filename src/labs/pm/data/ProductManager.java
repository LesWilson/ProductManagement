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

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.time.format.FormatStyle.SHORT;

/**
 * Factory Class that creates either Food or drink instances
 */
public class ProductManager {

    private static final Logger logger = Logger.getLogger(ProductManager.class.getName());
    private ResourceFormatter formatter;
    private ResourceBundle config = ResourceBundle.getBundle("labs.pm.data.config");
    private MessageFormat reviewFormat = new MessageFormat(config.getString("review.data.format"));
    private MessageFormat productFormat = new MessageFormat(config.getString("product.data.format"));

    private Path reportsFolder = Path.of(config.getString("reports.folder"));
    private Path dataFolder = Path.of(config.getString("data.folder"));
    private Path tempFolder = Path.of(config.getString("temp.folder"));

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
        try {
            return reviewProduct(findProduct(id), rating, comments);
        } catch (ProductManagerException e) {
            logger.log(Level.INFO, e.getMessage());
        }
        return null;
    }

    public Product reviewProduct(Product product, Rating rating, String comments) {
        List<Review> reviews = products.get(product);
        products.remove(product, reviews);
        reviews.add(new Review(rating, comments));
        // The video structures this code differently, but I don't think it's very readable
        // Better to split out the parts
        double average = reviews.stream()
                .mapToInt(r -> r.getRating().ordinal())
                .average()
                .orElse(0);

        Rating averageRating = Rateable.convert((int) Math.round(average));

        product = product.applyRating(averageRating);

        products.put(product, reviews);
        return product;
    }

    public Product findProduct(int id) throws ProductManagerException{
        return products
                .keySet()
                .stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ProductManagerException("Product with id " + id + " not found"));
    }

    public void printProductReport(int id) {
        try {
            printProductReport(findProduct(id));
        } catch (ProductManagerException e) {
            logger.log(Level.INFO, e.getMessage());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error printing product report " + e.getMessage(), e);
        }
    }
    public void printProductReport(Product product) throws IOException {
        List<Review> reviews = products.get(product);
        Collections.sort(reviews);
        Path productFile = reportsFolder.resolve(MessageFormat.format(config.getString("report.file"), product.getId()));
        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(Files.newOutputStream(productFile, StandardOpenOption.CREATE), StandardCharsets.UTF_8))) {
            out.append(formatter.formatProduct(product));
            out.append(System.lineSeparator());
            if (reviews.isEmpty()) {
                out.append(formatter.getText("no.reviews"));
                out.append(System.lineSeparator());
            } else {
                out.append(
                        reviews.stream()
                                .map(r -> formatter.formatReview(r) + System.lineSeparator())
                                .collect(Collectors.joining())
                );
            }
        }
    }

    public void printProducts(Predicate<Product> filter, Comparator<Product> sorter) {
        List<Product> productList = new ArrayList<>(products.keySet());
        productList.sort(sorter);
        StringBuilder txt = new StringBuilder();
        products.keySet()
                .stream()
                .sorted(sorter)
                .filter(filter)
                .forEach(p -> txt.append(formatter.formatProduct(p) + "\n"));
        System.out.println(txt);
    }

    private Product loadProduct(Path file) {
        Product product = null;
        try {
            product = parseProduct(
                    Files.lines(dataFolder.resolve(file), StandardCharsets.UTF_8)
                            .findFirst().orElseThrow();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not load product " + e.getMessage());
        }
        return product;
    }
    private List<Review> loadReviews(Product product) {
        List<Review> reviews = new ArrayList<>();
        Path file = reportsFolder.resolve(MessageFormat.format(config.getString("reviews.data.file"), product.getId()));
        if (Files.exists(file)) {
            try {
                reviews = Files.lines(file, Charset.forName("UTF-8"))
                        .map(text -> parseReview(text))
                        .filter(review -> review != null)
                        .collect(Collectors.toList());
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error loading reviews " + e.getMessage(), e);
            }
        }
        return reviews;
    }
    public Review parseReview(String text) {
        Review review = null;
        try {
            Object[] values = reviewFormat.parse(text);
            review = new Review(
                    Rateable.convert(Integer.parseInt((String)values[0])),
                    (String)values[1]);
        } catch (ParseException | NumberFormatException e) {
            logger.log(Level.WARNING, "Error parsing review " + text);
        }
        return review;
    }

    public Product parseProduct(String text) {
        Product product = null;
        try {
            Object[] values = productFormat.parse(text);
            int id = Integer.parseInt((String)values[1]);
            String name = (String)values[2];
            BigDecimal price = BigDecimal.valueOf(Double.parseDouble((String)values[3]));
            Rating rating = Rateable.convert(Integer.parseInt((String)values[4]));

            switch ((String)values[0]) {
                case "D":
                    product = new Drink(id, name, price, rating);
                    break;
                case "F":
                    LocalDate bestBefore = LocalDate.parse((String)values[5]);
                    product = new Food(id, name, price, rating, bestBefore);
                    break;
            }
        } catch (ParseException | NumberFormatException | DateTimeException e) {
            logger.log(Level.WARNING, "Error parsing product " + text + " " + e.getMessage());
        }
        return product;
    }

    public Map<String, String> getDiscounts() {
        return products
                .keySet()
                .stream()
                .collect(Collectors.groupingBy(p -> p.getRating().getStars(),
                        Collectors.collectingAndThen(
                                Collectors.summingDouble(p -> p.getDiscount().doubleValue()),
                                discount -> formatter.moneyFormat.format(discount)
                        )));
    }
    /**
     * Example of using keySet to loop through products
     */
//    public void printAllProducts() {
//        System.out.println("**** All Products start");
//        for(Product product : products.keySet()) {
//            printProductReport(product);
//        }
//        System.out.println("**** All Products end");
//    }

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
