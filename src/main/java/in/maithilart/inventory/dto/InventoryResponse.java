package in.maithilart.inventory.dto;

import java.time.Instant;
import java.util.UUID;

public class InventoryResponse {

    private UUID variantId;
    private UUID productId;
    private Integer availableQuantity;
    private Integer reservedQuantity;
    private String warehouseCode;
    private boolean isOutOfStock; // 💡 फ्रंटएंड के लिए क्विक फ्लैग ("Out of Stock" बैज दिखाने के लिए)
    private Instant updatedAt;

    // --- Constructors ---
    public InventoryResponse() {
    }

    public InventoryResponse(UUID variantId, UUID productId, Integer availableQuantity, 
                             Integer reservedQuantity, String warehouseCode, Instant updatedAt) {
        this.variantId = variantId;
        this.productId = productId;
        this.availableQuantity = availableQuantity;
        this.reservedQuantity = reservedQuantity;
        this.warehouseCode = warehouseCode;
        this.isOutOfStock = availableQuantity <= 0; // ऑटोमैटिक कैलकुलेशन
        this.updatedAt = updatedAt;
    }

    // --- Getters and Setters ---
    public UUID getVariantId() {
        return variantId;
    }

    public void setVariantId(UUID variantId) {
        this.variantId = variantId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
        this.isOutOfStock = availableQuantity <= 0; // री-कैलकुलेट अगर सेटर कॉल हो
    }

    public Integer getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(Integer reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public boolean isOutOfStock() {
        return isOutOfStock;
    }

    public void setOutOfStock(boolean isOutOfStock) {
        this.isOutOfStock = isOutOfStock;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}