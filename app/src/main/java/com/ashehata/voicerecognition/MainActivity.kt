package com.ashehata.voicerecognition

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import com.androidisland.ezpermission.EzPermission
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private  var results: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn.setOnClickListener {
            checkPermission()
            recordVoice()
        }
    }

    private fun recordVoice() {
        if (checkAPI()) {
            askSpeechInput()
        }
    }

    private fun checkAPI(): Boolean {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Toast.makeText(
                this,
                R.string.no_recognition_available,
                Toast.LENGTH_LONG
            ).show()
            return false
        } else
            return true
    }

    private fun askSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");
        }
        startActivityForResult(intent, 0);
    }

    private fun checkPermission() {
        EzPermission.with(this)
                .permissions(
                        Manifest.permission.RECORD_AUDIO
                )
                .request { granted, denied, permanentlyDenied ->
                    //Here you can check results...
                    if(granted.isEmpty()) {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            Log.d("TAG-R", results?.toString())
            txt.text = results?.get(0)?.toLowerCase()
        }
    }
}
