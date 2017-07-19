package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity
public class MachineType extends Model {
	@Id
	@Constraints.Min(10)
	public Long id;
	@Constraints.Required
	public String name;
	public String description;
	public int hazardousLevel;

	@ManyToMany(mappedBy = "types")
	public List<Machine> machines = new ArrayList<Machine>();
}
