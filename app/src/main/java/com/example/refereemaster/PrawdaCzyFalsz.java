package com.example.refereemaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import java.util.Random;

public class PrawdaCzyFalsz extends AppCompatActivity {
    ProgressBar czasomierz;
    CountDownTimer mCountDownTimer,mCountDownTimer2;
    Button tak,nie;
    TextView czas,pytanie;
    TextView punkty;
    LinearLayout gra;
    TextView odliczanie;
    int cono;
    int w,odliczanie2,k,pkt=0;
    String[] odpowiedzV;
    int poprzednio;
    private DatabaseReference databaseReference,databaseReference2;
    boolean czyzmienilo, czyzmienilo2;
    int exp;
    private FirebaseAuth firebaseAuth;
    boolean play;

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
        mCountDownTimer.cancel();
    }

    @Override
    protected void onStop() {
        // mCountDownTimer.cancel();

        super.onStop();
        mCountDownTimer.cancel();
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

    }
    @Override
    public void onBackPressed() {
        mCountDownTimer.cancel();
        startActivity(new Intent(getApplicationContext(),Games.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prawda_czy_falsz);
        getSupportActionBar().hide();
        gra = (LinearLayout)findViewById(R.id.graprawda);
        tak=findViewById(R.id.przycisktak);
        nie=findViewById(R.id.przycisknie);
        odliczanie=(TextView)findViewById(R.id.odliczanie2);

        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();

        mCountDownTimer = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Log.v("Log_tag", "Tick of Progress"+ i+ millisUntilFinished);
                if (play) {
                    czasomierz.setProgress((int)millisUntilFinished/1000);
                    czas.setText((int)millisUntilFinished/1000+"");
                  //  Toast.makeText(getApplicationContext(), "leci", Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onFinish() {
             
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        // w=90;

                        new AlertDialog.Builder(PrawdaCzyFalsz.this)
                                .setTitle("Informacja")
                                .setMessage("Skończył sie czas.\nZdobywasz "+pkt+" punktów")

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {


                                        mCountDownTimer.cancel();
                                        startActivity(new Intent(getApplicationContext(),Games.class));
                                    }
                                })/*.setNegativeButton("Graj za 10 złotych gwizdków", new DialogInterface.OnClickListener() {
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
                                        //exp = 10*pkt+experience;
                                        int le = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getLiczbaileegzamin();
                                        String data = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getData();
                                        int punktyczwartagra = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPunktyczwartagra();

                                        int pt = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPierwszytest();

                                        int dt = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getDrugitest();


                                        if(liczbabonusow>=10&&czyzmienilo2)
                                        {
                                            UserName userName = new UserName(dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getName(),punktypierwszagrat,experience,punktydrugragra,punktytrzeciagra,punktyczwartagra,liczbabonusow-10,data,le,pt,dt);
                                            databaseReference.child(user.getUid()).setValue(userName);
                                            czyzmienilo2=false;

                                            cono=1;
                                            koloruj_nowo(tak);
                                            koloruj_nowo(nie);
                                            play=true;
                                            try {
                                                play();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }


                                        }
                                        else if(liczbabonusow<10&&czyzmienilo2) {

                                            Toast.makeText(getApplicationContext(), "Nie masz wystarczająco dużo złotych gwizdków", Toast.LENGTH_LONG).show();
                                            cono = 2;
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

                        })*/

                                // A null listener allows the button to dismiss the dialog and take no further action.

                                .setIcon(R.drawable.ikonagry)
                                .show();


                    }
                },1000);
            }
        };
        odliczanie2=4;
        new AlertDialog.Builder(PrawdaCzyFalsz.this)
                .setTitle("Informacja")
                .setMessage("Gra polega odpowiadaniu 'TAK' lub 'NIE' na krótkie pytania.\nNa każde pytanie masz 20 sekund.\nPowodzenia!")
                .setCancelable(false).setIcon(R.drawable.ikonagrydobra)
                .setPositiveButton("Rozumiem", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCountDownTimer2= new CountDownTimer(3000, 1000) {
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
                                gra.setVisibility(View.VISIBLE);
                                mCountDownTimer2.cancel();
                                //    Toast.makeText(getApplicationContext(),"odlicza sie",Toast.LENGTH_LONG).show();
                                try {
                                    play();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();






                    }
                }).show();


    }
    public  void play() throws IOException {
        czasomierz=findViewById(R.id.czas);


        czas=findViewById(R.id.czastekst);
        punkty=findViewById(R.id.punkty);
        play=true;
        pytanie=(TextView)findViewById(R.id.pytanie);
        StringBuffer buf1 = new StringBuffer();
        StringBuffer buf2 = new StringBuffer();
        String str = "";
        String odpowiedz = "";
        InputStream is = this.getResources().openRawResource(R.raw.pytaniaprawdafalsz);
        InputStream is2 = this.getResources().openRawResource(R.raw.odpowiedziprawdafalsz);
        Charset ch = Charset.forName("windows-1250");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is,ch));
        if(is!=null)
        {
            while (( str = reader.readLine()) != null)
            {
                buf1.append(str + "\n");
            }
        }
        final String str2;
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
        odpowiedzV = odp.split("#");


        final String[] pytanieV = pytanko.split("#");
        final Random rand = new Random();
      //  Toast.makeText(getApplicationContext(), odpowiedzV.length+" - " +pytanieV.length+"", Toast.LENGTH_LONG).show();

        final int random = rand.nextInt(pytanieV.length-1);
        str2 = pytanieV[random];
        pytanie.setText(str2);
        tak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sprawdzenie(tak,random);
            }
        });

        nie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sprawdzenie(nie,random);
            }
        });






        mCountDownTimer.start();
    }

    public void sprawdzenie(final Button button, int random)
    {
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        mCountDownTimer.cancel();
        Animation animationprzyciski= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein);
        button.startAnimation(animationprzyciski);
       // Toast.makeText(getApplicationContext(), button.getText(), Toast.LENGTH_LONG).show();

        for(int i =0;i<odpowiedzV.length-1;i++)
        {


            if(random == i) {
                if (button.getText().equals(odpowiedzV[i])) {
                        Toast.makeText(getApplicationContext(),"zdobywasz punkt",Toast.LENGTH_LONG).show();

                    //  Toast.makeText(getApplicationContext(),"zdobywasz punkt",Toast.LENGTH_LONG).show();
                   pkt++;
                   koloruj_dobre(button);
                    punkty.setText(pkt+"");
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            koloruj_nowo(button);
                            play=true;
                            try {
                                play();
                            }catch (IOException e)
                            {
                                Toast.makeText(getApplicationContext(),"Były problemy z plikiem",Toast.LENGTH_LONG).show();
                            }

                        }
                    },3000);

                }
                else {


                    //        Toast.makeText(getApplicationContext(),"Nie zdobywasz punktu",Toast.LENGTH_LONG).show();
                    koloruj_zle(button);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {




                            czyzmienilo=true;
                           // w=90;
                            czyzmienilo2=true;
                            new AlertDialog.Builder(PrawdaCzyFalsz.this)
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

                                                    if(punktydrugragra<pkt&&czyzmienilo)
                                                    {
                                                        UserName userName = new UserName(dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getName(),punktypierwszagrat,experience+(10*pkt),pkt,punktytrzeciagra,punktyczwartagra,liczbabonusow+pkt,data,le,pt,dt);
                                                        databaseReference.child(user.getUid()).setValue(userName);
                                                        czyzmienilo=false;
                                                    }
                                                    else if(punktydrugragra>=pkt&&czyzmienilo)
                                                    {
                                                        UserName userName = new UserName(dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getName(),punktypierwszagrat,experience+(10*pkt),punktydrugragra,punktytrzeciagra,punktyczwartagra,liczbabonusow+pkt,data,le,pt,dt);
                                                        databaseReference.child(user.getUid()).setValue(userName);
                                                        czyzmienilo=false;
                                                    }
                                                    poprzednio=1;
                                                    czyzmienilo=false;
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
                                    databaseReference2.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            int experience = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getExp();
                                            final FirebaseUser user = firebaseAuth.getCurrentUser();
                                            int punktydrugragra = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPunktydrugagra();
                                            int punktypierwszagrat = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPunktypierwszagra();
                                            int punktytrzeciagra = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPunktytrzeciagra();
                                            int liczbabonusow = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getLiczbabonusów();
                                            String data = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getData();
                                            //exp = 10*pkt+experience;
                                            int punktyczwartagra = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPunktyczwartagra();
                                            int doliczenia = liczbabonusow-10;
                                            int le = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getLiczbaileegzamin();

                                            int dt = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getDrugitest();

                                            int pt = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPierwszytest();

                                         if(liczbabonusow>=10&&czyzmienilo2&&czyzmienilo)
                                            {
                                                UserName userName = new UserName(dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getName(),punktypierwszagrat,experience,punktydrugragra,punktytrzeciagra,punktyczwartagra,doliczenia,data,le,pt,dt);
                                                databaseReference.child(user.getUid()).setValue(userName);
                                                czyzmienilo2=false;

                                                cono=1;
                                                koloruj_nowo(tak);
                                                koloruj_nowo(nie);

                                                play=true;
                                                try {
                                                    play();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }


                                            }
                                            else if(liczbabonusow<10&&czyzmienilo2&&punktydrugragra<pkt) {

                                             Toast.makeText(getApplicationContext(), "Nie masz wystarczająco dużo złotych gwizdków", Toast.LENGTH_LONG).show();

                                             czyzmienilo=false;
                                             cono = 2;
                                             mCountDownTimer.cancel();

                                             czyzmienilo2 = false;
                                             UserName userName = new UserName(dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getName(),punktypierwszagrat,experience+(10*pkt),pkt,punktytrzeciagra,punktyczwartagra,liczbabonusow+pkt,data,le,pt,dt);
                                             databaseReference.child(user.getUid()).setValue(userName);
                                           //  finish();
                                             startActivity(new Intent(getApplicationContext(),Games.class));

                                         }
                                         else if(liczbabonusow<10&&czyzmienilo2&&punktydrugragra>=pkt)
                                         {


                                             Toast.makeText(getApplicationContext(), "Nie masz wystarczająco dużo złotych gwizdków", Toast.LENGTH_LONG).show();

                                             czyzmienilo=false;
                                             cono = 2;
                                             mCountDownTimer.cancel();

                                             czyzmienilo2 = false;
                                             UserName userName = new UserName(dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getName(),punktypierwszagrat,experience+(10*pkt),punktydrugragra,punktytrzeciagra,punktyczwartagra,liczbabonusow+pkt,data,le,pt,dt);
                                             databaseReference.child(user.getUid()).setValue(userName);
                                          //   finish();
                                             startActivity(new Intent(getApplicationContext(),Games.class));


                                         }


                                         czyzmienilo2 = false;
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
                    },1500);
                }


            }




        }
    }
    public void koloruj_zle(Button button)
    {
        Drawable image=(Drawable)getResources().getDrawable(R.drawable.kolka_zle);
        button.setBackground(image);
    }
    public void koloruj_nowo(Button button)
    {
        Drawable image=(Drawable)getResources().getDrawable(R.drawable.kolka);
        button.setBackground(image);
    }
    public void koloruj_dobre(Button button)
    {
        Drawable image=(Drawable)getResources().getDrawable(R.drawable.kolka_dobre);
        button.setBackground(image);
    }
}
