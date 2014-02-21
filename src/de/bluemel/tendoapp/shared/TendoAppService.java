package de.bluemel.tendoapp.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("service")
public interface TendoAppService extends RemoteService {

	String getServiceInfo();

	List<SeminarDTO> findAllSeminars();

	SeminarDTO findSeminarById(String id);

	void addNewSeminar(SeminarDTO seminar);

	void modifySeminar(SeminarDTO seminar);

	void removeSeminar(SeminarDTO seminar);

	Integer removeOutdatedSeminars();
}
