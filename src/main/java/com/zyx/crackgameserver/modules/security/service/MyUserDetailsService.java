package com.zyx.crackgameserver.modules.security.service;


import cn.hutool.core.collection.ListUtil;
import com.zyx.crackgameserver.modules.security.mapper.UserDetailMapper;
import com.zyx.crackgameserver.modules.security.model.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDetailMapper userDetailMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        if(StringUtils.isNotBlank(s)){
            SysUser user= userDetailMapper.finUserByName(s);

            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            //获取用户权限 ROLE_
            List<String> codelist = userDetailMapper.getRoleCodeByname(s);
            //System.out.println(userlist.get(0).getUsername());
            //System.out.println(userlist.get(0).getPassword());
            //System.out.println(codelist);
            codelist.forEach(code->{
                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(code);
                grantedAuthorities.add(simpleGrantedAuthority);
            });

            return new User(
                    user.getUsername(),
                    user.getPassword(),
                    grantedAuthorities);




        }

        return null;
    }
}
