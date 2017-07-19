package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity
public class Machine extends Model {
	
	@Id
	@Constraints.Min(10)
	public Long id;
	@Constraints.Required
	public String name;
	public String description;
	public Date datePurchased;
	public Date lastMaintainence;
	public int noiseLevel;
	@ManyToMany
    public List<MachineType> types = new ArrayList<MachineType>();

	public static Finder<Long, Machine> find = new Finder<Long, Machine>(
			Long.class, Machine.class);
}
