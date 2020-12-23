package s00189392.mail.itsligo.gameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlayScreen extends AppCompatActivity implements SensorEventListener {

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
    int Ncounter = 0;                // NORTH counter
    int Scounter = 0;                // SOUTH counter
    int Ecounter = 0;                // EAST counter
    int Wcounter = 0;                // WEST counter


    Button bRed, bBlue, bYellow, bGreen, fb,next;
    TextView tvText,tvScore;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    int[] arrayReturn,passedArray;
    int currentIndex, score;
    private final int BLUE = 3;
    private final int RED = 1;
    private final int YELLOW = 4;
    private final int GREEN = 2;
    Intent extras,gameOverPage;
    String name;
    Boolean passed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);
        extras = getIntent();
        currentIndex = 0;//Current position in the sequence
        passedArray = extras.getIntArrayExtra("Sequence");
        name = extras.getStringExtra("name");
        score = extras.getIntExtra("score",0);
        passed = false;
        for(int i = 0; i < passedArray.length;i++)
        {
            Log.i("Array " + String.valueOf(i) + ": " , String.valueOf(passedArray[i]));
        }
        arrayReturn = new int[passedArray.length];
        // 1 2 1 1  (N N W N)
        bRed = findViewById(R.id.btnRed);
        bBlue = findViewById(R.id.btnBlue);
        bYellow = findViewById(R.id.btnYellow);
        bGreen = findViewById(R.id.btnGreen);
        tvText = findViewById(R.id.tvText);
        tvScore = findViewById(R.id.tvFinalScore);
        tvText.setText(R.string.app_playing);

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
        tvScore.setText(String.valueOf(score));
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        // Can we get a north movement
        // you need to do your own mag calculating

        if ((x < NORTH_MOVE_FORWARD) && (NhighLimit == false)) {
            NhighLimit = true;
            EhighLimit = false;
            ShighLimit = false;
            WhighLimit = false;
        }
        if ((x > NORTH_MOVE_BACKWARD) && (NhighLimit == true)) {
            // we have a tilt to the NORTH
            Ncounter++;
            flashButton(bGreen);
            arrayReturn[currentIndex] = GREEN;
            NhighLimit = false;
            Log.i("Set Array[" + currentIndex + "]: ", String.valueOf(GREEN));
            if (arrayReturn[currentIndex] != passedArray[currentIndex] && arrayReturn[currentIndex] != 0)//if sequence doesn't match and value entered
            {
                mSensorManager.unregisterListener(this);
                Log.i("Game over: " , "game Over Triggered");
                doGameOver();
            }
            else {
                score++;
            }
                currentIndex++;
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
            flashButton(bYellow);
            arrayReturn[currentIndex] = YELLOW;
            ShighLimit = false;
            Log.i("Set Array[" + currentIndex + "]: ", String.valueOf(YELLOW));
            if (arrayReturn[currentIndex] != passedArray[currentIndex] && arrayReturn[currentIndex] != 0)//if sequence doesn't match and value entered
            {
                mSensorManager.unregisterListener(this);
                Log.i("Game over: " , "game Over Triggered");
                doGameOver();
            }
            else {
                score++;
            }
                currentIndex++;
        }

        if ((y > EAST_MOVE_FORWARD) && (EhighLimit == false)) {
            EhighLimit = true;
            ShighLimit = false;
            WhighLimit = false;
            NhighLimit = false;
        }
        if ((y < EAST_MOVE_BACKWARD) && (EhighLimit == true)) {
            // we have a tilt to the EAST
            Ecounter++;
            flashButton(bBlue);
            arrayReturn[currentIndex] = BLUE;
            EhighLimit = false;
            Log.i("Set Array[" + currentIndex + "]: ", String.valueOf(BLUE));
            if (arrayReturn[currentIndex] != passedArray[currentIndex] && arrayReturn[currentIndex] != 0)//if sequence doesn't match and value entered
            {
                mSensorManager.unregisterListener(this);
                Log.i("Game over: " , "game Over Triggered");
                doGameOver();
            }
            else {
                score++;
            }
                currentIndex++;
        }

        if ((y < WEST_MOVE_FORWARD) && (WhighLimit == false)) {
            WhighLimit = true;
            NhighLimit = false;
            ShighLimit = false;
            EhighLimit = false;
        }
        if ((y > WEST_MOVE_BACKWARD) && (WhighLimit == true)) {
            // we have a tilt to the WEST
            Wcounter++;
            flashButton(bRed);
            arrayReturn[currentIndex] = RED;
            WhighLimit = false;
            Log.i("Set Array[" + currentIndex + "]: ", String.valueOf(RED));
            if (arrayReturn[currentIndex] != passedArray[currentIndex] && arrayReturn[currentIndex] != 0)//if sequence doesn't match and value entered
            {
                mSensorManager.unregisterListener(this);
                Log.i("Game over: " , "game Over Triggered");
                doGameOver();
            }
            else {
                score++;
            }
                currentIndex++;
        }
        Log.i("CurrentIndex: " , String.valueOf(currentIndex));
        Log.i("PassedArrayLength: " , String.valueOf(passedArray.length));
        Log.i("ArrayreturnLength: " , String.valueOf(arrayReturn.length));
        if ((currentIndex == passedArray.length) && arrayReturn[currentIndex-1] == passedArray[currentIndex-1])
        {
            Log.i("Level Complete: " , "Level Complete Triggered");
            tvText.setText(R.string.level_Completed);
            Intent levelPassed = new Intent();
          //  levelPassed.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // levelPassed.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          //  levelPassed.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            passed = true;
            levelPassed.putExtra("arrayReturn",arrayReturn);
            levelPassed.putExtra("score",score);
            levelPassed.putExtra("passed",passed);
            setResult(RESULT_OK, levelPassed);
            finish();

        }
    }

    public void doGameOver(){
        gameOverPage = new Intent();
        passed = false;
        gameOverPage.putExtra("score",score);
        gameOverPage.putExtra("name",name);
        gameOverPage.putExtra("passed",passed);
        setResult(RESULT_OK, gameOverPage);
        finish();
    }
    private void flashButton(Button button) {
        fb = button;
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {

                fb.setPressed(true);
                fb.invalidate();
                fb.performClick();
                Handler handler1 = new Handler();
                Runnable r1 = new Runnable() {
                    public void run() {
                        fb.setPressed(false);
                        fb.invalidate();
                    }
                };
                handler1.postDelayed(r1, 600);

            } // end runnable
        };
        handler.postDelayed(r, 600);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not used
    }


}