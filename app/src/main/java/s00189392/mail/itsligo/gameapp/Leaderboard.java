package s00189392.mail.itsligo.gameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Criteria;
import android.os.Bundle;
        import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

        import java.util.ArrayList;
        import java.util.List;

public class Leaderboard extends AppCompatActivity {

    ListView listView;
    Button menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        listView = findViewById(R.id.leaderboard);
    menu = findViewById(R.id.btnMenu);
        DatabaseHandler db = new DatabaseHandler(this);

        db.emptyHiScores();     // empty table if required

        // Inserting hi scores
        Log.i("Insert: ", "Inserting ..");
        db.addHiScore(new HiScore("20 OCT 2020", "JohnJoe", 5));
        db.addHiScore(new HiScore("28 OCT 2020", "Simon", 6));
        db.addHiScore(new HiScore("20 NOV 2020", "Stephen", 20));
        db.addHiScore(new HiScore("20 NOV 2020", "Michael Coleman", 18));
        db.addHiScore(new HiScore("22 NOV 2020", "Mary", 2));
        db.addHiScore(new HiScore("30 NOV 2020", "Joe", 5));
        db.addHiScore(new HiScore("01 DEC 2020", "Martin", 2));
        db.addHiScore(new HiScore("02 DEC 2020", "Charlie", 12));


        // Reading all scores
        Log.i("Reading: ", "Reading all scores..");
        List<HiScore> hiScores = db.getAllHiScores();


        for (HiScore hs : hiScores) {
            String log =
                    "Id: " + hs.getScore_id() +
                            ", Date: " + hs.getGame_date() +
                            " , Player: " + hs.getPlayer_name() +
                            " , Score: " + hs.getScore();

            // Writing HiScore to log
            Log.i("Score: ", log);
        }

        Log.i("divider", "====================");

        HiScore singleScore = db.getHiScore(5);
        Log.i("High Score 5 is by ", singleScore.getPlayer_name() + " with a score of " +
                singleScore.getScore());

        Log.i("divider", "====================");

        // Calling SQL statement
        List<HiScore> allHiScores = db.getAllHiScores();

        for (HiScore hs : allHiScores) {
            String log =
                    "Id: " + hs.getScore_id() +
                            ", Date: " + hs.getGame_date() +
                            " , Player: " + hs.getPlayer_name() +
                            " , Score: " + hs.getScore();

            // Writing HiScore to log
            Log.i("Score: ", log);
        }
        Log.i("divider", "====================");

        HiScore hiScore = allHiScores.get(allHiScores.size() - 1);
        // hiScore contains the 5th highest score
        Log.i("fifth Highest score: ", String.valueOf(hiScore.getScore()) );

        // simple test to add a hi score
        int myCurrentScore = 40;
        // if 5th highest score < myCurrentScore, then insert new score
        if (hiScore.getScore() < myCurrentScore) {
            db.addHiScore(new HiScore("08 DEC 2020", "Elrond", 40));
        }

        Log.i("divider", "====================");

        // Calling SQL statement
        allHiScores = db.getAllHiScores();
        List<String> scoresStr;
        scoresStr = new ArrayList<>();

        int j = 1;
        for (HiScore hs : allHiScores) {

            String log =
                    "Id: " + hs.getScore_id() +
                            ", Date: " + hs.getGame_date() +
                            " , Player: " + hs.getPlayer_name() +
                            " , Score: " + hs.getScore();

            // store score in string array
            scoresStr.add(j++ + " : "  +
                    hs.getPlayer_name() + "\t" +
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
        listView.setAdapter(itemsAdapter);

    }
    public void finish(View view)
    {
        Intent mainPage = new Intent(view.getContext(),MainActivity.class);
        startActivity(mainPage);
    }
}