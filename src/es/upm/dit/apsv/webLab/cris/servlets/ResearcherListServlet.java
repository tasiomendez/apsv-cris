package es.upm.dit.apsv.webLab.cris.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.upm.dit.apsv.webLab.cris.dao.ResearcherDAOImplementation;
import es.upm.dit.apsv.webLab.cris.model.Researcher;

@SuppressWarnings("serial")
@WebServlet("/ResearcherListServlet")
public class ResearcherListServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Researcher> researchers = ResearcherDAOImplementation.getInstance().readAll();
		req.setAttribute("researchersList", researchers);
		getServletContext().getRequestDispatcher("/ResearcherListView.jsp").forward(req, resp);
	}
	
}
