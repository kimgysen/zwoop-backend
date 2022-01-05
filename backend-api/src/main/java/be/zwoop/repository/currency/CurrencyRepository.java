package be.zwoop.repository.currency;

import be.zwoop.repository.post.PostStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Integer> {
    Optional<CurrencyEntity> findByCurrency(String currency);
}