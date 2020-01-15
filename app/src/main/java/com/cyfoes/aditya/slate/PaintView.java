package com.cyfoes.aditya.slate;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.MediaPlayer;
import android.os.Handler;
import androidx.core.view.ViewCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Iterator;

public class PaintView extends View {

    private Paint brush = new Paint();
    private Paint brusho = new Paint();
    Button btnclear;
    Paint currentbrush;
    Path currentpath;
    Boolean drawcircle = Boolean.valueOf(false);
    Boolean eraser = Boolean.valueOf(false);
    Handler handler;
    Bitmap mBitmap;
    Canvas mcanvas;
    MediaPlayer mpdraw;
    ArrayList<Paint> mybrush = new ArrayList<>();
    ArrayList<Paint> mybrusho = new ArrayList<>();
    DatabaseHelper mydb = new DatabaseHelper(getContext());
    ArrayList<Path> mypath = new ArrayList<>();
    ArrayList<Path> mypatho = new ArrayList<>();
    Boolean offline = Boolean.valueOf(false);
    Boolean online = Boolean.valueOf(false);
    public ActionBar.LayoutParams param;
    private Path path = new Path();
    private Path patho = new Path();
    Boolean pendown = Boolean.valueOf(true);
    private float pointcx;
    private float pointcy;
    private float pointx;
    private float pointxprev = 0.0f;
    private float pointxprev1 = 0.0f;
    private float pointy;
    private float pointyprev = 0.0f;
    private float pointyprev1 = 0.0f;

    /* renamed from: px */
    float px;

    /* renamed from: py */
    float py;
    Runnable runnable;
    SharedPreferences sharedPreferences;

