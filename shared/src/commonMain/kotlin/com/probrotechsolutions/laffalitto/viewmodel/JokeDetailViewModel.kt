package com.probrotechsolutions.laffalitto.viewmodel

import com.probrotechsolutions.laffalitto.model.local.jokes.Joke
import com.probrotechsolutions.laffalitto.model.repositories.JokeRepositoryInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class JokeDetailViewModel(
    private val category: String,
    private val jokeRepo: JokeRepositoryInterface
) : ViewModel() {

    private val _state: MutableStateFlow<JokeDetailState> = MutableStateFlow(JokeDetailState())
    val state get() = _state.asStateFlow()

    init {
        fetchJoke()
    }

    fun fetchJoke() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, errorMsg = "") }
        val result = jokeRepo.getJoke(category)
        when {
            result.isSuccess -> {
                _state.update {
                    it.copy(isLoading = false, joke = result.getOrNull())
                }
            }
            else -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMsg = result.exceptionOrNull()?.message ?: "Unknown Error"
                    )
                }
            }
        }
    }
}

data class JokeDetailState(
    val isLoading: Boolean = false,
    val joke: Joke? = null,
    val errorMsg: String = ""
)
