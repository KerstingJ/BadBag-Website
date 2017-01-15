package design.badbag.models.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import design.badbag.models.Item;
import design.badbag.models.SiteUser;

@Transactional
@Repository
public interface ItemDao extends CrudRepository<Item, Integer> {

    Item findByUid(int uid);
    
    List<Item> findAll();
    
    List<Item> findByDesigner(SiteUser siteUser);
    List<Item> findByActive(boolean active);
    
    List<Item> findByActiveAndDesigner(boolean active, SiteUser siteUser);
    
    

}
