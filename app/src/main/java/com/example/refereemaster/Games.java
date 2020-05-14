package com.example.refereemaster;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Games extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    final Context context = this;
    int k;
    boolean flagacofanie=false;
    private TextView nazwaUzytkownika, punktypierwszagratxt,poziomuz,punktydrugagratxt,punktytrzeciagratxt,liczbaklejnotowtxt;
    private Button logout;
    boolean p = false;
    ArrayList<String> lista1 = new ArrayList<String>();
    ArrayList<String> listapoziom = new ArrayList<String>();
    ArrayList<String> listapierwszagra = new ArrayList<String>();
    ArrayList<String> listaczwartagra = new ArrayList<String>();
    ArrayList<String> listatrzeciagra = new ArrayList<String>();
    ArrayList<String> listadrugagra = new ArrayList<String>();
    ImageView ikonaegz,tutorial;
    ImageButton pokazranking;
    //private ArrayAdapter<String> adapter;
    LinearLayout ranking,grawidok;
    LinearLayout trzeciagra,czwartagra1;
    LayoutInflater layoutInflater;
   // private ArrayList<String> arrayList;
    String name2;
    private ProgressDialog progressDialog;
    int u = 1;
    Boolean flaga;
    private  FirebaseAuth.AuthStateListener mAuthListener;
    private  String userID;
    TextView progressexp,punktyczwartagratxt;
    private DatabaseReference databaseReference;
    EditText nameUser;
    Boolean pokazac;
    FirebaseFirestore city;
    ProgressBar exp;
    String text;


    @Override
    public void onBackPressed() {

        if(flagacofanie==true) {
            finish();
            startActivity(new Intent(getApplicationContext(),Games.class));
            flagacofanie = false;
        }
        else {}
       // super.onBackPressed();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        getSupportActionBar().hide();
        pokazranking = (ImageButton)findViewById(R.id.pokazranking);
        trzeciagra= (LinearLayout)findViewById(R.id.trzeciagra);
        czwartagra1=(LinearLayout)findViewById(R.id.czwarta_gra);
        punktyczwartagratxt=(TextView)findViewById(R.id.punktyczwartagratxt);
        liczbaklejnotowtxt = (TextView)findViewById(R.id.liczbaklejnotow);
        ikonaegz=(ImageView)findViewById(R.id.ikonaegzamin);
        tutorial = (ImageView)findViewById(R.id.pokazintro);
       // String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
       // Toast.makeText(getApplicationContext(),currentDate,Toast.LENGTH_LONG).show();
       exp = (ProgressBar)findViewById(R.id.exp);
        punktypierwszagratxt = (TextView)findViewById(R.id.punktypierwszagra);
        progressexp  = (TextView)findViewById(R.id.progressexp);
        punktydrugagratxt = (TextView)findViewById(R.id.punktydrugagra);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Przeładowywanie gry...");
        progressDialog.show();
        punktytrzeciagratxt=(TextView)findViewById(R.id.punktytrzeciagratxt);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);



 /*       if(!prefs.getBoolean("firstTime", false)  ) {
           pokazIntro();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }*/

        int liczba = getIntent().getIntExtra("tak",0);
        if(liczba==3)
        {
            pokazIntro();
        }
        else
        {

        }




        databaseReference = FirebaseDatabase.getInstance().getReference();
        poziomuz=(TextView)findViewById(R.id.poziomuz);



        exp.setScaleY(6f);
        firebaseAuth = FirebaseAuth.getInstance();
        city  = FirebaseFirestore.getInstance();
        if(firebaseAuth.getCurrentUser()==null)
        {
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        logout=(Button)findViewById(R.id.wylogowywanie);
        nazwaUzytkownika= (TextView)findViewById(R.id.nazwa);
        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pokazIntro();
                
            }
        });



       databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



              //  userInformation.setName(dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getName());
                String dowyswietlenie2 = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getName();
                nazwaUzytkownika.setText(dowyswietlenie2);
                int punktypierwszagrat = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPunktypierwszagra();
                int punktytrzeciagrat = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPunktytrzeciagra();
                int punktydrugragra = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPunktydrugagra();
                int czwartagra = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getPunktyczwartagra();
                int liczbabon = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getLiczbabonusów();
                int liczbaegz = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getLiczbaileegzamin();
                String datarejestracji = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getData();
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                Calendar c = Calendar.getInstance();
                try {
                    Date date = format.parse(datarejestracji);
                   // System.out.println(date);

                    c.setTime(date);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                c.add(Calendar.DATE,5);
                Date resultdate = new Date(c.getTimeInMillis());
                datarejestracji = format.format(resultdate);

//                if(liczbaegz==0){
//                    finish();
//                    startActivity(new Intent(getApplicationContext(),Test1.class));
//                }

                Date datedzis = Calendar.getInstance().getTime();
              //  Toast.makeText(getApplicationContext(),datedzis.toString(),Toast.LENGTH_LONG).show();
               // String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

//                if(datedzis.after(resultdate)&&liczbaegz==1)
//                {
//                    finish();
//                    startActivity(new Intent(getApplicationContext(),Test2.class));
//                }



                liczbaklejnotowtxt.setText(liczbabon+"");

                punktytrzeciagratxt.setText(punktytrzeciagrat+"");
                punktypierwszagratxt.setText(punktypierwszagrat+"");
                punktydrugagratxt.setText(punktydrugragra+"");
                punktyczwartagratxt.setText(czwartagra+"");
                if(punktypierwszagrat+punktydrugragra+punktytrzeciagrat<50)
                {
                    ikonaegz.setImageResource(R.drawable.padlock);
               //     czwartagra1.setEnabled(false);
                    k=1;
                }
                else
                {
                    ikonaegz.setImageResource(R.drawable.awardcup);
               //     czwartagra1.setEnabled(true);
                    k=2;
                }

                int experience = dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getExp();
           /*     for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UserInformation user= postSnapshot.getValue(UserInformation.class);
                   if (user.getName()!="peter"){
                       Toast.makeText(getApplicationContext(),user.getName()+user.getExp(),Toast.LENGTH_LONG).show();}
                }*/
                int poziom = experience/100;




                exp.setMax((poziom+1)*100);
                exp.setProgress(experience);
                poziom=poziom+1;
                poziomuz.setText("Poziom "+poziom);

                progressexp.setText(exp.getProgress()+"/"+exp.getMax());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








       if(!nazwaUzytkownika.getText().equals(user.getEmail())) {



       }
       pokazranking.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Animation animationprzyciski= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein);
               pokazranking.startAnimation(animationprzyciski);


               final Handler handler = new Handler();
               handler.postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       ranking = findViewById(R.id.glowna);
                       layoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                       final View myview = layoutInflater.inflate(R.layout.rankingniewiem,null);

                       final TableLayout lista;
                       //final ListView listapoziom1;
                       //final ListView pierwszagra;
                       //final ListView drugagra;
                       //final ListView trzeciagra;
                       //final ListView czwartagra;

                        //czwartagra = myview.findViewById(R.id.listaczwartaagra);
                       //trzeciagra = myview.findViewById(R.id.listatrzeciagra);
                       //pierwszagra = myview.findViewById(R.id.listapierwszagra);
                       //drugagra = myview.findViewById(R.id.listadrugagra);
                       lista = myview.findViewById(R.id.listarankingowa);
                       //listapoziom1 = myview.findViewById(R.id.listapoziom);
                       lista.setStretchAllColumns(true);
                       lista.bringToFront();
                      // final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.poziom,lista1);
                       //final ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(getApplicationContext(),R.layout.poziom,listatrzeciagra);
                       //final ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getApplicationContext(),R.layout.poziom,listadrugagra);
                       //final ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getApplicationContext(),R.layout.poziom,listapierwszagra);
                       //final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(),R.layout.poziom,listapoziom);
                       //final ArrayAdapter<String> adapter6 = new ArrayAdapter<String>(getApplicationContext(),R.layout.poziom,listaczwartagra);
                       databaseReference.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               String imie=   dataSnapshot.child(user.getUid()).getValue(UserInformation.class).getName();
                               //Toast.makeText(getApplicationContext(),dataSnapshot.getChildrenCount()+"",Toast.LENGTH_LONG).show();
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(60,60);
                               TableRow tableRow = new TableRow(getApplicationContext());
                               TextView nazwanazwa = new TextView(getApplicationContext());
                               nazwanazwa.setText("\n  Nazwa");

                               tableRow.addView(nazwanazwa);

                               TextView nazwapoziom = new TextView(getApplicationContext());
                               nazwapoziom.setText("\n Poziom");

                               tableRow.addView(nazwapoziom);


                               ImageView pierwszagra = new ImageView(getApplicationContext());
                               pierwszagra.setImageResource(R.drawable.pierwszaikonagryy);
                     //          pierwszagra.setLayoutParams(layoutParams);

                               tableRow.addView(pierwszagra);

                               ImageView drugagra = new ImageView(getApplicationContext());
                               drugagra.setImageResource(R.drawable.truefalsee);
                         //      drugagra.setLayoutParams(layoutParams);
                               tableRow.addView(drugagra);

                               ImageView trzeciagra = new ImageView(getApplicationContext());
                               trzeciagra.setImageResource(R.drawable.trzeciagraa);
                       //         trzeciagra.setLayoutParams(layoutParams);
                               tableRow.addView(trzeciagra);


                               ImageView czwartagra = new ImageView(getApplicationContext());
                               czwartagra.setImageResource(R.drawable.awardcupp);
                        //       czwartagra.setLayoutParams(layoutParams);

                               tableRow.addView(czwartagra);

                               lista.addView(tableRow);
                               for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                   UserInformation user = postSnapshot.getValue(UserInformation.class);


                                       TableRow tr = new TableRow(getApplicationContext());
                                       TextView txtname = new TextView(getApplicationContext());

                                       txtname.setPadding(20,20,20,20);
                                        txtname.setBackgroundResource(R.drawable.textview_border);
                                       txtname.setText(user.getName());
                                       tr.addView(txtname);


                                       TextView txtlevel = new TextView(getApplicationContext());
                                       txtlevel.setBackgroundResource(R.drawable.textview_border);
                                       txtlevel.setText(user.getExp()/100+1+"");
                                   txtlevel.setPadding(20,20,20,20);
                                       tr.addView(txtlevel);

                                       TextView txtpierwszagra = new TextView(getApplicationContext());
                                       txtpierwszagra.setBackgroundResource(R.drawable.textview_border);
                                       txtpierwszagra.setText(user.getPunktypierwszagra()+"");
                                   txtpierwszagra.setPadding(20,20,20,20);
                                       tr.addView(txtpierwszagra);


                                       TextView txtdrugagra = new TextView(getApplicationContext());
                                       txtdrugagra.setBackgroundResource(R.drawable.textview_border);
                                       txtdrugagra.setText(user.getPunktydrugagra()+"");
                                   txtdrugagra.setPadding(20,20,20,20);
                                       tr.addView(txtdrugagra);

                                       TextView txttrzeciagra = new TextView(getApplicationContext());
                                       txttrzeciagra.setBackgroundResource(R.drawable.textview_border);
                                       txttrzeciagra.setText(user.getPunktytrzeciagra()+"");
                                   txttrzeciagra.setPadding(20,20,20,20);
                                       tr.addView(txttrzeciagra);

                                       TextView txtczwartagra = new TextView(getApplicationContext());
                                       txtczwartagra.setBackgroundResource(R.drawable.textview_border);
                                       txtczwartagra.setText(user.getPunktyczwartagra()+"");
                                   txtczwartagra.setPadding(20,20,20,20);
                                       tr.addView(txtczwartagra);

                                       lista.addView(tr);


                                   //     Toast.makeText(getApplicationContext(),user.getName() + user.getExp() / 100 + user.getPunktypierwszagra(),Toast.LENGTH_LONG).show();
                               }
                           }
                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {
                           }
                       });
                       //czwartagra.setAdapter(adapter6);
                       //trzeciagra.setAdapter(adapter5);
                       //drugagra.setAdapter(adapter4);
                       //listapoziom1.setAdapter(adapter2);
                       //pierwszagra.setAdapter(adapter3);
                       //lista.setAdapter(adapter);
                       ranking.addView(myview);
                       flagacofanie = true;

                   }
               },1000);



           }
       });

        czwartagra1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(k==2) {
                   Animation animationprzyciski = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                   czwartagra1.startAnimation(animationprzyciski);

                   final Handler handler = new Handler();
                   handler.postDelayed(new Runnable() {
                       @Override
                       public void run() {

                           finish();
                           startActivity(new Intent(getApplicationContext(), Egzamin.class));

                       }
                   }, 1000);
               }
               else if(k==1)
               {
                   new AlertDialog.Builder(Games.this)
                           .setTitle("Informacja")
                           .setMessage("Aby odblokować egzamin musisz mieć co najmniej 50 punktów w sumie ze wszystkich Quizów")

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
           }
       });
       trzeciagra.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Animation animationprzyciski= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein);
               trzeciagra.startAnimation(animationprzyciski);

               final Handler handler = new Handler();
               handler.postDelayed(new Runnable() {
                   @Override
                   public void run() {

                       finish();
                       startActivity(new Intent(getApplicationContext(),Quizowanie.class));

                   }
               },1000);

           }
       });




        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animationprzyciski= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein);
                logout.startAnimation(animationprzyciski);


                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));

                    }
                },1000);



            }
        });


        final LinearLayout pierwszagra = (LinearLayout)findViewById(R.id.pierwszagra);
        final LinearLayout drugagra = (LinearLayout)findViewById(R.id.drugragra);
        pierwszagra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animationprzyciski= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein);
                pierwszagra.startAnimation(animationprzyciski);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(getApplicationContext(),RegulaminowyMistrz.class);
                        startActivity(intent);

                    }
                },1000);


            }
        });
        drugagra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animationprzyciski= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein);
                drugagra.startAnimation(animationprzyciski);


                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(getApplicationContext(),PrawdaCzyFalsz.class);
                        startActivity(intent);

                    }
                },1000);
            }
        });



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                progressDialog.cancel();



            }
        },1000);

    }
    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }
    public void pokazIntro()
    {

        final Dialog factory = new Dialog(context);
        factory.setContentView(R.layout.tutorial);
        TextView glowny = factory.findViewById(R.id.glowny);
        final TextView tip =factory.findViewById(R.id.numertipa);
        factory.setTitle("Title...");
        final TextView tresctipa = factory.findViewById(R.id.tresc);
        final ImageView gra = factory.findViewById(R.id.ikonatipa);
        final Button dalej = factory.findViewById(R.id.dalej);
        tresctipa.setText("Witamy w grze Referee Master\n\nDzięki temu samouczkowi zrozumiesz zasady działania gry.");
        dalej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(u==1)
                {
                    tresctipa.setText("Na początku gry przeszedłeś test określający twoją dotychczasową wiedzę na temat przepisów po 5 dniach używania aplikacji test zostanie powtórzony w celu zbadania progresu");
                    u++;
                }
              else   if(u==2)
                {
                    tresctipa.setText("Pierwsza gra to Regulaminowy król polega ona na uzupełnieniu punktu regulaminu w czasie 90 sekund");
                    gra.setImageResource(R.drawable.pierwszaikonagry);
                    u++;
                }
                else  if(u==3)
                {
                    tresctipa.setText("Druga gra polega na odpowiadaniu na krótkie pytania TAK lub NIE w czasie 20 sekund");
                    gra.setImageResource(R.drawable.truefalse);
                    u++;
                }
                else  if(u==4)
                {
                    tresctipa.setText("Trzecia gra polega na odpowiadaniu na pytania z katalogu ZPRP na jedno pytanie masz 90 sekund");
                    gra.setImageResource(R.drawable.trzeciagra);
                    u++;
                }
                else  if(u==5)
                {
                    tresctipa.setText("Czwarta gra jest to egzamin który zostanie odblokowany dopiero po osiągnięciu w sumie 50 punktów ze wszystkich Quizów\nOdpowiadasz na 40 pytań na jedno pytanie masz 90 sekund");
                    gra.setImageResource(R.drawable.awardcup);
                    u++;
                }
                else   if(u==6)
                {
                    tresctipa.setText("Z każdej gry otrzymujesz punkty doświadczenia wraz z punktami rośnie twój poziom. Dodatkowo za każdą udzieloną dobrą odpowiedź dostajesz jeden bonus w postaci złotego gwizdka, 10 złotych gwizdków możesz kiedy źle odpowiesz na pytanie a chcesz grać dalej");
                    gra.setImageResource(R.drawable.tutorialikona);
                    u++;
                }

                else if(u==7)
                {
                    tresctipa.setText("Powodzenia!");
                    gra.setImageResource(R.drawable.kciuk);
                    u++;
                    dalej.setText("Koniec");
                }
                else if(u==8)
                {
                    factory.dismiss();

                    u=1;
                }

                tip.setText(u+"");
            }
        });

        factory.show();

    }


}
