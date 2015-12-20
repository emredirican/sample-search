package com.kallavistudios.samplesearch.rest.logging;


import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import timber.log.Timber;

public final class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long requestTime = System.nanoTime();
        Timber.i("Request sent %s on %s\n%s", request.url(), chain.connection(), request.headers());

        Response response;
        Response newResponse = null;
        String responseBodyString = "";
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            response = null;
            responseBodyString = e.getMessage();
        }
        if (response == null) {
            Timber.v("RESPONSE BODY:\n%s\n", responseBodyString);
            return new Response.Builder().body(ResponseBody.create(request.body().contentType(), ""))
                    .build();
        } else {
            ResponseBody responseBody = response.body();
            responseBodyString = responseBody.string();

            newResponse = response.newBuilder()
                    .body(
                            ResponseBody.create(responseBody.contentType(), responseBodyString.getBytes("UTF-8")))
                    .build();
            long responseTime = System.nanoTime();
            Timber.i("Response received for %s in %.1fms%n%s", response.request().url(),
                    (responseTime - requestTime) / 1e6d, response.headers());
            Timber.v("RESPONSE BODY:\n%s\n", responseBodyString);
            return newResponse;
        }
    }

}
