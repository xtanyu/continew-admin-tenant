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

package top.continew.admin.tenant.util;

import top.continew.starter.extension.tenant.context.TenantContext;
import top.continew.starter.extension.tenant.context.TenantContextHolder;

/**
 * @description: 租户工具
 * @author: 小熊
 * @create: 2024-11-28 14:56
 */
public class TenantUtil {

    /**
     * 在指定租户中执行方法
     */
    public static void execute(Long tenantId, Runnable execute) {
        // 保存当前的租户上下文
        TenantContext originalContext = TenantContextHolder.getContext();
        try {
            // 设置新的租户上下文
            TenantContext context = new TenantContext();
            context.setTenantId(tenantId);
            TenantContextHolder.setContext(context);
            execute.run();
        } finally {
            // 恢复原始的租户上下文
            if (originalContext != null) {
                TenantContextHolder.setContext(originalContext);
            } else {
                TenantContextHolder.clearContext();
            }
        }
    }

}
