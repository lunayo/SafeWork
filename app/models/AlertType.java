package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class AlertType extends Model {
	@Id
	@Constraints.Min(10)
	public Long id;
	@Constraints.Required
	public String name;
	public String description;
	public Date createdAt;
	public Date lastModified;
	
	public static Finder<Long, AlertType> find = new Finder<Long, AlertType>(
			Long.class, AlertType.class);
}
