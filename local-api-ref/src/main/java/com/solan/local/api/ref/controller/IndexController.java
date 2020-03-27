package com.solan.local.api.ref.controller;

import com.encdata.oss.starter.web.result.P;
import com.encdata.oss.starter.web.result.R;
import com.encdata.psdos.rplanmgt.api.feign.IPlanInstcServiceClient;
import com.encdata.psdos.rplanmgt.api.feign.IPlanTmplServiceClient;
import com.encdata.psdos.rplanmgt.api.request.FindPreplanInstcByBizRelIdsRequest;
import com.encdata.psdos.rplanmgt.api.response.dto.PlanTmplResponse;
import com.encdata.psdos.rplanmgt.api.response.dto.PreplanInstcResponse;
import com.solan.local.api.ref.dto.ResultBean;
import feign.FeignException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: hyl
 * @date: 2020/3/23 15:31
 */
@Slf4j
@Api(tags = "SVC")
@RestController
@RequestMapping("/biz")
public class IndexController {
    @Autowired
    private IPlanTmplServiceClient tmplServiceClient;
    @Autowired
    private IPlanInstcServiceClient instcServiceClient;

    @ApiOperation(value = "搜索推荐的预案模板")
    @GetMapping("/searchSuggest")
    public R<P<List<PlanTmplResponse>>> searchSuggest(@ApiParam(value = "搜索关键字", required = false) @RequestParam(value = "keyword", required = false) String keyword,
                                                       @ApiParam(value = "页码", required = true, defaultValue = "0") @RequestParam("pageIndex") int pageIndex,
                                                       @ApiParam(value = "页大小", required = true, defaultValue = "10") @RequestParam("pageSize") int pageSize) {
        try {
            R<P<List<PlanTmplResponse>>> res = tmplServiceClient.searchSuggest(keyword, pageIndex, pageSize);
            return res;
        } catch (FeignException f) {
            f.printStackTrace();
            return R.failure(f.getMessage());
        }
    }

    @ApiOperation(value = "根据多个业务关联id查询预案实例列表")
    @PostMapping("/findPreplanInstcByBizRefIds")
    public R<List<PreplanInstcResponse>> findPreplanInstcByBizRefIds(@ApiParam(value = "业务关联id", required = true) @RequestBody FindPreplanInstcByBizRelIdsRequest request) {
        return instcServiceClient.findPreplanInstcByBizRelIds(request);
    }
}
