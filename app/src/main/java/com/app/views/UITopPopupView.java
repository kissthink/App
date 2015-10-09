package com.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.tnw.R;

/**
 * @description 从顶部划出一个view
 * @author JiaBF
 * @date 2014-5-16 下午2:33:36
 * @update 2014-5-16
 * @version V1.0
 */
public class UITopPopupView extends FrameLayout{
	private Animation animbottomOut,animBottomIn;
	private FrameLayout fayContentLayout,alPhaFrameLayout;
	private boolean isShow = false;
	private boolean isTouch = true;
	private boolean showing = false;
	private boolean isShowBg = true;
	private View parentView;
	private int duration = 0;
	
	
	public UITopPopupView(Context context) {
		super(context);
	}
	
	
	public UITopPopupView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * @description  设置弹出框内容 
	 * @param @param contentView 
	 * @return void 
	 * @author jiaBF
	 */
	public void setContentView(View contentView)
	{
		this.parentView = contentView;
		init();
	}
	
	private void init()
	{
		LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.pop_top_layout, null);
		addView(view);
		parentView.setVisibility(View.GONE);
		fayContentLayout = (FrameLayout) findViewById(R.id.fayContentLayout);
		fayContentLayout.addView(parentView);
		//顶部栏滑出
		animbottomOut = AnimationUtils.loadAnimation(getContext(), R.anim.push_top_out);
		//动画不恢复原
		animbottomOut.setFillAfter(true);
		animbottomOut.setAnimationListener(new AnimListener());
		//底部进入动画
		animBottomIn = AnimationUtils.loadAnimation(getContext(), R.anim.push_top_in);
		
		//动画不恢复原 
		animBottomIn.setFillAfter(true);
		animBottomIn.setAnimationListener(new AnimListener());
		if(0!=duration)
		{
			animbottomOut.setDuration(duration);
			animBottomIn.setDuration(duration);
		}
		
		alPhaFrameLayout = (FrameLayout) findViewById(R.id.alPhaFrameLayout);
		if(!isShowBg)
		{
			alPhaFrameLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		}
		alPhaFrameLayout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				if(!AppViewUtil.constansPoint(fayContentLayout,(int)event.getRawX(),(int)event.getRawY())  && isTouch)
				{
					isTouch = false;
					hiden();
				}
				return true;
			}
		});
	}
	
	/**
	 * @description 显示弹出框
	 * @param  
	 * @return void 
	 * @author jiaBF
	 */
	public void show()
	{
		if(parentView.getVisibility() == View.GONE && !isShow)
		{
			showing = true;
			isShow = true;
			isTouch = true;
			fayContentLayout.startAnimation(animBottomIn);
			parentView.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * @description 隐藏弹出框 
	 * @param  
	 * @return void 
	 * @author jiaBF
	 */
	public void hiden()
	{
		if(!showing)
		{
			return ;
		}
		showing = false;
		isShow = false;
		fayContentLayout.startAnimation(animbottomOut);
	}
	
	
	private class AnimListener implements AnimationListener{
		@Override
		public void onAnimationStart(Animation animation) 
		{
			if (isShow) 
			{
				fayContentLayout.setVisibility(View.VISIBLE);
				alPhaFrameLayout.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onAnimationEnd(Animation animation) 
		{
			if(isShow)
			{
				isShow = !isShow;
				if(null != popupVisiableListener)
				{
					popupVisiableListener.showState(true);
				}
			}
			else
			{
				parentView.setVisibility(View.GONE);
				fayContentLayout.setVisibility(View.GONE);
				alPhaFrameLayout.setVisibility(View.GONE);
				if(null != popupVisiableListener)
				{
					popupVisiableListener.showState(false);
				}
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {}
	}
	
	/**
	 * @description 返回时调用，用于返回键，如果没有关闭则关闭弹出框，返回true,表示拦截返回键事件 
	 * @param @return 
	 * @return boolean 
	 * @author jiaBF
	 */
	public boolean back()
	{
		if(isShow())
		{
			hiden();
			return true;
		}
		return false;
	}
	
	private IPopupVisiableListener popupVisiableListener;
	public interface IPopupVisiableListener{
		/**
		 * 显示状态
		 */
		public void showState(boolean isShow);
	}

	
	public boolean isShow()
	{
		return parentView.getVisibility() == View.VISIBLE;
	}


	public void setShowBg(boolean isShowBg) 
	{
		this.isShowBg = isShowBg;
	}


	public void setDuration(int duration) {
		this.duration = duration;
	}


	public void setPopupVisiableListener(
			IPopupVisiableListener popupVisiableListener) {
		this.popupVisiableListener = popupVisiableListener;
	}
	
}
