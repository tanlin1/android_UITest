package com.example.uitest.view;


import java.util.Date;

import android.widget.*;
import com.example.uitest.R;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView.OnScrollListener;

/**
 * ListView下拉刷新
 *
 */
public class CustomListView extends ListView implements OnScrollListener {

	public static String TAG = "CustomListView";

	private final static int RELEASE_To_REFRESH = 0;
	private final static int PULL_To_REFRESH = 1;
	private final static int REFRESHING = 2;
//	private final static int TO_LAOD_MORE = 3;

	private final static int LOADING = 4;
	private final static int DONE = 5;

	// 实际的padding的距离与界面上偏移距离的比例
	private final static int RATIO = 4;

	private LayoutInflater inflater;

	private LinearLayout headView;

	private TextView tipsTextView;
	private TextView lastUpdatedTextView;
	private ImageView arrowImageView;
	private ProgressBar progressBar;


	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	// 用于保证startY的值在一个完整的touch事件中只被记录一次
	private boolean isRecored;

	private int headContentWidth;
	private int headContentHeight;

//	private int footContentWidth;
//	private int footContentHeight;

	private int startY;
	private int firstItemIndex;

	private int state;

	private boolean isBack;

	private OnRefreshListener refreshListener;
	private OnLoadListener loadListener;

	private boolean isRefreshable;
	
	private ProgressBar moreProgressBar;
	private TextView loadMoreView;
	private View moreView;

	static Context context;

	public CustomListView(Context context) {
		super(context);
		CustomListView.context = context;
		init(context);
	}

	public CustomListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		CustomListView.context = context;
		init(context);
	}

	private void init(Context context) {

		setCacheColorHint(context.getResources().getColor(R.color.transparent));
		inflater = LayoutInflater.from(context);

		headView = (LinearLayout) inflater.inflate(R.layout.head, this, false);

		arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);
		arrowImageView.setMinimumWidth(70);
		arrowImageView.setMinimumHeight(50);
		progressBar = (ProgressBar) headView.findViewById(R.id.head_progressBar);
		tipsTextView = (TextView) headView.findViewById(R.id.head_tipsTextView);
		lastUpdatedTextView = (TextView) headView.findViewById(R.id.head_lastUpdatedTextView);
		lastUpdatedTextView.setText(R.string.pull_to_refresh_refresh_lasttime);

		measureView(headView);
		headContentHeight = headView.getMeasuredHeight();
		headContentWidth = headView.getMeasuredWidth();

		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		headView.invalidate();

		Log.i("size", "width:" + headContentWidth + " height:" + headContentHeight);

		setOnScrollListener(this);
		addHeaderView(headView, null, false);

		animation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		state = DONE;
		isRefreshable = false;

		moreView = LayoutInflater.from(context).inflate(R.layout.listfooter_more, this, false);

		moreProgressBar = (ProgressBar) moreView.findViewById(R.id.pull_to_refresh_progress);
		loadMoreView = (TextView) moreView.findViewById(R.id.load_more);
		loadMoreView.setText(" 试试下拉");
