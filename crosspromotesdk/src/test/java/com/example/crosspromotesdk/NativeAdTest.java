package com.example.crosspromotesdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.test.InstrumentationTestCase;
import android.test.UiThreadTest;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.crosspromotesdk.ad.GlideApp;
import com.example.crosspromotesdk.ad.GlideRequest;
import com.example.crosspromotesdk.ad.GlideRequests;
import com.example.crosspromotesdk.ad.NativeAd;
import com.example.crosspromotesdk.ad.NativeAdViewBinder;
import com.example.crosspromotesdk.ad.NativeAdViewHolder;
import com.example.crosspromotesdk.adlistener.AdListener;
import com.example.crosspromotesdk.data.AppPreference;
import com.example.crosspromotesdk.data.AppUtil;
import com.example.crosspromotesdk.data.BackUpManager;
import com.example.crosspromotesdk.modal.Advertisements;
import com.example.crosspromotesdk.modal.Body;
import com.example.crosspromotesdk.modal.CTA;
import com.example.crosspromotesdk.modal.Details;
import com.example.crosspromotesdk.modal.Hdpi;
import com.example.crosspromotesdk.modal.Icon;
import com.example.crosspromotesdk.modal.Mdpi;
import com.example.crosspromotesdk.modal.Media;
import com.example.crosspromotesdk.modal.Resource;
import com.example.crosspromotesdk.modal.Title;
import com.example.crosspromotesdk.modal.Xhdpi;
import com.example.crosspromotesdk.modal.Xxhdpi;
import com.example.crosspromotesdk.modal.Xxxhdpi;
import com.example.crosspromotesdk.network.Apiservice;
import com.example.crosspromotesdk.util.CrossPromote;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NativeAdViewHolder.class, NativeAd.class, GlideApp.class})
public class NativeAdTest extends InstrumentationTestCase {
    private static final int MDPI = 0;
    private static final int HDPI = 1;
    private static final int XHDPI = 2;
    private static final int XXHDPI = 3;
    private static final int XXXHDPI = 4;
    private static final int TYPE_3G = 1;
    private static final int TYPE_WIFI = 2;
    NativeAd nativeAd;
    @Mock
    Context context;
    @Mock
    AdListener adListener;
    @Mock
    SharedPreferences sharedPreferences;
    @Mock
    SharedPreferences.Editor mEditor;
    @Mock
    CrossPromote crossPromote;
    @Mock
    Apiservice apiservice;
    @Mock
    AppPreference appPreference;
    @Mock
    NativeAdViewHolder nativeAdViewHolder;
    @Mock
    ConnectivityManager connectivityManager;
    @Mock
    NetworkInfo networkInfo;
    @Mock
    NativeAdViewBinder nativeAdViewBinder;
    @Mock
    View view;
    @Mock
    ArrayList<Advertisements> advertisementsArrayList;
    @Mock
    Advertisements advertisements;
    @Mock
    TextView textView;
    @Mock
    Details details;
    @Mock
    Title title;
    @Mock
    GlideRequest<Drawable> drawableGlideRequest;
    @Mock
    Body body;
    @Mock
    GlideRequests glideRequests;
    @Mock
    ImageView imageView;
    @Mock
    ViewTarget<ImageView, Drawable> viewTarget;
    @Mock
    Button button;
    @Mock
    CTA cta;
    @Mock
    Drawable drawable;
    @Mock
    Icon icon;
    @Mock
    Resource resource;
    @Mock
    Mdpi mdpi;
    @Mock
    Hdpi hdpi;
    @Mock
    Xhdpi xhdpi;
    @Mock
    Xxhdpi xxhdpi;
    @Mock
    Xxxhdpi xxxhdpi;
    @Mock
    Media media;
    @Mock
    Resources resources;
    @Mock
    DisplayMetrics displayMetrics;
    @Mock
    AppUtil appUtil;
    @Mock
    Handler handler;
    @Mock
    ArrayList<BackUpManager> backUpManagerArrayList;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(context.getApplicationContext()).thenReturn(context);
        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences);
        when(sharedPreferences.edit()).thenReturn(mEditor);
        when(mEditor.putString(anyString(), anyString())).thenReturn(mEditor);
        when(mEditor.putInt(anyString(), anyInt())).thenReturn(mEditor);
        when(mEditor.putLong(anyString(), anyLong())).thenReturn(mEditor);
        when(mEditor.putFloat(anyString(), anyFloat())).thenReturn(mEditor);
        when(mEditor.putBoolean(anyString(), anyBoolean())).thenReturn(mEditor);
        when(mEditor.commit()).thenReturn(true);
        PowerMockito.mockStatic(GlideApp.class);
        when(GlideApp.with(context)).thenReturn(glideRequests);
        when(glideRequests.load(anyString())).thenReturn(drawableGlideRequest);
        when(drawableGlideRequest.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)).thenReturn(drawableGlideRequest);
        when(drawableGlideRequest.into(imageView)).thenReturn(viewTarget);
        nativeAd = new NativeAd(context);
        whenNew(AppUtil.class).withAnyArguments().thenReturn(appUtil);
        PowerMockito.whenNew(Handler.class).withNoArguments().thenReturn(handler);
        nativeAd.setAppPreference(appPreference);
        nativeAd.setCrossPromote(crossPromote);
        assertNotNull(context);
        }

    @Test
    public void getAppPreference() {
        nativeAd.getAppPreference();
    }

    @Test
    public void setAppPreference() {
        nativeAd.setAppPreference(appPreference);
    }

    @Test
    public void getCrossPromote() {
        nativeAd.getCrossPromote();
    }

    @Test
    public void setCrossPromote() {
        nativeAd.setCrossPromote(crossPromote);
    }


    @Test
    public void setListener() {
        nativeAd.setListener(adListener);
        assertNotNull(adListener);
    }

    @Test
    public void getListAdsOnline() {
        ResponseBody responseBody = mock(ResponseBody.class);
        when(crossPromote.getApiService()).thenReturn(apiservice);
        doReturn(Observable.just(responseBody)).when(apiservice).getAllAdvertisements(anyString(), anyString(), anyString());
      //  when(crossPromote.getPackageName()).thenReturn("defaultValue");
        nativeAd.getListAdsOnline();
        verify(crossPromote, times(1)).getApiService();
       // verify(crossPromote, times(1)).getPackageName();
        verify(apiservice).getAllAdvertisements(anyString(), anyString(), anyString());
    }

    @Test
    public void getListAdsOffline() {
        nativeAd.getListAdsOffline();
        verify(appPreference, Mockito.times(2)).getDataListAds();
    }

    @Test
    public void loadAdFirst() {
        ResponseBody responseBody = ResponseBody
                .create(MediaType.parse("application/json"), "{\"status\":true,\"errors\":[],\"data\":{\"time_server\":1532922297,\"list_advertisements\":[{\"advertisement_id\":\"vNo19BPZJGlzEpD5\",\"application_key\":\"com.vintro.taobiettuot.haihinhmotchu\",\"age_tag\":3,\"region\":\"vi\",\"details\":{\"CtA\":{\"font\":\"Test\",\"size\":60,\"text\":\"Cài đặt ngay\",\"color\":\"#08D324\",\"text_size\":22,\"link_checking\":\"google.com\",\"background_color\":\"#EE0E0E\",\"token\":\"AKSyqQoK3WhnxFa09Epe_g\"},\"body\":{\"font\":\"TimesNewRoman\",\"size\":24,\"color\":\"#F2FF27\",\"content\":\"game cho người nghèo\"},\"icon\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/girl.jpg\",\"resources\":{\"hdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/hdpi_girl.jpg\",\"size\":\"72x72\"},\"mdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/mdpi_girl.jpg\",\"size\":\"48x48\"},\"xhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xhdpi_girl.jpg\",\"size\":\"96x96\"},\"xxhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xxhdpi_girl.jpg\",\"size\":\"144x144\"},\"xxxhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xxxhdpi_girl.jpg\",\"size\":\"192x192\"}}},\"title\":{\"font\":\"TimesNewRoman\",\"size\":28,\"color\":\"#0E71EE\",\"content\":\"Phiên bản đặc biệt hai hình một chữ hay\"}}}]}}");
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        when(networkInfo.isConnectedOrConnecting()).thenReturn(true);
        when(appPreference.getTimedestination()).thenReturn("152010101");
        when(crossPromote.getApiService()).thenReturn(apiservice);
        doReturn(Observable.just(responseBody)).when(apiservice).getAllAdvertisements(anyString(), anyString(), anyString());
        //when(crossPromote.getPackageName()).thenReturn("defaultValue");
        nativeAd.setListener(adListener);
        nativeAd.loadAd();
        verify(context).getSystemService(Context.CONNECTIVITY_SERVICE);
        verify(connectivityManager).getActiveNetworkInfo();
        verify(networkInfo).isConnectedOrConnecting();
        verify(appPreference, times(2)).getTimedestination();
        verify(crossPromote, times(1)).getApiService();
       // verify(crossPromote, times(1)).getPackageName();
        verify(apiservice).getAllAdvertisements(anyString(), anyString(), anyString());
        verify(appPreference, never()).getDataListAds();
    }

    @Test
    public void loadAdCache() {
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        when(networkInfo.isConnectedOrConnecting()).thenReturn(true);
       // when(crossPromote.getPackageName()).thenReturn("defaultValue");
        nativeAd.setListener(adListener);
        //15789023121 là value tự tạo random
        when(appPreference.getTimedestination()).thenReturn("15789023121");
        nativeAd.loadAd();
        verify(context).getSystemService(Context.CONNECTIVITY_SERVICE);
        verify(connectivityManager).getActiveNetworkInfo();
        verify(networkInfo).isConnectedOrConnecting();
        verify(appPreference, times(2)).getTimedestination();
    }

    @Test
    public void loadAdNotCache() {
        ResponseBody responseBody = ResponseBody
                .create(MediaType.parse("application/json"), "{\"status\":true,\"errors\":[],\"data\":{\"time_server\":1532922297,\"list_advertisements\":[{\"advertisement_id\":\"vNo19BPZJGlzEpD5\",\"application_key\":\"com.vintro.taobiettuot.haihinhmotchu\",\"age_tag\":3,\"region\":\"vi\",\"details\":{\"CtA\":{\"font\":\"Test\",\"size\":60,\"text\":\"Cài đặt ngay\",\"color\":\"#08D324\",\"text_size\":22,\"link_checking\":\"google.com\",\"background_color\":\"#EE0E0E\",\"token\":\"AKSyqQoK3WhnxFa09Epe_g\"},\"body\":{\"font\":\"TimesNewRoman\",\"size\":24,\"color\":\"#F2FF27\",\"content\":\"game cho người nghèo\"},\"icon\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/girl.jpg\",\"resources\":{\"hdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/hdpi_girl.jpg\",\"size\":\"72x72\"},\"mdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/mdpi_girl.jpg\",\"size\":\"48x48\"},\"xhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xhdpi_girl.jpg\",\"size\":\"96x96\"},\"xxhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xxhdpi_girl.jpg\",\"size\":\"144x144\"},\"xxxhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xxxhdpi_girl.jpg\",\"size\":\"192x192\"}}},\"title\":{\"font\":\"TimesNewRoman\",\"size\":28,\"color\":\"#0E71EE\",\"content\":\"Phiên bản đặc biệt hai hình một chữ hay\"}}}]}}");
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        when(networkInfo.isConnectedOrConnecting()).thenReturn(true);
        when(appPreference.getTimedestination()).thenReturn("");
        when(crossPromote.getApiService()).thenReturn(apiservice);
        doReturn(Observable.just(responseBody)).when(apiservice).getAllAdvertisements(anyString(), anyString(), anyString());
       // when(crossPromote.getPackageName()).thenReturn("defaultValue");
        nativeAd.setListener(adListener);
        nativeAd.loadAd();
        verify(context).getSystemService(Context.CONNECTIVITY_SERVICE);
        verify(connectivityManager).getActiveNetworkInfo();
        verify(networkInfo).isConnectedOrConnecting();
        verify(appPreference, times(1)).getTimedestination();
        verify(crossPromote, times(1)).getApiService();
     //   verify(crossPromote, times(1)).getPackageName();
        verify(apiservice).getAllAdvertisements(anyString(), anyString(), anyString());
        verify(appPreference, never()).getDataListAds();
    }

    @Test
    public void loadAdNotNetWork() {
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        when(networkInfo.isConnectedOrConnecting()).thenReturn(false);
        nativeAd.loadAd();
        verify(context).getSystemService(Context.CONNECTIVITY_SERVICE);
        verify(connectivityManager).getActiveNetworkInfo();
        verify(networkInfo).isConnectedOrConnecting();
    }

    @Test
    public void destroy() {
        nativeAd.destroy();
    }

    @Test
    public void registerView() throws Exception {
        MemberModifier
                .field(NativeAd.class, "advertisements").set(
                nativeAd, advertisementsArrayList);
        MemberModifier
                .field(NativeAd.class, "advertisementSelected").set(
                nativeAd, advertisements);
        when(advertisementsArrayList.size()).thenReturn(2);
        whenNew(NativeAdViewHolder.class).withAnyArguments().thenReturn(nativeAdViewHolder);
        when(nativeAdViewHolder.getTitleText()).thenReturn(textView);
        when(nativeAdViewHolder.getBodyText()).thenReturn(textView);
        when(nativeAdViewHolder.getAdMedia()).thenReturn(imageView);
        when(nativeAdViewHolder.getCallToActionView()).thenReturn(button);
        when(nativeAdViewHolder.getIcon()).thenReturn(imageView);
        when(advertisements.getDetails()).thenReturn(details);
        when(details.getTitle()).thenReturn(title);
        when(details.getBody()).thenReturn(body);
        when(details.getCtA()).thenReturn(cta);
        when(title.getContent()).thenReturn("defaultValue");
        when(body.getContent()).thenReturn("defaultValue");
        when(cta.getText()).thenReturn("defaultValue");
        when(context.getApplicationContext()).thenReturn(context);
        when(advertisements.getAvailableUrlMedia()).thenReturn("http:google.com");
        when(advertisements.getAvailableUrlIcon()).thenReturn("http:google.com");
        nativeAd.registerView(view, nativeAdViewBinder);
        verify(nativeAdViewHolder, times(1)).getTitleText();
        verify(nativeAdViewHolder, times(1)).getBodyText();
        verify(nativeAdViewHolder, times(1)).getAdMedia();
        verify(nativeAdViewHolder, times(2)).getCallToActionView();
        verify(nativeAdViewHolder, times(2)).getIcon();
        verify(details).getTitle();
        verify(details).getBody();
        verify(details).getCtA();
        verify(title).getContent();
        verify(body).getContent();
        verify(cta).getText();
        verify(context, times(2)).getApplicationContext();
        verify(advertisements).getAvailableUrlIcon();
        verify(advertisements).getAvailableUrlMedia();

    }

    @UiThreadTest
    @Test
    public void handleListener() throws Exception {
        MemberModifier
                .field(NativeAd.class, "nativeAdViewHolder").set(
                nativeAd, nativeAdViewHolder);
        when(nativeAdViewHolder.getCallToActionView()).thenReturn(button);
        nativeAd.handleListener();
    }
    @Test
    public void TestCtaClick() throws Exception {
        MemberModifier
                .field(NativeAd.class, "advertisementSelected").set(
                nativeAd, advertisements);
        MemberModifier
                .field(NativeAd.class, "advertisements").set(
                nativeAd, advertisementsArrayList);
        when(advertisements.getDetails()).thenReturn(details);
        when(details.getCtA()).thenReturn(cta);
        when(cta.getLink_checking()).thenReturn("defaultValue");
        nativeAd.setListener(adListener);
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        when(networkInfo.isConnectedOrConnecting()).thenReturn(true);
        when(advertisementsArrayList.get(anyInt())).thenReturn(advertisements);
        when(advertisements.getAdvertisement_id()).thenReturn("1");
        when(crossPromote.getApiService()).thenReturn(apiservice);
        ResponseBody responseBody = ResponseBody
                .create(MediaType.parse("application/json"), "{status:true,errors:[],data:{click_time:1532398075,token:1tPYtK7lluZX2P0mj8iUNw,advertisement_id:73Ja0NBp7QRA294b,time_server:1532397830}}");
        doReturn(Observable.just(responseBody)).when(apiservice).postClickAds(any(),new HashMap<>());
        nativeAd.callToActionClick();
    }

    @Test
    public void setUrl() throws Exception {
        MemberModifier
                .field(NativeAd.class, "advertisements").set(
                nativeAd, advertisementsArrayList);
        when(advertisementsArrayList.get(anyInt())).thenReturn(advertisements);
        when(advertisements.getDetails()).thenReturn(details);
        when(details.getIcon()).thenReturn(icon);
        when(details.getMedia()).thenReturn(media);
        when(icon.getResources()).thenReturn(resource);
        when(media.getResources()).thenReturn(resource);
        when(resource.getMdpi()).thenReturn(mdpi);
        when(resource.getHdpi()).thenReturn(hdpi);
        when(resource.getXhdpi()).thenReturn(xhdpi);
        when(resource.getXxhdpi()).thenReturn(xxhdpi);
        when(resource.getXxxhdpi()).thenReturn(xxxhdpi);
        nativeAd.setUrl(0, anyInt());
    }

    @Test
    public void handleResponseListadsType3gMDPI() throws Exception {
        ResponseBody responseBody = ResponseBody
                .create(MediaType.parse("application/json"), "{\"status\":true,\"errors\":[],\"data\":{\"time_server\":1532922297,\"list_advertisements\":[{\"advertisement_id\":\"vNo19BPZJGlzEpD5\",\"application_key\":\"com.vintro.taobiettuot.haihinhmotchu\",\"age_tag\":3,\"region\":\"vi\",\"details\":{\"CtA\":{\"font\":\"Test\",\"size\":60,\"text\":\"Cài đặt ngay\",\"color\":\"#08D324\",\"text_size\":22,\"link_checking\":\"google.com\",\"background_color\":\"#EE0E0E\",\"token\":\"AKSyqQoK3WhnxFa09Epe_g\"},\"body\":{\"font\":\"TimesNewRoman\",\"size\":24,\"color\":\"#F2FF27\",\"content\":\"game cho người nghèo\"},\"icon\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/girl.jpg\",\"resources\":{\"hdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/hdpi_girl.jpg\",\"size\":\"72x72\"},\"mdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/mdpi_girl.jpg\",\"size\":\"48x48\"},\"xhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xhdpi_girl.jpg\",\"size\":\"96x96\"},\"xxhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xxhdpi_girl.jpg\",\"size\":\"144x144\"},\"xxxhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xxxhdpi_girl.jpg\",\"size\":\"192x192\"}}},\"title\":{\"font\":\"TimesNewRoman\",\"size\":28,\"color\":\"#0E71EE\",\"content\":\"Phiên bản đặc biệt hai hình một chữ hay\"}}}]}}");
        when(appPreference.getListAdsFromJson()).thenReturn(advertisementsArrayList);
        Mockito.when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        Mockito.when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).thenReturn(networkInfo);
        Mockito.when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(networkInfo);
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getDisplayMetrics()).thenReturn(displayMetrics);
        when(advertisementsArrayList.size()).thenReturn(2);
        when(advertisementsArrayList.get(anyInt())).thenReturn(advertisements);
        when(advertisements.getDetails()).thenReturn(details);
        when(details.getIcon()).thenReturn(icon);
        when(details.getMedia()).thenReturn(media);
        when(icon.getResources()).thenReturn(resource);
        when(media.getResources()).thenReturn(resource);
        when(resource.getMdpi()).thenReturn(mdpi);
        when(resource.getHdpi()).thenReturn(hdpi);
        when(resource.getXhdpi()).thenReturn(xhdpi);
        when(resource.getXxhdpi()).thenReturn(xxhdpi);
        when(resource.getXxxhdpi()).thenReturn(xxxhdpi);
        MemberModifier
                .field(NativeAd.class, "appUtil").set(
                nativeAd, appUtil);
        when(appUtil.checkTypeNetwork()).thenReturn(TYPE_3G);
        when(appUtil.getDeviceResolution()).thenReturn(MDPI);
        nativeAd.handleResponsegetlistAds(responseBody);
    }

    @Test
    public void handleResponseListadsType3gHDPI() throws Exception {
        ResponseBody responseBody = ResponseBody
                .create(MediaType.parse("application/json"), "{\"status\":true,\"errors\":[],\"data\":{\"time_server\":1532922297,\"list_advertisements\":[{\"advertisement_id\":\"vNo19BPZJGlzEpD5\",\"application_key\":\"com.vintro.taobiettuot.haihinhmotchu\",\"age_tag\":3,\"region\":\"vi\",\"details\":{\"CtA\":{\"font\":\"Test\",\"size\":60,\"text\":\"Cài đặt ngay\",\"color\":\"#08D324\",\"text_size\":22,\"link_checking\":\"google.com\",\"background_color\":\"#EE0E0E\",\"token\":\"AKSyqQoK3WhnxFa09Epe_g\"},\"body\":{\"font\":\"TimesNewRoman\",\"size\":24,\"color\":\"#F2FF27\",\"content\":\"game cho người nghèo\"},\"icon\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/girl.jpg\",\"resources\":{\"hdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/hdpi_girl.jpg\",\"size\":\"72x72\"},\"mdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/mdpi_girl.jpg\",\"size\":\"48x48\"},\"xhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xhdpi_girl.jpg\",\"size\":\"96x96\"},\"xxhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xxhdpi_girl.jpg\",\"size\":\"144x144\"},\"xxxhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xxxhdpi_girl.jpg\",\"size\":\"192x192\"}}},\"title\":{\"font\":\"TimesNewRoman\",\"size\":28,\"color\":\"#0E71EE\",\"content\":\"Phiên bản đặc biệt hai hình một chữ hay\"}}}]}}");
        when(appPreference.getListAdsFromJson()).thenReturn(advertisementsArrayList);
        Mockito.when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        Mockito.when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).thenReturn(networkInfo);
        Mockito.when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(networkInfo);
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getDisplayMetrics()).thenReturn(displayMetrics);
        when(advertisementsArrayList.size()).thenReturn(2);
        when(advertisementsArrayList.get(anyInt())).thenReturn(advertisements);
        when(advertisements.getDetails()).thenReturn(details);
        when(details.getIcon()).thenReturn(icon);
        when(details.getMedia()).thenReturn(media);
        when(icon.getResources()).thenReturn(resource);
        when(media.getResources()).thenReturn(resource);
        when(resource.getMdpi()).thenReturn(mdpi);
        when(resource.getHdpi()).thenReturn(hdpi);
        when(resource.getXhdpi()).thenReturn(xhdpi);
        when(resource.getXxhdpi()).thenReturn(xxhdpi);
        when(resource.getXxxhdpi()).thenReturn(xxxhdpi);
        MemberModifier
                .field(NativeAd.class, "appUtil").set(
                nativeAd, appUtil);
        when(appUtil.checkTypeNetwork()).thenReturn(TYPE_3G);
        when(appUtil.getDeviceResolution()).thenReturn(HDPI);
        nativeAd.handleResponsegetlistAds(responseBody);
    }

    @Test
    public void handleResponseListadsType3gXHDPI() throws Exception {
        ResponseBody responseBody = ResponseBody
                .create(MediaType.parse("application/json"), "{\"status\":true,\"errors\":[],\"data\":{\"time_server\":1532922297,\"list_advertisements\":[{\"advertisement_id\":\"vNo19BPZJGlzEpD5\",\"application_key\":\"com.vintro.taobiettuot.haihinhmotchu\",\"age_tag\":3,\"region\":\"vi\",\"details\":{\"CtA\":{\"font\":\"Test\",\"size\":60,\"text\":\"Cài đặt ngay\",\"color\":\"#08D324\",\"text_size\":22,\"link_checking\":\"google.com\",\"background_color\":\"#EE0E0E\",\"token\":\"AKSyqQoK3WhnxFa09Epe_g\"},\"body\":{\"font\":\"TimesNewRoman\",\"size\":24,\"color\":\"#F2FF27\",\"content\":\"game cho người nghèo\"},\"icon\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/girl.jpg\",\"resources\":{\"hdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/hdpi_girl.jpg\",\"size\":\"72x72\"},\"mdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/mdpi_girl.jpg\",\"size\":\"48x48\"},\"xhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xhdpi_girl.jpg\",\"size\":\"96x96\"},\"xxhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xxhdpi_girl.jpg\",\"size\":\"144x144\"},\"xxxhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xxxhdpi_girl.jpg\",\"size\":\"192x192\"}}},\"title\":{\"font\":\"TimesNewRoman\",\"size\":28,\"color\":\"#0E71EE\",\"content\":\"Phiên bản đặc biệt hai hình một chữ hay\"}}}]}}");
        when(appPreference.getListAdsFromJson()).thenReturn(advertisementsArrayList);
        Mockito.when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        Mockito.when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).thenReturn(networkInfo);
        Mockito.when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(networkInfo);
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getDisplayMetrics()).thenReturn(displayMetrics);
        when(advertisementsArrayList.size()).thenReturn(2);
        when(advertisementsArrayList.get(anyInt())).thenReturn(advertisements);
        when(advertisements.getDetails()).thenReturn(details);
        when(details.getIcon()).thenReturn(icon);
        when(details.getMedia()).thenReturn(media);
        when(icon.getResources()).thenReturn(resource);
        when(media.getResources()).thenReturn(resource);
        when(resource.getMdpi()).thenReturn(mdpi);
        when(resource.getHdpi()).thenReturn(hdpi);
        when(resource.getXhdpi()).thenReturn(xhdpi);
        when(resource.getXxhdpi()).thenReturn(xxhdpi);
        when(resource.getXxxhdpi()).thenReturn(xxxhdpi);
        MemberModifier
                .field(NativeAd.class, "appUtil").set(
                nativeAd, appUtil);
        when(appUtil.checkTypeNetwork()).thenReturn(TYPE_3G);
        when(appUtil.getDeviceResolution()).thenReturn(XHDPI);
        nativeAd.handleResponsegetlistAds(responseBody);
    }

    @Test
    public void handleResponseListadsType3gXXDPI() throws Exception {
        ResponseBody responseBody = ResponseBody
                .create(MediaType.parse("application/json"), "{\"status\":true,\"errors\":[],\"data\":{\"time_server\":1532922297,\"list_advertisements\":[{\"advertisement_id\":\"vNo19BPZJGlzEpD5\",\"application_key\":\"com.vintro.taobiettuot.haihinhmotchu\",\"age_tag\":3,\"region\":\"vi\",\"details\":{\"CtA\":{\"font\":\"Test\",\"size\":60,\"text\":\"Cài đặt ngay\",\"color\":\"#08D324\",\"text_size\":22,\"link_checking\":\"google.com\",\"background_color\":\"#EE0E0E\",\"token\":\"AKSyqQoK3WhnxFa09Epe_g\"},\"body\":{\"font\":\"TimesNewRoman\",\"size\":24,\"color\":\"#F2FF27\",\"content\":\"game cho người nghèo\"},\"icon\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/girl.jpg\",\"resources\":{\"hdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/hdpi_girl.jpg\",\"size\":\"72x72\"},\"mdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/mdpi_girl.jpg\",\"size\":\"48x48\"},\"xhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xhdpi_girl.jpg\",\"size\":\"96x96\"},\"xxhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xxhdpi_girl.jpg\",\"size\":\"144x144\"},\"xxxhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xxxhdpi_girl.jpg\",\"size\":\"192x192\"}}},\"title\":{\"font\":\"TimesNewRoman\",\"size\":28,\"color\":\"#0E71EE\",\"content\":\"Phiên bản đặc biệt hai hình một chữ hay\"}}}]}}");
        when(appPreference.getListAdsFromJson()).thenReturn(advertisementsArrayList);
        Mockito.when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        Mockito.when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).thenReturn(networkInfo);
        Mockito.when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(networkInfo);
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getDisplayMetrics()).thenReturn(displayMetrics);
        when(advertisementsArrayList.size()).thenReturn(2);
        when(advertisementsArrayList.get(anyInt())).thenReturn(advertisements);
        when(advertisements.getDetails()).thenReturn(details);
        when(details.getIcon()).thenReturn(icon);
        when(details.getMedia()).thenReturn(media);
        when(icon.getResources()).thenReturn(resource);
        when(media.getResources()).thenReturn(resource);
        when(resource.getMdpi()).thenReturn(mdpi);
        when(resource.getHdpi()).thenReturn(hdpi);
        when(resource.getXhdpi()).thenReturn(xhdpi);
        when(resource.getXxhdpi()).thenReturn(xxhdpi);
        when(resource.getXxxhdpi()).thenReturn(xxxhdpi);
        MemberModifier
                .field(NativeAd.class, "appUtil").set(
                nativeAd, appUtil);
        when(appUtil.checkTypeNetwork()).thenReturn(TYPE_3G);
        when(appUtil.getDeviceResolution()).thenReturn(XXHDPI);
        nativeAd.handleResponsegetlistAds(responseBody);
    }

    @Test
    public void handleResponseListadsType3gXXXHDPI() throws Exception {
        ResponseBody responseBody = ResponseBody
                .create(MediaType.parse("application/json"), "{\"status\":true,\"errors\":[],\"data\":{\"time_server\":1532922297,\"list_advertisements\":[{\"advertisement_id\":\"vNo19BPZJGlzEpD5\",\"application_key\":\"com.vintro.taobiettuot.haihinhmotchu\",\"age_tag\":3,\"region\":\"vi\",\"details\":{\"CtA\":{\"font\":\"Test\",\"size\":60,\"text\":\"Cài đặt ngay\",\"color\":\"#08D324\",\"text_size\":22,\"link_checking\":\"google.com\",\"background_color\":\"#EE0E0E\",\"token\":\"AKSyqQoK3WhnxFa09Epe_g\"},\"body\":{\"font\":\"TimesNewRoman\",\"size\":24,\"color\":\"#F2FF27\",\"content\":\"game cho người nghèo\"},\"icon\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/girl.jpg\",\"resources\":{\"hdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/hdpi_girl.jpg\",\"size\":\"72x72\"},\"mdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/mdpi_girl.jpg\",\"size\":\"48x48\"},\"xhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xhdpi_girl.jpg\",\"size\":\"96x96\"},\"xxhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xxhdpi_girl.jpg\",\"size\":\"144x144\"},\"xxxhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xxxhdpi_girl.jpg\",\"size\":\"192x192\"}}},\"title\":{\"font\":\"TimesNewRoman\",\"size\":28,\"color\":\"#0E71EE\",\"content\":\"Phiên bản đặc biệt hai hình một chữ hay\"}}}]}}");
        when(appPreference.getListAdsFromJson()).thenReturn(advertisementsArrayList);
        Mockito.when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        Mockito.when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).thenReturn(networkInfo);
        Mockito.when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(networkInfo);
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getDisplayMetrics()).thenReturn(displayMetrics);
        when(advertisementsArrayList.size()).thenReturn(2);
        when(advertisementsArrayList.get(anyInt())).thenReturn(advertisements);
        when(advertisements.getDetails()).thenReturn(details);
        when(details.getIcon()).thenReturn(icon);
        when(details.getMedia()).thenReturn(media);
        when(icon.getResources()).thenReturn(resource);
        when(media.getResources()).thenReturn(resource);
        when(resource.getMdpi()).thenReturn(mdpi);
        when(resource.getHdpi()).thenReturn(hdpi);
        when(resource.getXhdpi()).thenReturn(xhdpi);
        when(resource.getXxhdpi()).thenReturn(xxhdpi);
        when(resource.getXxxhdpi()).thenReturn(xxxhdpi);
        MemberModifier
                .field(NativeAd.class, "appUtil").set(
                nativeAd, appUtil);
        when(appUtil.checkTypeNetwork()).thenReturn(TYPE_3G);
        when(appUtil.getDeviceResolution()).thenReturn(XXXHDPI);
        nativeAd.handleResponsegetlistAds(responseBody);
    }

    @Test
    public void handleResponseListadsTypeWifiMDPI() throws Exception {
        ResponseBody responseBody = ResponseBody
                .create(MediaType.parse("application/json"), "{\"status\":true,\"errors\":[],\"data\":{\"time_server\":1532922297,\"list_advertisements\":[{\"advertisement_id\":\"vNo19BPZJGlzEpD5\",\"application_key\":\"com.vintro.taobiettuot.haihinhmotchu\",\"age_tag\":3,\"region\":\"vi\",\"details\":{\"CtA\":{\"font\":\"Test\",\"size\":60,\"text\":\"Cài đặt ngay\",\"color\":\"#08D324\",\"text_size\":22,\"link_checking\":\"google.com\",\"background_color\":\"#EE0E0E\",\"token\":\"AKSyqQoK3WhnxFa09Epe_g\"},\"body\":{\"font\":\"TimesNewRoman\",\"size\":24,\"color\":\"#F2FF27\",\"content\":\"game cho người nghèo\"},\"icon\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/girl.jpg\",\"resources\":{\"hdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/hdpi_girl.jpg\",\"size\":\"72x72\"},\"mdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/mdpi_girl.jpg\",\"size\":\"48x48\"},\"xhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xhdpi_girl.jpg\",\"size\":\"96x96\"},\"xxhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xxhdpi_girl.jpg\",\"size\":\"144x144\"},\"xxxhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xxxhdpi_girl.jpg\",\"size\":\"192x192\"}}},\"title\":{\"font\":\"TimesNewRoman\",\"size\":28,\"color\":\"#0E71EE\",\"content\":\"Phiên bản đặc biệt hai hình một chữ hay\"}}}]}}");
        when(appPreference.getListAdsFromJson()).thenReturn(advertisementsArrayList);
        Mockito.when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        Mockito.when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).thenReturn(networkInfo);
        Mockito.when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(networkInfo);
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getDisplayMetrics()).thenReturn(displayMetrics);
        when(advertisementsArrayList.size()).thenReturn(2);
        when(advertisementsArrayList.get(anyInt())).thenReturn(advertisements);
        when(advertisements.getDetails()).thenReturn(details);
        when(details.getIcon()).thenReturn(icon);
        when(details.getMedia()).thenReturn(media);
        when(icon.getResources()).thenReturn(resource);
        when(media.getResources()).thenReturn(resource);
        when(resource.getMdpi()).thenReturn(mdpi);
        when(resource.getHdpi()).thenReturn(hdpi);
        when(resource.getXhdpi()).thenReturn(xhdpi);
        when(resource.getXxhdpi()).thenReturn(xxhdpi);
        when(resource.getXxxhdpi()).thenReturn(xxxhdpi);
        MemberModifier
                .field(NativeAd.class, "appUtil").set(
                nativeAd, appUtil);
        when(appUtil.checkTypeNetwork()).thenReturn(TYPE_WIFI);
        when(appUtil.getDeviceResolution()).thenReturn(MDPI);
        nativeAd.handleResponsegetlistAds(responseBody);
    }

    @Test
    public void handleResponseListadsTypeWifiHDPI() throws Exception {
        ResponseBody responseBody = ResponseBody
                .create(MediaType.parse("application/json"), "{\"status\":true,\"errors\":[],\"data\":{\"time_server\":1532922297,\"list_advertisements\":[{\"advertisement_id\":\"vNo19BPZJGlzEpD5\",\"application_key\":\"com.vintro.taobiettuot.haihinhmotchu\",\"age_tag\":3,\"region\":\"vi\",\"details\":{\"CtA\":{\"font\":\"Test\",\"size\":60,\"text\":\"Cài đặt ngay\",\"color\":\"#08D324\",\"text_size\":22,\"link_checking\":\"google.com\",\"background_color\":\"#EE0E0E\",\"token\":\"AKSyqQoK3WhnxFa09Epe_g\"},\"body\":{\"font\":\"TimesNewRoman\",\"size\":24,\"color\":\"#F2FF27\",\"content\":\"game cho người nghèo\"},\"icon\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/girl.jpg\",\"resources\":{\"hdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/hdpi_girl.jpg\",\"size\":\"72x72\"},\"mdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/mdpi_girl.jpg\",\"size\":\"48x48\"},\"xhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xhdpi_girl.jpg\",\"size\":\"96x96\"},\"xxhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xxhdpi_girl.jpg\",\"size\":\"144x144\"},\"xxxhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xxxhdpi_girl.jpg\",\"size\":\"192x192\"}}},\"title\":{\"font\":\"TimesNewRoman\",\"size\":28,\"color\":\"#0E71EE\",\"content\":\"Phiên bản đặc biệt hai hình một chữ hay\"}}}]}}");
        when(appPreference.getListAdsFromJson()).thenReturn(advertisementsArrayList);
        Mockito.when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        Mockito.when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).thenReturn(networkInfo);
        Mockito.when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(networkInfo);
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getDisplayMetrics()).thenReturn(displayMetrics);
        when(advertisementsArrayList.size()).thenReturn(2);
        when(advertisementsArrayList.get(anyInt())).thenReturn(advertisements);
        when(advertisements.getDetails()).thenReturn(details);
        when(details.getIcon()).thenReturn(icon);
        when(details.getMedia()).thenReturn(media);
        when(icon.getResources()).thenReturn(resource);
        when(media.getResources()).thenReturn(resource);
        when(resource.getMdpi()).thenReturn(mdpi);
        when(resource.getHdpi()).thenReturn(hdpi);
        when(resource.getXhdpi()).thenReturn(xhdpi);
        when(resource.getXxhdpi()).thenReturn(xxhdpi);
        when(resource.getXxxhdpi()).thenReturn(xxxhdpi);
        MemberModifier
                .field(NativeAd.class, "appUtil").set(
                nativeAd, appUtil);
        when(appUtil.checkTypeNetwork()).thenReturn(TYPE_WIFI);
        when(appUtil.getDeviceResolution()).thenReturn(HDPI);
        nativeAd.handleResponsegetlistAds(responseBody);
    }

    @Test
    public void handleResponseListadsTypeWifiXHDPI() throws Exception {
        ResponseBody responseBody = ResponseBody
                .create(MediaType.parse("application/json"), "{\"status\":true,\"errors\":[],\"data\":{\"time_server\":1532922297,\"list_advertisements\":[{\"advertisement_id\":\"vNo19BPZJGlzEpD5\",\"application_key\":\"com.vintro.taobiettuot.haihinhmotchu\",\"age_tag\":3,\"region\":\"vi\",\"details\":{\"CtA\":{\"font\":\"Test\",\"size\":60,\"text\":\"Cài đặt ngay\",\"color\":\"#08D324\",\"text_size\":22,\"link_checking\":\"google.com\",\"background_color\":\"#EE0E0E\",\"token\":\"AKSyqQoK3WhnxFa09Epe_g\"},\"body\":{\"font\":\"TimesNewRoman\",\"size\":24,\"color\":\"#F2FF27\",\"content\":\"game cho người nghèo\"},\"icon\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/girl.jpg\",\"resources\":{\"hdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/hdpi_girl.jpg\",\"size\":\"72x72\"},\"mdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/mdpi_girl.jpg\",\"size\":\"48x48\"},\"xhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xhdpi_girl.jpg\",\"size\":\"96x96\"},\"xxhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xxhdpi_girl.jpg\",\"size\":\"144x144\"},\"xxxhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xxxhdpi_girl.jpg\",\"size\":\"192x192\"}}},\"title\":{\"font\":\"TimesNewRoman\",\"size\":28,\"color\":\"#0E71EE\",\"content\":\"Phiên bản đặc biệt hai hình một chữ hay\"}}}]}}");
        when(appPreference.getListAdsFromJson()).thenReturn(advertisementsArrayList);
        Mockito.when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        Mockito.when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).thenReturn(networkInfo);
        Mockito.when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(networkInfo);
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getDisplayMetrics()).thenReturn(displayMetrics);
        when(advertisementsArrayList.size()).thenReturn(2);
        when(advertisementsArrayList.get(anyInt())).thenReturn(advertisements);
        when(advertisements.getDetails()).thenReturn(details);
        when(details.getIcon()).thenReturn(icon);
        when(details.getMedia()).thenReturn(media);
        when(icon.getResources()).thenReturn(resource);
        when(media.getResources()).thenReturn(resource);
        when(resource.getMdpi()).thenReturn(mdpi);
        when(resource.getHdpi()).thenReturn(hdpi);
        when(resource.getXhdpi()).thenReturn(xhdpi);
        when(resource.getXxhdpi()).thenReturn(xxhdpi);
        when(resource.getXxxhdpi()).thenReturn(xxxhdpi);
        MemberModifier
                .field(NativeAd.class, "appUtil").set(
                nativeAd, appUtil);
        when(appUtil.checkTypeNetwork()).thenReturn(TYPE_WIFI);
        when(appUtil.getDeviceResolution()).thenReturn(XHDPI);
        nativeAd.handleResponsegetlistAds(responseBody);
    }

    @Test
    public void handleResponseListadsTypeWifiXXHDPI() throws Exception {
        ResponseBody responseBody = ResponseBody
                .create(MediaType.parse("application/json"), "{\"status\":true,\"errors\":[],\"data\":{\"time_server\":1532922297,\"list_advertisements\":[{\"advertisement_id\":\"vNo19BPZJGlzEpD5\",\"application_key\":\"com.vintro.taobiettuot.haihinhmotchu\",\"age_tag\":3,\"region\":\"vi\",\"details\":{\"CtA\":{\"font\":\"Test\",\"size\":60,\"text\":\"Cài đặt ngay\",\"color\":\"#08D324\",\"text_size\":22,\"link_checking\":\"google.com\",\"background_color\":\"#EE0E0E\",\"token\":\"AKSyqQoK3WhnxFa09Epe_g\"},\"body\":{\"font\":\"TimesNewRoman\",\"size\":24,\"color\":\"#F2FF27\",\"content\":\"game cho người nghèo\"},\"icon\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/girl.jpg\",\"resources\":{\"hdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/hdpi_girl.jpg\",\"size\":\"72x72\"},\"mdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/mdpi_girl.jpg\",\"size\":\"48x48\"},\"xhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xhdpi_girl.jpg\",\"size\":\"96x96\"},\"xxhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xxhdpi_girl.jpg\",\"size\":\"144x144\"},\"xxxhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xxxhdpi_girl.jpg\",\"size\":\"192x192\"}}},\"title\":{\"font\":\"TimesNewRoman\",\"size\":28,\"color\":\"#0E71EE\",\"content\":\"Phiên bản đặc biệt hai hình một chữ hay\"}}}]}}");
        when(appPreference.getListAdsFromJson()).thenReturn(advertisementsArrayList);
        Mockito.when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        Mockito.when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).thenReturn(networkInfo);
        Mockito.when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(networkInfo);
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getDisplayMetrics()).thenReturn(displayMetrics);
        when(advertisementsArrayList.size()).thenReturn(2);
        when(advertisementsArrayList.get(anyInt())).thenReturn(advertisements);
        when(advertisements.getDetails()).thenReturn(details);
        when(details.getIcon()).thenReturn(icon);
        when(details.getMedia()).thenReturn(media);
        when(icon.getResources()).thenReturn(resource);
        when(media.getResources()).thenReturn(resource);
        when(resource.getMdpi()).thenReturn(mdpi);
        when(resource.getHdpi()).thenReturn(hdpi);
        when(resource.getXhdpi()).thenReturn(xhdpi);
        when(resource.getXxhdpi()).thenReturn(xxhdpi);
        when(resource.getXxxhdpi()).thenReturn(xxxhdpi);
        MemberModifier
                .field(NativeAd.class, "appUtil").set(
                nativeAd, appUtil);
        when(appUtil.checkTypeNetwork()).thenReturn(TYPE_WIFI);
        when(appUtil.getDeviceResolution()).thenReturn(XXHDPI);
        nativeAd.handleResponsegetlistAds(responseBody);
    }

    @Test
    public void handleResponseListadsTypeWifiXXXHDPI() throws Exception {
        ResponseBody responseBody = ResponseBody
                .create(MediaType.parse("application/json"), "{\"status\":true,\"errors\":[],\"data\":{\"time_server\":1532922297,\"list_advertisements\":[{\"advertisement_id\":\"vNo19BPZJGlzEpD5\",\"application_key\":\"com.vintro.taobiettuot.haihinhmotchu\",\"age_tag\":3,\"region\":\"vi\",\"details\":{\"CtA\":{\"font\":\"Test\",\"size\":60,\"text\":\"Cài đặt ngay\",\"color\":\"#08D324\",\"text_size\":22,\"link_checking\":\"google.com\",\"background_color\":\"#EE0E0E\",\"token\":\"AKSyqQoK3WhnxFa09Epe_g\"},\"body\":{\"font\":\"TimesNewRoman\",\"size\":24,\"color\":\"#F2FF27\",\"content\":\"game cho người nghèo\"},\"icon\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/girl.jpg\",\"resources\":{\"hdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/hdpi_girl.jpg\",\"size\":\"72x72\"},\"mdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/mdpi_girl.jpg\",\"size\":\"48x48\"},\"xhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xhdpi_girl.jpg\",\"size\":\"96x96\"},\"xxhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xxhdpi_girl.jpg\",\"size\":\"144x144\"},\"xxxhdpi\":{\"url\":\"http://crossdev.ecomobile.vn/assets/uploads/icon/26/xxxhdpi_girl.jpg\",\"size\":\"192x192\"}}},\"title\":{\"font\":\"TimesNewRoman\",\"size\":28,\"color\":\"#0E71EE\",\"content\":\"Phiên bản đặc biệt hai hình một chữ hay\"}}}]}}");
        when(appPreference.getListAdsFromJson()).thenReturn(advertisementsArrayList);
        Mockito.when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        Mockito.when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).thenReturn(networkInfo);
        Mockito.when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(networkInfo);
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getDisplayMetrics()).thenReturn(displayMetrics);
        when(advertisementsArrayList.size()).thenReturn(2);
        when(advertisementsArrayList.get(anyInt())).thenReturn(advertisements);
        when(advertisements.getDetails()).thenReturn(details);
        when(details.getIcon()).thenReturn(icon);
        when(details.getMedia()).thenReturn(media);
        when(icon.getResources()).thenReturn(resource);
        when(media.getResources()).thenReturn(resource);
        when(resource.getMdpi()).thenReturn(mdpi);
        when(resource.getHdpi()).thenReturn(hdpi);
        when(resource.getXhdpi()).thenReturn(xhdpi);
        when(resource.getXxhdpi()).thenReturn(xxhdpi);
        when(resource.getXxxhdpi()).thenReturn(xxxhdpi);
        MemberModifier
                .field(NativeAd.class, "appUtil").set(
                nativeAd, appUtil);
        when(appUtil.checkTypeNetwork()).thenReturn(TYPE_WIFI);
        when(appUtil.getDeviceResolution()).thenReturn(XXXHDPI);
        when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        when(appUtil.isOnline()).thenReturn(true);
        nativeAd.setListener(adListener);
        when(advertisementsArrayList.get(anyInt())).thenReturn(advertisements);
        when(advertisements.getAdvertisement_id()).thenReturn("defaultValue");
        when(appPreference.getKeyTimeServer()).thenReturn("125232323");
        when(advertisementsArrayList.get(anyInt())).thenReturn(advertisements);
        when(advertisements.getDetails()).thenReturn(details);
        when(details.getCtA()).thenReturn(cta);
        when(cta.getToken()).thenReturn("defaultVadlue");
        when(crossPromote.getApiService()).thenReturn(apiservice);
        ResponseBody responseBodyPostRequest = ResponseBody
                .create(MediaType.parse("application/json"), "{\"status\":true,\"errors\":[],\"data\":{\"request_time\":\"1532340918\",\"advertisement_id\":\"LGzX4YyazledO0vD\",\"token\":\"LSyHtmuHGq-Npq6nGfAEOw\",\"time_server\":\"1532340917\"}}");
        doReturn(Observable.just(responseBodyPostRequest)).when(apiservice).postRequestAds(any(),new HashMap<>());
        nativeAd.handleResponsegetlistAds(responseBody);
    }

    @Test
    public void handleErrrorRequest() {
        when(appPreference.getListApiBackup()).thenReturn(backUpManagerArrayList);
        when(backUpManagerArrayList.size()).thenReturn(2);
        nativeAd.handleErrorRequest(new Throwable());
    }

    @Test
    public void handleErrrorClick() {
        when(appPreference.getListApiBackup()).thenReturn(backUpManagerArrayList);
        when(backUpManagerArrayList.size()).thenReturn(2);
        nativeAd.handleErrorClick(new Throwable());
    }

    @Test
    public void handleErrorListAds() {
        nativeAd.setListener(adListener);
        nativeAd.handleErrorgetlistAds(new Throwable());
    }

    @Test
    public void postViewAds() throws Exception {
        MemberModifier
                .field(NativeAd.class, "advertisements").set(
                nativeAd, advertisementsArrayList);
        when(advertisementsArrayList.get(anyInt())).thenReturn(advertisements);
        when(advertisements.getAdvertisement_id()).thenReturn("defaultValue");
        when(appPreference.getKeyTimeServer()).thenReturn("125232323");
        when(advertisementsArrayList.get(anyInt())).thenReturn(advertisements);
        when(advertisements.getDetails()).thenReturn(details);
        when(details.getCtA()).thenReturn(cta);
        when(cta.getToken()).thenReturn("defaultVadlue");
        when(crossPromote.getApiService()).thenReturn(apiservice);
        ResponseBody responseBodyPostRequest = ResponseBody
                .create(MediaType.parse("application/json"), "{\"status\":true,\"errors\":[],\"data\":{\"token\":\"1tPYtK7lluZX2P0mj8iUNw\",\"advertisement_id\":\"73Ja0NBp7QRA294b\",\"time_server\":\"1532397830\",\"view_time\":\"1532397910\"}}");
        doReturn(Observable.just(responseBodyPostRequest)).when(apiservice).postViewAds(any(),new HashMap<>());
        nativeAd.postViewAds(anyInt());
    }

    @Test
    public void getDataListAdsOffline() {
        when(advertisementsArrayList.size()).thenReturn(2);
        nativeAd.setListener(adListener);
        nativeAd.setDataOffline(advertisementsArrayList);
    }

    @Test
    public void handleErrorRequestViewAds() {
        when(appPreference.getListApiBackup()).thenReturn(backUpManagerArrayList);
        when(backUpManagerArrayList.size()).thenReturn(2);
        nativeAd.handleErrorView(new Throwable());
    }

    @Test
    public void checkDataValueTitle() throws Exception {
        MemberModifier
                .field(NativeAd.class, "nativeAdViewHolder").set(
                nativeAd, nativeAdViewHolder);
        when(nativeAdViewHolder.getTitleText()).thenReturn(textView);
        when(textView.getText()).thenReturn("defaultValue");
        nativeAd.checkDataValue();
    }

    @Test
    public void checkDataValueBody() throws Exception {
        MemberModifier
                .field(NativeAd.class, "nativeAdViewHolder").set(
                nativeAd, nativeAdViewHolder);
        when(nativeAdViewHolder.getBodyText()).thenReturn(textView);
        when(textView.getText()).thenReturn("defaultValue");
        nativeAd.checkDataValue();
    }

    @Test
    public void checkDataValueCTA() throws Exception {
        MemberModifier
                .field(NativeAd.class, "nativeAdViewHolder").set(
                nativeAd, nativeAdViewHolder);
        when(nativeAdViewHolder.getCallToActionView()).thenReturn(button);
        when(button.getText()).thenReturn("defaultValue");
        nativeAd.checkDataValue();
    }

    @Test
    public void checkDataValueIcon() throws Exception {
        MemberModifier
                .field(NativeAd.class, "nativeAdViewHolder").set(
                nativeAd, nativeAdViewHolder);
        when(nativeAdViewHolder.getIcon()).thenReturn(imageView);
        when(imageView.getDrawable()).thenReturn(drawable);
        nativeAd.checkDataValue();
    }

    @Test
    public void checkDataValueMedia() throws Exception {
        MemberModifier
                .field(NativeAd.class, "nativeAdViewHolder").set(
                nativeAd, nativeAdViewHolder);
        when(nativeAdViewHolder.getAdMedia()).thenReturn(imageView);
        when(imageView.getDrawable()).thenReturn(drawable);
        nativeAd.checkDataValue();
    }

    @Test
    public void handleResponseErrorListads() {
        ResponseBody responseBody = ResponseBody
                .create(MediaType.parse("application/json"), "{\"status\":false,\"errors\":{\"code\":10003,\"message\":\"Can't find application with this com.vintro.taobiettuot.haihinhmotchuasdf\"},\"data\":[]}");
        nativeAd.handleResponsegetlistAds(responseBody);
    }

    @Test
    public void handleResponseExceptionListadsTypeWifiXXXHDPI() {
        ResponseBody responseBody = ResponseBody
                .create(MediaType.parse("application/json"), "defaultValue");
        nativeAd.handleResponsegetlistAds(responseBody);
    }
    @Test
    public void setData(){
        nativeAd.setData("packaname","region");
    }
}