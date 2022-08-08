/**
 * Copyright 2020 OPSLI 快速开发平台 https://www.opsli.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.opsli.modulars.tools.searchhis.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.base.result.ResultWrapper;
import org.opsli.common.annotation.ApiRestController;
import org.opsli.common.annotation.Limiter;
import org.opsli.common.annotation.SearchHis;
import org.opsli.core.utils.SearchHisUtil;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * 搜索历史记录
 *
 * @author parker
 * @date 2020-05-23 13:30
 */
@Api(tags = "搜索历史记录")
@Slf4j
@ApiRestController("/{ver}/tools/searchhis")
public class SearchHisRestController {

    /**
     * 获得搜索历史记录
     */
    @Limiter
    @ApiOperation(value = "获得搜索历史记录", notes = "获得搜索历史记录")
    @PostMapping("/getSearchHis")
    public ResultWrapper<?> getSearchHis(String key, Integer count, HttpServletRequest request){

        Set<Object> searchHis = SearchHisUtil.getSearchHis(request, key, count);

        return ResultWrapper.getSuccessResultWrapper(searchHis);
    }

    /**
     * 测试存入搜索历史记录
     */
    @Limiter
    @SearchHis(keys = {"test"})
    @ApiOperation(value = "测试存入搜索历史记录", notes = "测试存入搜索历史记录")
    @PostMapping("/testPutSearchHis")
    public void testPutSearchHis(String test, HttpServletRequest request){
    }

}
