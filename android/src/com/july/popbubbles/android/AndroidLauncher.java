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
						channelId);// ����й�����ʾ����ʾ�����Զ�������һ�����
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
				System.out.println("�����ʾ");// ������Խ���ֹͣ��Ϸ
			}

			@Override
			public void onMDClose() {
				System.out.println("���ر�");
			}

			@Override
			public void onInstanll(int id) {
				System.out.println("��氲װid��" + id);
			}

			@Override
			public void onMDLoadSuccess() {
				System.out.println("�����سɹ�");
				// MyMediaManager.showExitOutDialog(AndroidLauncher.this,
				// MyMediaManager.CENTER_CENTER, cooID, channelId);
				// MyMediaManager.show(AndroidLauncher.this);
			}

			@Override
			public void onMDExitInFinish() {
				System.out.println("�ڲ��˳����˳���ť�ص�");
				Gdx.app.exit();
				((Activity) mContext).finish();
			}

			@Override
			public void onMDExitOutFinish() {
				System.out.println("�ⲿ�˳����˳���ť�ص�");
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
