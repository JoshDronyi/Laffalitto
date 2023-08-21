package com.probrotechsolutions.laffalitto.viewmodel

import co.touchlab.kermit.Logger
import com.probrotechsolutions.laffalitto.model.local.flags.Flag
import com.probrotechsolutions.laffalitto.model.local.jokeCategory.JokeCategory
import com.probrotechsolutions.laffalitto.model.local.jokes.BaseJoke
import com.probrotechsolutions.laffalitto.model.repositories.JokeRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class JokeViewModel(
    private val jokeRepo: JokeRepository
) : ViewModel() {
    private val TAG = "JokeViewModel"
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Logger.e("Oh no!!! there was an error in $TAG: ${throwable.message} ")
    }
    private val _jokeState: MutableStateFlow<JokeState> = MutableStateFlow(JokeState())

    private val selectedFlags: MutableList<Flag> = mutableListOf()
    val jokeState get() = _jokeState.asStateFlow()

    init {
        viewModelScope.launch {
            getCategories()
            getFlags()
        }
    }

    private fun getFlags() = viewModelScope.launch {
        _jokeState.update {
            it.copy(isLoading = true)
        }
        val response = jokeRepo.getFlags()
        when {
            response.isSuccess -> {
                _jokeState.update {
                    val list = response.getOrNull()
                    it.copy(
                        isLoading = false,
                        jokeCategoryUiModel = it.jokeCategoryUiModel.copy(
                            flags = list ?: emptyList()
                        )
                    )
                }
            }

            else -> {
                _jokeState.update {
                    it.copy(
                        isLoading = false,
                        errorMsg = response.exceptionOrNull()?.message ?: "UnKnown Error"
                    )
                }
            }
        }
    }

    private fun getCategories() = viewModelScope.launch {
        _jokeState.update {
            it.copy(isLoading = true)
        }
        val categories = jokeRepo.getJokeCategories()
        when {
            categories.isSuccess -> {
                _jokeState.update {
                    it.copy(
                        isLoading = false,
                        jokeCategoryUiModel = it.jokeCategoryUiModel.copy(
                            jokeCategories = categories.getOrDefault(
                                listOf()
                            )
                        )
                    )
                }
            }

            else -> {
                _jokeState.update {
                    it.copy(
                        isLoading = false,
                        errorMsg = categories.exceptionOrNull()?.message ?: "UnKnown Error"
                    )
                }
            }
        }
    }

    fun getJokes(category: String) = viewModelScope.launch {
        _jokeState.update {
            it.copy(isLoading = true)
        }
        val jokeResult = jokeRepo.getJoke(category, *selectedFlags.toTypedArray())
        if (jokeResult.isSuccess) {
            val jokes = jokeResult.getOrNull()
            Logger.e("Jokes was $jokes")
            jokes?.let { jawn ->
                _jokeState.update {
                    it.copy(
                        isLoading = false,
                        jokeCategoryUiModel = it.jokeCategoryUiModel.apply {
                            this.jokes.add(jawn)
                        }
                    )
                }
            }
        } else {
            _jokeState.update {
                it.copy(
                    isLoading = false,
                    errorMsg = jokeResult.exceptionOrNull()?.message ?: "unknown error in $TAG"
                )
            }
        }
    }

}


data class JokeState(
    val isLoading: Boolean = false,
    val errorMsg: String = "",
    val jokeCategoryUiModel: JokeCategoryUiModel = JokeCategoryUiModel()
)

data class JokeCategoryUiModel(
    val jokeCategories: List<JokeCategory> = emptyList(),
    val flags: List<Flag> = emptyList(),
    val jokes: MutableList<BaseJoke> = mutableListOf()
)