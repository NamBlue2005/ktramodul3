<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>Danh Sách Sách - Thư viện ABC</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <style>
    body { padding-top: 20px; }
    .action-button { width: 80px; }
  </style>
</head>
<body>
<div class="container">
  <h1 class="mb-4 text-center">Danh Sách Sách Trong Thư Viện</h1>

  <div class="mb-3 d-flex justify-content-end">
    <a href="${pageContext.request.contextPath}/borrowed-books" class="btn btn-info">Xem Sách Đang Mượn</a>
  </div>

  <c:if test="${param.success == 'Borrowed'}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
      Mượn sách thành công!
      <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
  </c:if>
  <c:if test="${param.error != null}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
      Đã xảy ra lỗi: ${param.error}
      <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
  </c:if>

  <table class="table table-striped table-bordered table-hover">
    <thead class="table-dark">
    <tr>
      <th>Mã Sách</th>
      <th>Tên Sách</th>
      <th>Tác Giả</th>
      <th>Số Lượng</th>
      <th>Hành Động</th>
    </tr>
    </thead>
    <tbody>
    <c:choose>
      <c:when test="${not empty bookList}">
        <c:forEach var="book" items="${bookList}">
          <tr>
            <td>${book.maSach}</td>
            <td><c:out value="${book.tenSach}"/></td>
            <td><c:out value="${book.tacGia}"/></td>
            <td>${book.soLuong}</td>
            <td>
              <c:choose>
                <c:when test="${book.soLuong > 0}">
                  <a href="${pageContext.request.contextPath}/borrow?bookId=${book.maSach}" class="btn btn-primary btn-sm action-button">Mượn</a>
                </c:when>
                <c:otherwise>
                  <button class="btn btn-secondary btn-sm action-button" disabled>Hết Sách</button>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
        </c:forEach>
      </c:when>
      <c:otherwise>
        <tr>
          <td colspan="5" class="text-center fst-italic">Không có sách nào trong thư viện.</td>
        </tr>
      </c:otherwise>
    </c:choose>
    </tbody>
  </table>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>