package com.cyfoes.aditya.slate;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.PhoneLookup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;


import java.io.PrintStream;
import java.util.ArrayList;

import spencerstudios.com.bungeelib.Bungee;


public class MainActivity extends AppCompatActivity {

    public static Context mcontext;
    private int _xDelta;
    private int _yDelta;
    Button arrowdown;
    Button arrowup;
    FrameLayout blackframe;
    FrameLayout blueframe;
    Button btnclear;
    Button btneraser;
    Button btnpencil;
    Button btnpointer;
    String buttonclicked = "";
    ImageView checkblack;
    ImageView checkblue;
    ImageView checkgreen;
    ImageView checkpink;
    ImageView checkred;
    float checkx = 0.0f;
    float checky = 0.0f;
    ImageView checkyellow;
    ImageView circle;
    DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("all");
    DatabaseReference dbrlinks = FirebaseDatabase.getInstance().getReference("links");
    DatabaseReference dbrregister = FirebaseDatabase.getInstance().getReference("register");
    DatabaseReference dbrchat = FirebaseDatabase.getInstance().getReference("chats");
    Dialog dialog;
    LinearLayout drawerlayout;
    Boolean eraseron = Boolean.valueOf(false);
    FirebaseAuth fauth = FirebaseAuth.getInstance();
    String flow = "";
    FrameLayout geenframe;
    Handler handler;
    Handler handlerlicence;
    int height;
    ImageView imgblack;
    ImageView imgblue;
    ImageView imggreen;
    ImageView imgpink;
    ImageView imgred;
    ImageView imgyellow;
    SeekBar linewidthseek;
    //private InterstitialAd mInterstitialAd;
    LinearLayout mainlayout;
    DatabaseHelper mydb;
    ChatDatabase chatDatabase;
    Menu mymenu;
    NewPaintView newPaintView;
    PaintView paintView;
    FrameLayout paintframe;
    LinearLayout paintlayout;
    RelativeLayout parentView;

