package com.example.uitest;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * listView 的子项
 * 也就是适配器需要的数据对象
 *
 * 不能被继承
 *
 * Created by Administrator on 2014/12/13.
 */
public final class IndexListViewItemBean {

	public ImageView photoHead;

	public TextView userName;

	public ImageView location;

	public TextView userAddress;

	public TextView dasyaAgo;

	public ImageView clockImage;

	public TextView photoDescribe;

	// 用户发表的图片
	public ImageView showingPicture;

	/**
	 * 该照片被喜欢的人数
	 */

	public TextView likeSum;

	/**
	 * “Like” 提供点击进入查看那些人喜欢
	 */
	public TextView likeText;

	/**
	 * 该图片被评论的数量
	 */
	public TextView commentSum;

	/**
	 * “Comment”
	 */
	public TextView commentText;


	public ProgressBar progressBar;

	// 喜欢等图片按钮
	public ImageView loveHeartImage;

	public ImageView commentImage;

	public ImageView sharImage;

	public ImageView moreMenu;
}
