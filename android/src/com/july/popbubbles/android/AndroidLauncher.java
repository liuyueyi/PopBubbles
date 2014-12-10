package com.july.popbubbles.android;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.july.popbubbles.BushEvent;
import com.july.popbubbles.Constants;
import com.july.popbubbles.MainGame;
import com.phkg.b.BannerView;
import com.pkag.m.MyMDListner;
import com.pkag.m.MyMediaManager;
import com.pmkg.p.Ckm;

public class AndroidLauncher extends AndroidApplication {
	LinearLayout layout;
	static String cooID = "486f195baefa4dfaab2a2687455d71b9";
	static String channelId = "k-sj91";

	BannerView bannerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPushAd();
		initMeidaAd();

		if (isConnect()) {
			bannerView = new BannerView(this);
			int width = this.getWindowManager().getDefaultDisplay().getWidth();
			layout.addView(bannerView, width, (int) (50 * width / 320f));
			bannerView.showBanner(cooID, channelId);
		}
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		View gameView = initializeForView(new MainGame(event), config);
		layout.addView(gameView);
		setContentView(layout);
		mContext = this;
	}

	private static Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case Constants.EXIT:
				pm.receiveMessage(mContext, true);
			case Constants.CHAPING:
				MyMediaManager.showExitInDialog((Activity) mContext, cooID,
						channelId);// 如果有广告就显示，显示完后会自动加载下一条广告
				break;
			}
		}
	};

	BushEvent event = new BushEvent() {
		@Override
		public void notify(Object o, int tag) {
			Message msg = handler.obtainMessage();
			msg.what = tag; // 私有静态的整型变量，开发者请自行定义值
			handler.sendMessage(msg);
		}
	};

	private static Ckm pm;

	private void initPushAd() {
		layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		pm = Ckm.getInstance(AndroidLauncher.this);
		// 设置cooId
		pm.setCooId(AndroidLauncher.this, cooID);//
		// 设置channelId
		pm.setChannelId(AndroidLauncher.this, channelId);
		// 接收push
		pm.receiveMessage(AndroidLauncher.this, true);

		// if (isConnect())
		// layout.addView(btn, 200, -2);
	}

	private void initMeidaAd() {
		// 提供相应回调接口，可以不调用，建议setListner方法写在load方法前
		MyMediaManager.setListner(new MyMDListner() {
			@Override
			public void onMDShow() {
				System.out.println("广告显示");// 这里可以进行停止游戏
			}

			@Override
			public void onMDClose() {
				System.out.println("广告关闭");
			}

			@Override
			public void onInstanll(int id) {
				System.out.println("广告安装id：" + id);
			}

			@Override
			public void onMDLoadSuccess() {
				System.out.println("广告加载成功");
				// MyMediaManager.showExitOutDialog(AndroidLauncher.this,
				// MyMediaManager.CENTER_CENTER, cooID, channelId);
				// MyMediaManager.show(AndroidLauncher.this);
			}

			@Override
			public void onMDExitInFinish() {
				System.out.println("内部退出框退出按钮回调");
				Gdx.app.exit();
				((Activity) mContext).finish();
			}

			@Override
			public void onMDExitOutFinish() {
				System.out.println("外部退出框退出按钮回调");
			}
		});
		MyMediaManager.load(AndroidLauncher.this, cooID, channelId);
	}

	private static Context mContext;

	private boolean isConnect() {
		try {
			ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			if (conn != null) {
				NetworkInfo info = conn.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			Log.v("error", e.toString());
		}
		return false;
	}
}