    public PaintView(Context context) {
        super(context);
        this.brush.setAntiAlias(true);
        this.brush.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.brush.setStyle(Paint.Style.STROKE);
        this.brush.setStrokeJoin(Paint.Join.ROUND);
        this.brush.setStrokeCap(Paint.Cap.ROUND);
        this.brush.setDither(true);
        this.brush.setStrokeWidth(4.0f);
        this.mypath.add(this.path);
        this.mybrush.add(this.brush);
        this.brusho.setAntiAlias(true);
        this.brusho.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.brusho.setStyle(Paint.Style.STROKE);
        this.brusho.setStrokeJoin(Paint.Join.ROUND);
        this.brusho.setStrokeCap(Paint.Cap.ROUND);
        this.brusho.setDither(true);
        this.brusho.setStrokeWidth(4.0f);
        this.mypatho.add(this.patho);
        this.mybrusho.add(this.brusho);
        this.sharedPreferences = getContext().getSharedPreferences("penactive", 0);
        Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), R.drawable.empty);
        this.mBitmap = Bitmap.createBitmap(decodeResource.getWidth(), decodeResource.getHeight(), Bitmap.Config.ARGB_8888);
        this.mcanvas = new Canvas();
        this.mcanvas.setBitmap(this.mBitmap);
        this.mcanvas.drawBitmap(decodeResource, 0.0f, 0.0f, null);
    }

    public float getPointx() {
        return this.pointx;
    }

    public void setPointx(float f) {
        this.pointx = f;
        this.px = f;
    }

    public float getPointy() {
        return this.pointy;
    }

    public void setPointy(float f) {
        this.pointy = f;
        this.py = f;
    }

    public Boolean getPendown() {
        return this.pendown;
    }

    public void setPendown(Boolean bool) {
        this.pendown = bool;
    }

    public Boolean getOnline() {
        return this.online;
    }

    public void setOnline(Boolean bool) {
        this.online = bool;
    }

    public boolean customdraw(MotionEvent motionEvent) {
        this.pointcx = motionEvent.getX();
        this.pointcy = motionEvent.getY();
        int action = motionEvent.getAction();
        if (action == 0) {
            ((Path) this.mypatho.get(this.mypatho.size() - 1)).moveTo(this.pointcx, this.pointcy);
            return true;
        } else if (action != 2) {
            return false;
        } else {
            this.online = Boolean.valueOf(true);
            ((Path) this.mypatho.get(this.mypatho.size() - 1)).lineTo(this.pointcx, this.pointcy);
            invalidate();
            return true;
        }
    }

    public boolean saveddraw(MotionEvent motionEvent) {
        this.pointcx = motionEvent.getX();
        this.pointcy = motionEvent.getY();
        int action = motionEvent.getAction();
        if (action == 0) {
            ((Path) this.mypatho.get(this.mypatho.size() - 1)).moveTo(this.pointcx, this.pointcy);
            return true;
        } else if (action != 2) {
            return false;
        } else {
            ((Path) this.mypatho.get(this.mypatho.size() - 1)).lineTo(this.pointcx, this.pointcy);
            invalidate();
            return true;
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.pointx = motionEvent.getX();
        this.pointy = motionEvent.getY();
        switch (motionEvent.getAction()) {
            case 0:
                ((Path) this.mypath.get(this.mypath.size() - 1)).moveTo(this.pointx, this.pointy);
                this.drawcircle = Boolean.valueOf(true);
                this.pendown = Boolean.valueOf(true);
                return true;
            case 1:
                this.drawcircle = Boolean.valueOf(false);
                this.pendown = Boolean.valueOf(false);
                createnewpath();
                return true;
            case 2:
                this.offline = Boolean.valueOf(true);
                ((Path) this.mypath.get(this.mypath.size() - 1)).lineTo(this.pointx, this.pointy);
                this.pendown = Boolean.valueOf(true);
                invalidate();
                return false;
            default:
                return false;
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.online.booleanValue()) {
            this.mcanvas.drawPath((Path) this.mypatho.get(this.mypatho.size() - 1), (Paint) this.mybrusho.get(this.mybrusho.size() - 1));
            this.online = Boolean.valueOf(false);
        } else if (this.offline.booleanValue()) {
            this.mcanvas.drawPath((Path) this.mypath.get(this.mypath.size() - 1), (Paint) this.mybrush.get(this.mybrush.size() - 1));
            this.offline = Boolean.valueOf(false);
        } else {
            Iterator it = this.mypatho.iterator();
            while (it.hasNext()) {
                Path path2 = (Path) it.next();
                this.mcanvas.drawPath(path2, (Paint) this.mybrusho.get(this.mypatho.indexOf(path2)));
            }
        }
        canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, null);
        super.onDraw(canvas);
    }

    public void clearscreen() {
        Iterator it = this.mypath.iterator();
        while (it.hasNext()) {
            ((Path) it.next()).reset();
            invalidate();
            this.mcanvas.drawColor(-1);
        }
        Iterator it2 = this.mypatho.iterator();
        while (it2.hasNext()) {
            ((Path) it2.next()).reset();
            invalidate();
            this.mcanvas.drawColor(-1);
        }
    }

    private void createnewpath() {
        this.mypath.add(new Path());
    }

    public void newcanvas() {
        Bitmap decodeResource = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.empty);
        this.mBitmap = Bitmap.createBitmap(decodeResource.getWidth(), decodeResource.getHeight(), Bitmap.Config.ARGB_8888);
        this.mcanvas = new Canvas();
        this.mcanvas.setBitmap(this.mBitmap);
        this.mcanvas.drawBitmap(decodeResource, 0.0f, 0.0f, null);
    }

    public void seterasertest() {
        this.mypath.add(new Path());
        Paint paint = new Paint();
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        paint.setStrokeWidth((float) this.sharedPreferences.getInt("linewidth", 4));
        this.mybrush.add(paint);
    }

    public void changecolor(String str) {
        this.mypath.add(new Path());
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor(str));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setDither(true);
        paint.setStrokeWidth((float) this.sharedPreferences.getInt("linewidth", 4));
        this.sharedPreferences.edit().putString("currentcolor", str).commit();
        this.mybrush.add(paint);
    }

    public void changewidth(int i) {
        this.mypath.add(new Path());
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        if (this.sharedPreferences.getBoolean("eraser", false)) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            paint.setAlpha(0);
        } else {
            paint.setColor(Color.parseColor(this.sharedPreferences.getString("currentcolor", "black")));
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth((float) i);
        this.mybrush.add(paint);
    }

    public void changecoloro(String str, int i) {
        this.mypatho.add(new Path());
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor(str));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setDither(true);
        paint.setStrokeWidth((float) i);
        this.sharedPreferences.edit().putString("currentcoloro", str).commit();
        this.mybrusho.add(paint);
    }

    public void seterasertest0(int i) {
        this.mypatho.add(new Path());
        Paint paint = new Paint();
        paint.setAlpha(0);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        paint.setStrokeWidth((float) i);
        this.mybrusho.add(paint);
    }

    public double getAngle() {
        if (this.pointxprev == 0.0f || this.pointxprev1 == 0.0f) {
            return 0.0d;
        }
        float f = this.pointx - this.pointxprev;
        float f2 = this.pointy - this.pointyprev;
        float sqrt = (float) Math.sqrt((double) ((f * f) + (f2 * f2)));
        float f3 = f / sqrt;
        float f4 = f2 / sqrt;
        float f5 = this.pointxprev - this.pointxprev1;
        float f6 = this.pointyprev - this.pointyprev1;
        float sqrt2 = (float) Math.sqrt((double) ((f5 * f5) + (f6 * f6)));
        return Math.toDegrees(Math.acos((double) ((f3 * (f5 / sqrt2)) + (f4 * (f6 / sqrt2)))));
    }


}
