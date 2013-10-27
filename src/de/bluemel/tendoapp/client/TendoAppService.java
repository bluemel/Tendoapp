package de.bluemel.tendoapp.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.bluemel.tendoapp.shared.SeminarDTO;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("service")
public interface TendoAppService extends RemoteService {

	String greetServer(String name) throws IllegalArgumentException;

	List<SeminarDTO> readAllSeminars();

	void addNewSeminar(SeminarDTO seminar);

	void modifySeminar(SeminarDTO seminar);

	void removeSeminar(SeminarDTO seminar);
}
