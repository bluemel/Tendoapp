package de.bluemel.tendoapp.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.bluemel.tendoapp.shared.SeminarDTO;

/**
 * The async counterpart of <code>TendoAppService</code>.
 */
public interface TendoAppServiceAsync {

	void greetServer(String input, AsyncCallback<String> callback) throws IllegalArgumentException;

	void readAllSeminars(AsyncCallback<List<SeminarDTO>> callback);

	void addNewSeminar(SeminarDTO seminar, AsyncCallback<Void> callback);

	void modifySeminar(SeminarDTO seminar, AsyncCallback<Void> callback);

	void removeSeminar(SeminarDTO seminar, AsyncCallback<Void> callback);
}
