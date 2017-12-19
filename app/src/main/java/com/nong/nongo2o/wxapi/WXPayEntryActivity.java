package com.nong.nongo2o.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.nong.nongo2o.AdventurerApp;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by Administrator on 2017-12-1.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AdventurerApp.wxApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        AdventurerApp.wxApi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.d(TAG, "onReq: " + baseReq);
    }

    @Override
    public void onResp(BaseResp baseResp) {
        int code = baseResp.errCode;
        switch (code) {
            case 0:
                Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction("updateOrderList");
                intent.setAction("paySuccess");
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                break;
            case -1:
                Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("payFail"));
                break;
            case -2:
                Toast.makeText(this, "支付取消", Toast.LENGTH_SHORT).show();
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("payCancel"));
                break;
            default:
                Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("payFail"));
                setResult(RESULT_OK);
                break;
        }
        finish();
    }
}
