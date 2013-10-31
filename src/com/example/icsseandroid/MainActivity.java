package com.example.icsseandroid;
//Download by http://www.codefans.net
import android.app.TabActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import com.and.netease.utils.MoveBg;
import com.example.icsseandroid.MyListView.MyListViewFling;

public class MainActivity extends TabActivity implements OnTouchListener,
GestureDetector.OnGestureListener, OnItemClickListener, MyListViewFling{
	private boolean hasMeasured = false;
	private LinearLayout layout_left;
	private LinearLayout layout_right;
	private ImageView iv_set;
	private MyListView lv_set;
	private final int MAX_WIDTH = 200;
	boolean first=true;
	private final static int SPEED = 30;
	private final static int sleep_time = 5;
	private GestureDetector mGestureDetector;
	private boolean isScrolling = false;
	private float mScrollX;
	private int window_width;
	private int currentpages=0;

	private String TAG = "jj";
	private View view = null;
	private String title[] = { "新闻", "ͬ话题", "图片", "跟帖", 
			"投票"};
	@SuppressWarnings("deprecation")
	void InitView() {
		layout_left = (LinearLayout) findViewById(R.id.layout_left);
		layout_right = (LinearLayout) findViewById(R.id.layout_right);
		iv_set = (ImageView) findViewById(R.id.iv_set);
		lv_set = (MyListView) findViewById(R.id.lv_set);
		lv_set.setAdapter(new ArrayAdapter<String>(this, R.layout.item,
				R.id.tv_item, title));
		lv_set.setOnItemClickListener(this);
		lv_set.setMyListViewFling(this);
		layout_right.setOnTouchListener(this);
		layout_left.setOnTouchListener(this);
		iv_set.setOnTouchListener(this);
		mGestureDetector = new GestureDetector(this);
		mGestureDetector.setIsLongpressEnabled(false);
		getMAX_WIDTH();

	}
	TabHost tabHost;
	TabHost.TabSpec tabSpec;
	RadioGroup radioGroup;
	RelativeLayout bottom_layout;
	ImageView img;
	int startLeft;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        bottom_layout = (RelativeLayout) findViewById(R.id.layout_bottom);
        
        tabHost = getTabHost();
        tabHost.addTab(tabHost.newTabSpec("news").setIndicator("News").setContent(new Intent(this, TabNewsActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("topic").setIndicator("Topic").setContent(new Intent(this, TabTopicActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("picture").setIndicator("Picture").setContent(new Intent(this, TabPicActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("follow").setIndicator("Follow").setContent(new Intent(this, TabFollowActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("vote").setIndicator("Vote").setContent(new Intent(this, TabVoteActivity.class)));
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(checkedChangeListener);
        img = new ImageView(this);
        img.setImageResource(R.drawable.tab_front_bg);
      int width =   getWindowManager().getDefaultDisplay().getWidth();
      LayoutParams params = new LayoutParams(width/5,LayoutParams.WRAP_CONTENT);
        bottom_layout.addView(img,params);
        InitView();
    }
    
	private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

		public void onCheckedChanged(RadioGroup group, int checkedId) {
			TextView tx=null;
			switch (checkedId) {
			case R.id.radio_news:{
				tabHost.setCurrentTabByTag("news");	
				tx=(TextView)findViewById(R.id.title);
				tx.setText("新闻");
				break;
			}
				
			case R.id.radio_topic:{
				tabHost.setCurrentTabByTag("topic");
				tx=(TextView)findViewById(R.id.title);
				tx.setText("话题");
				break;
			}
				
			case R.id.radio_pic:{
				tabHost.setCurrentTabByTag("picture");
				tx=(TextView)findViewById(R.id.title);
				tx.setText("图片");
				break;
			}
				
			case R.id.radio_follow:{
				tabHost.setCurrentTabByTag("follow");
				tx=(TextView)findViewById(R.id.title);
				tx.setText("跟帖");
				break;
			}
				
			case R.id.radio_vote:{
				tabHost.setCurrentTabByTag("vote");
				tx=(TextView)findViewById(R.id.title);
				tx.setText("投票");
				break;
			}
				

			default:
				break;
			}
		}
	};
	
	void doScrolling(float distanceX) {
		isScrolling = true;
		mScrollX -= distanceX;
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
				.getLayoutParams();
		RelativeLayout.LayoutParams layoutParams_1 = (RelativeLayout.LayoutParams) layout_right
				.getLayoutParams();
		layoutParams.leftMargin += mScrollX;
		layoutParams_1.leftMargin = -MAX_WIDTH + layoutParams.leftMargin;
		if (layoutParams.leftMargin <= 0) {
			isScrolling = false;
			layoutParams.leftMargin = 0;
			layoutParams_1.leftMargin = -MAX_WIDTH;

		} else if (layoutParams.leftMargin >= MAX_WIDTH) {
			isScrolling = false;
			layoutParams.leftMargin = MAX_WIDTH;
			layoutParams_1.leftMargin = 0;
		}
		layout_left.setLayoutParams(layoutParams);
		layout_right.setLayoutParams(layoutParams_1);
	}
	void getMAX_WIDTH() {
		ViewTreeObserver viewTreeObserver = layout_left.getViewTreeObserver();
		viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {
			@SuppressWarnings("deprecation")
			@Override
			public boolean onPreDraw() {
				if (!hasMeasured) {
					window_width = getWindowManager().getDefaultDisplay()
							.getWidth();
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
							.getLayoutParams();
					RelativeLayout.LayoutParams layoutParams_1 = (RelativeLayout.LayoutParams) layout_right
							.getLayoutParams();
					ViewGroup.LayoutParams layoutParams_2 = lv_set
							.getLayoutParams();
					layoutParams.width = window_width;
					layout_left.setLayoutParams(layoutParams);
					layoutParams_1.leftMargin = -MAX_WIDTH;
					layoutParams_1.width = MAX_WIDTH;
					layout_right.setLayoutParams(layoutParams_1);
					layoutParams_2.width = MAX_WIDTH;
					lv_set.setLayoutParams(layoutParams_2);
					hasMeasured = true;
				}
				return true;
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
					.getLayoutParams();
			if (layoutParams.leftMargin > 0) {
				new AsynMove().execute(-SPEED);
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		view = v;
		if (MotionEvent.ACTION_UP == event.getAction() && isScrolling == true) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
					.getLayoutParams();
			if (layoutParams.leftMargin > MAX_WIDTH / 2) {
				new AsynMove().execute(SPEED);
			} else {
				new AsynMove().execute(-SPEED);
			}
		}
		return mGestureDetector.onTouchEvent(event);
	}
	@Override
	public boolean onDown(MotionEvent e) {
		int position = lv_set.pointToPosition((int) e.getX(), (int) e.getY());
		if (position != ListView.INVALID_POSITION) {
			View child = lv_set.getChildAt(position
					- lv_set.getFirstVisiblePosition());
			if (child != null)
				child.setPressed(true);
		}

		mScrollX = 0;
		isScrolling = false;
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		if (view != null && view == iv_set) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
					.getLayoutParams();
			if (layoutParams.leftMargin <= 0) {
				new AsynMove().execute(SPEED);
				lv_set.setSelection(0);
			} else {
				new AsynMove().execute(-SPEED);
			}
		} else if (view != null && view == layout_left) {
			RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) layout_left
					.getLayoutParams();
			if (layoutParams.leftMargin > 0) {
				new AsynMove().execute(-SPEED);
			}
		}
		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
			doScrolling(distanceX);
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
	class AsynMove extends AsyncTask<Integer, Integer, Void> {
		@Override
		protected Void doInBackground(Integer... params) {
			int times = 0;
			if (MAX_WIDTH % Math.abs(params[0]) == 0)
				times = MAX_WIDTH / Math.abs(params[0]);
			else
				times = MAX_WIDTH / Math.abs(params[0]) + 1;
			for (int i = 0; i < times; i++) {
				publishProgress(params[0]);
				try {
					Thread.sleep(sleep_time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			return null;
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
					.getLayoutParams();
			RelativeLayout.LayoutParams layoutParams_1 = (RelativeLayout.LayoutParams) layout_right
					.getLayoutParams();
			if (values[0] > 0) {
				layoutParams.leftMargin = Math.min(layoutParams.leftMargin
						+ values[0], MAX_WIDTH);
				layoutParams_1.leftMargin = Math.min(layoutParams_1.leftMargin
						+ values[0], 0);
			} else {
				layoutParams.leftMargin = Math.max(layoutParams.leftMargin
						+ values[0], 0);
				layoutParams_1.leftMargin = Math.max(layoutParams_1.leftMargin
						+ values[0], - MAX_WIDTH);
				
			}
			layout_right.setLayoutParams(layoutParams_1);
			layout_left.setLayoutParams(layoutParams);
			

		}

	}

	@Override
	public void doFlingLeft(float distanceX) {
		doScrolling(distanceX);
	}
	@Override
	public void doFlingRight(float distanceX) {
		doScrolling(distanceX);
	}
	@Override
	public void doFlingOver(MotionEvent event) {
		if (MotionEvent.ACTION_UP == event.getAction() && isScrolling == true) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
					.getLayoutParams();
			if (layoutParams.leftMargin > MAX_WIDTH / 2) {
				new AsynMove().execute(SPEED);
			} else {
				new AsynMove().execute(-SPEED);
			}
		}

	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
				.getLayoutParams();
		if (layoutParams.leftMargin == MAX_WIDTH)
			switch(position){
			case 0:{
				    new AsynMove().execute(-SPEED);
				    RadioButton but=(RadioButton)radioGroup.getChildAt(0);
			        but.setChecked(true);	
					break;
			}
			case 1:{
				new AsynMove().execute(-SPEED);
				 RadioButton but=(RadioButton)radioGroup.getChildAt(1);
			     but.setChecked(true);
	            break;
			}
			case 2:{
				new AsynMove().execute(-SPEED);
				 RadioButton but=(RadioButton)radioGroup.getChildAt(2);
			        but.setChecked(true);	
	            break;    
			}
			case 3:{
				new AsynMove().execute(-SPEED);
				 RadioButton but=(RadioButton)radioGroup.getChildAt(3);
			     but.setChecked(true);
			     break;
			}
			case 4:{
				new AsynMove().execute(-SPEED);
				 RadioButton but=(RadioButton)radioGroup.getChildAt(4);
			     but.setChecked(true);
				break;
			}			
			
			}
	}
}