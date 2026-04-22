package com.apachi.auraskin.feature.skin

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SkinViewModel @Inject constructor(
    // private val getSkinLogsUseCase: GetSkinLogsUseCase,
    // private val addSkinLogUseCase: AddSkinLogUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SkinUiState())
    val uiState: StateFlow<SkinUiState> = _uiState.asStateFlow()

    fun onScoreChanged(score: Int) {
        _uiState.value = _uiState.value.copy(currentScore = score)
    }

    fun onSaveLog() {
        // TODO: Save to repository
    }
}

data class SkinUiState(
    val currentScore: Int = 3,
    val isLoading: Boolean = false,
    val error: String? = null
)
