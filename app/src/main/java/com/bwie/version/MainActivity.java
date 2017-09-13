package com.bwie.version;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.bwie.version.bean.Version;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private String url="http://125.39.134.47/r/a.gdown.baidu.com/data/wisegame/7c28ac069399b336/kuaishou_4812.apk";
    private ProgressDialog dialog;
    private Callback.Cancelable cancelable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initeview();
    }

    private void initeview() {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //取消下载,取消网络请求
                cancelable.cancel();
            }
        });
    }

    public void load(View v) throws PackageManager.NameNotFoundException {

        //第一步，先拿到本App的版本号


            PackageManager manager=getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            int versionCode=info.versionCode;//获取本app的版本号



        //第二步：进行网络请求，请求版本对象信息（解析json字符串转化为本地version对象）
        //由于没有服务器，这里用本地模拟数据库请求的数据

        Version version=new Version();
        version.setUrl(url);

        //第三步：比较，如果服务器版本号大于本app版本号，那么下载文件，下载完成后，进行安装
        if(versionCode < version.getVersionCode()){
            File file=new File(Environment.getExternalStorageDirectory()+"/zqj/kuaishou.apk");

     //       if(file != null && file.exists()){

                //走安装逻辑

     //       }else{
                //下载逻辑
                download();
       //     }

        }

    }

    private void download() {

        RequestParams params=new RequestParams(url);
        params.setAutoResume(true);//设置是否支持断点下载
        params.setCancelFast(true);//设置是否立即取消

        //判断sdcard是否可用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//判断sdcard存在并且可用


            params.setSaveFilePath(Environment.getExternalStorageDirectory()+"/zqj/kuaishou.apk");
        }
                                                                    //返回一个文件格式
        cancelable = x.http().get(params, new Callback.ProgressCallback<File>() {//此时，用Progress返回数据接口，因为我们要设置进度条
            @Override
            public void onSuccess(File result) {//下载成功的时候
                //进度条消失
                dialog.dismiss();
                //调用系统的安装程序，进行安装
                install(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {//下载开始
                dialog.show();//开始展示进度条

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

                //设置进度条的进度
                if (isDownloading) {//看是否在下载
                    System.out.println("xxxxx"+(int) total);
                    System.out.println("xxxxx"+(int) current);
                    dialog.setMax((int) total);
                    dialog.setProgress((int) current);
                    dialog.setTitle("下载进度");

                }
            }
        });


    }
    /**
     * 安装新版本
     */
    private void install(File file) {
        //调用系统安装器
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + file.getAbsolutePath()), "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
