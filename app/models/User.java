package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity
public class User extends Model {
	@Id
	@Constraints.Min(6)
	public String username;
	@Constraints.Required
	public String fullname;
	public String password;
	public Long phoneNumber;
	public Date createdAt;
	public Date lastModified;
	
	public static Finder<Long, User> find = new Finder<Long, User>(
			Long.class, User.class);
}
