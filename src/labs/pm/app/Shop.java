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

package labs.pm.app;

import labs.pm.data.Product;
import labs.pm.data.ProductManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;

import static labs.pm.data.Rating.*;
import static labs.pm.data.Rating.FIVE_STAR;

/**
 * {@code Shop} class represents an application that manages Products
 * This is based on the Java 11 certification course on the Oracle University online platform.
 * @version 2.0
 * @author xyz
 */
public class Shop {

    /**
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ProductManager pm = new ProductManager(Locale.UK);
        pm.printProductReport(101);
        pm.printProductReport(102);
        pm.printProductReport(103);
        pm.printProductReport(104);
        pm.printProductReport(105);
        pm.printProductReport(106);
        pm.printProductReport(107);

        pm.createProduct(164, "Instant Coffee", BigDecimal.valueOf(1.99), NOT_RATED);
        pm.reviewProduct(164, THREE_STAR, "Coffee was ok");
        pm.reviewProduct(164, ONE_STAR, "Where is the milk?!");
        pm.reviewProduct(164, FIVE_STAR, "It's perfect with ten spoons of sugar!");
        pm.printProductReport(164);

        Comparator<Product> ratingSorter = (p1, p2) -> p2.getRating().ordinal() - p1.getRating().ordinal();
        pm.printProducts(p -> p.getPrice().doubleValue() < 3, ratingSorter);
        Comparator<Product> priceSorter = (p1, p2) -> p2.getPrice().compareTo(p1.getPrice());

        // Comparator.comparing can be used if the above sorter was written as
        // (p1, p2) -> p1.getPrice().compareTo(p2.getPrice())
        Comparator<Product> priceSorter2 = Comparator.comparing(Product::getPrice);
        pm.printProducts(p -> p.getPrice().doubleValue() < 2, priceSorter2);

        pm.printProducts(p -> p.getPrice().doubleValue() < 4, ratingSorter.thenComparing(priceSorter).reversed());

        pm.getDiscounts().forEach((rating, discount) -> System.out.println(rating+"\t"+discount));

    }
}