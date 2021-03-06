<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.jungam.manage.vo.FileVO"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.jungam.manage.vo.BoardVO"%>
<%@page import="com.jungam.manage.vo.FileVO"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
 <head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<!-- <script type="text/javascript" src="http://code.jquery.com/jquery-2.1.3.min.js"></script> -->
<script type="text/javascript" src="/resources/js/jquery-2.1.3.js"></script>

<script>
	  $(document).ready(function () {
		  //alert('start!!');
		$('.writing').css('color', 'red');
		$(function(){
			$('.content').hide();
			$('.title').click(function(){
				if($(this).next().css('display') != 'none')
					$(this).next().slideUp(250);
				else {
					$('.content').slideUp(250);
					$(this).next().slideDown(250);
				}
			});
		});
	 });
</script>
	<title>Untitled Document</title>
	<link href="/resources/css/notice.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<jsp:include page="../ common/header.jsp" flush="false" />
<h1>공지사항</h1>
<table width="400" height="15" border="1" cellspacing="0" cellpadding="3" bordercolor="#ffffff" align="center">
<tr>
	<th  class="th" width="30" align="center">    번호    </th><th  class="th" width="500" align="center">    제목    </th><th  class="th" width="100" align="center">    작성자    </th><th  class="th" width="100" align="center">    등록일    </th>
</tr>
	<%
		int listCount;
			HashMap<Integer, BoardVO> resultTableList = (HashMap<Integer, BoardVO>) request.getAttribute("list");
			
			if(resultTableList.size() > 0){
			    		Iterator<Integer> key = resultTableList.keySet().iterator();
			    		while(key.hasNext()) {
			    			BoardVO notice = resultTableList.get(key.next());
	%>
				<tr class="title" align="center">
					<td>
				    <%=notice.getIndex() %>
				    </td>
				    <td>
				    <!-- <a href="notice/<%=notice.getIndex()%>.do"> -->
				    <%= notice.getTitle().toString() %></a>
				    </td>
				    <td>
				    <%= notice.getWriter().toString() %>
				    </td>
				    <td>
				    <%= notice.getRegiDate().toString() %>
				    </td>
				</tr>
				<tr class="content">
					<td colspan="4">
				    <%= notice.getContent().toString() %>
				    <% ArrayList<FileVO> files;
				    if((files = notice.getFiles()) != null) {
				    		for(FileVO file : files) {
				    			%> <h3> <%=file.getPath().toString()%></h3> <%
				    			//if(file.getContentType().equals("image/jpeg"))	{	%>
				    				<img src=<%= file.getPath().toString() %> width=100  heigh=500 >
				    				<img src="D:/workspace/Jung-am/Jung-am/upload_files/P20141028_181212000_E2444061-0834-4611-AC00-C3CD3EC13BFD.JPG"/>
				    				<%
				    			//}
				    		}
				    }
				    %>
				    	
				    </td>
			    </tr> 
	<%
			}
		}
	%>
</table>

<ul class="number">
<li><a href="#">1</a></li>
<li><a href="#">2</a></li>
<li><a href="#">3</a></li>
<li><a href="#">4</a></li>
<li class="last"><a href="#">5</a></li>
</ul>
<div class="modify">
<div class="writing"><a href="noticeWrite.do">글쓰기</a></div>
<div class="revise"><a href="#">수정</a></div>
<div class="delete"><a href="#">삭제</a></div>
</div>
<%@ include file="../common/footer.jsp" %>
</body>
</html>
