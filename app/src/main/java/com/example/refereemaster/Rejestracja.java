package com.example.refereemaster;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;

public class Rejestracja extends AppCompatActivity {
   private EditText mail;
   private EditText nazwaUzy;
    private  EditText pass;
    private  EditText pass2;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejestracja);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();
        nazwaUzy = (EditText)findViewById(R.id.nazwa);
        databaseReference = FirebaseDatabase.getInstance().getReference();
       if(internetIsConnected()==false)
       {
           new AlertDialog.Builder(Rejestracja.this)
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





         mail = (EditText)findViewById(R.id.mail);
        pass = (EditText)findViewById(R.id.pass);
        pass2 = (EditText)findViewById(R.id.pass2);
        Button register = (Button)findViewById(R.id.register);
        TextView textView = (TextView)findViewById(R.id.text);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pass.getText().toString().trim().equals(pass2.getText().toString().trim()))
                {
                    //pass empty
                    Toast.makeText(getApplicationContext(),"Źle powtórzone hasło",Toast.LENGTH_LONG).show();
                    return;
                }
                else
                registerUser();
            }
        });
    }

    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }
    private void registerUser()
    {
        String email = mail.getText().toString().trim();
        String password = pass.getText().toString().trim();

        if(TextUtils.isEmpty(nazwaUzy.getText().toString().trim()))
        {
            //pass empty
            Toast.makeText(this,"Wpisz nazwe użytkownika",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(email))
        {
            //e mail empty
            Toast.makeText(this,"E-mail nie może być pusty",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            //pass empty
            Toast.makeText(this,"Hasło nie może być puste",Toast.LENGTH_LONG).show();
            return;
        }


        //if validations are ok
        progressDialog.setMessage("Rejestrowanie użytkownika...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    Toast.makeText(Rejestracja.this,"Jesteś zarejestrowany",Toast.LENGTH_LONG).show();
                    String name = nazwaUzy.getText().toString().trim();
                    UserName userName = new UserName(name,0,0,0,0,0,0,currentDate,0,0,0);
                    final FirebaseUser user = firebaseAuth.getCurrentUser();
                    databaseReference.child(user.getUid()).setValue(userName);
                    progressDialog.cancel();
                    finish();
                    startActivity(new Intent(getApplicationContext(),Games.class));
                }
                else
                {
                    Toast.makeText(Rejestracja.this,"Nie zostałeś zarejestrowany",Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                }
            }
        });


    }

}
