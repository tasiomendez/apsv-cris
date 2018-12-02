package es.upm.dit.apsv.webLab.cris.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import es.upm.dit.apsv.webLab.cris.dao.PublicationDAO;
import es.upm.dit.apsv.webLab.cris.dao.PublicationDAOImplementation;
import es.upm.dit.apsv.webLab.cris.model.Publication;

@SuppressWarnings("serial")
@WebServlet("/UpdateCitationsAPIServlet")
public class UpdateCitationsAPIServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject elsevier = null;
		try {
			// Get credentials from the Resources folder
			ClassLoader classloader = getClass().getClassLoader();
			InputStream json = classloader.getResourceAsStream("elsevier.json");
			elsevier = (JSONObject) new JSONParser().parse(new InputStreamReader(json));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String id = req.getParameter("id");
		String url = "https://api.elsevier.com/content/abstract/scopus_id/" + id + 
				"?apiKey=" + elsevier.get("key");
		
		JSONObject response = this.getAPI(url);
		if (response == null) {
			resp.sendRedirect(req.getContextPath() + "/PublicationServlet" + "?id=" + id);
			return;
		}

		String cites = (String) ((JSONObject)((JSONObject) response.get("abstracts-retrieval-response"))
				.get("coredata"))
				.get("citedby-count");
		PublicationDAO pdao = PublicationDAOImplementation.getInstance();
		Publication p = pdao.read(id);
		p.setCiteCount(Integer.parseInt(cites));
		pdao.update(p);
		resp.sendRedirect(req.getContextPath() + "/PublicationServlet" + "?id=" + id);
	}

	private JSONObject getAPI(String targetUrl) {
	    JSONObject object = null;
	    try {
	        URL url = new URL(targetUrl);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoOutput(true);
	        connection.setInstanceFollowRedirects(false);
	        connection.setRequestMethod("GET");
	        connection.setRequestProperty("Content-Type", "application/json");
	        connection.setRequestProperty("Accept", "application/json");
	        int responseCode = connection.getResponseCode();
	        if(responseCode>=200 && responseCode<300) {
	            InputStream is = connection.getInputStream();
	            InputStreamReader isr = new InputStreamReader(is);
	            object =(JSONObject) new JSONParser().parse(isr);
	            is.close();
	        } else {
	            System.err.println("Request returned code "+ responseCode);
	            System.err.println(connection.getResponseMessage());
	        }
	        connection.getResponseCode();
	        connection.disconnect();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return object;
	}
	
}
