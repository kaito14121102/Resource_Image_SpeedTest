package com.gdt.speedtest.features.main.fragment.speed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

import com.gdt.speedtest.base.BasePresenter;
import com.gdt.speedtest.data.manager.ApiClient;
import com.gdt.speedtest.data.manager.DataManager;
import com.gdt.speedtest.data.model.Address;
import com.gdt.speedtest.data.remote.ApiAddress;
import com.gdt.speedtest.database.Result;
import com.gdt.speedtest.features.main.PageListener;
import com.gdt.speedtest.util.AppPreference;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.WIFI_SERVICE;
import static com.gdt.speedtest.Constants.EXTRA_KEY_ADS_FULL_CLICK_GO;
import static com.gdt.speedtest.Constants.MAX_COUNT_CLICK_GO;

public class SpeedFragmentPresenter extends BasePresenter<SpeedChangeListener> {
    private static final String TAG = "SpeedFragmentPresenter";
    private PageListener listener;
    private List<Address> addressList;

    @Inject
    SpeedFragmentPresenter() {
    }

    public void setListener(PageListener listener) {
        this.listener = listener;
    }

    public void requestForDownload() {
        ApiAddress service = ApiClient.getClient().create(ApiAddress.class);
        Call<Object> call = service.requestForDownload();
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (getView() != null) {
                    getView().onDownloadRequest();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (getView() != null) {
                    getView().onDownloadRequest();
                }
            }
        });
    }

    public void getDataFromApi(Context context, int countLoadAds) {
        AppPreference.getInstance(context).setKeyShowAds(EXTRA_KEY_ADS_FULL_CLICK_GO, countLoadAds);
        if (countLoadAds >= MAX_COUNT_CLICK_GO) {
            if (listener != null) {
                listener.loadFullAdsGoogleClickTest();
            }
        }

        ApiAddress service = ApiClient.getClient().create(ApiAddress.class);
        Call<List<Address>> call = service.getAddressList();
        addressList = new ArrayList<>();

        call.enqueue(new Callback<List<Address>>() {
            @Override
            public void onResponse(Call<List<Address>> call, Response<List<Address>> response) {
                if(getView() != null){
                    addressList = response.body();
                    getView().onGetDataFromApiCompleted(addressList);
                }
            }

            @Override
            public void onFailure(Call<List<Address>> call, Throwable t) {
                if(getView() != null){
                    getView().showDateFailToast();
                    getView().apiLoadFaild();
                }
            }
        });
    }

    public void requestForUpload() {
        ApiAddress service = ApiClient.getClient().create(ApiAddress.class);
        Call<Object> call = service.requestForUpload();
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (getView() != null) {
                    getView().onUploadRequest();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (getView() != null) {
                    getView().onUploadRequest();
                }
            }
        });
    }

    public void saveResult(Context context,String networkName, double ping, double downloadRate, double uploadRate) {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.ENGLISH);
        String time = dateFormat.format(date);
        Result result = new Result();
        result.setTime(time);
        result.setPing(ping);
        result.setDownload(downloadRate);
        result.setUpload(uploadRate);

        String ipPrivate = getIpPrivate();
        String ipPublic = getIpPublicFormat(context);
        String name = networkName+"-_-"+ipPrivate+"-_-"+ipPublic;
        result.setName(name);
        result.setTypeNetwork(checkTypeNetwork(context));
        DataManager.query().getResultDao().save(result);
        if (listener != null) {
            listener.onTestSuccess(result);
        }
    }
    private String getIpPublicFormat(Context context) {
        WifiManager wifiManager = (WifiManager)context.getSystemService(WIFI_SERVICE);
        DhcpInfo dhcp = wifiManager.getDhcpInfo();
        int ip = dhcp.gateway;
        return formatIP(ip);
    }
    @SuppressLint("DefaultLocale")
    private String formatIP(int ip) {
        return String.format(
                "%d.%d.%d.%d",
                (ip & 0xff),
                (ip >> 8 & 0xff),
                (ip >> 16 & 0xff),
                (ip >> 24 & 0xff)
        );
    }

    private String getIpPrivate() {
        boolean useIPv4 = true;
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "";
    }

    private String checkTypeNetwork(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();
        return Boolean.toString(isWifi);
    }

    public String[] getNearestServer(List<Address> addressList) {
        String[] urls = new String[3];
        int minDistance = 1000000;
        if (addressList != null) {
            for (Address address : addressList) {
                int distance = Integer.parseInt(address.getDistance());
                if (distance < minDistance) {
                    minDistance = distance;
                    String uploadUrl = address.getUrl().replace(":8080", "");
                    String downloadUrl = uploadUrl.replace(uploadUrl.split("/")[uploadUrl.split("/").length - 1], "");
                    urls[0] = address.getHost().replace(":8080", "");
                    urls[1] = downloadUrl;
                    urls[2] = uploadUrl;
                }
            }
        }
        return urls;
    }
}
