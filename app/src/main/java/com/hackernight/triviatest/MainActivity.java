package com.hackernight.triviatest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hackernight.data.AnswerListAsyncResponse;
import com.hackernight.data.QuestionModel;
import com.hackernight.model.Question;
import com.hackernight.model.Score;
import com.hackernight.util.Prefs;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView questionTextView;
    private TextView questionCounterTextView;
    private Button trueButton;
    private Button falseButton;
    private ImageButton prevButton;
    private ImageButton nextButton;
    private int currentQuestionIndex = 0;
    private List<Question> questions;
    private int scoreCounter = 0 ;
    private Score score;
    private TextView scoretextView;
    private Prefs prefs;
    private  TextView highscoretextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            score = new Score();

            prefs =  new Prefs(MainActivity.this);

            //get previous state
            currentQuestionIndex = prefs.getState();

            nextButton = findViewById(R.id.next_button);
            prevButton = findViewById(R.id.prev_button);
            trueButton = findViewById(R.id.true_button);
            falseButton = findViewById(R.id.false_button);
            questionCounterTextView = findViewById(R.id.counter_question_text);
            questionTextView = findViewById(R.id.question_textview);
            scoretextView = findViewById(R.id.score_textview);
            highscoretextView = findViewById(R.id.highestscoretextview);

            nextButton.setOnClickListener(this);
            prevButton.setOnClickListener(this);
            trueButton.setOnClickListener(this);
            falseButton.setOnClickListener(this);

            scoretextView.setText("Current Score: "+String.valueOf(score.getScore()));
            highscoretextView.setText("Highest Score: "+String.valueOf(prefs.getHighScore()));

            questions = new QuestionModel().getQuestion(new AnswerListAsyncResponse() {
            @Override
            public void processFinish(ArrayList<Question> questionArrayList) {
                questionTextView.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
                questionCounterTextView.setText(currentQuestionIndex+"/"+questionArrayList.size());
                //Log.d("QuestionList : ", questionArrayList.toString());
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.prev_button:
                if(currentQuestionIndex>0)
                 currentQuestionIndex = (currentQuestionIndex - 1) % questions.size() ;
                 updateQuestion() ;
                 break ;
            case R.id.next_button:
                 currentQuestionIndex = (currentQuestionIndex + 1) % questions.size() ;
                 updateQuestion() ;
                 break ;
            case R.id.true_button:
                 checkAnswer(true);
                 updateQuestion();
                 break ;
            case R.id.false_button:
                checkAnswer(false);
                updateQuestion();
                 break ;
        }
    }

    private void checkAnswer(boolean userChooseCorrect){

        boolean answerIsTrue = questions.get(currentQuestionIndex).isAnswerTrue();

        int answerId = 0;

        if (userChooseCorrect == answerIsTrue){
            fadeAnimation();
            addPoints();
            answerId = R.string.correctAnswer;
        }
        else{
            shakeAnimation();
            deductPoints();
            answerId = R.string.wrongAnswer;
        }

        Toast.makeText(this,answerId,Toast.LENGTH_SHORT).show();

    }

    public void addPoints(){
        scoreCounter +=100;
        score.setScore(scoreCounter);
        scoretextView.setText("Current Score: "+String.valueOf(score.getScore()));
    }

    public void deductPoints(){
        scoreCounter -=100;
        if (scoreCounter>0){
            score.setScore(scoreCounter);
        }else{
            scoreCounter = 0 ;
            score.setScore(scoreCounter);
        }
        scoretextView.setText("Current Score: "+String.valueOf(score.getScore()));
    }

    private void updateQuestion() {
        questionTextView.setText(questions.get(currentQuestionIndex).getAnswer());
        questionCounterTextView.setText(currentQuestionIndex+"/"+questions.size());
    }

    private void fadeAnimation(){
        final CardView cardView = findViewById(R.id.cardView);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                currentQuestionIndex = (currentQuestionIndex + 1) % questions.size() ;
                updateQuestion() ;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void shakeAnimation(){
        Animation animation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_animation);
        final CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                currentQuestionIndex = (currentQuestionIndex + 1) % questions.size() ;
                updateQuestion() ;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        prefs.saveHighScore(score.getScore());
        prefs.setState(currentQuestionIndex);
    }

}