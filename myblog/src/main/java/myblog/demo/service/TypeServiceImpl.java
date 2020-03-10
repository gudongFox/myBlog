package myblog.demo.service;

import myblog.demo.NotFoundException;
import myblog.demo.dao.TypeRepository;
import myblog.demo.po.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class TypeServiceImpl implements  TypeService {

    //注入dao数据库层的TypeRepository
    @Autowired
    private TypeRepository typeRepository;

    //保存功能实现
    @Transactional
    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    //查询功能实现
    @Transactional
    @Override
    public Type getType(Long id) {
        return typeRepository.findById(id).get();
    }

    @Override
    public Type getTypeByName(String name) {
        return typeRepository.findByName(name);
    }

    //实现分页查询功能
    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    //实现更新功能
    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t = typeRepository.findById(id).get();
        if (t == null){
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(type, t);
        return typeRepository.save(t);
    }

    //实现删除功能
    @Transactional
    @Override
    public void deleteType(Long id) {
        typeRepository.deleteById(id);
    }

    @Override
    public List<Type> ListType() {
        return typeRepository.findAll();
    }

    @Transactional
    @Override
    public List<Type> listTap(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = PageRequest.of(0,size,sort);
        return typeRepository.findTop(pageable);
    }
}
