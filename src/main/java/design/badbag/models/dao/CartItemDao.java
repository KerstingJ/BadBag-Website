package design.badbag.models.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import design.badbag.models.CartItem;
import design.badbag.models.Item;
import design.badbag.models.SiteUser;

@Transactional
@Repository
public interface CartItemDao extends CrudRepository<CartItem, Integer> {

    CartItem findByUid(int uid);
    
    List<CartItem> findAll();
    
    List<CartItem> findByShopperAndItem(SiteUser siteUser, Item item);
    List<CartItem> findByShopper(SiteUser siteUser);
    
    List<CartItem> findByItem(Item item);
    
    

}
