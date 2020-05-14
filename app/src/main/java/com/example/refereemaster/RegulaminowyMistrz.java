package com.example.refereemaster;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RegulaminowyMistrz extends AppCompatActivity {
    RelativeLayout simple, calagra;
    TextView pytanie;
    String zaznaczona;
    TextView punkty;
    int exp;
    int pkt;
    int p;
    int w;
    Button odp1,odp2,odp3,odp4;

    ProgressBar czasomierz;
    LinearLayout l1,l2;
    TextView czas,odliczanie;
    boolean czyzmienilo,czyzmienilo2,czyzmienilo3;
    String[] odpowiedzV;
    boolean play;
    CountDownTimer mCountDownTimer, countDownTimer2;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;


    int k,odliczanie2;

    @Override
    protected void onResume() {
        //mCountDownTimer.start();

        super.onResume();
        play=true;
    }




    @Override
    protected void onPause() {
        //mCountDownTimer.cancel();

        super.onPause();
        play=false;
    }

    @Override
    protected void onStop() {
       // mCountDownTimer.cancel();

        super.onStop();
        play=false;
    }

    @Override
    protected void onDestroy() {
      //  mCountDownTimer.cancel();

        super.onDestroy();
        play=false;
    }
    public void onStart()
    {
        super.onStart();
        play=true;
    }
    public void onRestart()
    {
        super.onRestart();
        play=true;
    }
    @Override
    public void onBackPressed() {
        mCountDownTimer.cancel();
        startActivity(new Intent(getApplicationContext(),Games.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_regulaminowy_mistrz);
        if(internetIsConnected()==false)
        {
            new AlertDialog.Builder(RegulaminowyMistrz.this)
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
        calagra=(RelativeLayout)findViewById(R.id.calagra);
        odliczanie = (TextView)findViewById(R.id.odliczanie) ;
        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        pkt=0;
        p=1;
        play=false;
        getSupportActionBar().hide();

        mCountDownTimer = new CountDownTimer(90000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Log.v("Log_tag", "Tick of Progress"+ i+ millisUntilFinished);
                if (play) {





                    czasomierz.setProgress((int)millisUntilFinished/1000);

                      czas.setText((int)millisUntilFinished/1000 + " s");

                }

            }
            @Override
            public void onFinish() {
                //Do what you want
                w--;
                czasomierz.setProgress(100);
                Toast.makeText(getApplicationContext(), "koniec czasu", Toast.LENGTH_LONG).show();
             //   try {
               //     Play();
             //   } catch (IOException e) {
                //    Toast.makeText(getApplicationContext(), "Były problemy z plikiem", Toast.LENGTH_LONG).show();
             //   }

                czyzmienilo2=true;
                new AlertDialog.Builder(RegulaminowyMistrz.this)
                        .setTitle("Informacja")
                        .setMessage("Przegrałeś zdobywając "+pkt+" punktów")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                mCountDownTimer.cancel();
                                startActivity(new Intent(getApplicationContext(),Games.class));
                            }
                        }).setNegativeButton("Graj za 10 złotych gwizdków", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                final FirebaseUser user = firebaseAuth.getCurrentUser();
                                int experience = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getExp();
                                int punktydrugragra = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPunktydrugagra();
                                int punktypierwszagrat = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPunktypierwszagra();
                                int punktytrzeciagra = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPunktytrzeciagra();
                                int liczbabonusow = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getLiczbabonusów();
                                String data = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getData();
                                //exp = 10*pkt+experience;
                                int le = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getLiczbaileegzamin();
                                int punktyczwartagra = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPunktyczwartagra();

                                int pt = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPierwszytest();

                                int dt = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getDrugitest();


                                if(liczbabonusow>=10&&czyzmienilo2)
                                {
                                    UserName userName = new UserName(dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getName(),punktypierwszagrat,experience,punktydrugragra,punktytrzeciagra,punktyczwartagra,liczbabonusow-10,data,le,pt,dt);
                                    databaseReference.child(user.getUid()).setValue(userName);
                                    czyzmienilo2=false;



                                    play=true;
                                    try {
                                        Play();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }


                                }
                                else if(liczbabonusow<10&&czyzmienilo2) {

                                    Toast.makeText(getApplicationContext(), "Nie masz wystarczająco dużo złotych gwizdków", Toast.LENGTH_LONG).show();

                                    mCountDownTimer.cancel();
                                    startActivity(new Intent(getApplicationContext(), Games.class));
                                    czyzmienilo2 = false;

                                }




                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }

                })

                        // A null listener allows the button to dismiss the dialog and take no further action.

                        .setIcon(R.drawable.ikonagry)
                        .show();
            }
        };



        odliczanie2=4;
        new AlertDialog.Builder(RegulaminowyMistrz.this)
                .setTitle("Informacja")
                .setMessage("Gra polega na uzupełnieniu w zaznaczonym miejscu podanego punktu regulaminu  słowami bądź słowem z przycisków.\nNa każdy punkt masz 90 sekund.\nPowodzenia!")
                .setCancelable(false).setIcon(R.drawable.ikonagrydobra)
                .setPositiveButton("Rozumiem", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Whatever...
                        countDownTimer2 = new CountDownTimer(3000, 1000) {
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
                                countDownTimer2.cancel();
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

    public void Play() throws IOException {
        l1 = (LinearLayout)findViewById(R.id.pierwszedwapytania);
        l2 = (LinearLayout)findViewById(R.id.drugiedwapytania);
        play=true;
        simple = (RelativeLayout)findViewById(R.id.cala_gra);
        pytanie = (TextView)findViewById(R.id.pytanie);
        odp1 = (Button)findViewById(R.id.odp1);
        odp2 = (Button)findViewById(R.id.odp2);
        odp3 = (Button)findViewById(R.id.odp3);
        odp4 = (Button)findViewById(R.id.odp4);
        czas = (TextView)findViewById(R.id.czas);
        punkty = (TextView)findViewById(R.id.punkty);
        czasomierz  = (ProgressBar)findViewById(R.id.progressBar);
        //    czasomierz.setMax(90);
        punkty.setText(pkt+" pkt");
        //   w=90;
        czasomierz.setMax(90);


        //   czasomierz.setProgress(w);
        //   mCountDownTimer.cancel();
        StringBuffer buf1 = new StringBuffer();
        StringBuffer buf2 = new StringBuffer();
        String str = "";
        String odpowiedz = "";
        InputStream is = this.getResources().openRawResource(R.raw.pytania);
        InputStream is2 = this.getResources().openRawResource(R.raw.odpowiedzi2);
        Charset ch = Charset.forName("windows-1250");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is,ch));
        if(is!=null)
        {
            while (( str = reader.readLine()) != null)
            {
                buf1.append(str + "\n");
            }
        }
        final String[] str2;
        is.close();
        String pytanko = buf1.toString();


        BufferedReader reader2 = new BufferedReader(new InputStreamReader(is2,ch));
        if(is2!=null)
        {
            while (( odpowiedz = reader2.readLine()) != null)
            {
                buf2.append(odpowiedz + "\n");
            }
        }
        final String[] str3;
        is2.close();
        String odp = buf2.toString();




        odpowiedzV = odp.split("&");
        final String[] pytanieV = pytanko.split("#");
        final Random rand = new Random();

        final int random = rand.nextInt(pytanieV.length-1);
        //  Toast.makeText(getApplicationContext(),odpowiedzV[0],Toast.LENGTH_LONG).show();

        str2 = pytanieV[random].split("&");
        str2[0] = str2[0].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
        str2[1] = str2[1].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
        str2[2] = str2[2].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
        str2[3] = str2[3].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
        str2[4] = str2[4].replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
        String textToHighlight = "___";
        String replacedWith = "<font color='red'>" + textToHighlight + "</font>";
        String originalString = str2[0];
        String modifiedString = originalString.replaceAll(textToHighlight,replacedWith);
        pytanie.setText(Html.fromHtml(modifiedString));
      Animation animationpytanie= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce);



        int[] liczby = {1,2,3,4};
        ArrayList<Integer> lista = new ArrayList<Integer>();
        Random rand2= new Random();
        while (lista.size()<4)
        {
            int random2 = rand2.nextInt(liczby.length);
            if(!lista.contains(liczby[random2]))
            {
                lista.add(liczby[random2]);
            }
        }

        odp1.setText(str2[lista.get(0)]);
        odp2.setText(str2[lista.get(1)]);
        odp3.setText(str2[lista.get(2)]);
        odp4.setText(str2[lista.get(3)]);
        odblokuj();
     //   Toast.makeText(getApplicationContext(),str2.length,Toast.LENGTH_LONG).show();
    //   Toast.makeText(getApplicationContext(),pytanieV.length+"",Toast.LENGTH_LONG).show();


        odp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCountDownTimer.cancel();
                //   czasomierz.setMax(90);
                // w=90;
                zaznaczona = str2[0].replace("___",odp1.getText());
                pytanie.setText(zaznaczona);
                //  kolruj(odp1);
                //   zablokuj();

                sprawdzenie(odp1,random);
                //  mCountDownTimer.cancel();
                //  w=90;

            }
        });
        odp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountDownTimer.cancel();
                //     czasomierz.setMax(90);
                //  w=90;
                zaznaczona = str2[0].replace("___",odp2.getText());
                pytanie.setText(zaznaczona);
                //  kolruj(odp2);
                //  zablokuj();
                sprawdzenie(odp2,random);
                //  mCountDownTimer.cancel();
                // w=90;
            }
        });
        odp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountDownTimer.cancel();
                //   czasomierz.setMax(90);
                //  w=90;
                zaznaczona = str2[0].replace("___",odp3.getText());
                pytanie.setText(zaznaczona);
                //  kolruj(odp3);
                //   zablokuj();
                sprawdzenie(odp3,random);
                // mCountDownTimer.cancel();
                //   w=90;
            }
        });
        odp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountDownTimer.cancel();

                zaznaczona = str2[0].replace("___",odp4.getText());
                pytanie.setText(zaznaczona);

                sprawdzenie(odp4,random);

            }
        });

        mCountDownTimer.start();
        pytanie.startAnimation(animationpytanie);
        Animation animationprzyciski= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce);
        odp1.startAnimation(animationprzyciski);
        odp2.startAnimation(animationprzyciski);
        odp3.startAnimation(animationprzyciski);
        odp4.startAnimation(animationprzyciski);

    }

    public void zablokuj(Button button)
    {
        odp1.setEnabled(false);
        odp2.setEnabled(false);
        odp3.setEnabled(false);
        odp4.setEnabled(false);
        button.setEnabled(true);
    }
    public void kolruj(Button button)
    {
        Drawable image=(Drawable)getResources().getDrawable(R.drawable.rectangle2);
        button.setBackground(image);
    }
    public void kolrujzle(Button button)
    {
        Drawable image=(Drawable)getResources().getDrawable(R.drawable.rectanglezle);
        button.setBackground(image);
    }
    public void kolruj_na_nowo(Button button)
    {
        Drawable image=(Drawable)getResources().getDrawable(R.drawable.rectangle);
        button.setBackground(image);
    }
    public void kolruj_dobre(String string)
    {

        final Drawable image=(Drawable)getResources().getDrawable(R.drawable.rectangle2);
        final Drawable image2=(Drawable)getResources().getDrawable(R.drawable.rectangle);
        if(odp1.getText().equals(string))
        {
            odp1.setBackground(image);
            k=1;
        }
        if(odp2.getText().equals(string))
        {
            odp2.setBackground(image);
            k=2;
        }
        if(odp3.getText().equals(string))
        {
            odp3.setBackground(image);
            k=3;
        }
        if(odp4.getText().equals(string))
        {
            odp4.setBackground(image);
            k=4;
        }
        if(k==1)
        {
            zablokuj(odp1);
        }
        if(k==2)
        {
            zablokuj(odp2);
        }
        if(k==3)
        {
            zablokuj(odp3);
        }
        if(k==4)
        {
            zablokuj(odp4);
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                odp1.setBackground(image2);
                odp2.setBackground(image2);
                odp3.setBackground(image2);
                odp4.setBackground(image2);


            }
        },3000);



    }
    public void odblokuj()
    {
        odp1.setEnabled(true);
        odp2.setEnabled(true);
        odp3.setEnabled(true);
        odp4.setEnabled(true);
    }
    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }
    public void sprawdzenie(final Button button, int random)
    {
        final FirebaseUser user = firebaseAuth.getCurrentUser();



        Animation animationprzyciski= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein);
        button.startAnimation(animationprzyciski);



        w=90;
        mCountDownTimer.cancel();
        play=false;
        for(int i =0;i<odpowiedzV.length-1;i++)
        {


            if(random == i)
            {
                if(button.getText().equals(odpowiedzV[i]))
                {
                    zablokuj(button);
                    kolruj(button);
                    //  Toast.makeText(getApplicationContext(),"zdobywasz punkt",Toast.LENGTH_LONG).show();
                    pkt++;
                    punkty.setText(pkt+" pkt");
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            kolruj_na_nowo(button);
                            play=true;
                            try {
                                Play();
                            }catch (IOException e)
                            {
                                Toast.makeText(getApplicationContext(),"Były problemy z plikiem",Toast.LENGTH_LONG).show();
                            }

                        }
                    },3000);

                    //  play=false;
                    // kolruj_na_nowo(button);

                }
                else
                {
                    kolruj_dobre(odpowiedzV[i]);
                    // zablokuj(button);
                    kolrujzle(button);

                    //        Toast.makeText(getApplicationContext(),"Nie zdobywasz punktu",Toast.LENGTH_LONG).show();
                    czyzmienilo=true;
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            kolruj_na_nowo(button);
                            w=90;

                            czyzmienilo3=true;
                            czyzmienilo=true;
                            czyzmienilo2=true;
                            new AlertDialog.Builder(RegulaminowyMistrz.this)
                                    .setTitle("Informacja")
                                    .setMessage("Przegrałeś zdobywając "+pkt+" punktów")

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
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

                                                    int dt = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getDrugitest();

                                                    int pt = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPierwszytest();
                                                    exp = 10*pkt+experience;
                                                    int le = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getLiczbaileegzamin();

                                                    if(punktypierwszagrat<pkt&&czyzmienilo3)
                                                    {
                                                        UserName userName = new UserName(dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getName(),pkt,experience+(10*pkt),dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPunktydrugagra(),punktytrzeciagra,punktyczwartagra,liczbabonusow+pkt,data,le,pt,dt);
                                                        databaseReference.child(user.getUid()).setValue(userName);
                                                        czyzmienilo=false;
                                                        czyzmienilo3=false;
                                                    }
                                                    else if(punktypierwszagrat>=pkt&&czyzmienilo&&czyzmienilo3)
                                                    {
                                                        UserName userName = new UserName(dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getName(),punktypierwszagrat,experience+(10*pkt),dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPunktydrugagra(),punktytrzeciagra,punktyczwartagra,liczbabonusow+pkt,data,le,pt,dt);
                                                        databaseReference.child(user.getUid()).setValue(userName);
                                                        czyzmienilo=false;
                                                        czyzmienilo3=false;

                                                    }


                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });


                                            mCountDownTimer.cancel();
                                            startActivity(new Intent(getApplicationContext(),Games.class));
                                        }
                                    }).setNegativeButton("Graj za 10 złotych gwizdków", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            int experience = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getExp();
                                            final FirebaseUser user = firebaseAuth.getCurrentUser();
                                            int punktydrugragra = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPunktydrugagra();
                                            int punktypierwszagrat = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPunktypierwszagra();
                                            int punktytrzeciagra = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPunktytrzeciagra();
                                            int liczbabonusow = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getLiczbabonusów();
                                            String data = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getData();
                                            int le = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getLiczbaileegzamin();
                                            int pt = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPierwszytest();

                                            int dt = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getDrugitest();
                                            //exp = 10*pkt+experience;
                                            int punktyczwartagra = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPunktyczwartagra();


                                            if(liczbabonusow>=10&&czyzmienilo2&&czyzmienilo)
                                            {
                                                UserName userName = new UserName(dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getName(),punktypierwszagrat,experience,punktydrugragra,punktytrzeciagra,punktyczwartagra,liczbabonusow-10,data,le,pt,dt);
                                                databaseReference.child(user.getUid()).setValue(userName);
                                                czyzmienilo2=false;



                                                play=true;
                                                try {
                                                    Play();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }


                                            }
                                            else if(liczbabonusow<10&&czyzmienilo2&&punktypierwszagrat<pkt) {

                                                Toast.makeText(getApplicationContext(), "Nie masz wystarczająco dużo złotych gwizdków", Toast.LENGTH_LONG).show();
                                                UserName userName = new UserName(dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getName(),pkt,experience+(10*pkt),dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPunktydrugagra(),punktytrzeciagra,punktyczwartagra,liczbabonusow+pkt,data,le,pt,dt);
                                                databaseReference.child(user.getUid()).setValue(userName);
                                                czyzmienilo=false;
                                                mCountDownTimer.cancel();
                                                startActivity(new Intent(getApplicationContext(), Games.class));
                                                czyzmienilo2 = false;

                                            }

                                            else if(liczbabonusow<10&&czyzmienilo2&&punktypierwszagrat>=pkt) {

                                                Toast.makeText(getApplicationContext(), "Nie masz wystarczająco dużo złotych gwizdków", Toast.LENGTH_LONG).show();
                                                UserName userName = new UserName(dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getName(),punktypierwszagrat,experience+(10*pkt),dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPunktydrugagra(),punktytrzeciagra,punktyczwartagra,liczbabonusow+pkt,data,le,pt,dt);
                                                databaseReference.child(user.getUid()).setValue(userName);
                                                czyzmienilo=false;
                                                mCountDownTimer.cancel();
                                                startActivity(new Intent(getApplicationContext(), Games.class));
                                                czyzmienilo2 = false;

                                            }




                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                }

                            })

                                    // A null listener allows the button to dismiss the dialog and take no further action.

                                    .setIcon(R.drawable.ikonagry)
                                    .show();


                        }
                    },3000);


                }
            }
        }
    }

}
