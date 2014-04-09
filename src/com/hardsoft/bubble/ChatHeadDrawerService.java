package com.hardsoft.bubble;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static android.view.WindowManager.LayoutParams.TYPE_PHONE;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatHeadDrawerService extends Service {

	private WindowManager mWindowManager;
	private View mChatHead;
	private ImageView mChatHeadImageView;
	private TextView mChatHeadTextView;
	private LinearLayout mLayout;
	private ImageView mTrash;
	private WindowManager.LayoutParams paramsChat;
	private WindowManager.LayoutParams paramsTrash;
	protected boolean hasBeenIn=false;
	private static int screenWidth;
	private static int screenHeight;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate() {
		super.onCreate();

		//Typeface tf = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");

		mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		Display display = mWindowManager.getDefaultDisplay();
		Point size= new Point();
		display.getSize(size);
		screenWidth = size.x;
		screenHeight = size.y;

		LayoutInflater inflater = LayoutInflater.from(this);
		mChatHead = inflater.inflate(R.layout.bubble, null);
		mTrash= (ImageView) inflater.inflate(R.layout.trash, null);
		//mChatHeadImageView = (ImageView) mChatHead.findViewById(R.id.chathead_imageview);
		//mChatHeadTextView = (TextView) mChatHead.findViewById(R.id.chathead_textview);
		//mLayout = (LinearLayout) mChatHead.findViewById(R.id.chathead_linearlayout);

//		mChatHeadTextView.setTypeface(tf);

		paramsChat = getChatParams();
		paramsTrash = getTrashParams();

		// Drag support!
		mChatHeadImageView.setOnTouchListener(new OnTouchListener() {

			int initialX, initialY;
			float initialTouchX, initialTouchY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case ACTION_DOWN:
					initialX = paramsChat.x;
					initialY = paramsChat.y;
					initialTouchX = event.getRawX();
					initialTouchY = event.getRawY();
					//Toast.makeText(getApplication(), "Drag it to here to remove!", Toast.LENGTH_SHORT).show();
					mChatHeadTextView.setVisibility(View.INVISIBLE);
					mWindowManager.addView(mTrash, paramsTrash);
					return true;

				case ACTION_MOVE:
					mChatHeadTextView.setVisibility(View.GONE);
					paramsChat.x = initialX + (int) (event.getRawX() - initialTouchX);
					paramsChat.y = initialY	+ (int) (event.getRawY() - initialTouchY);
					mWindowManager.updateViewLayout(mChatHead, paramsChat);
					
					if(paramsChat.y > screenHeight * 0.7 && ( paramsChat.x >= screenWidth * 0.35 && paramsChat.x <= screenWidth * 0.65)) {
						hasBeenIn =true;
						paramsTrash.alpha=1;
						android.view.ViewGroup.LayoutParams params = mTrash.getLayoutParams();
						params.width = 150;
						params.height = 150;
						mTrash.setLayoutParams(params);
						mTrash.setSelected(true);
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
						stopSelf();
					}

					if(paramsChat.x < screenWidth / 2) {
						mLayout.removeAllViews();
						mLayout.addView(mChatHeadImageView);
						mLayout.addView(mChatHeadTextView);
						mChatHeadTextView.setVisibility(View.VISIBLE);

					} else { // Set textView to left of image
						mLayout.removeAllViews();
						mLayout.addView(mChatHeadTextView);
						mLayout.addView(mChatHeadImageView);
						mChatHeadTextView.setVisibility(View.VISIBLE);
					}
					return true;

				default:
					return false;
				}
			}
		});

		mWindowManager.addView(mChatHead, paramsChat);
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

		paramsChat.x = 0;
		paramsChat.y = 50;
		paramsChat.gravity = Gravity.TOP | Gravity.LEFT;
		return paramsChat;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mChatHead != null) {
			mWindowManager.removeView(mChatHead);
		}
	}
}