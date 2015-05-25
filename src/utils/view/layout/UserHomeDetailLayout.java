package utils.view.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.example.uitest.R;

/**
 * 个人中心界面一
 *
 * 其中内容包括：头像，照片数量，关注，粉丝等
 *
 * Created by Administrator on 2014/11/24.
 */
public class UserHomeDetailLayout extends LinearLayout {
	public UserHomeDetailLayout(Context context) {
		super(context);
		initView(context);
	}

	public UserHomeDetailLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public UserHomeDetailLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	private void initView(Context context){
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		setLayoutParams(lp);

		LayoutInflater mInflater = LayoutInflater.from(context);

		View v = mInflater.inflate(R.layout.user_home_like_fans_number, null);
		addView(v,lp);
	}
}
