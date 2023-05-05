package com.sparrow.chat.boot.config;

import com.sparrow.chat.commons.TokenParser;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.json.Json;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.utility.StringUtility;
import jdk.nashorn.internal.parser.Token;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Named;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Named
public class LoginUserFilter implements Filter {
    @Value("${mock_login_user}")
    private Boolean mockLoginUser;
    private Json json = JsonFactory.getProvider();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            String loginTokenOfHeader = req.getHeader(Constant.REQUEST_HEADER_KEY_LOGIN_TOKEN);
            LoginUser loginUser = null;
            if (!StringUtility.isNullOrEmpty(loginTokenOfHeader)) {
                try {
                    loginUser = TokenParser.parseUserId(loginTokenOfHeader);
                } catch (BusinessException e) {
                    throw new ServletException(e);
                }
            } else {
                if (mockLoginUser) {
                    loginUser = LoginUser.create(
                            1L,
                            "mock-user",
                            "mock-nick-name",
                            "header",
                            "device id", 3);
                }
            }
            ThreadContext.bindLoginToken(loginUser);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
