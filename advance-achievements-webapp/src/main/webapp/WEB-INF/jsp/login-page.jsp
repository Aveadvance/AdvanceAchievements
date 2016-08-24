<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Advance Achievements - Welcome</title>

<!-- Bootstrap core CSS -->
<link href="${pageContext.request.contextPath}/static/css/bootstrap.css" rel="stylesheet"/>

<!-- Custom styles for this template -->
<link href="${pageContext.request.contextPath}/static/css/common.css" rel="stylesheet"/>

</head>
<body>
	<div class="container">
		<div class="panel panel-primary">
			<div class="panel-heading">
				Please sign in
			</div>
			<div class="panel-body">
				<div>
					<c:if test="${ param.exception != null }">
						<div class="alert alert-danger" role="alert">Check that your email and password are correct.</div>
					</c:if>
				</div>	
				<form action="${pageContext.request.contextPath}/login" method="POST">
				
					<div class="form-group">
					  <label for="email">Email address</label>
					  <input id="email" name="username" type="email" class="form-control" placeholder="Email">
					</div>
					
					<div class="form-group">
					  <label for="password">Password</label>
					  <input id="password" name="password" type="password" class="form-control" placeholder="Password">
					</div>
					
					<input type="hidden" name="${ _csrf.parameterName }" value="${ _csrf.token }"/>
					
		  			<button type="submit" class="btn btn-primary">Sign in</button>
		  			
		  			<a class="btn btn-link" href="${ pageContext.request.contextPath }/create-account">Create new account</a>
				</form>
			</div>
		</div>
	</div>
</body>
</html>