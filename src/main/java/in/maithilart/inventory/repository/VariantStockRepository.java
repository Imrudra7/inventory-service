package in.maithilart.inventory.repository;

import in.maithilart.inventory.entity.VariantStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VariantStockRepository extends JpaRepository<VariantStock, UUID> {

    // 1. वेरिएंट आईडी से स्टॉक ढूंढने के लिए (नॉर्मल रीड)
    Optional<VariantStock> findByVariantId(UUID variantId);

    // 2. ⚡ ATOMIC STOCK RESERVATION (High Concurrency Safety)
    // यह सीधे DB लेवल पर बिना रेस-कंडीशन के स्टॉक को रिजर्व करता है।
    // शर्त: available_quantity हमेशा मांगी गई क्वांटिटी से बड़ी या बराबर होनी चाहिए।
    @Modifying
    @Query("UPDATE VariantStock v " +
           "SET v.availableQuantity = v.availableQuantity - :qty, " +
           "    v.reservedQuantity = v.reservedQuantity + :qty " +
           "WHERE v.variantId = :variantId AND v.availableQuantity >= :qty")
    int reserveStockAtomic(@Param("variantId") UUID variantId, @Param("qty") int qty);

    // 3. ✅ CONFIRM RESERVATION (पेमेंट सक्सेस होने पर)
    // reserved_quantity में से स्टॉक को परमानेंटली माइनस कर देता है।
    @Modifying
    @Query("UPDATE VariantStock v " +
           "SET v.reservedQuantity = v.reservedQuantity - :qty " +
           "WHERE v.variantId = :variantId AND v.reservedQuantity >= :qty")
    int confirmReservationAtomic(@Param("variantId") UUID variantId, @Param("qty") int qty);

    // 4. ❌ RELEASE RESERVATION (पेमेंट फेल या आर्डर टाइमआउट होने पर)
    // reserved_quantity से निकालकर वापस available_quantity में डाल देता है।
    @Modifying
    @Query("UPDATE VariantStock v " +
           "SET v.reservedQuantity = v.reservedQuantity - :qty, " +
           "    v.availableQuantity = v.availableQuantity + :qty " +
           "WHERE v.variantId = :variantId AND v.reservedQuantity >= :qty")
    int releaseReservationAtomic(@Param("variantId") UUID variantId, @Param("qty") int qty);
    
    // 5. ➕ RESTOCK / ADD INITIAL STOCK (एडमिन पैनल या नया प्रोडक्ट आने पर)
    @Modifying
    @Query("UPDATE VariantStock v " +
           "SET v.availableQuantity = v.availableQuantity + :qty " +
           "WHERE v.variantId = :variantId")
    int addStockAtomic(@Param("variantId") UUID variantId, @Param("qty") int qty);
    
 // IN क्लॉज का यूज़ करके सारी IDs का डेटा एक बार में निकालो
    List<VariantStock> findByVariantIdIn(List<UUID> variantIds);

	Optional<VariantStock> findByProductIdAndVariantId(UUID productId, UUID variantId);
}