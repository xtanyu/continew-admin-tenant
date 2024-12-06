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

package top.continew.admin.tenant.model.resp;

import lombok.Data;

import java.util.List;

/**
 * @description: 租户通用信息返回
 * @author: 小熊
 * @create: 2024-11-28 09:53
 */
@Data
public class TenantCommonResp {

    /**
     * 是否开启了多租户
     */
    private Boolean isEnabled;

    /**
     * 可用租户列表
     */
    private List<TenantAvailableResp> availableList;

}
