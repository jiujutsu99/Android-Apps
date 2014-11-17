package com.raytts.texttospeech;

import java.util.Locale;
import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    private TextToSpeech ttspeech;
    private EditText editText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        editText = (EditText) findViewById(R.id.editText);
        ttspeech = new TextToSpeech(getApplicationContext(), 
            new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        ttspeech.setLanguage(Locale.US);
                    }
                }
            });
     }
    
    @Override
    public void onPause() {
       if (ttspeech != null) {
          ttspeech.stop();
          ttspeech.shutdown();
       }
       super.onPause();
    }
    
    public void speakText(View view) {
       String toSpeak = editText.getText().toString();
       Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
       ttspeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }
    
    public void speakYes(View view) {
        String str = "Yes";
        ttspeech.speak(str, TextToSpeech.QUEUE_FLUSH, null);
    }
    
    public void speakNo(View view) {
        String str = "No";
        ttspeech.speak(str, TextToSpeech.QUEUE_FLUSH, null);
    }
    
    public void speakTY(View view) {
        String str = "Thank you";
        ttspeech.speak(str, TextToSpeech.QUEUE_FLUSH, null);
    }
    
    public void speakYW(View view) {
        String str = "You're welcome";
        ttspeech.speak(str, TextToSpeech.QUEUE_FLUSH, null);
    }
}
