<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome Page</title>
</head>
<body>
    <h1>Welcome Page <c:out value="${currentUser.firstName} ${currentUser.lastName}"></c:out></h1>
    
    <main>
    	
    	<nav style="display: flex; justify-content: space-around">
    		<p>Dashboard</p>
    		<p>Dropdown menu</p>
    		<p><a href="/account">Account</a></p>
    		<form id="logoutForm" method="POST" action="/logout">
		        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		        <input type="submit" value="Logout!" />
		    </form>
    	</nav>
    	
    	<div id="budget-tables-container" style="display: flex; flex-wrap: wrap; justify-content: center">
    		<div id="free-to-spend" style="width: 51%">
    			<h5>Free to Spend</h5>
				<table style="border: 1px solid black">
	    			<tr>
	    				<th>Week</th>
	    				<th>Month</th>
	    			</tr>
	    			<tr>
	    				<td>$250</td>
	    				<td>$1000</td>
	    			</tr>
	    		</table>
    		</div>
    		<div id="balance" style="width: 51%">
    			<h5>Balance</h5>
				<table style="border: 1px solid black">
	    			<tr>
	    				<th>Balance</th>
	    			</tr>
	    			<tr>
	    				<td>$5000</td>
	    			</tr>
	    		</table>
    		</div>
    		<div id="expenses-table" style="width: 51%">
    			<h5>Expenses</h5>
				<table style="border: 1px solid black">
	    			<tr>
	    				<th>Name</th>
	    				<th>Amount</th>
	    				<th>Due Date</th>
	    			</tr>
	    			<tr>
	    				<td>Car Payment</td>
	    				<td>$200</td>
	    				<td>15 May 2021</td>
	    			</tr>
	    		</table>
    		</div>
    	</div>
    	
    	
    </main>
</body>
</html>