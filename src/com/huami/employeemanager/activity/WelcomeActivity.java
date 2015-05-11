package com.huami.employeemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;

import com.huami.employeemanager.base.AppManager;
import com.huami.employeemanager.base.BaseActivity;
import com.huami.employeemanager.base.BaseApplication;
import com.huami.employeemanager.ui.IpSetDialog;
import com.huami.employeemanager.activity.R;

/**
 * 欢迎页
 * 
 * @author NikolaQian
 *
 */
public class WelcomeActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.welcome_activity, null);
		setContentView(view);
		
		FrameLayout frameLayout = (FrameLayout)findViewById(R.id.welcome_frame);
		
		BaseApplication application = (BaseApplication)getApplication();
		
		if(application.isTablet()){
			frameLayout.setBackgroundResource(R.drawable.wecome_tablet_background);
		}else {
			frameLayout.setBackgroundResource(R.drawable.welcome_background);
		}
		
		//渐变启动
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
		
		//持续时间
		alphaAnimation.setDuration(2000);
		
		alphaAnimation.setAnimationListener(new MyAnimationListener());
		
		view.setAnimation(alphaAnimation);
	}
	
	private class MyAnimationListener implements AnimationListener{

		@Override
		public void onAnimationEnd(Animation arg0) {
			String ipAdress = application.getIpAdress();
			
//			if(ipAdress.equals("0.0.0.0")){
//				Intent intent = new Intent(WelcomeActivity.this, IpSetDialog.class);
//				intent.putExtra(IpSetDialog.IS_STRAT_MAIN, true);
//				startActivity(intent);
//			}else {
//				startActivity(MainActivity.class);
//			}
			
			Intent intent = new Intent(WelcomeActivity.this, IpSetDialog.class);
			intent.putExtra(IpSetDialog.IS_STRAT_MAIN, true);
			startActivity(intent);
			
			AppManager.getAppManager().finishActivity(WelcomeActivity.this);
		}

		@Override
		public void onAnimationRepeat(Animation arg0) {
			
		}

		@Override
		public void onAnimationStart(Animation arg0) {
			
		}
		
	}
	
}
