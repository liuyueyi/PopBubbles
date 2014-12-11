package com.july.popbubbles.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.wanpu.pay.PayConnect;
import com.wanpu.pay.PayResultListener;

public class AndroidLauncher extends AndroidApplication {
	LinearLayout layout;
	static String appID = "0d73a92e277f705d609fb3a7546437ff";
	static String appPid = "91";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		layout = new LinearLayout(this);
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
				// �����˳��Ի���
				AlertDialog isExit = new AlertDialog.Builder(mContext).create();
				// ���öԻ������
				isExit.setTitle("ϵͳ��ʾ");
				// ���öԻ�����Ϣ
				isExit.setMessage("�˳���Ϸ");
				// ���ѡ��ť��ע�����
				isExit.setButton("�����뿪", listener);
				isExit.setButton2("����һ��", listener);
				// ��ʾ�Ի���
				isExit.show();
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

	private static Context mContext;


	/** �����Ի��������button����¼� */
	static DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "ȷ��"��ť�˳�����
				Gdx.app.exit();
				((Activity) mContext).finish();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "ȡ��"�ڶ�����ťȡ���Ի���
				break;
			default:
				break;
			}
		}
	};
	
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
