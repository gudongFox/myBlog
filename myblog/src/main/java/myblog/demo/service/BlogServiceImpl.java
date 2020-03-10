package myblog.demo.service;

import myblog.demo.NotFoundException;
import myblog.demo.dao.BlogRepository;
import myblog.demo.po.Blog;
import myblog.demo.po.Type;
import myblog.demo.util.MarkdownUtils;
import myblog.demo.util.MyBeanUtils;
import myblog.demo.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    //注入数据库
    @Autowired
   private BlogRepository blogRepository;

    //查询功能
    @Override
    public Blog getBlog(Long id) {
        return blogRepository.findById(id).get();
    }

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog b = new Blog();
        Blog blog = blogRepository.findById(id).get();
        if (blog == null){
            throw  new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog,b);
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));

        blogRepository.updateViews(id);
        return b;
    }


    //分页查询
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null){
                    predicates.add(cb.like(root.<String>get("title"), "%"+blog.getTitle()+"%"));
                }
                if (blog.getTypeId() != null){
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if (blog.isRecommend()){
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    //搜索页面展示
    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findQuery(query, pageable);
    }

    //根据tagId查询博客
    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                //将blog与tags两张表关联起来
                Join join = root.join("tags");
                return cb.equal(join.get("id"), tagId);  //会返回满足条件的页面
            }
        }, pageable);
    }

    //前端首页推荐展示
    @Override
    public List<Blog> listBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogRepository.findTop(pageable);
    }


    //归档
    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years){
            map.put(year, blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }

    //保存功能
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        } else {
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }


    //更新功能
    @Transactional  //事务
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog blog1 = blogRepository.findById(id).get();
        if (blog1 == null){
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog,blog1, MyBeanUtils.getNullPropertyNames(blog));
        blog1.setUpdateTime(new Date());
        return blogRepository.save(blog1);
    }

    //删除功能
    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
