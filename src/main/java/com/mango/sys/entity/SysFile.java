package com.mango.sys.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class SysFile implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String uuid;

    private String name;

    private String type;

    private Long size;

    private String url;

    private String store;

    private JSONObject extFields;

    private JSONObject permission;

    private Boolean isDeleted;

    private Boolean isSynced;

    private Long creatorId;

    private LocalDateTime createTime;

    private Long modifierId;

    private LocalDateTime modifyTime;

    private String remark;


}
