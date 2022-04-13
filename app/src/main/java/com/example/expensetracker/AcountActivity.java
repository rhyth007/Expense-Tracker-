package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class AcountActivity extends AppCompatActivity {

    private TextView userEmail;
    private Button logoutBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("My Account");
        setContentView(R.layout.activity_acount);
        userEmail = findViewById(R.id.userEmail);
        logoutBtn = findViewById(R.id.logoutBtn);

        userEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(AcountActivity.this).setTitle("Expense Tracker")
                        .setMessage("Are you sure you want to Sign out!!!").setCancelable(false)
                        .setPositiveButton("Yes",(dialogInterface, i) ->{
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(AcountActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();

                        })
                        .setNegativeButton("No",null).show();
            }
        });




    }
}