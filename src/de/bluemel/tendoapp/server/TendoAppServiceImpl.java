package de.bluemel.tendoapp.server;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.bluemel.tendoapp.client.TendoAppService;
import de.bluemel.tendoapp.shared.EMF;
import de.bluemel.tendoapp.shared.FieldVerifier;
import de.bluemel.tendoapp.shared.Seminar;

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
	public void addNewSeminar(final Seminar seminar) {
		EntityManager em = EMF.get().createEntityManager();
		try {
			em.getTransaction().begin();
			seminar.setKey(UUID.randomUUID().toString());
			em.persist(seminar);
			em.getTransaction().commit();
		} catch (RuntimeException e) {
			em.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public void modifySeminar(final Seminar newSeminarData) {
		EntityManager em = EMF.get().createEntityManager();
		try {
			em.getTransaction().begin();
			final Seminar seminar = em.find(Seminar.class, newSeminarData.getKey());
			if (seminar == null) {
				throw new RuntimeException("Seminar to modify \"" + newSeminarData.getKey().toString() + "\" not found");
			}
			seminar.setFirstDay(newSeminarData.getFirstDay());
			seminar.setLastDay(newSeminarData.getLastDay());
			seminar.setTitle(newSeminarData.getTitle());
			seminar.setInstructor(newSeminarData.getInstructor());
			seminar.setOrganizer(newSeminarData.getOrganizer());
			seminar.setLocation(newSeminarData.getLocation());
			seminar.setAnnouncement(newSeminarData.getAnnouncement());
			em.persist(seminar);
			em.getTransaction().commit();
		} catch (RuntimeException e) {
			em.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public void removeSeminar(Seminar seminarToRemove) {
		EntityManager em = EMF.get().createEntityManager();
		try {
			em.getTransaction().begin();
			final Seminar seminar = em.find(Seminar.class, seminarToRemove.getKey());
			if (seminar == null) {
				throw new RuntimeException("Seminar to remove \"" + seminarToRemove.getKey().toString()
						+ "\" not found");
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
	public List<Seminar> readAllSeminars() {
		EntityManager em = EMF.get().createEntityManager();
		final List<Seminar> resultSet = new ArrayList<Seminar>();
		for (final Seminar s : (List<Seminar>) em.createQuery("SELECT s FROM Seminar s ORDER BY firstDay ASC")
				.getResultList()) {
			resultSet.add(new Seminar(s));
		}
		return resultSet;
	}
}
