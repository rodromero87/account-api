package com.bank.account.infrastructure.adapter.out.jpa.entity

import com.bank.account.domain.enums.Currency
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "accounts")
class AccountEntity(
    @Id
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    var id: UUID,

    @Column(name = "owner_id", nullable = false, columnDefinition = "BINARY(16)")
    var ownerId: UUID,

    @Column(name = "balance_amount", nullable = false, precision = 19, scale = 2)
    var balanceAmount: BigDecimal,

    @Enumerated(EnumType.STRING)
    @Column(name = "balance_currency", nullable = false, length = 3)
    var balanceCurrency: Currency,

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant
) {}
