<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8">
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
				Project
			</div>
			<div class="panel-body">	
				<form id="create-project-form" action="${pageContext.request.contextPath}/newproject" method="POST">
					<div class="form-group">
					  <label for="name">Name</label>
					  <input id="name" name="name" type="text" class="form-control" placeholder="Project name" autofocus>
					</div>
					<c:if test='${ exceptions.getFieldErrors("name").size() > 0 }'>
						<div class="alert alert-danger">
							<c:forEach items='${ exceptions.getFieldErrors("name") }' var='item'>
								<div>${ item.getDefaultMessage() }</div>
							</c:forEach>
						</div>
					</c:if>
					
					<div class="form-group">
					  <label for="description">Description</label>
					  <textarea id="description" name="description" class="form-control" placeholder="Project description" rows="3"></textarea>
					</div>
					<c:if test='${ exceptions.getFieldErrors("description").size() > 0 }'>
						<div class="alert alert-danger">
							<c:forEach items='${ exceptions.getFieldErrors("description") }' var='item'>
								<div>${ item.getDefaultMessage() }</div>
							</c:forEach>
						</div>
					</c:if>
					
					<input type="hidden" name="${ _csrf.parameterName }" value="${ _csrf.token }" />
					
		  			<button type="submit" class="btn btn-primary">Save</button>
				</form>
			</div>
			<c:if test="${ userProjectToUpdate != null }">
				<script>
					document.getElementById('name').value = "${ fn:replace(userProjectToUpdate.name,"\"","\\\"") }";
					document.getElementById('description').value = "${ fn:replace(userProjectToUpdate.description,"\"","\\\"") }";
					
					var input = document.createElement("input");
					input.type = 'hidden';
					input.name = 'id';
					input.value = '${ userProjectToUpdate.id }';
					document.getElementById('create-project-form').appendChild(input);
				</script>
			</c:if>
		</div>
	</div>
</body>
</html>