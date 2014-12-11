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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.july.popbubbles.Assets;
import com.july.popbubbles.BushEvent;
import com.july.popbubbles.Constants;
import com.july.popbubbles.MainGame;
import com.pkag.m.MyMDListner;
import com.pkag.m.MyMediaManager;
import com.pmkg.p.Ckm;
import com.wanpu.pay.PayConnect;
import com.wanpu.pay.PayResultListener;

public class AndroidLauncher extends AndroidApplication {
	LinearLayout layout;
	static String cooID = "486f195baefa4dfaab2a2687455d71b9";
	static String channelId = "k-sj91";
	static String appID = "0d73a92e277f705d609fb3a7546437ff";
	static String appPid = "waps";

//	BannerView bannerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPushAd();
		initMeidaAd();

		// if (isConnect()) {
//		bannerView = new BannerView(this);
//		int width = this.getWindowManager().getDefaultDisplay().getWidth();
//		layout.addView(bannerView, width, 50);
//		bannerView.showBanner(cooID, channelId);
		// }
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		View gameView = initializeForView(new MainGame(event), config);
		layout.addView(gameView);
		setContentView(layout);
		mContext = this;

		initPay();
	}

	@Override
	public void onDestroy() {
		PayConnect.getInstance(mContext).close();
		super.onDestroy();
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
			case Constants.PAY1:
				((AndroidLauncher) mContext).pay(1);
				break;
			case Constants.PAY2:
				((AndroidLauncher) mContext).pay(2);
				break;
			case Constants.PAY3:
				((AndroidLauncher) mContext).pay(5);
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
			}

			@Override
			public void onMDClose() {
			}

			@Override
			public void onInstanll(int id) {
			}

			@Override
			public void onMDLoadSuccess() {
				// MyMediaManager.showExitOutDialog(AndroidLauncher.this,
				// MyMediaManager.CENTER_CENTER, cooID, channelId);
				// MyMediaManager.show(AndroidLauncher.this);
			}

			@Override
			public void onMDExitInFinish() {
				Gdx.app.exit();
				((Activity) mContext).finish();
			}

			@Override
			public void onMDExitOutFinish() {
			}
		});
		MyMediaManager.load(AndroidLauncher.this, cooID, channelId);
	}

	private static Context mContext;

//	private boolean isConnect() {
//		try {
//			ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//			if (conn != null) {
//				NetworkInfo info = conn.getActiveNetworkInfo();
//				if (info != null && info.isConnected()) {
//					if (info.getState() == NetworkInfo.State.CONNECTED) {
//						return true;
//					}
//				}
//			}
//		} catch (Exception e) {
//			Log.v("error", e.toString());
//		}
//		return false;
//	}

	// 应用或游戏商自定义的支付订单(每条支付订单数据不可相同)
	String orderId = "";
	// 用户标识
	String userId = "";
	// 支付商品名称
	String goodsName = "购买爱心";
	// 支付金额
	float price = 0.0f;
	// 支付时间
	String time = "";
	// 支付描述
	String goodsDesc = "";
	// 应用或游戏商服务器端回调接口（无服务器可不填写）
	String notifyUrl = "";

	private void initPay() {
		// 初始化统计器(必须调用)
		PayConnect.getInstance(appID, appPid, this);
		userId = PayConnect.getInstance(AndroidLauncher.this).getDeviceId(
				AndroidLauncher.this);
	}

	static int addHeart = 0;
	private void pay(int money) {
		try {
			// 游戏商自定义支付订单号，保证该订单号的唯一性，建议在执行支付操作时才进行该订单号的生成
			orderId = System.currentTimeMillis() + "";
			switch (money) {
			case 1:
				goodsDesc = "特价优惠,1元购买45个爱心";
				addHeart = 45;
				break;
			case 2:
				goodsDesc = "超值优惠，2元购买105个爱心";
				addHeart = 105;
				break;
			case 3:
				goodsDesc = "天降福利, 5元购买300爱心";
				addHeart = 300;
				break;
			}
			price = money;
			PayConnect.getInstance(AndroidLauncher.this).pay(
					AndroidLauncher.this, orderId, userId, price, goodsName,
					goodsDesc, notifyUrl, new MyPayResultListener());

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 自定义Listener实现PaySuccessListener，用于监听支付成功
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyPayResultListener implements PayResultListener {

		@Override
		public void onPayFinish(Context payViewContext, String orderId,
				int resultCode, String resultString, int payType, float amount,
				String goodsName) {
			// 可根据resultCode自行判断
			if (resultCode == 0) {
				Toast.makeText(getApplicationContext(),
						resultString + "：" + amount + "元", Toast.LENGTH_LONG)
						.show();
				// 支付成功时关闭当前支付界面
				PayConnect.getInstance(AndroidLauncher.this).closePayView(
						payViewContext);

				// TODO 在客户端处理支付成功的操作
				Assets.instance.heart += addHeart;
				addHeart = 0;

				// 未指定notifyUrl的情况下，交易成功后，必须发送回执
				PayConnect.getInstance(AndroidLauncher.this).confirm(orderId,
						payType);
			} else {
				Toast.makeText(getApplicationContext(), resultString,
						Toast.LENGTH_LONG).show();
			}
		}
	}

}
