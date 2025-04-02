<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>Mượn Sách - Thư viện ABC</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <style>
    body { padding-top: 20px; }
    .readonly-field { background-color: #e9ecef; opacity: 1; }
  </style>
</head>
<body>
<div class="container">
  <h2 class="mb-4 text-center">Mượn Sách</h2>

  <c:if test="${not empty errorMessage}">
    <div class="alert alert-danger" role="alert">
      <strong>Vui lòng sửa các lỗi sau:</strong><br>
      <c:out value="${errorMessage}" escapeXml="false"/>
    </div>
  </c:if>

  <c:choose>
    <c:when test="${empty sach or sach.soLuong <= 0}">
      <div class="alert alert-warning text-center" role="alert">
        <h4>Thông Báo</h4>
        Sách "<c:out value="${sach.tenSach}"/>" đã hết hàng.<br>
        Không thể thực hiện mượn sách này.
      </div>
      <div class="text-center mt-3">
        <a href="${pageContext.request.contextPath}/books" class="btn btn-secondary">Quay Lại Danh Sách Sách</a>
      </div>
    </c:when>
    <c:otherwise>
      <form method="post" action="${pageContext.request.contextPath}/borrow" id="borrowForm">
        <input type="hidden" name="maSach" value="${sach.maSach}">

        <div class="mb-3">
          <label for="tenSach" class="form-label">Tên Sách:</label>
          <input type="text" id="tenSach" class="form-control readonly-field" value="<c:out value="${sach.tenSach}"/>" readonly>
        </div>

        <div class="mb-3">
          <label for="maMuonSach" class="form-label">Mã Mượn Sách:</label>
          <input type="text" id="maMuonSach" name="maMuonSach" class="form-control"
                 pattern="MS-\d{4}" title="Mã Mượn Sách phải có định dạng MS-XXXX (ví dụ: MS-1234)"
                 value="<c:out value='${submittedMaMuonSach}'/>" required>
          <div class="form-text text-muted">Nhập mã theo định dạng: MS-XXXX (Ví dụ: MS-0001)</div>
        </div>

        <div class="mb-3">
          <label for="maHocSinh" class="form-label">Tên Học Sinh:</label>
          <select id="maHocSinh" name="maHocSinh" class="form-select" required>
            <option value="">-- Vui lòng chọn học sinh --</option>
            <c:forEach var="hs" items="${hocSinhList}">
              <option value="${hs.maHocSinh}" ${hs.maHocSinh == submittedMaHocSinh ? 'selected' : ''}>
                <c:out value="${hs.hoTen}"/> - Lớp: <c:out value="${hs.lop}"/>
              </option>
            </c:forEach>
          </select>
        </div>

        <div class="mb-3">
          <label for="ngayMuonDisplay" class="form-label">Ngày Mượn:</label>
          <input type="text" id="ngayMuonDisplay" class="form-control readonly-field" value="${ngayMuonDisplay}" readonly>
        </div>

        <div class="mb-3">
          <label for="ngayTra" class="form-label">Ngày Trả:</label>
          <input type="date" id="ngayTra" name="ngayTra" class="form-control"
                 min="${today}" value="<c:out value='${submittedNgayTra}'/>" required>
          <div class="form-text">Ngày trả không được trước ngày mượn.</div>
        </div>

        <hr>

        <div class="d-flex justify-content-between mt-4">
          <button type="submit" class="btn btn-primary">
            Xác Nhận Mượn Sách
          </button>
          <button type="button" class="btn btn-secondary" onclick="confirmBack()">
            Trở về danh sách
          </button>
        </div>
      </form>
    </c:otherwise>
  </c:choose>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
  function confirmBack() {
    if (confirm("Bạn có chắc chắn muốn quay lại danh sách sách không? Mọi thông tin chưa lưu sẽ bị mất.")) {
      window.location.href = '${pageContext.request.contextPath}/books';
    }
  }
</script>
</body>
</html>