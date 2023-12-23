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

package top.charles7c.continew.admin.system.model.resp;

import java.io.Serial;
import java.time.LocalDateTime;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import top.charles7c.continew.admin.system.enums.FileTypeEnum;
import top.charles7c.continew.starter.extension.crud.base.BaseResp;

/**
 * 文件信息
 *
 * @author Charles7c
 * @since 2023/12/23 10:38
 */
@Data
@Schema(description = "文件信息")
public class FileResp extends BaseResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    @Schema(description = "名称")
    private String name;

    /**
     * 大小（字节）
     */
    @Schema(description = "大小（字节）")
    private Long size;

    /**
     * URL
     */
    @Schema(description = "URL")
    private String url;

    /**
     * 扩展名
     */
    @Schema(description = "扩展名")
    private String extension;

    /**
     * MIME类型
     */
    @Schema(description = "MIME类型")
    private String mimeType;

    /**
     * 类型
     */
    @Schema(description = "类型")
    private FileTypeEnum type;

    /**
     * 存储库ID
     */
    @Schema(description = "存储库ID")
    private Long storageId;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;
}