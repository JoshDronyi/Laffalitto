package com.probrotechsolutions.laffalitto.viewmodel

import com.probrotechsolutions.laffalitto.model.network.KtorClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class TestViewModel() : ViewModel() {


    private val _state: MutableStateFlow<TestState> = MutableStateFlow(TestState(image = null))
    val state: StateFlow<TestState> = _state.asStateFlow()

    fun getImage(image: String) = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        val result = KtorClient().getImage(image)
        _state.update { it.copy(isLoading = false, image = result) }
    }

    override fun onCleared() {
        super.onCleared()
    }

    companion object {
        const val IMAGE_RESULT = "image_result"
        fun create() = TestViewModel()
    }
}


data class TestState(
    val isLoading: Boolean = false,
    val image: ByteArray?,
    val error: String = ""
)