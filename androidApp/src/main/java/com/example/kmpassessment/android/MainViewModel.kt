package com.example.kmpassessment.android

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.kmpassessment.Story
import com.example.kmpassessment.cache.StoriesDatabase
import com.example.kmpassessment.network.NetworkModule
import com.example.kmpassessment.network.StoryService
import com.example.kmpassessment.repository.StoryRepository
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {


    private val driver = AndroidSqliteDriver(
        schema  = StoriesDatabase.Schema,
        context = App.context,
        name    = "stories.db"
    )

    private val database = StoriesDatabase(driver)

    private val service = StoryService(
        NetworkModule.client(OkHttp.create())
    )

    private val repo = StoryRepository(
        api = service,
        db = database,
        io = Dispatchers.IO
    )


    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    val filteredStories: StateFlow<List<Story>> =
        _query
            .debounce(300)
            .flatMapLatest { q -> repo.filter(q) }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _showOfflineSnackbar = MutableStateFlow(false)
    val showOfflineSnackbar: StateFlow<Boolean> = _showOfflineSnackbar.asStateFlow()

    // ─── Lifecycle ───
    init { refresh() }

    // ─── UI Actions ───
    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                repo.refresh()
                    .onSuccess {
                        Log.i("MainViewModel", "Loaded ${it.size} stories")
                    }
                    .onFailure {
                        Log.e("MainViewModel", "Refresh FAILED", it)
                        _showOfflineSnackbar.value = true
                    }
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun onQueryChanged(text: String) {
        _query.value = text
    }

    fun clearOfflineSnackbar() {
        _showOfflineSnackbar.value = false
    }
}
