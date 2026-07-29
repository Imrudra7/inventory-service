package in.maithilart.inventory.util;

import org.springframework.beans.factory.annotation.Value;

import in.maithilart.common.context.provider.CommunicatorSecretProvider;

public class InventoryCommSecretProvider implements CommunicatorSecretProvider {

	@Value("${communicator.secret}")
	private String secret;

	@Override
	public String getCommunicatorSecret() {
		return secret == null ? "INVENTORY-COMM_SECRET" : secret;
	}

}
