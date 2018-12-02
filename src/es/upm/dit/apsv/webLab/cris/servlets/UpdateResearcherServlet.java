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
@WebServlet("/UpdateResearcherServlet")
public class UpdateResearcherServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ResearcherDAO rdao = ResearcherDAOImplementation.getInstance();
		String id = req.getParameter("id");
		String name = req.getParameter("name");
		String lastName = req.getParameter("last_name");
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		String scopusUrl = req.getParameter("scopus_url");
		String eid = req.getParameter("eid");
		
		Researcher r = rdao.read(id);
		Researcher user = (Researcher) req.getSession().getAttribute("user");
		
		if (r != null && ( "true".equals(req.getSession().getAttribute("userAdmin"))
				|| r.getId().equals(user.getId()) )) {
			
			if (name != null) r.setName(name);
			if (lastName != null) r.setLastName(lastName);
			if (email != null) r.setEmail(email);
			if (password != null) r.setPassword(password);
			if (scopusUrl != null) r.setScopusUrl(scopusUrl);
			if (eid != null) r.setEid(eid);
			
			rdao.update(r);
		}
		
		resp.sendRedirect(req.getContextPath() + "/ResearcherServlet" + "?id=" + r.getId());

	}
	
}
