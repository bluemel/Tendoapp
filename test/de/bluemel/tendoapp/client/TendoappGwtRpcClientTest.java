package de.bluemel.tendoapp.client;

import org.junit.Assert;
import org.junit.Test;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.google.gwt.user.client.rpc.InvocationException;

public class TendoappGwtRpcClientTest {

	@Test(expected = InvocationException.class)
	public void testReadAllSeminars() {
		TendoAppService rpcService = (TendoAppService) SyncProxy.newProxyInstance(TendoAppService.class,
				"http://tendo-app.appspot.com/tendoapp/", "service");
		// readAllSeminars does not work out so far
		rpcService.readAllSeminars();
		//		for (Seminar seminar : rpcService.readAllSeminars()) {
		//			System.out.println(seminar.toString());
		//		}
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
