package com.example.painterxd;


import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.os.Bundle;
import android.os.Environment;
import android.animation.AnimatorSet.Builder;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.GesturePoint;
import android.gesture.GestureStroke;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Paint.Cap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	//Button color1,color2,color3,color4;
	LinearLayout colorBar,colorBar2;
	Button colorBtn,colorBtn2,newBtn,openBtn,saveBtn,sendBtn;
	List<String> rgbs;
	Context context;
	MyView myView;
	View view;
	SeekBar sizeBar;
	ImageView set,eraser,undo,redo,bgColor;
	EditText sendTo;
	
	Display display;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context=this;
		
		set=(ImageView) findViewById(R.id.imageView1);
		eraser=(ImageView) findViewById(R.id.imageView2);
		undo=(ImageView) findViewById(R.id.imageView3);
		redo=(ImageView) findViewById(R.id.imageView4);
		colorBar=(LinearLayout) findViewById(R.id.colorLayout);
		sizeBar=(SeekBar) findViewById(R.id.seekBar1);
		myView=(MyView) findViewById(R.id.myView1);
		
		rgbs=Arrays.asList(getResources().getStringArray(R.array.rgb_array));
		display=getWindowManager().getDefaultDisplay();
		
		ColorIni(); 
		set.setOnClickListener(new MyOnClickListener());
		eraser.setOnClickListener(new MyOnClickListener());
		undo.setOnClickListener(new MyOnClickListener());
		redo.setOnClickListener(new MyOnClickListener());
		sizeBar.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
		sizeBar.setProgress(25);
		
	}
	
	public void ColorIni(){
		for(String rgb: rgbs){
			colorBtn=new Button(this);
			colorBtn.setBackgroundColor((int)Long.parseLong(rgb, 16));
			colorBtn.setLayoutParams(new LinearLayout.LayoutParams(55, 55));  //調整顏色大小
			colorBtn.setOnClickListener(new MyOnClickListener());
			colorBar.addView(colorBtn);    
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public class MyOnClickListener implements OnClickListener{

		@Override
		public void onClick(View view) {
			//Toast.makeText(context, "ll", Toast.LENGTH_SHORT).show();
			switch(view.getId()){
			case R.id.imageView1:
				SetDialog builder = new SetDialog(MainActivity.this); 
				AlertDialog dialog=builder.create();
				dialog.show();
				break;
			case R.id.imageView2:
				myView.setColor(Color.WHITE);
				break;
			case R.id.imageView3:
				myView.undo();
				break;
			case R.id.imageView4:
				myView.redo();
				break;
			default:
				ColorDrawable drawable=  (ColorDrawable) view.getBackground();
				int color=drawable.getColor();
				myView.setColor(color);	
			}
		}
		
	}   
	
	public class MyOnSeekBarChangeListener implements OnSeekBarChangeListener{

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekbar) {
			int seekProgress = seekbar.getProgress();  
	        if(seekProgress<13){  
	            sizeBar.setProgress(0);  
	            myView.setSize(4);
	        }else if(seekProgress>=13 && seekProgress<38){  
	        	sizeBar.setProgress(25);  
	        	myView.setSize(7);
	        }else if(seekProgress>=38 && seekProgress<63){  
	        	sizeBar.setProgress(50); 
	        	myView.setSize(12);
	        }else if(seekProgress>=63 && seekProgress<88){  
	        	sizeBar.setProgress(75);  
	        	myView.setSize(17);
	        }else if(seekProgress>=88){  
	        	sizeBar.setProgress(100);  
	        	myView.setSize(22);
	        }
		}
		
	}
	
	public class SetDialog extends AlertDialog.Builder {

		public SetDialog(Context context) {
			super(context);
			LayoutInflater factory=LayoutInflater.from(MainActivity.this);
			final View view=factory.inflate(R.layout.setdialog,null);
			view.setMinimumHeight((int) (display.getHeight()*0.7));
			view.setMinimumWidth((int) (display.getWidth()));
			
			colorBar2=(LinearLayout) view.findViewById(R.id.colorLayout2);
			newBtn=(Button) view.findViewById(R.id.button_n);
			saveBtn=(Button) view.findViewById(R.id.button_s);
			openBtn=(Button) view.findViewById(R.id.button_o);
			sendBtn=(Button) view.findViewById(R.id.button_t);
			sendTo=(EditText) view.findViewById(R.id.editText1);  
			bgColor=(ImageView) view.findViewById(R.id.imageViewbg);
			
			ColorIni2(context); 
			view.setMinimumHeight((int) (display.getHeight()*0.7));
			view.setMinimumWidth((int) (display.getWidth()));
			this.setView(view);
			
			newBtn.setOnClickListener(new MyOnClickListenerF());
			saveBtn.setOnClickListener(new MyOnClickListenerF());
			openBtn.setOnClickListener(new MyOnClickListenerF());
			sendBtn.setOnClickListener(new MyOnClickListenerF());
			sendTo.setOnClickListener(new MyOnClickListenerF());
		}
		
		public void ColorIni2(Context context){
			for(String rgb: rgbs){
				colorBtn=new Button(context);
				colorBtn.setBackgroundColor((int)Long.parseLong(rgb, 16));
				colorBtn.setLayoutParams(new LinearLayout.LayoutParams(55, 55));  //調整顏色大小
				colorBtn.setOnClickListener(new MyOnClickListenerF());
				colorBar2.addView(colorBtn);    
			}
		}
		
		public class MyOnClickListenerF implements OnClickListener{

			@Override
			public void onClick(View view) {
				switch(view.getId()){
				case R.id.button_n:
					myView.clearAll();
					bgColor.setBackgroundColor(Color.WHITE);
					sizeBar.setProgress(25);
					break;
				case R.id.button_s:
					myView.saveCanvas(context);
					break;
				case R.id.button_o:
					Toast.makeText(context, "o", Toast.LENGTH_SHORT).show();
					break;
				case R.id.button_t:
					Toast.makeText(context, "t", Toast.LENGTH_SHORT).show();
					break;
				default:
					ColorDrawable drawable=  (ColorDrawable) view.getBackground();
					int color=drawable.getColor();
					bgColor.setBackgroundColor(color);
					myView.setBGColor(color);	
				}
			}
			
		}
		

	}

}
