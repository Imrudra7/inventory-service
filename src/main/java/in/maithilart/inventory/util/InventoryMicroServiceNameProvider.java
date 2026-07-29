package in.maithilart.inventory.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import in.maithilart.common.context.provider.MicroserviceNameProvider;

@Component
public class InventoryMicroServiceNameProvider implements MicroserviceNameProvider {

	@Value("${spring.application.name}")
	private String client;

	@Override
	public String getMicroservicename() {
		// TODO Auto-generated method stub
		return client != null ? client : "MAITHIL-CART";
	}

}
