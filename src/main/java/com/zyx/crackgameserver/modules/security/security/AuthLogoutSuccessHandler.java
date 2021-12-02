package com.zyx.crackgameserver.modules.security.security;

import com.zyx.crackgameserver.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("authLogoutSuccessHandler")
public class AuthLogoutSuccessHandler extends ResponseWriteJson implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        Result result = Result.ok().message("注销成功");

        this.WriteJSON(httpServletRequest,httpServletResponse,result);
    }
}
