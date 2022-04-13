package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class WeeklyAnalyticsActivity extends AppCompatActivity {

    private Toolbar SettingsToolbar;
    private FirebaseAuth mAuth;

    private  String onLineUserId = "";
    private DatabaseReference expenseRef,personalRef;

    private  TextView monthSpentAmount,MonthRatioSpending;
    private TextView totalBudgetAmountTextView,analyticsTransportAmount,analyticsFoodAmount,analyticsEntertainmentAmount,analyticsHouseAmount,analyticsHealthAmount,analyticsCharityAmount,analyticsPersonalAmount,analyticsOtherAmount;

    private RelativeLayout linearLayoutAnalysis,relativeLayoutTransport,relativeLayoutFood,relativeLayoutEntertainment,relativeLayoutHouse,relativeLayoutHealth,relativeLayoutCharity,relativeLayoutPersonal,relativeLayoutOther;


    private AnyChartView anyChartView;
    private  TextView progress_ratio_Transport,progress_ratio_Food,progress_ratio_Entertainment,progress_ratio_House,progress_ratio_Health,progress_ratio_Charity,progress_ratio_Personal,progress_ratio_Other;
    private ImageView MonthRatioSpending_image, status_image__Transport,status_image__Food,status_image__Entertainment,status_image__House,status_image__Health,status_image__Charity,status_image__Personal,status_image__Other;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_analytics);
        getSupportActionBar().setTitle("Weekly Analytics");

        mAuth = FirebaseAuth.getInstance();
        onLineUserId = mAuth.getCurrentUser().getUid();
        expenseRef = FirebaseDatabase.getInstance().getReference("expenses").child(onLineUserId);
        personalRef = FirebaseDatabase.getInstance().getReference("personal").child(onLineUserId);




        getSupportActionBar().setTitle("Weekly Analytics");


        mAuth = FirebaseAuth.getInstance();
        onLineUserId = mAuth.getCurrentUser().getUid();
        expenseRef = FirebaseDatabase.getInstance().getReference("expenses").child(onLineUserId);
        personalRef = FirebaseDatabase.getInstance().getReference("personal").child(onLineUserId);


        totalBudgetAmountTextView = findViewById(R.id.totalAmountSpentOn);


        //Analytics
        monthSpentAmount  = findViewById(R.id.monthSpentAmount);
        linearLayoutAnalysis = findViewById(R.id.linearLayoutAnalysis);
        MonthRatioSpending = findViewById(R.id.monthRatioSpending);
        MonthRatioSpending_image = findViewById(R.id.monthRatioSpending_img);


        analyticsTransportAmount = findViewById(R.id.analyticsTransportAmount);
        analyticsFoodAmount = findViewById(R.id.analyticsFoodAmount);
        analyticsEntertainmentAmount = findViewById(R.id.analyticsEntertainmentAmount);
        analyticsHouseAmount = findViewById(R.id.analyticsHouseAmount);
        analyticsHealthAmount = findViewById(R.id.analyticsHealthAmount);
        analyticsCharityAmount = findViewById(R.id.analyticsCharityAmount);
        analyticsPersonalAmount = findViewById(R.id.analyticsPersonalAmount);
        analyticsOtherAmount= findViewById(R.id.analyticsOtherAmount);

        //     //Relative
        relativeLayoutTransport= findViewById(R.id.relativeLayoutTransport);
        relativeLayoutFood = findViewById(R.id.relativeLayoutFood);
        relativeLayoutEntertainment= findViewById(R.id.relativeLayoutEntertainment);
        relativeLayoutHouse = findViewById(R.id.relativeLayoutHouse);
        relativeLayoutHealth= findViewById(R.id.relativeLayoutHealth);
        relativeLayoutCharity= findViewById(R.id.relativeLayoutCharity);
        relativeLayoutPersonal= findViewById(R.id.relativeLayoutPersonal);
        relativeLayoutOther= findViewById(R.id.relativeLayoutOther);

        progress_ratio_Transport = findViewById(R.id.progress_ratio_transport);
        progress_ratio_Food = findViewById(R.id.progress_ratio_food);
        progress_ratio_Entertainment = findViewById(R.id.progress_ratio_entertainment);
        progress_ratio_House = findViewById(R.id.progress_ratio_house);
        progress_ratio_Health= findViewById(R.id.progress_ratio_health);
        progress_ratio_Charity= findViewById(R.id.progress_ratio_charity);
        progress_ratio_Personal= findViewById(R.id.progress_ratio_personal);
        progress_ratio_Other= findViewById(R.id.progress_ratio_other);
        MonthRatioSpending = findViewById(R.id.monthRatioSpending);


        //Image View

        status_image__Transport = findViewById(R.id.status_image__Transport);
        status_image__Food= findViewById(R.id.status_image__Food);
        status_image__Entertainment= findViewById(R.id.status_image__Entertainment);
        status_image__House= findViewById(R.id.status_image__House);
        status_image__Health= findViewById(R.id.status_image__Health);
        status_image__Charity= findViewById(R.id.status_image__Charity);
        status_image__Personal= findViewById(R.id.status_image__Personal);
        status_image__Other= findViewById(R.id.status_image__Other);
        MonthRatioSpending_image  = findViewById(R.id.monthRatioSpending_img);


        anyChartView = findViewById(R.id.anyChartView);


        getTotalDailyTransportExpenses();
        getTotalDailyFoodExpenses();
        getTotalDailyEntertainmentExpenses();
        getTotalDailyHouseExpenses();
        getTotalDailyHealthExpenses();
        getTotalDailyCharityExpenses();
        getTotalDailyPersonalExpenses();
        getTotalDailyOtherExpenses();
        getTotalDayExpenses();


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                loadGraph();
                setStatusAndImageResource();
            }
        }, 4000);


    }

    private void getTotalDailyOtherExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch,now);
        String itemNday = "Other"+weeks.getWeeks();



        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(mAuth.getCurrentUser().getUid());
        Query query = reference.orderByChild("itemweek").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    int totalAmount =0;
                    for (DataSnapshot ds : snapshot.getChildren()) {

                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsOtherAmount.setText("\u20B9" + String.valueOf(totalAmount));
                    }
                    personalRef.child("weekOther").setValue(totalAmount);
                }
                else{
                    relativeLayoutOther.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTotalDailyPersonalExpenses() {

        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch,now);
        String itemNday = "Personal"+weeks.getWeeks();



        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(mAuth.getCurrentUser().getUid());
        Query query = reference.orderByChild("itemweek").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    int totalAmount =0;
                    for (DataSnapshot ds : snapshot.getChildren()) {

                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsPersonalAmount.setText("\u20B9" + String.valueOf(totalAmount));
                    }
                    personalRef.child("weekPersonal").setValue(totalAmount);
                }
                else{
                    relativeLayoutPersonal.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTotalDailyCharityExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch,now);
        String itemNday = "Charity"+weeks.getWeeks();



        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(mAuth.getCurrentUser().getUid());
        Query query = reference.orderByChild("itemweek").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    int totalAmount =0;
                    for (DataSnapshot ds : snapshot.getChildren()) {

                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsCharityAmount.setText("\u20B9" + String.valueOf(totalAmount));
                    }
                    personalRef.child("weekCharity").setValue(totalAmount);
                }
                else{
                    relativeLayoutCharity.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTotalDailyHealthExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch,now);
        String itemNday = "Health"+weeks.getWeeks();



        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(mAuth.getCurrentUser().getUid());
        Query query = reference.orderByChild("itemweek").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    int totalAmount =0;
                    for (DataSnapshot ds : snapshot.getChildren()) {

                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsHealthAmount.setText("\u20B9" + String.valueOf(totalAmount));
                    }
                    personalRef.child("weekHealth").setValue(totalAmount);
                }
                else{
                    relativeLayoutHealth.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTotalDailyHouseExpenses() {

        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch,now);
        String itemNday = "House"+weeks.getWeeks();



        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(mAuth.getCurrentUser().getUid());
        Query query = reference.orderByChild("itemweek").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    int totalAmount =0;
                    for (DataSnapshot ds : snapshot.getChildren()) {

                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsHouseAmount.setText("\u20B9" + String.valueOf(totalAmount));
                    }
                    personalRef.child("weekHouse").setValue(totalAmount);
                }
                else{
                    relativeLayoutHouse.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTotalDailyEntertainmentExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch,now);
        String itemNday = "Entertainment"+weeks.getWeeks();



        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(mAuth.getCurrentUser().getUid());
        Query query = reference.orderByChild("itemweek").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    int totalAmount =0;
                    for (DataSnapshot ds : snapshot.getChildren()) {

                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsEntertainmentAmount.setText("\u20B9" + String.valueOf(totalAmount));
                    }
                    personalRef.child("weekEntertainment").setValue(totalAmount);
                }
                else{
                    relativeLayoutEntertainment.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTotalDailyFoodExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch,now);
        String itemNday = "Food"+weeks.getWeeks();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(mAuth.getCurrentUser().getUid());
        Query query = reference.orderByChild("itemweek").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    int totalAmount =0;
                    for (DataSnapshot ds : snapshot.getChildren()) {

                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsFoodAmount.setText("\u20B9" + String.valueOf(totalAmount));
                    }
                    personalRef.child("weekFood").setValue(totalAmount);
                }
                else{
                    relativeLayoutFood.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getTotalDailyTransportExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch,now);
        String itemNday = "Transport"+weeks.getWeeks();



        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(mAuth.getCurrentUser().getUid());
        Query query = reference.orderByChild("itemweek").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    int totalAmount =0;
                    for (DataSnapshot ds : snapshot.getChildren()) {

                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsTransportAmount.setText("\u20B9" + String.valueOf(totalAmount));
                    }
                    personalRef.child("weekTrans").setValue(totalAmount);
                }
                else{
                    relativeLayoutTransport.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTotalDayExpenses() {


        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch,now);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(mAuth.getCurrentUser().getUid());
        Query query = reference.orderByChild("week").equalTo(weeks.getWeeks());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists() && snapshot.getChildrenCount()>0)  {
                    int totalAmount =0;
                    for (DataSnapshot ds : snapshot.getChildren()) {

                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                    }
                    totalBudgetAmountTextView.setText("Total Week's Spending \u20B9 "+totalAmount);
                    monthSpentAmount.setText("Total Spent \u20B9 "+totalAmount);
                }
                else{
                    totalBudgetAmountTextView.setText("You've not spent this Week ");
                    anyChartView.setVisibility(View.GONE);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void loadGraph(){
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    int traTotal;
                    if (snapshot.hasChild("weekTrans")) {
                        traTotal = Integer.parseInt(snapshot.child("weekTrans").getValue().toString());
                    } else {
                        traTotal = 0;
                    }

                    int foodTotal;
                    if (snapshot.hasChild("weekFood")) {
                        foodTotal = Integer.parseInt(snapshot.child("weekFood").getValue().toString());
                    } else {
                        foodTotal = 0;
                    }
                    int EntertainmentTotal;
                    if (snapshot.hasChild("weekEntertainment")) {
                        EntertainmentTotal = Integer.parseInt(snapshot.child("weekEntertainment").getValue().toString());
                    } else {
                        EntertainmentTotal = 0;
                    }
                    int HealthTotal;
                    if (snapshot.hasChild("weekHealth")) {
                        HealthTotal = Integer.parseInt(snapshot.child("weekHealth").getValue().toString());
                    } else {
                        HealthTotal = 0;
                    }
                    int HouseTotal;
                    if (snapshot.hasChild("weekHouse")) {
                        HouseTotal = Integer.parseInt(snapshot.child("weekHouse").getValue().toString());
                    } else {
                        HouseTotal = 0;
                    }
                    int CharityTotal;
                    if (snapshot.hasChild("weekCharity")) {
                        CharityTotal = Integer.parseInt(snapshot.child("weekCharity").getValue().toString());
                    } else {
                        CharityTotal = 0;
                    }
                    int PersonalTotal;
                    if (snapshot.hasChild("weekPersonal")) {
                        PersonalTotal = Integer.parseInt(snapshot.child("weekPersonal").getValue().toString());
                    } else {
                        PersonalTotal = 0;
                    }
                    int OtherTotal;
                    if (snapshot.hasChild("weekOther")) {
                        OtherTotal = Integer.parseInt(snapshot.child("weekOther").getValue().toString());
                    } else {
                        OtherTotal = 0;
                    }

                    Pie pie = AnyChart.pie();
                    List<DataEntry> data = new ArrayList<>();
                    data.add(new ValueDataEntry("Transport", traTotal));
                    data.add(new ValueDataEntry("Food", foodTotal));
                    data.add(new ValueDataEntry("Entertainment", EntertainmentTotal));
                    data.add(new ValueDataEntry("House", HouseTotal));
                    System.out.println("Health"+HealthTotal);
                    data.add(new ValueDataEntry("Health", HealthTotal));
                    data.add(new ValueDataEntry("Charity", CharityTotal));
                    data.add(new ValueDataEntry("Personal", PersonalTotal));
                    data.add(new ValueDataEntry("Other", OtherTotal));

//                    data.add(new ValueDataEntry("Transport", 1000));
//                    data.add(new ValueDataEntry("Food", 2000));
//                    data.add(new ValueDataEntry("Entertainment", 400));
//                    data.add(new ValueDataEntry("House", 300));
//                    data.add(new ValueDataEntry("Health", 850));
//                    data.add(new ValueDataEntry("Charity", 750));
//                    data.add(new ValueDataEntry("Personal", 900));
//                    data.add(new ValueDataEntry("Other", 2500));

                    pie.data(data);

                    pie.title("Daily Analytics");
                    pie.labels().position("outside");
                    pie.legend().title().enabled(true);
                    pie.legend().title().text("Items Spent On").padding(0d, 0d, 10d, 0d);
                    pie.legend().position("center-bottom").itemsLayout(LegendLayout.HORIZONTAL).align(Align.CENTER);
                    anyChartView.setChart(pie);

                }
                else{
                    Toast.makeText(WeeklyAnalyticsActivity.this,"Child Doesnt Exist",Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void setStatusAndImageResource(){
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {

                    float traTotal;
                    if (snapshot.hasChild("weekTrans")) {
                        traTotal = Integer.parseInt(snapshot.child("weekTrans").getValue().toString());
                    } else {
                        traTotal = 0;
                    }

                    float foodTotal;
                    if (snapshot.hasChild("weekFood")) {
                        foodTotal = Integer.parseInt(snapshot.child("weekFood").getValue().toString());
                    } else {
                        foodTotal = 0;
                    }
                    float EntertainmentTotal;
                    if (snapshot.hasChild("weekEntertainment")) {
                        EntertainmentTotal = Integer.parseInt(snapshot.child("weekEntertainment").getValue().toString());
                    } else {
                        EntertainmentTotal = 0;
                    }
                    float HealthTotal;
                    if (snapshot.hasChild("weekHealth")) {
                        HealthTotal = Integer.parseInt(snapshot.child("weekHealth").getValue().toString());
                    } else {
                        HealthTotal = 0;
                    }
                    float HouseTotal;
                    if (snapshot.hasChild("weekHouse")) {
                        HouseTotal = Integer.parseInt(snapshot.child("weekHouse").getValue().toString());
                    } else {
                        HouseTotal = 0;
                    }
                    float CharityTotal;
                    if (snapshot.hasChild("weekCharity")) {
                        CharityTotal = Integer.parseInt(snapshot.child("weekCharity").getValue().toString());
                    } else {
                        CharityTotal = 0;
                    }
                    float PersonalTotal;
                    if (snapshot.hasChild("weekPersonal")) {
                        PersonalTotal = Integer.parseInt(snapshot.child("weekPersonal").getValue().toString());
                        System.out.println("PersonalTotal "+PersonalTotal);
                    } else {
                        PersonalTotal = 0;
                    }
                    float OtherTotal;
                    if (snapshot.hasChild("weekOther")) {
                        OtherTotal = Integer.parseInt(snapshot.child("dayOther").getValue().toString());
                    } else {
                        OtherTotal = 0;
                    }

                    float monthTotalSpentAmount;
                    if (snapshot.hasChild("week")) {
                        monthTotalSpentAmount = Integer.parseInt(snapshot.child("week").getValue().toString());
                    } else {
                        monthTotalSpentAmount = 0;
                    }


                    ///Getting Ratios


                    float traRatio;
                    if (snapshot.hasChild("weekTransRatio")) {
                        traRatio = Integer.parseInt(snapshot.child("weekTransRatio").getValue().toString());
                    } else {
                        traRatio = 0;
                    }

                    float foodRatio;
                    if (snapshot.hasChild("weekFood")) {
                        foodRatio = Integer.parseInt(snapshot.child("weekFoodRatio").getValue().toString());
                    } else {
                        foodRatio = 0;
                    }
                    float EntertainmentRatio;
                    if (snapshot.hasChild("weekEntertainmentRatio")) {
                        EntertainmentRatio = Integer.parseInt(snapshot.child("weekEntertainmentRatio").getValue().toString());
                    } else {
                        EntertainmentRatio = 0;
                    }
                    float HealthRatio;
                    if (snapshot.hasChild("weekHealthRatio")) {
                        HealthRatio = Integer.parseInt(snapshot.child("weekHealthRatio").getValue().toString());
                    } else {
                        HealthRatio = 0;
                    }
                    float HouseRatio;
                    if (snapshot.hasChild("weekHouseRatio")) {
                        HouseRatio = Integer.parseInt(snapshot.child("weekHouseRatio").getValue().toString());
                    } else {
                        HouseRatio = 0;
                    }
                    float CharityRatio;
                    if (snapshot.hasChild("weekCharityRatio")) {
                        CharityRatio = Integer.parseInt(snapshot.child("weekCharityRatio").getValue().toString());
                    } else {
                        CharityRatio = 0;
                    }
                    float PersonalRatio;
                    if (snapshot.hasChild("weekPersonalRatio")) {
                        PersonalRatio = Integer.parseInt(snapshot.child("weekPersonalRatio").getValue().toString());
                    } else {
                        PersonalRatio = 0;
                    }
                    float OtherRatio;
                    if (snapshot.hasChild("weekOtherRatio")) {
                        OtherRatio = Integer.parseInt(snapshot.child("weekOtherRatio").getValue().toString());
                    } else {
                        OtherRatio = 0;
                    }

                    float monthTotalSpentAmountRatio;
                    if (snapshot.hasChild("weeklybudget")) {
                        monthTotalSpentAmountRatio = Integer.parseInt(snapshot.child("weeklybudget").getValue().toString());
                    } else {
                        monthTotalSpentAmountRatio = 0;
                    }


                    float monthPercent = (monthTotalSpentAmount/monthTotalSpentAmountRatio)*100;

                    System.out.println("MONTH PERCENT"+monthPercent);
                    if(monthPercent<50){
                        MonthRatioSpending.setText(monthPercent+" %"+" used of "+monthTotalSpentAmountRatio+" \u20B9");
                        MonthRatioSpending_image.setImageResource(R.drawable.green);

                    }
                    else if(monthPercent>=50 && monthPercent<100 ){
                        MonthRatioSpending.setText(monthPercent+" %"+" used of "+monthTotalSpentAmountRatio+" \u20B9");
                        MonthRatioSpending_image.setImageResource(R.drawable.brown);

                    }else{
                        MonthRatioSpending.setText(monthPercent+" %"+" used of "+monthTotalSpentAmountRatio+" \u20B9");
                        MonthRatioSpending_image.setImageResource(R.drawable.red);
                    }


                    float TransPercent = (traTotal/traRatio)*100;

                    if(TransPercent<50){
                        progress_ratio_Transport.setText(TransPercent+"%"+"used of "+traRatio+" \u20B9");
                        status_image__Transport.setImageResource(R.drawable.green);

                    }
                    else if(TransPercent>=50 && TransPercent<100 ){
                        progress_ratio_Transport.setText(TransPercent+"%"+"used of "+traRatio+" \u20B9");
                        status_image__Transport.setImageResource(R.drawable.brown);

                    }else{
                        progress_ratio_Transport.setText(TransPercent+"%"+"used of "+traRatio+" \u20B9");
                        status_image__Transport.setImageResource(R.drawable.red);
                    }


                    float FoodPercent = (foodTotal/foodRatio)*100;

                    if(FoodPercent<50){
                        progress_ratio_Food.setText(FoodPercent+" %"+" used of "+foodRatio+" \u20B9");
                        status_image__Food.setImageResource(R.drawable.green);

                    }
                    else if(FoodPercent>=50 && FoodPercent<100 ){
                        progress_ratio_Food.setText(FoodPercent+" %"+" used of "+foodRatio+" \u20B9");
                        status_image__Food.setImageResource(R.drawable.brown);

                    }else{
                        progress_ratio_Food.setText(FoodPercent+" %"+" used of "+foodRatio+" \u20B9");
                        status_image__Food.setImageResource(R.drawable.red);
                    }


                    float entPercent = (EntertainmentTotal/EntertainmentRatio)*100;

                    if(entPercent<50){
                        progress_ratio_Entertainment.setText(entPercent+" %"+" used of "+EntertainmentRatio+" \u20B9");
                        status_image__Entertainment.setImageResource(R.drawable.green);

                    }
                    else if(entPercent>=50 && entPercent<100 ){
                        progress_ratio_Entertainment.setText(entPercent+" %"+" used of "+EntertainmentRatio+" \u20B9");
                        status_image__Entertainment.setImageResource(R.drawable.brown);

                    }else{
                        progress_ratio_Entertainment.setText(entPercent+" %"+" used of "+EntertainmentRatio+" \u20B9");
                        status_image__Entertainment.setImageResource(R.drawable.red);
                    }

                    float HousePercent = (HouseTotal/HouseRatio)*100;

                    if(HousePercent<50){
                        progress_ratio_House.setText(HousePercent+" %"+" used of "+HouseRatio+" \u20B9. Status");
                        status_image__House.setImageResource(R.drawable.green);

                    }
                    else if(HousePercent>=50 && HousePercent<100 ){
                        progress_ratio_House.setText(HousePercent+" %"+" used of "+HouseRatio+" \u20B9");
                        status_image__House.setImageResource(R.drawable.brown);

                    }else{
                        progress_ratio_House.setText(HousePercent+" %"+" used of "+HouseRatio+" \u20B9");
                        status_image__House.setImageResource(R.drawable.red);
                    }

                    float HealthPercent = (HealthTotal/HealthRatio)*100;

                    if(HealthPercent<50){
                        progress_ratio_Health.setText(HealthPercent+" %"+" used of "+HealthRatio+" \u20B9");
                        status_image__Health.setImageResource(R.drawable.green);

                    }
                    else if(HealthPercent>=50 && HealthPercent<100 ){
                        progress_ratio_Health.setText(HealthPercent+" %"+" used of "+HealthRatio+" \u20B9");
                        status_image__Health.setImageResource(R.drawable.brown);

                    }else{
                        progress_ratio_Health.setText(HealthPercent+" %"+" used of "+HealthRatio+" \u20B9");
                        status_image__Health.setImageResource(R.drawable.red);
                    }


                    float CharityPercent = (CharityTotal/CharityRatio)*100;

                    if(CharityPercent<50){
                        progress_ratio_Charity.setText(CharityPercent+" %"+" used of "+CharityRatio+" \u20B9");
                        status_image__Charity.setImageResource(R.drawable.green);

                    }
                    else if(CharityPercent>=50 && CharityPercent<100 ){
                        progress_ratio_Charity.setText(CharityPercent+" %"+" used of "+CharityRatio+" \u20B9");
                        status_image__Charity.setImageResource(R.drawable.brown);

                    }else{
                        progress_ratio_Charity.setText(CharityPercent+" %"+" used of "+CharityRatio+" \u20B9");
                        status_image__Charity.setImageResource(R.drawable.red);
                    }

                    float PersonalPercent = (PersonalTotal/PersonalRatio)*100;

                    if(PersonalPercent<50){
                        progress_ratio_Personal.setText(PersonalPercent+" %"+" used of "+PersonalRatio+" \u20B9");
                        status_image__Personal.setImageResource(R.drawable.green);

                    }
                    else if(PersonalPercent>=50 && PersonalPercent<100 ){
                        progress_ratio_Personal.setText(PersonalPercent+" %"+" used of "+PersonalRatio+" \u20B9");
                        status_image__Personal.setImageResource(R.drawable.brown);

                    }else{
                        progress_ratio_Personal.setText(PersonalPercent+" %"+" used of "+PersonalRatio+" \u20B9");
                        status_image__Personal.setImageResource(R.drawable.red);
                    }


                    float OtherPercent = (OtherTotal/OtherRatio)*100;

                    if(OtherPercent<50){
                        progress_ratio_Other.setText(OtherPercent+" \u20B9"+" used of "+OtherRatio+" \u20B9");
                        status_image__Other.setImageResource(R.drawable.green);

                    }
                    else if(OtherPercent>=50 && OtherPercent<100 ){
                        progress_ratio_Other.setText(OtherPercent+" \u20B9"+" used of "+OtherRatio+" \u20B9");
                        status_image__Other.setImageResource(R.drawable.brown);

                    }else{
                        progress_ratio_Other.setText(OtherPercent+" \u20B9"+" used of "+OtherRatio+" \u20B9");
                        status_image__Other.setImageResource(R.drawable.red);
                    }








                }
                else{
                    Toast.makeText(WeeklyAnalyticsActivity.this,"Child Doesnt Exist",Toast.LENGTH_SHORT);
                }







            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
