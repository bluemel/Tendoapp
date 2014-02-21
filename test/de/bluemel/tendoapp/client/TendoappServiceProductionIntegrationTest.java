package de.bluemel.tendoapp.client;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gdevelop.gwt.syncrpc.SyncProxy;

import de.bluemel.tendoapp.shared.SeminarDTO;
import de.bluemel.tendoapp.shared.TendoAppService;
import de.bluemel.tendoapp.shared.TimeLogic;

/**
 * It was not so easy as it looks like now, to use the gwt-syncproxy library.
 * Beside connection (proxy) problems in my various environments I had to solve 3 issues:
 * 
 * 1) SyncProxy.newProxyInstance must be fed with correct moduleBaseURL and remoteServiceRelativePath
 * (really dodgy)
 * .. - moduleBaseURL: <app url>/<module name>
 * ..... the URL where you usually start your GWT app e. g. http://tendo-app.appspot.com/
 * ..... - module name: module from <root package>.<app name>.gwt.xml or first part from war/WEB-INF/web.xml, servlet-mapping / url-pattern
 * ..... - remoteServiceRelativePath: <servlet path> last part from war/WEB-INF/web.xml, servlet-mapping / url-pattern
 * 
 * 2) war/tendoapp/148D67F4E5F2428A31168C8C2913E726.gwt.rpc has to be in the class path
 * 
 * 3) De-serialization of byte code enhanced class sucks => separate SeminarPO from SeminarDTO
 * 
 * @author Bluemel1.Martin
 */
public class TendoappServiceProductionIntegrationTest {

	private static TendoAppService rpcService;

	@BeforeClass
	public static void initService() {
		rpcService = (TendoAppService) SyncProxy.newProxyInstance(TendoAppService.class,
				"http://tendo-app.appspot.com/tendoapp/", "service");
	}

	@Test
	public void testCreateReadModifyRemoveSeminar() {
		Assert.assertNull(rpcService.findSeminarById("TEST-SEMI-NAR0-0001"));

		SeminarDTO seminar = new SeminarDTO("TEST-SEMI-NAR0-0001", TimeLogic.parse("1.1.2019"),
				TimeLogic.parse("2.1.2019"), "Test Seminar 0001", "John Doe", "FC Bayern München", "Munich", "nope");
		rpcService.addNewSeminar(seminar);
		SeminarDTO createdSeminar = rpcService.findSeminarById("TEST-SEMI-NAR0-0001");
		Assert.assertEquals("Test Seminar 0001", createdSeminar.getTitle());

		createdSeminar.setTitle("Test Seminar 0001 changed");
		rpcService.modifySeminar(createdSeminar);
		SeminarDTO modifiedSeminar = rpcService.findSeminarById("TEST-SEMI-NAR0-0001");
		Assert.assertEquals("Test Seminar 0001 changed", modifiedSeminar.getTitle());

		createdSeminar.setLocation("sektion");
		createdSeminar.setAnnouncement("tendo");
		rpcService.removeSeminar(createdSeminar);
		Assert.assertNull(rpcService.findSeminarById("TEST-SEMI-NAR0-0001"));
	}

	@Test
	public void testFindAllSeminars() {
		List<SeminarDTO> resultList = rpcService.findAllSeminars();
		for (SeminarDTO seminar : resultList) {
			System.out.println(seminar);
		}
	}

	@Test
	public void testGreatServer() {
		TendoAppService rpcService = (TendoAppService) SyncProxy.newProxyInstance(TendoAppService.class,
				"http://tendo-app.appspot.com/tendoapp/", "service");
		String hello = rpcService.getServiceInfo();
		Assert.assertEquals("I am running Google App Engine/Google App Engine/1.8.9.<br><br>"
				+ "It looks like you are using:<br>Java/1.7.0_45", hello);
	}
}
