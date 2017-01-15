package design.badbag.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="items")
public class Item extends AbstractEntity {
	private String name, description;
	
	//add support for multiple image files
	//private List<String> imagePaths;
	private String imagePath;
	private SiteUser designer;
	private Double price;
	private int inStock;
	private boolean active;

	public Item() {}
	
	public Item(String name, Double price, int stock, SiteUser designer) {
		
		super();
		
		this.name = name;
		this.price = price;
		this.inStock = stock;
		this.designer = designer;
		this.active = true;
	}
	
	@Column(name="name")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name="imagePath")
	public String getImagePaths() {
		return imagePath;
	}
	
	public void setImagePaths(String imagePaths) {
		this.imagePath = imagePaths;
	}
	
	public void addImage(String path) {
		this.imagePath = path;
		
		//this.imagePaths.add(path);
	}
	
	@ManyToOne
	public SiteUser getDesigner() {
		return designer;
	}
	public void setDesigner(SiteUser designer) {
		this.designer = designer;
	}
	
	@Column(name="price")
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	
	@Column(name="stock")
	public int getInStock() {
		return inStock;
	}
	public void setInStock(int inStock) {
		this.inStock = inStock;
	}
	
	@Column(name="active")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
