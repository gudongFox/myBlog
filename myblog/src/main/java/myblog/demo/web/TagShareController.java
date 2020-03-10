package myblog.demo.web;

import myblog.demo.po.Tag;
import myblog.demo.service.BlogService;
import myblog.demo.service.TagService;
import myblog.demo.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TagShareController {

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/tags/{id}")
    public String tags(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        @PathVariable Long id, Model model){
        //按照每个分类的博客数目排列,分页查询
        List<Tag> tags = tagService.listTagTop(10000);
        if (id == -1){  // 默认分类时
            id = tags.get(0).getId();
        }
        model.addAttribute("tags", tags);
        model.addAttribute("page", blogService.listBlog(id, pageable));
        //传回id到前端，表示选中状态
        model.addAttribute("activeTagId", id);
        return "tags";
    }
}
