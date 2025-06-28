package com.example.cloudtorain;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {

    TextView questionText, resultText;
    RadioGroup answerGroup;
    RadioButton option1, option2, option3;
    Button submitBtn, backBtn;

    String[] questions = {
            "What is the first step of the water cycle?",
            "What is the step where water falls from clouds as rain?",
            "What causes clouds to release rain?"
    };

    String[][] options = {
            {"Condensation", "Evaporation", "Precipitation"},     // Evaporation is correct
            {"Evaporation", "Precipitation", "Collection"},       // Precipitation is correct
            {"Condensation", "Collection", "Heavy clouds"}        // Condensation is correct
    };

    int[] correctAnswers = {1, 1, 0}; // Indexes of correct options

    int currentQuestion = 0;
    int score = 0;
    boolean answered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            finish(); // Ends QuizActivity and returns to the previous activity
        });

        questionText = findViewById(R.id.questionText);
        resultText = findViewById(R.id.resultText);
        answerGroup = findViewById(R.id.answerGroup);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        submitBtn = findViewById(R.id.submitBtn);

        loadQuestion();

        submitBtn.setOnClickListener(v -> {
            if (!answered) {
                int selectedId = answerGroup.getCheckedRadioButtonId();
                if (selectedId == -1) return;

                int answerIndex = answerGroup.indexOfChild(findViewById(selectedId));
                if (answerIndex == correctAnswers[currentQuestion]) {
                    score++;
                    resultText.setText("✅ Correct!");
                    resultText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                } else {
                    resultText.setText("❌ Incorrect");
                    resultText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }
                resultText.setVisibility(View.VISIBLE);
                submitBtn.setText(currentQuestion == questions.length - 1 ? "Finish" : "Next");
                answered = true;
            } else {
                currentQuestion++;
                if (currentQuestion < questions.length) {
                    loadQuestion();
                    resultText.setVisibility(View.GONE);
                    submitBtn.setText("Submit");
                    answered = false;
                } else {
                    questionText.setText("🎉 Quiz complete! You scored " + score + "/" + questions.length);
                    answerGroup.setVisibility(View.GONE);
                    submitBtn.setVisibility(View.GONE);
                    resultText.setVisibility(View.GONE);
                }
            }
        });
    }

    void loadQuestion() {
        questionText.setText(questions[currentQuestion]);
        option1.setText(options[currentQuestion][0]);
        option2.setText(options[currentQuestion][1]);
        option3.setText(options[currentQuestion][2]);
        answerGroup.clearCheck();
    }
}