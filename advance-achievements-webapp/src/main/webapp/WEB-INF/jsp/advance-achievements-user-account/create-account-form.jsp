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
				Create new account
			</div>
			<div class="panel-body">	
				<form action="${pageContext.request.contextPath}/newaccount" method="POST">
					<div class="form-group">
					  <label for="email">Email address</label>
					  <input id="email" name="email" type="email" class="form-control" placeholder="Email">
					</div>
					<c:if test='${ exceptions.getFieldErrors("email").size() > 0 }'>
						<div class="alert alert-danger">
							<c:forEach items='${ exceptions.getFieldErrors("email") }' var='item'>
								<div>${ item.getDefaultMessage() }</div>
							</c:forEach>
						</div>
					</c:if>
					
					<div class="form-group">
					  <label for="password">Password</label>
					  <input id="password" name="password" type="password" class="form-control" placeholder="Password">
					</div>
					<c:if test='${ exceptions.getFieldErrors("password").size() > 0 }'>
						<div class="alert alert-danger">
							<c:forEach items='${ exceptions.getFieldErrors("password") }' var='item'>
								<div>${ item.getDefaultMessage() }</div>
							</c:forEach>
						</div>
					</c:if>
					
					<input type="hidden" name="${ _csrf.parameterName }" value="${ _csrf.token }" />
					
		  			<button type="submit" class="btn btn-primary">Create account</button>
				</form>
			</div>
		</div>
	</div>
</body>
</html>