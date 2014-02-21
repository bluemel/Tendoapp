package de.bluemel.tendoapp.server;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.bluemel.tendoapp.shared.SeminarDTO;
import de.bluemel.tendoapp.shared.TendoAppService;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TendoAppServiceImpl extends RemoteServiceServlet implements TendoAppService {

	private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("transactions-optional");

	@Override
	public void addNewSeminar(final SeminarDTO seminarDTO) {
		final EntityManager em = emf.createEntityManager();
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
		} finally {
			em.close();
		}
	}

	@Override
	public void modifySeminar(final SeminarDTO newSeminarDTO) {
		final EntityManager em = emf.createEntityManager();
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
		} finally {
			em.close();
		}
	}

	@Override
	public void removeSeminar(SeminarDTO seminarDTO) {
		final EntityManager em = emf.createEntityManager();
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
		} finally {
			em.close();
		}
	}

	private static final long MAX_AGE_MILLIS = 3 * 24 * 60 * 60 * 1000;

	@Override
	public Integer removeOutdatedSeminars() {
		final EntityManager em = emf.createEntityManager();
		final long currentTimeMills = System.currentTimeMillis();
		int count = 0;
		try {
//			em.getTransaction().begin();
			for (SeminarPO seminar : findAllSeminars(em, DateOrdering.ascending)) {
				if (currentTimeMills - seminar.getLastDay().getTime() > MAX_AGE_MILLIS) {
					em.getTransaction().begin();
					em.remove(seminar);
					em.getTransaction().commit();
					count++;
				} else {
					// since we have ascending order we can stop
					break;
				}
			}
//			em.getTransaction().commit();
			return new Integer(count);
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}
	}

	@Override
	public List<SeminarDTO> findAllSeminars() {
		final EntityManager em = emf.createEntityManager();
		final List<SeminarDTO> resultSet = new ArrayList<SeminarDTO>();
		for (SeminarPO seminar : findAllSeminars(em, DateOrdering.ascending)) {
			resultSet.add(new SeminarDTO(seminar));
		}
		return resultSet;
	}

	@SuppressWarnings("unchecked")
	private List<SeminarPO> findAllSeminars(final EntityManager em, DateOrdering dateOrdering) {
		String query = "SELECT s FROM SeminarPO s";
		switch (dateOrdering) {
		case ascending:
			query += " ORDER BY firstDay ASC";
			break;
		case descending:
			query += " ORDER BY lastDay DESC";
			break;
		default:
			break;
		}
		return (List<SeminarPO>) em.createQuery(query).getResultList();
	}

	@Override
	public SeminarDTO findSeminarById(final String id) {
		SeminarDTO foundSeminar = null;
		final EntityManager em = emf.createEntityManager();
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
	 * Escape an html string. Escaping data received from the client helps to prevent cross-site script vulnerabilities.
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
