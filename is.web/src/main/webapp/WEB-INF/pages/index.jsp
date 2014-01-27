<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<body>
	<h2>DEMO :: Search DATA</h2>

	<form:form modelAttribute="searchFileUpload" method="post"
		enctype="multipart/form-data">
		<fieldset>
			<legend>Data for Search</legend>

			<form:label for="name" path="name">Name</form:label>
			<br />
			<form:input path="name" />
			<br /> <br />
			<form:label for="fileData" path="fileData">File</form:label>
			<br />
			<form:input path="fileData" type="file" />

			<p>
				<input type="submit" value="Search Data" />
			</p>

		</fieldset>
	</form:form>
	<div  style="float: left;">
	<c:if test="${not empty results}">
		
			<c:forEach var="o" items="${results}">
				<div style="float: left;">
					<img src="http://localhost.is/${o.fileName}" width="200" />
				</div>
			</c:forEach>
		
	</c:if>
	</div>


</body>
</html>