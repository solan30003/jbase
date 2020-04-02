package com.solan.localfdfs.controller;

import com.solan.jbase.log.annotation.LogActive;
import com.solan.localfdfs.dto.LogEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author: hyl
 * @date: 2020/3/31 7:50
 */
@RestController
@RequestMapping("/home")
public class Home {
    @LogActive
    @GetMapping("/index")
    public String index() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
