package com.july.popbubbles.android;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.july.popbubbles.Assets;
import com.july.popbubbles.BushEvent;
import com.july.popbubbles.Constants;
import com.july.popbubbles.MainGame;
import com.phkg.b.BannerView;
import com.phkg.b.MyBMDevListner;
import com.pkag.m.MyMDListner;
import com.pkag.m.MyMediaManager;

public class AndroidLauncher extends AndroidApplication implements
		MyBMDevListner {
	static RelativeLayout layout;
	static Context mContext;
	static String cooID = "486f195baefa4dfaab2a2687455d71b9";
	static String channelId = "k-sj91";

	private static Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case Constants.EXIT:
				MyMediaManager.showExitInDialog((Activity) mContext, cooID,
						channelId);// 如果有广告就显示，显示完后会自动加载下一条广告
				break;
			case Constants.CHAPING:
				MyMediaManager.show((Activity) mContext);
				break;
			case Constants.SIGN:
				Toast.makeText(mContext, "奖励5个爱心", Toast.LENGTH_SHORT).show();
				break;
			case Constants.INSTALL:
				System.out.println("安装成功，奖励红心");
				Assets.instance.heart += 30;
				Toast.makeText(mContext, "奖励30个爱心", Toast.LENGTH_LONG).show();
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

	BannerView bannerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		initAd();
		layout = new RelativeLayout(this);
		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT, 50);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);

		bannerView = new BannerView(this);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		View gameView = initializeForView(new MainGame(event), config);
		layout.addView(gameView);
		layout.addView(bannerView, adParams);
		bannerView.showBanner(cooID, channelId);
		setContentView(layout);
	}

	private void initAd() {
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
				event.notify(this, Constants.INSTALL);
			}

			@Override
			public void onMDLoadSuccess() {
				System.out.println("广告加载成功");
				// 这里实现广告加载完成立即显示广告，更多用法可以查看开发文档的“方法调用时机”；
				// 具体参数参考开发文档参数说明：Context context
			}

			@Override
			public void onMDExitInFinish() {
				System.out.println("内部退出框退出按钮回调");
			}

			@Override
			public void onMDExitOutFinish() {
				System.out.println("外部退出框退出按钮回调");
			}
		});

		// 请求广告
		// 具体参数参考开发文档参数说明：Context context,String cooId,String channelId
		MyMediaManager.load(AndroidLauncher.this, cooID, channelId);
	}

	@Override
	public void onInstall(int arg0) {
		// TODO Auto-generated method stub
		event.notify(this, Constants.INSTALL);
	}

	@Override
	public void onShowBanner() {
		// TODO Auto-generated method stub

	}
}
