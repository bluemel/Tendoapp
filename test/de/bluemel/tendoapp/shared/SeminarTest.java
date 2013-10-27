package de.bluemel.tendoapp.shared;

import org.junit.Assert;
import org.junit.Test;

import de.bluemel.tendoapp.server.SeminarPO;

public class SeminarTest {

	@Test
	public void testDTO() {
		Seminar seminar = new SeminarDTO(null, TimeLogic.parse("1.4.2014"), TimeLogic.parse("2.4.2014"), "a", "b", "c",
				"d", "none");
		Assert.assertEquals("de.bluemel.tendoapp.shared.SeminarDTO[key = <no key>, "
				+ "firstDay = \"01.04.2014\", lastDay = \"02.04.2014\", "
				+ "title = \"a\", instructor = \"b\", organizer = \"c\", "
				+ "location = \"d\", announcement = \"none\"]", seminar.toString());
		seminar = new SeminarDTO("4711", TimeLogic.parse("1.4.2014"), TimeLogic.parse("2.4.2014"), "a", "b", "c", "d",
				"none");
		Assert.assertEquals("de.bluemel.tendoapp.shared.SeminarDTO[key = \"4711\", "
				+ "firstDay = \"01.04.2014\", lastDay = \"02.04.2014\", "
				+ "title = \"a\", instructor = \"b\", organizer = \"c\", "
				+ "location = \"d\", announcement = \"none\"]", seminar.toString());
	}

	@Test
	public void testPO() {
		Seminar seminar = new SeminarPO(null, TimeLogic.parse("1.4.2014"), TimeLogic.parse("2.4.2014"), "a", "b", "c",
				"d", "none");
		Assert.assertEquals("de.bluemel.tendoapp.server.SeminarPO[key = <no key>, "
				+ "firstDay = \"01.04.2014\", lastDay = \"02.04.2014\", "
				+ "title = \"a\", instructor = \"b\", organizer = \"c\", "
				+ "location = \"d\", announcement = \"none\"]", seminar.toString());
		seminar = new SeminarPO("4712", TimeLogic.parse("1.4.2014"), TimeLogic.parse("2.4.2014"), "a", "b", "c", "d",
				"none");
		Assert.assertEquals("de.bluemel.tendoapp.server.SeminarPO[key = \"4712\", "
				+ "firstDay = \"01.04.2014\", lastDay = \"02.04.2014\", "
				+ "title = \"a\", instructor = \"b\", organizer = \"c\", "
				+ "location = \"d\", announcement = \"none\"]", seminar.toString());
	}
}
