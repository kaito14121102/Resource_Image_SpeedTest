package com.example.crosspromotesdk.ad;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.crosspromotesdk.BuildConfig;
import com.example.crosspromotesdk.R;
import com.example.crosspromotesdk.adlistener.AdError;
import com.example.crosspromotesdk.adlistener.AdListener;
import com.example.crosspromotesdk.data.AppPreference;
import com.example.crosspromotesdk.data.AppUtil;
import com.example.crosspromotesdk.data.BackUpManager;
import com.example.crosspromotesdk.modal.Advertisements;
import com.example.crosspromotesdk.util.CrossPromote;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class NativeAd {
    private static final int MDPI = 0;
    private static final int HDPI = 1;
    private static final int XHDPI = 2;
    private static final int XXHDPI = 3;
    private static final int XXXHDPI = 4;
    private static final int IS_3G = 1;
    private ArrayList<Advertisements> advertisements;
    private AdListener adListener;
    private Context context;
    private CompositeDisposable mCompositeDisposable;
    private String timeService, jsonList;
    private Random random;
    private int position;
    private String code, message;
    private Advertisements advertisementSelected;
    private NativeAdViewHolder nativeAdViewHolder;
    private String urlIcon, urlMedia;
    private long timeCurrent;
    private Disposable disposable;
    private BackUpManager backUpManager;
    private ArrayList<BackUpManager> listApiBackup;
    private Handler handler;
    private AppUtil appUtil;
    private AppPreference appPreference;
    private CrossPromote crossPromote;
    private String packageName,Region;
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(checkDataValue()){
                postViewAds(position);
            }
            handler.removeCallbacks(runnable);
        }
    };
    public void setData(String packageName,String region){
        this.packageName = packageName;
        this.Region = region;
    }

    public NativeAd(Context context) {
        mCompositeDisposable = new CompositeDisposable();
        advertisements = new ArrayList<>();
        random = new Random();
        this.context = context;
        //Khởi tạo net work module
        advertisementSelected = new Advertisements();
        backUpManager = new BackUpManager();
        listApiBackup = new ArrayList<>();
        handler = new Handler();
        appUtil = new AppUtil(context);
        appPreference = AppPreference.getInstance(context);
        crossPromote = CrossPromote.getInstance();
        appPreference.pushBuildVersion(BuildConfig.VERSION_NAME);
    }

    public AppPreference getAppPreference() {
        return appPreference;
    }

    public void setAppPreference(AppPreference appPreference) {
        this.appPreference = appPreference;
    }

    public CrossPromote getCrossPromote() {
        return crossPromote;
    }

    public void setCrossPromote(CrossPromote crossPromote) {
        this.crossPromote = crossPromote;
    }

    // Hàm nhận về kết quả khi chạy api get list ads
    public void handleResponsegetlistAds(ResponseBody listData) {
        try {
            if(listData!=null){
                String json = listData.string();
                //Kiểm tra trong chuỗi json có chứa 2 từ này thì pass
                if (json.contains("\"status\":true")) {
                    appPreference.pushTimedestination(String.valueOf(timeCurrent));
                    JSONObject jsonObject = new JSONObject(json);
                    jsonObject = jsonObject.getJSONObject("data");
                    timeService = String.valueOf(jsonObject.get("time_server"));
                    jsonList = jsonObject.getJSONArray("list_advertisements").toString();
                    //Lưu time server và list ads vào local
                    appPreference.pushKeyTimeServer(timeService);
                    appPreference.pushKeyListAds(jsonList);
                    process();
                } else {
                    JSONObject jsonObject = new JSONObject(json);
                    jsonObject = jsonObject.getJSONObject("errors");
                    code = String.valueOf(jsonObject.get("code"));
                    message = String.valueOf(jsonObject.get("message"));
                    if (adListener != null) {
                        adListener.onError(new AdError(Integer.valueOf(code), message));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void process() {
        //Lấy list ads data local khi lưu về từ bước trước để lọc ra url ảnh phù hợp với tốc độ mạng và kích thước màn hình
        //Vừa tiến hành mã hóa base 64 vừa hiển thị quảng cáo luôn để tối ưu thời gian
        advertisements = appPreference.getListAdsFromJson();

        checkUrlavailable();
        if(!appPreference.getKeyLoadCache()){
            loadFirstCacheImage();
        }
        //Lấy ngẫu nhiên 1 quảng cáo để load
        position = random.nextInt(advertisements.size());
        if (adListener != null) {
                postRequestAds(position);
            advertisementSelected = advertisements.get(position);
            adListener.onAdLoaded(advertisementSelected);
        }
    }

    private void checkUrlavailable() {
        for (int i = 0; i < advertisements.size(); i++) {
            processGetImage(i);
            //setUrl Icon và media phù hợp với tốc độ đường truyền và kích thước màn hình
            advertisements.get(i).setAvailableUrlIcon(urlIcon);
            advertisements.get(i).setAvailableUrlMedia(urlMedia);
        }
        appPreference.saveDatalistAds(advertisements);
    }

    //Lấy dữ liệu khi mất mạng
    public void setDataOffline(ArrayList<Advertisements> advertisements) {
        if (advertisements.size() > 0) {
            //random để lấy ra quảng cáo
            position = random.nextInt(advertisements.size());
            if (adListener != null) {
                advertisementSelected = advertisements.get(position);
                postRequestAds(position);
                adListener.onAdLoaded(advertisementSelected);
            }
        } else {
            if (adListener != null) {
                adListener.onError(new AdError(1, context.getString(R.string.nofillads)));
            }
        }
    }

    //Trả về lỗi khi không lấy được danh sách quảng cáo
    public void handleErrorgetlistAds(Throwable error) {
        if (adListener != null) {
            adListener.onError(new AdError(1, error.getMessage()));
        }
    }

    //Set listener cho call back AdListener
    public void setListener(AdListener adListener) {
        this.adListener = adListener;
    }

    public void getListAdsOnline() {
        disposable = crossPromote.getApiService().getAllAdvertisements(appPreference.getBuildVersion(),crossPromote.getPackageName(), Locale.getDefault().getLanguage())
       // disposable = crossPromote.getApiService().getAllAdvertisements(appPreference.getBuildVersion(),packageName, Region)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponsegetlistAds, this::handleErrorgetlistAds);
        mCompositeDisposable.add(disposable);
    }

    public void getListAdsOffline() {
        if (appPreference.getDataListAds() != null) {
            advertisements = appPreference.getDataListAds();
        }
        setDataOffline(advertisements);
    }

    public void loadAd() {
        if (adListener != null) {
            adListener.onAdLoading();
        }
        if (appUtil.isOnline()) {
            //Lấy ra thời gian hiện tại
            timeCurrent = Calendar.getInstance().getTimeInMillis() / 1000;
            /*
            Nếu đã từng request api get list ads trước đó
            thì kiểm tra thời từ lần cuối cùng request với thời gian hiện tại, nếu chênh lệch nhau 2 hour
            thì request api, nếu không lấy data từ local ra
            */
            if (!appPreference.getTimedestination().equals("")) {
                if(timeCurrent > (Long.valueOf(appPreference.getTimedestination()) + 7200)) {
                    getListAdsOnline();
                } else {
                    getListAdsOffline();
                }
            } else {
                getListAdsOnline();
            }

        } else {
            getListAdsOffline();
        }
    }

    public void destroy() {
        mCompositeDisposable.clear();
    }

    //Api post request ads báo hiệu ads ở vị trí position đang được request lên server
    private void postRequestAds(int position) {
        HashMap<String, String> param = new HashMap<>();
        param.put("request_time", String.valueOf(Calendar.getInstance().getTimeInMillis() / 1000));
        param.put("advertisement_id", advertisements.get(position).getAdvertisement_id());
        param.put("token", advertisements.get(position).getDetails().getCtA().getToken());
        param.put("time_server", appPreference.getKeyTimeServer());
        backUpManager.setApiMiss(param);
        //type 1: api request, type 2: api view, type 3: click ads
        //set type để sau post lại api biết param là của api loại nào
        backUpManager.setType(1);
        disposable = crossPromote.getApiService().postRequestAds(appPreference.getBuildVersion(),param)
                .subscribeOn(Schedulers.io())
                .subscribe(this::handlerResponseRequest, this::handleErrorRequest);
        mCompositeDisposable.add(disposable);
    }

    //Lắng nghe lỗi api request ads
    public void handleErrorRequest(Throwable throwable) {
        if (appPreference.getListApiBackup() != null && appPreference.getListApiBackup().size() > 0) {
            listApiBackup = appPreference.getListApiBackup();
        }
        listApiBackup.add(backUpManager);
        appPreference.savelistAPiBackup(listApiBackup);
        /*
        {"apiMiss":{"time_server":"1532341193","token":"DnIs8eekkhuA0f_3CfeNFA","advertisement_id":"6E1NJVQWOQrDpn4X","request_time":"1532341193"},"type":1}
        */
    }

    //Trả về kết quả thành công khi post request ads
    private void handlerResponseRequest(ResponseBody responseBody) {
        try {
            if (responseBody != null) {
                JSONObject jsonObject = new JSONObject(responseBody.string());
                String status = String.valueOf(jsonObject.get("status"));
                if (status.trim().equals("true")) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Api click ads được gọi khi người dùng click vào CTA
    private void postClickAds(int position) {
        HashMap<String, String> param = new HashMap<>();
        param.put("click_time", String.valueOf(Calendar.getInstance().getTimeInMillis() / 1000));
        param.put("advertisement_id", advertisements.get(position).getAdvertisement_id());
        param.put("token", advertisements.get(position).getDetails().getCtA().getToken());
        param.put("time_server", appPreference.getKeyTimeServer());
        backUpManager.setApiMiss(param);
        backUpManager.setType(3);
        disposable = crossPromote.getApiService().postClickAds(appPreference.getBuildVersion(),param)
                .subscribeOn(Schedulers.io())
                .subscribe(this::handlerResponseClick, this::handleErrorClick);
        mCompositeDisposable.add(disposable);
    }


    //Thông báo lỗi khi request api click ads
    public void handleErrorClick(Throwable throwable) {
        if (appPreference.getListApiBackup() != null && appPreference.getListApiBackup().size() > 0) {
            listApiBackup = appPreference.getListApiBackup();
        }
        listApiBackup.add(backUpManager);
        appPreference.savelistAPiBackup(listApiBackup);

    }

    //Hàm trả về thông báo thành công khi request api click ads
    private void handlerResponseClick(ResponseBody responseBody) {
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
    }

    //Api View ads: khi quảng cáo được hiện đầy đủ sẽ thông báo cho server
    public void postViewAds(int position) {
        HashMap<String, String> param = new HashMap<>();
        param.put("view_time", String.valueOf(Calendar.getInstance().getTimeInMillis() / 1000));
        param.put("advertisement_id", advertisements.get(position).getAdvertisement_id());
        param.put("token", advertisements.get(position).getDetails().getCtA().getToken());
        param.put("time_server", appPreference.getKeyTimeServer());
        backUpManager.setApiMiss(param);
        backUpManager.setType(2);
        disposable = crossPromote.getApiService().postViewAds(appPreference.getBuildVersion(),param)
                .subscribeOn(Schedulers.io())
                .subscribe(this::handlerResponseView, this::handleErrorView);
        mCompositeDisposable.add(disposable);
    }

    //Thông báo lỗi khi request api view ads failed
    public void handleErrorView(Throwable throwable) {
        if (appPreference.getListApiBackup() != null && appPreference.getListApiBackup().size() > 0) {
            listApiBackup = appPreference.getListApiBackup();
        }
        listApiBackup.add(backUpManager);
        appPreference.savelistAPiBackup(listApiBackup);

    }

    //Thông báo thành công khi request ads view
    private void handlerResponseView(ResponseBody responseBody) {
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
    }

    //check view xem dữ liệu được hiển thị đầy đủ hết chưa
    public boolean checkDataValue() {
        if (nativeAdViewHolder.getTitleText()!=null&&!nativeAdViewHolder.getTitleText().getText().equals("")) {
            return true;
        }
        if (nativeAdViewHolder.getBodyText()!=null&&!nativeAdViewHolder.getBodyText().getText().equals("")) {
            return true;
        }
        if (nativeAdViewHolder.getCallToActionView()!=null&&!nativeAdViewHolder.getCallToActionView().getText().equals("")) {
            return true;
        }
        if (nativeAdViewHolder.getIcon()!=null&&nativeAdViewHolder.getIcon().getDrawable() != null) {
            return true;
        }
        return nativeAdViewHolder.getAdMedia() != null && nativeAdViewHolder.getAdMedia().getDrawable() != null;
    }

    public void loadFirstCacheImage(){
        if(appUtil.isOnline()&&advertisements.size()>0){
            for (int i=0;i<advertisements.size();i++){
                Glide.with(context.getApplicationContext())
                        .load(advertisements.get(i).getAvailableUrlMedia())
                        .downloadOnly(500, 500);
                Glide.with(context.getApplicationContext())
                        .load(advertisements.get(i).getAvailableUrlIcon())
                        .downloadOnly(500, 500);
                }
            appPreference.pushKeyLoadCache(true);
        }
    }

    public void registerView(View viewParent, NativeAdViewBinder viewBinder) {
        nativeAdViewHolder = new NativeAdViewHolder(viewParent, viewBinder);
        if (advertisements.size() > 0) {
            nativeAdViewHolder.getTitleText().setText(advertisementSelected.getDetails().getTitle().getContent());
            nativeAdViewHolder.getBodyText().setText(advertisementSelected.getDetails().getBody().getContent());
            //Cơ chế hiển thị: Load ảnh bằng Glide, mất mạng Glide sẽ tự cache
            GlideApp
                    .with(context.getApplicationContext())
                    .load(advertisementSelected.getAvailableUrlMedia())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(nativeAdViewHolder.getAdMedia());
            GlideApp
                    .with(context.getApplicationContext())
                    .load(advertisementSelected.getAvailableUrlIcon())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(nativeAdViewHolder.getIcon());
            nativeAdViewHolder.getIcon().setBackground(null);
            nativeAdViewHolder.getCallToActionView().setText(advertisementSelected.getDetails().getCtA().getText());
            handleListener();
            handler.postDelayed(runnable, 2000);
        }
    }

    public void handleListener() {
        nativeAdViewHolder.getCallToActionView().setOnClickListener(v -> {
            callToActionClick();
        });
    }

    public void callToActionClick() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(advertisementSelected.getDetails().getCtA().getLink_checking()));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
        if (adListener != null) {
            adListener.onAdClicked(this);
                postClickAds(position);
        }
    }

    private void processGetImage(int i) {
        if (appUtil.checkTypeNetwork() == IS_3G) {
            switch (appUtil.getDeviceResolution()) {
                case MDPI:
                    setUrl(i, 0);
                    break;
                case HDPI:
                    setUrl(i, 0);
                    break;
                case XHDPI:
                    setUrl(i, 1);
                    break;
                case XXHDPI:
                    setUrl(i, 2);
                    break;
                case XXXHDPI:
                    setUrl(i, 3);
                    break;
            }
        } else {
            switch (appUtil.getDeviceResolution()) {
                case MDPI:
                    setUrl(i, 0);
                    break;
                case HDPI:
                    setUrl(i, 1);
                    break;
                case XHDPI:
                    setUrl(i, 2);
                    break;
                case XXHDPI:
                    setUrl(i, 3);
                    break;
                case XXXHDPI:
                    setUrl(i, 4);
                    break;
            }
        }
    }

    public void setUrl(int i, int caseCheck) {
        switch (caseCheck) {
            case MDPI:
                urlIcon = advertisements.get(i).getDetails().getIcon().getResources().getMdpi().getUrl();
                urlMedia = advertisements.get(i).getDetails().getMedia().getResources().getMdpi().getUrl();
                break;
            case HDPI:
                urlIcon = advertisements.get(i).getDetails().getIcon().getResources().getHdpi().getUrl();
                urlMedia = advertisements.get(i).getDetails().getMedia().getResources().getHdpi().getUrl();
                break;
            case XHDPI:
                urlIcon = advertisements.get(i).getDetails().getIcon().getResources().getXhdpi().getUrl();
                urlMedia = advertisements.get(i).getDetails().getMedia().getResources().getXhdpi().getUrl();
                break;
            case XXHDPI:
                urlIcon = advertisements.get(i).getDetails().getIcon().getResources().getXxhdpi().getUrl();
                urlMedia = advertisements.get(i).getDetails().getMedia().getResources().getXxhdpi().getUrl();
                break;
            case XXXHDPI:
                urlIcon = advertisements.get(i).getDetails().getIcon().getResources().getXxxhdpi().getUrl();
                urlMedia = advertisements.get(i).getDetails().getMedia().getResources().getXxxhdpi().getUrl();
                break;
        }
    }
    public void clearData(){
        appPreference.clearSharePreference();
    }
}
