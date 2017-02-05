package com.thulium.beetobee.WebService;

import retrofit.RestAdapter;

/**
 * Created by Alex on 17/01/2017.
 */

public class RestService {

    private static final String URL = "https://www.beetobee.fr:8443";
    private RequeteService apiService;


    public RestService()
    {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        apiService = restAdapter.create(RequeteService.class);
    }

    public RequeteService getService() { return apiService; }

}
