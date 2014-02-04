package com.example.painterxd;

public class Item {
	private int size;
	private int itemImage;
	
	public Item(int size,int itemImage) {
		this.size = size;
		this.itemImage=itemImage;
	}
	
	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size= size;
	}
	public int getItemImage(){
		return itemImage;
	}
	
}
