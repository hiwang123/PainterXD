package com.example.painterxd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.os.ParcelFileDescriptor.FileDescriptorDetachedException;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class MyView extends View implements OnTouchListener{

	private float preX,preY;
	private ArrayList<Pair<Path, Paint>> paths=new ArrayList<Pair<Path, Paint>>();
	private ArrayList<Pair<Path, Paint>> undopaths=new ArrayList<Pair<Path, Paint>>();
	private Path path;
	private Paint paint;
	private int CurrColor;
	private int CurrSize;
	private int BgColor;
	private Bitmap myBitmap;
	Canvas canvas;
	Context context;
	
	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFocusable(true);
		setFocusableInTouchMode(true);
		this.setOnTouchListener(this);
		context=this.context;
		
		paths.clear();
		undopaths.clear();
		
		CurrColor=Color.BLACK;
		BgColor=Color.WHITE;
		CurrSize=7;
		
		path=new Path();
		paint=new Paint();
		setPaintStyle();
		//myBitmap= Bitmap.createBitmap(, canvas.getHeight(), Bitmap.Config.ARGB_8888);
		canvas=new Canvas();
		//myBitmap= Bitmap.createBitmap( 100, 100, Bitmap.Config.ARGB_8888);
		//canvas.setBitmap(myBitmap);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(BgColor);
		for(Pair<Path, Paint> p: paths){
			canvas.drawPath(p.first, p.second);
		}
		canvas.drawPath(path, paint);
	}

	public void touch_start(float x,float y){
		paint=new Paint();
		setPaintStyle();
		path.reset();
		path.moveTo(x, y);
		path.lineTo(x+1, y);
		preX=x;
		preY=y;
		
		if(undopaths.size()>0){
			undopaths.clear();
		}
	}
	
	public void touch_move(float x,float y){
		path.quadTo(preX,preY,(preX+x)/2, (preY+y)/2);
		preX=x;
		preY=y;
	}

	public void touch_up(float x,float y){
		path.lineTo(x, y);
		paths.add(new Pair<Path, Paint>(path, paint));
		path=new Path();
	}
	
	public void undo(){
		if(paths.size()>0){
			undopaths.add(paths.remove(paths.size() - 1));
			invalidate();
		}
	}
	
	public void redo(){
		if(undopaths.size()>0){
			paths.add(undopaths.remove(undopaths.size() - 1));
			invalidate();
		}
	}
	
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		float x=event.getX();
		float y=event.getY();
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			touch_start(x,y);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			touch_move(x,y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			touch_up(x,y);
			invalidate();
			break;
		}
		return true;
	}
	

	public void setColor(int color){
		CurrColor=color;
	}
	
	public void setSize(int size){
		CurrSize=size;
	}
	
	public void setBGColor(int color){
		BgColor=color;
		invalidate();
	}
	public void clearAll(){
		paths.clear();
		undopaths.clear();
		
		CurrColor=Color.BLACK;
		BgColor=Color.WHITE;
		CurrSize=7;

		invalidate();
	}

	public void setPaintStyle(){
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setColor(CurrColor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(CurrSize);
	}
	
	public void saveCanvas(){
		String name="jj.png";
		String sdPath=Environment.getExternalStorageDirectory().getAbsolutePath();
		try {
			//BitmapDrawable bitmapDrawable=canvas.
			FileOutputStream stream=new FileOutputStream(sdPath+"/"+name);
			myBitmap=this.getDrawingCache();
			myBitmap.compress(CompressFormat.PNG, 100, stream);
			stream.close();
			//Toast.makeText(context, "save "+filename, Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Log.d("System.out","not open");
			e.printStackTrace();
			//Toast.makeText(context, "xxx ", Toast.LENGTH_SHORT).show();
		}
		
	}

	

}
