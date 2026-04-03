package com.apachi.auraskin.domain.use_case

import com.apachi.auraskin.domain.model.Quote
import com.apachi.auraskin.domain.repository.QuoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteQuotesUseCase @Inject constructor(
    private val quoteRepository: QuoteRepository
) {
    operator fun invoke(): Flow<List<Quote>> {
        return quoteRepository.getFavoriteQuotes()
    }
}
