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
	public int getCount() { // ���w�g�k(���oList��Ƶ���)
		return list.size();
	}

	@Override
	public Object getItem(int position) { // ���w�g�k(���o�ӵ����)
		return list.get(position);
	}

	@Override
	public long getItemId(int position) { // ���w�g�k(���o�ӵ���ƪ�position)
		return position;
	}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	// ���o convertView (convertView �N�O�N R.layout.item �����)
    	convertView = myInflater.inflate(R.layout.item, null); 
    	
    	//�N��ƪ`�J�� layout UI ����
    	Item item = list.get(position);
    	
    	
    	ImageView img=(ImageView) convertView.findViewById(R.id.imageView1);
    	img.setImageResource(item.getItemImage());
    	convertView.setTag(item);
    	LinearLayout layout=(LinearLayout) convertView.findViewById(R.id.spinner_layout);
    	layout.setBackgroundColor(0x0022FFFF);
        
        return convertView;
    }

}