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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;

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
        pm.parseProduct("D,101,Tea,1.99,0,2021-04-21");
        pm.parseReview("101,4,Nice hot cup of tea");
        pm.parseReview("101,2,Rather weak tea");
        pm.parseReview("101,4,Fine tea");
        pm.parseReview("101,4,Good tea");
        pm.parseReview("101,5,Perfect tea");
        pm.parseReview("101,3,Just add some lemon");
        pm.printProductReport(101);

        pm.parseProduct("D,102,Coffee,1.99,0,");
        pm.parseReview("102,3,Coffee was ok");
        pm.parseReview("102,1,Where is the milk?!");
        pm.parseReview("102,5,It's perfect with ten spoons of sugar!");
        pm.printProductReport(102);
        pm.parseProduct("F,103,Cake,3.99,0,"+LocalDate.now().format(DateTimeFormatter.ISO_DATE));

        pm.parseReview("103,5,Very nice cake");
        pm.parseReview("103,4,It's good, but I've expected more chocolate?!");
        pm.parseReview("103,5,This cake is perfect!");
        pm.printProductReport(103);
        pm.parseProduct("F,104,Cookie,2.99,0,"+LocalDate.now().format(DateTimeFormatter.ISO_DATE));
        pm.parseReview("104,3,Just another cookie");
        pm.parseReview("104,3,Ok");
        pm.printProductReport(104);
        pm.parseProduct("D,105,Hot Chocolate,2.50,0,");
        pm.parseReview("105,4,Tasty!");
        pm.parseReview("105,4,Not bad at all");
        pm.printProductReport(105);
        pm.parseProduct("F,106,Chocolate,2.50,0,"+ LocalDate.now().plusDays(3).format(DateTimeFormatter.ISO_DATE));
        pm.parseReview("106,2,Too sweet");
        pm.parseReview("106,3,Better than cookie");
        pm.parseReview("106,2,Too bitter");
        pm.parseReview("106,1,I don't get it!");
        pm.printProductReport(106);

        // Invalid string, not enough values
//        pm.parseProduct("D,109,Tea,1.99,0");
        // Invalid date
//        pm.parseProduct("F,103,Cake,3.99,0,2021-04-46");

//        Comparator<Product> ratingSorter = (p1, p2) -> p2.getRating().ordinal() - p1.getRating().ordinal();
//        pm.printProducts(p -> p.getPrice().doubleValue() < 3, ratingSorter);
//        Comparator<Product> priceSorter = (p1, p2) -> p2.getPrice().compareTo(p1.getPrice());
//
//        // Comparator.comparing can be used if the above sorter was written as
//        // (p1, p2) -> p1.getPrice().compareTo(p2.getPrice())
//        Comparator<Product> priceSorter2 = Comparator.comparing(Product::getPrice);
//        pm.printProducts(p -> p.getPrice().doubleValue() < 2, priceSorter2);
//
//        pm.printProducts(p -> p.getPrice().doubleValue() < 4, ratingSorter.thenComparing(priceSorter).reversed());
//
//        pm.getDiscounts().forEach((rating, discount) -> System.out.println(rating+"\t"+discount));

    }
}