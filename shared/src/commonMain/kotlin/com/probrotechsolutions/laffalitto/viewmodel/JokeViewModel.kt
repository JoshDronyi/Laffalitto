package com.probrotechsolutions.laffalitto.viewmodel

import com.probrotechsolutions.laffalitto.model.local.jokes.JokeCategory
import com.probrotechsolutions.laffalitto.model.repositories.JokeRepositoryInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class JokeViewModel(
    private val jokeRepo: JokeRepositoryInterface
) : ViewModel() {

    private val scope: CoroutineScope = coroutineScope ?: viewModelScope

    private val _jokeState: MutableStateFlow<JokeState> = MutableStateFlow(JokeState())
    val jokeState get() = _jokeState.asStateFlow()

    init {
        getCategories()
    }

    fun getCategories() = scope.launch {
        _jokeState.update {
            it.copy(isLoading = true, errorMsg = "")
        }
        val categories = jokeRepo.getJokeCategories()
        when {
            categories.isSuccess -> {
                _jokeState.update {
                    it.copy(
                        isLoading = false,
                        jokeCategories = categories.getOrDefault(listOf())
                    )
                }
            }

            else -> {
                _jokeState.update {
                    it.copy(
                        isLoading = false,
                        errorMsg = categories.exceptionOrNull()?.message ?: "Unknown Error"
                    )
                }
            }
        }
    }

}


data class JokeState(
    val isLoading: Boolean = false,
    val jokeCategories: List<JokeCategory> = emptyList(),
    val errorMsg: String = ""
)
