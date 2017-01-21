package design.badbag.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import design.badbag.models.Item;
import design.badbag.models.SiteUser;

/*
 * 
 * Things in this Controller:
 * 	- myOrders
 * 	- myItems
 *  - addItem
 * 
 */

@Controller
public class StorebackController extends AbstractController {
	@RequestMapping(value = "/manageOrders")
	public String manageOrders(HttpServletRequest request, Model model) {
		
		SiteUser siteUser = getUserFromSession(request.getSession());
		
		List<Item> items = itemDao.findByDesigner(siteUser);
		
		model.addAttribute("items", items);
		
		return "myOrders";
	}
	
	
	
	@RequestMapping(value = "/manageItems", method = RequestMethod.GET)
	public String myItems(HttpServletRequest request, Model model) {
		
		SiteUser siteUser = getUserFromSession(request.getSession());
		
		List<Item> items = itemDao.findByActiveAndDesigner(true, siteUser);
		
		model.addAttribute("items", items);
		
		return "myItems";
	}
	
	@RequestMapping(value = "/manageItems", method = RequestMethod.POST)
	public String manageItems(HttpServletRequest request, Model model) {
		SiteUser siteUser = getUserFromSession(request.getSession());
		int item_id = Integer.parseInt(request.getParameter("item_ID"));
		Double item_price = Double.parseDouble(request.getParameter("price"));
		int item_stock = Integer.parseInt(request.getParameter("inStock"));
		
		Item item = itemDao.findByUid(item_id);
		
		//if they hit delete
		if (request.getParameter("delete") != null) {
			
			item.setActive(false);
			
		}
		
		//if they hit update
		if (request.getParameter("update") != null) {
			
			if (item.getPrice() != item_price) {
				item.setPrice(item_price);
			}
			
			if (item.getInStock() != item_stock) {
				item.setInStock(item_stock);
			}
			
			
		}
		
		itemDao.save(item);

		
		List<Item> items = itemDao.findByActiveAndDesigner(true, siteUser);
		
		model.addAttribute("items", items);
		
		return "myItems";
	}
	
	
	
	@RequestMapping(value = "/addItem", method = RequestMethod.GET)
	public String addItemForm(Model model) {
		return "addItem";
	}

	@RequestMapping(value = "/addItem", method = RequestMethod.POST)
	public String addItem(HttpServletRequest request, Model model, @RequestParam("itemImage") MultipartFile imageFile) {
		String itemName = request.getParameter("itemName");
		String itemDescription = request.getParameter("itemName");
		String itemDollars = request.getParameter("itemDollars");
		String itemCents = request.getParameter("itemCents");
		String itemStockString = request.getParameter("itemStock");

		Double itemPrice = 0.0;
		int itemStock = 0;

		SiteUser activeUser = this.getUserFromSession(request.getSession());

		if (exists(itemName)) {

			if (!exists(itemDollars)) { itemDollars = "0"; }

			if (!exists(itemCents)) { itemCents = "0"; }

			itemPrice = (Double.parseDouble(itemDollars) + (Double.parseDouble(itemCents) / 100));

			if (exists(itemStockString)) { itemStock = Integer.parseInt(itemStockString); }

			Item item = new Item(itemName, itemPrice, itemStock, activeUser);

			if (exists(itemDescription)) { item.setDescription(itemDescription); }
			
			itemDao.save(item);
			
			System.out.println(item.getUid());
			
			String newFileName = uploadImage(imageFile, item.getUid());
			
			if (newFileName.contains("Error")) {
				
				model.addAttribute("error", newFileName);
				
			} else {
				
				item.addImage(newFileName);
				itemDao.save(item);
			}

		}

		return "addItem";
	}
}