//		measureView(moreView);
//		footContentHeight = moreView.getMeasuredHeight();
//		footContentWidth = moreView.getMeasuredWidth();

		moreView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onLoad();
			}
		});
		//moreView.setVisibility(View.GONE);

		addFooterView(moreView);
		//scrollTo(0, footContentHeight+headContentHeight);
	}

	boolean lastOne = false;

	public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		firstItemIndex = firstVisibleItem;
		if(totalItemCount < 2){
			return ;
		}
		lastOne = (firstVisibleItem + visibleItemCount) >= totalItemCount;
	}

	public void onScrollStateChanged(AbsListView arg0, int scrollState) {

		if((scrollState == SCROLL_STATE_IDLE)){

			moreView.setVisibility(VISIBLE);
			isRefreshable = true;

		} else if(state == REFRESHING || state == LOADING) {

			isRefreshable = false;
		}
	}


	@SuppressWarnings("NullableProblems")
	public boolean onTouchEvent(MotionEvent event) {

		if (isRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (firstItemIndex == 0 && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
				}
				break;

			case MotionEvent.ACTION_UP:

				if (state != REFRESHING && state != LOADING) {
					if (state == DONE) {
						Log.i(TAG, "DONE ,do nothing");
					}
					if (state == PULL_To_REFRESH) {
						state = DONE;
						changeHeaderViewByState();
					}
					if (state == RELEASE_To_REFRESH) {
						state = REFRESHING;
						changeHeaderViewByState();
						onRefresh();
					}
				}
				isRecored = false;
				isBack = false;

				break;

			case MotionEvent.ACTION_MOVE:

				int tempY = (int) event.getY();

				if (!isRecored && firstItemIndex == 0) {
					isRecored = true;
					startY = tempY;
				}
				if (state != REFRESHING && isRecored && state != LOADING) {

					// 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动
					// 可以松手去刷新了
					if (state == RELEASE_To_REFRESH) {

						setSelection(0);

						// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
						if (((tempY - startY) / RATIO < headContentHeight) && (tempY - startY) > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
						// 一下子推到顶了
						else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
						}
						// 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
					}
					// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
					if (state == PULL_To_REFRESH) {

						setSelection(0);

						// 下拉到可以进入RELEASE_TO_REFRESH的状态
						if ((tempY - startY) / RATIO >= headContentHeight) {
							state = RELEASE_To_REFRESH;
							isBack = true;
							changeHeaderViewByState();
						}
						else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
						}
					}

					if (state == DONE) {
						if (tempY - startY > 0) {
							state = PULL_To_REFRESH;
						}
					}

					if (state == PULL_To_REFRESH) {
						headView.setPadding(0, -1 * headContentHeight + (tempY - startY) / RATIO, 0, 0);

					}

					if (state == RELEASE_To_REFRESH) {
						headView.setPadding(0, (tempY - startY) / RATIO - headContentHeight, 0, 0);
					}
				}

				break;
			}
		}
		return super.onTouchEvent(event);
	}

	// 当状态改变时候，调用该方法，以更新界面
	private void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_To_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextView.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);
			tipsTextView.setText(R.string.pull_to_refresh_release_label);

			break;
		case PULL_To_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextView.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			// 是由RELEASE_To_REFRESH状态转变来的
			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(reverseAnimation);

				tipsTextView.setText(R.string.pull_to_refresh_refresh_pull_down);
			} else {
				tipsTextView.setText(R.string.pull_to_refresh_refresh_pull_down);
			}
			break;

		case REFRESHING:
			headView.setPadding(0, 0, 0, 0);
			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextView.setText(R.string.pull_to_refresh_refreshing);
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			break;
		case DONE:
			headView.setPadding(0, -1 * headContentHeight, 0, 0);

			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setImageResource(R.drawable.arrow);
			tipsTextView.setText(R.string.pull_to_refresh_refresh_pull_down);
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			break;
		}
	}

	public void setonRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}
	
	public void setonLoadListener(OnLoadListener loadListener) {
		this.loadListener = loadListener;
	}

	public interface OnRefreshListener {
		public void onRefresh();
	}
	
	public interface OnLoadListener {
		public void onLoad();
	}

	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}
	public void onRefreshComplete() {
		state = DONE;
		lastUpdatedTextView.setText("更新于:" + new Date().toLocaleString());
		changeHeaderViewByState();
		moreProgressBar.setVisibility(View.GONE);
		loadMoreView.setText(getContext().getString(R.string.more_data));
	}
	
	private void onLoad() {
		if (loadListener != null) {
			moreProgressBar.setVisibility(View.VISIBLE);
			loadMoreView.setText(getContext().getString(R.string.load_more));
			loadListener.onLoad();
		}
	}
	
	public void onLoadComplete() {
		state = DONE;
//		moreView.setVisibility(View.GONE);
		moreProgressBar.setVisibility(View.GONE);
		loadMoreView.setText(getContext().getString(R.string.more_data));
//		loadMoreView.setVisibility(GONE);
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		//
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}
}
