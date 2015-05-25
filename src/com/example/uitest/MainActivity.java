package com.example.uitest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.*;

import android.widget.Button;
import com.example.uitest.model.AppInfo;
import com.example.uitest.view.CustomListView;
import com.example.uitest.view.CustomListView.OnLoadListener;
import com.example.uitest.view.CustomListView.OnRefreshListener;
import utils.adapter.MyContentListViewAdapter;
import utils.json.JSONObject;

public class MainActivity extends Activity {
	
	private static final String TAG = MainActivity.class.getSimpleName();

	private static final int LOAD_DATA_FINISH = 0;

	private static final int REFRESH_DATA_FINISH = 1;

	private static final int GET_ONE_RECORD = 2;


	
	private List<AppInfo> mList = new ArrayList<AppInfo>();
//	private CustomListAdapter mAdapter;
	private MyContentListViewAdapter mAdapter;
	private CustomListView mListView;

	//private ListView mListView;
	private static ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();

	private int count = 10;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {

			int info = msg.getData().getInt("Main");
			switch (info){
				case REFRESH_DATA_FINISH:

					if(mAdapter!=null){
						mAdapter.notifyDataSetChanged();
					}

					mListView.onRefreshComplete();	//下拉刷新完成
					//mListView.finishRefreshing();
					break;
				case LOAD_DATA_FINISH:
					if(mAdapter!=null){
						mAdapter.notifyDataSetChanged();
					}
					mListView.onLoadComplete();	//加载更多完成
					break;
				case GET_ONE_RECORD:
						IGetThis();
					break;
				default:
					break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		Button b = (Button) findViewById(R.id.button);
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, PictureSelect.class));
			}
		});
		
		mAdapter = new MyContentListViewAdapter(MainActivity.this, items);

		mListView = (CustomListView) findViewById(R.id.mListView);

		mListView.setAdapter(mAdapter);

		mListView.setonRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				//TODO 下拉刷新
				Log.e(TAG, "onRefresh");
				//loadData(0);
				refreshData("refresh");

			}
		});
		
		mListView.setonLoadListener(new OnLoadListener() {
			
			@Override
			public void onLoad() {
				//TODO 加载更多
				Log.e(TAG, "onLoad");
				//loadData(1);
				refreshData("load");
			}
		});
	}
	HashMap<String, Object> map;
	int name = 0;
	private List<HashMap<String, Object>> getListItems() {

		if(items == null){
			items = new ArrayList<HashMap<String, Object>>();
		}
		for(int i = 0; i < 3; i++){
			map = new HashMap<String, Object>();
			Resources res = getResources();
			BitmapDrawable bd = (BitmapDrawable) res.getDrawable(R.drawable.cloud_xiling);
			Bitmap bitmap = bd.getBitmap();

			map.put("userName","test"+name++);
			map.put("userAddress","test");
			map.put("userPicture", bitmap);
			map.put("myWords","test");
			map.put("commentsNumber",12);
			map.put("likesNumber",21);

			sendMessage("Main", GET_ONE_RECORD);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return items;
	}

	private void IGetThis(){
		System.out.println("主线程 正在处理");
		items.add(map);
		mAdapter.notifyDataSetChanged();
		System.out.println("主线程 处理 完成");

	}

	private void refreshData(String way) {

		new freshThread(way).start();
	}

	public class freshThread extends Thread {

		private String way;

		public freshThread(String way) {

			this.way = way;
		}

		@Override
		public void run() {

			if (way.equals("refresh")) {
				//IndexFragment.sendMessage("fresh", "sub_thread");
				internetRefresh();
			} else if (way.equals("load")) {
				internetLoad();
			} else {
				System.out.println("wrong fresh way !");
			}
		}
	}

	private void internetRefresh() {

		getListItems();
		sendMessage("Main", REFRESH_DATA_FINISH);
	}

	private void internetLoad() {

		getListItems();
		sendMessage("Main", LOAD_DATA_FINISH);
	}

	private void sendMessage(String key, int value){
		Bundle bundle = new Bundle();
		Message msg = new Message();
		bundle.putInt(key, value);
		msg.setData(bundle);
		if(handler != null){
			System.out.println("一条 数据 完成 发消息 给主线程");
			handler.sendMessage(msg);
		}
	}



	private static void setObject(IndexInfoBean bean, JSONObject obj) {

		if (obj.has("ID")) {
			bean.setId(obj.getInt("ID"));
		}
		if (obj.has("rs_id")) {
			bean.setRs_id(obj.getInt("rs_id"));
		}
		if (obj.has("detailPhoto")) {
			bean.setDetailPhoto(obj.getString("detailPhoto"));
		}
		if (obj.has("isLocated")) {
			bean.setIsLocated(obj.getString("isLocated"));
		}
		if (obj.has("sharesNumber")) {
			bean.setSharesNumber(obj.getInt("sharesNumber"));
		}
		if (obj.has("myWords")) {
			bean.setMyWords(obj.getString("myWords"));
		}
		if (obj.has("commentsNumber")) {
			bean.setCommentNumber(obj.getInt("commentsNumber"));
		}
		if (obj.has("likesNumber")) {
			bean.setLikeNumber(obj.getInt("likesNumber"));
		}
		if (obj.has("time")) {
			bean.setTime(obj.getString("time"));
		}
		if (obj.has("viewPhoto")) {
			bean.setViewPhoto(obj.getString("viewPhoto"));
		}
		if (obj.has("hasDetail")) {
			bean.setHasDetail(obj.getString("hasDetail"));
		}
		if(obj.has("isLocated") && Boolean.valueOf(obj.getString("isLocated"))){
			bean.setLocation(obj.getString("location"));
		}
		//        if (obj.has("location")) {
		//            bean.setLocation(obj.getJSONArray("location"));
		//        }
	}


}
