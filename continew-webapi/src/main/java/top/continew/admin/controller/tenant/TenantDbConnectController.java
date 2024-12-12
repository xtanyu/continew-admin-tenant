package top.continew.admin.controller.tenant;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;
import top.continew.admin.common.base.BaseController;
import top.continew.admin.tenant.model.query.TenantDbConnectQuery;
import top.continew.admin.tenant.model.req.TenantDbConnectReq;
import top.continew.admin.tenant.model.resp.TenantDbConnectDetailResp;
import top.continew.admin.tenant.model.resp.TenantDbConnectResp;
import top.continew.admin.tenant.service.TenantDbConnectService;
import top.continew.starter.extension.crud.annotation.CrudRequestMapping;
import top.continew.starter.extension.crud.enums.Api;

/**
 * 租户数据连接管理 API
 *
 * @author 小熊
 * @since 2024/12/12 19:13
 */
@Tag(name = "租户数据连接管理 API")
@RestController
@CrudRequestMapping(value = "/tenant/dbConnect", api = {Api.PAGE, Api.DETAIL, Api.ADD, Api.UPDATE, Api.DELETE})
public class TenantDbConnectController extends BaseController<TenantDbConnectService, TenantDbConnectResp, TenantDbConnectDetailResp, TenantDbConnectQuery, TenantDbConnectReq> {}