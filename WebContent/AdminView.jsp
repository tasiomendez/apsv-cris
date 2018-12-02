<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Admin View</title>
</head>
<body>
	<%@ include file="Header.jsp"%>
	<h2>Set researcher login credentials</h2>
	<form action="UpdateResearcherCredentialsServlet" method="post">

		<input type="text" name="id" placeholder="User Id" required> 
		<input type="text" name="email" placeholder="Email" required> 
		<input type="text" name="password" placeholder="Password" required>
		<button type="submit">Set credentials</button>
	</form>
	<h2>Create researcher</h2>
	<form action="CreateResearcherServlet" method="post">

		<input type="text" name="uid" placeholder="User Id" required> 
		<input type="text" name="name" placeholder="Name" required> 
		<input type="text" name="last_name" placeholder="Last name" required>
		<button type="submit">Create researcher</button>
	</form>
	<h2>Populate researchers</h2>
	<form action="PopulateResearchersServlet" method="post" enctype="multipart/form-data">
		<input type="file" name="file" required/>
		<button type="submit">Populate</button>
	</form>
	<h2>Populate publications</h2>
	<form action="PopulatePublicationsServlet" method="post" enctype="multipart/form-data">
		<input type="file" name="file" required/>
		<button type="submit">Populate</button>
	</form>
</body>
</html>