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
        pm.createProduct(101, "Tea", BigDecimal.valueOf(1.99), NOT_RATED);
        pm.printProductReport(101);
        pm.reviewProduct(101, FOUR_STAR, "Nice hot cup of tea");
        pm.reviewProduct(101, TWO_STAR, "Rather weak tea");
        pm.reviewProduct(101, FOUR_STAR, "Fine tea");
        pm.reviewProduct(101, FOUR_STAR, "Good tea");
        pm.reviewProduct(101, FIVE_STAR, "Perfect tea");
        pm.reviewProduct(101, THREE_STAR, "Just add some lemon");
        pm.printProductReport(101);
        pm.createProduct(102, "Coffee", BigDecimal.valueOf(1.99), NOT_RATED);
        pm.reviewProduct(102, THREE_STAR, "Coffee was ok");
        pm.reviewProduct(102, ONE_STAR, "Where is the milk?!");
        pm.reviewProduct(102, FIVE_STAR, "It's perfect with ten spoons of sugar!");
        pm.changeLocale("fr-FR");
        pm.printProductReport(102);
        pm.createProduct(103, "Cake", BigDecimal.valueOf(3.99), NOT_RATED);
        pm.reviewProduct(103, FIVE_STAR, "Very nice cake");
        pm.reviewProduct(103, FOUR_STAR, "It's good, but I've expected more chocolate?!");
        pm.reviewProduct(103, FIVE_STAR, "This cake is perfect!");
        pm.printProductReport(103);
        pm.createProduct(104, "Cookie", BigDecimal.valueOf(2.99), NOT_RATED);
        pm.reviewProduct(104, THREE_STAR, "Just another cookie");
        pm.reviewProduct(104, THREE_STAR, "Ok");
        pm.printProductReport(104);
        pm.createProduct(105, "Hot Chocolate", BigDecimal.valueOf(2.50), NOT_RATED);
        pm.reviewProduct(105, FOUR_STAR, "Tasty!");
        pm.reviewProduct(105, FOUR_STAR, "Not bad at all");
        pm.printProductReport(105);
        pm.createProduct(106, "Chocolate", BigDecimal.valueOf(2.50), NOT_RATED);
        pm.reviewProduct(106, TWO_STAR, "Too sweet");
        pm.reviewProduct(106, THREE_STAR, "Better than cookie");
        pm.reviewProduct(106, TWO_STAR, "Too bitter");
        pm.reviewProduct(106, ONE_STAR, "I don't get it!");
        pm.printProductReport(106);

        pm.printAllProducts();

    }
}