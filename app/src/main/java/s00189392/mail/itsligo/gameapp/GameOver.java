package s00189392.mail.itsligo.gameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameOver extends AppCompatActivity {
    DatabaseHandler db;
    int score;
    Date gameDate;
    String stringDate,playerName;
    Button menu,leader;
    TextView finalScore;
    ListView myScoresLV;
    Intent LeaderboardPage;
    List<String> scoresStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        score = getIntent().getIntExtra("score",-1);
        playerName = getIntent().getStringExtra("name");
        menu = findViewById(R.id.btnMenu);
        leader = findViewById(R.id.btnLeaderboard);
        myScoresLV = findViewById(R.id.listMyScores);
        finalScore = findViewById(R.id.tvFinalScore);
        finalScore.setText(String.valueOf(score));
        gameDate = new Date();
        boolean beatLeader = false;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyy");
        stringDate = dateFormat.format((gameDate));
        db = new DatabaseHandler(this);

        List<HiScore> myScores = db.getScoresByName(playerName);


        scoresStr = new ArrayList<>();

        int j = 1;
        for (HiScore hs : myScores) {

            String log =
                            ", Date: " + hs.getGame_date() +
                            " , Player: " + hs.getPlayer_name() +
                            " , Score: " + hs.getScore();

            // store score in string array
            scoresStr.add(j++ + " : "  +
                    hs.getPlayer_name() + "\t" + hs.getGame_date() + "\t" +
                    hs.getScore());
            // Writing HiScore to log
            Log.i("Score: ", log);
        }

        Log.i("divider", "====================");
        Log.i("divider", "Scores in list <>>");
        for (String ss : scoresStr) {
            Log.i("Score: ", ss);
        }
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, scoresStr);
        myScoresLV.setAdapter(itemsAdapter);

        List<HiScore> scores = db.getTopFiveScores();
        for (HiScore s : scores) {
            String log = "Id: " + s.getScore_id() + " ,Name: " + s.getPlayer_name() + " ,Date: " + s.getGame_date() + " ,Score: " +
                    s.getScore();
            Toast.makeText(getApplicationContext(), "Id: " + s.getScore_id() + " \nName: " + s.getPlayer_name() + "\nDate: " + s.getGame_date() + "\nScore: " +
                    s.getScore(),Toast.LENGTH_SHORT).show();
            // Writing Contacts to log
            Log.i("Game Record: ", log);
            if(score > s.getScore())
            {
                beatLeader = true;
            }
        }
        if(beatLeader == true){
            db.addHiScore(new HiScore(stringDate, playerName,score));
        }


    }
    public void finish()
    {
        finish();
    }
    public void leaderboard(View view)
    {
        LeaderboardPage = new Intent(view.getContext(), Leaderboard.class);
        startActivity(LeaderboardPage);
    }
}