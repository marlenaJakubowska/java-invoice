package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {

    private static int lastInvoiceNumber;
    private final int invoiceNumber;
    private final Map<Product, Integer> products = new HashMap<>();

    public Invoice() {
        lastInvoiceNumber++;
        this.invoiceNumber = lastInvoiceNumber;
    }

    public void addProduct(Product product) {
        addProduct(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException();
        }
        products.put(product, quantity);
    }

    public BigDecimal getNetTotal() {
        BigDecimal totalNet = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalNet = totalNet.add(product.getPrice().multiply(quantity));
        }
        return totalNet;
    }

    public BigDecimal getTaxTotal() {
        return getGrossTotal().subtract(getNetTotal());
    }

    public BigDecimal getGrossTotal() {
        BigDecimal totalGross = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalGross = totalGross.add(product.getPriceWithTax().multiply(quantity));
        }
        return totalGross;
    }

    public String getPrintableInvoice() {
        StringBuilder printableInvoice = new StringBuilder();
        printableInvoice.append("Numer faktury: ").append(invoiceNumber).append("\n");
        if (products.isEmpty()) {
            printableInvoice.append("Brak pozycji");
        } else {
            int positionCount = 0;
            for (Map.Entry<Product, Integer> entry : products.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                printableInvoice.append("Pozycja ").append(++positionCount).append(": ")
                        .append(product.getName()).append(", ")
                        .append(quantity).append(" szt., ")
                        .append(product.getPrice()).append("\n");
            }
            printableInvoice.append("Liczba pozycji: ").append(positionCount);
        }
        return printableInvoice.toString();
    }

    public int getInvoiceNumber() {
        return invoiceNumber;
    }
}
