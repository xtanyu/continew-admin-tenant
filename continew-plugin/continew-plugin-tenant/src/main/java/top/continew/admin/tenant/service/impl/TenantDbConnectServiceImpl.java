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

package top.continew.admin.tenant.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import top.continew.admin.common.constant.CacheConstants;
import top.continew.admin.tenant.mapper.TenantDbConnectMapper;
import top.continew.admin.tenant.model.entity.TenantDbConnectDO;
import top.continew.admin.tenant.model.enums.TenantConnectTypeEnum;
import top.continew.admin.tenant.model.query.TenantDbConnectQuery;
import top.continew.admin.tenant.model.req.TenantDbConnectReq;
import top.continew.admin.tenant.model.resp.TenantDbConnectDetailResp;
import top.continew.admin.tenant.model.resp.TenantDbConnectResp;
import top.continew.admin.tenant.service.TenantDbConnectService;
import top.continew.starter.cache.redisson.util.RedisUtils;
import top.continew.starter.core.exception.BusinessException;
import top.continew.starter.core.validation.CheckUtils;
import top.continew.starter.extension.crud.service.BaseServiceImpl;

import javax.sql.DataSource;
import java.util.List;

/**
 * 租户数据连接业务实现
 *
 * @author 小熊
 * @since 2024/12/12 19:13
 */
@Service
@RequiredArgsConstructor
public class TenantDbConnectServiceImpl extends BaseServiceImpl<TenantDbConnectMapper, TenantDbConnectDO, TenantDbConnectResp, TenantDbConnectDetailResp, TenantDbConnectQuery, TenantDbConnectReq> implements TenantDbConnectService {

    @Override
    @Cached(name = CacheConstants.DB_CONNECT_KEY_PREFIX, key = "#id")
    public TenantDbConnectDetailResp get(Long id) {
        return super.get(id);
    }

    @Override
    protected void beforeAdd(TenantDbConnectReq req) {
        TenantConnectTypeEnum connectTypeEnum = TenantConnectTypeEnum.getByOrdinal(req.getType());
        if (TenantConnectTypeEnum.MYSQL.equals(connectTypeEnum)) {
            getMysqlConnect(req);
            checkRepeat(req, null);
        }
    }

    /**
     * 验证mysql连接有消息并返回数据源
     */
    private DataSource getMysqlConnect(TenantDbConnectReq req) {
        try {
            String activeProfile = SpringUtil.getActiveProfile();
            String jdbcUrl = StrUtil.format("jdbc:mysql://{}:{}", req.getHost(), req.getPort());
            String driverClassName = "com.mysql.cj.jdbc.Driver";
            if (activeProfile.equals("dev")) {
                jdbcUrl = StrUtil.format("jdbc:p6spy:mysql://{}:{}", req.getHost(), req.getPort());
                driverClassName = "com.p6spy.engine.spy.P6SpyDriver";
            }
            HikariConfig configuration = new HikariConfig();
            configuration.setJdbcUrl(jdbcUrl);
            configuration.setDriverClassName(driverClassName);
            configuration.setUsername(req.getUsername());
            configuration.setPassword(req.getPassword());
            DataSource dataSource = new HikariDataSource(configuration);
            dataSource.getConnection();
            return dataSource;
        } catch (Exception e) {
            throw new BusinessException("数据库连接失败,请检查基础配置信息");
        }
    }

    /**
     * 验证重复数据
     */
    private void checkRepeat(TenantDbConnectReq req, Long id) {
        CheckUtils.throwIf(baseMapper.exists(Wrappers.lambdaQuery(TenantDbConnectDO.class)
            .eq(TenantDbConnectDO::getHost, req.getHost())
            .eq(TenantDbConnectDO::getPort, req.getPort())
            .eq(TenantDbConnectDO::getUsername, req.getUsername())
            .ne(id != null, TenantDbConnectDO::getId, id)), "数据库连接已存在");
    }

    @Override
    protected void beforeUpdate(TenantDbConnectReq req, Long id) {
        if (req.getType().equals(TenantConnectTypeEnum.MYSQL)) {
            getMysqlConnect(req);
            checkRepeat(req, id);
        }
    }

    @Override
    protected void afterUpdate(TenantDbConnectReq req, TenantDbConnectDO entity) {
        RedisUtils.delete(CacheConstants.DB_CONNECT_KEY_PREFIX + entity.getId());
    }

    @Override
    protected void afterDelete(List<Long> ids) {
        ids.forEach(id -> RedisUtils.delete(CacheConstants.DB_CONNECT_KEY_PREFIX + id));
    }

    @Override
    public JdbcTemplate getConnectJdbcTemplateById(Long id) {
        TenantDbConnectDetailResp tenantDbConnectDetailResp = get(id);
        TenantConnectTypeEnum connectTypeEnum = TenantConnectTypeEnum.getByOrdinal(tenantDbConnectDetailResp.getType());
        if (TenantConnectTypeEnum.MYSQL.equals(connectTypeEnum)) {
            TenantDbConnectReq dbConnectReq = BeanUtil
                .copyProperties(tenantDbConnectDetailResp, TenantDbConnectReq.class);
            return new JdbcTemplate(getMysqlConnect(dbConnectReq));
        }
        return null;
    }

}