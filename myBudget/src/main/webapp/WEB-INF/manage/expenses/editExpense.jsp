<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous"/>
	<title>Edit Expense</title>
</head>
<body>

		<h2>Edit <c:out value="${expense.title}"></c:out></h2>
		
	    <c:forEach items="${errors}" var="error">
       		<p style="color: red"><c:out value="${error}"></c:out></p>
       	</c:forEach>
		
	    <form:form method="post" action="/edit/expenses/${expense.id}" modelAttribute="expense">
	    	<input value="put" name="_method" type="hidden" />
	    	
	    	<form:input path="user" type="hidden" value="${user.id}"/>
	    	<p>
	            <form:label path="title">Title:</form:label>
	            <form:errors path="title" />
	            <form:input path="title" required="true"/>
	        </p>
	        <p>
	            <form:label path="amount">Amount: $</form:label>
	            <form:errors path="amount" />
	            <form:input path="amount" required="true"/>
	        </p>
	        <input type="submit" value="Edit" class="btn btn-primary"/>
	    
	    <c:forEach items="${duedateErrors}" var="error">
       		<p style="color: red"><c:out value="${error}"></c:out></p>
       	</c:forEach>
	    </form:form>
	    	<form:form method="post" action="/add/duedate/${expense.id}" modelAttribute="duedate">
	    	
	    	<form:input path="expense" type="hidden" value="${expense.id}"/>
	    	<p>
	            <form:label path="date">Day:</form:label>
	            <form:errors path="date" />
	            <form:input path="date" placeholder="Enter day(Example: 15)" required="true"/>
	        </p>
	        <input type="submit" value="Add" class="btn btn-primary"/>
	    </form:form>

</body>
</html>