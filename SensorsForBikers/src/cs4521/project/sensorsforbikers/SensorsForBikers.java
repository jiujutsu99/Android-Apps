package cs4521.project.sensorsforbikers;

import java.util.ArrayList;
import java.util.Random;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class SensorsForBikers extends Activity implements IBaseGpsListener {
    int request_Code = 1;
    private TextToSpeech ttspeech;
    private ArrayList<String> motiv = new ArrayList<String>();
    private Random rand = new Random();
    private int interval = 240000;

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            String str = "Your current speed is " + getCurrSpeed();
            ttspeech.speak(str, TextToSpeech.QUEUE_FLUSH, null);
            timerHandler.postDelayed(this, interval);
        }
    };
    
    private Handler motivHandler = new Handler();
    private Runnable motivRunnable = new Runnable() {
        @Override
        public void run() {
            ttspeech.speak(motiv.get(rand.nextInt(10)), TextToSpeech.QUEUE_FLUSH, null);
            motivHandler.postDelayed(this, interval);
        }
    };
    
    public String getCurrSpeed() {
        TextView currSpeed = (TextView) findViewById(R.id.txtCurrentSpeed);
        String spd = currSpeed.getText().toString();

        String strUnits = "miles per hour";
        if (this.useMetricUnits()) {
            strUnits = "kilometers per hour";
        }
        return spd + " " + strUnits;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors_for_bikers);
        
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        this.updateSpeed(null);

        CheckBox chkUseMetricUnits = (CheckBox)this.findViewById(R.id.chkUseMetricUnits);
        chkUseMetricUnits.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SensorsForBikers.this.updateSpeed(null);
            }
        });
        
        ttspeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR)
                    ttspeech.setLanguage(Locale.US);
            }
        });
        addWords();
        motivHandler.postDelayed(motivRunnable, interval);
        Button b = (Button) findViewById(R.id.button3);
        b.setText("Stop");
        timerHandler.postDelayed(timerRunnable, interval/2);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals("Stop")) {
                    timerHandler.removeCallbacks(timerRunnable);
                    motivHandler.removeCallbacks(motivRunnable);
                    b.setText("Start");
                } else {
                    timerHandler.postDelayed(timerRunnable, interval/2);
                    motivHandler.postDelayed(motivRunnable, interval);
                    b.setText("Stop");
                }
            }
        });
    }
    
    public void finish() {
        super.finish();
        System.exit(0);
    }
    
    public void onResume() {
        super.onResume();
        Button b = (Button) findViewById(R.id.button3);
        b.setText("Stop");
        timerHandler.postDelayed(timerRunnable, interval/2);
        motivHandler.postDelayed(motivRunnable, interval);
    }
    
    public void addWords() {
        motiv.add("Keep it up you're doing great.");
        motiv.add("You're making great progress, keep up the good work.");
        motiv.add("Keep going. Just a little more.");
        motiv.add("You are awesome. Keep on going.");
        motiv.add("Just 5 more minutes, hang in there.");
        motiv.add("That was an awesome sprint, good job.");
        motiv.add("Just a little more. Keep up the great work.");
        motiv.add("Don't give up. Keep going.");
        motiv.add("You got this. Keep it up.");
        motiv.add("Keep on going. You're almost done.");
    }
    
    public void onClick(View view) {
        startActivityForResult(new Intent("com.example.Settings"), request_Code);
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == request_Code) {
            if (resultCode == RESULT_OK && isNum(data.getData().toString())) {
                    interval = Integer.parseInt(data.getData().toString())*1000;
            }
            else
                Toast.makeText(this, "Not a valid number", Toast.LENGTH_SHORT).show();
        }
    }
    
    private boolean isNum(String num) {
        for (int i = 0; i < num.length(); i++) {
            if (!Character.isDigit(num.charAt(i)))
                return false;
        }
        return true;
    }

    public void updateSpeed(CLocation location) {
        int nCurrentSpeed = 0;

        if(location != null) {
            location.setUseMetricUnits(this.useMetricUnits());
            nCurrentSpeed = Math.round(location.getSpeed());
        }

        String strUnits = "mph";
        if (this.useMetricUnits()) {
            strUnits = "kph";
        }
        TextView units = (TextView) findViewById(R.id.units);
        units.setText(strUnits);

        TextView txtCurrentSpeed = (TextView) this.findViewById(R.id.txtCurrentSpeed);
        txtCurrentSpeed.setText(String.valueOf(nCurrentSpeed));
    }

    public boolean useMetricUnits() {
        CheckBox chkUseMetricUnits = (CheckBox)this.findViewById(R.id.chkUseMetricUnits);
        return chkUseMetricUnits.isChecked();
    }

    
    public void onLocationChanged(Location location) {
        if (location != null) {
            CLocation myLocation = new CLocation(location, this.useMetricUnits());
            this.updateSpeed(myLocation);
        }
    }
    
    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onGpsStatusChanged(int event) {
        // TODO Auto-generated method stub       
    }
    
    @Override
    public void onStop() {
        super.onStop();
        timerHandler.removeCallbacks(timerRunnable);
        motivHandler.removeCallbacks(motivRunnable);
        Button b = (Button)findViewById(R.id.button3);
        b.setText("start");
    }
}