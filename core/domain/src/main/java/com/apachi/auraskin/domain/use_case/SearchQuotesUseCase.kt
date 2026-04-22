package com.apachi.auraskin.domain.use_case

import com.apachi.auraskin.domain.model.Quote
import com.apachi.auraskin.domain.repository.QuoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchQuotesUseCase @Inject constructor(
    private val repository: QuoteRepository
) {
    operator fun invoke(query: String): Flow<List<Quote>> {
        return repository.searchQuotes(query)
    }
}
