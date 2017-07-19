package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity
public class Alert extends Model {
	public enum AlertTreshold {
		LessThanEqual, Equal, MoreThanEqual;
		
		private static AlertTreshold[] allValues = values();
	    public static AlertTreshold fromOrdinal(int n) {return allValues[n];}
	}
	
	@Id
	@Constraints.Min(10)
	public Long id;
	@ManyToOne(targetEntity = User.class)
	public User user;
	@ManyToOne(targetEntity = AlertType.class)
	public AlertType alertType;
	public AlertTreshold treshold;
	public int tresholdValue;
	public Date createdAt;

	public static Finder<Long, Alert> find = new Finder<Long, Alert>(
			Long.class, Alert.class);
}
