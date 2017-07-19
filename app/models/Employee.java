package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity
public class Employee extends Model {
	
	public enum HealthIssue {
		Normal, ColorBlindness, HeartProblem;
		
		private static HealthIssue[] allValues = values();
	    public static HealthIssue fromOrdinal(int n) {return allValues[n];}
	}

	@Id
	@Constraints.Min(16)
	public String id;
	@Constraints.Required
	public String name;
	public String address;
	public Long phoneNumber;
	public int age;
	public HealthIssue healthIssue;
	public Date createdAt;
	public Date lastModified;

	public static Finder<Long, Employee> find = new Finder<Long, Employee>(
			Long.class, Employee.class);
}
