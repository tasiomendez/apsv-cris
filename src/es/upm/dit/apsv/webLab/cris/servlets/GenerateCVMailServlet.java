package es.upm.dit.apsv.webLab.cris.servlets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletException;
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
@WebServlet("/GenerateCVMailServlet")
public class GenerateCVMailServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PublicationDAO pdao = PublicationDAOImplementation.getInstance();
		ResearcherDAO rdao = ResearcherDAOImplementation.getInstance();
		Researcher researcher = rdao.read(req.getParameter("id"));
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		//Create pdf object
		PdfDocument pdf = new PdfDocument(new PdfWriter(baos));
		Document document = new Document(pdf);
		//Add paragraphs
		Paragraph p = new Paragraph("Curriculum Vitae").setFontSize(20);
		document.add(p);

		//Add list with attributes
		List list = new List();
		ListItem item = new ListItem("Full name: " + researcher.getName()+ " " + researcher.getLastName());
		list.add(item);
		item = new ListItem("Email: " + researcher.getEmail());
		list.add(item);
		document.add(list);

		//Add a table with publications
		//The table should be initialized with an array of floats indicating the relative width of each column
		Table table = new Table(new float[]{7, 1});
		table.addHeaderCell("Publication title");
		table.addHeaderCell("Citations");
		for(Publication pub : pdao.parsePublications(researcher.getPublications())){
			table.addCell(pub.getTitle());
			table.addCell(Integer.toString(pub.getCiteCount()));
		}
		document.add(table);

		//Close the document
		document.close();
		pdf.close();
		
		// Initialize the mail session
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		
		// Create the message
		Message msg = new MimeMessage(session);
		
		// Emails can be sent from any address from the application domain @[gae_project_id].appspotmail.com
		try {
			msg.setFrom(new InternetAddress("cv@cris-223518.appspotmail.com", "CRIS System"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(researcher.getEmail(), 
					researcher.getName()));
			msg.setSubject("CV - CRIS System");
			msg.setText("You will find attached your CV made from the CRIS System");
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		// Attach the pdf
		Multipart mp = new MimeMultipart();
		MimeBodyPart attachment = new MimeBodyPart();
		
		// baos is the stream in which the pdf is written
		InputStream attachmentDataStream = new ByteArrayInputStream(baos.toByteArray());
		try {
			attachment.setFileName("cv.pdf");
			attachment.setContent(attachmentDataStream, "application/pdf");
			mp.addBodyPart(attachment);
			msg.setContent(mp);

			// Send the email
			Transport.send(msg);
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(req.getContextPath() + "/ResearcherServlet" + "?id=" + researcher.getId());
	}
	
}
