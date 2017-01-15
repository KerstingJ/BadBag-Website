package design.badbag.models.dao;

import java.util.List;

import javax.transaction.Transactional;

import design.badbag.models.SiteUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface SiteUserDao extends CrudRepository<SiteUser, Integer> {

    SiteUser findByUid(int uid);
    
    List<SiteUser> findAll();
    
    SiteUser findByUsername(String username);
    
    // TODO - add method signatures as needed

}
