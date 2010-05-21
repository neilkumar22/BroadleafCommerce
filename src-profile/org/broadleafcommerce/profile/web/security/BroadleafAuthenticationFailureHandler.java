/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.broadleafcommerce.profile.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.broadleafcommerce.util.StringUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class BroadleafAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String failureUrlParam = StringUtil.cleanseUrlString(request.getParameter("failureUrl"));
        String successUrlParam = StringUtil.cleanseUrlString(request.getParameter("successUrl"));
        String failureUrl = null;
        if (StringUtils.isNotEmpty(failureUrlParam)) {
            failureUrl = failureUrlParam;
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
        if (StringUtils.isNotEmpty(successUrlParam)) {
            if (!failureUrl.contains("?")) {
                failureUrl += "?successUrl=" + successUrlParam;
            } else {
                failureUrl += "&successUrl=" + successUrlParam;
            }
        }
        request.getRequestDispatcher(failureUrl).forward(request, response);
    }

}