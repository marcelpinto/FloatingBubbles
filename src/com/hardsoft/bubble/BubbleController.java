package com.hardsoft.bubble;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static android.view.WindowManager.LayoutParams.TYPE_PHONE;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hardsoft.bubble.data.BubbleType;
import com.hardsoft.bubble.interfaces.FloatingBubbleInterface;

public class BubbleController implements OnTouchListener {

    private static final int GLOBAL_PADDING = 5;
	private final Context mContext;
    private final FloatingBubbleInterface mFloatingBubbleInterface;
    private final WindowManager mWindowManager;
    
    private CharSequence mTitle;

    private CharSequence mMessage;

    private View mCustomView;
    
    private RelativeLayout mContentView;

    private int mIconId = -1;
    
    private Drawable mIcon;
    
    private ImageView mIconView;
    
    private TextView mTitleView;

    private TextView mMessageView;

    private ViewGroup mBubble;
	private LayoutParams mBubbleParams;
	private int initialX;
	private int initialY;
	private float initialTouchX;
	private float initialTouchY;
	private boolean isRemovable;
	private ImageView mTrash;
	private boolean hasBeenInTrash;


    public BubbleController(Context context, FloatingBubbleInterface di, WindowManager manager, BubbleType type) {
        mContext = context;
        mFloatingBubbleInterface = di;
        mWindowManager = manager;

		LayoutInflater inflater = LayoutInflater.from(mContext);
		switch (type) {
		case BUBLE_ARCH_MENU:
			mBubble = (ViewGroup) inflater.inflate(R.layout.ray_menu, null);
			break;
		default:
			mBubble = (ViewGroup) inflater.inflate(R.layout.bubble, null);
			break;
		}
		
		mIconView = (ImageView) mBubble.findViewById(R.id.w_imv_bubble_icon);
		mIconView.setOnTouchListener(this);
    	mContentView = (RelativeLayout) mBubble.findViewById(R.id.w_frl_bubble_content);
    }
    
    
    public void setTitle(CharSequence title) {
        mTitle = title;
        if (mTitleView != null) {
            mTitleView.setText(title);
        }
    }

    public void setMessage(CharSequence message) {
        mMessage = message;
        if (mMessageView != null) {
            mMessageView.setText(message);
        }
    }

    /**
     * Set the view to display in the dialog.
     */
    public void setView(View view) {
    	if (view!=null) {
	    	if (view.findViewById(R.id.w_tev_default_bubble_title)!=null) {
	    		mTitleView = (TextView) view.findViewById(R.id.w_tev_default_bubble_title);
	    	}
	    	if (view.findViewById(R.id.w_tev_default_bubble_message)!=null) {
	    		mMessageView = (TextView) view.findViewById(R.id.w_tev_default_bubble_message);
	    	}
    	}
        mCustomView = view;
    }

    /**
     * Set resId to 0 if you don't want an icon.
     * @param resId the resourceId of the drawable to use as the icon or 0
     * if you don't want an icon.
     */
    public void setIcon(int resId) {
        mIconId = resId;
        if (mIconView != null) {
            if (resId > 0) {
                mIconView.setImageResource(mIconId);
            } else if (resId == 0) {
                mIconView.setVisibility(View.GONE);
            }
        }
    }
    
    public void setIcon(Drawable icon) {
        mIcon = icon;
        if ((mIconView != null) && (mIcon != null)) {
            mIconView.setImageDrawable(icon);
        }
    }

    /**
     * @param attrId the attributeId of the theme-specific drawable
     * to resolve the resourceId for.
     *
     * @return resId the resourceId of the theme-specific drawable
     */
    public int getIconAttributeResId(int attrId) {
        TypedValue out = new TypedValue();
        mContext.getTheme().resolveAttribute(attrId, out, true);
        return out.resourceId;
    }
    
    public void setupView() {
    	if (isRemovable)
    		mTrash = createTrash();

    	RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mContentView.getLayoutParams();
    	params.width = (int) (getScreenSize(mWindowManager).x - (mContext.getResources().getDimension(R.dimen.dim_bubble_icon)+GLOBAL_PADDING));
    	mContentView.setLayoutParams(params);
    	mContentView.addView(mCustomView);
    	mWindowManager.addView(mBubble, mBubbleParams =getBubbleParams());
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
		paramsTrash.alpha = (float) 0.8;
		return paramsTrash;
	}

	private WindowManager.LayoutParams getBubbleParams() {
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

	public ImageView createTrash() {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(mContext);
		return (ImageView) inflater.inflate(R.layout.trash, null);
	}

	private static Point getScreenSize(WindowManager wm) {
		// TODO Auto-generated method stub
		Display display = wm.getDefaultDisplay();
		Point size = new Point();

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR1) {
			display.getSize(size);
		} else {
			size =new Point(display.getWidth(),display.getHeight());
		}
		return size;
	}


	public void hideBubbleView() {
		mContentView.setVisibility(View.GONE);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (v==null) {
			return true;
		}
		switch (event.getAction()) {
		case ACTION_DOWN:
			initialX = mBubbleParams.x;
			initialY = mBubbleParams.y;
			initialTouchX = event.getRawX();
			initialTouchY = event.getRawY();
			//Toast.makeText(getApplication(), "Drag it to here to remove!", Toast.LENGTH_SHORT).show();
			hideBubbleView();
			RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams)mIconView.getLayoutParams();
			p.addRule(RelativeLayout.ALIGN_PARENT_LEFT,0);
			p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,0);
			mIconView.setLayoutParams(p);
			if (isRemovable) {
				Log.v("MPB","Add trash");
				mWindowManager.addView(mTrash, getTrashParams());
			}
			return true;

