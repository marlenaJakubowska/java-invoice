package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public class BottleOfWine extends Product implements TaxableWithExcise {
    private static final BigDecimal EXCISE_TAX = new BigDecimal("5.56");

    public BottleOfWine(String name, BigDecimal price, BigDecimal tax) {
        super(name, price, tax);
    }

    @Override
    public BigDecimal getExciseTax() {
        return EXCISE_TAX;
    }

    @Override
    public BigDecimal getPriceWithExcise() {
        return getPriceWithTax().add(getExciseTax());
    }

    @Override
    public BigDecimal getPriceWithTax() {
        BigDecimal basePriceWithVat = super.getPrice()
                .multiply(super.getTaxPercent().add(BigDecimal.ONE));
        return basePriceWithVat.add(EXCISE_TAX);
    }
}