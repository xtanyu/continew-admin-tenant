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

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;
import top.continew.admin.common.base.BaseController;
import top.continew.admin.tenant.model.query.TenantDbConnectQuery;
import top.continew.admin.tenant.model.req.TenantDbConnectReq;
import top.continew.admin.tenant.model.resp.TenantDbConnectDetailResp;
import top.continew.admin.tenant.model.resp.TenantDbConnectResp;
import top.continew.admin.tenant.service.TenantDbConnectService;
import top.continew.starter.extension.crud.annotation.CrudRequestMapping;
import top.continew.starter.extension.crud.enums.Api;

/**
 * 租户数据连接管理 API
 *
 * @author 小熊
 * @since 2024/12/12 19:13
 */
@Tag(name = "租户数据连接管理 API")
@RestController
@CrudRequestMapping(value = "/tenant/dbConnect", api = {Api.PAGE, Api.DETAIL, Api.ADD, Api.UPDATE, Api.DELETE})
public class TenantDbConnectController extends BaseController<TenantDbConnectService, TenantDbConnectResp, TenantDbConnectDetailResp, TenantDbConnectQuery, TenantDbConnectReq> {}