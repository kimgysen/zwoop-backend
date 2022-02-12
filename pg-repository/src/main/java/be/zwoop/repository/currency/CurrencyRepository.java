package be.zwoop.repository.currency;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Integer> {
    Optional<CurrencyEntity> findByCurrencyCode(String currencyCode);
}
