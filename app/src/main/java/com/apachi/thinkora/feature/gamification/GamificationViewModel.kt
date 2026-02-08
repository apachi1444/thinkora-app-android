package com.apachi.thinkora.feature.gamification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apachi.thinkora.domain.model.Achievement
import com.apachi.thinkora.domain.repository.GamificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class GamificationViewModel @Inject constructor(
    private val repository: GamificationRepository
) : ViewModel() {

    val achievements: StateFlow<List<Achievement>> = repository.getAchievements()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
