/*
 * Copyright (c) 2022-present Charles7c Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.charles7c.cnadmin.webapi.controller.system;

import static top.charles7c.cnadmin.common.annotation.CrudRequestMapping.Api;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import top.charles7c.cnadmin.common.annotation.CrudRequestMapping;
import top.charles7c.cnadmin.common.base.BaseController;
import top.charles7c.cnadmin.common.model.query.SortQuery;
import top.charles7c.cnadmin.common.model.vo.R;
import top.charles7c.cnadmin.system.model.query.MenuQuery;
import top.charles7c.cnadmin.system.model.request.MenuRequest;
import top.charles7c.cnadmin.system.model.vo.MenuVO;
import top.charles7c.cnadmin.system.service.MenuService;

/**
 * 菜单管理 API
 *
 * @author Charles7c
 * @since 2023/2/15 20:35
 */
@Tag(name = "菜单管理 API")
@RestController
@CrudRequestMapping(value = "/system/menu", api = {Api.LIST, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE, Api.EXPORT})
public class MenuController extends BaseController<MenuService, MenuVO, MenuVO, MenuQuery, MenuRequest> {

    @Override
    @Operation(summary = "查询列表树")
    public R<List<MenuVO>> list(@Validated MenuQuery query, @Validated SortQuery sortQuery) {
        List<MenuVO> list = baseService.list(query, sortQuery);
        return R.ok(baseService.buildListTree(list));
    }
}
