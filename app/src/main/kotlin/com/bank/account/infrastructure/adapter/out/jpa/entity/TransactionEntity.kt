package com.bank.account.infrastructure.adapter.out.jpa.entity

import com.bank.account.domain.enums.Currency
import com.bank.account.domain.enums.StatusTransaction
import com.bank.account.domain.enums.TypeTransaction
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID


@Entity
@Table(
    name = "transactions",
    indexes = [
        Index(name = "idx_tx_account_ts", columnList = "account_id,timestamp"),
        Index(name = "idx_tx_status", columnList = "status")
    ]
)
class TransactionEntity(@Id
                        @Column(name = "id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
                        var id: UUID,
                        @Column(name = "account_id", nullable = false, columnDefinition = "BINARY(16)")
                        var accountId: UUID,
                        @Enumerated(EnumType.STRING)
                        @Column(name = "type", nullable = false, length = 20)
                        var type: TypeTransaction,
                        @Column(name = "amount", nullable = false, precision = 19, scale = 2)
                        var amount: BigDecimal,
                        @Enumerated(EnumType.STRING)
                        @Column(name = "currency", nullable = false, length = 3)
                        var currency: Currency,
                        @Enumerated(EnumType.STRING)
                        @Column(name = "status", nullable = false, length = 20)
                        var status: StatusTransaction,
                        @Column(name = "timestamp", nullable = false)
                        var timestamp: Instant

) {
}