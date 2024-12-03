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

package top.continew.admin.controller.tenant;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.lang.tree.Tree;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.continew.admin.common.enums.DisEnableStatusEnum;
import top.continew.admin.system.model.query.MenuQuery;
import top.continew.admin.system.service.MenuService;
import top.continew.admin.tenant.config.TenantConfig;
import top.continew.admin.tenant.model.query.TenantPackageQuery;
import top.continew.admin.tenant.model.req.TenantPackageReq;
import top.continew.admin.tenant.model.resp.TenantPackageDetailResp;
import top.continew.admin.tenant.model.resp.TenantPackageResp;
import top.continew.admin.tenant.service.TenantPackageService;
import top.continew.starter.extension.crud.annotation.CrudRequestMapping;
import top.continew.starter.extension.crud.controller.BaseController;
import top.continew.starter.extension.crud.enums.Api;

import java.util.List;

/**
 * 租户套餐管理 API
 *
 * @author 小熊
 * @since 2024/11/26 11:25
 */
@Tag(name = "租户套餐管理 API")
@RestController
@AllArgsConstructor
@CrudRequestMapping(value = "/tenant/package", api = {Api.LIST, Api.PAGE, Api.DETAIL, Api.ADD, Api.UPDATE,
    Api.DELETE})
public class TenantPackageController extends BaseController<TenantPackageService, TenantPackageResp, TenantPackageDetailResp, TenantPackageQuery, TenantPackageReq> {

    private final MenuService menuService;
    private final TenantConfig tenantConfig;

    @GetMapping("/menuTree")
    @SaCheckPermission("tenant:package:detail")
    @Operation(summary = "获取租户套餐菜单", description = "获取租户套餐菜单")
    public List<Tree<Long>> menuTree() {
        MenuQuery query = new MenuQuery();
        //必须是启用状态的菜单
        query.setStatus(DisEnableStatusEnum.ENABLE);
        //过滤掉租户不能使用的菜单
        query.setExcludeMenuIdList(tenantConfig.getIgnoreMenus());
        return menuService.tree(query, null, true);
    }

}