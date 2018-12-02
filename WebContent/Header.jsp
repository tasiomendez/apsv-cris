<c:if test="${not (empty user)}">
	<p>You are authenticated as ${user.id}</p>
	<p>
		<a href="LogoutServlet">Logout</a>
	</p>
</c:if>
<c:if test="${empty user}">
	<a href="LoginServlet">Login</a>
</c:if>
<p style="color: red;">${message}</p>
<h3>
	<a href="ResearcherListServlet">Researchers list</a>
</h3>
<hr>
