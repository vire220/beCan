package com.mru.becan.beacon;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vire7 on 3/5/2018.
 */

public class Question implements Parcelable{

    private String question;
    private String correctAnswer;
    private String[] answers;

    public Question(String question, String correctAnswer, String[] answers) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.answers = answers;
    }

    public Question(JSONObject jsonObject) {
        try {
            this.question = jsonObject.getString("question");
            this.correctAnswer = jsonObject.getString("correctAnswer");
            JSONArray answers = jsonObject.getJSONArray("answers");
            this.answers = new String[answers.length()];

            for(int i = 0; i < answers.length(); i++){
                this.answers[i] = answers.getString(i);
            }
        } catch (JSONException e) {
            Log.e("QUESTION_CLASS", e.getMessage());
        }
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String[] getAnswers() {
        return answers;
    }

    public Question(Parcel parcel) {
        question = parcel.readString();
        correctAnswer = parcel.readString();
        answers = new String[4];
        parcel.readStringArray(answers);
    }

    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {

        @Override
        public Question createFromParcel(Parcel parcel) {
            return new Question(parcel);
        }

        @Override
        public Question[] newArray(int i) {
            return new Question[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(question);
        parcel.writeString(correctAnswer);
        parcel.writeStringArray(answers);
    }
}
