package in.maithilart.inventory.service.impl;

import in.maithilart.common.constants.MaithilConstants;
import in.maithilart.common.dto.MaithilResponse;
import in.maithilart.common.exception.MaithilException;
import in.maithilart.inventory.client.OrderClient;
import in.maithilart.inventory.client.dto.OrderItemResponse;
import in.maithilart.inventory.client.dto.OrderResponse;
import in.maithilart.inventory.client.dto.ProductVariantResponse;
import in.maithilart.inventory.dto.InventoryResponse;
import in.maithilart.inventory.entity.VariantStock;
import in.maithilart.inventory.repository.VariantStockRepository;
import in.maithilart.inventory.service.IInventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InventoryService implements IInventoryService {

	private final VariantStockRepository stockRepository;
	private final OrderClient orderClient;

	public InventoryService(VariantStockRepository stockRepository, OrderClient orderClient) {
		this.stockRepository = stockRepository;
		this.orderClient = orderClient;
	}

	@Override
	@Transactional
	public void initializeStock(UUID productId, UUID variantId, int initialQty) {
		// चेक करो कहीं इस वेरिएंट का स्टॉक पहले से तो नहीं बना हुआ
		stockRepository.findByVariantId(variantId).ifPresent(s -> {
			throw new IllegalStateException("Stock already initialized for variant: " + variantId);
		});

		VariantStock stock = new VariantStock();
		stock.setId(UUID.randomUUID());
		stock.setProductId(productId);
		stock.setVariantId(variantId);
		stock.setAvailableQuantity(initialQty);
		stock.setReservedQuantity(0);

		stockRepository.save(stock);
	}

	@Override
	@Transactional
	public void reserveStock(UUID variantId, int qty) {
		// ⚡ एटॉमिकली रिजर्व करो और रो अपडेट काउंट चेक करो
		int rowsUpdated = stockRepository.reserveStockAtomic(variantId, qty);

		if (rowsUpdated == 0) {
			// अगर 0 रो अपडेट हुई, मतलब या तो वेरिएंट मिला नहीं या स्टॉक कम था
			throw new RuntimeException("Insufficient stock or variant not found for ID: " + variantId);
		}
	}

	@Override
	@Transactional
	public void confirmStock(UUID variantId, int qty) {
		int rowsUpdated = stockRepository.confirmReservationAtomic(variantId, qty);

		if (rowsUpdated == 0) {
			throw new RuntimeException("Failed to confirm stock reservation. Invalid quantity or ID: " + variantId);
		}
	}

	@Override
	@Transactional
	public void releaseStock(UUID variantId, int qty) {
		int rowsUpdated = stockRepository.releaseReservationAtomic(variantId, qty);

		if (rowsUpdated == 0) {
			throw new RuntimeException("Failed to release stock reservation. Invalid quantity or ID: " + variantId);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public int getAvailableStock(UUID variantId) {
		return stockRepository.findByVariantId(variantId).map(VariantStock::getAvailableQuantity).orElse(0);
	}

	@Override
	@Transactional
	public void handlePaymentSuccess(UUID orderId) {
		// 1. Order Service से आर्डर की कुंडली (Variants और Qty) निकालो
		MaithilResponse<OrderResponse> orderResponse = orderClient.getInternalOrder(orderId);

		if (orderResponse != null && orderResponse.isSuccess() && orderResponse.getData() != null) {
			OrderResponse orderInfo = orderResponse.getData();

			// 2. हर एक आइटम पर लूप चलाओ और इन्वेंट्री टेबल से Reserved Stock को Confirm
			// (माइनस) करो
			orderInfo.getItems().forEach(item -> {
				// यह तुम्हारे लिखे हुए 'confirmReservationAtomic' को कॉल करेगा भाई!
				confirmStock(item.getVariantId(), item.getQuantity());
			});

			System.out.println("🎉 Inventory permanently updated for Order ID: " + orderId);
		} else {
			throw new MaithilException(MaithilConstants.FAILED_STATUS,
					"Failed to fetch order details from Order Service for ID: " + orderId);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public InventoryResponse getStockDetails(UUID variantId) {
		return stockRepository.findByVariantId(variantId)
				.map(stock -> new InventoryResponse(stock.getVariantId(), stock.getProductId(),
						stock.getAvailableQuantity(), stock.getReservedQuantity(), stock.getWarehouseCode(),
						stock.getUpdatedAt()))
				.orElseThrow(() -> new RuntimeException("Stock record not found for variant: " + variantId));
	}

	@Override
	@Transactional(readOnly = true)
	public List<InventoryResponse> getAvailableStocks() {
		return stockRepository.findAll().stream()
				.map(stock -> new InventoryResponse(stock.getVariantId(), stock.getProductId(),
						stock.getAvailableQuantity(), stock.getReservedQuantity(), stock.getWarehouseCode(),
						stock.getUpdatedAt()))
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<InventoryResponse> getAvailableStocksByVariantIds(List<UUID> variantIds) {
		if (variantIds == null || variantIds.isEmpty()) {
			return Collections.emptyList();
		}

		return stockRepository.findByVariantIdIn(variantIds).stream()
				.map(stock -> new InventoryResponse(stock.getVariantId(), stock.getProductId(),
						stock.getAvailableQuantity(), stock.getReservedQuantity(), stock.getWarehouseCode(),
						stock.getUpdatedAt()))
				.toList();
	}

	@Transactional
	@Override
	public void handleProductCreated(String productId, List<ProductVariantResponse> variants) {

		for (ProductVariantResponse variant : variants) {

			stockRepository.findByVariantId(variant.getId()).ifPresent(s -> {
				throw new MaithilException(MaithilConstants.FAILED_STATUS,
						"Stock already initialized for variant: " + variant.getId());
			});

			initializeStock(UUID.fromString(productId), variant.getId(), variant.getStockQuantity());
		}

	}

	@Transactional
	@Override
	public void handleProductUpdated(String productId, List<ProductVariantResponse> variants) {

		if (null == productId || null == variants || variants.isEmpty()) {
			throw new MaithilException(MaithilConstants.FAILED_STATUS,
					"Stock updation after product update got failed, either productId or variants are empty.");
		}

		for (ProductVariantResponse variant : variants) {

			Optional<VariantStock> opStock = stockRepository.findByProductIdAndVariantId(UUID.fromString(productId),
					variant.getId());

			if (opStock.isEmpty()) {
				throw new MaithilException(MaithilConstants.FAILED_STATUS,
						"Stock not found to be updated for variant: " + variant.getId());
			}

			VariantStock stock = opStock.get();
			stock.setAvailableQuantity(variant.getStockQuantity());

			stockRepository.save(stock);
		}

	}

	@Transactional
	@Override
	public void handleOrderStatusUpdated(List<OrderItemResponse> items) {
		if (items == null || items.isEmpty())
			return;

		System.out.println("🔄 Order status changed. Releasing reserved stock...");
		for (OrderItemResponse item : items) {
			int qty = item.getQuantity() != null ? item.getQuantity() : 0;
			if (qty <= 0)
				continue;

			releaseStock(item.getVariantId(), qty);

		}
		System.out.println("✅ Reserved stock released back to available pool successfully!");
	}

	@Transactional
	@Override
	public void handleOrderCreated(List<OrderItemResponse> items) {
		if (items == null || items.isEmpty())
			return;

		System.out.println("⏳ Reserving stock for new order. Total items: " + items.size());
		for (OrderItemResponse item : items) {
			int qty = item.getQuantity() != null ? item.getQuantity() : 0;
			if (qty <= 0)
				continue;

			reserveStock(item.getVariantId(), qty);
		}
		System.out.println("✅ Bulk stock reserved successfully!");
	}
}