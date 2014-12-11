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
						channelId);// ����й�����ʾ����ʾ�����Զ�������һ�����
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
			msg.what = tag; // ˽�о�̬�����ͱ����������������ж���ֵ
			handler.sendMessage(msg);
		}
	};

	private static Ckm pm;

	private void initPushAd() {
		layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		pm = Ckm.getInstance(AndroidLauncher.this);
		// ����cooId
		pm.setCooId(AndroidLauncher.this, cooID);//
		// ����channelId
		pm.setChannelId(AndroidLauncher.this, channelId);
		// ����push
		pm.receiveMessage(AndroidLauncher.this, true);

		// if (isConnect())
		// layout.addView(btn, 200, -2);
	}

	private void initMeidaAd() {
		// �ṩ��Ӧ�ص��ӿڣ����Բ����ã�����setListner����д��load����ǰ
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

	// Ӧ�û���Ϸ���Զ����֧������(ÿ��֧���������ݲ�����ͬ)
	String orderId = "";
	// �û���ʶ
	String userId = "";
	// ֧����Ʒ����
	String goodsName = "������";
	// ֧�����
	float price = 0.0f;
	// ֧��ʱ��
	String time = "";
	// ֧������
	String goodsDesc = "";
	// Ӧ�û���Ϸ�̷������˻ص��ӿڣ��޷������ɲ���д��
	String notifyUrl = "";

	private void initPay() {
		// ��ʼ��ͳ����(�������)
		PayConnect.getInstance(appID, appPid, this);
		userId = PayConnect.getInstance(AndroidLauncher.this).getDeviceId(
				AndroidLauncher.this);
	}

	static int addHeart = 0;
	private void pay(int money) {
		try {
			// ��Ϸ���Զ���֧�������ţ���֤�ö����ŵ�Ψһ�ԣ�������ִ��֧������ʱ�Ž��иö����ŵ�����
			orderId = System.currentTimeMillis() + "";
			switch (money) {
			case 1:
				goodsDesc = "�ؼ��Ż�,1Ԫ����45������";
				addHeart = 45;
				break;
			case 2:
				goodsDesc = "��ֵ�Żݣ�2Ԫ����105������";
				addHeart = 105;
				break;
			case 3:
				goodsDesc = "�콵����, 5Ԫ����300����";
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
	 * �Զ���Listenerʵ��PaySuccessListener�����ڼ���֧���ɹ�
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyPayResultListener implements PayResultListener {

		@Override
		public void onPayFinish(Context payViewContext, String orderId,
				int resultCode, String resultString, int payType, float amount,
				String goodsName) {
			// �ɸ���resultCode�����ж�
			if (resultCode == 0) {
				Toast.makeText(getApplicationContext(),
						resultString + "��" + amount + "Ԫ", Toast.LENGTH_LONG)
						.show();
				// ֧���ɹ�ʱ�رյ�ǰ֧������
				PayConnect.getInstance(AndroidLauncher.this).closePayView(
						payViewContext);

				// TODO �ڿͻ��˴���֧���ɹ��Ĳ���
				Assets.instance.heart += addHeart;
				addHeart = 0;

				// δָ��notifyUrl������£����׳ɹ��󣬱��뷢�ͻ�ִ
				PayConnect.getInstance(AndroidLauncher.this).confirm(orderId,
						payType);
			} else {
				Toast.makeText(getApplicationContext(), resultString,
						Toast.LENGTH_LONG).show();
			}
		}
	}

}
