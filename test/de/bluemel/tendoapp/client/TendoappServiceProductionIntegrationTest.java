package de.bluemel.tendoapp.client;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gdevelop.gwt.syncrpc.SyncProxy;

import de.bluemel.tendoapp.shared.SeminarDTO;

public class TendoappServiceProductionIntegrationTest {

	private static TendoAppService rpcService;

	@BeforeClass
	public static void initService() {
		rpcService = (TendoAppService) SyncProxy.newProxyInstance(TendoAppService.class,
				"http://tendo-app.appspot.com/tendoapp/", "service");
	}

	@Test
	public void testReadAllSeminars() {
		List<SeminarDTO> resultList = rpcService.readAllSeminars();
		for (SeminarDTO seminar : resultList) {
			System.out.println(seminar);
		}
	}

	@Test
	public void testGreatServer() {
		TendoAppService rpcService = (TendoAppService) SyncProxy.newProxyInstance(TendoAppService.class,
				"http://tendo-app.appspot.com/tendoapp/", "service");
		String hello = rpcService.greetServer("Martin");
		Assert.assertEquals(
				"Hello, Martin!<br><br>I am running Google App Engine/Google App Engine/1.8.6.<br><br>It looks like you are using:<br>Java/1.7.0_09",
				hello);
	}
}
