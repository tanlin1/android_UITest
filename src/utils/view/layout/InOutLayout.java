package utils.view.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2014/11/4.
 */
public class InOutLayout extends RelativeLayout {
	// 动画
	private Animation animation;

	public InOutLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public InOutLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public InOutLayout(Context context) {
		super(context);
	}

	@Override
	protected void onAnimationEnd() {
		super.onAnimationEnd();
		if (this.animation instanceof InOutAnimation) {
			setVisibility(((InOutAnimation) animation).direction != InOutAnimation.Direction.OUT ?
					View.VISIBLE : View.GONE);
		}
	}

	@Override
	protected void onAnimationStart() {
		super.onAnimationStart();
		if (this.animation instanceof InOutAnimation) {
			setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void startAnimation(Animation animation) {
		super.startAnimation(animation);
		this.animation = animation;
		getRootView().postInvalidate();
	}
}
