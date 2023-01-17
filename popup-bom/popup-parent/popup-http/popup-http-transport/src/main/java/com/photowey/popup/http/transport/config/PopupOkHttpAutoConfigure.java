/*
 * Copyright © 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.photowey.popup.http.transport.config;

import com.photowey.popup.http.transport.okhttp.interceptor.OkhttpInterceptor;
import com.photowey.popup.http.transport.property.PopupHttpProperties;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * {@code PopupOkHttpAutoConfigure}
 *
 * @author photowey
 * @date 2023/01/03
 * @since 1.0.0
 */
@AutoConfiguration(after = PopupHttpAutoConfigure.class)
public class PopupOkHttpAutoConfigure implements BeanFactoryAware {

    private ListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ListableBeanFactory) beanFactory;
    }

    @Bean
    @ConditionalOnMissingBean
    public OkHttpClient okHttpClient(
            SSLSocketFactory sslSocketFactory,
            HostnameVerifier hostnameVerifier,
            X509TrustManager trustManager
    ) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        this.addInterceptors(builder);

        PopupHttpProperties httpProperties = this.beanFactory.getBean(PopupHttpProperties.class);
        ConnectionPool connectionPool = this.beanFactory.getBean(ConnectionPool.class);

        return builder.sslSocketFactory(sslSocketFactory, trustManager)
                // java.io.EOFException: \n not found: limit=0 content=…
                // @link https://github.com/square/okhttp/issues/5390
                .retryOnConnectionFailure(true)
                .connectionPool(connectionPool)
                .connectTimeout(httpProperties.getOkHttp().getConnectTimeout(), TimeUnit.SECONDS)
                .readTimeout(httpProperties.getOkHttp().getReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(httpProperties.getOkHttp().getWriteTimeout(), TimeUnit.SECONDS)
                .hostnameVerifier(hostnameVerifier)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public ConnectionPool okHttpConnectionPool() {
        PopupHttpProperties httpProperties = this.beanFactory.getBean(PopupHttpProperties.class);

        return new ConnectionPool(
                httpProperties.getOkHttp().getMaxIdleConnections(),
                httpProperties.getOkHttp().getKeepAliveDuration(), TimeUnit.SECONDS
        );
    }

    private void addInterceptors(OkHttpClient.Builder builder) {
        Map<String, OkhttpInterceptor> beans = this.beanFactory.getBeansOfType(OkhttpInterceptor.class);
        beans.forEach((k, v) -> {
            if (v.isNetworkInterceptor()) {
                builder.addNetworkInterceptor(v);
            } else {
                builder.addInterceptor(v);
            }
        });
    }
}
