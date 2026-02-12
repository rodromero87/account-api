package com.bank.account.domain.out.port

import com.bank.account.domain.model.Account
import java.util.UUID

interface SearchAccountPort {
        fun searchAccount(id: UUID): Account
}