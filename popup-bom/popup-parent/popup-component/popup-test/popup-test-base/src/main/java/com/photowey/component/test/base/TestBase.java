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
package com.photowey.component.test.base;

import com.alibaba.fastjson2.JSON;
import com.photowey.component.common.constant.PopupConstants;
import com.photowey.component.common.util.ObjectUtils;
import com.photowey.component.test.base.query.DefaultQuery;
import jakarta.servlet.Filter;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * {@code TestBase}
 *
 * @author photowey
 * @date 2023/01/20
 * @since 1.0.0
 */
public abstract class TestBase {

    public static final String HEALTH_API = "/healthz";

    @Autowired
    protected WebApplicationContext applicationContext;

    @Autowired
    private Filter springSecurityFilterChain;

    protected MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        this.mockMvc();
        this.mockUser();
    }

    @AfterEach
    void tearDown() {

    }

    private void mockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.applicationContext)
                .addFilter(this.springSecurityFilterChain)
                .build();
    }

    private void mockUser() {

    }

    // -----------------------------------------------------------------------------------------------------------------

    protected void doHealthz() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(HEALTH_API))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

    }

    // -----------------------------------------------------------------------------------------------------------------

    protected <T> String doPostRequest(T payload, String route) throws Exception {
        return this.doPostRequest(payload, route, (actions) -> {
            try {
                actions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(PopupConstants.API_OK));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    protected <T> String doPostRequestB(T payload, String route, Consumer<MockHttpServletRequestBuilder> fn) throws Exception {
        return this.doPostRequest(payload, route, fn, (actions) -> {
            try {
                actions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(PopupConstants.API_OK));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    protected <T> String doPostRequest(T payload, String route, Consumer<ResultActions> fx) throws Exception {
        return this.doPostRequest(payload, route, (builder) -> {
        }, fx);
    }

    protected <T> String doPostRequest(
            T payload,
            String route,
            Consumer<MockHttpServletRequestBuilder> fn,
            Consumer<ResultActions> fx) throws Exception {

        String body = JSON.toJSONString(payload);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(route)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body);
        fn.accept(requestBuilder);

        ResultActions actions = this.mockMvc.perform(requestBuilder).andExpect(status().isOk());
        fx.accept(actions);

        String content = actions.andDo(print())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        return content;
    }

    protected <T> String doPutRequest(T payload, String route) throws Exception {
        return this.doPutRequest(payload, route, (actions) -> {
            try {
                actions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(PopupConstants.API_OK));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    protected <T> String doPutRequest(
            T payload,
            String route,
            Consumer<ResultActions> fx) throws Exception {
        return this.doPutRequest(payload, route, (builder) -> {
        }, fx);
    }

    protected <T> String doPutRequestB(
            T payload,
            String route,
            Consumer<MockHttpServletRequestBuilder> fn) throws Exception {
        return this.doPutRequest(payload, route, fn, (actions) -> {
            try {
                actions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(PopupConstants.API_OK));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    protected <T> String doPutRequest(
            T payload,
            String route,
            Consumer<MockHttpServletRequestBuilder> fn,
            Consumer<ResultActions> fx) throws Exception {

        String body = JSON.toJSONString(payload);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(route)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body);
        fn.accept(requestBuilder);

        ResultActions actions = this.mockMvc.perform(requestBuilder).andExpect(status().isOk());
        fx.accept(actions);

        String content = actions.andDo(print())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        return content;
    }

    protected <T> void doDeleteRequest(T payload, String route) throws Exception {
        this.doDeleteRequest(payload, route, (actions) -> {
            try {
                actions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(PopupConstants.API_OK));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    protected <T> void doDeleteRequest(
            T payload,
            String route,
            Consumer<ResultActions> fx) throws Exception {
        this.doDeleteRequest(payload, route, (builder) -> {
        }, fx);
    }

    protected <T> void doDeleteRequestB(
            T payload,
            String route,
            Consumer<MockHttpServletRequestBuilder> fn) throws Exception {
        this.doDeleteRequest(payload, route, fn, (actions) -> {
            try {
                actions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(PopupConstants.API_OK));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    protected <T> void doDeleteRequest(
            T payload,
            String route,
            Consumer<MockHttpServletRequestBuilder> fn,
            Consumer<ResultActions> fx) throws Exception {

        String body = JSON.toJSONString(payload);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(route)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body);
        fn.accept(requestBuilder);

        ResultActions actions = this.mockMvc.perform(requestBuilder).andExpect(status().isOk());
        fx.accept(actions);

        actions.andDo(print())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

    }

    protected void doDeleteRequest(String route) throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(route))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(PopupConstants.API_OK))
                .andDo(print())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

    protected String doGetResult(String route) throws Exception {
        return this.doGetResult(DefaultQuery.empty(), route, (b) -> {
        }, (actions) -> {
            try {
                actions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(PopupConstants.API_OK));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    protected String doGetResult(String route, Consumer<ResultActions> fx) throws Exception {
        return this.doGetResult(DefaultQuery.empty(), route, (b) -> {
        }, fx);
    }

    protected String doGetResult(String route, Consumer<MockHttpServletRequestBuilder> fn, Consumer<ResultActions> fx) throws Exception {
        return this.doGetResult(DefaultQuery.empty(), route, fn, fx);
    }

    protected String doGetResultB(String route, Consumer<MockHttpServletRequestBuilder> fn) throws Exception {
        return this.doGetResult(DefaultQuery.empty(), route, fn, (actions) -> {
            try {
                actions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(PopupConstants.API_OK));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    protected <Q> String doGetResult(
            Q query,
            String route,
            Consumer<MockHttpServletRequestBuilder> fn,
            Consumer<ResultActions> fx) throws Exception {
        MultiValueMap<String, String> params = this.getMultiValueMap(query);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(route)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        if (ObjectUtils.isNotNullOrEmpty(params)) {
            builder.queryParams(params);
        }

        fn.accept(builder);
        ResultActions actions = this.mockMvc
                .perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk());
        fx.accept(actions);

        String content = actions.andDo(print())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        return content;
    }

    protected <Q> void doGetResult(Q query, String route, Consumer<ResultActions> fx) throws Exception {
        MultiValueMap<String, String> params = this.getMultiValueMap(query);

        ResultActions actions = this.mockMvc
                .perform(MockMvcRequestBuilders.get(route).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).queryParams(params))
                .andExpect(MockMvcResultMatchers.status().isOk());

        fx.accept(actions);

        actions.andDo(print())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

    private <Q> MultiValueMap<String, String> getMultiValueMap(Q query) throws IllegalAccessException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        if (ObjectUtils.isNullOrEmpty(query) || query instanceof DefaultQuery) {
            return params;
        }

        Class<?> clazz = query.getClass();
        while (Object.class != clazz) {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (Modifier.isStatic(declaredField.getModifiers())) {
                    continue;
                }

                ReflectionUtils.makeAccessible(declaredField);
                Object v = declaredField.get(query);
                if (ObjectUtils.isNotNullOrEmpty(v)) {
                    params.put(declaredField.getName(), Lists.newArrayList(String.valueOf(v)));
                }
            }

            clazz = clazz.getSuperclass();
        }

        return params;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Configuration
    public static class Injector {

    }
}
