package com.apachi.thinkora.domain.use_case

import com.apachi.thinkora.domain.repository.QuoteRepository
import javax.inject.Inject

class MarkQuoteAsReadUseCase @Inject constructor(
    private val quoteRepository: QuoteRepository
) {
    suspend operator fun invoke(quoteId: String) {
        quoteRepository.markQuoteAsRead(quoteId)
    }
}
