package top.continew.admin.tenant.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.continew.starter.extension.crud.model.resp.BaseResp;

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

    /**
     * 连接密码
     */
    @Schema(description = "连接密码")
    private String password;
}