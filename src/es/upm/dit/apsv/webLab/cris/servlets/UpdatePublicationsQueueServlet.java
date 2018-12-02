package es.upm.dit.apsv.webLab.cris.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.cloud.pubsub.v1.stub.GrpcSubscriberStub;
import com.google.cloud.pubsub.v1.stub.SubscriberStub;
import com.google.cloud.pubsub.v1.stub.SubscriberStubSettings;
import com.google.pubsub.v1.AcknowledgeRequest;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PullRequest;
import com.google.pubsub.v1.PullResponse;
import com.google.pubsub.v1.ReceivedMessage;

import es.upm.dit.apsv.webLab.cris.dao.PublicationDAO;
import es.upm.dit.apsv.webLab.cris.dao.PublicationDAOImplementation;
import es.upm.dit.apsv.webLab.cris.dao.ResearcherDAO;
import es.upm.dit.apsv.webLab.cris.dao.ResearcherDAOImplementation;
import es.upm.dit.apsv.webLab.cris.model.Publication;
import es.upm.dit.apsv.webLab.cris.model.Researcher;

@SuppressWarnings("serial")
@WebServlet("/UpdatePublicationsQueueServlet")
public class UpdatePublicationsQueueServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PublicationDAO pdao = PublicationDAOImplementation.getInstance();
		ResearcherDAO rdao = ResearcherDAOImplementation.getInstance();
		Researcher researcher = rdao.read(req.getParameter("id"));

		JSONObject credentials = null;
		try {
			// Get credentials from the Resources folder
			ClassLoader classloader = getClass().getClassLoader();
			InputStream json = classloader.getResourceAsStream("credentials.json");
			credentials = (JSONObject) new JSONParser().parse(new InputStreamReader(json));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String projectId = (String) credentials.get("project_id");
		String subscriptionId = "cris";
		SubscriberStubSettings subscriberStubSettings = SubscriberStubSettings.newBuilder().build();
		SubscriberStub subscriber = GrpcSubscriberStub.create(subscriberStubSettings);
		String subscriptionName = ProjectSubscriptionName.format(projectId, subscriptionId);
		PullRequest pullRequest = PullRequest.newBuilder()
				.setMaxMessages(100)
				.setReturnImmediately(true)
				.setSubscription(subscriptionName)
				.build();

		PullResponse pullResponse = subscriber.pullCallable().call(pullRequest);
		List<String> ackIds = new ArrayList<>();
		for (ReceivedMessage message : pullResponse.getReceivedMessagesList()) {
			JSONObject jsonPublication = null;
			try {
				jsonPublication = (JSONObject) new JSONParser()
						.parse(message.getMessage().getData().toStringUtf8());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			// If publication exists or first author do not match continue
			if (pdao.read((String) jsonPublication.get("id")) != null ||
					!jsonPublication.get("firstAuthor").equals(researcher.getId()))
				continue;
			
			// Else build and save new publication
			Publication publication = new Publication();
			publication.setId((String) jsonPublication.get("id"));
			publication.setTitle((String) jsonPublication.get("tilte")); // Bug in pub/sub file
			publication.setEid((String) jsonPublication.get("eid"));
			publication.setPublicationName((String) jsonPublication.get("publicationName"));
			publication.setPublicationDate((String) jsonPublication.get("publicationDate"));
			publication.setFirstAuthor(researcher.getId());
			
			String[] authors = ((String) jsonPublication.get("authors")).split(";");
			publication.setAuthors(new ArrayList<String>(Arrays.asList(authors)));
			pdao.create(publication);
			
			researcher.getPublications().add(publication.getId());
			rdao.update(researcher);
			ackIds.add(message.getAckId());
			
			for (String author : authors) {
				Researcher a = rdao.read(author);
				if (a != null && !a.getPublications().contains(publication.getId())) {
					a.getPublications().add(publication.getId());
					rdao.update(a);
				}
			}
		}

		if(!ackIds.isEmpty()) {
			AcknowledgeRequest acknowledgeRequest = AcknowledgeRequest.newBuilder()
					.setSubscription(subscriptionName)
					.addAllAckIds(ackIds)
					.build();
			subscriber.acknowledgeCallable().call(acknowledgeRequest);
		}
		
		resp.sendRedirect(req.getContextPath() + "/ResearcherServlet" + "?id=" + researcher.getId());
	}

}
