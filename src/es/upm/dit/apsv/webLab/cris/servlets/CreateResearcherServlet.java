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
@WebServlet("/CreateResearcherServlet")
public class CreateResearcherServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if ("true".equals(req.getSession().getAttribute("userAdmin")))
			getServletContext().getRequestDispatcher("/AdminView.jsp").forward(req, resp);
		else {
			req.setAttribute("message", "You are not allowed to view this page");
			getServletContext().getRequestDispatcher("/LoginView.jsp").forward(req, resp);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String uid = req.getParameter("uid");
		String name = req.getParameter("name");
		String lastName = req.getParameter("last_name");
		
		ResearcherDAO rdao = ResearcherDAOImplementation.getInstance();
		if (rdao.read(uid) != null && "true".equals(req.getSession().getAttribute("userAdmin"))) { 
			req.setAttribute("message", "ID already exists");
			getServletContext().getRequestDispatcher("/AdminView.jsp").forward(req, resp);
			
		} else if ("true".equals(req.getSession().getAttribute("userAdmin"))) {
			
			Researcher researcher = new Researcher();
			researcher.setId(uid);
			researcher.setName(name);
			researcher.setLastName(lastName);
			rdao.create(researcher);
			resp.sendRedirect(req.getContextPath() + "/ResearcherServlet" + "?id=" + researcher.getId());
			
		} else {
			req.setAttribute("message", "You are not allowed to view this page");
			getServletContext().getRequestDispatcher("/LoginView.jsp").forward(req, resp);
		}
	}
	
}