		case ACTION_MOVE:
			mBubbleParams.x = initialX + (int) (event.getRawX() - initialTouchX);
			mBubbleParams.y = initialY	+ (int) (event.getRawY() - initialTouchY);
			mWindowManager.updateViewLayout(mBubble, mBubbleParams);
			
			if (isRemovable) {
				LayoutParams paramsTrash = getTrashParams();
				if(mBubbleParams.y > getScreenSize(mWindowManager).y * 0.7 && ( mBubbleParams.x >= getScreenSize(mWindowManager).x * 0.35 && mBubbleParams.x <= getScreenSize(mWindowManager).x * 0.65)) {
					hasBeenInTrash = true;
					paramsTrash.alpha=1;
					android.view.ViewGroup.LayoutParams params = mTrash.getLayoutParams();
					params.width = 200;
					params.height = 200;
					mTrash.setSelected(true);
					mTrash.setLayoutParams(params);
					mWindowManager.updateViewLayout(mTrash, paramsTrash);
				} else if (hasBeenInTrash) {
					hasBeenInTrash=false;
					paramsTrash.alpha=(float)0.5;
					android.view.ViewGroup.LayoutParams params = mTrash.getLayoutParams();
					params.width = 100;
					params.height = 100;
					mTrash.setSelected(false);
					mTrash.setLayoutParams(params);
					mWindowManager.updateViewLayout(mTrash, paramsTrash);
				}
			}
			
			return true;

		case ACTION_UP:
			if (isRemovable) {
				mWindowManager.removeView(mTrash);

				if (mBubbleParams.y > getScreenSize(mWindowManager).y * 0.7
						&& (mBubbleParams.x >= getScreenSize(mWindowManager).x * 0.35 && mBubbleParams.x <= getScreenSize(mWindowManager).x * 0.65)) {
					// mChatHead.setVisibility(View.GONE);
					mWindowManager.removeView(mBubble);
					mTrash = null;
					mBubble = null;
					// TODO: callback
					return true;
				}
			}

			RelativeLayout ly =  (RelativeLayout) mBubble.findViewById(R.id.w_lil_bubble_root);
			ly.removeAllViews();
			//mIconView.setVisibility(View.GONE);
			mIconView.setVisibility(View.VISIBLE);
			mContentView.setVisibility(View.VISIBLE);
			RelativeLayout.LayoutParams iconParams = (RelativeLayout.LayoutParams)mIconView.getLayoutParams();
			RelativeLayout.LayoutParams contentParams = (RelativeLayout.LayoutParams)mContentView.getLayoutParams();
			if(mBubbleParams.x < getScreenSize(mWindowManager).x / 2) {
				//ly.removeAllViews();
				//ly.addView(mIconView);
				//ly.addView(mContentView);
				
				iconParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,1);
				iconParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,0);
				mIconView.setLayoutParams(iconParams);
				contentParams.addRule(RelativeLayout.LEFT_OF,0);
				contentParams.addRule(RelativeLayout.RIGHT_OF,mIconView.getId());
		        mContentView.setLayoutParams(contentParams);
		        ly.addView(mContentView);
		        ly.addView(mIconView);
		        //mContentView.setLayoutParams(lp);

			} else { // Set textView to left of image
				iconParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,0);
				iconParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,1);
				mIconView.setLayoutParams(iconParams);
				contentParams.addRule(RelativeLayout.LEFT_OF,mIconView.getId());
				contentParams.addRule(RelativeLayout.RIGHT_OF,0);
		        mContentView.setLayoutParams(contentParams);
		        ly.addView(mContentView);
		        ly.addView(mIconView);
				
		        //mContentView.setLayoutParams(lp);
				//ly.removeAllViews();
				//ly.addView(mContentView);
				//ly.addView(mIconView);
				 
			}
			
			
			//mBubbleParams.x = (int) event.getRawX();
			//mBubbleParams.y = (int) event.getRawY();
			//mWindowManager.updateViewLayout(mBubble, mBubbleParams);
			return true;

		default:
			return false;
		}
	}
	
  
    public static class BubbleParams {
        public final Context mContext;
        public final LayoutInflater mInflater;
        
        public int mIconId = 0;
        public Drawable mIcon;
        public int mIconAttrId = 0;
        public CharSequence mTitle;
        public CharSequence mMessage;
        public boolean mIsRemovable;
        public FloatingBubbleInterface.OnBubbleClick mOnBubbleClick;
        public FloatingBubbleInterface.OnBubbleDragged mOnBubbleDragged;
        public FloatingBubbleInterface.OnBubbleDestroyed mOnBubbleDestroyed;
        public final BubbleType mType;
        public View mView;
        
        public BubbleParams(Context context, BubbleType bubbleType) {
            mContext = context;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mType = bubbleType;
        }
    
        public void apply(BubbleController dialog) {
        	dialog.isRemovable=mIsRemovable;
            if (mIcon != null) {
                dialog.setIcon(mIcon);
            }
            if (mIconId >= 0) {
                dialog.setIcon(mIconId);
            }
            if (mIconAttrId > 0) {
                dialog.setIcon(dialog.getIconAttributeResId(mIconAttrId));
            }
            if (mView != null) {
            	dialog.setView(mView);
            } else {
            	LayoutInflater inflater = LayoutInflater.from(mContext);
        		View defaultView = inflater.inflate(R.layout.default_view, null);
            	dialog.setView(defaultView);
            }
            if (mTitle != null) {
                dialog.setTitle(mTitle);
            }
            if (mMessage != null) {
                dialog.setMessage(mMessage);
            }
            
        }
        
    }


}
