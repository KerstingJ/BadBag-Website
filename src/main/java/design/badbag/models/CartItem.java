package design.badbag.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cartItem")
public class CartItem extends AbstractEntity {

	private SiteUser shopper;
	private Item item;
	private int quantity;
	
	public CartItem() {}
	
	public CartItem(SiteUser shopper, Item item, int quantity) {
		this.shopper = shopper;
		this.item = item;
		this.quantity = quantity;
	}
	
	@ManyToOne
	public SiteUser getShopper() {
		return shopper;
	}
	public void setShopper(SiteUser shopper) {
		this.shopper = shopper;
	}
	
	@ManyToOne
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	
	@Column(name="quantity")
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int qunatity) {
		this.quantity = qunatity;
	}

}
