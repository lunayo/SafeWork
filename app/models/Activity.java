package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Activity extends Model {
	
	@Id
	@Constraints.Min(10)
	public Long id;
	@ManyToOne(targetEntity = Employee.class)
	public Employee employee;
	@ManyToOne(targetEntity = Employee.class)
	public Machine machine;
	public Date checkInDate;
	public Date checkOutDate;

	public static Finder<Long, Activity> find = new Finder<Long, Activity>(
			Long.class, Activity.class);
}
