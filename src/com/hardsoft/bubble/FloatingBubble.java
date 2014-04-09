package com.hardsoft.bubble;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import com.hardsoft.bubble.data.BubbleType;
import com.hardsoft.bubble.interfaces.FloatingBubbleInterface;

public class FloatingBubble implements FloatingBubbleInterface {

	private BubbleController mBubble;

	protected FloatingBubble(Context context, BubbleType type) {
		mBubble = new BubbleController(context, this, (WindowManager) context.getSystemService(Context.WINDOW_SERVICE),type);
	}

	public static class Builder {

		BubbleController.BubbleParams B;
		BubbleType mType;
		
		public Builder(Context c) {
			this(c, BubbleType.BUBLE_TIP);
		}

		public Builder(Context c, BubbleType type) {
			B = new BubbleController.BubbleParams(c, type);
			mType = type;
		}

		/**
         * Returns a {@link Context} with the appropriate theme for dialogs created by this Builder.
         * Applications should use this Context for obtaining LayoutInflaters for inflating views
         * that will be used in the resulting dialogs, as it will cause views to be inflated with
         * the correct theme.
         *
         * @return A Context for built Dialogs.
         */
        public Context getContext() {
            return B.mContext;
        }
        
        public Builder setRemovable(boolean b) {
        	B.mIsRemovable = b;
        	return this;
        }

        /**
         * Set the title using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTitle(int titleId) {
            B.mTitle = B.mContext.getText(titleId);
            return this;
        }
        
        /**
         * Set the title displayed in the {@link Dialog}.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTitle(CharSequence title) {
            B.mTitle = title;
            return this;
        }
        
        /**
         * Set the message to display using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setMessage(int messageId) {
            B.mMessage = B.mContext.getText(messageId);
            return this;
        }
        
        /**
         * Set the message to display.
          *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setMessage(CharSequence message) {
            B.mMessage = message;
            return this;
        }
        
        /**
         * Set the resource id of the {@link Drawable} to be used in the title.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setIcon(int iconId) {
            B.mIconId = iconId;
            return this;
        }
        
        /**
         * Set the {@link Drawable} to be used in the title.
          *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setIcon(Drawable icon) {
            B.mIcon = icon;
            return this;
        }

        /**
         * Set an icon as supplied by a theme attribute. e.g. android.R.attr.alertDialogIcon
         *
         * @param attrId ID of a theme attribute that points to a drawable resource.
         */
        public Builder setIconAttribute(int attrId) {
            TypedValue out = new TypedValue();
            B.mContext.getTheme().resolveAttribute(attrId, out, true);
            B.mIconId = out.resourceId;
            return this;
        }

        
        /**
         * Set a custom view to be the contents of the Dialog. If the supplied view is an instance
         * of a {@link ListView} the light background will be used.
         *
         * @param view The view to use as the contents of the Dialog.
         * 
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setView(View view) {
            B.mView = view;
            return this;
        }
        
        public Builder setMenuViews(ArrayList<View> views) {
        	B.mMenuViews = views;
        	return this;
        }
        
        public Builder setOnBubbleClickListener(FloatingBubbleInterface.OnBubbleClick listener) {
        	B.mOnBubbleClick = listener;
        	return this;
        }
        
        public Builder setOnDestroyedListener(FloatingBubbleInterface.OnBubbleDestroyed listener) {
        	B.mOnBubbleDestroyed = listener;
        	return this;
        }
        
        public Builder setOnDraggedListener(FloatingBubbleInterface.OnBubbleDragged listener) {
        	B.mOnBubbleDragged = listener;
        	return this;
        }


        /**
         * Creates a {@link AlertDialog} with the arguments supplied to this builder. It does not
         * {@link Dialog#show()} the dialog. This allows the user to do any extra processing
         * before displaying the dialog. Use {@link #show()} if you don't have any other processing
         * to do and want this to be created and displayed.
         * @throws Exception 
         */
        public FloatingBubble create() throws Exception {
            final FloatingBubble dialog = new FloatingBubble(B.mContext, mType);
            B.apply(dialog.mBubble);
            return dialog;
        }

        /**
         * Creates a {@link AlertDialog} with the arguments supplied to this builder and
         * {@link Dialog#show()}'s the dialog.
         * @throws Exception 
         */
        public FloatingBubble show() throws Exception {
        	FloatingBubble dialog = create();
        	dialog.mBubble.setupView();
            return dialog;
        }


	}

}
