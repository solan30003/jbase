package com.solan.rules.controller;

import com.solan.rules.domain.Product;
import com.solan.rules.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: hyl
 * @date: 2020/3/27 15:28
 */
@RestController
@RequestMapping("/rule")
public class RuleController {
    @Autowired
    private RuleService ruleService;

    @PostMapping("/checkProduct")
    public String checkProduct(@RequestBody Product product) {
        return ruleService.doRule(product);
    }
}
