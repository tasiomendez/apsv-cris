<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<% UserService userService = UserServiceFactory.getUserService(); %>

<c:if test="${pageContext.request.userPrincipal == null and (empty user)}">
    <p>
    	<a href='<%=userService.createLoginURL("/LoginServlet")%>'>Login with Google</a>
    </p>
</c:if>

<c:if test="${pageContext.request.userPrincipal != null}">
	<p>You are authenticated as ${user.name} ${user.lastName} (${user.id})</p>
    <p>
    	<a href='<%=userService.createLogoutURL("/LogoutServlet")%>'>Logout from Google</a>
    </p>
</c:if>

<c:if test="${user.id == 'root'}">
	<p>You are authenticated as ${user.name} ${user.lastName} (${user.id})</p>
	<p>
		<a href="LogoutServlet">Logout</a>
	</p>
</c:if>

<p style="color: red;">${message}</p>
<h3>
	<a href="ResearcherListServlet">Researchers list</a>
</h3>
<hr>