package com.solan.rules.service.impl;

import com.solan.rules.domain.Product;
import com.solan.rules.domain.Product2;
import com.solan.rules.service.RuleService;
import org.apache.commons.lang3.RandomUtils;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 * TODO:
 *
 * @author: hyl
 * @date: 2020/3/27 14:58
 */
@Service
public class RuleServiceImpl implements RuleService {
    @Override
    public String doRule(Product product) {
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        KieSession kSession = kContainer.newKieSession("ksession-rules");
        for (int i = 0; i < 5; i++) {
            Product2 p = new Product2();
            BigDecimal b = new BigDecimal(RandomUtils.nextDouble(0, 1999));
            p.setPrice(b.setScale(2, RoundingMode.HALF_UP).doubleValue());
            p.setName("default product " + i);
            kSession.insert(p);
        }
        if (product != null) {
            kSession.insert(product);
        }

        int k = kSession.fireAllRules();
        System.out.println("fire rules count: " + k);
        kSession.dispose();
        return product.getMessage();
    }
}
