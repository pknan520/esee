package com.nong.nongo2o.service;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.nong.nongo2o.entity.domain.App;
import com.nong.nongo2o.uils.AppManager;
import com.nong.nongo2o.uils.NetUtils;

import java.io.File;

/**
 * Created by Administrator on 2017-10-17.
 */

public class VersionUpdateHelper implements ServiceConnection {

    private Context context;
    private VersionUpdateService service;
    private AlertDialog waitForUpdateDialog;
    private ProgressDialog progressDialog;

    private static boolean isCanceled;

    private boolean showDialogOnStart;
    private boolean toastInfo;

    public static final int MUST_UPDATE = 3;
    public static final int NEED_UPDATE = 2;
    public static final int DONOT_NEED_UPDATE = 1;
    public static final int CHECK_FAILD = -1;
    public static final int USER_CANCELED = 0;

    private CheckCallBack checkCallBack;

    public interface CheckCallBack{
        void callBack(int code);
    }

    public VersionUpdateHelper(Context context) {
        this.context = context;
    }

    public void setCheckCallBack(CheckCallBack checkCallBack) {
        this.checkCallBack = checkCallBack;
    }

    public static void resetCancelFlag() {
        isCanceled = false;
    }

    /**
     * 设置非强制更新时,是否显示更新对话框
     *
     * @param showDialogOnStart
     */
    public void setShowDialogOnStart(boolean showDialogOnStart) {
        this.showDialogOnStart = showDialogOnStart;
    }

    /**
     * 是否吐司更新消息
     *
     * @param toastInfo
     */
    public void setToastInfo(boolean toastInfo) {
        this.toastInfo = toastInfo;
    }

    public void startUpdateVersion() {
        Log.d("VersionUpdateService", "startUpdateVersion");
        if (isCanceled)
            return;
        if (isWaitForUpdate() || isWaitForDownload()) {
            return;
        }
        if (service == null && context != null) {
            context.bindService(new Intent(context, VersionUpdateService.class), this, Context.BIND_AUTO_CREATE);
            Log.d("VersionUpdateService", "bindService");
        }
    }

    public void stopUpdateVersion() {
        Log.d("VersionUpdateService", "stopUpdateVersion");
        unBindService();
    }

    private void cancel() {
        isCanceled = true;
        unBindService();
    }

    private void unBindService() {
        if (isWaitForUpdate() || isWaitForDownload()) {
            return;
        }
        if (service != null && !service.isDownLoading()) {
            Log.d("VersionUpdateService", "unBindService");
            context.unbindService(this);
            service = null;
        }
    }

    private boolean isWaitForUpdate() {
        return waitForUpdateDialog != null && waitForUpdateDialog.isShowing();
    }

    private boolean isWaitForDownload() {
        return progressDialog != null && progressDialog.isShowing();
    }

    private void showNotWifiDownloadDialog() {
        final AlertDialog.Builder builer = new AlertDialog.Builder(context);
        builer.setTitle("下载新版本");
        builer.setMessage("检查到您的网络处于非wifi状态,下载新版本将消耗一定的流量,是否继续下载?");
        builer.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //exit app
                boolean mustUpdate = service.getAppInfo().isMustUpgrade();
                dialog.cancel();
                unBindService();
                if (mustUpdate) {
                    AppManager.getAppManager().AppExit(context);
                }
            }
        });
        builer.setPositiveButton("继续下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                service.doDownLoadTask();
            }
        });
        builer.setCancelable(false);
        builer.show();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        service = ((VersionUpdateService.LocalBinder) binder).getService();
        service.setCheckVersionCallBack(new VersionUpdateService.CheckVersionCallBack() {
            @Override
            public void onSuccess() {
                App appInfo = service.getAppInfo();

//                VersionUpdateEvent versionUpdateEvent = new VersionUpdateEvent();
//                versionUpdateEvent.setShowTips(versionUpdateModel.isNeedUpgrade());
//                EventBus.getDefault().postSticky(versionUpdateEvent);

                if (!appInfo.isNeedUpgrade()) {
                    if (toastInfo) {
                        Toast.makeText(context, "暂无新版本", Toast.LENGTH_SHORT).show();
                    }
                    if(checkCallBack != null){
                        checkCallBack.callBack(DONOT_NEED_UPDATE);
                    }
                    cancel();
                    return;
                }
//                if (!appInfo.isMustUpgrade() && !showDialogOnStart) {
//                    cancel();
//                    return;
//                }
                if(checkCallBack != null){
                    if (appInfo.isMustUpgrade())
                        checkCallBack.callBack(MUST_UPDATE);
                    else
                        checkCallBack.callBack(NEED_UPDATE);
                }
                final AlertDialog.Builder builer = new AlertDialog.Builder(context);
                builer.setTitle("版本升级");
                builer.setMessage(appInfo.getDetail());
                //当点确定按钮时从服务器上下载新的apk 然后安装
                builer.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (NetUtils.isWifi(context)) {
                            service.doDownLoadTask();
                        } else {
                            showNotWifiDownloadDialog();
                        }
                    }
                });

                //当点取消按钮时进行登录
                if (appInfo.isMustUpgrade()) {
                    builer.setNegativeButton("退出应用", (dialog, which) -> {
                        dialog.cancel();
                        cancel();
                        AppManager.getAppManager().AppExit(context);
                    });
                    builer.setCancelable(false);
                } else {
                    builer.setNegativeButton("稍后更新", (dialog, which) -> {
                        dialog.cancel();
                        cancel();
                        if(checkCallBack != null){
                            checkCallBack.callBack(USER_CANCELED);
                        }
                    });
                    builer.setCancelable(true);
                }
                waitForUpdateDialog = builer.create();
                waitForUpdateDialog.show();
            }

            @Override
            public void onError() {
                if (toastInfo) {
                    Toast.makeText(context, "检查失败,请检查网络设置", Toast.LENGTH_SHORT).show();
                }
                unBindService();
                if(checkCallBack != null){
                    checkCallBack.callBack(CHECK_FAILD);
                }
            }
        });

        service.setDownLoadListener(new VersionUpdateService.DownLoadListener() {
            @Override
            public void begain() {
                App appInfo = service.getAppInfo();
                if (appInfo.isMustUpgrade() || appInfo.isNeedUpgrade()) {
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("正在下载更新");
                    progressDialog.show();
                }
            }

            @Override
            public void inProgress(float progress, long total) {
                if (progressDialog != null) {
                    progressDialog.setMax(100);
                    progressDialog.setProgress((int) progress);
                }
            }

            @Override
            public void downLoadLatestSuccess(File file) {
                if (progressDialog != null)
                    progressDialog.cancel();
                service.setDownLoading(false);
                unBindService();
            }

            @Override
            public void downLoadLatestFailed() {
                if (progressDialog != null)
                    progressDialog.cancel();
                service.setDownLoading(false);
                unBindService();
            }
        });

        service.doCheckUpdateTask();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.cancel();

        if (waitForUpdateDialog != null && waitForUpdateDialog.isShowing())
            waitForUpdateDialog.cancel();

        if (service != null) {
            service.setDownLoadListener(null);
            service.setCheckVersionCallBack(null);
        }
        service = null;
        context = null;
    }
}
