import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.Akka;
import scala.concurrent.duration.Duration;

public class Global extends GlobalSettings {

	@Override
	public void onStart(Application application) {

		// Akka.system().scheduler().scheduleOnce(
		// Duration.create(10, TimeUnit.MILLISECONDS),
		// () -> Logger.info("ON START ---    " + System.currentTimeMillis(),
		// Akka.system().dispatcher()
		// );
		Akka.system()
				.scheduler()
				.schedule(Duration.create(0, TimeUnit.MILLISECONDS),
						Duration.create(1, TimeUnit.MINUTES), () -> {
							// Check the database for new alerts
							
						}, Akka.system().dispatcher());

		// Akka.system().scheduler().schedule(
		// Duration.create(nextExecutionInSeconds(8, 0), TimeUnit.SECONDS),
		// Duration.create(24, TimeUnit.HOURS),
		// new Runnable() {
		// @Override
		// public void run() {
		// Logger.info("EVERY DAY AT 8:00 ---    " +
		// System.currentTimeMillis());
		// }
		// }
		// );
	}

	public static int nextExecutionInSeconds(int hour, int minute) {
		return Seconds.secondsBetween(new DateTime(),
				nextExecution(hour, minute)).getSeconds();
	}

	public static DateTime nextExecution(int hour, int minute) {
		DateTime next = new DateTime().withHourOfDay(hour)
				.withMinuteOfHour(minute).withSecondOfMinute(0)
				.withMillisOfSecond(0);

		return (next.isBeforeNow()) ? next.plusHours(24) : next;
	}
}