package de.bluemel.tendoapp.server;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.bluemel.tendoapp.client.TendoAppService;
import de.bluemel.tendoapp.shared.SeminarDTO;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TendoAppServiceImpl extends RemoteServiceServlet implements TendoAppService {

	private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("transactions-optional");

	@Override
	public void addNewSeminar(final SeminarDTO seminarDTO) {
		final EntityManager em = /* EMF.get() */emf.createEntityManager();
		try {
			em.getTransaction().begin();
			SeminarPO seminar = new SeminarPO(seminarDTO);
			if (seminarDTO.getKey() == null || seminarDTO.getKey().trim().length() == 0) {
				seminar.setKey(UUID.randomUUID().toString());
			}
			em.persist(seminar);
			em.getTransaction().commit();
		} catch (RuntimeException e) {
			em.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public void modifySeminar(final SeminarDTO newSeminarDTO) {
		final EntityManager em = /* EMF.get() */emf.createEntityManager();
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
		final EntityManager em = /* EMF.get() */emf.createEntityManager();
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
	public List<SeminarDTO> findAllSeminars() {
		final EntityManager em = /* EMF.get() */emf.createEntityManager();
		final List<SeminarDTO> resultSet = new ArrayList<SeminarDTO>();
		for (final SeminarPO s : (List<SeminarPO>) em.createQuery("SELECT s FROM SeminarPO s ORDER BY firstDay ASC")
				.getResultList()) {
			resultSet.add(new SeminarDTO(s));
		}
		return resultSet;
	}

	@Override
	public SeminarDTO findSeminarById(final String id) {
		SeminarDTO foundSeminar = null;
		final EntityManager em = /* EMF.get() */emf.createEntityManager();
		final SeminarPO seminar = em.find(SeminarPO.class, id);
		if (seminar != null) {
			foundSeminar = new SeminarDTO(seminar);
		}
		return foundSeminar;
	}


	@Override
	public String getServiceInfo() {
		final String serverInfo = getServletContext().getServerInfo();
		final String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		// Escape data from the client to avoid cross-site script vulnerabilities.
		return "I am running " + serverInfo + ".<br><br>It looks like you are using:<br>" + escapeHtml(userAgent);
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html
	 *            the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(final String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}
}
