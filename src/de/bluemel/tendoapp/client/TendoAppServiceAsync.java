package de.bluemel.tendoapp.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.bluemel.tendoapp.shared.Seminar;

/**
 * The async counterpart of <code>TendoAppService</code>.
 */
public interface TendoAppServiceAsync {

	void greetServer(String input, AsyncCallback<String> callback) throws IllegalArgumentException;

	void readAllSeminars(AsyncCallback<List<Seminar>> callback);

	void addNewSeminar(Seminar seminar, AsyncCallback<Void> callback);

	void modifySeminar(Seminar seminar, AsyncCallback<Void> callback);

	void removeSeminar(Seminar seminar, AsyncCallback<Void> callback);
}
