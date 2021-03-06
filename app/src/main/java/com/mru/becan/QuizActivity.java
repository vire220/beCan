package com.mru.becan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mru.becan.beacon.Beacon;
import com.mru.becan.beacon.Question;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QUIZ_ACTIVITY";

    private Beacon beacon;
    private Beacon[] beacons;
    private Question question;

    private TextView titleTextView;
    private TextView questionTextView;
    private Button answerButton1;
    private Button answerButton2;
    private Button answerButton3;
    private Button answerButton4;

    private Button[] buttonArray;

    private View.OnClickListener correctAnswerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new AlertDialog.Builder(QuizActivity.this)
                    .setMessage("Correct!")
                    .setNeutralButton("Next Clue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            beacon = getNextBeacon();

                            if (beacon != null) {
                                Intent intent = new Intent(QuizActivity.this, SearchActivity.class);
                                intent.putExtra("beacon", beacon);
                                intent.putExtra("beaconArray", beacons);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(QuizActivity.this, SearchActivity.class);
                                intent.putExtra("victory", true);
                                startActivity(intent);
                            }
                        }
                    })
                    .show();
        }
    };

    private View.OnClickListener wrongAnswerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast toast = Toast.makeText(getApplicationContext(), "Incorrect!", Toast.LENGTH_SHORT);
            toast.show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Intent intent = getIntent();
        beacon = intent.getParcelableExtra("beacon");
        Parcelable[] parceledArray = intent.getParcelableArrayExtra("beaconArray");
        beacons = new Beacon[parceledArray.length];
        for (int i = 0; i < parceledArray.length; i++) {
            beacons[i] = (Beacon)parceledArray[i];
        }

        question = beacon.getQuestions()[0];

        titleTextView = findViewById(R.id.textView_quiz_title);
        questionTextView = findViewById(R.id.textView_quiz_question);

        answerButton1 = findViewById(R.id.button_answer_1);
        answerButton2 = findViewById(R.id.button_answer_2);
        answerButton3 = findViewById(R.id.button_answer_3);
        answerButton4 = findViewById(R.id.button_answer_4);

        buttonArray = new Button[]{answerButton1, answerButton2, answerButton3, answerButton4};

        titleTextView.setText(beacon.getName());
        questionTextView.setText(question.getQuestion());

        answerButton1.setText(question.getAnswers()[0]);
        answerButton2.setText(question.getAnswers()[1]);
        answerButton3.setText(question.getAnswers()[2]);
        answerButton4.setText(question.getAnswers()[3]);

        for(int i = 0; i < buttonArray.length; i++){
            if(buttonArray[i].getText().equals(question.getCorrectAnswer())){
                buttonArray[i].setOnClickListener(correctAnswerListener);
            } else {
                buttonArray[i].setOnClickListener(wrongAnswerListener);
            }
        }
    }

    private Beacon getNextBeacon() {
        for (int i = 0; i < beacons.length; i++) {
            if(i < beacons.length - 1 && beacon.getBeaconId().equals(beacons[i].getBeaconId())) {
                    return beacons[i + 1];
                }
            }
        return null;
    }
}
