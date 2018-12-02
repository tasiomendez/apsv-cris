package es.upm.dit.apsv.webLab.cris.servlets;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.upm.dit.apsv.webLab.cris.dao.PublicationDAO;
import es.upm.dit.apsv.webLab.cris.dao.PublicationDAOImplementation;
import es.upm.dit.apsv.webLab.cris.dao.ResearcherDAO;
import es.upm.dit.apsv.webLab.cris.dao.ResearcherDAOImplementation;
import es.upm.dit.apsv.webLab.cris.model.Publication;
import es.upm.dit.apsv.webLab.cris.model.Researcher;

@SuppressWarnings("serial")
@WebServlet("/_ah/mail/*")
public class CreatePublicationFromEmailServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PublicationDAO pdao = PublicationDAOImplementation.getInstance();
		ResearcherDAO rdao = ResearcherDAOImplementation.getInstance();
		
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		MimeMessage message;
		String from, subject;
		
		try {
			message = new MimeMessage(session, req.getInputStream());
			from = new InternetAddress( message.getFrom()[0].toString()).getAddress();
			subject = message.getSubject();

			String[] lSplit = subject.split(",");
			if (pdao.read(lSplit[0]) != null || rdao.readAsUser(from, null) == null) {
				return;
			} else {
				Publication p = new Publication();
				p.setId(lSplit[0]);
				p.setTitle(lSplit[1]);
				p.setEid(lSplit[2]);
				p.setPublicationName(lSplit[3]);
				p.setPublicationDate(lSplit[4]);
				p.setFirstAuthor(lSplit[5]);
				p.setAuthors(Arrays.asList(lSplit[6].split(";")));
				pdao.create(p);

				Researcher r = rdao.readAsUser(from, null);
				r.getPublications().add(p.getId());
				rdao.update(r);

				for (String author : lSplit[6].split(";")) {
					Researcher a = rdao.read(author);
					if (a != null && !a.getPublications().contains(p.getId())) {
						a.getPublications().add(p.getId());
						rdao.update(a);
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
