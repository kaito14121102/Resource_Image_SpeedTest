package com.gdt.speedtest.data.remote;

import com.gdt.speedtest.data.model.Address;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiAddress {

    @GET("servers?engine=js")
    Call<List<Address>> getAddressList();

    @GET("servers?engine=js")
    Call<Object> requestForDownload();

    @GET("servers?engine=js")
    Call<Object> requestForUpload();
}
