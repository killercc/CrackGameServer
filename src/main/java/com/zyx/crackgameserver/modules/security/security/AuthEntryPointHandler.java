package com.zyx.crackgameserver.modules.security.security;


import com.zyx.crackgameserver.response.Result;
import com.zyx.crackgameserver.response.ResultCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("authEntryPoint")
public class AuthEntryPointHandler extends ResponseWriteJson  implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        Result result = Result.error(ResultCode.USER_NOT_LOGIN);
        this.WriteJSON(httpServletRequest,httpServletResponse,result);
    }
}
