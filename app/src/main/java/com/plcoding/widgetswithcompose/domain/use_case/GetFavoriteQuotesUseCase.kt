package com.plcoding.widgetswithcompose.domain.use_case

import com.plcoding.widgetswithcompose.domain.model.Quote
import com.plcoding.widgetswithcompose.domain.repository.QuoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteQuotesUseCase @Inject constructor(
    private val quoteRepository: QuoteRepository
) {
    operator fun invoke(): Flow<List<Quote>> {
        return quoteRepository.getFavoriteQuotes()
    }
}
