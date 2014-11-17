package com.raytts.texttospeech;

import java.util.Locale;
import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    TextToSpeech ttspeech;
    private EditText write;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        write = (EditText) findViewById(R.id.editText1);
        ttspeech = new TextToSpeech(getApplicationContext(), 
            new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        ttspeech.setLanguage(Locale.UK);
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
       String toSpeak = write.getText().toString();
       Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
       ttspeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }
}
