package in.maithilart.inventory.client.dto;


import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProductResponse {

    private UUID id;
    private String name;
    private String slug;
    private String description;
    private UUID categoryId;
    private UUID merchantId;
    private ProductType type;
    private Map<String, Object> attributes;
    private String imageUrl;
    private boolean isActive;
    private Instant createdAt;
    private List<ProductVariantResponse> variants;

    // Constructors
    public ProductResponse() {}

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public UUID getCategoryId() { return categoryId; }
    public void setCategoryId(UUID categoryId) { this.categoryId = categoryId; }

    public UUID getMerchantId() { return merchantId; }
    public void setMerchantId(UUID merchantId) { this.merchantId = merchantId; }

    public ProductType getType() { return type; }
    public void setType(ProductType type) { this.type = type; }

    public Map<String, Object> getAttributes() { return attributes; }
    public void setAttributes(Map<String, Object> attributes) { this.attributes = attributes; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public List<ProductVariantResponse> getVariants() { return variants; }
    public void setVariants(List<ProductVariantResponse> variants) { this.variants = variants; }

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}
    
}
