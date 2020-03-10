package myblog.demo.service;

import myblog.demo.po.Blog;
import myblog.demo.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {

    //查询
    Blog getBlog(Long id);
    Blog getAndConvert(Long id);

    //分页查询
    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);
    Page<Blog> listBlog(Pageable pageable);
    Page<Blog> listBlog(String query, Pageable pageable);

    //针对标签分类的查询方法
    Page<Blog> listBlog(Long tagId, Pageable pageable);

    //首页推荐显示功能
    List<Blog> listBlogTop(Integer size);

    //归档功能
    Map<String,List<Blog>> archiveBlog();
    Long countBlog();

    //新增
    Blog saveBlog(Blog blog);

    //修改
    Blog updateBlog(Long id,Blog blog);

    //删除
    void deleteBlog(Long id);

}
