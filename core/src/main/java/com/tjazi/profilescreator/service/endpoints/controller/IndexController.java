package com.tjazi.profilescreator.service.endpoints.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Krzysztof Wasiak on 22/10/2015.
 */

@Controller
@RequestMapping(value = "/")
public class IndexController {

    private static final Logger log = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String displayStatusPage() {

        log.debug("Requesting status page...");

        return "status";
    }
}
