<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
	<title>Recaptcha</title>
	<meta charset="UTF-8">
	<link rel="shortcut icon" href="#">
	<!-- reCAPTCHA Libary -->
	<script src='https://www.google.com/recaptcha/api.js?hl=en'></script>
</head>
<body>
	<%--	<jsp:include page="home.jsp"></jsp:include>--%>
	<h3>Login:</h3>
	<p style="color: red;">${errorString}</p>
	<form name="loginForm" method="POST" action="/recaptcha">
		<table>
			<tr>
				<td>User Name</td>
				<td><input value="abc" type="text" name="username" /></td>
			</tr>
			<tr>
				<td>Password</td>
				<td><input value="123" type="password" name="password" /></td>
			</tr>
		</table>
		<!-- reCAPTCHA -->
		<div class="g-recaptcha" data-sitekey="${SITE_KEY}"></div>
		<input type="submit" value="Submit" />
	</form>
</body>
</html>
