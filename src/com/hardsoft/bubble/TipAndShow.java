package com.hardsoft.bubble;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static android.view.WindowManager.LayoutParams.TYPE_PHONE;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TipAndShow implements OnClickListener {

	private WindowManager mWindowManager;
	private View mChatHead;
	private ImageView mChatHeadImageView;
	private TextView mMessage;
	private LinearLayout mLayout;
	private ImageView mTrash;
	private WindowManager.LayoutParams paramsChat;
	private WindowManager.LayoutParams paramsTrash;
	protected boolean hasBeenIn=false;
	private Context mContext;
	private TextView mTitle;
	private ImageButton mClose;
	private LinearLayout lyShow;
	private TipListener mCallback;
	private boolean isFirstShown;
	private static int screenWidth;
	private static int screenHeight;
	private boolean showTextOnDisplay = false;

	public interface TipListener {
		void onTipShown();
		void onTipDestroy();
	}
	
	public TipAndShow(Context mContext, TipListener c) {
		this.mContext = mContext;
		this.mCallback = c;
	}


	@SuppressLint("NewApi")
	public void createAndShowTip(String title, String txt) {

		//Typeface tf = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
/*
		mWindowManager = (WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);
		Display display = mWindowManager.getDefaultDisplay();
		Point size= new Point();
		display.getSize(size);
		screenWidth = size.x;
		screenHeight = size.y;

		LayoutInflater inflater = LayoutInflater.from(mContext);
		mChatHead = inflater.inflate(R.layout.bubble, null);
		mTrash= (ImageView) inflater.inflate(R.layout.trash, null);
		mChatHeadImageView = (ImageView) mChatHead.findViewById(R.id.chathead_imageview);
		mClose = (ImageButton) mChatHead.findViewById(R.id.bt_close);
		mClose.setOnClickListener(this);
		lyShow = (LinearLayout) mChatHead.findViewById(R.id.ly_show);
		LinearLayout.LayoutParams lp =(LinearLayout.LayoutParams) lyShow.getLayoutParams();
		lp.width=screenWidth/2;
		lyShow.setLayoutParams(lp);
		LayoutTransition mTransitioner = new LayoutTransition();
		if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.JELLY_BEAN)
			mTransitioner.enableTransitionType(LayoutTransition.CHANGING);
		lyShow.setLayoutTransition(mTransitioner);
		mMessage = (TextView) mChatHead.findViewById(R.id.chathead_textview);
		mTitle = (TextView) mChatHead.findViewById(R.id.txt_title);
		mTitle.setText(title);
		mMessage.setText(txt);
		mLayout = (LinearLayout) mChatHead.findViewById(R.id.chathead_linearlayout);
		if (!showTextOnDisplay)
			lyShow.setVisibility(View.GONE);
		else
			lyShow.setVisibility(View.VISIBLE);
//		mMessage.setTypeface(tf);

		paramsChat = getChatParams();
		paramsTrash = getTrashParams();

		isFirstShown=true;
		// Drag support!
		mChatHeadImageView.setOnTouchListener(new OnTouchListener() {

			int initialX, initialY;
			float initialTouchX, initialTouchY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (v==null) {
					destroyTip();
					return true;
				}
				switch (event.getAction()) {
				case ACTION_DOWN:
					if (isFirstShown) {
						mCallback.onTipShown();
						isFirstShown=false;
					}
					initialX = paramsChat.x;
					initialY = paramsChat.y;
					initialTouchX = event.getRawX();
					initialTouchY = event.getRawY();
					//Toast.makeText(getApplication(), "Drag it to here to remove!", Toast.LENGTH_SHORT).show();
					lyShow.setVisibility(View.GONE);
					mWindowManager.addView(mTrash, paramsTrash);
					return true;

				case ACTION_MOVE:
					paramsChat.x = initialX + (int) (event.getRawX() - initialTouchX);
					paramsChat.y = initialY	+ (int) (event.getRawY() - initialTouchY);
					mWindowManager.updateViewLayout(mChatHead, paramsChat);
					
					if(paramsChat.y > screenHeight * 0.7 && ( paramsChat.x >= screenWidth * 0.35 && paramsChat.x <= screenWidth * 0.65)) {
						hasBeenIn =true;
						paramsTrash.alpha=1;
						android.view.ViewGroup.LayoutParams params = mTrash.getLayoutParams();
						params.width = 200;
						params.height = 200;
						mTrash.setSelected(true);
						mTrash.setLayoutParams(params);
						mWindowManager.updateViewLayout(mTrash, paramsTrash);
					} else if (hasBeenIn) {
						hasBeenIn=false;
						paramsTrash.alpha=(float)0.5;
						android.view.ViewGroup.LayoutParams params = mTrash.getLayoutParams();
						params.width = 100;
						params.height = 100;
						mTrash.setSelected(false);
						mTrash.setLayoutParams(params);
						mWindowManager.updateViewLayout(mTrash, paramsTrash);
					}
					
					return true;

				case ACTION_UP:

					mWindowManager.removeView(mTrash);
					if(paramsChat.y > screenHeight * 0.7 && ( paramsChat.x >= screenWidth * 0.35 && paramsChat.x <= screenWidth * 0.65)) {
						//mChatHead.setVisibility(View.GONE);
						mWindowManager.removeView(mChatHead);
						mTrash=null;
						mChatHead=null;
						mCallback.onTipDestroy();
						return true;
					}

					if(paramsChat.x < screenWidth / 2) {
						mLayout.removeAllViews();
						mLayout.addView(mChatHeadImageView);
						mLayout.addView(lyShow);
						lyShow.setVisibility(View.VISIBLE);

					} else { // Set textView to left of image
						mLayout.removeAllViews();
						mLayout.addView(lyShow);
						mLayout.addView(mChatHeadImageView);
						lyShow.setVisibility(View.VISIBLE); 
					}
					return true;

				default:
					return false;
				}
			}
		});
		
		mWindowManager.addView(mChatHead, paramsChat);
		
		Animation shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
		mChatHeadImageView.startAnimation(shake);
		
		mChatHeadImageView.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				Log.i("RBM", "On focus change");
				destroyTip();
			}
		});*/
	}
	
	public void setDisplayText(boolean b) {
		showTextOnDisplay=b;
	}

	private WindowManager.LayoutParams getTrashParams() {
		// TODO Auto-generated method stub
		WindowManager.LayoutParams paramsTrash = new WindowManager.LayoutParams(
				100, // Width
				100, // Height
				TYPE_PHONE, // Type
				FLAG_NOT_FOCUSABLE, // Flag
				PixelFormat.TRANSLUCENT // Format
		);

		paramsTrash.gravity = Gravity.BOTTOM | Gravity.CENTER;
		paramsTrash.alpha=(float)0.5;
		return paramsTrash;
	}

	private WindowManager.LayoutParams getChatParams() {
		// TODO Auto-generated method stub
		WindowManager.LayoutParams paramsChat = new WindowManager.LayoutParams(
				WRAP_CONTENT, // Width
				WRAP_CONTENT, // Height
				TYPE_PHONE, // Type
				FLAG_NOT_FOCUSABLE, // Flag
				PixelFormat.TRANSLUCENT // Format
		);

		paramsChat.x = 50;
		paramsChat.y = 50;
		paramsChat.gravity = Gravity.TOP | Gravity.LEFT;
		return paramsChat;
	}

	public void destroyTip() {
		if (mChatHead!=null && mChatHead.getParent()!=null) {
			mWindowManager.removeView(mChatHead);
			mChatHead=null;
		}
		if (mTrash!=null && mTrash.getParent()!=null) {
			mWindowManager.removeView(mTrash);
			mTrash=null;
		}
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		destroyTip();
		mCallback.onTipDestroy();
	}
	
	
}
