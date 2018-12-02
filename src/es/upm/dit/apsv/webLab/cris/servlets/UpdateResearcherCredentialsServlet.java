package es.upm.dit.apsv.webLab.cris.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.upm.dit.apsv.webLab.cris.dao.ResearcherDAO;
import es.upm.dit.apsv.webLab.cris.dao.ResearcherDAOImplementation;
import es.upm.dit.apsv.webLab.cris.model.Researcher;

@SuppressWarnings("serial")
@WebServlet("/UpdateResearcherCredentialsServlet")
public class UpdateResearcherCredentialsServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ResearcherDAO rdao = ResearcherDAOImplementation.getInstance();
		String id = req.getParameter("id");
		String email = req.getParameter("email");
		String password = req.getParameter("password");

		Researcher r = rdao.read(id);

		if (r != null && "true".equals(req.getSession().getAttribute("userAdmin"))) {
			r.setEmail(email);
			r.setPassword(password);
			
			rdao.update(r);
		}

		resp.sendRedirect(req.getContextPath() + "/ResearcherServlet" + "?id=" + r.getId());
	}
}
