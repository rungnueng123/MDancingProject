package com.mocom.com.mdancingproject.config;

public class config {

    public config(){

    }
    //บ้านศรีพงษ์
//    public static final String DATA_URL = "http://192.168.100.5/MDancingPHP/";
    //ที่ทำงาน
//    public static final String DATA_URL = "http://10.10.30.92/MDancingPHP/";
    //บ้านแปะ
//    public static final String DATA_URL = "http://192.168.1.37/MDancingPHP/";
    //host
    public static final String DATA_URL = "https://danceschool.matchbox-station.com/MDancingPHP/";

    //Youtube
    private static final String YOUTUBE_KEY = "AIzaSyCxHiH9LBRrBpVtRREchulc_3e5hSdBdaA";

    public static String getYoutubeKey() {
        return YOUTUBE_KEY;
    }

    public static final String HOST_URL = "https://danceschool.matchbox-station.com/";

    //Omise
    public static final String OMISE_PUBLIC_KEY = "pkey_test_5ex0mvopz0piutpxdzw";
    public static final String OMISE_SECRET_KEY = "skey_test_5ex0mvoq91lh9fi9jty";
    public static final String OMISE_API_VERSION = "2015-11-17";
}
