package myblog.demo.dao;


import myblog.demo.po.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findByName(String name);

    //自定义查询
    @Query("select t from Tag t")
    List<Tag> findTop(Pageable pageable);
}
