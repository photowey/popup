package com.photowey.popup.http.transport.okhttp.client;

import com.photowey.component.common.exception.ExceptionThrower;
import com.photowey.popup.http.api.request.HttpRequest;
import com.photowey.popup.http.api.response.HttpResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * {@code OkHttpClientExecutor}
 *
 * {@code OkHttp} executor of {@link com.photowey.popup.http.api.client.HttpClient}
 *
 * @author photowey
 * @date 2023/01/08
 * @since 1.0.0
 */
public class OkHttpClientExecutor extends OkHttpClientExecutorAdaptor {

    private final okhttp3.OkHttpClient okHttpClient;

    public OkHttpClientExecutor() {
        // TODO
        this.okHttpClient = new OkHttpClient();
    }

    @Override
    public byte[] downloadBins(String url) throws IOException {
        HttpRequest httpRequest = this.buildDownloadRequest(url);
        try (Response okHttpResponse = this.executeDownload(httpRequest)) {
            if (okHttpResponse.body() != null) {
                return Objects.requireNonNull(okHttpResponse.body()).bytes();
            }
        } catch (Exception e) {
            throw ExceptionThrower.throwUnchecked(e);
        }

        // return null

        return new byte[0];
    }

    @Override
    public InputStream doDownload(HttpRequest httpRequest) {
        // TODO
        try (Response okHttpResponse = this.executeDownload(httpRequest)) {
            if (okHttpResponse.body() != null) {
                return Objects.requireNonNull(okHttpResponse.body()).byteStream();
            }
        } catch (Exception e) {
            throw ExceptionThrower.throwUnchecked(e);
        }

        return null;
    }

    @Override
    public <T> HttpResponse<T> execute(HttpRequest request, Class<T> responseClass) {
        // TODO

        return null;
    }

    private Request buildOkHttpRequest(HttpRequest request) {
        return null;
    }

    private Response executeDownload(HttpRequest httpRequest) {
        Request okHttpRequest = this.buildOkHttpRequest(httpRequest);
        try {
            // preHandle
            Response okHttpResponse = okHttpClient
                    .newCall(okHttpRequest)
                    .execute();
            // posHandle

            if (super.isSuccessful(okHttpResponse.code())) {
                // TODO
            }

            return okHttpResponse;
        } catch (Exception e) {
            throw ExceptionThrower.throwUnchecked(e);
        }
    }
}
