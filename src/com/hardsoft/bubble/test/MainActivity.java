package com.hardsoft.bubble.test;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.hardsoft.bubble.FloatingBubble;
import com.hardsoft.bubble.R;
import com.hardsoft.bubble.arcmenu.RayMenu;
import com.hardsoft.bubble.data.BubbleType;

public class MainActivity extends Activity {
	
	private RayMenu mRayMenu;

	private final static int[] ITEM_DRAWABLES = {R.drawable.ic_bubble, R.drawable.ic_close, R.drawable.ic_connected};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_demo);
		//Intent i = new Intent(this, ChatHeadDrawerService.class);
		//startService(i);
		//ts = new TipAndShow(this);
		//ts.onCreateAndShowTip(1);
		mRayMenu = new RayMenu(this);
		final int itemCount = ITEM_DRAWABLES.length;
		for (int i = 0; i < itemCount; i++) {
		    ImageView item = new ImageView(this);
		    item.setImageResource(ITEM_DRAWABLES[i]);

		    final int position = i;
		    mRayMenu.addItem(item, new OnClickListener() {

		        @Override
		        public void onClick(View v) {
		            Toast.makeText(MainActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
		        }
		    });// Add a menu item
		}
	}
	
	public void showDefaultBubble(View v) {
		LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		FloatingBubble bubble = new FloatingBubble.Builder(getApplicationContext(), BubbleType.BUBLE_ARCH_MENU)
			.setIcon(-1)
			//.setView(mRayMenu)
			.setRemovable(true)
			//.setView(mInflater.inflate(R.layout.view_fragment_test, new RelativeLayout(MainActivity.this), false))
			.setTitle("Testing FloatingBubbles")
			.setMessage("This is the content of a super bubble!")
			.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//ts.onDestroy();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Log.v("MPB", "Entra");
		return super.onOptionsItemSelected(item);
	}

}
