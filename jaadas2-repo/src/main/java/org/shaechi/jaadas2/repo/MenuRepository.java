package org.shaechi.jaadas2.repo;


import org.shaechi.jaadas2.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long>{
    Menu findByUrl(String url);
}
