package com.example.androidlabs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;

public class TestToolbar extends AppCompatActivity {

    private Snackbar sb;
    private String toastMsg = "This is the initial Message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        Toolbar tBar = findViewById(R.id.my_toolbar);
        setSupportActionBar(tBar);

        sb = Snackbar.make(tBar, "", Snackbar.LENGTH_LONG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.item1:
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                break;

            case R.id.item2:
                alert();
                break;

            case R.id.item3:
                sb.setAction("Go Back?", e -> finish());
                sb.show();
                break;

            case R.id.item4:
                Toast.makeText(this, "You clicked on the overflow menu", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    public void alert(){

        View dialogBox = getLayoutInflater().inflate(R.layout.dialog, null);
        EditText et = (EditText)dialogBox.findViewById(R.id.dialog_edit_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setPositiveButton("Positive", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        toastMsg = et.getText().toString();
                    }
                })
                .setNegativeButton("Negative", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).setView(dialogBox);
        builder.create().show();
    }
}
