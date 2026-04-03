package com.apachi.auraskin.domain.use_case

import com.apachi.auraskin.domain.model.Quote
import com.apachi.auraskin.domain.repository.QuoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuotesByCategoryUseCase @Inject constructor(
    private val repository: QuoteRepository
) {
    operator fun invoke(category: String): Flow<List<Quote>> {
        return repository.getQuotesByCategory(category)
    }
}

