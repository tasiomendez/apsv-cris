package es.upm.dit.apsv.webLab.cris.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import es.upm.dit.apsv.webLab.cris.dao.PublicationDAO;
import es.upm.dit.apsv.webLab.cris.dao.PublicationDAOImplementation;
import es.upm.dit.apsv.webLab.cris.dao.ResearcherDAO;
import es.upm.dit.apsv.webLab.cris.dao.ResearcherDAOImplementation;
import es.upm.dit.apsv.webLab.cris.model.Publication;
import es.upm.dit.apsv.webLab.cris.model.Researcher;

@SuppressWarnings("serial")
@MultipartConfig
@WebServlet("/PopulatePublicationsServlet")
public class PopulatePublicationsServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ResearcherDAO rdao = ResearcherDAOImplementation.getInstance();
		PublicationDAO pdao = PublicationDAOImplementation.getInstance();
		
		if ("true".equals(req.getSession().getAttribute("userAdmin"))) {
			Part filePart = req.getPart("file");
			InputStream fileContent = filePart.getInputStream();
			BufferedReader bReader = new BufferedReader(
					new InputStreamReader(fileContent));
			String line = bReader.readLine();
	
			while (null != (line = bReader.readLine())) {
				String[] lSplit = line.split(",");
				if (pdao.read(lSplit[0]) != null || rdao.read(lSplit[5]) == null) {
					continue;
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
					
					Researcher r = rdao.read(lSplit[5]);
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
			}
		}
		resp.sendRedirect(req.getContextPath() + "/AdminServlet");
	}
	
}
