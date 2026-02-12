package com.bank.account.domain.model

import java.time.Instant
import java.util.UUID

data class Account(val id: UUID,
                   val owner: UUID,
                   val createAt: Instant= Instant.now(),
                   val updateAt: Instant= Instant.now(),
                   val balance: Balance)
