package com.dcagon.decapay.respositories;
import com.dcagon.decapay.entities.Wallet;
import com.dcagon.decapay.entities.WalletLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletLogRepository extends MongoRepository<WalletLog, String> {
   Optional<List<WalletLog>> findByUuid(String uuid);
   Optional<List<WalletLog>> findByWalletId(String walletId);
   Optional<List<WalletLog>> findByWalletIdOrSourceWalletId(String walletId, String sourceWalletId);
}
