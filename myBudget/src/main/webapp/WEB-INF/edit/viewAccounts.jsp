<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous"/>
	<title>Cash Accounts</title>
</head>
<body>

	<header>
		<h1>Welcome to your Cash Accounts</h1>
	</header>
	
	<main style="display: flex; justify-content: center">
		<table class="table-striped table-hover table-bordered">
   			<tr>
   				<th>Name of Account</th>
   				<th>Balance</th>
   			</tr>
   			<c:forEach items="${accounts}" var="account">
	   			<tr>
	   				<td>${account.title}</td>
	   				<td>${account.amount}</td>
	   			</tr>
   			</c:forEach>
   		</table>
 
	</main>
	
	<div id="add-new-cashAccount">
		<h4>Add a New Account</h4>
		
		<p style="color: red"><form:errors path="cashacct.*"/></p>
   
	    <form:form method="POST" action="/new/cashAcct" modelAttribute="cashacct">
	    	<form:input path="user" type="hidden" value="${user.getId()}" />
	        <p>
	            <form:label path="title">Title:</form:label>
	            <form:input path="title"/>
	        </p>
	        <p>
	            <form:label path="amount">Amount: $</form:label>
	            <form:input path="amount"/>
	        </p>
	        <button type="submit">Create</button>
	    </form:form>
  		</div>
	
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-p34f1UUtsS3wqzfto5wAAmdvj+osOnFyQFpp4Ua3gs/ZVWx6oOypYoCJhGGScy+8" crossorigin="anonymous"></script>
</body>
</html> 