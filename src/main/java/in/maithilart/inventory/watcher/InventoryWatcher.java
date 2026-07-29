package in.maithilart.inventory.watcher;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.maithilart.inventory.client.dto.OrderItemResponse;
import in.maithilart.inventory.client.dto.ProductVariantResponse;
import in.maithilart.inventory.service.IInventoryService;
import in.maithilart.common.constants.MaithilConstants;
import in.maithilart.common.context.provider.MicroserviceNameProvider;
import in.maithilart.common.dto.DeliveryRecord;
import in.maithilart.common.event.poller.MaithilEventPoller;
import in.maithilart.common.event.util.JsonUtil;
import in.maithilart.common.event.util.Messenger;
import in.maithilart.common.exception.MaithilException;

@Service
public class InventoryWatcher {

	private final MaithilEventPoller eventPoller;
	private final MicroserviceNameProvider microserviceNameProvider;
	private final Messenger messenger;
	private final IInventoryService inventoryService;

	public InventoryWatcher(MaithilEventPoller eventPoller, Messenger messenger,
			MicroserviceNameProvider microserviceNameProvider, IInventoryService inventoryService) {
		this.eventPoller = eventPoller;
		this.microserviceNameProvider = microserviceNameProvider;
		this.messenger = messenger;
		this.inventoryService = inventoryService;
	}

	@Scheduled(fixedDelay = 3000)
	public void poll() {
		System.out.println(microserviceNameProvider.getMicroservicename() + " Watching 👀⌚⌚⌚");
		List<DeliveryRecord> deliveries = eventPoller
				.pollPendingDeliveries(microserviceNameProvider.getMicroservicename(), 50);
		if (null != deliveries && !deliveries.isEmpty()) {
			System.out.println("👀👀👁️👁️Event Found: " + deliveries);
		}
		for (DeliveryRecord delivery : deliveries) {

			try {
				process(delivery);
				eventPoller.markSuccess(delivery.getDeliveryId());

			} catch (Exception ex) {
				eventPoller.markFailed(delivery, ex.getMessage(),ex);
			}

		}
	}

	private void process(DeliveryRecord delivery) {

		switch (delivery.getEventType()) {

		case MaithilConstants.PAYMENT_SUCCESS -> handlePaymentSuccess(delivery);

		case MaithilConstants.PRODUCT_CREATED -> handleProductCreated(delivery);

		case MaithilConstants.PRODUCT_UPDATED -> handleProductUpdated(delivery);
		
		case MaithilConstants.ORDER_CREATED_EVENT -> handleOrderCreated(delivery);
		
		case MaithilConstants.ORDER_STATUS_UPDATED_EVENT -> handleOrderStatusUpdated(delivery);

		default -> throw new IllegalArgumentException("Unsupported Event : " + delivery.getEventType());
		}
	}

	@Transactional
	private void handleOrderStatusUpdated(DeliveryRecord delivery) {

		String payload = delivery.getPayload();

		Map<String, Object> data = messenger.unpack(payload);


		List<OrderItemResponse> items = JsonUtil.convert(data.get("items"), List.class,OrderItemResponse.class);
		try {
			inventoryService.handleOrderStatusUpdated(items);
		} catch (Exception e) {
			throw new MaithilException(MaithilConstants.FAILED_STATUS, MaithilConstants.MSG_STOCK_UPDATE_FAILED, e);

		}
		System.out.println("Products stock with productId:" + "" + " got created!!");
	}

	@Transactional
	private void handleOrderCreated(DeliveryRecord delivery) {

		String payload = delivery.getPayload();

		Map<String, Object> data = messenger.unpack(payload);



		List<OrderItemResponse> items = JsonUtil.convert(data.get("items"), List.class,OrderItemResponse.class);
		try {
			inventoryService.handleOrderCreated(items);
		} catch (Exception e) {
			throw new MaithilException(MaithilConstants.FAILED_STATUS, MaithilConstants.MSG_STOCK_UPDATE_FAILED, e);

		}
		System.out.println("Products stock with productId:" + "" + " got created!!");
	}

	@Transactional
	private void handleProductCreated(DeliveryRecord delivery) {

		String payload = delivery.getPayload();

		Map<String, Object> data = messenger.unpack(payload);

		String productId = (String) data.get("id");

		// List<ProductVariantResponse> variants = (List<ProductVariantResponse>) data.get("variants");
		List<ProductVariantResponse> variants = JsonUtil.convert(data.get("variants"), List.class,ProductVariantResponse.class);
		try {
			inventoryService.handleProductCreated(productId, variants);
		} catch (Exception e) {
			throw new MaithilException(MaithilConstants.FAILED_STATUS, MaithilConstants.MSG_STOCK_UPDATE_FAILED, e);

		}
		System.out.println("Products stock with productId:" + productId + " got created!!");
	}

	@Transactional
	private void handleProductUpdated(DeliveryRecord delivery) {

		String payload = delivery.getPayload();

		Map<String, Object> data = messenger.unpack(payload);

		String productId = (String) data.get("id");

		//List<ProductVariantResponse> variants = (List<ProductVariantResponse>) data.get("variants");
		List<ProductVariantResponse> variants = JsonUtil.convert(data.get("variants"), List.class,ProductVariantResponse.class);
		try {
			inventoryService.handleProductUpdated(productId, variants);
		} catch (Exception e) {
			throw new MaithilException(MaithilConstants.FAILED_STATUS, MaithilConstants.MSG_STOCK_UPDATE_FAILED, e);

		}
		System.out.println("Products stock with orderId:" + productId + " got updated!!");
	}

	@Transactional
	private void handlePaymentSuccess(DeliveryRecord delivery) {

		String payload = delivery.getPayload();

		Map<String, Object> data = messenger.unpack(payload);

		String orderId = (String) data.get("orderId");
		try {
			inventoryService.handlePaymentSuccess(UUID.fromString(orderId));
		} catch (Exception e) {
			throw new MaithilException(MaithilConstants.FAILED_STATUS, MaithilConstants.MSG_STOCK_UPDATE_FAILED, e);

		}
		System.out.println("Products stock with orderId:" + orderId + " got updated!!");
	}

}
