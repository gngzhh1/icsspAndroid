package com.example.icsseandroid;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.AnimationUtils;
import android.view.View;
import android.widget.ListView;

public class MyListView extends ListView implements OnGestureListener {

	private GestureDetector gd;
	public static final char FLING_CLICK = 0;
	public static final char FLING_LEFT = 1;
	public static final char FLING_RIGHT = 2;
	public static char flingState = FLING_CLICK;
	private float distanceX;
	private MyListViewFling myListViewFling;
	public static boolean isClick = false;

	public void setMyListViewFling(MyListViewFling myListViewFling) {
		this.myListViewFling = myListViewFling;
	}

	public float getDistanceX() {
		return distanceX;
	}

	public char getFlingState() {
		return flingState;
	}

	private Context context;

	public MyListView(Context context) {
		super(context);

	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		gd = new GestureDetector(this);
	}

	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		myListViewFling.doFlingOver(event);
		this.gd.onTouchEvent(event);

		return super.dispatchTouchEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		
		if (ev.getAction() == MotionEvent.ACTION_DOWN)
			isClick = true;
		if (ev.getAction() == MotionEvent.ACTION_MOVE)
			isClick = false;
		return super.onTouchEvent(ev);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		int position = pointToPosition((int) e.getX(), (int) e.getY());
		if (position != ListView.INVALID_POSITION) {
			View child = getChildAt(position - getFirstVisiblePosition());
		}
		return true;
	}
	@Override
	public void onShowPress(MotionEvent e) {

	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		if (distanceX > 0) {
			flingState = FLING_RIGHT;
			myListViewFling.doFlingLeft(distanceX);
		} else if (distanceX < 0) {
			flingState = FLING_LEFT;
			myListViewFling.doFlingRight(distanceX);
		}

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		return false;
	}

	
	interface MyListViewFling {
		void doFlingLeft(float distanceX);
		void doFlingRight(float distanceX);
		void doFlingOver(MotionEvent event);

	}
	
	@Override
	public void onGlobalLayout() {
		getViewTreeObserver().removeGlobalOnLayoutListener(this);
		final int heightPx = getContext().getResources().getDisplayMetrics().heightPixels;
		boolean inversed = false;
		final int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			int[] location = new int[2];
			child.getLocationOnScreen(location);
			if (location[1] > heightPx) {
				break;
			}

			inversed = !inversed;
		}

	}

}
