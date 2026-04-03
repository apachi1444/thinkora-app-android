package com.apachi.auraskin.domain.use_case

import com.apachi.auraskin.domain.repository.QuoteRepository
import javax.inject.Inject

class MarkQuoteAsReadUseCase @Inject constructor(
    private val quoteRepository: QuoteRepository
) {
    suspend operator fun invoke(quoteId: String) {
        quoteRepository.markQuoteAsRead(quoteId)
    }
}
