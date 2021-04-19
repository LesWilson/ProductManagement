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
import java.util.Locale;

import static labs.pm.data.Rating.*;

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
        Product p1 = pm.createProduct(101, "Tea", BigDecimal.valueOf(1.99), NOT_RATED);
        pm.printProductReport(p1);
        p1 = pm.reviewProduct(p1, FOUR_STAR, "Nice hot cup of tea");
        p1 = pm.reviewProduct(p1, TWO_STAR, "Rather weak tea");
        p1 = pm.reviewProduct(p1, FOUR_STAR, "Fine tea");
        p1 = pm.reviewProduct(p1, FOUR_STAR, "Good tea");
        p1 = pm.reviewProduct(p1, FIVE_STAR, "Perfect tea");
        p1 = pm.reviewProduct(p1, THREE_STAR, "Just add some lemon");
        pm.printProductReport(p1);
        Product p2 = pm.createProduct(102, "Coffee", BigDecimal.valueOf(1.99), NOT_RATED);
        p2 = pm.reviewProduct(p2, THREE_STAR, "Coffee was ok");
        p2 = pm.reviewProduct(p2, ONE_STAR, "Where is the milk?!");
        p2 = pm.reviewProduct(p2, FIVE_STAR, "It's perfect with ten spoons of sugar!");
        pm.printProductReport(p2);
        Product p3 = pm.createProduct(103, "Cake", BigDecimal.valueOf(3.99), NOT_RATED);
        p3 = pm.reviewProduct(p3, FIVE_STAR, "Very nice cake");
        p3 = pm.reviewProduct(p3, FOUR_STAR, "It's good, but I've expected more chocolate?!");
        p3 = pm.reviewProduct(p3, FIVE_STAR, "This cake is perfect!");
        pm.printProductReport(p3);
        Product p4 = pm.createProduct(104, "Cookie", BigDecimal.valueOf(2.99), NOT_RATED);
        p4 = pm.reviewProduct(p4, THREE_STAR, "Just another cookie");
        p4 = pm.reviewProduct(p4, THREE_STAR, "Ok");
        pm.printProductReport(p4);
        Product p5 = pm.createProduct(104, "Hot Chocolate", BigDecimal.valueOf(2.50), NOT_RATED);
        p5 = pm.reviewProduct(p5, FOUR_STAR, "Tasty!");
        p5 = pm.reviewProduct(p5, FOUR_STAR, "Not bad at all");
        pm.printProductReport(p5);
        Product p6 = pm.createProduct(104, "Chocolate", BigDecimal.valueOf(2.50), NOT_RATED);
        p6 = pm.reviewProduct(p6, TWO_STAR, "Too sweet");
        p6 = pm.reviewProduct(p6, THREE_STAR, "Better than cookie");
        p6 = pm.reviewProduct(p6, TWO_STAR, "Too bitter");
        p6 = pm.reviewProduct(p6, ONE_STAR, "I don't get it!");
        pm.printProductReport(p6);

        pm.printAllProducts();

    }
}