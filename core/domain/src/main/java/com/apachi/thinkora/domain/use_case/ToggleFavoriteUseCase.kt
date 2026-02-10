package com.apachi.thinkora.domain.use_case

import com.apachi.thinkora.domain.repository.QuoteRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val quoteRepository: QuoteRepository
) {
    suspend operator fun invoke(quoteId: String) {
        quoteRepository.toggleFavorite(quoteId)
    }
}
