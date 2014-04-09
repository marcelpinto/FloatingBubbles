package com.hardsoft.bubble;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.hardsoft.bubble.data.BubbleType;

public class Bubble extends RelativeLayout {
	
	private int id;
	private BubbleType type;
	/**
	 * Normal type of buble 
	 */
	private String title;
	private String message;
	private Bitmap icon;
	/**
	 * Add a custom view, whatever you want
	 */
	private View customView;
	
	public Bubble(Context context) {
		super(context);
	}
	
	public Bubble(Context context, AttributeSet attrs) {
		super(context,attrs);
	}
	
	public Bubble(Context context, AttributeSet attrs, int style) {
		super(context,attrs,style);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BubbleType getType() {
		return type;
	}

	public void setType(BubbleType type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Bitmap getIcon() {
		return icon;
	}

	public void setIcon(Bitmap icon) {
		this.icon = icon;
	}

	public View getCustomView() {
		return customView;
	}

	public void setCustomView(View customView) {
		this.customView = customView;
	}
	
	
}
