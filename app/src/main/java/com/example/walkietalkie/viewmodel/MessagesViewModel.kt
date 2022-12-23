package com.example.walkietalkie.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.walkietalkie.model.Message
import com.example.walkietalkie.model.MessagesService
import kotlinx.coroutines.*

class MessagesViewModel : ViewModel() {

    private val messagesService = MessagesService.getMessagesService()
    private var messageJob: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception: ${throwable.localizedMessage}")
    }

    val messages = MutableLiveData<MutableList<Message>>()
    val messagesLoadError = MutableLiveData<String?>()

    fun refresh() {
        fetchMessages()
    }

    private fun fetchMessages() {
        messageJob = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = messagesService.getMessages()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    messages.value = response.body() as MutableList<Message>?
                    messagesLoadError.value = null
                } else {
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    private fun onError(errorMessage: String) {
        messagesLoadError.postValue(errorMessage)
    }

    override fun onCleared() {
        super.onCleared()
        messageJob?.cancel()
    }

}