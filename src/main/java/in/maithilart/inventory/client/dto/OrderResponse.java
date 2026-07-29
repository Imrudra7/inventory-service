package in.maithilart.inventory.client.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class OrderResponse {

    private UUID orderId;

    private String status;

    private OrderAddressResponse address;

    private List<OrderItemResponse> items;

    private BigDecimal itemTotalAmount;

    private BigDecimal discountedItemTotalAmount;

    private BigDecimal handlingFee;

    private BigDecimal deliveryPartnerFee;

    private BigDecimal platformFee;

    private BigDecimal toPay;

    private Instant createdAt;
    
    private Instant updatedAt;

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OrderAddressResponse getAddress() {
        return address;
    }

    public void setAddress(OrderAddressResponse address) {
        this.address = address;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }

    public BigDecimal getItemTotalAmount() {
        return itemTotalAmount;
    }

    public void setItemTotalAmount(BigDecimal itemTotalAmount) {
        this.itemTotalAmount = itemTotalAmount;
    }

    public BigDecimal getDiscountedItemTotalAmount() {
        return discountedItemTotalAmount;
    }

    public void setDiscountedItemTotalAmount(BigDecimal discountedItemTotalAmount) {
        this.discountedItemTotalAmount = discountedItemTotalAmount;
    }

    public BigDecimal getHandlingFee() {
        return handlingFee;
    }

    public void setHandlingFee(BigDecimal handlingFee) {
        this.handlingFee = handlingFee;
    }

    public BigDecimal getDeliveryPartnerFee() {
        return deliveryPartnerFee;
    }

    public void setDeliveryPartnerFee(BigDecimal deliveryPartnerFee) {
        this.deliveryPartnerFee = deliveryPartnerFee;
    }

    public BigDecimal getPlatformFee() {
        return platformFee;
    }

    public void setPlatformFee(BigDecimal platformFee) {
        this.platformFee = platformFee;
    }

    public BigDecimal getToPay() {
        return toPay;
    }

    public void setToPay(BigDecimal toPay) {
        this.toPay = toPay;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt= createdAt;
		
	}

}