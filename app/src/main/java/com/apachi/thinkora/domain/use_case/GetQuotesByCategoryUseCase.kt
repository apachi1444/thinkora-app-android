package com.apachi.thinkora.domain.use_case

import com.apachi.thinkora.data.local.dao.QuoteDao
import com.apachi.thinkora.domain.model.Quote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetQuotesByCategoryUseCase @Inject constructor(
    private val quoteDao: QuoteDao
) {
    operator fun invoke(category: String): Flow<List<Quote>> {
        return quoteDao.getQuotesByCategories(listOf(category)).map { entities ->
            entities.map { entity ->
                Quote(
                    id = entity.id,
                    content = entity.content,
                    author = entity.author,
                    category = entity.category,
                    isFavorite = entity.isFavorite,
                    isRead = entity.isRead
                )
            }
        }
    }
}
