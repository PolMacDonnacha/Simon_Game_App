package s00189392.mail.itsligo.gameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class TestPlay extends AppCompatActivity implements SensorEventListener {

    // experimental values for hi and lo magnitude limits
    private final double NORTH_MOVE_FORWARD = -5;     // upper mag limit
    private final double NORTH_MOVE_BACKWARD = 0;      // lower mag limit
    private final double SOUTH_MOVE_FORWARD = 5;     // upper mag limit
    private final double SOUTH_MOVE_BACKWARD = 0;      // lower mag limit
    private final double EAST_MOVE_FORWARD = 5;     // upper mag limit
    private final double EAST_MOVE_BACKWARD = 0;      // lower mag limit
    private final double WEST_MOVE_FORWARD = -5;     // upper mag limit
    private final double WEST_MOVE_BACKWARD = 0;      // lower mag limit
    boolean NhighLimit = false;      // detect high limit
    boolean ShighLimit = false;      // detect high limit
    boolean EhighLimit = false;      // detect high limit
    boolean WhighLimit = false;      // detect high limit
    int Ncounter = 0;                // step counter
    int Scounter = 0;                // step counter
    int Ecounter = 0;                // step counter
    int Wcounter = 0;                // step counter

    TextView tvx, tvy, tvz, tvNorth,tvSouth,tvEast,tvWest;
    private SensorManager mSensorManager;
    private Sensor mSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_play);

        // int[] arrayB = extras.getIntArray("numbers");
        // 1 2 1 1  (N N W N)

        tvx = findViewById(R.id.tvX);
        tvy = findViewById(R.id.tvY);
        tvz = findViewById(R.id.tvZ);
        tvNorth = findViewById(R.id.tvNorth);
        tvSouth = findViewById(R.id.tvSouth);
        tvEast = findViewById(R.id.tvEast);
        tvWest = findViewById(R.id.tvWest);
        // we are going to use the sensor service
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }


    /*
     * When the app is brought to the foreground - using app on screen
     */
    protected void onResume() {
        super.onResume();
        // turn on the sensor
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    /*
     * App running but not on screen - in the background
     */
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);    // turn off listener to save power
    }


    @Override
    public void onSensorChanged(SensorEvent event) {


        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        tvx.setText(String.valueOf(x));
        tvy.setText(String.valueOf(y));
        tvz.setText(String.valueOf(z));


        // Can we get a north movement
        // you need to do your own mag calculating

        if ((x < NORTH_MOVE_FORWARD) && (NhighLimit == false)) {
            NhighLimit = true;
            EhighLimit = false;
            ShighLimit = false;
            WhighLimit = false;
        }
        if ((x > NORTH_MOVE_BACKWARD) && (NhighLimit == true)) {
            // we have a tilt to the north
            Ncounter++;
            tvNorth.setText(String.valueOf(Ncounter));
            NhighLimit = false;
        }

        if ((x > SOUTH_MOVE_FORWARD) && (ShighLimit == false)) {
            ShighLimit = true;
            EhighLimit = false;
            NhighLimit = false;
            WhighLimit = false;
        }
        if ((x < SOUTH_MOVE_BACKWARD) && (ShighLimit == true)) {
            // we have a tilt to the SOUTH
            Scounter++;
            tvSouth.setText(String.valueOf(Scounter));
            ShighLimit = false;
        }

        if ((y > EAST_MOVE_FORWARD) && (EhighLimit == false)) {
            EhighLimit = true;
            ShighLimit = false;
            WhighLimit = false;
            NhighLimit = false;
        }
        if ((y < EAST_MOVE_BACKWARD) && (EhighLimit == true)) {
            // we have a tilt to the SOUTH
            Ecounter++;
            tvEast.setText(String.valueOf(Ecounter));
            EhighLimit = false;
        }

        if ((y < WEST_MOVE_FORWARD) && (WhighLimit == false)) {
            WhighLimit = true;
            NhighLimit = false;
            ShighLimit = false;
            EhighLimit = false;
        }
        if ((y > WEST_MOVE_BACKWARD) && (WhighLimit == true)) {
            // we have a tilt to the SOUTH
            Wcounter++;
            tvWest.setText(String.valueOf(Wcounter));
            WhighLimit = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not used
    }


}