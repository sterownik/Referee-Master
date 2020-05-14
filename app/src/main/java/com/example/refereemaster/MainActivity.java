package com.example.refereemaster;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;





public class MainActivity extends AppCompatActivity {
    final Context context = this;
    EditText loginEdit;
    EditText hasloEdit;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        ImageView ustawienia = (ImageView)findViewById(R.id.ustawienia);
        Button login = (Button)findViewById(R.id.przycisklogowania);
         loginEdit = (EditText)findViewById(R.id.login);
         hasloEdit = (EditText)findViewById(R.id.haslo);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        if(internetIsConnected()==false)
        {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Uwaga")
                    .setMessage("Nie masz internetu!\nAby aplikacja działała poprawnie włącz internet.")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.

                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        if(firebaseAuth.getCurrentUser()!=null)
        {
            finish();
            startActivity(new Intent(getApplicationContext(),Games.class));
        }
        TextView bezKonta = (TextView)findViewById(R.id.bezkonta);
        ustawienia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettings();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userLogin();


            }
        });
        bezKonta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Rejestracja.class);
                startActivity(intent);
            }
        });



    }
    private void userLogin()
    {
        String email = loginEdit.getText().toString().trim();
        String haslo = hasloEdit.getText().toString().trim();

        if(TextUtils.isEmpty(email))
        {
            //e mail empty
          //  Crouton.cancelAllCroutons();



            Toast.makeText(this,"Login nie może być pusty",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(haslo))
        {
            //pass empty
            Toast.makeText(this,"Hasło nie może być puste",Toast.LENGTH_LONG).show();
            return;
        }
        //if validations are ok
        progressDialog.setMessage("Logowanie użytkownika...");
        progressDialog.show();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        firebaseAuth.signInWithEmailAndPassword(email,haslo).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if(task.isSuccessful())
                {
                    finish();
                    startActivity(new Intent(getApplicationContext(),Games.class));
                    //start profile activity

                }
                else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Błąd!")
                            .setMessage("Wpisałeś błędny login lub hasło.")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.

                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                }

            }
        });
    }
    private void showSettings()
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.settings);
        dialog.setTitle("Title...");
        CheckBox dzwiek = (CheckBox) dialog.findViewById(R.id.dzwiek);
        CheckBox wibracje = (CheckBox) dialog.findViewById(R.id.wibracje);
        dialog.show();
    }

    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }


}
