package com.sebastiandavis.news_app.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.Locale
import java.util.UUID

class TextToSpeechManager(
    context: Context
) {
    private var textToSpeech: TextToSpeech? = null
    private var isInitialized = false
    private var onInitSuccessListener: (() -> Unit)? = null
    private var onInitFailureListener: (() -> Unit)? = null

    init {
        textToSpeech = TextToSpeech(context) { status ->
            isInitialized = (status == TextToSpeech.SUCCESS)
            if (isInitialized) {
                textToSpeech?.language = Locale.US
                onInitSuccessListener?.invoke()
            } else {
                onInitFailureListener?.invoke()
            }
        }

        textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                // Handle start of speech
            }

            override fun onDone(utteranceId: String?) {
                // Handle completion of speech
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                // Handle speech error
            }
        })
    }

    fun speak(text: String) {
        if (isInitialized) {
            val utteranceId = UUID.randomUUID().toString()
            textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        }
    }

    fun stop() {
        textToSpeech?.stop()
    }

    fun shutdown() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
    }

    fun setOnInitSuccessListener(listener: () -> Unit) {
        this.onInitSuccessListener = listener
        if (isInitialized) {
            listener.invoke()
        }
    }

    fun setOnInitFailureListener(listener: () -> Unit) {
        this.onInitFailureListener = listener
    }
} 