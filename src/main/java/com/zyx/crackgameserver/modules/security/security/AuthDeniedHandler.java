package com.zyx.crackgameserver.modules.security.security;

import com.zyx.crackgameserver.response.Result;
import com.zyx.crackgameserver.response.ResultCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("authDeniedHandler")
public class AuthDeniedHandler extends ResponseWriteJson implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        Result result = Result.error(ResultCode.NO_PERMISSION);
        this.WriteJSON(httpServletRequest,httpServletResponse,result);
    }
}
