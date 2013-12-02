package de.bluemel.tendoapp.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.bluemel.tendoapp.shared.SeminarDTO;

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
}
