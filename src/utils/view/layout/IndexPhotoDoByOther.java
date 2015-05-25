package utils.view.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.example.uitest.R;


/**
 * Created by Administrator on 2014/11/14.
 */
public class IndexPhotoDoByOther extends LinearLayout {

	public IndexPhotoDoByOther (Context context) {

		super(context);
		initView(context);
	}

	public IndexPhotoDoByOther (Context context, AttributeSet attrs) {

		super(context, attrs);
		initView(context);
	}

	public IndexPhotoDoByOther (Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);
		initView(context);
	}
	/**
	 * 加载另一个布局文件
	 *
	 * @param context 谁需要加载当前布局
	 */
	private void initView(Context context) {
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		setLayoutParams(lp);

		LayoutInflater mInflater = LayoutInflater.from(context);

		View v = mInflater.inflate(R.layout.index_photo_do_by_other, null);
		addView(v,lp);
	}
}
