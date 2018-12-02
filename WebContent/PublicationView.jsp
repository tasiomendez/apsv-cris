<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Publication view</title>
</head>
<body>
	<%@ include file="Header.jsp"%>
	<p style="color: red;">${message}</p>
	
	<h2>Publication info</h2>
	<p>ID: ${publication.id }</p>
	<p>Title: ${publication.title }</p>
	<p>Publication Name: ${publication.publicationName }</p>
	<p>Publication Date: ${publication.publicationDate }</p>
	<p>Cite Count: ${publication.citeCount }</p>
	<p>First Author: <a href="ResearcherServlet?id=${firstAuthor.id}">
								${firstAuthor.name } ${firstAuthor.lastName }</a></p>
	<h2>Authors</h2>
	<table border="1">
		<tr>
			<th>Id</th>
			<th>First Name</th>
			<th>Last Name</th>
		</tr>
		<c:forEach items="${authors}" var="a_i">
			<tr>
				<td><a href="ResearcherServlet?id=${a_i.id}">${a_i.id}</a></td>
				<td>${a_i.name}</td>
				<td>${a_i.lastName}</td>
			</tr>
		</c:forEach>
	</table>
	
	<c:if test="${firstAuthor.id==user.id || userAdmin}">

		You have permission to modify this page
		
		<h3>Update citations</h3>
		<form action="UpdateCitationsAPIServlet" method="post">
			<input type="hidden" name="id" value="${publication.id}" />
			<button type="submit">Update Citations</button>
		</form>
		
		<h3>Update info</h3>
		<form action="UpdatePublicationServlet" method="post">
			<input type="hidden" name="first_author" value="${firstAuthor.id}" />
			<input type="hidden" name="id" value="${publication.id }" />
			<p>
				Title: <input type="text" name="title" value="${publication.title }"/>
			</p>
			<p>
				Authors (separated by semicolon): <input type="text" name="authors"  
								value="${stringAuthors }"/>
			</p>
			<p>
				Eid: <input type="text" name="eid"  value="${publication.eid }"/>
			</p>
			<p>
				Publication name: <input type="text" name="publication_name"  value="${publication.publicationName }"/>
			</p>
			<p>
				Publication date: <input type="text" name="publication_date"  value="${publication.publicationDate }"/>
			</p>
			<button type="submit">Update</button>
		</form>
	</c:if>
	
</body>
</html>