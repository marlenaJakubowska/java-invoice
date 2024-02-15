package pl.edu.agh.mwo.invoice;

import pl.edu.agh.mwo.invoice.product.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class Invoice {
    private Collection<Product> products = new ArrayList<>();


    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException();
        }
        products.add(product);
    }

    public void addProduct(Product product, Integer quantity) {
        if(quantity <= 0) {
            throw new IllegalArgumentException("Quantity cannot be less or equal zero");
        }
        for (int i = 0; i < quantity; i++) {
            products.add(product);
        }
    }

    public BigDecimal getTotalBeforeTax() {
        if (products.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalBeforeTax = BigDecimal.ZERO;
        for (Product product : this.products) {
            totalBeforeTax = totalBeforeTax.add(product.getPrice());
        }

        return totalBeforeTax;
    }

    public BigDecimal getTaxAmount() {
        if (products.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal totalTaxAmount = BigDecimal.ZERO;

        for (Product product : this.products) {
            totalTaxAmount = totalTaxAmount.add(product.getTaxAmount());
        }
        return totalTaxAmount;
    }

    public BigDecimal getTotalAfterTax() {
        if (products.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalAfterTax = BigDecimal.ZERO;

        for (Product product : this.products) {
            totalAfterTax = totalAfterTax.add(product.getPriceWithTax());
        }
        return totalAfterTax;
    }
}
