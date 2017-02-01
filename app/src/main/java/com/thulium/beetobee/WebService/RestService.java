package com.thulium.beetobee.WebService;

import retrofit.RestAdapter;

/**
 * Created by Alex on 17/01/2017.
 */

public class RestService {

    private static final String URL = "https://www.beetobee.fr:8443";
    private retrofit.RestAdapter restAdapter;
    private RequeteService apiService;


    public RestService()
    {
        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        apiService = restAdapter.create(RequeteService.class);
    }

    public RequeteService getService() { return apiService; }

}
