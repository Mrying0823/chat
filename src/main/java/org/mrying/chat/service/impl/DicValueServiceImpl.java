package org.mrying.chat.service.impl;

import org.mrying.chat.mapper.DicValueMapper;
import org.mrying.chat.model.DicValue;
import org.mrying.chat.service.DicValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 邓和颖
 * @since 2023/12/10 21:23
 */
@Service("dicValueService")
public class DicValueServiceImpl implements DicValueService {

    private DicValueMapper dicValueMapper;

    @Autowired
    public void setDicValueMapper(DicValueMapper dicValueMapper) {
        this.dicValueMapper = dicValueMapper;
    }

    // 根据 type_code 和 created_by 查询数据字典值
    @Override
    public List<DicValue> queryDicValueByTypeCodeAndCreatedBy(String typeCode, String createdBy) {
        return dicValueMapper.selectDicValueByTypeCodeAndCreatedBy(typeCode, createdBy);
    }
}
