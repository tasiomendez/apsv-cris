package es.upm.dit.apsv.webLab.cris.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import es.upm.dit.apsv.webLab.cris.dao.ResearcherDAOImplementation;
import es.upm.dit.apsv.webLab.cris.model.Researcher;

@SuppressWarnings("serial")
@WebServlet({"/LoginServlet","/"})
public class LoginServlet extends HttpServlet {
	final String ADMIN = "root";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (null != req.getUserPrincipal()) {
			UserService userService = UserServiceFactory.getUserService();
			Researcher researcher = ResearcherDAOImplementation.getInstance()
					.readAsUser(userService.getCurrentUser().getEmail(), null);

			if (null != researcher) {
				req.getSession().setAttribute("userAdmin", "false");
				req.getSession().setAttribute("user", researcher);
				resp.sendRedirect(req.getContextPath() + "/ResearcherServlet" + "?id=" + researcher.getId());
			} else {
				Researcher visitor = new Researcher();
				visitor.setId("visitor");
				visitor.setName("Visitante");
				req.getSession().setAttribute("userAdmin", false);
				req.getSession().setAttribute("user", visitor);
			}
		} 
		getServletContext().getRequestDispatcher("/LoginView.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String email = req.getParameter("email");
		String password = req.getParameter("password");

		if (ADMIN.equals(email) && ADMIN.equals(password)) {
			Researcher root = new Researcher();
			root.setId("root");
			req.getSession().setAttribute("userAdmin", "true");
			req.getSession().setAttribute("user", root);
			resp.sendRedirect(req.getContextPath() + "/AdminServlet");

		} else {
			req.setAttribute("message", "Invalid user or password");
			getServletContext().getRequestDispatcher("/LoginView.jsp").forward(req, resp);
		}
	}

}