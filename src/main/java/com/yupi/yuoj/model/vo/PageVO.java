package com.yupi.yuoj.model.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 分页视图（脱敏）
 *
 */
@Data
public class PageVO<T> implements Serializable {

    protected List<T> records;
    protected long total;
    protected long pageSize;
    protected long current;

    private static final long serialVersionUID = 1L;
}