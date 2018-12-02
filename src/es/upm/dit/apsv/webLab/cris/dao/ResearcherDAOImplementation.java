package es.upm.dit.apsv.webLab.cris.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Session;

import es.upm.dit.apsv.webLab.cris.model.Researcher;

public class ResearcherDAOImplementation implements ResearcherDAO {
	
	private static ResearcherDAOImplementation instance = null;
	private ResearcherDAOImplementation() {};
	public static ResearcherDAOImplementation getInstance() {
		if(instance == null)
			instance = new ResearcherDAOImplementation();
		return instance;
	}

	@Override
	public Researcher create(Researcher researcher) {
		Session session = SessionFactoryService.get().openSession();
		try {
			session.beginTransaction();
			session.save(researcher);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return researcher;
	}

	@Override
	public Researcher read(String researcherId) {
		Researcher researcher = null;
		Session session = SessionFactoryService.get().openSession();
		try {
			session.beginTransaction();
			researcher = session.get(Researcher.class, researcherId);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return researcher;
	}

	@Override
	public Researcher update(Researcher researcher) {
		Session session = SessionFactoryService.get().openSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(researcher);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return researcher;
	}

	@Override
	public Researcher delete(Researcher researcher) {
		Session session = SessionFactoryService.get().openSession();
		try {
			session.beginTransaction();
			session.delete(researcher);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return researcher;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Researcher> readAll() {
		List<Researcher> researchers = new ArrayList<>();
		Session session = SessionFactoryService.get().openSession();
		try {
			session.beginTransaction();
			researchers.addAll(
					session.createQuery("from Researcher").list()
			);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return researchers;
	}

	@Override
	public Researcher readAsUser(String email, String password) {
		Researcher researcher = null;
		Session session = SessionFactoryService.get().openSession();
		try {
			session.beginTransaction();
			researcher = (Researcher) session.createQuery("select r from Researcher r where r.email = :email and r.password = :password")
					.setParameter("email", email)
					.setParameter("password", password)
					.getSingleResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return researcher;
	}

	@Override
	public List<Researcher> parseResearchers(Collection<String> ids) {
		List<Researcher> researchers = new ArrayList<>();
		Session session = SessionFactoryService.get().openSession();
		try {
			session.beginTransaction();
			for (String id : ids) {
				Researcher r = session.get(Researcher.class, id);
				if (r != null) researchers.add(r);
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return researchers;
	}

}
