package com.mango.sys.entity;

import com.alibaba.fastjson.JSONArray;
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
 * @since 2019-08-01
 */
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class SysMail implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String subject;

    private String content;

    private String fromSite;

    private JSONArray toSite;

    private JSONArray ccSite;

    private JSONArray attachment;

    private JSONArray inline;

    private Integer counter;

    private String state;

    private LocalDateTime sendTime;

    private String message;

    private Boolean isDeleted;

    private Long creatorId;

    private LocalDateTime createTime;

    private String remark;


}
