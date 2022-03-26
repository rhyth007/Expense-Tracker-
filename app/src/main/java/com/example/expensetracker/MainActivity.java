package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private CardView BudgetcardView,TodayCardView,WeekCardView,MonthCardView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BudgetcardView = findViewById(R.id.BudgetCardView);
        TodayCardView = findViewById(R.id.TodayCardView);
        WeekCardView = findViewById(R.id.WeekCardView);
        MonthCardView = findViewById(R.id.MonthCardView);
        BudgetcardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,BudgetActivity.class);
                startActivity(intent);

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

        WeekCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(MainActivity.this,WeekSpendingActivity.class);
                intent3.putExtra("type","week");
                startActivity(intent3);
            }
        });

        MonthCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(MainActivity.this,WeekSpendingActivity.class);
                intent4.putExtra("type","month");
                startActivity(intent4);
            }
        });
    }
}