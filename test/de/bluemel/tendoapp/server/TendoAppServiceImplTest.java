package de.bluemel.tendoapp.server;

import org.junit.Test;

import de.bluemel.tendoapp.shared.SeminarDTO;
import de.bluemel.tendoapp.shared.TimeLogic;

public class TendoAppServiceImplTest {

	// private static TendoAppServiceImpl service = new TendoAppServiceImpl();

	@Test
	public void testAddNewSeminar() {
		new SeminarDTO(null, TimeLogic.parse("1.4.2014"), TimeLogic.parse("2.4.2014"), "a", "b", "c", "d", "none");
		//		service.addNewSeminar(seminar);
		//		List<SeminarDTO> seminars = service.readAllSeminars();
		//		Assert.assertEquals(1, seminars.size());
	}
}
