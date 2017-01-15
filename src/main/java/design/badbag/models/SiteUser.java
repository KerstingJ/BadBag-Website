package design.badbag.models;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "site_user")
public class SiteUser extends AbstractEntity {

	private String username, email;
	private String pwHash;
	private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	private List<Post> posts;
	private List<Item> items;
	private List<CartItem> cart;
	
	
	public SiteUser() {}
	
	public SiteUser(String username, String password) {
		
		super();
		
		if (!isValidUsername(username)) {
			throw new IllegalArgumentException("Invalid username");
		}
		
		this.username = username;
		this.pwHash = hashPassword(password);
		
	}
	
	@NotNull
    @Column(name = "pwhash")
	public String getPwHash() {
		return pwHash;
	}
	
	@SuppressWarnings("unused")
	private void setPwHash(String pwHash) {
		this.pwHash = pwHash;
	}
	
	@NotNull
    @Column(name = "username", unique = true)
	public String getUsername() {
		return username;
	}
	
	private static String hashPassword(String password) {		
		return encoder.encode(password);
	}
	
	@SuppressWarnings("unused")
	private void setUsername(String username) {
		this.username = username;
	}
	
	public boolean isMatchingPassword(String password) {
		return encoder.matches(password, pwHash);
	}
	
	public static boolean isValidPassword(String password) {
		Pattern validUsernamePattern = Pattern.compile("(\\S){6,20}");
		Matcher matcher = validUsernamePattern.matcher(password);
		return matcher.matches();
	}
	
	public static boolean isValidUsername(String username) {
		Pattern validUsernamePattern = Pattern.compile("[a-zA-Z][a-zA-Z0-9_-]{4,11}");
		Matcher matcher = validUsernamePattern.matcher(username);
		return matcher.matches();
	}
	
	public static boolean isValidEmail(String email) {
		Pattern validEmailPattern = Pattern.compile("[a-zA-Z][a-zA-Z0-9_-]{3,20}@[a-zA-Z]{2,30}.");
		Matcher matcher = validEmailPattern.matcher(email);
		return matcher.matches();
	}
	
	protected void addPost(Post post) {
		posts.add(post);
	}
	
	@OneToMany
    @JoinColumn(name = "author_uid")
    public List<Post> getPosts() {
        return posts;
    }
	
	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
	
	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	protected void addItem(Item item) {
		items.add(item);
	}
	
	@OneToMany
    @JoinColumn(name = "designer_uid")
    public List<Item> getItems() {
        return items;
    }

	@Column(name = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@OneToMany
	@JoinColumn(name = "shopper_id")
	public List<CartItem> getCart() {
		return cart;
	}

	public void setCart(List<CartItem> cart) {
		this.cart = cart;
	}
	
	public void addToCart(CartItem item) {
		this.cart.add(item);
	}
	
	public void removeFromCart(CartItem item) {
		this.cart.remove(item);
	}
	
	public Double findCartTotal() {
		Double total = 0.0;
		
		for (CartItem item: cart) {
			total += item.getItem().getPrice() * item.getQuantity();
		}
		
		return total;
	}
}
