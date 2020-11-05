package com.example.crosspromotesdk.util;

import android.content.Context;
import android.util.Log;

import com.example.crosspromotesdk.BuildConfig;
import com.example.crosspromotesdk.data.AppPreference;
import com.example.crosspromotesdk.data.BackUpManager;
import com.example.crosspromotesdk.network.Apiservice;
import com.example.crosspromotesdk.network.Networkmodule;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class CrossPromote {

    private static final int TYPE_REQUEST = 1;
    private static final int TYPE_VIEW = 2;
    private static final int TYPE_CLICK = 3;
    private static CrossPromote sInstance;
    private static String PACKAGE_NAME;
    private Networkmodule netWorkModule;
    private Apiservice apiService;
    private ArrayList<BackUpManager> listBackupManager;
    private CompositeDisposable mCompositeDisposable;
    private ArrayList<BackUpManager> listApiBackup;
    private Context mContext;
    public CrossPromote(Context context) {
        mContext = context;
        netWorkModule = new Networkmodule(context, "https://smatrix.io/");
        apiService = netWorkModule.provideRetrofit();
        mCompositeDisposable = new CompositeDisposable();
        listBackupManager = new ArrayList<>();
        listApiBackup = new ArrayList<>();
        listBackupManager = AppPreference.getInstance(mContext).getListApiBackup();
        if (listBackupManager != null && listBackupManager.size() > 0) {
            backupApi();
        }
    }

    public static void init(Context context) {

        if (context != null) {
            init(context, context.getPackageName());
        }
    }

    public static void init(Context context, String packageName) {
        PACKAGE_NAME = packageName;
        sInstance = new CrossPromote(context);
    }

    public static CrossPromote getInstance() {
        return sInstance;
    }

    public Apiservice getApiService() {
        return apiService;
    }

    public void setApiService(Apiservice apiService) {
        this.apiService = apiService;
    }

    public String getPackageName() {
        return PACKAGE_NAME;
    }

    public void setPackageName(String packageName) {
        PACKAGE_NAME = packageName;
    }

    public void backupApi() {
        for (int i = 0; i < listBackupManager.size(); i++) {
            if (listBackupManager.get(i).getType() == TYPE_REQUEST) {
                postRequetAds(listBackupManager.get(i).getApiMiss(), listBackupManager.get(i).getType());
                listBackupManager.remove(listBackupManager.get(i));
                i--;
            } else if (listBackupManager.get(i).getType() == TYPE_VIEW) {
                postViewAds(listBackupManager.get(i).getApiMiss(), listBackupManager.get(i).getType());
                listBackupManager.remove(listBackupManager.get(i));
                i--;
            } else if (listBackupManager.get(i).getType() == TYPE_CLICK) {
                postClickAds(listBackupManager.get(i).getApiMiss(), listBackupManager.get(i).getType());
                listBackupManager.remove(listBackupManager.get(i));
                i--;
            }
        }
    }

    public void postRequetAds(HashMap<String, String> param, int type) {
        listApiBackup.add(new BackUpManager(param, type));
        Disposable disposable = getApiService().postRequestAds(AppPreference.getInstance(mContext).getBuildVersion(), param)
                .subscribeOn(Schedulers.io())
                .subscribe(this::handlerResponseRequest, this::handleErrorRequest);
        mCompositeDisposable.add(disposable);
    }

    //Lắng nghe lỗi api request ads
    public void handleErrorRequest(Throwable throwable) {
        AppPreference.getInstance(mContext).savelistAPiBackup(listApiBackup);
    }

    //Trả về kết quả thành công khi post request ads
    public void handlerResponseRequest(ResponseBody responseBody) {
        if (listApiBackup.size() > 0) {
            listApiBackup.remove(listApiBackup.size() - 1);
        }
        try {
            if (responseBody != null) {
                JSONObject jsonObject = new JSONObject(responseBody.string());
                String status;
                status = String.valueOf(jsonObject.get("status"));
                if (status.trim().equals("true")) {
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AppPreference.getInstance(mContext).savelistAPiBackup(listApiBackup);
    }

    //Api click ads được gọi khi người dùng click vào CTA
    public void postClickAds(HashMap<String, String> param, int type) {
        listApiBackup.add(new BackUpManager(param, type));
        Disposable disposable = getApiService().postClickAds(AppPreference.getInstance(mContext).getBuildVersion(), param)
                .subscribeOn(Schedulers.io())
                .subscribe(this::handlerResponseClick, this::handleErrorClick);
        mCompositeDisposable.add(disposable);
    }


    //Thông báo lỗi khi request api click ads
    public void handleErrorClick(Throwable throwable) {
        AppPreference.getInstance(mContext).savelistAPiBackup(listApiBackup);
    }

    //Hàm trả về thông báo thành công khi request api click ads
    public void handlerResponseClick(ResponseBody responseBody) {
        if (listApiBackup.size() > 0) {
            listApiBackup.remove(listApiBackup.size() - 1);
        }
        try {
            if (responseBody != null) {
                JSONObject jsonObject = new JSONObject(responseBody.string());
                String status = String.valueOf(jsonObject.get("status"));
                if (status.equals("true")) {
                    Log.e("res click ", jsonObject.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        AppPreference.getInstance(mContext).savelistAPiBackup(listApiBackup);
    }

    //Api View ads: khi quảng cáo được hiện đầy đủ sẽ thông báo cho server
    public void postViewAds(HashMap<String, String> param, int type) {
        listApiBackup.add(new BackUpManager(param, type));
        Disposable disposable = getApiService().postViewAds(AppPreference.getInstance(mContext).getBuildVersion(), param)
                .subscribeOn(Schedulers.io())
                .subscribe(this::handlerResponseView, this::handleErrorView);
        mCompositeDisposable.add(disposable);
    }

    //Thông báo lỗi khi request api view ads failed
    public void handleErrorView(Throwable throwable) {
        AppPreference.getInstance(mContext).savelistAPiBackup(listApiBackup);
    }

    //Thông báo thành công khi request ads view
    public void handlerResponseView(ResponseBody responseBody) {
        if (listApiBackup.size() > 0) {
            listApiBackup.remove(listApiBackup.size() - 1);
        }
        try {
            if (responseBody != null) {
                JSONObject jsonObject = new JSONObject(responseBody.string());
                String status = String.valueOf(jsonObject.get("status"));
                if (status.equals("true")) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        AppPreference.getInstance(mContext).savelistAPiBackup(listApiBackup);
    }

}
