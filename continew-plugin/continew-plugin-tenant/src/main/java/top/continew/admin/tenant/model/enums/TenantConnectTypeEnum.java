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

package top.continew.admin.tenant.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.continew.starter.core.exception.BusinessException;

@Getter
@AllArgsConstructor
public enum TenantConnectTypeEnum {

    MYSQL;

    public static TenantConnectTypeEnum getByOrdinal(Integer ordinal) {
        for (TenantConnectTypeEnum item : TenantConnectTypeEnum.values()) {
            if (item.ordinal() == ordinal) {
                return item;
            }
        }
        throw new BusinessException("未知的连接类型");
    }

}
