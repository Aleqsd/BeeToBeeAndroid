package com.thulium.beetobee;

import retrofit.RestAdapter;

/**
 * Created by Alex on 17/01/2017.
 */

public class RestService {

    private static final String URL = "http://10.0.2.2:9001";
    private retrofit.RestAdapter restAdapter;
    private InstituteService apiService;

    public RestService()
    {
        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        apiService = restAdapter.create(InstituteService.class);
    }

    public InstituteService getService() { return apiService; }

}
