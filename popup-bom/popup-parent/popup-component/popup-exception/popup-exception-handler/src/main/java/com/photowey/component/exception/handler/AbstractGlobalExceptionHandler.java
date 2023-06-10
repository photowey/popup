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
package com.photowey.component.exception.handler;

import com.photowey.component.exception.core.enums.ResponseStatusEnum;
import com.photowey.component.exception.core.model.PopupException;
import com.photowey.component.exception.core.wrapper.ResponseWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code AbstractGlobalExceptionHandler}
 *
 * @author photowey
 * @date 2023/06/10
 * @since 1.0.0
 */
@Slf4j
public abstract class AbstractGlobalExceptionHandler extends AbstractApplyContentType {

    @ResponseBody
    @ExceptionHandler(value = SocketTimeoutException.class)
    public ResponseEntity<ResponseWrapper> handleSocketTimeoutException(
            HttpServletRequest request, HttpServletResponse response, SocketTimeoutException exception) {
        this.populateContentType(response);

        ResponseWrapper wrapper = new ResponseWrapper(ResponseStatusEnum.TIME_OUT);

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @ResponseBody
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseWrapper> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.populateContentType(response);

        ResponseWrapper wrapper = new ResponseWrapper(ResponseStatusEnum.BAD_REQUEST, "Invalid character.");

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    // ---------------------------------------------------------------------------------------------------- @Validate

    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseWrapper> handleMissingServletRequestParameterException(
            HttpServletRequest request, HttpServletResponse response, MissingServletRequestParameterException exception) throws Exception {
        this.populateContentType(response);
        String method = request.getMethod().toUpperCase();
        String path = HELPER.getLookupPathForRequest(request);

        ResponseWrapper wrapper = ResponseWrapper.badRequest();

        String[] profileActivated = this.environment.getActiveProfiles();
        List<String> messages = new ArrayList<>();
        this.handleParameterException(exception, messages);
        this.populateStackMessage(messages, wrapper);

        log.error("handle profile.{} [{} {}] catch MissingServletRequestParameter exception",
                this.arrayToString(profileActivated), method, path, exception);

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseWrapper> handleMethodArgumentNotValidException(
            HttpServletRequest request, HttpServletResponse response, MethodArgumentNotValidException exception) throws Exception {
        this.populateContentType(response);
        String method = request.getMethod().toUpperCase();
        String path = HELPER.getLookupPathForRequest(request);

        ResponseWrapper wrapper = ResponseWrapper.badRequest();
        String[] profileActivated = this.environment.getActiveProfiles();
        List<String> messages = new ArrayList<>();
        this.handleValidException(exception, messages);
        this.populateStackMessage(messages, wrapper);

        log.error("handle profile.{} [{} {}] catch MethodArgumentNotValid exception",
                this.arrayToString(profileActivated), method, path, exception);

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @ResponseBody
    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<ResponseWrapper> handleValidationException(
            HttpServletRequest request, HttpServletResponse response, ValidationException exception) throws Exception {
        this.populateContentType(response);
        String method = request.getMethod().toUpperCase();
        String path = HELPER.getLookupPathForRequest(request);

        ResponseWrapper wrapper = ResponseWrapper.badRequest();
        String[] profileActivated = this.environment.getActiveProfiles();
        List<String> messages = new ArrayList<>();
        this.handleValidationException(exception, messages);
        this.populateStackMessage(messages, wrapper);

        log.error("handle profile.{} [{} {}] catch Validation exception",
                this.arrayToString(profileActivated), method, path, exception);

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<ResponseWrapper> handleBindException(
            HttpServletRequest request, HttpServletResponse response, BindException exception) throws Exception {
        this.populateContentType(response);
        String method = request.getMethod().toUpperCase();
        String path = HELPER.getLookupPathForRequest(request);

        ResponseWrapper wrapper = ResponseWrapper.badRequest();
        String[] profileActivated = this.environment.getActiveProfiles();
        List<String> messages = new ArrayList<>();
        this.handleBindException(exception, messages);
        this.populateStackMessage(messages, wrapper);

        log.error("handle profile.{} [{} {}] catch Validation exception",
                this.arrayToString(profileActivated), method, path, exception);

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @ResponseBody
    @ExceptionHandler(value = PopupException.class)
    public ResponseEntity<ResponseWrapper> handlePopupException(
            HttpServletRequest request, HttpServletResponse response, PopupException exception) throws Exception {
        this.populateContentType(response);
        String method = request.getMethod().toUpperCase();
        String path = HELPER.getLookupPathForRequest(request);

        ResponseWrapper wrapper = exception.toResponseWrapper();
        String[] profileActivated = this.environment.getActiveProfiles();
        log.error("handle profile.{} [{} {}] catch platform exception",
                this.arrayToString(profileActivated), method, path, exception);

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ResponseWrapper> handleAnonymousException(
            HttpServletRequest request, HttpServletResponse response, Exception exception) throws Exception {
        this.populateContentType(response);
        String method = request.getMethod().toUpperCase();
        String path = HELPER.getLookupPathForRequest(request);

        ResponseWrapper wrapper = ResponseWrapper.badUnHandle();
        String[] profileActivated = this.environment.getActiveProfiles();
        log.error("handle profile.{} [{} {}] catch Unknown exception",
                this.arrayToString(profileActivated), method, path, exception);

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    protected void populateContentType(HttpServletResponse response) {
        this.applyJSONContentType(response);
    }

    protected void handleDefaultException(List<String> messages) {
        messages.add("System error~");
    }

    protected void handleNotValidException(MethodArgumentNotValidException exception, List<String> messages) {
        BindingResult bindingResult = exception.getBindingResult();
        for (FieldError error : bindingResult.getFieldErrors()) {
            messages.add(error.getDefaultMessage());
        }
    }

    protected void handleParameterException(MissingServletRequestParameterException exception, List<String> messages) {
        messages.add("Parameter " + exception.getParameterName() + " not exist");
    }

    private void handleBindException(BindException exception, List<String> messages) {
        BindingResult bindingResult = exception.getBindingResult();
        for (FieldError error : bindingResult.getFieldErrors()) {
            messages.add(error.getDefaultMessage());
        }
    }

    private void populateStackMessage(List<String> messages, ResponseWrapper wrapper) {
        if (null != messages && messages.size() > 0) {
            wrapper.setMessage(String.join("#", messages));
        }
    }

    private void handleValidException(MethodArgumentNotValidException exception, List<String> messages) {
        BindingResult bindingResult = exception.getBindingResult();
        for (FieldError error : bindingResult.getFieldErrors()) {
            messages.add(error.getDefaultMessage());
        }
    }

    private void handleValidationException(ValidationException exception, List<String> messages) {
        String message = null != exception.getCause() ? exception.getCause().getMessage() : "";
        if (StringUtils.hasLength(message)) {
            messages.add(message);
        }
    }
}