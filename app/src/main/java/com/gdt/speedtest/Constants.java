package com.gdt.speedtest;

/**
 * Created by shivam on 29/5/17.
 */
public interface Constants {
    String POLICY_LINK="http://gdtmobileapp.blogspot.com/2020/06/policy-internet-speed-test.html";
    String URL_INHOUSE_ADS="http://gdtmobileapp.blogspot.com/2020/06/inhouse-ads.html";

    String TYPE_IMAGE = ".webp";
    //Package name Cross app
    String PACKAGE_WATERMARK_PHOTO="com.gdt.addwatermark";
    String PACKAGE_APPLOCK="com.GDT.applock.applockfingerprint";

    String PREF_FILE_NAME = "speed_test_pref_file";
    String BASE_URL = "http://www.speedtest.net/api/js/";
    String DATA = "data";
    String SAVE_PATH = "/SpeedTest/";
    String DEFAULT_IMAGE_NAME = "screenshot.png";
    String DATA_TYPE = "text/plain";
    String PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id=";
    String COMPLETE_KEY = "complete";
    String PING_KEY = "ping";
    String NOT_CONNECT_INTERNET = "Not Connect";
    String WIFI = "WIFI";
    String CONNECT_2G = "2G";
    String CONNECT_3G = "3G";
    String CONNECT_4G = "4G";
    String UNKNOW = "Unknown";
    String FORMAT = "#.##";
    int STATUS_NOT_START = 0;
    int STATUS_DOWNLOAD = 1;
    int STATUS_UPLOAD = 2;

    String DATABASE_NAME = "notes-db-encrypted";
    String DATABASE_PASS = "super-secret";
    String DATABASE_QUERY = "PRAGMA cipher_migrate";
    String MIGRATED = "migrated";

    String EXTRA_KEY_ADS_RESULT="EXTRA_KEY_ADS_RESULT";
    String EXTRA_KEY_ADS_SETTINGS="EXTRA_KEY_ADS_SETTINGS";
    String EXTRA_KEY_ADS_FULL_CLICK_GO="EXTRA_KEY_ADS_FULL_CLICK_GO";

    int MAX_COUNT_CLICK_GO=2;

    String MIGRATE_NAME = "first_migrate";

    String EXTRA_ADS_CLICK_RATE_APP = "EXTRA_ADS_CLICK_RATE_APP";
    int MAX_COUNT_RATE_APP=2;


    String PRE_REMOVED_ADS="PRE_REMOVED_ADS";
    String PRE_REMOVED_UNLIMITED_TEST="PRE_REMOVED_UNLIMITED_TEST";
    String PRE_UNLIMITED_TEST="PRE_UNLIMITED_TEST";

    String MAIL_TYPE = "text/email";
    String[] MAIL_LIST = {"luutuanminh.cntt1@gmail.com"};
}
