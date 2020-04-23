package com.zxg.pay.web.controller.lakala.paymax.other;

import java.io.InputStream;

public interface Request<T> {
    String getMethod();

    String getRequestUriPath();

    String getRequestQueryString();

    InputStream getRequestBody();

    String getHeaderValue(String name);
}
