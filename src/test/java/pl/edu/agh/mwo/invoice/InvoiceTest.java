package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.mwo.invoice.product.*;

public class InvoiceTest {
    private Invoice invoice;

    @Before
    public void createEmptyInvoiceForTheTest() {
        invoice = new Invoice();
    }

    @Test
    public void testEmptyInvoiceHasEmptySubtotal() {
        Assert.assertEquals(BigDecimal.ZERO, invoice.getNetTotal());
    }

    @Test
    public void testEmptyInvoiceHasEmptyTaxAmount() {
        Assert.assertEquals(BigDecimal.ZERO, invoice.getTaxTotal());
    }

    @Test
    public void testEmptyInvoiceHasEmptyTotal() {
        Assert.assertEquals(BigDecimal.ZERO, invoice.getGrossTotal());
    }

    @Test
    public void testInvoiceSubtotalWithTwoDifferentProducts() {
        Product onions = new TaxFreeProduct("Warzywa", new BigDecimal("10"));
        Product apples = new TaxFreeProduct("Owoce", new BigDecimal("10"));
        invoice.addProduct(onions);
        invoice.addProduct(apples);
        Assert.assertEquals(new BigDecimal("20"), invoice.getNetTotal());
    }

    @Test
    public void testInvoiceSubtotalWithManySameProducts() {
        Product onions = new TaxFreeProduct("Warzywa", BigDecimal.valueOf(10));
        invoice.addProduct(onions, 100);
        Assert.assertEquals(new BigDecimal("1000"), invoice.getNetTotal());
    }

    @Test
    public void testInvoiceHasTheSameSubtotalAndTotalIfTaxIsZero() {
        Product taxFreeProduct = new TaxFreeProduct("Warzywa", new BigDecimal("199.99"));
        invoice.addProduct(taxFreeProduct);
        Assert.assertEquals(invoice.getNetTotal(), invoice.getGrossTotal());
    }

    @Test
    public void testInvoiceHasProperSubtotalForManyProducts() {
        invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        invoice.addProduct(new OtherProduct("Wino", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("310"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasProperTaxValueForManyProduct() {
        // tax: 0
        invoice.addProduct(new TaxFreeProduct("Pampersy", new BigDecimal("200")));
        // tax: 8
        invoice.addProduct(new DairyProduct("Kefir", new BigDecimal("100")));
        // tax: 2.30
        invoice.addProduct(new OtherProduct("Piwko", new BigDecimal("10")));
        Assert.assertEquals(new BigDecimal("10.30"), invoice.getTaxTotal());
    }

    @Test
    public void testInvoiceHasProperTotalValueForManyProduct() {
        // price with tax: 200
        invoice.addProduct(new TaxFreeProduct("Maskotki", new BigDecimal("200")));
        // price with tax: 108
        invoice.addProduct(new DairyProduct("Maslo", new BigDecimal("100")));
        // price with tax: 12.30
        invoice.addProduct(new OtherProduct("Chipsy", new BigDecimal("10")));
        Assert.assertEquals(new BigDecimal("320.30"), invoice.getGrossTotal());
    }

    @Test
    public void testInvoiceHasPropoerSubtotalWithQuantityMoreThanOne() {
        // 2x kubek - price: 10
        invoice.addProduct(new TaxFreeProduct("Kubek", new BigDecimal("5")), 2);
        // 3x kozi serek - price: 30
        invoice.addProduct(new DairyProduct("Kozi Serek", new BigDecimal("10")), 3);
        // 1000x pinezka - price: 10
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("50"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasProperTotalWithQuantityMoreThanOne() {
        // 2x chleb - price with tax: 10
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        // 3x chedar - price with tax: 32.40
        invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        // 1000x pinezka - price with tax: 12.30
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("54.70"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithZeroQuantity() {
        invoice.addProduct(new TaxFreeProduct("Tablet", new BigDecimal("1678")), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithNegativeQuantity() {
        invoice.addProduct(new DairyProduct("Zsiadle mleko", new BigDecimal("5.55")), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddingNullProduct() {
        invoice.addProduct(null);
    }

    @Test
    public void testNextInvoiceHasGreaterInvoiceNumber() {
        Invoice invoice2 = new Invoice();
        Assert.assertTrue(invoice2.getInvoiceNumber() > invoice.getInvoiceNumber());
    }

    @Test
    public void testInvoicePrintContainsAnInvoiceNumber() {
        Invoice invoicePrintable = new Invoice();
        Assert.assertTrue(invoicePrintable.getPrintableInvoice().contains("Numer faktury:"));
    }

    @Test
    public void addingSameProductTwiceShouldIncreaseQuantity() {
        Invoice invoice = new Invoice();
        Product product = new DairyProduct("Milk", BigDecimal.TEN);

        invoice.addProduct(product, 2);
        invoice.addProduct(product, 3);

        Assert.assertEquals(1, invoice.getProducts().size());
        Assert.assertEquals(5, invoice.getProducts().get(product).intValue());
    }

    @Test
    public void testBottleOfWinePriceWithExcise() {
        Product wine = new BottleOfWine("Czerwone wino", new BigDecimal("100"), new BigDecimal("0.23"));
        BigDecimal expectedPriceWithTax = new BigDecimal("128.56"); // 100 * 1.23 + 5.56
        Assert.assertEquals(expectedPriceWithTax, wine.getPriceWithTax());
    }

    @Test
    public void testBottleOfWineHasProperExciseTax() {
        BottleOfWine wine = new BottleOfWine("Białe wino", new BigDecimal("150"), new BigDecimal("0.23"));
        BigDecimal expectedExciseTax = new BigDecimal("5.56");
        Assert.assertEquals(expectedExciseTax, wine.getExciseTax());
    }

    @Test
    public void testFuelCanisterPriceWithExcise() {
        Product fuel = new FuelCanister("Benzyna", new BigDecimal("200"), new BigDecimal("0")); // brak podatku procentowego
        BigDecimal expectedPriceWithTax = new BigDecimal("205.56"); // 200 + 5.56
        Assert.assertEquals(expectedPriceWithTax, fuel.getPriceWithTax());
    }

    @Test
    public void testFuelCanisterHasProperExciseTax() {
        FuelCanister fuel = new FuelCanister("Diesel", new BigDecimal("300"), new BigDecimal("0"));
        BigDecimal expectedExciseTax = new BigDecimal("5.56");
        Assert.assertEquals(expectedExciseTax, fuel.getExciseTax());
    }
}
