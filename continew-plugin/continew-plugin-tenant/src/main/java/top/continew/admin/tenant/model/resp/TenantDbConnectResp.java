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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.continew.admin.common.model.resp.BaseResp;

import java.io.Serial;

/**
 * 租户数据连接信息
 *
 * @author 小熊
 * @since 2024/12/12 19:13
 */
@Data
@Schema(description = "租户数据连接信息")
public class TenantDbConnectResp extends BaseResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 连接名称
     */
    @Schema(description = "连接名称")
    private String connectName;

    /**
     * 连接类型
     */
    @Schema(description = "连接类型")
    private Integer type;

    /**
     * 连接主机地址
     */
    @Schema(description = "连接主机地址")
    private String host;

    /**
     * 连接端口
     */
    @Schema(description = "连接端口")
    private Integer port;

    /**
     * 连接用户名
     */
    @Schema(description = "连接用户名")
    private String username;

}