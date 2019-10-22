package com.monkey.home.portal.pagecontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 门户页面Controller
 *
 * @author yangfan
 * @createTime 2019-10-19 23:44
 */
@Controller
@RequestMapping("portal")
public class PortalController {

    @RequestMapping("home")
    public String home() {
        return "/portal/index_home";
    }
}
