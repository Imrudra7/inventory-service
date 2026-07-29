package in.maithilart.inventory.controller;

import in.maithilart.common.constants.MaithilConstants;
import in.maithilart.common.dto.MaithilResponse;
import in.maithilart.inventory.dto.InventoryResponse;
import in.maithilart.inventory.service.IInventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/inventory/api/internal")
public class InventoryController {

	private final IInventoryService inventoryService;

	public InventoryController(IInventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}

	// 1. नया वेरिएंट बनने पर स्टॉक इनिशियलाइज करने के लिए
	@PostMapping("/initialize")
	public ResponseEntity<MaithilResponse<Void>> initializeStock(@RequestParam UUID productId,
			@RequestParam UUID variantId, @RequestParam int initialQty) {

		inventoryService.initializeStock(productId, variantId, initialQty);

		// MaithilResponse.success(code, message, data) का उपयोग किया
		return ResponseEntity.ok(MaithilResponse.success(MaithilConstants.CODE_STOCK_INITIALIZED,
				MaithilConstants.MSG_STOCK_INITIALIZED, null));
	}

	// 2. आर्डर प्लेस होने पर स्टॉक रिजर्व (Block) करने के लिए
	@PostMapping("/variants/{variantId}/reserve")
	public ResponseEntity<MaithilResponse<Void>> reserveStock(@PathVariable UUID variantId,
			@RequestParam int quantity) {

		inventoryService.reserveStock(variantId, quantity);

		return ResponseEntity.ok(MaithilResponse.success(MaithilConstants.CODE_STOCK_RESERVED,
				MaithilConstants.MSG_STOCK_RESERVED, null));
	}

	// 3. पेमेंट सक्सेस होने पर रिजर्व्ड स्टॉक को परमानेंटली कम्प्लीट करने के लिए
	@PostMapping("/variants/{variantId}/confirm")
	public ResponseEntity<MaithilResponse<Void>> confirmStock(@PathVariable UUID variantId,
			@RequestParam int quantity) {

		inventoryService.confirmStock(variantId, quantity);

		return ResponseEntity.ok(MaithilResponse.success(MaithilConstants.CODE_STOCK_CONFIRMED,
				MaithilConstants.MSG_STOCK_CONFIRMED, null));
	}

	// 4. पेमेंट फेल या आर्डर टाइमआउट होने पर स्टॉक वापस रिलीज करने के लिए
	@PostMapping("/variants/{variantId}/release")
	public ResponseEntity<MaithilResponse<Void>> releaseStock(@PathVariable UUID variantId,
			@RequestParam int quantity) {

		inventoryService.releaseStock(variantId, quantity);

		return ResponseEntity.ok(MaithilResponse.success(MaithilConstants.CODE_STOCK_RELEASED,
				MaithilConstants.MSG_STOCK_RELEASED, null));
	}

	// 5. किसी वेरिएंट का करंट अवेलेबल स्टॉक देखने के लिए
	@GetMapping("/variants/{variantId}/stock")
	public ResponseEntity<MaithilResponse<Integer>> getStock(@PathVariable UUID variantId) {
		int availableStock = inventoryService.getAvailableStock(variantId);

		// यहाँ जेनेरिक T की जगह Integer डेटा पास हो रहा है
		return ResponseEntity.ok(MaithilResponse.success(MaithilConstants.CODE_STOCK_FETCHED,
				MaithilConstants.MSG_STOCK_FETCHED, availableStock));
	}

	@PostMapping("/variants/stock/bulk")
	public ResponseEntity<MaithilResponse<List<InventoryResponse>>> getBulkStocks(@RequestBody List<UUID> variantIds) {

		List<InventoryResponse> responses = inventoryService.getAvailableStocksByVariantIds(variantIds);

		return ResponseEntity.ok(MaithilResponse.success(MaithilConstants.CODE_BULK_STOCK_FETCHED,
				MaithilConstants.MSG_BULK_STOCK_FETCHED, responses));
	}
}