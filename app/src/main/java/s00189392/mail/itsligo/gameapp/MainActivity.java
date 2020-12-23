package s00189392.mail.itsligo.gameapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class MainActivity extends AppCompatActivity {
    EditText playerName;

    private final int BLUE = 3;
    private final int RED = 1;
    private final int YELLOW = 4;
    private final int GREEN = 2;

    String name;
    Button bRed, bBlue, bYellow, bGreen, fb,leaderboard;
    TextView tvText;
    Boolean passed = false;
    int sequenceCount = 4, n = 0;
    int sequenceOptions = 4;//Colour options
    int[] gameSequence = new int[sequenceCount];
    int arrayIndex = 0,score = 0;
    Intent PlayPage,TestPage,LeaderboardPage, gameOverPage;
    CountDownTimer ct = new CountDownTimer((sequenceCount*1000) + ((sequenceCount/2)*1000), 1500) {

        public void onTick(long millisUntilFinished) {
            //mTextField.setText("seconds remaining: " + millisUntilFinished / 1500);
            oneButton();
            //here you can have your logic to set text to edittext
        }

        public void onFinish() {
            //mTextField.setText("done!");
            // we now have the game sequence

            for (int i = 0; i < arrayIndex; i++)
                Log.i("game sequence", String.valueOf(gameSequence[i]));
            // start next activity
            PlayPage.putExtra("Sequence",gameSequence);
            PlayPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PlayPage.addFlags(FLAG_ACTIVITY_CLEAR_TASK);
            startActivityForResult(PlayPage,1);
            // put the sequence into the next activity
            // stack overglow https://stackoverflow.com/questions/3848148/sending-arrays-with-intent-putextra
            //Intent i = new Intent(A.this, B.class);
            //i.putExtra("numbers", array);
            //startActivity(i);

            // start the next activity
            // int[] arrayB = extras.getIntArray("numbers");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bRed = findViewById(R.id.btnRed);
        bBlue = findViewById(R.id.btnBlue);
        bYellow = findViewById(R.id.btnYellow);
        bGreen = findViewById(R.id.btnGreen);
        tvText = findViewById(R.id.tvText);
        playerName = findViewById(R.id.etName);
        tvText.setText(R.string.app_launch);
        leaderboard = findViewById(R.id.btnLeaderboard);

    }

    public void doPlay(View view) {
        name = "";
        name = playerName.getText().toString();
        if(name != ""){
            ct.start();
            PlayPage = new Intent(MainActivity.this, PlayScreen.class);
            PlayPage.putExtra("name",name);
            PlayPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PlayPage.addFlags(FLAG_ACTIVITY_CLEAR_TASK);
            tvText.setText(R.string.app_sequence);
        }
        else
        {
            Toast.makeText(this, "Please Enter a Name ", Toast.LENGTH_SHORT).show();
        }

    }
    public void doTest(View view) {
        TestPage = new Intent(view.getContext(), TestPlay.class);
        startActivity(TestPage);
    }

    private void oneButton() {
        n = getRandom(sequenceOptions);

        Toast.makeText(this, "Number = " + n, Toast.LENGTH_SHORT).show();

        switch (n) {
            case 1:
                flashButton(bRed);
                gameSequence[arrayIndex++] = RED;
                break;
            case 2:
                flashButton(bGreen);
                gameSequence[arrayIndex++] = GREEN;
                break;
            case 3:
                flashButton(bBlue);
                gameSequence[arrayIndex++] = BLUE;
                break;
            case 4:
                flashButton(bYellow);
                gameSequence[arrayIndex++] = YELLOW;
                break;
            default:
                break;
        }   // end switch
    }

    //
    // return a number between 1 and maxValue
    private int getRandom(int maxValue) {
        return ((int) ((Math.random() * maxValue) + 1));
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                sequenceCount += 2;

                score = data.getIntExtra("score",0);
                passed = data.getBooleanExtra("passed",false);
                Log.i("Passed value on finish: ", String.valueOf(passed));
                int[] returnedArray = data.getIntArrayExtra("arrayReturn");
                if(passed == false){
                    doGameOver();
                }

                arrayIndex = gameSequence.length;
                gameSequence = new int[gameSequence.length + 2];
                Toast.makeText(this, "Finished level", Toast.LENGTH_LONG).show();
                for (int i = 0; i <gameSequence.length;i++) {
                    if (i < returnedArray.length)//copying existing sequence
                    {
                        gameSequence[i] = returnedArray[i];
                    }
                }
                Log.i("arrayIndex: ", String.valueOf(arrayIndex));
                Log.i("Sequence length: ", String.valueOf(gameSequence.length));
                for (int i = arrayIndex;i <gameSequence.length;i++)
                {
                    n = getRandom(sequenceOptions);
                    Toast.makeText(this, "New number added: " + n, Toast.LENGTH_LONG).show();

                    switch (n) {
                        case 1:
                            gameSequence[i] = RED;
                            break;
                        case 2:
                            gameSequence[i] = GREEN;
                            break;
                        case 3:
                            gameSequence[i] = BLUE;
                            break;
                        case 4:
                            gameSequence[i] = YELLOW;
                            break;
                        default:
                            break;
                    }   // end switch
                }
                for (int i = 0; i < gameSequence.length; i++) {
                    Log.i("New game sequence", String.valueOf(gameSequence[i]));
                }
                CountDownTimer nct = new CountDownTimer((sequenceCount*1000) + ((sequenceCount/2)*1000), 1500) {
                    int arrayIndex = 0;
                    public void onTick(long millisUntilFinished) {
                        //mTextField.setText("seconds remaining: " + millisUntilFinished / 1500);
                        Log.i("Current array index in onTick", String.valueOf(arrayIndex));
                        //   Toast.makeText(MainActivity.this, "Number = " + gameSequence[arrayIndex], Toast.LENGTH_SHORT).show();
                        switch (gameSequence[arrayIndex])
                        {
                            case RED:
                                arrayIndex++;
                                flashButton(bRed);
                                break;
                            case GREEN:
                                arrayIndex++;
                                flashButton(bGreen);
                                break;
                            case BLUE:
                                arrayIndex++;
                                flashButton(bBlue);
                                break;
                            case YELLOW:
                                arrayIndex++;
                                flashButton(bYellow);
                                break;
                        }
                        //here you can have your logic to set text to edittext
                    }


                    public void onFinish() {
                        //mTextField.setText("done!");
                        // we now have the game sequence

                        for (int i = 0; i < arrayIndex; i++) {
                            Log.d("game sequence", String.valueOf(gameSequence[i]));
                        }
                        // start next activity
                        PlayPage.putExtra("Sequence",gameSequence);
                        PlayPage.putExtra("name",name);
                        PlayPage.putExtra("score",score);
                        PlayPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PlayPage.addFlags(FLAG_ACTIVITY_CLEAR_TASK);
                        startActivityForResult(PlayPage,1);
                        // put the sequence into the next activity
                        // stack overglow https://stackoverflow.com/questions/3848148/sending-arrays-with-intent-putextra
                        //Intent i = new Intent(A.this, B.class);
                        //i.putExtra("numbers", array);
                        //startActivity(i);

                        // start the next activity
                        // int[] arrayB = extras.getIntArray("numbers");
                    }
                };

                nct.start();

            }

        }
    }
    public void doGameOver(){
        gameOverPage = new Intent(MainActivity.this, GameOver.class);
        gameOverPage.putExtra("score",score);
        gameOverPage.putExtra("name",name);
        gameOverPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );//|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(gameOverPage);
    }
    public void Leaderboard(View view)
    {
        LeaderboardPage = new Intent(view.getContext(), Leaderboard.class);
        startActivity(LeaderboardPage);
    }
}