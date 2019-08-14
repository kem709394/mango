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
 * @since 2019-07-30
 */
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class SysMessage implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String type;

    private String title;

    private String content;

    private String note;

    private JSONObject extFields;

    private Long sysUserId;

    private String state;

    private LocalDateTime readTime;

    private Boolean isDeleted;

    private Long creatorId;

    private LocalDateTime createTime;

    private Long modifierId;

    private LocalDateTime modifyTime;

    private String remark;


}
