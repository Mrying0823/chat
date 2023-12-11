package org.mrying.chat.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mrying.chat.model.DicValue;

import java.util.List;

/**
 * @author 邓和颖
 */
@Mapper
public interface DicValueMapper {

    // 根据 type_code 和 created_by 查询数据字典值
    List<DicValue> selectDicValueByTypeCodeAndCreatedBy(@Param("typeCode") String typeCode, @Param("createdBy") String createdBy);
}