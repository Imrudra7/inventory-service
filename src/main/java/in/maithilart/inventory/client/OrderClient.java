package in.maithilart.inventory.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import in.maithilart.common.dto.MaithilResponse;
import in.maithilart.inventory.client.dto.OrderResponse;
import in.maithilart.inventory.client.dto.ProductResponse;
import in.maithilart.inventory.config.FeignConfig;

import java.util.List;
import java.util.UUID;

@Component
@FeignClient(name = "order-service", url = "${order.service.url}", configuration = FeignConfig.class)
public interface OrderClient {

	@GetMapping("/order/api/internal/{orderId}")
	MaithilResponse<OrderResponse> getInternalOrder(@PathVariable UUID orderId);
}