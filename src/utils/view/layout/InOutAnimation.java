package utils.view.layout;

import android.view.View;
import android.view.animation.AnimationSet;

/**
 * 主页上的加好按钮旋转，可以不用这个实现
 *
 * Created by Administrator on 2014/11/4.
 */
public abstract class InOutAnimation extends AnimationSet {

	public Direction	direction;

	public enum Direction {
		IN, OUT
	}

	public InOutAnimation(Direction direction, long l, View[] views) {
		super(true);
		this.direction = direction;
		switch (this.direction) {
			case IN:
				addInAnimation(views);
				break;
			case OUT:
				addOutAnimation(views);
				break;
		}
		setDuration(l);
	}

	protected abstract void addInAnimation(View[] views);

	protected abstract void addOutAnimation(View[] views);
}
