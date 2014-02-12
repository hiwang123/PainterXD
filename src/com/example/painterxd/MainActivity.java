package com.example.painterxd;


import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.animation.AnimatorSet.Builder;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification.Action;
import android.content.Context;
import android.content.Intent;
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
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
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
	ImageButton set,eraser,undo,redo;
	Spinner eraserSize;
	ImageView bgColor;
	EditText sendTo;
	AlertDialog dialog;
	MyAdapter adapter;
	
	Display display;
	
	final int SELECT_PIC=1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context=this;
		display=getWindowManager().getDefaultDisplay();
		
		set=(ImageButton) findViewById(R.id.imageSet);
		eraser=(ImageButton) findViewById(R.id.imageButton1);
		undo=(ImageButton) findViewById(R.id.imageButton2);
		redo=(ImageButton) findViewById(R.id.imageButton3);
		colorBar=(LinearLayout) findViewById(R.id.colorLayout);
		myView=(MyView) findViewById(R.id.myView1);
		eraserSize=(Spinner) findViewById(R.id.spinner1);
		List<Item> list = new ArrayList<Item>();
		list.add(new Item(4,R.drawable.size5));
		list.add(new Item(7,R.drawable.size4));
		list.add(new Item(10,R.drawable.size3));
		list.add(new Item(15,R.drawable.size2));
		list.add(new Item(22,R.drawable.size1));
		
		adapter = new MyAdapter(context, list);
		eraserSize.setAdapter(adapter);
		
		eraserSize.setOnItemSelectedListener(new MyOnItemSelectedListener());
		
		rgbs=Arrays.asList(getResources().getStringArray(R.array.rgb_array));
		
		ColorIni(); 
		set.setOnClickListener(new MyOnClickListener());
		eraser.setOnClickListener(new MyOnClickListener());
		undo.setOnClickListener(new MyOnClickListener());
		redo.setOnClickListener(new MyOnClickListener());
		
	}
	// Spinner OnItemSelectedListener 監聽器
	private class MyOnItemSelectedListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			Item item = (Item)view.getTag();
			myView.setSize(item.getSize());
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
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
			case R.id.imageSet:
				SetDialog builder = new SetDialog(MainActivity.this); 
				dialog=builder.create();
				dialog.show();
				break;
			case R.id.imageButton1:
				myView.setErase();
				break;
			case R.id.imageButton2:
				myView.undo();
				break;
			case R.id.imageButton3:
				myView.redo();
				break;
			default:
				ColorDrawable drawable=  (ColorDrawable) view.getBackground();
				int color=drawable.getColor();
				myView.clsErase();
				myView.setColor(color);	
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
			
			bgColor.setBackgroundColor(myView.getBGColor());
			
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
					myView.setBGColor(myView.getBGColor());
					dialog.dismiss();
					break;
				case R.id.button_s:
					myView.saveCanvas(context);
					break;
				case R.id.button_o:
					myView.clearAll();
					myView.setBGColor(myView.getBGColor());
					
					Intent intent = new Intent(Intent.ACTION_PICK, 
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, SELECT_PIC);
					
					dialog.dismiss();
					break;
				case R.id.button_t:
					Toast.makeText(context, "t", Toast.LENGTH_SHORT).show();
					break;
				case R.id.editText1:
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==SELECT_PIC&&resultCode==RESULT_OK){
			Uri SelectedImageUri=data.getData();
			myView.drawBitmap(SelectedImageUri,context);
		}
	}

}
