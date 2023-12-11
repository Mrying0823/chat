package org.mrying.chat.service;

import org.mrying.chat.model.DicValue;

import java.util.List;

/**
 * @author 邓和颖
 * @since 2023/12/10 21:22
 */
public interface DicValueService {

    // 根据 type_code 和 created_by 查询数据字典值
    List<DicValue> queryDicValueByTypeCodeAndCreatedBy(String typeCode, String createdBy);
}
