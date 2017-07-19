package helpers;

import java.util.List;

import models.Activity;
import models.Alert;
import models.Machine;

public class AlertNotification {
	
	public void pushNotification() {
		
	}

	public void checkNotification() {
		// Check alert table if there is outstanding alert
		Boolean shouldAlert = false;
		List<Alert> alerts = Alert.find.all();
		for (Alert alert : alerts) {
			switch (alert.alertType.id.intValue()) {
			case 1:
				// Machine noise level
				switch (alert.treshold) {
				case LessThanEqual:
					if (Machine.find.where().le("noiseLevel", alert.tresholdValue).findList().size() > 0)
						shouldAlert = true;
					break;
				case Equal:
					if (Machine.find.where().eq("noiseLevel", alert.tresholdValue).findList().size() > 0)
						shouldAlert = true;
					break;
				case MoreThanEqual:
					if (Machine.find.where().ge("noiseLevel", alert.tresholdValue).findList().size() > 0)
						shouldAlert = true;
					break;
				default:
					break;
				}
				break;
			case 2:
				break;
			case 3:
				// Machine used hours per day
				break;
			case 4:
				// Machine on/off frequency per day
				break;
			}
		}
	}
}
