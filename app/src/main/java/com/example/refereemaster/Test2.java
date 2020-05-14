package com.example.refereemaster;




import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import static android.view.Window.FEATURE_NO_TITLE;
import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;

public class Test2 extends AppCompatActivity {
    RelativeLayout simple,calagra;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    boolean czyzmienilo;
    ArrayList<String> lista = new ArrayList<String>();
    TextView odliczanie,punktylay,iloscpytantxt;
    String tmp2 = " ";
    int ilosc = 0;
    int dobre = 0;
    int w,odliczanie2;

    int j = 0;
    boolean czas1;
    boolean play;
    CountDownTimer mCountDownTimer,mCountDownTimer2;

    @Override
    public void onBackPressed() {
        mCountDownTimer.cancel();
        startActivity(new Intent(getApplicationContext(),Games.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egzamin);
        firebaseAuth = FirebaseAuth.getInstance();
        odliczanie=(TextView)findViewById(R.id.odliczanie);
        calagra=(RelativeLayout)findViewById(R.id.calagra);
        punktylay=(TextView)findViewById(R.id.punktylay);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        getSupportActionBar().hide();
        iloscpytantxt=(TextView)findViewById(R.id.iloscpytan);


        odliczanie2=4;

        new AlertDialog.Builder(Test2.this)
                .setTitle("Informacja")
                .setMessage("Witam w ostatecznym teście progresu po użytkowaniu aplikacji Referee Master.")
                .setCancelable(false).setIcon(R.drawable.ikonagrydobra)
                .setPositiveButton("Rozumiem", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Whatever...
                        mCountDownTimer2 = new CountDownTimer(3000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                // Log.v("Log_tag", "Tick of Progress"+ i+ millisUntilFinished);

                                odliczanie2--;
                                Animation animationprzyciski= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.mixed_anim);
                                odliczanie.startAnimation(animationprzyciski);
                                odliczanie.setText(odliczanie2+"");

                            }
                            @Override
                            public void onFinish() {
                                //Do what you want
                                odliczanie.setVisibility(View.INVISIBLE);
                                odliczanie.clearAnimation();
                                calagra.setVisibility(View.VISIBLE);
                                mCountDownTimer2.cancel();
                                //    Toast.makeText(getApplicationContext(),"odlicza sie",Toast.LENGTH_LONG).show();
                                try {
                                    Play();
                                } catch (IOException e) {
                                    Toast.makeText(getApplicationContext(), "Były problemy z plikiem", Toast.LENGTH_LONG).show();
                                }
                            }
                        }.start();

                    }
                }).show();




    }





    @SuppressLint("ResourceType")
    public void Play() throws IOException
    {
        czas1 = true;
        play=true;


        final ArrayList<Integer> lista = new ArrayList<Integer>();



        simple = (RelativeLayout)findViewById(R.id.rel);

        final CheckBox jedynka=(CheckBox)findViewById(R.id.pierwszy);
        final CheckBox dwojka=(CheckBox)findViewById(R.id.drugi);
        final CheckBox trojka=(CheckBox)findViewById(R.id.trzeci);
        final CheckBox czwarty=(CheckBox)findViewById(R.id.czwarty);
        final CheckBox piaty=(CheckBox)findViewById(R.id.piaty);
        final CheckBox szosty=(CheckBox)findViewById(R.id.szosty);
        final CheckBox siodmy=(CheckBox)findViewById(R.id.siodmy);
        final CheckBox osmy = (CheckBox)findViewById(R.id.osmy);
        final CheckBox dziewiaty = (CheckBox)findViewById(R.id.dziewiaty);
        final TextView pt = (TextView)findViewById(R.id.pytanie);
        final ProgressBar czasomierz = (ProgressBar)findViewById(R.id.czas);
        final CheckBox[] checkBoxes = {jedynka,dwojka,trojka,czwarty,piaty,szosty,siodmy,osmy,dziewiaty};
        Animation animationprzyciski= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce);
        pt.startAnimation(animationprzyciski);

        w=90;
        czasomierz.setMax(90);

        czasomierz.setProgress(w);







        RelativeLayout.LayoutParams buttonParam = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        final Button myButton = new Button(this);  // create a new Button
        myButton.setId(1);
        myButton.setText("Kolejne pytanie"); // set Text in the Button
        myButton.setLayoutParams(buttonParam); // set defined layout params to Button


        //simple.addView(myButton);






        Charset ch = Charset.forName("windows-1250");
        String str = "";
        String odp = "";

        StringBuffer buf1 = new StringBuffer();
        StringBuffer buf = new StringBuffer();
        InputStream is = this.getResources().openRawResource(R.raw.polskie);
        InputStream is2 = this.getResources().openRawResource(R.raw.moje);
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(is2,ch));
        BufferedReader reader = new BufferedReader(new InputStreamReader(is,ch));
        if(is!=null)
        {
            while (( str = reader.readLine()) != null)
            {
                buf.append(str + "\n");
            }
        }
        is.close();
        if(is2!=null)
        {
            while (( odp = reader2.readLine()) != null)
            {
                buf1.append(odp + "\n");
            }
        }
        is2.close();

        String odpowiedz = buf1.toString();

        String pytanko = buf.toString();

        String[] odp2;
        String[] str2;

        final String[] odppytanie = odpowiedz.split("#");
        //  Toast.makeText(getApplicationContext(),odppytanie[4],Toast.LENGTH_LONG).show();






        final String[] str1 = pytanko.split("#");
        final Random rand = new Random();

        final int random = rand.nextInt(40);
        //Toast.makeText(getApplicationContext(),Integer.toString(random),Toast.LENGTH_LONG).show();


        str2 = str1[random].split("&");
        str2[0] = str2[0].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
        str2[1] = str2[1].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
        str2[2] = str2[2].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
        pt.setText(str2[0]);
        jedynka.setText(str2[1]);
        dwojka.setText(str2[2]);








        if (str2.length==3)
        {
            trojka.setVisibility(View.INVISIBLE);
            czwarty.setVisibility(View.INVISIBLE);
            piaty.setVisibility(View.INVISIBLE);
            szosty.setVisibility(View.INVISIBLE);
            siodmy.setVisibility(View.INVISIBLE);
            osmy.setVisibility(View.INVISIBLE);
            dziewiaty.setVisibility(View.INVISIBLE);
            buttonParam.addRule(RelativeLayout.BELOW, R.id.drugi);




        }
        if (str2.length==4)
        {
            str2[3] = str2[3].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            trojka.setText(str2[3]);
            trojka.setVisibility(View.VISIBLE);
            czwarty.setVisibility(View.INVISIBLE);
            piaty.setVisibility(View.INVISIBLE);
            szosty.setVisibility(View.INVISIBLE);
            siodmy.setVisibility(View.INVISIBLE);
            osmy.setVisibility(View.INVISIBLE);
            dziewiaty.setVisibility(View.INVISIBLE);
            buttonParam.addRule(RelativeLayout.BELOW, R.id.trzeci);


        }
        if(str2.length==5)
        {
            str2[3] = str2[3].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            str2[4] = str2[4].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            trojka.setText(str2[3]);
            trojka.setVisibility(View.VISIBLE);
            czwarty.setText(str2[4]);
            czwarty.setVisibility(View.VISIBLE);
            piaty.setVisibility(View.INVISIBLE);
            szosty.setVisibility(View.INVISIBLE);
            siodmy.setVisibility(View.INVISIBLE);
            osmy.setVisibility(View.INVISIBLE);
            dziewiaty.setVisibility(View.INVISIBLE);
            buttonParam.addRule(RelativeLayout.BELOW, R.id.czwarty);


        }
        if (str2.length==6)
        {
            str2[3] = str2[3].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            str2[4] = str2[4].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            str2[5] = str2[5].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            trojka.setText(str2[3]);
            trojka.setVisibility(View.VISIBLE);
            piaty.setText(str2[5]);
            czwarty.setText(str2[4]);
            czwarty.setVisibility(View.VISIBLE);
            piaty.setVisibility(View.VISIBLE);
            szosty.setVisibility(View.INVISIBLE);
            siodmy.setVisibility(View.INVISIBLE);
            osmy.setVisibility(View.INVISIBLE);
            dziewiaty.setVisibility(View.INVISIBLE);
            buttonParam.addRule(RelativeLayout.BELOW, R.id.piaty);


        }
        if(str2.length==7)
        {
            str2[3] = str2[3].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            str2[4] = str2[4].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            str2[5] = str2[5].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            str2[6] = str2[6].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            trojka.setText(str2[3]);
            trojka.setVisibility(View.VISIBLE);
            piaty.setVisibility(View.VISIBLE);
            szosty.setVisibility(View.VISIBLE);
            czwarty.setVisibility(View.VISIBLE);
            piaty.setText(str2[5]);
            czwarty.setText(str2[4]);
            szosty.setText(str2[6]);
            siodmy.setVisibility(View.INVISIBLE);
            osmy.setVisibility(View.INVISIBLE);
            dziewiaty.setVisibility(View.INVISIBLE);
            buttonParam.addRule(RelativeLayout.BELOW, R.id.szosty);


        }
        if(str2.length==8)
        {
            str2[3] = str2[3].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            str2[4] = str2[4].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            str2[5] = str2[5].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            str2[6] = str2[6].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            str2[7] = str2[7].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            trojka.setText(str2[3]);
            trojka.setVisibility(View.VISIBLE);
            piaty.setVisibility(View.VISIBLE);
            szosty.setVisibility(View.VISIBLE);
            czwarty.setVisibility(View.VISIBLE);
            piaty.setText(str2[5]);
            czwarty.setText(str2[4]);
            szosty.setText(str2[6]);
            siodmy.setVisibility(View.VISIBLE);
            siodmy.setText(str2[7]);
            osmy.setVisibility(View.INVISIBLE);
            dziewiaty.setVisibility(View.INVISIBLE);
            buttonParam.addRule(RelativeLayout.BELOW, R.id.siodmy);


        }
        if(str2.length==9)
        {
            str2[3] = str2[3].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            str2[4] = str2[4].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            str2[5] = str2[5].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            str2[6] = str2[6].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            str2[7] = str2[7].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            str2[8] = str2[8].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            trojka.setText(str2[3]);
            trojka.setVisibility(View.VISIBLE);
            piaty.setVisibility(View.VISIBLE);
            szosty.setVisibility(View.VISIBLE);
            czwarty.setVisibility(View.VISIBLE);
            piaty.setText(str2[5]);
            czwarty.setText(str2[4]);
            szosty.setText(str2[6]);
            siodmy.setVisibility(View.VISIBLE);
            siodmy.setText(str2[7]);
            osmy.setText(str2[8]);
            osmy.setVisibility(View.VISIBLE);
            dziewiaty.setVisibility(View.INVISIBLE);
            buttonParam.addRule(RelativeLayout.BELOW, R.id.osmy);


        }
        if(str2.length==10)
        {
            str2[3] = str2[3].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            str2[4] = str2[4].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            str2[5] = str2[5].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            str2[6] = str2[6].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            str2[7] = str2[7].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            str2[8] = str2[8].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            str2[9] = str2[9].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
            trojka.setText(str2[3]);
            trojka.setVisibility(View.VISIBLE);
            piaty.setVisibility(View.VISIBLE);
            szosty.setVisibility(View.VISIBLE);
            czwarty.setVisibility(View.VISIBLE);
            piaty.setText(str2[5]);
            czwarty.setText(str2[4]);
            szosty.setText(str2[6]);
            siodmy.setVisibility(View.VISIBLE);
            siodmy.setText(str2[7]);
            osmy.setText(str2[8]);
            osmy.setVisibility(View.VISIBLE);
            dziewiaty.setVisibility(View.VISIBLE);
            dziewiaty.setText(str2[9]);
            buttonParam.addRule(RelativeLayout.BELOW, R.id.dziewiaty);
        }



        buttonParam.setMargins(0,10,0,10);
        myButton.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.bialy));
        myButton.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.rectangle));
        buttonParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        simple.addView(myButton);
        //     myButton.setVisibility(View.VISIBLE);



        RelativeLayout.LayoutParams txt1 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT  );
        txt1.setMargins(0,10,0,10);

        //  final TextView txt = new TextView(this);
        //  txt.setText(Integer.toString(dobre)+"");
        //   txt.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.blekit));
        //    txt.setTextSize(20);
        //    txt.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bottomblue));
        //    txt.setLayoutParams(txt1);
        //     txt1.addRule(RelativeLayout.BELOW,1);
        punktylay.setText(Integer.toString(dobre)+"");
        iloscpytantxt.setText(Integer.toString(15-ilosc)+"");



        //     txt1.addRule(RelativeLayout.CENTER_HORIZONTAL);
        // simple.addView(txt);
        //    txt.setVisibility(View.VISIBLE);








        // wyniczek.setText("Ilosc odpowiedzi dobrych : "+Integer.toString(dobre)+"\n"+"Ilosc udzielonych odpowiedzi : "+Integer.toString(ilosc));

