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

package top.continew.admin.tenant.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.continew.starter.data.core.annotation.Query;
import top.continew.starter.data.core.enums.QueryType;

import java.io.Serial;
import java.io.Serializable;

/**
 * 租户查询条件
 *
 * @author 小熊
 * @since 2024/11/26 17:20
 */
@Data
@Schema(description = "租户查询条件")
public class TenantQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 租户名称
     */
    @Schema(description = "租户名称")
    @Query(type = QueryType.LIKE)
    private String name;

    /**
     * 租户套餐编号
     */
    @Schema(description = "租户套餐编号")
    @Query(type = QueryType.EQ)
    private Long packageId;

}