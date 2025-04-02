<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>Sách Đang Mượn - Thư viện ABC</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <style>
    body { padding-top: 20px; }
    .action-button { width: 90px; }
  </style>
</head>
<body>
<div class="container">
  <h1 class="mb-4 text-center">Thống Kê Sách Đang Được Mượn</h1>

  <div class="mb-3">
    <a href="${pageContext.request.contextPath}/books" class="btn btn-secondary">
      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-left-circle" viewBox="0 0 16 16">
        <path fill-rule="evenodd" d="M1 8a7 7 0 1 0 14 0A7 7 0 0 0 1 8m15 0A8 8 0 1 1 0 8a8 8 0 0 1 16 0m-4.5-.5a.5.5 0 0 1 0 1H5.707l2.147 2.146a.5.5 0 0 1-.708.708l-3-3a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L5.707 7.5z"/>
      </svg>
      Quay Lại Danh Sách Sách
    </a>
  </div>

  <form method="get" action="${pageContext.request.contextPath}/borrowed-books" class="mb-4 p-3 border rounded bg-light">
    <div class="row g-3 align-items-center">
      <div class="col-md-5">
        <label for="searchBookName" class="visually-hidden">Tìm theo Tên Sách</label>
        <input type="text" class="form-control" id="searchBookName" name="searchBookName" placeholder="Tìm theo tên sách..." value="<c:out value="${searchBookName}"/>">
      </div>
      <div class="col-md-5">
        <label for="searchStudentName" class="visually-hidden">Tìm theo Tên Học Sinh</label>
        <input type="text" class="form-control" id="searchStudentName" name="searchStudentName" placeholder="Tìm theo tên học sinh..." value="<c:out value="${searchStudentName}"/>">
      </div>
      <div class="col-md-2 d-flex justify-content-end">
        <button type="submit" class="btn btn-primary me-2">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-search" viewBox="0 0 16 16">
            <path d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001q.044.06.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1 1 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0"/>
          </svg>
          Tìm
        </button>
        <a href="${pageContext.request.contextPath}/borrowed-books" class="btn btn-outline-secondary">Xóa Lọc</a>
      </div>
    </div>
  </form>

  <c:if test="${param.success == 'Returned'}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
      Trả sách thành công:
      <c:if test="${not empty returnedBookName}"><c:out value="${returnedBookName}"/></c:if>
      <c:if test="${not empty returnedBorrowCode}"> (Mã mượn: <c:out value="${returnedBorrowCode}"/>)</c:if>
      <c:if test="${not empty returnedStudentName}"> - Người mượn: <c:out value="${returnedStudentName}"/></c:if>
      <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
  </c:if>
  <c:if test="${param.error != null}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
      Đã xảy ra lỗi khi xử lý yêu cầu: ${param.error}
      <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
  </c:if>

  <table class="table table-striped table-bordered table-hover">
    <thead class="table-dark">
    <tr>
      <th>Mã Mượn</th>
      <th>Tên Sách</th>
      <th>Người Mượn</th>
      <th>Lớp</th>
      <th>Ngày Mượn</th>
      <th>Ngày Trả</th>
      <th>Hành Động</th>
    </tr>
    </thead>
    <tbody>
    <c:choose>
      <c:when test="${not empty borrowedList}">
        <c:forEach var="borrow" items="${borrowedList}">
          <tr>
            <td>${borrow.maMuonSach}</td>
            <td><c:out value="${borrow.tenSach}"/></td>
            <td><c:out value="${borrow.hoTenHocSinh}"/></td>
            <td><c:out value="${borrow.lop}"/></td>
            <td>${borrow.formattedNgayMuon}</td>
            <td>${borrow.formattedNgayTra}</td>
            <td>
              <form method="post" action="${pageContext.request.contextPath}/return-book" style="display:inline;" onsubmit="return confirmReturn('${borrow.maMuonSach}', '${borrow.tenSach}', '${borrow.hoTenHocSinh}');">
                <input type="hidden" name="maMuonSach" value="${borrow.maMuonSach}">
                <input type="hidden" name="maSach" value="${borrow.maSach}">
                <button type="submit" class="btn btn-success btn-sm action-button">Trả Sách</button>
              </form>
            </td>
          </tr>
        </c:forEach>
      </c:when>
      <c:otherwise>
        <tr>
          <td colspan="7" class="text-center fst-italic">Không có sách nào đang được mượn hoặc không tìm thấy kết quả phù hợp.</td>
        </tr>
      </c:otherwise>
    </c:choose>
    </tbody>
  </table>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
  function confirmReturn(maMuonSach, tenSach, tenHocSinh) {
    return confirm("Học sinh " + tenHocSinh + " thực hiện trả sách '" + tenSach + "' với mã '" + maMuonSach + "'?");
  }
</script>
</body>
</html>