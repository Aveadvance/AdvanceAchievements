<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Advance Achievements - New task category</title>

<!-- Bootstrap core CSS -->
<link href="${pageContext.request.contextPath}/static/css/bootstrap.css" rel="stylesheet"/>

<!-- Custom styles for this template -->
<link href="${pageContext.request.contextPath}/static/css/common.css" rel="stylesheet"/>

</head>
<body>
	<div class="container">
		<div class="panel panel-primary">
			<div class="panel-heading">
				Task category
			</div>
			<div class="panel-body">
				<form id="create-task-category-form" action="${pageContext.request.contextPath}/newtaskcategory" method="POST">
					<div class="form-group">
					  <label for="task-category-name">Task category name</label>
					  <input id="task-category-name" name="name" type="text" class="form-control" placeholder="Task category name" autofocus>
					</div>
					<c:if test='${ exceptions.getFieldErrors("name").size() > 0 }'>
						<div class="alert alert-danger">
							<c:forEach items='${ exceptions.getFieldErrors("name") }' var='item'>
								<div>${ item.getDefaultMessage() }</div>
							</c:forEach>
						</div>
					</c:if>
					
					<input type="hidden" name="${ _csrf.parameterName }" value="${ _csrf.token }" />
					
		  			<button type="submit" class="btn btn-primary">Save</button>
				</form>
				<c:if test="${ userTaskCategoryToUpdate != null }">
					<script>
						document.getElementById("task-category-name").value = "${ fn:replace(userTaskCategoryToUpdate.name, "\"", "\\\"") }";
						var input = document.createElement("input");
						input.type = 'hidden';
						input.name = 'id';
						input.value = ${ userTaskCategoryToUpdate.id };
						document.getElementById("create-task-category-form").appendChild(input);
					</script>
				</c:if>
			</div>
		</div>
	</div>
</body>
</html>