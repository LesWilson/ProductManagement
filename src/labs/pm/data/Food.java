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
import java.time.LocalDate;

public final class Food extends Product {
    private LocalDate bestBefore;

    Food(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
        super(id, name, price, rating);
        this.bestBefore = bestBefore;
    }

    /**
     * Get the value of bestBefore date for the Food
     * @return the value of bestBefore
     */
    @Override
    public LocalDate getBestBefore() {
        return bestBefore;
    }

    @Override
    public BigDecimal getDiscount() {
        return bestBefore.isEqual(LocalDate.now()) ? super.getDiscount() : BigDecimal.ZERO;
    }

    @Override
    public Product applyRating(Rating newRating) {
        return new Food(getId(), getName(), getPrice(), newRating, bestBefore);
    }

    @Override
    /*
      This is no longer required as we added the bestBefore to the Product toString.
      Training video does not remove it so you get bestBefore displayed twice
     */
    public String toString() {
        return super.toString() + ", bestBefore=" + bestBefore;
    }

}
