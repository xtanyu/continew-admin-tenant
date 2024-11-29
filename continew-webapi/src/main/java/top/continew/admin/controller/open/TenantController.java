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

package top.continew.admin.controller.open;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.collection.ListUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.continew.admin.system.model.entity.MenuDO;
import top.continew.admin.system.service.*;
import top.continew.admin.tenant.config.TenantConfig;
import top.continew.admin.tenant.model.query.TenantQuery;
import top.continew.admin.tenant.model.req.TenantReq;
import top.continew.admin.tenant.model.resp.TenantCommonResp;
import top.continew.admin.tenant.model.resp.TenantDetailResp;
import top.continew.admin.tenant.model.resp.TenantPackageDetailResp;
import top.continew.admin.tenant.model.resp.TenantResp;
import top.continew.admin.tenant.service.TenantPackageService;
import top.continew.admin.tenant.service.TenantService;
import top.continew.admin.tenant.util.TenantUtil;
import top.continew.starter.extension.crud.annotation.CrudRequestMapping;
import top.continew.starter.extension.crud.controller.BaseController;
import top.continew.starter.extension.crud.enums.Api;
import top.continew.starter.extension.crud.model.resp.BaseIdResp;
import top.continew.starter.extension.crud.model.resp.BaseResp;

import java.util.List;

/**
 * 租户管理 API
 *
 * @author 小熊
 * @since 2024/11/26 17:20
 */
@Tag(name = "租户管理 API")
@RestController
@AllArgsConstructor
@CrudRequestMapping(value = "/open/tenant", api = {Api.PAGE, Api.DETAIL, Api.ADD, Api.UPDATE, Api.DELETE})
public class TenantController extends BaseController<TenantService, TenantResp, TenantDetailResp, TenantQuery, TenantReq> {

    private final TenantConfig tenantConfig;
    private final DeptService deptService;
    private final MenuService menuService;
    private final TenantPackageService packageService;
    private final RoleService roleService;
    private final RoleDeptService roleDeptService;
    private final RoleMenuService roleMenuService;
    private final UserService userService;

    @GetMapping("/common")
    @SaIgnore
    @Operation(summary = "多租户通用信息查询", description = "多租户通用信息查询")
    public TenantCommonResp common() {
        TenantCommonResp commonResp = new TenantCommonResp();
        commonResp.setIsEnabled(tenantConfig.isEnabled());
        commonResp.setAvailableList(baseService.getAvailableList());
        return commonResp;
    }

    @Override
    @Transactional
    public BaseIdResp<Long> add(TenantReq req) {
        //租户添加
        BaseIdResp<Long> baseIdResp = super.add(req);
        TenantPackageDetailResp detailResp = packageService.get(req.getPackageId());
        //菜单
        List<MenuDO> menuRespList = menuService.listByIds(detailResp.getMenuIds());
        //在租户中执行数据插入
        TenantUtil.execute(baseIdResp.getId(), () -> {
            //租户部门初始化
            Long deptId = deptService.initTenantDept(req.getName());
            //租户菜单初始化
            menuInit(menuRespList, 0L, 0L);
            //租户角色初始化
            Long roleId = roleService.initTenantRole();
            //角色绑定部门
            roleDeptService.add(ListUtil.of(deptId), roleId);
            //角色绑定菜单
            roleMenuService.add(menuService.listAll(baseIdResp.getId()).stream().map(BaseResp::getId).toList(), roleId);
            //管理用户初始化
            Long userId = userService.initTenantUser(req.getUsername(), req.getPassword(), deptId);
            //用户绑定角色
            roleService.assignToUsers(roleId, ListUtil.of(userId));
            //租户绑定用户
            baseService.bindUser(baseIdResp.getId(), userId);
        });
        return baseIdResp;
    }

    /**
     * 递归初始化菜单
     */
    private void menuInit(List<MenuDO> menuList, Long oldParentId, Long newParentId) {
        List<MenuDO> children = menuList.stream().filter(menuDO -> menuDO.getParentId().equals(oldParentId)).toList();
        for (MenuDO menuDO : children) {
            Long oldId = menuDO.getId();
            menuDO.setId(null);
            menuDO.setParentId(newParentId);
            menuService.save(menuDO);
            menuInit(menuList, oldId, menuDO.getId());
        }
    }

}