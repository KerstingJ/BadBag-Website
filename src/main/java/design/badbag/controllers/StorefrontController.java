package design.badbag.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import design.badbag.models.CartItem;
import design.badbag.models.Item;
import design.badbag.models.SiteUser;

/*
 * 
 * Things in this Controller:
 * 	- index, landing page
 * 	- viewCart
 * 	- checkOut
 *  - item/*
 * 
 * 
 */


@Controller
public class StorefrontController extends AbstractController {

	@RequestMapping( value = {"/index", "/"}, method = RequestMethod.GET)
	public String index(Model model) {
		
		List<Item> items = itemDao.findByActive(true);
		
		model.addAttribute("items", items);
		return "index";
	}
	
	@RequestMapping(value = {"/index", "/"}, method = RequestMethod.POST)
	public String addItem(HttpServletRequest request, Model model) {
		
		//get user and item_id
		SiteUser siteUser = getUserFromSession(request.getSession());
		int item_id = Integer.parseInt(request.getParameter("item_ID"));
		
		//get item and add to cart
		Item item = itemDao.findByUid(item_id);
		CartItem cartItem = new CartItem(siteUser, item, 1);
		cartItemDao.save(cartItem);
		siteUser.addToCart(cartItem); //will need to get a quantity to add to cart
		siteUserDao.save(siteUser);
		
		List<Item> items = itemDao.findByActive(true);

		model.addAttribute("items", items);
		return "index";
	}
	
	
	
	@RequestMapping(value = "/viewCart", method = RequestMethod.GET)
	public String viewCart(HttpServletRequest request, Model model) {
		
		SiteUser siteUser = getUserFromSession(request.getSession());
		List<CartItem> cart = siteUser.getCart();
		
		model.addAttribute("cart_items", cart);
		return "viewCart";
	}
	
	@RequestMapping(value = "/viewCart", method = RequestMethod.POST)
	public String manageCart(HttpServletRequest request, Model model) {
		SiteUser siteUser = getUserFromSession(request.getSession());
		
		if (request.getParameter("remove") != null) {
			int item_id = Integer.parseInt(request.getParameter("item_ID"));
			Item item = itemDao.findByUid(item_id);
			
			List<CartItem> removeItems = cartItemDao.findByShopperAndItem(siteUser, item);
			
			for(CartItem i: removeItems) {
				cartItemDao.delete(i);
			}
			
		}
		
		
		List<CartItem> cart = siteUser.getCart();
		
		model.addAttribute("cart_items", cart);
		return "viewCart";
	}
	
	
	
	@RequestMapping(value = "/checkOut")
	public String checkOut(HttpServletRequest request, Model model) {
		
		//put together list of items from users cart
		//total the costs of items
		
		SiteUser siteUser = getUserFromSession(request.getSession());
		Double total = 0.0;
		
		List<CartItem> items = cartItemDao.findByShopper(siteUser);
		
		for (CartItem i: items){
			
			total = total + i.getItem().getPrice();
		}
		
		model.addAttribute("items", items);
		model.addAttribute("cartTotal", total);
		return "checkOut";
	}
	
	
	@RequestMapping(value = "/item/{item_id}", method = RequestMethod.GET)
	public String itemPage(@PathVariable String item_id, HttpServletRequest request, Model model) {
		
		Item item = itemDao.findByUid(Integer.parseInt(item_id));
		
		model.addAttribute("item", item);
		return "item";
	}

}
