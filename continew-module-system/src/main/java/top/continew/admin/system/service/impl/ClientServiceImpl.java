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

package top.continew.admin.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.DigestUtil;
import org.springframework.stereotype.Service;
import top.continew.admin.auth.enums.AuthTypeEnum;
import top.continew.admin.system.mapper.ClientMapper;
import top.continew.admin.system.model.entity.ClientDO;
import top.continew.admin.system.model.query.ClientQuery;
import top.continew.admin.system.model.req.ClientReq;
import top.continew.admin.system.model.resp.ClientResp;
import top.continew.admin.system.service.ClientService;
import top.continew.starter.core.constant.StringConstants;
import top.continew.starter.core.validation.ValidationUtils;
import top.continew.starter.extension.crud.service.BaseServiceImpl;

import java.util.List;

/**
 * 客户端业务实现
 *
 * @author KAI
 * @author Charles7c
 * @since 2024/12/03 16:04
 */
@Service
public class ClientServiceImpl extends BaseServiceImpl<ClientMapper, ClientDO, ClientResp, ClientResp, ClientQuery, ClientReq> implements ClientService {

    @Override
    public void beforeAdd(ClientReq req) {
        String clientId = DigestUtil.md5Hex(req.getClientKey() + StringConstants.COLON + req.getClientSecret());
        req.setClientId(clientId);
    }

    @Override
    public void beforeDelete(List<Long> ids) {
        // 查询如果删除客户端记录以后是否还存在账号认证的方式，不存在则不允许删除
        List<ClientDO> clientList = baseMapper.lambdaQuery()
            .in(ClientDO::getId, ids)
            .like(ClientDO::getAuthType, AuthTypeEnum.ACCOUNT.getValue())
            .list();
        ValidationUtils.throwIfEmpty(clientList, "请至少保留 [{}] 认证类型", AuthTypeEnum.ACCOUNT.getDescription());
        super.beforeDelete(ids);
    }

    @Override
    public ClientResp getByClientId(String clientId) {
        return baseMapper.lambdaQuery()
            .eq(ClientDO::getClientId, clientId)
            .oneOpt()
            .map(client -> BeanUtil.copyProperties(client, ClientResp.class))
            .orElse(null);
    }
}