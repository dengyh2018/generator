package org.mybatis.generator.modules.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.generator.modules.entity.Datasource;
import java.util.List;

/**
 *
 *<datasource Dao自定义方法接口>
 *
 * @since 2019/11/15 10:33:13
 * @table datasource
 * @mbggenerated
 */
//@Mapper
public interface DatasourceMapper extends DatasourceBaseMapper {

    List<Datasource> list(Datasource datasource);

}