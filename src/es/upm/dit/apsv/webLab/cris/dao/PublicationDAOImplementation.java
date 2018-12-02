package es.upm.dit.apsv.webLab.cris.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Session;

import es.upm.dit.apsv.webLab.cris.model.Publication;

public class PublicationDAOImplementation implements PublicationDAO {
	
	private static PublicationDAOImplementation instance = null;
	private PublicationDAOImplementation() {};
	public static PublicationDAOImplementation getInstance() {
		if(instance == null)
			instance = new PublicationDAOImplementation();
		return instance;
	}

	@Override
	public Publication create(Publication publication) {
		Session session = SessionFactoryService.get().openSession();
		try {
			session.beginTransaction();
			session.save(publication);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return publication;
	}

	@Override
	public Publication read(String publicationId) {
		Publication publication = null;
		Session session = SessionFactoryService.get().openSession();
		try {
			session.beginTransaction();
			publication = session.get(Publication.class, publicationId);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return publication;
	}

	@Override
	public Publication update(Publication publication) {
		Session session = SessionFactoryService.get().openSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(publication);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return publication;
	}

	@Override
	public Publication delete(Publication publication) {
		Session session = SessionFactoryService.get().openSession();
		try {
			session.beginTransaction();
			session.delete(publication);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return publication;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Publication> readAll() {
		List<Publication> publications = new ArrayList<>();
		Session session = SessionFactoryService.get().openSession();
		try {
			session.beginTransaction();
			publications.addAll(
					session.createQuery("from Researcher").list()
			);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return publications;
	}

	@Override
	public List<Publication> parsePublications(Collection<String> ids) {
		List<Publication> publications = new ArrayList<>();
		Session session = SessionFactoryService.get().openSession();
		try {
			session.beginTransaction();
			for (String id : ids) {
				Publication p = session.get(Publication.class, id);
				if (p != null) publications.add(p);
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return publications;
	}

}
