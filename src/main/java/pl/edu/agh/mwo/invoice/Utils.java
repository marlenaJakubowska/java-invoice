package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;

public class Utils {
    public static BigDecimal validatePrice(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        } else if (price.signum() == -1) {
            throw new IllegalArgumentException("Price cannot be less than zero");
        } else
            return price;
    }

    public static String validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        } else if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        } else
            return name;
    }
}
