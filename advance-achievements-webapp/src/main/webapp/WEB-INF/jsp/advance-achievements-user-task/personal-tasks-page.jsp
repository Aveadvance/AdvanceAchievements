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
    	<a href="${ pageContext.request.contextPath }/create-task-page" class="btn btn-success">Create new task</a>
		<div class="list-group">
			<c:forEach items="${ personalTasks }" var="task">
				<a href="#" class="list-group-item">
					<h4 class="list-group-item-heading">${ task.title }</h4>
					<p class="list-group-item-text">${ task.description }</p>
				</a>
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
    <!-- Placed at the end of the document so the pages load faster -->
    <!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script>window.jQuery || document.write('<script src="../../assets/js/vendor/jquery.min.js"><\/script>')</script>
    <script src="../../dist/js/bootstrap.min.js"></script> -->
  </body>
</html>
