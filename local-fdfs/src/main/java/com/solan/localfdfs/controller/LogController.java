package com.solan.localfdfs.controller;

import com.solan.localfdfs.dto.LogEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author: hyl
 * @date: 2020/3/30 17:25
 */
@RestController
@RequestMapping("/logs")
public class LogController {
    @PostMapping("/log")
    public String addLog(@RequestBody LogEntity entity) {
        System.out.println(entity.toString());
        return "true";
    }
}
