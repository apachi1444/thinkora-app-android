package com.plcoding.widgetswithcompose.domain.use_case

import com.plcoding.widgetswithcompose.domain.repository.QuoteRepository
import javax.inject.Inject

class MarkQuoteAsReadUseCase @Inject constructor(
    private val quoteRepository: QuoteRepository
) {
    suspend operator fun invoke(quoteId: String) {
        quoteRepository.markQuoteAsRead(quoteId)
    }
}
