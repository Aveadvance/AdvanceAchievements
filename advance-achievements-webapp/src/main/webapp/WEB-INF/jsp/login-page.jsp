<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
		<form class="form-signin" action="${pageContext.request.contextPath}/login" method="POST">
			<h2 class="form-signin-heading">Please sign in</h2>
			<label for="inputEmail" class="sr-only">Email address</label>
			<input type="text" name="username" id="inputEmail" class="form-control" placeholder="Email address" required autofocus>
			<label for="inputPassword" class="sr-only">Password</label>
			<input type="password" name="password" id="inputPassword" class="form-control" placeholder="Password" required>
			<input type="hidden" name="${ _csrf.parameterName }" value="${ _csrf.token }"/>
			<div class="checkbox">
			  <label>
			    <input type="checkbox" value="remember-me"> Remember me
			  </label>
			</div>
			<button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
		</form>
	</div>
</body>
</html>