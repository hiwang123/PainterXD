package com.example.painterxd;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter  {

	private LayoutInflater myInflater;
    private Context context;
    private List<Item> list;
    
    public MyAdapter(Context context, List<Item> list){
    	myInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
    }

    @Override
	public int getCount() { // 公定寫法(取得List資料筆數)
		return list.size();
	}

	@Override
	public Object getItem(int position) { // 公定寫法(取得該筆資料)
		return list.get(position);
	}

	@Override
	public long getItemId(int position) { // 公定寫法(取得該筆資料的position)
		return position;
	}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	// 取得 convertView (convertView 就是將 R.layout.item 物件化)
    	convertView = myInflater.inflate(R.layout.item, null); 
    	
    	//將資料注入到 layout UI 物件中
    	Item item = list.get(position);
    	
    	
    	ImageView img=(ImageView) convertView.findViewById(R.id.imageView1);
    	img.setImageResource(item.getItemImage());
    	convertView.setTag(item);
    	LinearLayout layout=(LinearLayout) convertView.findViewById(R.id.spinner_layout);
    	layout.setBackgroundColor(0x0022FFFF);
        
        return convertView;
    }

}