czyzmienilo=true;
        if(ilosc==15)
        {
            final FirebaseUser user = firebaseAuth.getCurrentUser();
            mCountDownTimer.cancel();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    int experience = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getExp();
                    final FirebaseUser user = firebaseAuth.getCurrentUser();
                    int punktypierwszagrat = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPunktypierwszagra();
                    int punktytrzeciagra = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPunktytrzeciagra();
                    int punktyczwartagra = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPunktyczwartagra();
                    int liczbabonusow = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getLiczbabonusów();
                    String data = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getData();
                    int le = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getLiczbaileegzamin();

                    int dt = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getDrugitest();
                    int pt = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPierwszytest();
                    //exp = 10*pkt+experience;

                    if(czyzmienilo)
                    {
                    UserName userName = new UserName(dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getName(),punktypierwszagrat,experience,dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPunktydrugagra(),punktytrzeciagra,punktyczwartagra,liczbabonusow,data,2,pt,dobre);
                    databaseReference.child(user.getUid()).setValue(userName);
                    czyzmienilo=false;
                    }



                    new AlertDialog.Builder(Test2.this)
                            .setTitle("Informacja")
                            .setMessage("Koniec testu zdobyłeś "+dobre+" punktów\nDziękuje za skorzystanie.\nZachęcam do użytkowania aplikacji.")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                    mCountDownTimer.cancel();
                                    startActivity(new Intent(getApplicationContext(),Games.class));
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.

                            .setIcon(R.drawable.ikonagry)
                            .show();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        mCountDownTimer = new CountDownTimer(90000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // Log.v("Log_tag", "Tick of Progress"+ i+ millisUntilFinished);
                if(play) {
                    w--;
                    //  czasomierz.setProgress((int) w * 50 / (5000 / 1000));
                    czasomierz.setProgress(w);
                }

            }

            @Override
            public void onFinish() {
                //Do what you want
                w--;
                czasomierz.setProgress(100);
                Toast.makeText(getApplicationContext(), "koniec czasu", Toast.LENGTH_LONG).show();
                ilosc++;
                czas1 = false;

                sprawdzanie(checkBoxes, myButton,odppytanie, random,czas1);
                zmien(checkBoxes);


            }
        }.start();

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myButton.setVisibility(View.INVISIBLE);


                mCountDownTimer.cancel();
                w=90;
                ilosc++;               /* if(jedynka.isChecked())
                {
                    tmp2=tmp2+"a";
                    j++;
                }
                if(dwojka.isChecked())
                {
                    tmp2=tmp2+"b";
                    j++;
                }

                if(trojka.isChecked())
                {
                    tmp2=tmp2+"c";
                    j++;
                }

                if(czwarty.isChecked())
                {
                    tmp2=tmp2+"d";
                    j++;
                }

                if(piaty.isChecked()){
                    tmp2=tmp2+"e";
                    j++;
                }
                if(szosty.isChecked()){
                    tmp2=tmp2+"f";
                    j++;
                }
                if(siodmy.isChecked()){
                    tmp2=tmp2+"g";
                    j++;
                }
                if(osmy.isChecked())
                {
                    tmp2=tmp2+"h";
                    j++;
                }
                if(dziewiaty.isChecked())
                {
                    tmp2=tmp2+"i";
                    j++;
                }

                txt.setVisibility(View.INVISIBLE);
            for(int i=0;i<odppytanie.length;i++) {
                if (random == i) {
                    if (tmp2.contains(odppytanie[i]) && j==odppytanie[i].length()) {
                       Toast.makeText(getApplicationContext(), "Zdobywasz punkt", Toast.LENGTH_LONG).show();
                       dobre++;
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Zla odpowiedz", Toast.LENGTH_LONG).show();
                    }
                }
            }
            tmp2=" ";
            j=0;*/

                sprawdzanie(checkBoxes,myButton,odppytanie,random,czas1);

                if(sprawdzanie(checkBoxes,myButton,odppytanie,random,czas1))
                {

                    zmienkolor(checkBoxes);
                    zmien(checkBoxes);
                    dobre++;



                    try {
                        Play();
                    }catch (IOException e)
                    {
                        Toast.makeText(getApplicationContext(),"Były problemy z plikiem",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    mCountDownTimer.cancel();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            zmienkolor(checkBoxes);
                            zmien(checkBoxes);

                            try {
                                Play();
                            }catch (IOException e)
                            {
                                Toast.makeText(getApplicationContext(),"Były problemy z plikiem",Toast.LENGTH_LONG).show();
                            }

                        }
                    },3000);
                }




            }
        });

    }

    public void zmien(CheckBox[] checkBoxes)
    {
        for (int i = 0 ;i<checkBoxes.length;i++)
        {
            checkBoxes[i].setChecked(false);
        }
    }

    public boolean sprawdzanie(final CheckBox[] checkBoxes, Button button, final String[] str, int liczba, boolean czas)
    {

        boolean dobra = false;
        if(checkBoxes[0].isChecked())
        {
            tmp2=tmp2+"a";
            j++;
        }
        if(checkBoxes[1].isChecked())
        {
            tmp2=tmp2+"b";
            j++;
        }

        if(checkBoxes[2].isChecked())
        {
            tmp2=tmp2+"c";
            j++;
        }

        if(checkBoxes[3].isChecked())
        {
            tmp2=tmp2+"d";
            j++;
        }

        if(checkBoxes[4].isChecked()){
            tmp2=tmp2+"e";
            j++;
        }
        if(checkBoxes[5].isChecked()){
            tmp2=tmp2+"f";
            j++;
        }
        if(checkBoxes[6].isChecked()){
            tmp2=tmp2+"g";
            j++;
        }
        if(checkBoxes[7].isChecked())
        {
            tmp2=tmp2+"h";
            j++;
        }
        if(checkBoxes[8].isChecked())
        {
            tmp2=tmp2+"i";
            j++;
        }
        button.setVisibility(View.INVISIBLE);

        boolean znak;
        znak = true;
        for( int i=0;i<str.length;i++) {
            if (liczba == i) {
                if (tmp2.contains(str[i]) && j==str[i].length() && znak) {
                    Toast.makeText(getApplicationContext(), "Zdobywasz punkt", Toast.LENGTH_LONG).show();
                    czyzmienilo=true;
                    dobra=true;
                    znak = false;

                }
                else if(!czas)
                {

                    Toast.makeText(getApplicationContext(), "Skonczyl sie czas", Toast.LENGTH_LONG).show();
                    dobra=false;
                }
                else
                {
                    dobra=false;

                    char[] odpowiedz;
                    String que="";
                    for (int j =0;j<str[i].length();j++)
                    {
                        odpowiedz=str[i].toCharArray();
                        que=que+odpowiedz[j]+ " ";
                    }
                    final String que1 = que;
                    Toast.makeText(getApplicationContext(), "Zla odpowiedz", Toast.LENGTH_LONG).show();


                    if(que1.contains("a"))
                    {
                        checkBoxes[0].setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                    }
                    if(que1.contains("b"))
                    {
                        checkBoxes[1].setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                    }
                    if(que1.contains("c"))
                    {
                        checkBoxes[2].setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                    }
                    if(que1.contains("d"))
                    {
                        checkBoxes[3].setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                    }
                    if(que1.contains("e"))
                    {
                        checkBoxes[4].setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                    }
                    if(que1.contains("f"))
                    {
                        checkBoxes[5].setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                    }
                    if(que1.contains("g"))
                    {
                        checkBoxes[6].setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                    }
                    if(que1.contains("h"))
                    {
                        checkBoxes[7].setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                    }
                    if(que1.contains("i"))
                    {
                        checkBoxes[8].setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                    }


                }
            }
        }
        tmp2=" ";
        j=0;

        return dobra;
    }


    @Override
    protected void onResume() {
        //mCountDownTimer.start();
        super.onResume();

    }




    @Override
    protected void onPause() {
        mCountDownTimer.cancel();

        super.onPause();
    }

    @Override
    protected void onStop() {
        mCountDownTimer.cancel();

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mCountDownTimer.cancel();

        super.onDestroy();
    }

    public void zmienkolor(CheckBox[] checkBoxes)
    {
        for(int i=0;i<checkBoxes.length;i++)
        {
            checkBoxes[i].setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.blekit));
        }
    }

}


