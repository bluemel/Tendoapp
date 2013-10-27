package de.bluemel.tendoapp.server;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.bluemel.tendoapp.client.TendoAppService;
import de.bluemel.tendoapp.shared.EMF;
import de.bluemel.tendoapp.shared.FieldVerifier;
import de.bluemel.tendoapp.shared.SeminarDTO;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TendoAppServiceImpl extends RemoteServiceServlet implements TendoAppService {

	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid.
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException("Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);

		return "Hello, " + input + "!<br><br>I am running " + serverInfo + ".<br><br>It looks like you are using:<br>"
				+ userAgent;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html
	 *            the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

	@Override
	public void addNewSeminar(final SeminarDTO seminarDTO) {
		EntityManager em = EMF.get().createEntityManager();
		try {
			em.getTransaction().begin();
			SeminarPO seminar = new SeminarPO(seminarDTO);
			seminar.setKey(UUID.randomUUID().toString());
			em.persist(seminar);
			em.getTransaction().commit();
		} catch (RuntimeException e) {
			em.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public void modifySeminar(final SeminarDTO newSeminarDTO) {
		EntityManager em = EMF.get().createEntityManager();
		try {
			em.getTransaction().begin();
			final SeminarPO seminar = em.find(SeminarPO.class, newSeminarDTO.getKey());
			if (seminar == null) {
				throw new RuntimeException("Seminar to modify \"" + newSeminarDTO.getKey().toString() + "\" not found");
			}
			seminar.setFirstDay(newSeminarDTO.getFirstDay());
			seminar.setLastDay(newSeminarDTO.getLastDay());
			seminar.setTitle(newSeminarDTO.getTitle());
			seminar.setInstructor(newSeminarDTO.getInstructor());
			seminar.setOrganizer(newSeminarDTO.getOrganizer());
			seminar.setLocation(newSeminarDTO.getLocation());
			seminar.setAnnouncement(newSeminarDTO.getAnnouncement());
			em.persist(seminar);
			em.getTransaction().commit();
		} catch (RuntimeException e) {
			em.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public void removeSeminar(SeminarDTO seminarDTO) {
		EntityManager em = EMF.get().createEntityManager();
		try {
			em.getTransaction().begin();
			final SeminarPO seminar = em.find(SeminarPO.class, seminarDTO.getKey());
			if (seminar == null) {
				throw new RuntimeException("Seminar to remove \"" + seminarDTO.getKey().toString() + "\" not found");
			}
			em.remove(seminar);
			em.getTransaction().commit();
		} catch (RuntimeException e) {
			em.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SeminarDTO> readAllSeminars() {
		EntityManager em = EMF.get().createEntityManager();
		final List<SeminarDTO> resultSet = new ArrayList<SeminarDTO>();
		for (final SeminarPO s : (List<SeminarPO>) em.createQuery("SELECT s FROM SeminarPO s ORDER BY firstDay ASC")
				.getResultList()) {
			resultSet.add(new SeminarDTO(s));
		}
		return resultSet;
	}
}
