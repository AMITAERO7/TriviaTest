package com.hackernight.data;

import com.hackernight.model.Question;

import java.util.ArrayList;

public interface AnswerListAsyncResponse {
    void processFinish(ArrayList<Question> questionArrayList);
}