    /* renamed from: pd */
    ProgressDialog pd;
    Boolean pendown = Boolean.valueOf(true);
    Boolean pendowncheck = Boolean.valueOf(true);
    FrameLayout pinkframe;
    ImageView pointer;
    Boolean pointeron = Boolean.valueOf(false);
    String points = "";
    String pointsoffline = "";
    FrameLayout redframe;
    String rejected_sessionname = "";
    Runnable runnable;
    Runnable runnablelicence;
    SharedPreferences sharedPreferences;
    Boolean startsession = Boolean.valueOf(false);
    int width;
    FrameLayout yellowframe;
    Animation zoomin;
    Animation zoomout;
    ImageView chat;
    LinearLayout laychat;
    LinearLayout linearchat;
    ImageButton btnsend;
    ScrollView chatscroll;
    EditText message;
    SharedPreferences sprefchat;
    Button clrchat;
    LinearLayout savechatlayout;
    ImageButton savechatbtn;
    ScrollView savedscroll;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //MobileAds.initialize(this, getString(R.string.admob_app_id));
        mcontext = this;
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission("android.permission.READ_CONTACTS") == PackageManager.PERMISSION_DENIED) {
            requestcontact();
        }
        //this.mInterstitialAd = newInterstitialAd();
        //loadInterstitial();
        //((AdView) findViewById(R.id.adView)).loadAd(new GestureDescription.Builder().setRequestAgent("android_studio:ad_template").build());
        this.pd = new ProgressDialog(mcontext);
        sprefchat = getSharedPreferences("chat", 0);
        this.sharedPreferences = getSharedPreferences("penactive", 0);
        this.mydb = new DatabaseHelper(this);
        chatDatabase = new ChatDatabase(MainActivity.this);
        this.sharedPreferences.edit().putBoolean("eraser", false).commit();

        //deleteOldDrawings();

        savedscroll = (ScrollView) findViewById(R.id.savedscroll);
        savechatlayout = (LinearLayout) findViewById(R.id.savedchatlayout);
        savechatbtn = (ImageButton) findViewById(R.id.savedchatbtn);
        clrchat = (Button) findViewById(R.id.clrchat);
        message = (EditText) findViewById(R.id.message);
        chatscroll = (ScrollView) findViewById(R.id.chatscroll);
        btnsend = (ImageButton) findViewById(R.id.btnsendchat);
        linearchat = (LinearLayout) findViewById(R.id.linearchat);
        laychat = (LinearLayout) findViewById(R.id.laychat);
        chat = (ImageView) findViewById(R.id.chat);
        this.mainlayout = (LinearLayout) findViewById(R.id.mainlayout);
        this.btnclear = (Button) findViewById(R.id.btnclear);
        this.paintlayout = (LinearLayout) findViewById(R.id.paintlayout);
        this.drawerlayout = (LinearLayout) findViewById(R.id.drawerlayout);
        this.arrowdown = (Button) findViewById(R.id.arrowdown);
        this.arrowup = (Button) findViewById(R.id.arrowup);
        this.imgblack = (ImageView) findViewById(R.id.img16777216);
        this.imgred = (ImageView) findViewById(R.id.img65536);
        this.imgyellow = (ImageView) findViewById(R.id.img256);
        this.imgblue = (ImageView) findViewById(R.id.img16776961);
        this.imggreen = (ImageView) findViewById(R.id.img16711936);
        this.imgpink = (ImageView) findViewById(R.id.img65281);
        this.arrowdown.setVisibility(View.INVISIBLE);
        this.pinkframe = (FrameLayout) findViewById(R.id.pinkframe);
        this.blackframe = (FrameLayout) findViewById(R.id.blackframe);
        this.geenframe = (FrameLayout) findViewById(R.id.greenframe);
        this.redframe = (FrameLayout) findViewById(R.id.redframe);
        this.blueframe = (FrameLayout) findViewById(R.id.blueframe);
        this.yellowframe = (FrameLayout) findViewById(R.id.yellowframe);
        this.checkblack = (ImageView) findViewById(R.id.check16777216);
        this.linewidthseek = (SeekBar) findViewById(R.id.linewidthseek);
        this.btnpencil = (Button) findViewById(R.id.btnpencil);
        this.btneraser = (Button) findViewById(R.id.btneraser);
        this.pointer = (ImageView) findViewById(R.id.pointer);
        this.paintframe = (FrameLayout) findViewById(R.id.paintframe);
        this.btnpointer = (Button) findViewById(R.id.btnpointer);
        this.linewidthseek.setMax(50);
        this.linewidthseek.setProgress(4);
        this.btnpencil.setClickable(false);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.newPaintView = new NewPaintView(this);
        this.newPaintView.setLayoutParams(layoutParams);
        this.newPaintView.setBackgroundColor(Color.parseColor("white"));
        this.paintlayout.addView(this.newPaintView);
        if (getIntent().hasExtra("saved")) {
            openSavedDrawing(getIntent().getExtras().getString("saved"));
        }

        savechatbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (savedscroll.getVisibility() == View.GONE) {
                    savedscroll.setVisibility(View.VISIBLE);
                } else {
                    savedscroll.setVisibility(View.GONE);
                }
            }
        });

        KeyboardUtils.addKeyboardToggleListener(this, new KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {
                if (getIntent().hasExtra("saved")) {
                    openSavedDrawing(getIntent().getExtras().getString("saved"));
                }
                openunsaveddrawing();
            }
        });

        clrchat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sprefchat.edit().putString("chattemp", "").commit();
                linearchat.removeAllViews();
            }
        });

        final Handler handler = new Handler();
        final int delay = 1000; //milliseconds

        handler.postDelayed(new Runnable() {
            public void run() {
                //do something
                setbottm();
                handler.postDelayed(this, delay);
            }
        }, delay);

        chat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (laychat.getVisibility() == View.GONE) {
                    laychat.setVisibility(View.VISIBLE);
                } else {
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {

                    }
                    laychat.setVisibility(View.GONE);
                }
            }
        });

        if (!sharedPreferences.getBoolean("startsession", false)) {
            chat.setVisibility(View.GONE);
        } else {
            opentempchat();
            checkmessages();
        }

        openunsaveddrawing();

        btnsend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                LinearLayout lay = (LinearLayout) inflater.inflate(R.layout.mytext, null, false);
                TextView text = lay.findViewById(R.id.mytextview);
                if (message.getText().toString().trim().length() > 0) {
                    playsentaudio();
                    text.setText(message.getText().toString().trim());

                    String cs = sharedPreferences.getString("current_session", "");
                    String st = sharedPreferences.getString("current_status", "");

                    String ch = sprefchat.getString("chattemp", "");
                    if (ch.equals("")) {
                        ch = "^^<;;>" + message.getText().toString().trim();
                    } else {
                        ch = ch + "##,##^^<;;>" + message.getText().toString().trim();
                    }
                    sprefchat.edit().putString("chattemp", ch).commit();

                    if (st.equals("inviter")) {
                        dbrchat.child(cs).child("tro").child(System.currentTimeMillis() + "").setValue(message.getText().toString().trim());
                    } else {
                        dbrchat.child(cs).child("fro").child(System.currentTimeMillis() + "").setValue(message.getText().toString().trim());
                    }

                    message.setText("");
                    linearchat.addView(lay);
                }
            }
        });

        this.btnclear.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.paintView.clearscreen();
                MainActivity.this.mydb.deleteall();
            }
        });
        this.arrowup.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.arrowup.setVisibility(View.INVISIBLE);
                MainActivity.this.arrowdown.setVisibility(View.VISIBLE);
                float applyDimension = TypedValue.applyDimension(1, 0.0f, MainActivity.this.getResources().getDisplayMetrics());
                int i = (int) applyDimension;
                ResizeAnimation resizeAnimation = new ResizeAnimation(MainActivity.this.drawerlayout, (int) TypedValue.applyDimension(1, 200.0f, MainActivity.this.getResources().getDisplayMetrics()), i);
                resizeAnimation.setDuration(500);
                MainActivity.this.drawerlayout.startAnimation(resizeAnimation);
            }
        });
        this.arrowdown.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.arrowdown.setVisibility(View.INVISIBLE);
                MainActivity.this.arrowup.setVisibility(View.VISIBLE);
                float applyDimension = TypedValue.applyDimension(1, 0.0f, MainActivity.this.getResources().getDisplayMetrics());
                int i = (int) applyDimension;
                ResizeAnimationSmall resizeAnimationSmall = new ResizeAnimationSmall(MainActivity.this.drawerlayout, i, (int) TypedValue.applyDimension(1, 200.0f, MainActivity.this.getResources().getDisplayMetrics()));
                resizeAnimationSmall.setDuration(500);
                MainActivity.this.drawerlayout.startAnimation(resizeAnimationSmall);
            }
        });
        this.linewidthseek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (i > 0) {
                    MainActivity.this.newPaintView.changewidth(i);
                    MainActivity.this.sharedPreferences.edit().putInt("linewidth", i).commit();
                }
            }
        });
        this.zoomin = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        this.zoomout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);
        this.imgblack.startAnimation(this.zoomin);
        this.checkblack.setVisibility(View.VISIBLE);
        this.btneraser.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.newPaintView.changecolor("white");
                MainActivity.this.btneraser.setClickable(false);
                MainActivity.this.btnpencil.setClickable(true);
                MainActivity.this.pointeron = Boolean.valueOf(false);
                //MainActivity.this.pointer.setVisibility(View.INVISIBLE);
                MainActivity.this.newPaintView.setPointeron(Boolean.valueOf(false));
            }
        });
        this.btnpencil.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.newPaintView.restorecolor();
                MainActivity.this.btneraser.setClickable(true);
                MainActivity.this.btnpencil.setClickable(false);
                MainActivity.this.pointeron = Boolean.valueOf(false);
                //MainActivity.this.pointer.setVisibility(View.VISIBLE);
                MainActivity.this.newPaintView.setPointeron(Boolean.valueOf(false));
            }
        });
        this.imgred.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.zoomoutbuttons();
                MainActivity.this.newPaintView.changecolor("red");
                MainActivity.this.imgred.startAnimation(MainActivity.this.zoomin);
                MainActivity.this.imgred.setClickable(false);
                ((ImageView) MainActivity.this.findViewById(R.id.check65536)).setVisibility(View.VISIBLE);
                MainActivity.this.pointeron = Boolean.valueOf(false);
                //MainActivity.this.pointer.setVisibility(View.VISIBLE);
                MainActivity.this.newPaintView.setPointeron(Boolean.valueOf(false));
            }
        });
        this.imgblack.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.zoomoutbuttons();
                MainActivity.this.newPaintView.changecolor("black");
                MainActivity.this.imgblack.startAnimation(MainActivity.this.zoomin);
                MainActivity.this.imgblack.setClickable(false);
                ((ImageView) MainActivity.this.findViewById(R.id.check16777216)).setVisibility(View.VISIBLE);
                MainActivity.this.pointeron = Boolean.valueOf(false);
                //MainActivity.this.pointer.setVisibility(View.VISIBLE);
                MainActivity.this.newPaintView.setPointeron(Boolean.valueOf(false));
            }
        });
        this.imggreen.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.zoomoutbuttons();
                MainActivity.this.newPaintView.changecolor("green");
                MainActivity.this.imggreen.startAnimation(MainActivity.this.zoomin);
                MainActivity.this.imggreen.setClickable(false);
                ((ImageView) MainActivity.this.findViewById(R.id.check16711936)).setVisibility(View.VISIBLE);
                MainActivity.this.pointeron = Boolean.valueOf(false);
                //MainActivity.this.pointer.setVisibility(View.VISIBLE);
                MainActivity.this.newPaintView.setPointeron(Boolean.valueOf(false));
            }
        });
        this.imgyellow.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.zoomoutbuttons();
                MainActivity.this.newPaintView.changecolor("yellow");
                MainActivity.this.imgyellow.startAnimation(MainActivity.this.zoomin);
                MainActivity.this.imgyellow.setClickable(false);
                ((ImageView) MainActivity.this.findViewById(R.id.check256)).setVisibility(View.VISIBLE);
                MainActivity.this.pointeron = Boolean.valueOf(false);
                //MainActivity.this.pointer.setVisibility(View.VISIBLE);
                MainActivity.this.newPaintView.setPointeron(Boolean.valueOf(false));
            }
        });
        this.imgpink.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.zoomoutbuttons();
                MainActivity.this.newPaintView.changecolor("magenta");
                MainActivity.this.imgpink.startAnimation(MainActivity.this.zoomin);
                MainActivity.this.imgpink.setClickable(false);
                ((ImageView) MainActivity.this.findViewById(R.id.check65281)).setVisibility(View.VISIBLE);
                MainActivity.this.pointeron = Boolean.valueOf(false);
                //MainActivity.this.pointer.setVisibility(View.VISIBLE);
                MainActivity.this.newPaintView.setPointeron(Boolean.valueOf(false));
            }
        });
        this.imgblue.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.zoomoutbuttons();
                MainActivity.this.newPaintView.changecolor("blue");
                MainActivity.this.imgblue.startAnimation(MainActivity.this.zoomin);
                MainActivity.this.imgblue.setClickable(false);
                ((ImageView) MainActivity.this.findViewById(R.id.check16776961)).setVisibility(View.VISIBLE);
                MainActivity.this.pointeron = Boolean.valueOf(false);
                //MainActivity.this.pointer.setVisibility(View.VISIBLE);
                MainActivity.this.newPaintView.setPointeron(Boolean.valueOf(false));
            }
        });
        this.btnpointer.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.pointeron = Boolean.valueOf(true);
                MainActivity.this.pointer.setVisibility(View.INVISIBLE);
                MainActivity.this.newPaintView.setPointeron(Boolean.valueOf(true));
            }
        });
        if (this.fauth.getCurrentUser() != null) {
            this.dbr.addValueEventListener(new ValueEventListener() {
                public void onCancelled(DatabaseError databaseError) {
                }

                public void onDataChange(DataSnapshot dataSnapshot) {
                    float f;
                    DataSnapshot dataSnapshot2 = dataSnapshot;
                    for (DataSnapshot dataSnapshot3 : dataSnapshot.getChildren()) {
                        if (dataSnapshot3.getKey().length() > 7) {
                            String[] split = dataSnapshot3.getKey().toString().split("-");
                            char c = 0;
                            String str = split[0];
                            int i = 1;
                            if (split[1].equals(MainActivity.this.getnationalnumber(MainActivity.this.fauth.getCurrentUser().getPhoneNumber().toString()))) {
                                MainActivity.this.flow = "tro";
                            } else if (str.equals(MainActivity.this.getnationalnumber(MainActivity.this.fauth.getCurrentUser().getPhoneNumber().toString())) || str.equals(MainActivity.this.fauth.getCurrentUser().getPhoneNumber().toString())) {
                                MainActivity.this.flow = "fro";
                            }
                            if (dataSnapshot3.hasChild("clear") && dataSnapshot3.child("clear").getValue().toString().equals("1")) {
                                MainActivity.this.newPaintView.clearCanvas();
                                MainActivity.this.getIntent().removeExtra("saved");
                                MainActivity.this.deleteOldDrawings();
                                dataSnapshot3.child("clear").getRef().setValue(null);
                            }
                            if (dataSnapshot3.hasChild("start")) {
                                if (MainActivity.this.flow.equals("tro")) {
                                    if (dataSnapshot3.child("start").getValue().toString().equals("1")) {
                                        MainActivity.this.showstartdialog(dataSnapshot3.getKey().toString(), str);
                                    }
                                } else if (MainActivity.this.flow.equals("fro")) {
                                    if (dataSnapshot3.child("start").getValue().toString().equals("0")) {
                                        MainActivity.this.sharedPreferences.edit().putString("current_session", "").commit();
                                        MainActivity.this.sharedPreferences.edit().putString("current_status", "").commit();
                                        MainActivity.this.pd.dismiss();
                                        MainActivity.this.onCreateOptionsMenu(MainActivity.this.mymenu);
                                        MainActivity.this.dialog.dismiss();
                                        Toast.makeText(MainActivity.this, "Request was rejected", Toast.LENGTH_SHORT).show();
                                        dataSnapshot3.child("start").getRef().setValue(null);
                                    }
                                    if (dataSnapshot3.child("start").getValue().toString().equals("2")) {
                                        MainActivity.this.pd.dismiss();
                                        MainActivity.this.onCreateOptionsMenu(MainActivity.this.mymenu);
                                        MainActivity.this.dialog.dismiss();
                                        Toast.makeText(MainActivity.this, "Request was accepted", Toast.LENGTH_SHORT).show();
                                        MainActivity.this.sharedPreferences.edit().putBoolean("startsession", true).commit();
                                        MainActivity.this.sharedPreferences.edit().putLong("starttime", System.currentTimeMillis()).commit();
                                        dataSnapshot3.child("start").getRef().setValue(null);
                                        deleteOldDrawings();
                                        sprefchat.edit().putString("chattemp", "").commit();
                                        linearchat.removeAllViews();
                                        chat.setVisibility(View.VISIBLE);
                                        laychat.setVisibility(View.GONE);
                                        checkmessages();
                                    }
                                }
                            }
                            if (MainActivity.this.sharedPreferences.getBoolean("startsession", false)) {
                                float f2 = 1.0f;
                                if (dataSnapshot3.hasChild("000res")) {
                                    String[] split2 = dataSnapshot3.child("000res").getValue().toString().split("/");
                                    float parseFloat = Float.parseFloat(split2[0]);
                                    f2 = ((float) MainActivity.this.newPaintView.getWidth()) / parseFloat;
                                    f = ((float) MainActivity.this.newPaintView.getHeight()) / Float.parseFloat(split2[1]);
                                } else {
                                    f = 1.0f;
                                }
                                if (dataSnapshot2.child(MainActivity.this.sharedPreferences.getString("current_session", "")).hasChild("end")) {
                                    MainActivity.this.showenddialog();
                                }
                                if (dataSnapshot2.child(MainActivity.this.sharedPreferences.getString("current_session", "")).hasChild("undo")) {
                                    for (DataSnapshot dataSnapshot4 : dataSnapshot2.child(MainActivity.this.sharedPreferences.getString("current_session", "")).child("undo").getChildren()) {
                                        String str2 = dataSnapshot4.getKey().toString();
                                        if (!dataSnapshot4.getValue().toString().equals(MainActivity.this.fauth.getCurrentUser().getPhoneNumber().toString())) {
                                            MainActivity.this.newPaintView.undoOnline(str2);
                                            dataSnapshot4.getRef().setValue(null);
                                        }
                                    }
                                }
                                if (dataSnapshot2.child(MainActivity.this.sharedPreferences.getString("current_session", "")).hasChild("start")) {
                                    dataSnapshot2.child(MainActivity.this.sharedPreferences.getString("current_session", "")).child("start").getRef().setValue(null);
                                }
                                if (dataSnapshot3.hasChild(MainActivity.this.flow)) {
                                    for (DataSnapshot dataSnapshot5 : dataSnapshot3.child(MainActivity.this.flow).getChildren()) {
                                        String obj = dataSnapshot5.getValue().toString();
                                        String[] split3 = obj.split("/");
                                        String str3 = dataSnapshot3.getKey().toString();
                                        String str4 = dataSnapshot5.getKey().toString();
                                        StringBuilder sb = new StringBuilder();
                                        sb.append(f2);
                                        sb.append("/");
                                        sb.append(f);
                                        Boolean.valueOf(MainActivity.this.mydb.insertData("samjibhai", obj, sb.toString(), str4, str3));
                                        String str5 = split3[split3.length - 2];
                                        String str6 = split3[split3.length - i];
                                        MainActivity.this.sharedPreferences.edit().putString("peno", str5).commit();
                                        if (str5.equals("-16777216")) {
                                            MainActivity.this.newPaintView.changecoloro("black", Float.parseFloat(str6));
                                        }
                                        if (str5.equals("-16776961")) {
                                            MainActivity.this.newPaintView.changecoloro("blue", Float.parseFloat(str6));
                                        }
                                        if (str5.equals("-65536")) {
                                            MainActivity.this.newPaintView.changecoloro("red", Float.parseFloat(str6));
                                        }
                                        if (str5.equals("-256")) {
                                            MainActivity.this.newPaintView.changecoloro("yellow", Float.parseFloat(str6));
                                        }
                                        if (str5.equals("-65281")) {
                                            MainActivity.this.newPaintView.changecoloro("magenta", Float.parseFloat(str6));
                                        }
                                        if (str5.equals("-16711936")) {
                                            MainActivity.this.newPaintView.changecoloro("green", Float.parseFloat(str6));
                                        }
                                        if (str5.equals("-1")) {
                                            MainActivity.this.newPaintView.changecoloro("white", Float.parseFloat(str6));
                                        }
                                        int length = split3.length;
                                        int i2 = 0;
                                        int i3 = 0;
                                        int i4 = 0;
                                        while (i2 < length) {
                                            String str7 = split3[i2];
                                            i3 += i;
                                            i4 += i;
                                            if (i4 < split3.length - 2) {
                                                String[] split4 = str7.split(",");
                                                float parseFloat2 = Float.parseFloat(split4[c]) * f2;
                                                float parseFloat3 = Float.parseFloat(split4[1]) * f;
                                                if (i3 == 1) {
                                                    MainActivity.this.newPaintView.customdraw(MotionEvent.obtain(0, 0, 0, parseFloat2, parseFloat3, 0.5f, 5.0f, 0, 1.0f, 1.0f, 0, 0));
                                                    dataSnapshot5.getRef().setValue(null);
                                                } else {
                                                    MainActivity.this.newPaintView.customdraw(MotionEvent.obtain(0, 0, 2, parseFloat2, parseFloat3, 0.5f, 5.0f, 0, 1.0f, 1.0f, 0, 0));
                                                    dataSnapshot5.getRef().setValue(null);
                                                }
                                            }
                                            i2++;
                                            c = 0;
                                            i = 1;
                                        }
                                        i = 1;
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }

    }

    private void checkmessages() {
        final String cs = sharedPreferences.getString("current_session", "");
        final String st = sharedPreferences.getString("current_status", "");
        String st1 = "tro";
        if (st.equals("inviter")) {
            st1 = "fro";
        } else {
            st1 = "tro";
        }
        final String finalSt = st1;
        dbrchat.child(cs).child(st1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dm : dataSnapshot.getChildren()) {
                    playreceiveaudio();
                    LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                    LinearLayout lay = (LinearLayout) inflater.inflate(R.layout.yourtext, null, false);
                    TextView text = lay.findViewById(R.id.yourtextview);
                    text.setText(dm.getValue().toString());
                    linearchat.addView(lay);

                    String ch = sprefchat.getString("chattemp", "");
                    if (ch.equals("")) {
                        ch = "!!<;;>" + dm.getValue().toString();
                    } else {
                        ch = ch + "##,##!!<;;>" + dm.getValue().toString();
                    }
                    sprefchat.edit().putString("chattemp", ch).commit();

                    dbrchat.child(cs).child(finalSt).child(dm.getKey().toString()).setValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void playsentaudio() {
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.hollow);
        mp.start();
    }

    private void playreceiveaudio() {
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.receive);
        mp.start();
    }

    private void setbottm() {
        View lastChild = chatscroll.getChildAt(chatscroll.getChildCount() - 1);
        int bottom = lastChild.getBottom() + chatscroll.getPaddingBottom();
        int sy = chatscroll.getScrollY();
        int sh = chatscroll.getHeight();
        int delta = bottom - (sy + sh);

        chatscroll.smoothScrollBy(0, delta);
    }

    private void opentempchat() {
        String ch = sprefchat.getString("chattemp", "");
        if (ch.length() > 0) {
            String[] csplit = ch.split("##,##");
            for (int i = 0; i < csplit.length; i++) {
                String[] csp2 = csplit[i].split("<;;>");
                if (csp2[0].equals("^^")) {
                    LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                    LinearLayout lay = (LinearLayout) inflater.inflate(R.layout.mytext, null, false);
                    TextView text = lay.findViewById(R.id.mytextview);
                    text.setText(csp2[1]);
                    linearchat.addView(lay);
                } else {
                    LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                    LinearLayout lay = (LinearLayout) inflater.inflate(R.layout.yourtext, null, false);
                    TextView text = lay.findViewById(R.id.yourtextview);
                    text.setText(csp2[1]);
                    linearchat.addView(lay);
                }
            }
        }
    }

    private void openSavedDrawing(String str) {
        Cursor allData = this.mydb.getAllData();
        Cursor chatdata = chatDatabase.getalldata();

        if (chatdata != null && chatdata.getCount() > 0) {
            savechatlayout.removeAllViews();
            savechatbtn.setVisibility(View.VISIBLE);
            while (chatdata.moveToNext()) {
                if (chatdata.getString(1).equals(str)) {
                    String chat = chatdata.getString(2);
                    String parti = chatdata.getString(4);
                    String cs = sharedPreferences.getString("current_session", "");

                    String[] chatsplit1 = chat.split("##,##");
                    for (int k = 0; k < chatsplit1.length; k++) {
                        String[] sp2 = chatsplit1[k].split("<;;>");

                        if (sp2[0].equals("^^")) {
                            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                            LinearLayout lay = (LinearLayout) inflater.inflate(R.layout.mytext, null, false);
                            TextView text = lay.findViewById(R.id.mytextview);
                            text.setText(sp2[1]);
                            savechatlayout.addView(lay);
                        } else {
                            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                            LinearLayout lay = (LinearLayout) inflater.inflate(R.layout.yourtext, null, false);
                            TextView text = lay.findViewById(R.id.yourtextview);
                            text.setText(sp2[1]);
                            savechatlayout.addView(lay);
                        }
                    }
                }
            }
        }

        if (allData != null && allData.getCount() > 0) {
            while (allData.moveToNext()) {
                if (allData.getString(1).equals(str)) {
                    String string = allData.getString(2);
                    String string2 = allData.getString(3);
                    String[] split = string.split("/");
                    String str2 = split[split.length - 2];
                    String str3 = split[split.length - 1];
                    float parseFloat = Float.parseFloat(string2.split("/")[0]);
                    float parseFloat2 = Float.parseFloat(string2.split("/")[1]);
                    if (str2.equals("-16777216")) {
                        this.newPaintView.changecolors("black", Float.parseFloat(str3));
                    }
                    if (str2.equals("-16776961")) {
                        this.newPaintView.changecolors("blue", Float.parseFloat(str3));
                    }
                    if (str2.equals("-65536")) {
                        this.newPaintView.changecolors("red", Float.parseFloat(str3));
                    }
                    if (str2.equals("-256")) {
                        this.newPaintView.changecolors("yellow", Float.parseFloat(str3));
                    }
                    if (str2.equals("-65281")) {
                        this.newPaintView.changecolors("magenta", Float.parseFloat(str3));
                    }
                    if (str2.equals("-16711936")) {
                        this.newPaintView.changecolors("green", Float.parseFloat(str3));
                    }
                    if (str2.equals("-1")) {
                        this.newPaintView.changecolors("white", Float.parseFloat(str3));
                    }
                    int i = 0;
                    int i2 = 0;
                    for (String str4 : split) {
                        i++;
                        i2++;
                        if (i2 < split.length - 2) {
                            String[] split2 = str4.split(",");
                            float parseFloat3 = Float.parseFloat(split2[0]) * parseFloat;
                            float parseFloat4 = Float.parseFloat(split2[1]) * parseFloat2;
                            if (i == 1) {
                                this.newPaintView.saveddraw(MotionEvent.obtain(0, 0, 0, parseFloat3, parseFloat4, 0.5f, 5.0f, 0, 1.0f, 1.0f, 0, 0));
                            } else {
                                this.newPaintView.saveddraw(MotionEvent.obtain(0, 0, 2, parseFloat3, parseFloat4, 0.5f, 5.0f, 0, 1.0f, 1.0f, 0, 0));
                            }
                        }
                    }
                }
            }
        }
    }

    private void openunsaveddrawing() {
        Cursor allData = this.mydb.getAllData();
        if (allData != null && allData.getCount() > 0) {
            while (allData.moveToNext()) {
                if (allData.getString(1).equals("samjibhai")) {
                    String string = allData.getString(2);
                    String string2 = allData.getString(3);
                    String[] split = string.split("/");
                    String str2 = split[split.length - 2];
                    String str3 = split[split.length - 1];
                    float parseFloat = Float.parseFloat(string2.split("/")[0]);
                    float parseFloat2 = Float.parseFloat(string2.split("/")[1]);
                    if (str2.equals("-16777216")) {
                        this.newPaintView.changecolors("black", Float.parseFloat(str3));
                    }
                    if (str2.equals("-16776961")) {
                        this.newPaintView.changecolors("blue", Float.parseFloat(str3));
                    }
                    if (str2.equals("-65536")) {
                        this.newPaintView.changecolors("red", Float.parseFloat(str3));
                    }
                    if (str2.equals("-256")) {
                        this.newPaintView.changecolors("yellow", Float.parseFloat(str3));
                    }
                    if (str2.equals("-65281")) {
                        this.newPaintView.changecolors("magenta", Float.parseFloat(str3));
                    }
                    if (str2.equals("-16711936")) {
                        this.newPaintView.changecolors("green", Float.parseFloat(str3));
                    }
                    if (str2.equals("-1")) {
                        this.newPaintView.changecolors("white", Float.parseFloat(str3));
                    }
                    int i = 0;
                    int i2 = 0;
                    for (String str4 : split) {
                        i++;
                        i2++;
                        if (i2 < split.length - 2) {
                            String[] split2 = str4.split(",");
                            float parseFloat3 = Float.parseFloat(split2[0]) * parseFloat;
                            float parseFloat4 = Float.parseFloat(split2[1]) * parseFloat2;
                            if (i == 1) {
                                this.newPaintView.saveddraw(MotionEvent.obtain(0, 0, 0, parseFloat3, parseFloat4, 0.5f, 5.0f, 0, 1.0f, 1.0f, 0, 0));
                            } else {
                                this.newPaintView.saveddraw(MotionEvent.obtain(0, 0, 2, parseFloat3, parseFloat4, 0.5f, 5.0f, 0, 1.0f, 1.0f, 0, 0));
                            }
                        }
                    }
                }
            }
        }
    }

    public void deleteOldDrawings() {
        Cursor allData = this.mydb.getAllData();
        if (allData != null && allData.getCount() > 0) {
            while (allData.moveToNext()) {
                String string = allData.getString(1);
                String string2 = allData.getString(0);
                if (string.equals("samjibhai")) {
                    this.mydb.deleteData(string2);
                }
            }
        }
    }

    public void showstartdialog(final String str, String str2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setTitle("New Session Request");
        String str3 = getname(this, str2);
        if (str3 != null) {
            str2 = str3;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append(" wants to connect. Do you want to allow?");
        builder.setMessage(sb.toString());
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.sharedPreferences.edit().putString("current_session", str).commit();
                MainActivity.this.sharedPreferences.edit().putString("current_status", "invitee").commit();
                MainActivity.this.dbr.child(str).child("start").setValue(Integer.valueOf(2));
                MainActivity.this.sharedPreferences.edit().putBoolean("startsession", true).commit();
                MainActivity.this.sharedPreferences.edit().putLong("starttime", System.currentTimeMillis()).commit();
                MainActivity.this.onCreateOptionsMenu(MainActivity.this.mymenu);
                deleteOldDrawings();
                sprefchat.edit().putString("chattemp", "").commit();
                linearchat.removeAllViews();
                chat.setVisibility(View.VISIBLE);
                laychat.setVisibility(View.GONE);
                checkmessages();

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.dbr.child(str).child("start").setValue(Integer.valueOf(0));
            }
        }).create().show();
    }

    public void showenddialog() {
        this.dbr.child(this.sharedPreferences.getString("current_session", "")).getRef().setValue(null);
        this.sharedPreferences.edit().putString("current_session", "").commit();
        this.sharedPreferences.edit().putString("current_status", "").commit();
        this.sharedPreferences.edit().putBoolean("startsession", false).commit();
        sprefchat.edit().putString("chattemp", "").commit();
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setTitle("Ending Session");
        builder.setMessage("This session has been ended at other side");
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.onCreateOptionsMenu(MainActivity.this.mymenu);
            }
        }).create().show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.mymenu = menu;
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        if (this.fauth.getCurrentUser() != null) {
            menu.findItem(R.id.logout).setVisible(true);
            menu.findItem(R.id.login).setVisible(false);
        } else {
            menu.findItem(R.id.logout).setVisible(false);
            menu.findItem(R.id.login).setVisible(true);
        }
        if (this.sharedPreferences.getString("current_session", "").equals("")) {
            menu.findItem(R.id.end).setVisible(false);
            menu.findItem(R.id.group).setVisible(false);
            menu.findItem(R.id.end).setVisible(false);
        } else {
            menu.findItem(R.id.adduser).setVisible(false);
        }
        invalidateOptionsMenu();
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.aboutus:
                aboutus_dialog();
                return true;
            case R.id.adduser:
                if (this.fauth.getCurrentUser() != null) {
                    /*this.buttonclicked = "adduser";
                    showInterstitial();*/
                    startActivity(new Intent(this, login.class));
                    Bungee.zoom(mcontext);
                } else {
                    startActivity(new Intent(this, login.class));
                    Bungee.zoom(mcontext);
                }
                return true;
            /*case R.id.buy:
                openpaidlink();
                break;*/
            case R.id.clear:
                this.newPaintView.clearCanvas();
                getIntent().removeExtra("saved");
                deleteOldDrawings();
                if (!this.sharedPreferences.getString("current_session", "").equals("")) {
                    this.dbr.child(this.sharedPreferences.getString("current_session", "")).child("clear").setValue(Integer.valueOf(1));
                }
                return true;
            case R.id.dots:
                return true;
            case R.id.end:
                endsessiondialog();
                return true;
            case R.id.group:
                groupinfo();
                return true;
            case R.id.login:
                startActivity(new Intent(this, login.class));
                Bungee.zoom(this);
                return true;
            case R.id.logout:
                this.fauth.signOut();
                finish();
                startActivity(getIntent());
                Bungee.zoom(this);
                return true;
            case R.id.save:
                if (getIntent().hasExtra("saved")) {
                    overright(getIntent().getExtras().getString("saved"));
                } else {
                    showSaveDialog();
                }
                return true;
            case R.id.saved:
                startActivity(new Intent(this, saved_drawings.class));
                Bungee.zoom(mcontext);
                /*this.buttonclicked = "saved";
                showInterstitial();*/
                return true;
            case R.id.undo:
                this.newPaintView.redraw();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void groupinfo() {
        ArrayList arrayList = new ArrayList();
        Dialog dialog2 = new Dialog(mcontext);
        dialog2.setContentView(R.layout.group_dialog);
        dialog2.show();
        dialog2.getWindow().setLayout(-1, -2);
        ListView listView = (ListView) dialog2.findViewById(R.id.grouplist);
        String string = this.sharedPreferences.getString("current_session", "");
        if (!string.equals("")) {
            String[] split = string.split("-");
            arrayList.add("You");
            for (String str : split) {
                if (!str.equals(this.fauth.getCurrentUser().getPhoneNumber().toString()) && !str.equals(getnationalnumber(this.fauth.getCurrentUser().getPhoneNumber().toString()))) {
                    String str2 = getname(mcontext, str);
                    if (str2 != null) {
                        arrayList.add(str2);
                    } else {
                        arrayList.add(str);
                    }
                }
            }
            listView.setAdapter(new ArrayAdapter<String>(mcontext, R.layout.groupinfotext, arrayList));
        }
    }

    private void aboutus_dialog() {
        Dialog dialog2 = new Dialog(mcontext);
        dialog2.setContentView(R.layout.aboutus_dialog);
        dialog2.show();
    }

    private void overright(final String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setTitle("Save file");
        StringBuilder sb = new StringBuilder();
        sb.append("Do you want to override the file named '");
        sb.append(str);
        sb.append("'?");
        builder.setMessage(sb.toString());
        builder.setPositiveButton("Override", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Cursor allData = MainActivity.this.mydb.getAllData();
                String valueOf = String.valueOf(System.currentTimeMillis());
                if (allData != null && allData.getCount() > 0) {
                    while (allData.moveToNext()) {
                        String string = allData.getString(0);
                        String string2 = allData.getString(1);

                        String ch = sprefchat.getString("chattemp", "");
                        String string6 = MainActivity.this.sharedPreferences.getString("current_session", "");
                        chatDatabase.insertdata(string2, ch, System.currentTimeMillis() + "", string6);

                        if (string2.equals("samjibhai")) {
                            MainActivity.this.mydb.update_name(str, string, valueOf);
                        }
                        if (string2.equals(str)) {
                            MainActivity.this.mydb.update_name(str, string, valueOf);
                        }
                    }
                    Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("Rename", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.showSaveDialog();
            }
        }).create().show();
    }

    public void showSaveDialog() {
        final Dialog dialog2 = new Dialog(mcontext);
        dialog2.setContentView(R.layout.save_dialog);
        dialog2.show();
        dialog2.getWindow().setLayout(-1, -2);
        final EditText editText = (EditText) dialog2.findViewById(R.id.etname);
        ((Button) dialog2.findViewById(R.id.btnsave)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String trim = editText.getText().toString().trim();
                String valueOf = String.valueOf(System.currentTimeMillis());
                if (trim.length() > 0) {
                    Cursor allData = MainActivity.this.mydb.getAllData();
                    if (allData != null && allData.getCount() > 0) {
                        while (allData.moveToNext()) {
                            String string = allData.getString(0);
                            String string2 = allData.getString(1);
                            if (string2.equals("samjibhai")) {
                                MainActivity.this.mydb.update_name(trim, string, valueOf);
                                String string6 = MainActivity.this.sharedPreferences.getString("current_session", "");
                                String ch = sprefchat.getString("chattemp", "");
                                if (!string6.equals("")) {
                                    chatDatabase.insertdata(trim, ch, System.currentTimeMillis() + "", string6);
                                }
                            }
                            if (MainActivity.this.getIntent().hasExtra("saved")) {
                                String string3 = MainActivity.this.getIntent().getExtras().getString("saved");
                                String string4 = allData.getString(2);
                                String string5 = allData.getString(3);
                                if (string2.equals(string3)) {
                                    String string6 = MainActivity.this.sharedPreferences.getString("current_session", "");
                                    if (!string6.equals("")) {
                                        MainActivity.this.mydb.insertData(trim, string4, string5, valueOf, string6);
                                    } else {
                                        MainActivity.this.mydb.insertData(trim, string4, string5, valueOf, "You");
                                    }
                                    Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
                dialog2.dismiss();
            }
        });
    }

    private void endsessiondialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setTitle("Ending Session");
        builder.setMessage("Are you sure you want to end this session?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.dbr.child(MainActivity.this.sharedPreferences.getString("current_session", "")).child("end").setValue(Integer.valueOf(1));
                MainActivity.this.sharedPreferences.edit().putString("current_session", "").commit();
                MainActivity.this.sharedPreferences.edit().putString("current_status", "").commit();
                MainActivity.this.sharedPreferences.edit().putBoolean("startsession", false).commit();
                sprefchat.edit().putString("chattemp", "").commit();
                chat.setVisibility(View.GONE);
                MainActivity.this.onCreateOptionsMenu(MainActivity.this.mymenu);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create().show();
    }

    private void showAddUserDialog() {
        this.dialog = new Dialog(mcontext);
        this.dialog.setContentView(R.layout.add_user_dialog);
        this.dialog.show();
        TextView textView = (TextView) this.dialog.findViewById(R.id.txtclose);
        final EditText editText = (EditText) this.dialog.findViewById(R.id.etcontact);
        Button button = (Button) this.dialog.findViewById(R.id.btnadd);
        Button button2 = (Button) this.dialog.findViewById(R.id.btncontact);
        String string = this.sharedPreferences.getString("assign_contact", "");
        String str = getnationalnumber(string);
        if (str.equals("0")) {
            editText.setText(string.replaceAll("[^A-Za-z0-9]", ""));
        } else {
            editText.setText(str.replaceAll("[^A-Za-z0-9]", ""));
        }
        textView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.dialog.dismiss();
            }
        });
        button2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (VERSION.SDK_INT < 23) {
                    return;
                }
                if (ContextCompat.checkSelfPermission(MainActivity.this, "android.permission.READ_CONTACTS") != PackageManager.PERMISSION_GRANTED) {
                    MainActivity.this.requestcontact();
                    return;
                }
                MainActivity.this.startActivityForResult(new Intent("android.intent.action.PICK", Phone.CONTENT_URI), 1);
            }
        });
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.pd.setMessage("Waiting for invitee to confirm...");
                MainActivity.this.pd.show();
                String trim = editText.getText().toString().trim();
                if (trim.length() > 1) {
                    String str = MainActivity.this.fauth.getCurrentUser().getPhoneNumber().toString();
                    StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    sb.append("-");
                    sb.append(trim);
                    MainActivity.this.sharedPreferences.edit().putString("current_session", sb.toString()).commit();
                    MainActivity.this.sharedPreferences.edit().putString("current_status", "inviter").commit();
                    DatabaseReference databaseReference = MainActivity.this.dbr;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str);
                    sb2.append("-");
                    sb2.append(trim);
                    databaseReference.child(sb2.toString()).getRef().setValue(null);
                    DatabaseReference databaseReference2 = MainActivity.this.dbr;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str);
                    sb3.append("-");
                    sb3.append(trim);
                    databaseReference2.child(sb3.toString()).child("start").setValue(Integer.valueOf(1));
                }
            }
        });
    }

    public String getnationalnumber(String str) {
        try {
            PhoneNumberUtil pars = PhoneNumberUtil.getInstance();
            PhoneNumber parse = pars.parse(str, "");
            //parse.getCountryCode();
            long nationalNumber = parse.getNationalNumber();
            StringBuilder sb = new StringBuilder();
            sb.append(nationalNumber);
            sb.append("");
            return sb.toString();
        } catch (NumberParseException e) {
            PrintStream printStream = System.err;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("NumberParseException was thrown: ");
            sb2.append(e.toString());
            printStream.println(sb2.toString());
            StringBuilder sb3 = new StringBuilder();
            sb3.append(0);
            sb3.append("");
            return sb3.toString();
        }
    }

    private String getname(Context context, String str) {
        ContentResolver contentResolver = context.getContentResolver();
        String str2 = null;
        if (VERSION.SDK_INT < 23) {
            Cursor query = contentResolver.query(Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(str)), new String[]{"display_name"}, null, null, null);
            if (query == null) {
                return null;
            }
            if (query.moveToFirst()) {
                str2 = query.getString(query.getColumnIndex("display_name"));
            }
            if (query != null && query.isClosed()) {
                query.close();
            }
        } else if (ContextCompat.checkSelfPermission(this, "android.permission.READ_CONTACTS") != 0) {
            requestcontact();
        } else {
            Cursor query2 = contentResolver.query(Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(str)), new String[]{"display_name"}, null, null, null);
            if (query2 == null) {
                return null;
            }
            if (query2.moveToFirst()) {
                str2 = query2.getString(query2.getColumnIndex("display_name"));
            }
            if (query2 != null && query2.isClosed()) {
                query2.close();
            }
        }
        return str2;
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        if (i2 != -1) {
            Toast.makeText(this, "Failed to pick contact!", Toast.LENGTH_SHORT).show();
        } else if (i == 1 && i == 1) {
            try {
                this.dialog.dismiss();
                Cursor query = getContentResolver().query(intent.getData(), null, null, null, null);
                query.moveToFirst();
                this.sharedPreferences.edit().putString("assign_contact", query.getString(query.getColumnIndex("data1"))).commit();
                showAddUserDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint({"NewApi"})
    public void requestcontact() {
        requestPermissions(new String[]{"android.permission.READ_CONTACTS"}, 123);
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 123) {
            if (iArr[0] == 0) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Read Contact permission is required", Toast.LENGTH_SHORT).show();
            }
        }
        if (i == 321) {
            if (iArr[0] == 0) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Write storage permission is required", Toast.LENGTH_SHORT).show();
            }
        }
        if (i != 520) {
            return;
        }
        /*if (iArr[0] == 0) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            ReadPhoneStat_dialog();
        }*/
    }


    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            public void onAdFailedToLoad(int i) {
            }

            public void onAdLoaded() {
            }

            public void onAdClosed() {
                MainActivity.this.goToNextLevel();
            }
        });
        return interstitialAd;
    }

    /*private void showInterstitial() {
        if (this.mInterstitialAd == null || !this.mInterstitialAd.isLoaded()) {
            goToNextLevel();
        } else {
            this.mInterstitialAd.show();
        }
    }

    private void loadInterstitial() {
        this.mInterstitialAd.loadAd(new Builder().setRequestAgent("android_studio:ad_template").build());
    }*/

    public void goToNextLevel() {
        if (this.buttonclicked == "saved") {
            startActivity(new Intent(this, saved_drawings.class));
            Bungee.zoom(mcontext);
        } else if (this.buttonclicked == "adduser") {
            showAddUserDialog();
        }
        this.buttonclicked = "";
    }

    public void zoomoutbuttons() {
        this.btnpencil.setClickable(false);
        this.btneraser.setClickable(true);
        ArrayList mypaint = this.newPaintView.getMypaint();
        String valueOf = String.valueOf(((Paint) mypaint.get(mypaint.size() - 1)).getColor());
        if (valueOf.equals("-1")) {
            valueOf = String.valueOf(((Paint) mypaint.get(mypaint.size() - 2)).getColor());
        }
        String substring = valueOf.substring(1);

        StringBuilder sb = new StringBuilder();
        sb.append("img");
        sb.append(substring);
        int resid = getResources().getIdentifier(sb.toString(), "id", getPackageName());
        ImageView imageView = (ImageView) findViewById(resid);
        imageView.clearAnimation();
        imageView.startAnimation(this.zoomout);
        imageView.setClickable(true);

        StringBuilder sb2 = new StringBuilder();
        sb2.append("check");
        sb2.append(substring);
        int resid2 = getResources().getIdentifier(sb2.toString(), "id", getPackageName());
        ImageView imageView1 = (ImageView) findViewById(resid2);
        imageView1.setVisibility(View.INVISIBLE);

    }
}
