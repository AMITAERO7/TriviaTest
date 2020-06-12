package com.hackernight.data;

import android.app.Application;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.hackernight.controller.AppController;
import com.hackernight.model.Question;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

public class QuestionModel extends Application {

    private ArrayList<Question> questionsArrayList = new ArrayList<>();

    private String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestion(final AnswerListAsyncResponse callback){

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i =0 ; i<response.length();i++){
                    try {

                        Question question = new Question(response.getJSONArray(i).getString(0),
                                response.getJSONArray(i).getBoolean(1));

                        //Log.d("TAGAA",""+question);

                        //Adding question to question Array List
                        questionsArrayList.add(question);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (callback != null)
                    callback.processFinish(questionsArrayList);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        //Adding JSONArrayRequest to request queue
        AppController.getInstance().addToRequestQueue(arrayRequest);

        return questionsArrayList;

    }

}
