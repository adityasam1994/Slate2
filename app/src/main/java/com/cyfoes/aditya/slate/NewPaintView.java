package com.cyfoes.aditya.slate;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Iterator;

public class NewPaintView extends View {
    DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("all");
    Boolean eraser = Boolean.valueOf(false);
    Bitmap mBitmap;
    Canvas mcanvas;
    DatabaseHelper mydb = new DatabaseHelper(getContext());
    ArrayList<Paint> mypaint = new ArrayList<>();
    ArrayList<Paint> mypainto = new ArrayList<>();
    ArrayList<Paint> mypaints = new ArrayList<>();
    ArrayList<Path> mypath = new ArrayList<>();
    ArrayList<Path> mypatho = new ArrayList<>();
    ArrayList<Path> mypaths = new ArrayList<>();
    Boolean offline = Boolean.valueOf(false);
    Boolean online = Boolean.valueOf(false);

    /* renamed from: pX */
    int pX;

    /* renamed from: pY */
    int pY;
    Paint paint;
    Paint painto;
    Paint paints;
    Path path = new Path();
    Path patho;
    Path paths;
    Boolean pendown = Boolean.valueOf(false);
    ImageView pointer;
    Boolean pointeron = Boolean.valueOf(false);
    String points = "";
    float pointx;
    float pointy;
    Boolean saved = Boolean.valueOf(false);
    SharedPreferences sharedPreferences;
    int undocount = 0;
    Boolean undraw = Boolean.valueOf(false);

    public NewPaintView(Context context) {
        super(context);
        this.mypath.add(this.path);
        this.patho = new Path();
        this.mypatho.add(this.patho);
        this.paths = new Path();
        this.mypaths.add(this.paths);
        this.paint = new Paint();
        this.painto = new Paint();
        this.paints = new Paint();
        this.paint.setColor(Color.parseColor("black"));
        this.paint.setAntiAlias(true);
        this.paint.setStrokeWidth(10.0f);
        this.paint.setStrokeCap(Cap.ROUND);
        this.paint.setStrokeJoin(Join.ROUND);
        this.paint.setStyle(Style.STROKE);
        this.mypaint.add(this.paint);
        this.painto.setColor(Color.parseColor("black"));
        this.painto.setAntiAlias(true);
        this.painto.setStrokeWidth(10.0f);
        this.painto.setStrokeCap(Cap.ROUND);
        this.painto.setStrokeJoin(Join.ROUND);
        this.painto.setStyle(Style.STROKE);
        this.mypainto.add(this.painto);
        this.paints.setColor(Color.parseColor("black"));
        this.paints.setAntiAlias(true);
        this.paints.setStrokeWidth(10.0f);
        this.paints.setStrokeCap(Cap.ROUND);
        this.paints.setStrokeJoin(Join.ROUND);
        this.paints.setStyle(Style.STROKE);
        this.mypaints.add(this.paints);
        this.sharedPreferences = getContext().getSharedPreferences("penactive", 0);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
        this.mcanvas = new Canvas(this.mBitmap);
    }

    public boolean undoDraw(MotionEvent motionEvent) {
        this.pointx = motionEvent.getX();
        this.pointy = motionEvent.getY();
        int action = motionEvent.getAction();
        if (action == 0) {
            ((Path) this.mypaths.get(this.mypaths.size() - 1)).moveTo(this.pointx, this.pointy);
            return true;
        } else if (action != 2) {
            return false;
        } else {
            this.undraw = Boolean.valueOf(true);
            ((Path) this.mypaths.get(this.mypaths.size() - 1)).lineTo(this.pointx, this.pointy);
            invalidate();
            return true;
        }
    }

    public boolean saveddraw(MotionEvent motionEvent) {
        this.pointx = motionEvent.getX();
        this.pointy = motionEvent.getY();
        int action = motionEvent.getAction();
        if (action == 0) {
            ((Path) this.mypaths.get(this.mypaths.size() - 1)).moveTo(this.pointx, this.pointy);
            return true;
        } else if (action != 2) {
            return false;
        } else {
            this.saved = Boolean.valueOf(true);
            ((Path) this.mypaths.get(this.mypaths.size() - 1)).lineTo(this.pointx, this.pointy);
            invalidate();
            return true;
        }
    }

