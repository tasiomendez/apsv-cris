package es.upm.dit.apsv.webLab.cris.servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import es.upm.dit.apsv.webLab.cris.dao.PublicationDAO;
import es.upm.dit.apsv.webLab.cris.dao.PublicationDAOImplementation;
import es.upm.dit.apsv.webLab.cris.dao.ResearcherDAO;
import es.upm.dit.apsv.webLab.cris.dao.ResearcherDAOImplementation;
import es.upm.dit.apsv.webLab.cris.model.Publication;
import es.upm.dit.apsv.webLab.cris.model.Researcher;

@SuppressWarnings("serial")
@MultipartConfig
@WebServlet("/GenerateCVServlet")
public class GenerateCVServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PublicationDAO pdao = PublicationDAOImplementation.getInstance();
		ResearcherDAO rdao = ResearcherDAOImplementation.getInstance();
		Researcher researcher = rdao.read(req.getParameter("id"));
		
		ServletOutputStream sout = resp.getOutputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		// Create pdf object
		PdfDocument pdf = new PdfDocument(new PdfWriter(baos));
		Document document = new Document(pdf);
		// Add paragraphs
		Paragraph p = new Paragraph("Curriculum Vitae").setFontSize(20);
		document.add(p);

		// Add list with attributes
		List list = new List();
		ListItem item = new ListItem("Full name: " + researcher.getName()+ " " + researcher.getLastName());
		list.add(item);
		item = new ListItem("Email: " + researcher.getEmail());
		list.add(item);
		document.add(list);

		// Add a table with publications
		// The table should be initialized with an array of floats indicating the relative width of each column
		Table table = new Table(new float[]{7, 1});
		table.addHeaderCell("Publication title");
		table.addHeaderCell("Citations");
		for(Publication pub : pdao.parsePublications(researcher.getPublications())){
			table.addCell(pub.getTitle());
			table.addCell(Integer.toString(pub.getCiteCount()));
		}
		document.add(table);

		// Close the document
		document.close();
		pdf.close();

		// Write the file
		resp.setContentType("application/pdf");
		resp.setContentLength(baos.size());
		baos.writeTo(sout);
	}

}
