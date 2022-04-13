package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private CardView BudgetcardView,TodayCardView,WeekCardView,MonthCardView,AnalyticCardView,HistoryCardView;
    private TextView budgetTextView,todayTextView,WeekTextView,MonthTextView,SavingTextView;

    private FirebaseAuth mAuth;
    private DatabaseReference budgetRef,expensesRef,personalRef;
    private  String onlineUserID = "";

    private  int TotalAMountMonth = 0;
   private int TotalAMountBudget = 0;
   private int TotalAmountBudgetB=0;
   private int TotalAmountBudgetC = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BudgetcardView = findViewById(R.id.BudgetCardView);
        TodayCardView = findViewById(R.id.TodayCardView);
        WeekCardView = findViewById(R.id.WeekCardView);
        MonthCardView = findViewById(R.id.MonthCardView);
        AnalyticCardView = findViewById(R.id.SavingCardView);
        HistoryCardView = findViewById(R.id.HistoryCardView);
        getSupportActionBar().setTitle("Personal Expense Tracker");

     budgetTextView = findViewById(R.id.budgetTextView);
     WeekTextView = findViewById(R.id.WeekTextView);
     MonthTextView = findViewById(R.id.MonthTextView);
     SavingTextView = findViewById(R.id.SavingTextView);
        todayTextView= findViewById(R.id.todayTextView);

     mAuth = FirebaseAuth.getInstance();
     onlineUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
     budgetRef = FirebaseDatabase.getInstance().getReference("budget").child(onlineUserID);
     expensesRef = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserID);
     personalRef = FirebaseDatabase.getInstance().getReference("personal").child(onlineUserID);





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
        AnalyticCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(MainActivity.this,ChooseAnalyticActivity.class);
                intent4.putExtra("type","month");
                startActivity(intent4);
            }
        });
        HistoryCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(MainActivity.this,HistoryActivity.class);
                startActivity(intent4);
            }
        });

        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0){
                    for (DataSnapshot ds : snapshot.getChildren()) {

                        Map<String,Object> map = (Map<String, Object>)ds.getValue();
                        Object total  = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        TotalAmountBudgetB += pTotal;
                    }
                    TotalAmountBudgetC = TotalAmountBudgetB;
                    personalRef.child("budget").setValue(TotalAmountBudgetC);
                }else{
                    personalRef.child("budget").setValue(0);
                    Toast.makeText(MainActivity.this,"Please Set a Budget",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

           getBudgetAmount();
           getTodaySpentAmount();
           getWeekSpentAmount();
           getMonthSpentAmount();
           getSavings();

    }

    private void getBudgetAmount() {
        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0){
                    for (DataSnapshot ds : snapshot.getChildren()) {

                        Map<String,Object> map = (Map<String, Object>)ds.getValue();
                        Object total  = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        TotalAMountBudget += pTotal;
                        budgetTextView.setText("\u20B9"+String.valueOf(TotalAMountBudget));
                    }

                }else{
                    TotalAMountBudget =0;
                    budgetTextView.setText(" \u20B9"+String.valueOf(0));
                    Toast.makeText(MainActivity.this,"Please Set a Budget",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getTodaySpentAmount() {

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserID);
        Query query = reference.orderByChild("date").equalTo(date);


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int totalAmount =0;
                    for (DataSnapshot ds : snapshot.getChildren()) {

                        Map<String,Object> map = (Map<String, Object>)ds.getValue();
                        Object total  = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        todayTextView.setText("\u20B9"+String.valueOf(totalAmount));
                    }
                    personalRef.child("today").setValue(totalAmount);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void getWeekSpentAmount() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch,now);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserID);
        Query query = reference.orderByChild("week").equalTo(weeks.getWeeks());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int totalAmount =0;
                for (DataSnapshot ds : snapshot.getChildren()) {

                    Map<String,Object> map = (Map<String, Object>)ds.getValue();
                    Object total  = map.get("amount");
                    int pTotal = Integer.parseInt(String.valueOf(total));
                    totalAmount += pTotal;
                    WeekTextView.setText("\u20B9"+String.valueOf(totalAmount));
                }
                personalRef.child("week").setValue(totalAmount);
                TotalAMountMonth = totalAmount;
                //totalAmountRemaining =TotalAmountBudgetC+totalAmount;



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void getMonthSpentAmount() {


        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch,now);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserID);
        Query query = reference.orderByChild("month").equalTo(months.getMonths());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int totalAmount =0;
                for (DataSnapshot ds : snapshot.getChildren()) {

                    Map<String,Object> map = (Map<String, Object>)ds.getValue();
                    Object total  = map.get("amount");
                    int pTotal = Integer.parseInt(String.valueOf(total));
                    totalAmount += pTotal;
                    MonthTextView.setText("\u20B9"+String.valueOf(totalAmount));
                }
                personalRef.child("month").setValue(totalAmount);
                TotalAMountMonth = totalAmount;
                //totalAmountRemaining =TotalAmountBudgetC+totalAmount;



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getSavings() {


        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    int budget;
                    if(snapshot.hasChild("budget")){
                        budget =Integer.parseInt(snapshot.child("budget").getValue().toString());
                        System.out.println(budget);
                    }else{
                        budget = 0;
                        System.out.println(budget);
                    }
                    int monthSpending;
                    if(snapshot.hasChild("month")){
                       monthSpending = Integer.parseInt(Objects.requireNonNull(snapshot.child("month").getValue().toString()));
                        System.out.println(monthSpending);
                    }else{
                        monthSpending = 0;
                    }

                    int savings = budget - monthSpending;
                    SavingTextView.setText("\u20B9"+savings);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.account){
            Intent intent = new Intent(MainActivity.this,AcountActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}