package com.mango.sys.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author kem
 * @since 2019-07-06
 */
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class SysRoleHasSysResource implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "sys_role_id", type = IdType.INPUT)
    private Long sysRoleId;

    private Long sysResourceId;

    private JSONObject filter;

    private String remark;


}
