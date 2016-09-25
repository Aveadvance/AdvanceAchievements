<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
				Create new task
			</div>
			<div class="panel-body">	
				<form action="${pageContext.request.contextPath}/newtask" method="POST">
					<div class="form-group">
					  <label for="title">Title</label>
					  <input id="title" name="title" type="text" class="form-control" placeholder="Task title">
					</div>
					<c:if test='${ exceptions.getFieldErrors("title").size() > 0 }'>
						<div class="alert alert-danger">
							<c:forEach items='${ exceptions.getFieldErrors("title") }' var='item'>
								<div>${ item.getDefaultMessage() }</div>
							</c:forEach>
						</div>
					</c:if>
					
					<div class="form-group">
					  <label for="description">Description</label>
					  <textarea id="description" name="description" class="form-control" placeholder="Task description" rows="3"></textarea>
					</div>
					<c:if test='${ exceptions.getFieldErrors("description").size() > 0 }'>
						<div class="alert alert-danger">
							<c:forEach items='${ exceptions.getFieldErrors("description") }' var='item'>
								<div>${ item.getDefaultMessage() }</div>
							</c:forEach>
						</div>
					</c:if>
					
					<div class="form-group">
					  <label for="priority">Priority</label>
					  <select id="priority" name="priority" class="form-control">
					  	<option value="VERY_HIGH">Very high</option>
					  	<option value="HIGH">High</option>
					  	<option value="MIDDLE" selected="selected">Middle</option>
					  	<option value="LOW">Low</option>
					  	<option value="VERY_LOW">Very low</option>
					  </select>
					</div>
					<c:if test='${ exceptions.getFieldErrors("priority").size() > 0 }'>
						<div class="alert alert-danger">
							<c:forEach items='${ exceptions.getFieldErrors("priority") }' var='item'>
								<div>${ item.getDefaultMessage() }</div>
							</c:forEach>
						</div>
					</c:if>
					
					<c:if test='${ userTaskCategoryId != null }'>
						<input type="hidden" name="userTaskCategoryId" value="${ userTaskCategoryId }" />
					</c:if>
					<input type="hidden" name="${ _csrf.parameterName }" value="${ _csrf.token }" />
					
		  			<button type="submit" class="btn btn-primary">Create task</button>
				</form>
			</div>
		</div>
	</div>
</body>
</html>