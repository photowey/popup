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
package com.photowey.component.test.delayed.queue.app;

import com.alibaba.fastjson2.JSON;
import com.photowey.component.common.constant.PopupConstants;
import com.photowey.component.common.util.ObjectUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
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
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
    }

    private void mockUser() {

    }

    // -----------------------------------------------------------------------------------------------------------------

    protected void doHealthz() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(HEALTH_API))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    }

    // -----------------------------------------------------------------------------------------------------------------

    protected <T> void doPostRequest(T payload, String route) throws Exception {
        this.doPostRequest(payload, route, (actions) -> {
            try {
                actions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(PopupConstants.API_OK));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    protected <T> void doPostRequest(T payload, String route, Consumer<ResultActions> fx) throws Exception {
        String body = JSON.toJSONString(payload);

        ResultActions actions = this.mockMvc.perform(MockMvcRequestBuilders.post(route).contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
                .andExpect(status().isOk());

        fx.accept(actions);

        actions.andDo(print())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

    protected <T> void doPutRequest(T payload, String route) throws Exception {
        String body = JSON.toJSONString(payload);

        this.mockMvc.perform(MockMvcRequestBuilders.put(route).contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(PopupConstants.API_OK))
                .andDo(print())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

    protected <T> void doDeleteRequest(T payload, String route) throws Exception {
        String body = JSON.toJSONString(payload);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(route).contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(PopupConstants.API_OK))
                .andDo(print())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

    protected void doDeleteRequest(String route) throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.delete(route))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(PopupConstants.API_OK))
                .andDo(print())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

    protected void doGetResult(String route, Consumer<ResultActions> fx) throws Exception {

        ResultActions actions = this.mockMvc.perform(MockMvcRequestBuilders.get(route))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(PopupConstants.API_OK));
        fx.accept(actions);

        actions.andDo(print())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

    protected <Q> void doGetResult(Q query, String route, Consumer<ResultActions> fx) throws Exception {
        MultiValueMap<String, String> params = this.getMultiValueMap(query);

        ResultActions actions = this.mockMvc
                .perform(MockMvcRequestBuilders.get(route).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).queryParams(params))
                .andExpect(MockMvcResultMatchers.status().isOk());

        fx.accept(actions);

        actions.andDo(print())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

    private <Q> MultiValueMap<String, String> getMultiValueMap(Q query) throws IllegalAccessException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

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
