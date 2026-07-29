package in.maithilart.inventory.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "variant_stocks", schema = "inventory", indexes = {
		@Index(name = "idx_stock_variant", columnList = "variant_id", unique = true),
		@Index(name = "idx_stock_product", columnList = "product_id") })
public class VariantStock {

	@Id
	private UUID id;

	

	@Column(name = "variant_id", nullable = false, unique = true)
	private UUID variantId;

	@Column(name = "product_id", nullable = false)
	private UUID productId;

	@Column(name = "available_quantity", nullable = false)
	private Integer availableQuantity = 0;

	@Column(name = "reserved_quantity", nullable = false)
	private Integer reservedQuantity = 0;

	@Column(name = "warehouse_code")
	private String warehouseCode = "DEFAULT_BIHAR";

	@Version
	private Long version; // Optimistic Locking

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private Instant createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private Instant updatedAt;

	// --- Constructors ---
	public VariantStock() {
	}

	// --- Getters and Setters ---
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}

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

	public Long getVersion() {
		return version;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	// --- Domain Business Logic Methods ---
	public boolean hasSufficientStock(int reqQty) {
		return this.availableQuantity >= reqQty;
	}

	public void reserveStock(int qty) {
		if (!hasSufficientStock(qty)) {
			throw new IllegalStateException("Insufficient stock for variant: " + this.variantId);
		}
		this.availableQuantity -= qty;
		this.reservedQuantity += qty;
	}

	public void confirmReservation(int qty) {
		if (this.reservedQuantity < qty) {
			throw new IllegalStateException("Invalid state: confirmation qty higher than reservation");
		}
		this.reservedQuantity -= qty;
	}

	public void releaseReservation(int qty) {
		if (this.reservedQuantity < qty) {
			throw new IllegalStateException("Invalid state: release qty higher than reservation");
		}
		this.reservedQuantity -= qty;
		this.availableQuantity += qty;
	}
}