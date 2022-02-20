package com.example.learn.Fragment_main.Quizz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.example.learn.Fragment_main.Fragment.PagerAdaptor.Student_content;
import com.example.learn.Fragment_main.home_fragment;
import com.example.learn.R;

public class WonActivity extends AppCompatActivity {

    TextView qs,points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won);
        qs = (TextView) findViewById(R.id.qs_AW);
        points = (TextView) findViewById(R.id.points_AW);

        // getting data
        Intent intent = getIntent();
        String crt = intent.getStringExtra("correct");
        String wrg = intent.getStringExtra("wrong");
        String tot = intent.getStringExtra("total");

        qs.setText(tot);
        points.setText(crt);

    }

    //@Override
    //public void onBackPressed() {
    //    super.onBackPressed();
    //    Intent i = new Intent(WonActivity.this,home_fragment.class);
    //    i.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP);
    //    startActivity(i);
    //    //finish();
    //}

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            //finish();
            startActivity(new Intent(WonActivity.this, home_fragment.class));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/
}