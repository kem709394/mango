package com.mango.sys.entity;

import com.alibaba.fastjson.JSONArray;
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
public class SysResource implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String code;

    private String name;

    private String path;

    private String method;

    private String mapClass;

    private JSONArray attributes;

    private String note;

    private Integer priority;

    private JSONObject extFields;

    private String state;

    private Boolean isDeleted;

    private Long creatorId;

    private LocalDateTime createTime;

    private Long modifierId;

    private LocalDateTime modifyTime;

    private String remark;


}
