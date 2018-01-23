package com.nong.nongo2o.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.nong.nongo2o.R;
import com.nong.nongo2o.entity.domain.App;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiConstants;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.nong.nongo2o.uils.AppUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.progressmanager.ProgressListener;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.progressmanager.body.ProgressInfo;

/**
 * Created by Administrator on 2017-10-17.
 */

public class VersionUpdateService extends Service {

    private LocalBinder binder = new LocalBinder();

    private DownLoadListener downLoadListener;
    private boolean downLoading;
    private int progress;

    private NotificationManager mNotificationManager;
    private NotificationUpdaterThread notificationUpdaterThread;
    private Notification.Builder notificationBuilder;
    private final int NOTIFICATION_ID = 100;

    private App appInfo;

    public VersionUpdateService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setDownLoadListener(null);
        setCheckVersionCallBack(null);
        stopDownLoadForground();
        if (mNotificationManager != null)
            mNotificationManager.cancelAll();
        downLoading = false;
    }

    public interface DownLoadListener {
        void begain();

        void inProgress(float progress, long total);

        void downLoadLatestSuccess(File file);

        void downLoadLatestFailed();
    }

    public interface CheckVersionCallBack {
        void onSuccess();

        void onError();
    }

    private CheckVersionCallBack checkVersionCallBack;

    public void setCheckVersionCallBack(CheckVersionCallBack checkVersionCallBack) {
        this.checkVersionCallBack = checkVersionCallBack;
    }

    private class NotificationUpdaterThread extends Thread {
        @Override
        public void run() {
            while (true) {
                notificationBuilder.setContentTitle("正在下载更新" + progress + "%"); // the label of the entry
                notificationBuilder.setProgress(100, progress, false);
                mNotificationManager.notify(NOTIFICATION_ID, notificationBuilder.getNotification());
                if (progress >= 100) {
                    break;
                }
            }
        }
    }

    public boolean isDownLoading() {
        return downLoading;
    }

    public void setDownLoading(boolean downLoading) {
        this.downLoading = downLoading;
    }

    /**
     * 让Service保持活跃,避免出现:
     * 如果启动此服务的前台Activity意外终止时Service出现的异常(也将意外终止)
     */
    private void starDownLoadForground() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = "下载中,请稍后...";
        // The PendingIntent to launch our activity if the user selects this notification
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, MainActivity.class), 0);
        notificationBuilder = new Notification.Builder(this);
        notificationBuilder.setSmallIcon(R.mipmap.logo);  // the status icon
        notificationBuilder.setTicker(text);  // the status text
        notificationBuilder.setWhen(System.currentTimeMillis());  // the time stamp
        notificationBuilder.setContentText(text);  // the contents of the entry
//        notificationBuilder.setContentIntent(contentIntent);  // The intent to send when the entry is clicked
        notificationBuilder.setContentTitle("正在下载更新" + 0 + "%"); // the label of the entry
        notificationBuilder.setProgress(100, 0, false);
        notificationBuilder.setOngoing(true);
        notificationBuilder.setAutoCancel(true);
        Notification notification = notificationBuilder.getNotification();
        startForeground(NOTIFICATION_ID, notification);
    }

    private void stopDownLoadForground() {
        stopForeground(true);
    }

    public void doCheckUpdateTask() {
        //  检查版本
        RetrofitHelper.getAppAPI()
                .checkVersion()
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (resp.getRows() != null && !resp.getRows().isEmpty()) {
                        appInfo = resp.getRows().get(0);

                        if (appInfo != null) {
                            appInfo.setNeedUpgrade(AppUtils.getVersionCode(this) < appInfo.getNewVersion());
                            appInfo.setMustUpgrade(AppUtils.getVersionCode(this) < appInfo.getMinVersion());
                        }

                        if (checkVersionCallBack != null) checkVersionCallBack.onSuccess();
                    }
                }, throwable -> {
                    Toast.makeText(this, "获取版本信息失败", Toast.LENGTH_SHORT).show();
                    if (checkVersionCallBack != null) checkVersionCallBack.onError();
                });
    }

    public void doDownLoadTask() {
//        if (mNotificationManager == null)
//            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        starDownLoadForground();
//
//        notificationUpdaterThread = new NotificationUpdaterThread();
//        notificationUpdaterThread.start();

        String path = Environment.getExternalStorageDirectory().getPath() + "/download";
        String fileName = "ease.apk";

        downLoading = true;

        if (downLoadListener != null) {
            downLoadListener.begain();
        }

        String downloadUrl = appInfo.getUrl();
        Log.d("Update", "downloadUrl: " + downloadUrl);

        ProgressManager.getInstance().addResponseListener(downloadUrl, new ProgressListener() {
            @Override
            public void onProgress(ProgressInfo progressInfo) {
                if (downLoadListener != null) {
                    downLoadListener.inProgress(progressInfo.getPercent(), progressInfo.getContentLength());
                    progress = progressInfo.getPercent();
                }
            }

            @Override
            public void onError(long id, Exception e) {

            }
        });

        RetrofitHelper.getFileAPI()
                .downloadAPK(downloadUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(responseBody -> {
                    Log.e("Update", "ResponseBody Size: " + responseBody.contentLength());
                    InputStream is = responseBody.byteStream();

                    File file = new File(path);
                    if (!file.mkdirs()) file.mkdirs();
                    File apkFile = new File(path + "/" + fileName);
                    if (apkFile.exists()) apkFile.delete();

                    FileOutputStream fos = new FileOutputStream(apkFile);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[1024];

                    int len;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.flush();
                    fos.close();
                    bis.close();
                    is.close();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBody -> {
                    downLoading = false;
                    if (mNotificationManager != null) mNotificationManager.cancelAll();

                    File file = new File(path + "/" + fileName);
                    if (file.exists()) {
                        if (downLoadListener != null) downLoadListener.downLoadLatestSuccess(file);
                        installApk(file, this);
                    }
                }, throwable -> {
                    downLoading = false;
                    if (mNotificationManager != null) mNotificationManager.cancelAll();
                    if (downLoadListener != null) downLoadListener.downLoadLatestFailed();
                    Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public App getAppInfo() {
        return appInfo;
    }

    public void setDownLoadListener(DownLoadListener downLoadListener) {
        this.downLoadListener = downLoadListener;
    }

    //安装apk
    public void installApk(File file, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //执行的数据类型
        Uri data;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 判断版本大于等于7.0
            // "com.nong.nongo2o.updateprovider"即是在清单文件中配置的authorities
            data = FileProvider.getUriForFile(context, "com.nong.nongo2o.updateprovider", file);
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(file);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        public VersionUpdateService getService() {
            return VersionUpdateService.this;
        }
    }

}
