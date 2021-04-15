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

import labs.pm.data.Drink;
import labs.pm.data.Food;
import labs.pm.data.Product;

import java.math.BigDecimal;
import java.time.LocalDate;

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
        Product p1 = new Product(101, "Tea", BigDecimal.valueOf(1.99));
        Product p2 = new Drink(102, "Coffee", BigDecimal.valueOf(1.99), FOUR_STAR);
        Product p3 = new Food(103, "Cake", BigDecimal.valueOf(3.99), FIVE_STAR, LocalDate.now().plusDays(2));
        Product p4 = new Product();
        Product p5 = p3.applyRating(THREE_STAR);
        Product p6 = new Drink(104, "Chocolate", BigDecimal.valueOf(2.99), FIVE_STAR);
        Product p7 = new Food(104, "Chocolate", BigDecimal.valueOf(2.99), FIVE_STAR, LocalDate.now().plusDays(2));

        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p3);
        System.out.println(p4);
        System.out.println(p5);
        System.out.println(p6.equals(p7));
    }
}