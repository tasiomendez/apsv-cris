package es.upm.dit.apsv.webLab.cris.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
@WebServlet("/CreatePublicationServlet")
public class CreatePublicationServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ResearcherDAO rdao = ResearcherDAOImplementation.getInstance();
		PublicationDAO pdao = PublicationDAOImplementation.getInstance();
		String id = req.getParameter("id");
		String title = req.getParameter("title");
		String eid = req.getParameter("eid");
		String publicationName = req.getParameter("publication_name");
		String publicationDate = req.getParameter("publication_date");
		String firstAuthor = req.getParameter("first_author");
		String[] authors = (!req.getParameter("authors").isEmpty()) ? 
				req.getParameter("authors").split(";") : null;
		
		if (pdao.read(id) != null) {
			req.setAttribute("message", "ID already exists");
			getServletContext().getRequestDispatcher("/LoginView.jsp").forward(req, resp);
			
		} else {
			Publication p = new Publication();
			p.setId(id);
			p.setTitle(title);
			p.setEid(eid);
			p.setPublicationName(publicationName);
			p.setPublicationDate(publicationDate);
			p.setFirstAuthor(firstAuthor);
			if (authors != null)
				p.setAuthors(new ArrayList<String>(Arrays.asList(authors)));
			if (!p.getAuthors().contains(firstAuthor))
				p.getAuthors().add(firstAuthor);
			pdao.create(p);
			
			Researcher r = rdao.read(firstAuthor);
			r.getPublications().add(id);
			rdao.update(r);

			if (authors != null) {
				for (String author : authors) {
					Researcher a = rdao.read(author);
					if (a != null && !a.getPublications().contains(id)) {
						a.getPublications().add(id);
						rdao.update(a);
					}
				}
			}
		}
		
		resp.sendRedirect(req.getContextPath() + "/PublicationServlet" + "?id=" + id);
	}

}
