package in.maithilart.inventory.service;

import in.maithilart.inventory.client.dto.OrderItemResponse;
import in.maithilart.inventory.client.dto.ProductVariantResponse;
import in.maithilart.inventory.dto.InventoryResponse;
import in.maithilart.inventory.dto.StockResponse; // अगर DTO बनाना चाहो तो

import java.util.List;
import java.util.UUID;

public interface IInventoryService {
    
    // नया प्रोडक्ट वेरिएंट बनने पर स्टॉक इनिशियलाइज करने के लिए
    void initializeStock(UUID productId, UUID variantId, int initialQty);
    
    // आर्डर प्लेस होने पर स्टॉक ब्लॉक (Reserve) करने के लिए
    void reserveStock(UUID variantId, int qty);
    
    // पेमेंट सक्सेस होने पर रिजर्व्ड स्टॉक पक्का घटाने के लिए
    void confirmStock(UUID variantId, int qty);
    
    // पेमेंट फेल या आर्डर टाइमआउट होने पर स्टॉक वापस रिलीज़ करने के लिए
    void releaseStock(UUID variantId, int qty);
    
    // करंट स्टॉक चेक करने के लिए
    int getAvailableStock(UUID variantId);

	void handlePaymentSuccess(UUID orderId);

	InventoryResponse getStockDetails(UUID variantId);

	List<InventoryResponse> getAvailableStocksByVariantIds(List<UUID> variantIds);

	List<InventoryResponse> getAvailableStocks();

	void handleProductCreated(String productId, List<ProductVariantResponse> variants);

	void handleProductUpdated(String productId, List<ProductVariantResponse> variants);

	void handleOrderStatusUpdated(List<OrderItemResponse> items);

	void handleOrderCreated(List<OrderItemResponse> items);

}