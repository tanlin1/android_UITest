package utils.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.uitest.IndexListViewItemBean;
import com.example.uitest.R;

import java.util.HashMap;
import java.util.List;


/**
 * 主页适配器
 * 避免大量重复代码
 * Created by Administrator on 2015/3/24.
 */
public class MyContentListViewAdapter extends BaseAdapter {
	private List<HashMap<String, Object>> listData;

	private Activity activity;
	//视图容器
	private LayoutInflater listContainer;

	public MyContentListViewAdapter(Context context, List<HashMap<String, Object>> content) {

		listContainer = LayoutInflater.from(context);
		this.listData = content;
		activity = (Activity) context;
	}

	@Override
	public int getCount() {

		return listData.size();
	}

	@Override
	public Object getItem(int position) {

		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final IndexListViewItemBean listItem;

		if (convertView == null) {
			listItem = new IndexListViewItemBean();
			convertView = listContainer.inflate(R.layout.index_listview_content, null);
			listItem.photoHead = (ImageView) convertView.findViewById(R.id.index_head_photo_thumbnail);
			listItem.userName = (TextView) convertView.findViewById(R.id.userNameIndex);
			listItem.userName.setTextScaleX(1.2f);
			listItem.userAddress = (TextView) convertView.findViewById(R.id.address);
			listItem.showingPicture = (ImageView) convertView.findViewById(R.id.showingPicture);
			listItem.photoDescribe = (TextView) convertView.findViewById(R.id.index_photo_describe);

			listItem.progressBar = (android.widget.ProgressBar) convertView.findViewById(R.id.index_processBar);
			listItem.likeSum = (TextView) convertView.findViewById(R.id.index_photo_like_num);
			listItem.likeText = (TextView) convertView.findViewById(R.id.index_photo_like_text);
			listItem.commentSum = (TextView) convertView.findViewById(R.id.index_photo_comment_num);
			listItem.commentText = (TextView) convertView.findViewById(R.id.index_photo_comment_text);

			// 设置控件集到convertView中
			convertView.setTag(listItem);
		} else {
			listItem = (IndexListViewItemBean) convertView.getTag();
		}

		HashMap<String, Object> map = listData.get(position);
		if (map.get("photoHead") instanceof Bitmap) {
			listItem.photoHead.setImageBitmap((Bitmap) map.get("photoHead"));
		} else {
			listItem.photoHead.setImageResource(R.drawable.head2);
		}

		listItem.userName.setText((CharSequence) map.get("userName"));
		listItem.userAddress.setText((CharSequence) map.get("userAddress"));

		listItem.showingPicture.setImageBitmap((Bitmap) map.get("userPicture"));
		if (map.get("userPicture") != null) {
			listItem.progressBar.setVisibility(View.GONE);
		}
		listItem.photoDescribe.setText((CharSequence) map.get("myWords"));

		listItem.commentSum.setText(String.valueOf(map.get("commentsNumber")));
		listItem.likeSum.setText(String.valueOf(map.get("likesNumber")));

		listItem.showingPicture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = null;//new Intent(activity, PictureScanActivity.class);

				Integer userId = (Integer) listData.get(position).get("id");
				Integer rs_id = (Integer) listData.get(position).get("rs_id");
				String detail = (String) listData.get(position).get("detailPhoto");

				intent.putExtra("userId", userId);
				intent.putExtra("rs_id", rs_id);

				intent.putExtra("detailPhoto", detail);

				activity.startActivity(intent);
				activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			}
		});


		viewTheComment(listItem.likeSum);
		viewTheComment(listItem.likeText);

		viewTheComment(listItem.commentSum);
		viewTheComment(listItem.commentText);

		return convertView;
	}

	/**
	 * 查看评论
	 *
	 * @param v 屏幕上的某个可点击视图
	 */
	private void viewTheComment(View v) {

		v.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				//activity.startActivity(new Intent(activity, ViewLikeOrComment.class));
			}
		});
	}
}
