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
						channelId);// ����й�����ʾ����ʾ�����Զ�������һ�����
				break;
			case Constants.CHAPING:
				MyMediaManager.show((Activity) mContext);
				break;
			case Constants.SIGN:
				Toast.makeText(mContext, "����5������", Toast.LENGTH_SHORT).show();
				break;
			case Constants.INSTALL:
				System.out.println("��װ�ɹ�����������");
				Assets.instance.heart += 30;
				Toast.makeText(mContext, "����30������", Toast.LENGTH_LONG).show();
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
				event.notify(this, Constants.INSTALL);
			}

			@Override
			public void onMDLoadSuccess() {
				System.out.println("�����سɹ�");
				// ����ʵ�ֹ��������������ʾ��棬�����÷����Բ鿴�����ĵ��ġ���������ʱ������
				// ��������ο������ĵ�����˵����Context context
			}

			@Override
			public void onMDExitInFinish() {
				System.out.println("�ڲ��˳����˳���ť�ص�");
			}

			@Override
			public void onMDExitOutFinish() {
				System.out.println("�ⲿ�˳����˳���ť�ص�");
			}
		});

		// ������
		// ��������ο������ĵ�����˵����Context context,String cooId,String channelId
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
