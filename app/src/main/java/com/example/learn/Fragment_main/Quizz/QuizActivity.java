package com.example.learn.Fragment_main.Quizz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learn.Fragment_main.Fragment.PagerAdaptor.Student_content;
import com.example.learn.Fragment_main.profile_fragment;
import com.example.learn.Helper_class.Model.quizModel;
import com.example.learn.R;
import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;

import java.util.Collections;
import java.util.List;

import soup.neumorphism.NeumorphCardView;

import static com.example.learn.Fragment_main.Fragment.Adaptor.Quiz_stu_adaptor.listOfQ;


public class QuizActivity extends AppCompatActivity {
    private static final String TAG = QuizActivity.class.getSimpleName();
    // [ATTRIBUTES]
    TextView qs,op1,op2,op3,op4;
    NeumorphCardView Aop1,Aop2,Aop3,Aop4;
    Button next;

    // [COUNT]
    int index = 0;
    int correctCount = 0;
    int wrongCount = 0;
    int timerValue = 20;

    // [TIMER]
    CountDownTimer countDownTimer;
    RoundedHorizontalProgressBar progressBar;

    // [MODEL CLASS]
    quizModel qm;
    List<quizModel> allQs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Hooks();

        allQs=listOfQ;
        Collections.shuffle(allQs);
        qm = listOfQ.get(index);
        Log.i(TAG, "Spark start,");
        Aop1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        Aop2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        Aop3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        Aop4.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        next.setClickable(false);

        countDownTimer = new CountDownTimer(20000,1000) {
            @Override
            public void onTick(long l) {
                timerValue = timerValue - 1;
                progressBar.setProgress(timerValue);
            }

            @Override
            public void onFinish() {
               //Dialog dialog = new Dialog(QuizActivity.this);
               //dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
               //dialog.setContentView(R.layout.time_out_dialog);
               //dialog.show();

                index++;
                qm = listOfQ.get(index);
                resetColor();
                setAllData();
                enableButton();
            }
        }.start();
        setAllData();
    }



    private void Hooks() {
        progressBar = findViewById(R.id.quiz_timer);

        qs = findViewById(R.id.question);
        op1 = findViewById(R.id.option1);
        op2 = findViewById(R.id.option2);
        op3 = findViewById(R.id.option3);
        op4 = findViewById(R.id.option4);

        Aop1 = findViewById(R.id.cardA);
        Aop2 = findViewById(R.id.cardB);
        Aop3 = findViewById(R.id.cardC);
        Aop4 = findViewById(R.id.cardD);

        next = findViewById(R.id.next);
    }

    private void setAllData() {
        qs.setText(qm.getQuestion());
        op1.setText(qm.getOption1());
        op2.setText(qm.getOption2());
        op3.setText(qm.getOption3());
        op4.setText(qm.getOption4());
        timerValue = 20;
        countDownTimer.cancel();
        countDownTimer.start();

    }

    public void correct(NeumorphCardView cardop){
        cardop.setBackgroundColor(getResources().getColor(R.color.correct));
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correctCount++;
                Log.i(TAG, "Spark start,"+index+1);
                Log.i(TAG, "Spark start index, correct");
                if(index<listOfQ.size()-1){
                    index++;
                    qm = listOfQ.get(index);

                    resetColor();
                    setAllData();
                    enableButton();
                }else{
                    won();
                    countDownTimer.cancel();
                    finish();
                }

            }
        });
    }

    public void wrong(NeumorphCardView cardop){
        cardop.setBackgroundColor(getResources().getColor(R.color.wrong));
        showCorrect();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wrongCount++;
                Log.i(TAG, "Spark start,"+index+1);
                Log.i(TAG, "Spark start index, wrong");
                if(index < listOfQ.size()-1){
                    index++;
                    qm = listOfQ.get(index);

                    resetColor();
                    setAllData();
                    enableButton();
                }else{
                    won();
                    countDownTimer.cancel();
                    finish();
                }
            }
        });

    }

    private void showCorrect() {
        qm.getAnswer();
        if(qm.getAnswer().equals(qm.getOption1()))
            Aop1.setBackgroundColor(getResources().getColor(R.color.correct));
        if(qm.getAnswer().equals(qm.getOption2()))
            Aop2.setBackgroundColor(getResources().getColor(R.color.correct));
        if(qm.getAnswer().equals(qm.getOption3()))
            Aop3.setBackgroundColor(getResources().getColor(R.color.correct));
        if(qm.getAnswer().equals(qm.getOption4()))
            Aop4.setBackgroundColor(getResources().getColor(R.color.correct));
        Toast.makeText(this,qm.getAnswer(),Toast.LENGTH_SHORT).show();
    }

    private void won() {
        Intent in = new Intent(QuizActivity.this,WonActivity.class);
        in.putExtra("correct",String.valueOf(correctCount));
        in.putExtra("wrong",String.valueOf(wrongCount));
        in.putExtra("total",String.valueOf(index+1));
        //in.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity(in);
    }

    public void enableButton(){
        Aop1.setClickable(true);
        Aop2.setClickable(true);
        Aop3.setClickable(true);
        Aop4.setClickable(true);
    }

    public void disableButton(){
        Aop1.setClickable(false);
        Aop2.setClickable(false);
        Aop3.setClickable(false);
        Aop4.setClickable(false);
    }

    public void resetColor(){
        Aop1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        Aop2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        Aop3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        Aop4.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    public void optionAclick(View view) {
        disableButton();
        next.setClickable(true);
        if(qm.getOption1().equals(qm.getAnswer())){
            correct(Aop1);
        }else{
            wrong(Aop1);
        }
    }
    public void optionBclick(View view) {
        disableButton();
        next.setClickable(true);
        if(qm.getOption2().equals(qm.getAnswer())){
            correct(Aop2);
        }else{
            wrong(Aop2);
        }
    }
    public void optionCclick(View view) {
        disableButton();
        next.setClickable(true);

        if(qm.getOption3().equals(qm.getAnswer())){
            correct(Aop3);
        }else{
            wrong(Aop3);
        }
    }
    public void optionDclick(View view) {
        disableButton();
        next.setClickable(true);
        if(qm.getOption4().equals(qm.getAnswer())){
            correct(Aop4);
        }else{
            wrong(Aop4);
        }
    }
}