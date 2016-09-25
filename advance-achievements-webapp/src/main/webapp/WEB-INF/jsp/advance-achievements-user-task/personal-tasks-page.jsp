<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Advance Achievements</title>
    
    <script>
    	var rootPath = "${ pageContext.request.contextPath }";
    </script>

    <!-- Bootstrap core CSS -->
    <link href="${ pageContext.request.contextPath }/static/css/bootstrap.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="${ pageContext.request.contextPath }/static/css/common.css" rel="stylesheet">
  </head>

  <body>
    <!-- Fixed navbar -->
    <nav class="navbar navbar-default navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">Advance Achievements</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
            <li class="active"><a href="#">Home</a></li>
            <li><a href="#about">About</a></li>
            <li><a href="#contact">Contact</a></li>
          </ul>
			<ul class="nav navbar-nav navbar-right">
				<li>
					<form action="${ pageContext.request.contextPath }/logout" method="post">
					<input type="submit" class="btn btn-link" value="Logout" />
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
					</form>
				</li>
			</ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>

    <!-- Begin page content -->
    <div class="container">
		<div id="exceptions">
			${ exceptions }
			<c:forEach items="${ exceptionsDto.exceptions }" var="entry">
				<c:if test="${ entry.key eq 'global' }">
					<div class="alert alert-danger" role="alert">
						<c:forEach items="${ entry.value }" var="exception">
							<div>${ exception }</div>
						</c:forEach>
					</div>
				</c:if>
			</c:forEach>
		</div>
    	<a href="${ pageContext.request.contextPath }/create-task-page" class="btn btn-success">Create new task</a>
    	<a href="${ pageContext.request.contextPath }/create-task-category-page" class="btn btn-success">Create new category</a>
		<p></p>
		<div class="list-group">
			<c:forEach items="${ personalTasks }" var="entry">
				<div class="panel panel-default">
					<div class="panel-heading">
						<span class="panel-title">${ entry.key.present?entry.key.get().name:"" }</span>
					</div>
					<div class="panel-body">
						<ul class="list-group">
						<c:forEach items="${ entry.value }" var="task">
							<li class="list-group-item">
								<h4 class="list-group-item-heading">
								${ task.title }
								
								<span class="btn-group" style="float:right;">
									<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
										Task menu
										<span class="caret"></span>
									</button>
									<ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
									<li onclick="deleteTask(${ task.id })"><a href="#">Delete task</a></li>
									<!-- <li><a href="#">Another action</a></li>
									<li><a href="#">Something else here</a></li>
									<li role="separator" class="divider"></li>
									<li><a href="#">Separated link</a></li> -->
									</ul>
								</span>
								</h4>
								<div style="clear:both;"></div>
								
								
								
								<p class="list-group-item-text" style="white-space:pre;">${ task.description }</p>
							</li>
						</c:forEach>
						</ul>
						<c:if test="${ entry.key.present }">
							<a href="${ pageContext.request.contextPath }/create-task-page/${ entry.key.present?entry.key.get().id:"" }" class="btn btn-success">Create new task</a>
							<button class="btn btn-warning" onclick="deleteTaskCategory(${ entry.key.present?entry.key.get().id:'' })">Delete category</button>
						</c:if>
					</div>
				</div>
			</c:forEach>
		</div>
    </div>

    <footer class="footer">
      <div class="container">
        <p class="text-muted">Place sticky footer content here.</p>
      </div>
    </footer>


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <script src="${ pageContext.request.contextPath }/static/js/jquery-3.1.1.min.js"></script>
    <script src="${ pageContext.request.contextPath }/static/js/bootstrap.min.js"></script>
    <script src="${ pageContext.request.contextPath }/static/js/common.js"></script>
    <!-- Placed at the end of the document so the pages load faster -->
    <!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script>window.jQuery || document.write('<script src="../../assets/js/vendor/jquery.min.js"><\/script>')</script>
    <script src="../../dist/js/bootstrap.min.js"></script> -->
  </body>
</html>
