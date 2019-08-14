package com.mango.sys.entity;

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
public class SysCaptcha implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String session;

    private String code;

    private LocalDateTime expireTime;

    private String remark;


}
