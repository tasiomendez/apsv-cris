package es.upm.dit.apsv.webLab.cris.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.upm.dit.apsv.webLab.cris.dao.PublicationDAO;
import es.upm.dit.apsv.webLab.cris.dao.PublicationDAOImplementation;
import es.upm.dit.apsv.webLab.cris.dao.ResearcherDAOImplementation;
import es.upm.dit.apsv.webLab.cris.model.Researcher;

@SuppressWarnings("serial")
@WebServlet("/ResearcherServlet")
public class ResearcherServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");
		Researcher researcher = ResearcherDAOImplementation.getInstance().read(id);
		req.getSession().setAttribute("researcher", researcher);
		
		PublicationDAO pdao = PublicationDAOImplementation.getInstance();
		List<String> ids = researcher.getPublications();
		req.getSession().setAttribute("publications", pdao.parsePublications(ids));
		getServletContext().getRequestDispatcher("/ResearcherView.jsp").forward(req, resp);
	}

}
