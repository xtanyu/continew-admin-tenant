package top.continew.admin.tenant.service;

import top.continew.starter.extension.crud.service.BaseService;
import top.continew.admin.tenant.model.query.TenantDbConnectQuery;
import top.continew.admin.tenant.model.req.TenantDbConnectReq;
import top.continew.admin.tenant.model.resp.TenantDbConnectDetailResp;
import top.continew.admin.tenant.model.resp.TenantDbConnectResp;

/**
 * 租户数据连接业务接口
 *
 * @author 小熊
 * @since 2024/12/12 19:13
 */
public interface TenantDbConnectService extends BaseService<TenantDbConnectResp, TenantDbConnectDetailResp, TenantDbConnectQuery, TenantDbConnectReq> {}