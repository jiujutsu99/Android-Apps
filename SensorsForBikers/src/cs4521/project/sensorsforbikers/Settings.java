package cs4521.project.sensorsforbikers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.util.ArrayList;

public class Settings extends Activity {
    private ArrayList<String> motiv = new ArrayList<String>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }
    
    public void onClick(View view) {
        Intent data = new Intent();

        EditText txt_username = (EditText) findViewById(R.id.interval);
      
        data.setData(Uri.parse(txt_username.getText().toString()));
        setResult(RESULT_OK, data);
      
        finish();
    }
}
