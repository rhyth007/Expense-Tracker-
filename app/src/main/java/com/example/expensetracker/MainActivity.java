package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private CardView BudgetcardView,TodayCardView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BudgetcardView = findViewById(R.id.BudgetCardView);
        TodayCardView = findViewById(R.id.TodayCardView);
        BudgetcardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,BudgetActivity.class);
                startActivity(intent);
                //finish();
            }
        });
       TodayCardView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent2 = new Intent(MainActivity.this,TodaySpendingActivity.class);
               System.out.println("xxxxxxxx");
               startActivity(intent2);
           }
       });
    }
}