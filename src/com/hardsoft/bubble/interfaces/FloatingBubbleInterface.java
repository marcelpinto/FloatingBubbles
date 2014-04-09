package com.hardsoft.bubble.interfaces;

public interface FloatingBubbleInterface {
	interface OnBubbleClick {
		public void onClick();
	}
	
	interface OnBubbleDragged {
		public void onDrag();
	}
	
	interface OnBubbleDestroyed {
		public void onDestroyBubble();
	}
	
}
