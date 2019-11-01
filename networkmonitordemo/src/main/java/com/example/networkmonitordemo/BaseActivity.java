package com.example.networkmonitordemo;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.networkmonitordemo.utils.NetBroadcastReceiver;
import com.example.networkmonitordemo.utils.NetUtil;
import com.example.networkmonitordemo.utils.T;
import com.example.networkmonitordemo.utils.dialog.MyAlertDialog;
/**
 * @Author: 薛志辉
 * @Date: 2019/11/1 14:03
 * @Description: 2531295581
 */
public abstract class BaseActivity  extends AppCompatActivity implements NetBroadcastReceiver.NetChangeListener {
    public static NetBroadcastReceiver.NetChangeListener listener;
    private MyAlertDialog alertDialog=null;
    private NetBroadcastReceiver netBroadcastReceiver;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //全部禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        listener = this;
        //Android 7.0以上需要动态注册
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //实例化IntentFilter对象
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            netBroadcastReceiver = new NetBroadcastReceiver();
            //注册广播接收
            registerReceiver(netBroadcastReceiver, filter);
        }
        checkNet();
        initView();
        initData();
    }

    /**
     * 网络类型
     */
    private int netType;

    protected abstract int initLayout();

    protected abstract void initView();

    protected abstract void initData();
    /**
     * 网络变化之后的类型
     */
    @Override
    public void onChangeListener(int status) {
        // TODO Auto-generated method stub
        this.netType = status;
        Log.i("netType", "netType:" + status);
        if (!isNetConnect()) {
            showNetDialog();
            T.showShort("网络异常，请检查网络，哈哈");
        } else {
            hideNetDialog();
            T.showShort("网络恢复正常");
        }
    }

    /**
     * 隐藏设置网络框
     */
    private void hideNetDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        alertDialog = null;
    }
    /**
     * 初始化时判断有没有网络
     */
    public boolean checkNet() {
        this.netType = NetUtil.getNetWorkState(BaseActivity.this);
        if (!isNetConnect()) {
            //网络异常，请检查网络
            showNetDialog();
            T.showShort("网络异常，请检查网络，哈哈");
        }
        return isNetConnect();
    }
    /**
     * 判断有无网络 。
     *
     * @return true 有网, false 没有网络.
     */
    public boolean isNetConnect() {
        if (netType == 1) {
            return true;
        } else if (netType == 0) {
            return true;
        } else if (netType == -1) {
            return false;
        }
        return false;
    }

    /**
     * 弹出设置网络框
     */
    private void showNetDialog() {
        if (alertDialog == null) {
            alertDialog = new MyAlertDialog(this).builder().setTitle("网络异常")
                    .setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).setPositiveButton("设置", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startActivity(intent);
                        }
                    }).setCancelable(false);
        }
        alertDialog.show();
        showMsg("网络异常，请检查网络");
    }
    public void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
