package com.dcagon.decapay.respositories;
import com.dcagon.decapay.entities.Wallet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends MongoRepository<Wallet, String> {
   Optional<Wallet> findByUuid(String uuid);
   Optional<Wallet> findByWalletId(String walletId);
}
