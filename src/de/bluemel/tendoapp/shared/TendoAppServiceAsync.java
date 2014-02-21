package de.bluemel.tendoapp.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>TendoAppService</code>.
 */
public interface TendoAppServiceAsync {

	void findAllSeminars(AsyncCallback<List<SeminarDTO>> callback);

	void findSeminarById(String id, AsyncCallback<SeminarDTO> callback);

	void addNewSeminar(SeminarDTO seminar, AsyncCallback<Void> callback);

	void modifySeminar(SeminarDTO seminar, AsyncCallback<Void> callback);

	void removeSeminar(SeminarDTO seminar, AsyncCallback<Void> callback);

	void getServiceInfo(AsyncCallback<String> callback);

	void removeOutdatedSeminars(AsyncCallback<Integer> callback);
}
