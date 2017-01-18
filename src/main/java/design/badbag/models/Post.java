package design.badbag.models;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "post")
public class Post extends AbstractEntity {
	
	private String body;
	private SiteUser author;
	private Date modified;
	
	public static boolean isValidString(String value) {
		/*
		 *  Static method to ensure posts are not made of spaces
		 */
		Pattern validString = Pattern.compile("^ *$");
		Matcher matcher = validString.matcher(value);
		return matcher.matches();
	}
	
	public Post() {}
	
	public Post(String body, SiteUser author) {
		
		super();

		this.body = body;
		this.author = author;
		this.updated();
		
		author.addPost(this);
	}


	@NotNull
    @Column(name = "body", length = 10485760)
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
		this.updated();
	}
	
	@ManyToOne
	public SiteUser getAuthor() {
		return author;
	}
	
	@SuppressWarnings("unused")
	private void setAuthor(SiteUser author) {
		this.author = author;
	}
	
	@NotNull
	@Column(name = "modified")
	public Date getModified() {
		return modified;
	}
	
	private void updated() {
		this.modified = new Date();
	}
	
}
