package design.badbag.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public abstract class AbstractEntity {

	private int uid;
	private Date created, modified;
	
	public AbstractEntity() {
		this.created = new Date();
	}
	
	@Id
    @GeneratedValue
    @NotNull
    @Column(name = "uid", unique = true)
	public int getUid() {
		return this.uid;
	}
	
	protected void setUid(int uid) {
        this.uid = uid;
    }
	
	@Column(name = "date_created")
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	@Column(name = "date_modified")
	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}
	
}
