package net.slipcor.fsm.utilities;

public class FireworkLanguage {
	public static enum ErrorCode {
		ARGS_LENGTH("Not enough arguments!"),
		ARG2_NOT_NUMERIC("Second argument is not numeric!"),
		ARG1_NOT_NUMERIC("First argument is not numeric!"),
		ARG1_UNKNOWN("First argument unknown!"),
		INTERVAL_TOO_HIGH("Given interval number is too high!");
		
		private final String msg;
		private ErrorCode(String content) {
			msg = content;
		}
		
		@Override
		public String toString() {
			return msg;
		}
	}
	
	public static enum MSG {
		ALREADY_SET("This block already is set!"),
		NOW_SETTING("Now setting interval #%1%"),
		SET_TIME("Set time to hour %1%!"),
		SET_DURATION("Set duration to %1% intervals!"),
		SET_BIGINTERVAL("Set interval between shows to %1% seconds!"),
		SET_INTERVAL("Set interval to %1% ticks!"),
		SETTING_DONE("Spawn setting done!"),
		SET_LOCATION("Added block: %1%"),
		SET_FIREWORK("Added effect: %1%"),
		
		SHOW_STARTING("The firework show is starting!"),
		SHOW_OVER("The firework show is over!"),
		
		START_DONE("Show timer started!"),
		STOP_DONE("Show stopped!"),
		STOPFULL_DONE("Show and timer stopped!"),
		TEST_DONE("Testing show!"), ;
		
		private final String msg;
		private MSG(String content) {
			msg = content;
		}
		
		@Override
		public String toString() {
			return msg;
		}
		
		public String parse(String s) {
			return msg.replace("%1%", s);
		}
	}
}
