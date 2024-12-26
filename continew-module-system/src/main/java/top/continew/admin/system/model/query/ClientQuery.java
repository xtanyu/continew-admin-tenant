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

package top.continew.admin.system.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.continew.admin.common.enums.DisEnableStatusEnum;
import top.continew.starter.data.core.annotation.Query;
import top.continew.starter.data.core.enums.QueryType;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 客户端管理查询条件
 *
 * @author MoChou
 * @since 2024/12/03 16:04
 */
@Data
@Schema(description = "客户端管理查询条件")
public class ClientQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 客户端Key
     */
    @Schema(description = "客户端Key")
    @Query(type = QueryType.EQ)
    private String clientKey;

    /**
     * 客户端秘钥
     */
    @Schema(description = "客户端秘钥")
    @Query(type = QueryType.EQ)
    private String clientSecret;

    /**
     * 登录类型
     */
    @Schema(description = "登录类型")
    @Query(type = QueryType.IN)
    private List<String> authType;
    /**
     * 客户端类型
     */
    @Schema(description = "客户端类型")
    @Query(type = QueryType.EQ)
    private String clientType;

    /**
     * 状态（1：启用；2：禁用）
     */
    @Schema(description = "状态")
    @Query(type = QueryType.EQ)
    private DisEnableStatusEnum status;
}