package com.apachi.thinkora.domain.use_case

import com.apachi.thinkora.domain.model.Quote
import com.apachi.thinkora.domain.repository.QuoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuotesByCategoryUseCase @Inject constructor(
    private val repository: QuoteRepository
) {
    operator fun invoke(category: String): Flow<List<Quote>> {
        return repository.getQuotesByCategory(category)
    }
}

