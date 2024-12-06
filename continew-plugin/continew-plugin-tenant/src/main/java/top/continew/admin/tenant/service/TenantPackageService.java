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

package top.continew.admin.tenant.service;

import top.continew.starter.extension.crud.service.BaseService;
import top.continew.admin.tenant.model.query.TenantPackageQuery;
import top.continew.admin.tenant.model.req.TenantPackageReq;
import top.continew.admin.tenant.model.resp.TenantPackageDetailResp;
import top.continew.admin.tenant.model.resp.TenantPackageResp;

/**
 * 租户套餐业务接口
 *
 * @author 小熊
 * @since 2024/11/26 11:25
 */
public interface TenantPackageService extends BaseService<TenantPackageResp, TenantPackageDetailResp, TenantPackageQuery, TenantPackageReq> {}