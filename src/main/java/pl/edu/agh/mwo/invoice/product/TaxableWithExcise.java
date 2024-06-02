package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public interface TaxableWithExcise {
    BigDecimal getExciseTax();
    BigDecimal getPriceWithExcise();
}
