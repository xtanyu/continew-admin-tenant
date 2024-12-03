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
import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.continew.admin.common.util.SecureUtils;
import top.continew.admin.open.service.AppService;
import top.continew.admin.system.model.entity.MenuDO;
import top.continew.admin.system.model.entity.UserDO;
import top.continew.admin.system.service.*;
import top.continew.admin.tenant.config.TenantConfig;
import top.continew.admin.tenant.model.query.TenantQuery;
import top.continew.admin.tenant.model.req.TenantLoginUserInfoReq;
import top.continew.admin.tenant.model.req.TenantReq;
import top.continew.admin.tenant.model.resp.TenantCommonResp;
import top.continew.admin.tenant.model.resp.TenantDetailResp;
import top.continew.admin.tenant.model.resp.TenantPackageDetailResp;
import top.continew.admin.tenant.model.resp.TenantResp;
import top.continew.admin.tenant.service.TenantPackageService;
import top.continew.admin.tenant.service.TenantService;
import top.continew.admin.tenant.util.TenantUtil;
import top.continew.starter.core.util.ExceptionUtils;
import top.continew.starter.core.validation.CheckUtils;
import top.continew.starter.core.validation.ValidationUtils;
import top.continew.starter.extension.crud.annotation.CrudRequestMapping;
import top.continew.starter.extension.crud.controller.BaseController;
import top.continew.starter.extension.crud.enums.Api;
import top.continew.starter.extension.crud.model.entity.BaseIdDO;
import top.continew.starter.extension.crud.model.resp.BaseIdResp;

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
@CrudRequestMapping(value = "/tenant/user", api = {Api.PAGE, Api.DETAIL, Api.ADD, Api.UPDATE, Api.DELETE})
public class TenantController extends BaseController<TenantService, TenantResp, TenantDetailResp, TenantQuery, TenantReq> {

    private final TenantConfig tenantConfig;
    private final DeptService deptService;
    private final MenuService menuService;
    private final TenantPackageService packageService;
    private final RoleService roleService;
    private final UserService userService;
    private final TenantSysDataService tenantSysDataService;
    private final AppService appService;

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
            menuService.menuInit(menuRespList, 0L, 0L);
            //租户角色初始化
            Long roleId = roleService.initTenantRole();
            //管理用户初始化
            Long userId = userService.initTenantUser(req.getUsername(), req.getPassword(), deptId);
            //用户绑定角色
            roleService.assignToUsers(roleId, ListUtil.of(userId));
            //租户绑定用户
            baseService.bindUser(baseIdResp.getId(), userId);
        });
        return baseIdResp;
    }

    @Override
    @Transactional
    public void delete(List<Long> ids) {
        for (Long id : ids) {
            //在租户中执行数据清除
            TenantUtil.execute(id, () -> {
                //应用数据清除
                appService.clear();
                //系统数据清楚
                tenantSysDataService.clear();
            });
        }
        super.delete(ids);
    }

    /**
     * 租户管理账号信息更新
     */
    @PutMapping("/loginUser")
    @Operation(summary = "租户管理账号信息更新", description = "租户管理账号信息更新")
    @SaCheckPermission("tenant:user:editLoginUserInfo")
    public void editLoginUserInfo(@Validated @RequestBody TenantLoginUserInfoReq req) {
        TenantDetailResp detailResp = baseService.get(req.getTenantId());
        CheckUtils.throwIfNull(detailResp, "租户不存在");
        TenantUtil.execute(detailResp.getId(), () -> {
            UserDO userDO = userService.getById(detailResp.getUserId());
            CheckUtils.throwIfNull(userDO, "用户不存在");
            //修改用户名
            if (!req.getUsername().equals(userDO.getUsername())) {
                userService.update(Wrappers.lambdaUpdate(UserDO.class)
                    .set(UserDO::getUsername, req.getUsername())
                    .eq(BaseIdDO::getId, userDO.getId()));
            }
            //修改密码
            if (StrUtil.isNotEmpty(req.getPassword())) {
                String password = ExceptionUtils.exToNull(() -> SecureUtils.decryptByRsaPrivateKey(req.getPassword()));
                ValidationUtils.throwIfNull(password, "密码解密失败");
                userService.updatePassword(userDO.getPassword(), password, userDO.getId());
            }
        });
    }

}