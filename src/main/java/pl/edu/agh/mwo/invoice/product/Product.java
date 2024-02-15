package pl.edu.agh.mwo.invoice.product;

import pl.edu.agh.mwo.invoice.Utils;
import java.math.BigDecimal;

import static pl.edu.agh.mwo.invoice.Utils.validateName;

public abstract class Product {
    private final String name;

    private final BigDecimal price;

    private final BigDecimal taxPercent;

    protected Product(String name, BigDecimal price, BigDecimal tax) {
        this.name = validateName(name);
        this.price = Utils.validatePrice(price);
        this.taxPercent = tax;
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public BigDecimal getTaxPercent() {
        return this.taxPercent;
    }

    public BigDecimal getPriceWithTax() {
        return this.price.add(getTaxAmount());
    }

    public BigDecimal getTaxAmount() {
        return this.price.multiply(taxPercent);
    }
}