    public boolean customdraw(MotionEvent motionEvent) {
        this.pointx = motionEvent.getX();
        this.pointy = motionEvent.getY();
        int action = motionEvent.getAction();
        if (action == 0) {
            ((Path) this.mypatho.get(this.mypatho.size() - 1)).moveTo(this.pointx, this.pointy);
            return true;
        } else if (action != 2) {
            return false;
        } else {
            this.online = Boolean.valueOf(true);
            ((Path) this.mypatho.get(this.mypatho.size() - 1)).lineTo(this.pointx, this.pointy);
            invalidate();
            return true;
        }
    }

    public int getpX() {
        return this.pX;
    }

    public void setpX(int i) {
        this.pX = i;
    }

    public int getpY() {
        return this.pY;
    }

    public void setpY(int i) {
        this.pY = i;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.pointx = motionEvent.getX();
        this.pointy = motionEvent.getY();
        this.pX = (int) motionEvent.getRawX();
        this.pY = (int) motionEvent.getRawY();
        if (this.points.equals("")) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.pointx);
            sb.append(",");
            sb.append(this.pointy);
            this.points = sb.toString();
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(this.points);
            sb2.append("/");
            sb2.append(this.pointx);
            sb2.append(",");
            sb2.append(this.pointy);
            this.points = sb2.toString();
        }
        String string = this.sharedPreferences.getString("current_session", "");
        long currentTimeMillis = System.currentTimeMillis();
        String valueOf = String.valueOf(((Paint) this.mypaint.get(this.mypaint.size() - 1)).getColor());
        String valueOf2 = String.valueOf(((Paint) this.mypaint.get(this.mypaint.size() - 1)).getStrokeWidth());
        if (this.pointeron.booleanValue()) {
            switch (motionEvent.getAction()) {
                case 0:
                    this.pendown = Boolean.valueOf(true);
                    break;
                case 1:
                    this.pendown = Boolean.valueOf(false);
                    break;
            }
        } else {
            switch (motionEvent.getAction()) {
                case 0:
                    ((Path) this.mypath.get(this.mypath.size() - 1)).moveTo(this.pointx, this.pointy);
                    break;
                case 1:
                    if (string.equals("") || !this.sharedPreferences.getBoolean("startsession", false)) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(this.points);
                        sb3.append("/");
                        sb3.append(valueOf);
                        sb3.append("/");
                        sb3.append(valueOf2);
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(currentTimeMillis);
                        sb4.append("");
                        Boolean.valueOf(this.mydb.insertData("samjibhai", sb3.toString(), "1/1", sb4.toString(), "You"));
                        this.points = "";
                    } else {
                        setDatabase();
                    }
                    createnewpath();
                    break;
                case 2:
                    this.offline = Boolean.valueOf(true);
                    ((Path) this.mypath.get(this.mypath.size() - 1)).lineTo(this.pointx, this.pointy);
                    break;
                default:
                    return true;
            }
        }
        invalidate();
        return true;
    }

    public Boolean getPendown() {
        return this.pendown;
    }

    public void setPendown(Boolean bool) {
        this.pendown = bool;
    }

    public Boolean getPointeron() {
        return this.pointeron;
    }

    public void setPointeron(Boolean bool) {
        this.pointeron = bool;
    }

    private void setDatabase() {
        long currentTimeMillis = System.currentTimeMillis();
        String valueOf = String.valueOf(((Paint) this.mypaint.get(this.mypaint.size() - 1)).getColor());
        String valueOf2 = String.valueOf(((Paint) this.mypaint.get(this.mypaint.size() - 1)).getStrokeWidth());
        String string = this.sharedPreferences.getString("current_session", "");
        int width = getWidth();
        int height = getHeight();
        StringBuilder sb = new StringBuilder();
        sb.append(this.points);
        sb.append("/");
        sb.append(valueOf);
        sb.append("/");
        sb.append(valueOf2);
        StringBuilder sb2 = new StringBuilder();
        sb2.append(currentTimeMillis);
        sb2.append("");
        Boolean.valueOf(this.mydb.insertData("samjibhai", sb.toString(), "1/1", sb2.toString(), string));
        DatabaseReference child = this.dbr.child(this.sharedPreferences.getString("current_session", "")).child("000res");
        StringBuilder sb3 = new StringBuilder();
        sb3.append(width);
        sb3.append("/");
        sb3.append(height);
        child.setValue(sb3.toString());
        if (this.sharedPreferences.getString("current_status", "").equals("inviter")) {
            DatabaseReference child2 = this.dbr.child(this.sharedPreferences.getString("current_session", "")).child("tro");
            StringBuilder sb4 = new StringBuilder();
            sb4.append(currentTimeMillis);
            sb4.append("");
            DatabaseReference child3 = child2.child(sb4.toString());
            StringBuilder sb5 = new StringBuilder();
            sb5.append(this.points);
            sb5.append("/");
            sb5.append(valueOf);
            sb5.append("/");
            sb5.append(valueOf2);
            child3.setValue(sb5.toString());
            this.points = "";
            return;
        }
        DatabaseReference child4 = this.dbr.child(this.sharedPreferences.getString("current_session", "")).child("fro");
        StringBuilder sb6 = new StringBuilder();
        sb6.append(currentTimeMillis);
        sb6.append("");
        DatabaseReference child5 = child4.child(sb6.toString());
        StringBuilder sb7 = new StringBuilder();
        sb7.append(this.points);
        sb7.append("/");
        sb7.append(valueOf);
        sb7.append("/");
        sb7.append(valueOf2);
        child5.setValue(sb7.toString());
        this.points = "";
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, this.paint);
        if (this.offline.booleanValue()) {
            this.mcanvas.drawPath((Path) this.mypath.get(this.mypath.size() - 1), (Paint) this.mypaint.get(this.mypaint.size() - 1));
            this.offline = Boolean.valueOf(false);
        } else if (this.online.booleanValue()) {
            this.mcanvas.drawPath((Path) this.mypatho.get(this.mypatho.size() - 1), (Paint) this.mypainto.get(this.mypainto.size() - 1));
            this.online = Boolean.valueOf(false);
        } else if (this.undraw.booleanValue()) {
            this.mcanvas.drawPath((Path) this.mypaths.get(this.mypaths.size() - 1), (Paint) this.mypaints.get(this.mypaints.size() - 1));
            this.undraw = Boolean.valueOf(false);
        } else if (this.saved.booleanValue()) {
            Iterator it = this.mypaths.iterator();
            while (it.hasNext()) {
                Path path2 = (Path) it.next();
                this.mcanvas.drawPath(path2, (Paint) this.mypaints.get(this.mypaths.indexOf(path2)));
            }
            this.saved = Boolean.valueOf(false);
        }
        super.onDraw(canvas);
    }

    private void createnewpath() {
        this.mypath.add(new Path());
    }

    public void changecolor(String str) {
        this.mypath.add(new Path());
        Paint paint2 = new Paint();
        paint2.setColor(Color.parseColor(str));
        paint2.setAntiAlias(true);
        if(mypaint.size() > 0) {
            paint2.setStrokeWidth(((Paint) this.mypaint.get(this.mypaint.size() - 1)).getStrokeWidth());
        }
        paint2.setStrokeCap(Cap.ROUND);
        paint2.setStrokeJoin(Join.ROUND);
        paint2.setStyle(Style.STROKE);
        this.mypaint.add(paint2);
    }

    public void restorecolor() {
        this.mypath.add(new Path());
        Paint paint2 = new Paint();
        if(mypaint.size() > 1) {
            paint2.setColor(((Paint) this.mypaint.get(this.mypaint.size() - 2)).getColor());
        }
        paint2.setAntiAlias(true);
        if(mypaint.size() > 0) {
            paint2.setStrokeWidth(((Paint) this.mypaint.get(this.mypaint.size() - 1)).getStrokeWidth());
        }
        paint2.setStrokeCap(Cap.ROUND);
        paint2.setStrokeJoin(Join.ROUND);
        paint2.setStyle(Style.STROKE);
        this.mypaint.add(paint2);
    }

    public void changecoloro(String str, float f) {
        this.mypatho.add(new Path());
        Paint paint2 = new Paint();
        paint2.setColor(Color.parseColor(str));
        paint2.setAntiAlias(true);
        paint2.setStrokeWidth(f);
        paint2.setStrokeCap(Cap.ROUND);
        paint2.setStrokeJoin(Join.ROUND);
        paint2.setStyle(Style.STROKE);
        this.mypainto.add(paint2);
    }

    public void changecolors(String str, float f) {
        this.mypaths.add(new Path());
        Paint paint2 = new Paint();
        paint2.setColor(Color.parseColor(str));
        paint2.setAntiAlias(true);
        paint2.setStrokeWidth(f);
        paint2.setStrokeCap(Cap.ROUND);
        paint2.setStrokeJoin(Join.ROUND);
        paint2.setStyle(Style.STROKE);
        this.mypaints.add(paint2);
    }

    public void changewidth(int i) {
        this.mypath.add(new Path());
        Paint paint2 = new Paint();
        paint2.setColor(((Paint) this.mypaint.get(this.mypaint.size() - 1)).getColor());
        paint2.setAntiAlias(true);
        paint2.setStrokeWidth((float) i);
        paint2.setStrokeCap(Cap.ROUND);
        paint2.setStrokeJoin(Join.ROUND);
        paint2.setStyle(Style.STROKE);
        this.mypaint.add(paint2);
    }

    public ArrayList<Paint> getMypaint() {
        return this.mypaint;
    }

    public void setMypaint(ArrayList<Paint> arrayList) {
        this.mypaint = arrayList;
    }

    public void clearCanvas() {
        Iterator it = this.mypath.iterator();
        while (it.hasNext()) {
            ((Path) it.next()).reset();
        }
        Iterator it2 = this.mypatho.iterator();
        while (it2.hasNext()) {
            ((Path) it2.next()).reset();
        }
        this.mcanvas.drawColor(-1);
        invalidate();
        clearsavedpath();
        clearonlinedpath();
    }

    public int getUndocount() {
        return this.undocount;
    }

    public void setUndocount(int i) {
        this.undocount = i;
    }

    private void clearsavedpath() {
        this.mypaths = new ArrayList<>();
        this.mypaints = new ArrayList<>();
    }

    private void clearonlinedpath() {
        this.mypatho = new ArrayList<>();
        this.mypainto = new ArrayList<>();
    }

    public void undoOnline(String str) {
        clearCanvas();
        Cursor allData = this.mydb.getAllData();
        if (allData != null && allData.getCount() > 0) {
            while (allData.moveToNext()) {
                String string = allData.getString(1);
                String string2 = allData.getString(4);
                if (!string.equals("samjibhai")) {
                    String str2 = str;
                } else if (!string2.equals(str)) {
                    String string3 = allData.getString(2);
                    String string4 = allData.getString(3);
                    String[] split = string3.split("/");
                    String str3 = split[split.length - 2];
                    String str4 = split[split.length - 1];
                    float parseFloat = Float.parseFloat(string4.split("/")[0]);
                    float parseFloat2 = Float.parseFloat(string4.split("/")[1]);
                    if (str3.equals("-16777216")) {
                        changecolors("black", Float.parseFloat(str4));
                    }
                    if (str3.equals("-16776961")) {
                        changecolors("blue", Float.parseFloat(str4));
                    }
                    if (str3.equals("-65536")) {
                        changecolors("red", Float.parseFloat(str4));
                    }
                    if (str3.equals("-256")) {
                        changecolors("yellow", Float.parseFloat(str4));
                    }
                    if (str3.equals("-65281")) {
                        changecolors("magenta", Float.parseFloat(str4));
                    }
                    if (str3.equals("-16711936")) {
                        changecolors("green", Float.parseFloat(str4));
                    }
                    if (str3.equals("-1")) {
                        changecolors("white", Float.parseFloat(str4));
                    }
                    int i = 0;
                    int i2 = 0;
                    for (String str5 : split) {
                        i++;
                        i2++;
                        if (i2 < split.length - 2) {
                            String[] split2 = str5.split(",");
                            float parseFloat3 = Float.parseFloat(split2[0]) * parseFloat;
                            float parseFloat4 = Float.parseFloat(split2[1]) * parseFloat2;
                            if (i == 1) {
                                saveddraw(MotionEvent.obtain(0, 0, 0, parseFloat3, parseFloat4, 0.5f, 5.0f, 0, 1.0f, 1.0f, 0, 0));
                            } else {
                                saveddraw(MotionEvent.obtain(0, 0, 2, parseFloat3, parseFloat4, 0.5f, 5.0f, 0, 1.0f, 1.0f, 0, 0));
                            }
                        }
                    }
                } else {
                    this.mydb.deleteData(allData.getString(0));
                }
            }
        }
    }

    public void redraw() {
        clearCanvas();
        Cursor allData = this.mydb.getAllData();
        if (allData != null && allData.getCount() > 0) {
            int i = 0;
            while (allData.moveToNext()) {
                i++;
                if (allData.getString(1).equals("samjibhai")) {
                    if (i < allData.getCount()) {
                        String string = allData.getString(2);
                        String string2 = allData.getString(3);
                        String[] split = string.split("/");
                        String str = split[split.length - 2];
                        String str2 = split[split.length - 1];
                        float parseFloat = Float.parseFloat(string2.split("/")[0]);
                        float parseFloat2 = Float.parseFloat(string2.split("/")[1]);
                        if (str.equals("-16777216")) {
                            changecolors("black", Float.parseFloat(str2));
                        }
                        if (str.equals("-16776961")) {
                            changecolors("blue", Float.parseFloat(str2));
                        }
                        if (str.equals("-65536")) {
                            changecolors("red", Float.parseFloat(str2));
                        }
                        if (str.equals("-256")) {
                            changecolors("yellow", Float.parseFloat(str2));
                        }
                        if (str.equals("-65281")) {
                            changecolors("magenta", Float.parseFloat(str2));
                        }
                        if (str.equals("-16711936")) {
                            changecolors("green", Float.parseFloat(str2));
                        }
                        if (str.equals("-1")) {
                            changecolors("white", Float.parseFloat(str2));
                        }
                        int i2 = 0;
                        int i3 = 0;
                        for (String str3 : split) {
                            i2++;
                            i3++;
                            if (i3 < split.length - 2) {
                                String[] split2 = str3.split(",");
                                float parseFloat3 = Float.parseFloat(split2[0]) * parseFloat;
                                float parseFloat4 = Float.parseFloat(split2[1]) * parseFloat2;
                                if (i2 == 1) {
                                    saveddraw(MotionEvent.obtain(0, 0, 0, parseFloat3, parseFloat4, 0.5f, 5.0f, 0, 1.0f, 1.0f, 0, 0));
                                } else {
                                    saveddraw(MotionEvent.obtain(0, 0, 2, parseFloat3, parseFloat4, 0.5f, 5.0f, 0, 1.0f, 1.0f, 0, 0));
                                }
                            }
                        }
                    } else {
                        String string3 = allData.getString(0);
                        String string4 = allData.getString(4);
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            String str4 = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().toString();
                            if (!this.sharedPreferences.getString("current_session", "").equals("")) {
                                this.dbr.child(this.sharedPreferences.getString("current_session", "")).child("undo").child(string4).setValue(str4);
                            }
                        }
                        this.mydb.deleteData(string3);
                    }
                }
            }
        }
        this.undocount++;
    }
}
