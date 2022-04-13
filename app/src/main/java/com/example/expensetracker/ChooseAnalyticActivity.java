package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ChooseAnalyticActivity extends AppCompatActivity {


    private CardView TodayCardView,WeekCardView,MonthCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_analytic);

        TodayCardView = findViewById(R.id.TodayCardView);
        WeekCardView = findViewById(R.id.WeekCardView);
        MonthCardView = findViewById(R.id.MonthCardView);


        TodayCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseAnalyticActivity.this,DailyAnlayticsActivity.class);
                startActivity(intent);

            }
        });
        WeekCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseAnalyticActivity.this,WeeklyAnalyticsActivity.class);
                startActivity(intent);

            }
        });
        MonthCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseAnalyticActivity.this,MonthlyAnalyticsActivity.class);
                startActivity(intent);

            }
        });
    }


}