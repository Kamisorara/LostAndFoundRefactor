package com.laf.framwork.service.impl;


import com.laf.framwork.service.ipService;
import com.laf.framwork.util.RequestUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Service
public class ipServiceImpl implements ipService {
    @Override
    public String getUserInquireIpAddress(ServletRequestAttributes servletRequestAttributes) {
        //查询ip地址
        HttpServletRequest request = servletRequestAttributes.getRequest();
        return RequestUtil.getRequestIp(request);
    }
}
