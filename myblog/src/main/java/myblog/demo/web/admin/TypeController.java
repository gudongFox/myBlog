package myblog.demo.web.admin;


import myblog.demo.po.Type;
import myblog.demo.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    public String Types(@PageableDefault(size = 10,sort = {"id"}, direction = Sort.Direction.DESC)
                                Pageable pageable, Model model) {
        model.addAttribute("page",typeService.listType(pageable));

        return "admin/types";
    }

    //返回“新增”页面
    @GetMapping("/types/input")
    public String addInput(Model model){
        model.addAttribute("type",new Type());
        return "admin/types-input";
    }

    //编辑分类功能
    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("type",typeService.getType(id));
        return "admin/types-input";
    }

    //提交新增数据
    @PostMapping("/types")
    public String post(@Valid Type type, BindingResult result, RedirectAttributes attributes) {
        Type t = typeService.getTypeByName(type.getName());
        if (t != null) {
            result.rejectValue("name", "nameError", "该分类名称不能重复添加");
        }
        if (result.hasErrors()) {
            return "admin/types-input";
        }
        Type type1 = typeService.saveType(type);
        if (type1 == null) {
            //保存失败
            attributes.addFlashAttribute("message", "新增失败");
        } else {
            //保存成功
            attributes.addFlashAttribute("message", "新增成功");
        }
        return "redirect:/admin/types";
    }

    //编辑操作事宜
    @PostMapping("/types/{id}")
    public String editPost(@Valid Type type, BindingResult result, @PathVariable Long id, RedirectAttributes attributes){
        Type t = typeService.getTypeByName(type.getName());
        if (t != null){
            result.rejectValue("name","nameError","该分类名称不能重复添加");
        }
        if (result.hasErrors()){
            return "admin/types-input";
        }
        Type type1 = typeService.updateType(id, type);
        if (type1 == null){
            //保存失败
            attributes.addFlashAttribute("message","更新失败");
        }else {
            //保存成功
            attributes.addFlashAttribute("message","更新成功");
        }
        return "redirect:/admin/types";
    }

    //删除
    @GetMapping("types/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        typeService.deleteType(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/types";
    }
}
