package com.hackernight.triviatest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.hackernight.controller.AppController;
import com.hackernight.data.AnswerListAsyncResponse;
import com.hackernight.data.QuestionModel;
import com.hackernight.model.Question;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Question> questions = new QuestionModel().getQuestion(new AnswerListAsyncResponse() {
            @Override
            public void processFinish(ArrayList<Question> questionArrayList) {
                Log.d("QuestionList : ", questionArrayList.toString());
            }
        });

    }
}