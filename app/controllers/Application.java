package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Activity;
import models.Alert;
import models.Alert.AlertTreshold;
import models.AlertType;
import models.Employee;
import models.Employee.HealthIssue;
import models.EmployeeActivity;
import models.EmployeeViewModel;
import models.Machine;
import models.User;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import com.fasterxml.jackson.databind.JsonNode;

public class Application extends Controller {

	public static Result index() {
		return ok();
	}

	public static Result getEmployees() {
		List<EmployeeViewModel> viewModels = new ArrayList<EmployeeViewModel>();
		List<Employee> employees = Employee.find.all();
		for (Employee employee : employees) {
			EmployeeViewModel model = new EmployeeViewModel();
			model.id = employee.id;
			model.name = employee.name;
			model.address = employee.address;
			model.phoneNumber = employee.phoneNumber;
			model.age = employee.age;
			model.healthIssue = employee.healthIssue;
			model.totalHours = getTotalWorkingHour(employee.id);
			viewModels.add(model);
		}
		
		return ok(Json.toJson(viewModels));
	}
	
	public static Result getMachines() {
		return ok(Json.toJson(Machine.find.all()));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result addEmployee() {
		JsonNode json = request().body().asJson();
		String id = json.findPath("id").textValue();
		String name = json.findPath("name").textValue();
		String address = json.findPath("address").textValue();
		Long phoneNumber = json.findPath("phoneNumber").longValue();
		HealthIssue healthIssue = HealthIssue.values()[json.findPath(
				"healthIssue").asInt()];
		int age = json.findPath("age").asInt();

		// add to the database
		// Created implicit transaction
		List<Employee> employees = Employee.find.where().eq("id", id)
				.findList();
		// Transaction commited or rollbacked
		if (employees.size() == 0) {
			// Created implicit transaction
			Employee employee = new Employee();
			employee.id = id;
			employee.name = name;
			employee.address = address;
			employee.phoneNumber = phoneNumber;
			employee.age = age;
			employee.healthIssue = healthIssue;
			employee.createdAt = new Date();
			employee.lastModified = new Date();
			Ebean.save(employee);
			// Transaction commited or rollbacked
		}

		return ok();
	}

	public static Result getActivities(String identifier) {
		String sql = "SELECT machine.id AS id, machine.name AS name, "
				+ " machine.description AS machine_description, machine.noise_level AS noise_level,"
				+ " SUM(check_out_date - check_in_date) AS total_hours"
				+ " FROM activity INNER JOIN machine ON activity.machine_id=machine.id"
				+ " WHERE employee_id = :identifier"
				+ " group by id";
		 
		List<SqlRow> sqlRows = 
		    Ebean.createSqlQuery(sql)
		        .setParameter("identifier", identifier)
		        .findList();
		 
		List<EmployeeActivity> activities = new ArrayList<EmployeeActivity>();
		for (SqlRow sqlRow : sqlRows) {
			EmployeeActivity empActivity = new EmployeeActivity();
			empActivity.id = sqlRow.getLong("id");
			empActivity.name = sqlRow.getString("name");
			empActivity.description = sqlRow.getString("machine_description");
			empActivity.noiseLevel = sqlRow.getInteger("noise_level");
			empActivity.totalHours = sqlRow.getLong("total_hours");
			activities.add(empActivity);
		}
		return ok(Json.toJson(activities));
	}
	
	public static long getTotalWorkingHour(String identifier) {
		String sql = "SELECT "
				+ " SUM(check_out_date - check_in_date) AS total_hours"
				+ " FROM activity INNER JOIN machine ON activity.machine_id=machine.id"
				+ " WHERE employee_id = :identifier"
				+ " and DATE(check_in_date) = DATE(NOW()) ";
		 
		long totalHours = 0;
		try {
			List<SqlRow> sqlRows = 
				    Ebean.createSqlQuery(sql)
				        .setParameter("identifier", identifier)
				        .findList();
				for (SqlRow sqlRow : sqlRows) {
					totalHours = sqlRow.getLong("total_hours");
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return totalHours;
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result addActivity(String id) {

		JsonNode json = request().body().asJson();
		Long machineId = json.findPath("machineId").asLong();

		// find the employee
		List<Employee> employees = Employee.find.where().eq("id", id)
				.findList();
		if (employees.size() == 0) {
			return badRequest("employee not exists");
		}
		Employee employee = employees.get(0);

		List<Machine> machines = Machine.find.where().eq("id", machineId)
				.findList();
		if (machines.size() == 0) {
			return badRequest("machines not exists");
		}
		Machine machine = machines.get(0);

		// Check if this is check in or out
		List<Activity> activities = Activity.find.where()
				.eq("employee.id", id).eq("machine.id", machineId)
				.eq("checkOutDate", null)
				.findList();
		if (activities.size() > 0) {
			// do an update
			Activity activity = activities.get(0);
			activity.checkOutDate = new Date();
			Ebean.save(activity);
		} else {
			// Add to the database
			// Created implicit transaction
			Activity activity = new Activity();
			activity.employee = employee;
			activity.machine = machine;
			activity.checkInDate = new Date();
			Ebean.save(activity);
		}
		

		return ok();
	}

	public static Result getAlerts(String username) {
		return ok(Json.toJson(Alert.find.where().eq("user.username", username)
				.findList()));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result addAlert(String username) {

		JsonNode json = request().body().asJson();
		int alertId = json.findPath("alertType").asInt();
		AlertTreshold treshold = AlertTreshold.values()[json.findPath(
				"alertTreshold").asInt()];
		int tresholdValue = json.findPath("tresholdValue").asInt();

		// find the employee
		List<AlertType> alertTypes = AlertType.find.where().eq("id", alertId)
				.findList();
		if (alertTypes.size() == 0) {
			return badRequest("alert type not exists");
		}
		AlertType alertType = alertTypes.get(0);

		List<User> users = User.find.where().eq("username", username)
				.findList();
		if (users.size() == 0) {
			return badRequest("user not exists");
		}
		User user = users.get(0);

		// Add to the database

		// Created implicit transaction
		Alert alert = new Alert();
		alert.user = user;
		alert.alertType = alertType;
		alert.treshold = treshold;
		alert.tresholdValue = tresholdValue;
		alert.createdAt = new Date();
		Ebean.save(alert);

		return ok();
	}
}
