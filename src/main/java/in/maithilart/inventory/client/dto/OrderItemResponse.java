package in.maithilart.inventory.client.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderItemResponse {

    private UUID orderItemId;

    private UUID productId;

    private UUID variantId;

    private String productName;

    private String imageUrl;

    private String variantLabel;

    private Integer quantity;

    private BigDecimal packSize;

    private String uom;

    private BigDecimal unitPrice;

    private BigDecimal discountedPrice;

    private BigDecimal totalAmount;

    private BigDecimal discountedTotalAmount;

    public UUID getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(UUID orderItemId) {
        this.orderItemId = orderItemId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public UUID getVariantId() {
        return variantId;
    }

    public void setVariantId(UUID variantId) {
        this.variantId = variantId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVariantLabel() {
        return variantLabel;
    }

    public void setVariantLabel(String variantLabel) {
        this.variantLabel = variantLabel;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPackSize() {
        return packSize;
    }

    public void setPackSize(BigDecimal packSize) {
        this.packSize = packSize;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(BigDecimal discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDiscountedTotalAmount() {
        return discountedTotalAmount;
    }

    public void setDiscountedTotalAmount(BigDecimal discountedTotalAmount) {
        this.discountedTotalAmount = discountedTotalAmount;
    }

}