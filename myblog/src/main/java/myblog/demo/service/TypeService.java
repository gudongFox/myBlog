package myblog.demo.service;

import myblog.demo.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {

    //保存功能接口
    Type saveType(Type type);

    //查询功能接口
    Type getType(Long id);

    Type getTypeByName(String name);

    //分页查询功能接口
    Page<Type> listType(Pageable pageable);

    //修改功能接口
    Type updateType(Long id, Type type);

    //删除功能接口
    void deleteType(Long id);

    List<Type> ListType();
    List<Type> listTap(Integer size);  //首页展示条数
}
