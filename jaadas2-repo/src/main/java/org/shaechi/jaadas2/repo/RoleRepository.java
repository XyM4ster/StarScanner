package org.shaechi.jaadas2.repo;


import org.shaechi.jaadas2.entity.Menu;
import org.shaechi.jaadas2.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
    List<Role> findByMenu(Menu menu);

}
