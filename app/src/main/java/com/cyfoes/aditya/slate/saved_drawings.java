package com.cyfoes.aditya.slate;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.ContactsContract.PhoneLookup;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import spencerstudios.com.bungeelib.Bungee;

public class saved_drawings extends AppCompatActivity{
    private String TAG = saved_drawings.class.getSimpleName();
    ArrayAdapter<String> adapter;
    FirebaseAuth fauth = FirebaseAuth.getInstance();
    ArrayList<String> list = new ArrayList<>();
    ListView listView;
    InterstitialAd mInterstitialAd;
    DatabaseHelper mydb;
    ChatDatabase chatdb;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_saved_drawings);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle((CharSequence) "Saved");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.listView = (ListView) findViewById(R.id.saved_list);
        this.mydb = new DatabaseHelper(this);
        chatdb = new ChatDatabase(this);
        setListviewData();
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                String str = (String) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(saved_drawings.this, MainActivity.class);
                intent.putExtra("saved", str);
                saved_drawings.this.startActivity(intent);
                Bungee.zoom(saved_drawings.this);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        finish();
        return true;
    }

    public void finish() {
        super.finish();
        Bungee.zoom(this);
    }

    private void setListviewData() {
        Cursor allData = this.mydb.getAllData();
        if (allData != null && allData.getCount() > 0) {
            while (allData.moveToNext()) {
                String string = allData.getString(1);
                if (!this.list.contains(string) && !string.equals("samjibhai")) {
                    this.list.add(string);
                }
            }
        }
        this.adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, this.list);
        this.listView.setAdapter(this.adapter);
        registerForContextMenu(this.listView);
    }

    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenuInfo contextMenuInfo) {
        super.onCreateContextMenu(contextMenu, view, contextMenuInfo);
        contextMenu.setHeaderTitle("Select the option");
        contextMenu.add(0, view.getId(), 0, "Delete");
        contextMenu.add(0, view.getId(), 0, "Details");
    }

    public boolean onContextItemSelected(MenuItem menuItem) {
        if (menuItem.getTitle() == "Delete") {
            AdapterContextMenuInfo adapterContextMenuInfo = (AdapterContextMenuInfo) menuItem.getMenuInfo();
            Cursor allData = this.mydb.getAllData();
            if (allData != null && allData.getCount() > 0) {
                while (allData.moveToNext()) {
                    String string = allData.getString(1);
                    String string2 = allData.getString(0);
                    if (string.equals(this.list.get(adapterContextMenuInfo.position))) {
                        this.mydb.deleteData(string2);
                        chatdb.deleteData(string);
                    }
                }
            }
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(getIntent());
            Bungee.zoom(this);
        } else if (menuItem.getTitle() == "Details") {
            AdapterContextMenuInfo adapterContextMenuInfo2 = (AdapterContextMenuInfo) menuItem.getMenuInfo();
            Cursor allData2 = this.mydb.getAllData();
            if (allData2 != null && allData2.getCount() > 0) {
                String str = "";
                String str2 = "";
                while (allData2.moveToNext()) {
                    String string3 = allData2.getString(5);
                    String string4 = allData2.getString(1);
                    allData2.getString(0);
                    if (string4.equals(this.list.get(adapterContextMenuInfo2.position))) {
                        str = allData2.getString(4);
                    }
                    if (string3.length() > str2.length()) {
                        str2 = string3;
                    }
                }
                showdetails(new SimpleDateFormat("dd/MMM/yyyy HH:mm").format(new Date(Long.parseLong(str))), str2);
            }
        }
        return true;
    }

    private void showdetails(String str, String str2) {
        String[] split;
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.saved_detail_dialog);
        dialog.show();
        TextView textView = (TextView) dialog.findViewById(R.id.txtdate);
        TextView textView2 = (TextView) dialog.findViewById(R.id.txtparicipants);
        String str3 = "";
        for (String str4 : str2.split("-")) {
            if (this.fauth.getCurrentUser() == null) {
                if (getname(this, str4) != null) {
                    if (!str3.equals("")) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(str3);
                        sb.append("\n");
                        sb.append(getname(this, str4));
                        str3 = sb.toString();
                    } else {
                        str3 = getname(this, str4);
                    }
                } else if (!str3.equals("")) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str3);
                    sb2.append("\n");
                    sb2.append(str4);
                    str3 = sb2.toString();
                }
            } else if (str4.equals(this.fauth.getCurrentUser().getPhoneNumber().toString()) || str4.equals(getnationalnumber(this.fauth.getCurrentUser().getPhoneNumber().toString()))) {
                if (!str3.equals("")) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str3);
                    sb3.append("\nYou");
                    str3 = sb3.toString();
                } else {
                    str3 = "You";
                }
            } else {
                if (getname(this, str4) != null) {
                    if (!str3.equals("")) {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(str3);
                        sb4.append("\n");
                        sb4.append(getname(this, str4));
                        str3 = sb4.toString();
                    } else {
                        str3 = getname(this, str4);
                    }
                } else if (!str3.equals("")) {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(str3);
                    sb5.append("\n");
                    sb5.append(str4);
                    str3 = sb5.toString();
                }
            }
            str3 = str4;
        }
        textView.setText(str);
        textView2.setText(str3);
    }

    private String getnationalnumber(String str) {
        try {
            PhoneNumber parse = PhoneNumberUtil.getInstance().parse(str, "");
            parse.getCountryCode();
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
        } else if (ContextCompat.checkSelfPermission(this, "android.permission.READ_CONTACTS") != PackageManager.PERMISSION_GRANTED) {
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

    @SuppressLint({"NewApi"})
    private void requestcontact() {
        requestPermissions(new String[]{"android.permission.READ_CONTACTS"}, 123);
    }

    public void onBackPressed() {
        super.onBackPressed();
        Bungee.zoom(this);
    }
}
