package com.liyujie.portencapsulation.utils;

import android.content.Context;

import com.blankj.utilcode.util.ToastUtils;
import com.dyhdyh.support.countdowntimer.CountDownTimerSupport;
import com.dyhdyh.support.countdowntimer.OnCountDownTimerListener;
import com.kongqw.serialportlibrary.SerialPortManager;
import com.kongqw.serialportlibrary.listener.OnOpenSerialPortListener;
import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener;
import com.liyujie.portencapsulation.R;
import com.liyujie.portencapsulation.connector.Port;
import com.liyujie.portencapsulation.dialog.LoadDialog;

import java.io.File;


public class OpenPortUtils {
    private static LoadDialog loadDialog;
    public static SerialPortManager mSerialPortManager;
    private static CountDownTimerSupport mTimer = new CountDownTimerSupport(2000, 1000);
    public static void initSerial() {
        mSerialPortManager = new SerialPortManager();
        mSerialPortManager.setOnOpenSerialPortListener(new OnOpenSerialPortListener() {
            @Override
            public void onSuccess(File file) {
                ToastUtils.showShort(String.format("串口 [%s] 打开成功", new File("/dev/ttyS0")));
            }

            @Override
            public void onFail(File file, Status status) {
                switch (status) {
                    case NO_READ_WRITE_PERMISSION:
                        ToastUtils.showShort("没有读写权限");
                        break;
                    case OPEN_FAIL:
                    default:
                        ToastUtils.showShort("串口打开失败");
                        break;
                }
            }
        });
    }

    /**
     * 打开串口
     * @param door
     * @param port
     */
    public static void Transmit(Context mContext,String door, final Port port){
        mSerialPortManager.setOnSerialPortDataListener(new OnSerialPortDataListener() {
            @Override
            public void onDataReceived(byte[] bytes) {
                if(null != mTimer){
                    mTimer.reset();
                    mTimer.stop();
                }
                cancelProgressDialog();
                port.onDataSuccess(bytes);

            }

            @Override
            public void onDataSent(byte[] bytes) {
                ToastUtils.showShort("正在打开门");
            }
        });

        mTimer.setOnCountDownTimerListener(new OnCountDownTimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                port.onDataFailure();
                cancelProgressDialog();
            }
        });

        if(null != mTimer){
            mTimer.start();
        }
        showProgressDialog(mContext);
        byte[] sendContentBytes = new byte[6];
        sendContentBytes[0] = Byte.valueOf(door); //设备地址
        sendContentBytes[1] = 0;//命令
        sendContentBytes[2] = 1; //桢序列
        sendContentBytes[3] = 1;////数据长度
        sendContentBytes[4] =  Byte.valueOf(door); //数据在和
        sendContentBytes[5] =(byte)( 0 - sendContentBytes[0] - sendContentBytes[1] - sendContentBytes[2] - sendContentBytes[3] - sendContentBytes[4]); //校验
        mSerialPortManager.sendBytes(sendContentBytes);
    }


    /**
     * 显示加载框
     */
    public static void showProgressDialog(Context mContext) {
        loadDialog = new LoadDialog(mContext, R.style.dialog_style);
        if(null != loadDialog && !loadDialog.isShowing()){
            loadDialog.show();
        }
    }
    /**
     * 关闭加载框
     */

    public static void cancelProgressDialog() {
        if (loadDialog != null) {
            loadDialog.dismiss();
        }
    }

}
