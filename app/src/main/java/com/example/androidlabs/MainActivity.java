package com.example.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {

    SharedPreferences share;
    EditText emailField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_lab3);

        //loading the sharedpreferences and users email address
        Button nextButton = (Button)findViewById(R.id.buttonLogin);//button from login page
        emailField = (EditText)findViewById(R.id.editTextEmail);
        share = getSharedPreferences("FileName", Context.MODE_PRIVATE);
        String saveEmail = share.getString("ReserveName", "Default value");
        emailField.setText(saveEmail);
        nextButton.setOnClickListener( b -> {

            //Give directions to go from this page, to SecondActivity
            Intent nextPage = new Intent(MainActivity.this, ProfileActivity.class);

            //   EditText et =(EditText)findViewById(R.id.)
            nextPage.putExtra("emailType", emailField.getText().toString());
            //Now make the transition:
            startActivityForResult(nextPage, 345);
        });



    }

    //in the onPause, used sharedPreferences to save the users email address
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = share.edit();
        String typeEmail  = emailField.getText().toString();
        editor.putString("ReserveName", typeEmail);
        editor.commit();
    }
}