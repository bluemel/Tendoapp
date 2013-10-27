package de.bluemel.tendoapp.client;

import java.util.ArrayList;
import java.util.List;

import de.bluemel.tendoapp.shared.Seminar;
import de.bluemel.tendoapp.shared.TimeLogic;

public class OverlappingSeminarsException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;

	public static String NEWLINE = System.getProperty("line.separator", "\n");

	private List<Seminar> overlappingSeminars;

	public OverlappingSeminarsException(final List<Seminar> overlappingSeminars) {
		this.overlappingSeminars = overlappingSeminars;
	}

	public List<Seminar> getOverlappingSeminars() {
		return overlappingSeminars;
	}

	public String getMessage() {
		final StringBuilder sb = new StringBuilder();
		int i = 0;
		for (final String messageLine : getMessageLines()) {
			if (i > 0) {
				sb.append(NEWLINE);
			}
			sb.append(messageLine);
			i++;
		}
		return sb.toString();
	}

	public List<String> getMessageLines() {
		final List<String> messageLines = new ArrayList<String>();
		if (this.overlappingSeminars.size() == 1) {
			messageLines.add("Bereits eingetragener Termin:");
		} else {
			messageLines.add("Bereits eingetragene Termine:");
		}
		for (final Seminar seminar : overlappingSeminars) {
			messageLines.add(TimeLogic.format(seminar.getFirstDay()) + " " + TimeLogic.format(seminar.getLastDay())
					+ ", " + seminar.getInstructor() + ", " + seminar.getLocation());
		}
		return messageLines;
	}
}
