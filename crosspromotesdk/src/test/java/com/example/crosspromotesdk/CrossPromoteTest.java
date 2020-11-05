package com.example.crosspromotesdk;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.crosspromotesdk.data.BackUpManager;
import com.example.crosspromotesdk.network.Apiservice;
import com.example.crosspromotesdk.util.CrossPromote;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CrossPromote.class})
@PowerMockIgnore("javax.net.ssl.*")
public class CrossPromoteTest {
    private static final int TYPE_REQUEST = 1;
    private static final int TYPE_VIEW = 2;
    private static final int TYPE_CLICK = 3;
    @Mock
    Context context;
    @Mock
    SharedPreferences sharedPrefs;
    CrossPromote crossPromote;
    @Mock(answer = Answers.RETURNS_SELF)
    SharedPreferences.Editor mEditor;
    @Mock
    Apiservice apiservice;
    @Mock
    ArrayList<BackUpManager> backUpManagerArrayListTemp;
    private ArrayList<BackUpManager> backUpManagerArrayList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        apiservice = mock(Apiservice.class);
        mEditor = mock(SharedPreferences.Editor.class, RETURNS_DEEP_STUBS);
        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPrefs);
        when(sharedPrefs.edit()).thenReturn(mEditor);
        when(mEditor.putString(anyString(), anyString())).thenReturn(mEditor);
        when(mEditor.commit()).thenReturn(true);
        when(context.getApplicationContext()).thenReturn(context);
        crossPromote = new CrossPromote(context);
    }

    @Test
    public void TestInit() {
        when(context.getApplicationContext()).thenReturn(context);
        when(sharedPrefs.getString(anyString(), anyString())).thenReturn("[{apiMiss:{time_server:1532341193,token:DnIs8eekkhuA0f_3CfeNFA,advertisement_id:6E1NJVQWOQrDpn4X,request_time:1532341193},type:1}]");
        CrossPromote.init(context);
    }

    @Test
    public void TestgetApiService() {
        assertNotNull(crossPromote.getApiService());
    }

    @Test
    public void TestsetApiService() {
        crossPromote.setApiService(crossPromote.getApiService());
    }

    @Test
    public void TestsetPackageName() {
        crossPromote.setPackageName("google.com");
        assertNotEquals("", crossPromote.getPackageName());
    }

    //json api request
    //{"status":true,"errors":[],"data":{"request_time":"1532340918","advertisement_id":"LGzX4YyazledO0vD","token":"LSyHtmuHGq-Npq6nGfAEOw","time_server":"1532340917"}}
    @Test
    public void TestPostRequestApiSuccess() throws Exception {
        MemberModifier
                .field(CrossPromote.class, "listApiBackup").set(
                crossPromote, backUpManagerArrayListTemp);
        ResponseBody responseBody = ResponseBody
                .create(MediaType.parse("application/json"), "{status:true,errors:[],data:{request_time:1532340918,advertisement_id:LGzX4YyazledO0vD,token:LSyHtmuHGq-Npq6nGfAEOw,time_server:1532340917}}");
        Apiservice mockedApiService = Mockito.mock(Apiservice.class);
        when(mockedApiService.postRequestAds(anyString(),new HashMap<>())).thenReturn(Observable.just(responseBody));
        when(backUpManagerArrayListTemp.size()).thenReturn(2);
        crossPromote.handlerResponseRequest(responseBody);
        assertNotNull(responseBody);
    }

    @Test
    public void TestPostRequestApiException() {
        ResponseBody responseBody = ResponseBody
                .create(MediaType.parse("application/json"), "{statuss:true,errors:[],data:{request_time:1532340918,advertisement_id:LGzX4YyazledO0vD,token:LSyHtmuHGq-Npq6nGfAEOw,time_server:1532340917}}");
        Apiservice mockedApiService = Mockito.mock(Apiservice.class);
        when(mockedApiService.postRequestAds(anyString(),new HashMap<>())).thenReturn(Observable.just(responseBody));
        crossPromote.handlerResponseRequest(responseBody);
        assertNotNull(responseBody);
    }

    @Test
    public void TestPostRequestApiFailed() {
        Throwable throwable = Mockito.mock(Throwable.class);
        when(apiservice.postRequestAds(any(),new HashMap<>())).thenReturn(Observable.error(new IOException()));
        crossPromote.handleErrorRequest(throwable);
        assertNotNull(throwable);
    }

    //json api view
    //{"status":true,"errors":[],"data":{"token":"1tPYtK7lluZX2P0mj8iUNw","advertisement_id":"73Ja0NBp7QRA294b","time_server":"1532397830","view_time":"1532397910"}}
    @Test
    public void TestPostViewApi() {
        ResponseBody responseBody = ResponseBody
                .create(MediaType.parse("application/json"), "{status:true,errors:[],data:{token:1tPYtK7lluZX2P0mj8iUNw,advertisement_id:73Ja0NBp7QRA294b,time_server:1532397830,view_time:1532397910}}");
        Apiservice mockedApiService = Mockito.mock(Apiservice.class);
        when(mockedApiService.postViewAds(anyString(),new HashMap<>())).thenReturn(Observable.just(responseBody));
        crossPromote.handlerResponseView(responseBody);
        assertNotNull(responseBody);
    }

    @Test
    public void TestPostViewApiException() throws Exception {
        MemberModifier
                .field(CrossPromote.class, "listApiBackup").set(
                crossPromote, backUpManagerArrayListTemp);
        ResponseBody responseBody = ResponseBody
                .create(MediaType.parse("application/json"), "{statuss:true,errors:[],data:{token:1tPYtK7lluZX2P0mj8iUNw,advertisement_id:73Ja0NBp7QRA294b,time_server:1532397830,view_time:1532397910}}");
        Apiservice mockedApiService = Mockito.mock(Apiservice.class);
        when(mockedApiService.postViewAds("",new HashMap<>())).thenReturn(Observable.just(responseBody));
        when(backUpManagerArrayListTemp.size()).thenReturn(2);
        crossPromote.handlerResponseView(responseBody);
    }

    @Test
    public void TestViewApiFailed() {
        Throwable throwable = Mockito.mock(Throwable.class);
        when(apiservice.postViewAds(any(),new HashMap<>())).thenReturn(Observable.error(new IOException()));
        crossPromote.handleErrorView(throwable);
        assertNotNull(throwable);
    }

    //json api click
    //{"status":true,"errors":[],"data":{"click_time":"1532398075","token":"1tPYtK7lluZX2P0mj8iUNw","advertisement_id":"73Ja0NBp7QRA294b","time_server":"1532397830"}}
    @Test
    public void TestPostClickApi() {
        ResponseBody responseBody = ResponseBody
                .create(MediaType.parse("application/json"), "{status:true,errors:[],data:{click_time:1532398075,token:1tPYtK7lluZX2P0mj8iUNw,advertisement_id:73Ja0NBp7QRA294b,time_server:1532397830}}");
        Apiservice mockedApiService = Mockito.mock(Apiservice.class);
        when(mockedApiService.postClickAds("",new HashMap<>())).thenReturn(Observable.just(responseBody));
        crossPromote.handlerResponseClick(responseBody);
        assertNotNull(responseBody);
    }

    @Test
    public void TestPostClickApiException() throws Exception {
        MemberModifier
                .field(CrossPromote.class, "listApiBackup").set(
                crossPromote, backUpManagerArrayListTemp);
        ResponseBody responseBody = ResponseBody
                .create(MediaType.parse("application/json"), "{statuss:true,errors:[],data:{click_time:1532398075,token:1tPYtK7lluZX2P0mj8iUNw,advertisement_id:73Ja0NBp7QRA294b,time_server:1532397830}}");
        Apiservice mockedApiService = Mockito.mock(Apiservice.class);
        when(mockedApiService.postClickAds("",new HashMap<>())).thenReturn(Observable.just(responseBody));
        when(backUpManagerArrayListTemp.size()).thenReturn(2);
        crossPromote.handlerResponseClick(responseBody);
        assertNotNull(responseBody);
    }

    @Test
    public void TestClickApiFailed() {
        Throwable throwable = Mockito.mock(Throwable.class);
        when(apiservice.postClickAds(any(),new HashMap<>())).thenReturn(Observable.error(new IOException()));
        crossPromote.handleErrorClick(throwable);
        assertNotNull(throwable);
    }

    @Test
    public void postClickAds() {
        crossPromote.postClickAds(new HashMap<>(), TYPE_CLICK);
    }

    @Test
    public void postViewAds() {
        crossPromote.postViewAds(new HashMap<>(), TYPE_VIEW);
    }

    @Test
    public void postRequestAds() {
        crossPromote.postRequetAds(new HashMap<>(), TYPE_REQUEST);
    }

    @Test
    public void backupApi() throws Exception {
        backUpManagerArrayList = new ArrayList<>();
        backUpManagerArrayList.add(new BackUpManager(new HashMap<>(), TYPE_REQUEST));
        backUpManagerArrayList.add(new BackUpManager(new HashMap<>(), TYPE_VIEW));
        backUpManagerArrayList.add(new BackUpManager(new HashMap<>(), TYPE_CLICK));
        MemberModifier
                .field(CrossPromote.class, "listBackupManager").set(
                crossPromote, backUpManagerArrayList);
        crossPromote.backupApi();
    }

    @Test
    public void getInstance() {
        CrossPromote.getInstance();
    }

}
