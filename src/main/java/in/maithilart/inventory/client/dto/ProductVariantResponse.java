package in.maithilart.inventory.client.dto;


import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public class ProductVariantResponse {

    private UUID id;
    private String skuCode;
    private BigDecimal price;
    private BigDecimal salePrice;
    private Integer stockQuantity;
    private Map<String, Object> variantAttributes;
    private boolean isActive;

    // Constructors
    public ProductVariantResponse() {}

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getSkuCode() { return skuCode; }
    public void setSkuCode(String skuCode) { this.skuCode = skuCode; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }

    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }

    public Map<String, Object> getVariantAttributes() { return variantAttributes; }
    public void setVariantAttributes(Map<String, Object> variantAttributes) { this.variantAttributes = variantAttributes; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}