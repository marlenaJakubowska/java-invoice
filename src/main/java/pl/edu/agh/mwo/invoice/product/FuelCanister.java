package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public class FuelCanister extends Product implements TaxableWithExcise {
    private static final BigDecimal EXCISE_TAX = new BigDecimal("5.56");

    public FuelCanister(String name, BigDecimal price, BigDecimal tax) {
        super(name, price, tax);
    }

    @Override
    public BigDecimal getExciseTax() {
        return EXCISE_TAX;
    }

    @Override
    public BigDecimal getPriceWithExcise() {
        return super.getPrice().add(getExciseTax());
    }

    @Override
    public BigDecimal getPriceWithTax() {
        return getPriceWithExcise();
    }
}
