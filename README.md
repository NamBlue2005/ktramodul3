# Database để ở đây



CREATE DATABASE IF NOT EXISTS library_management;
USE library_management;

CREATE TABLE HocSinh (
maHocSinh INT AUTO_INCREMENT PRIMARY KEY,     
hoTen VARCHAR(100) NOT NULL,
lop VARCHAR(50) NOT NULL
) ;


CREATE TABLE Sach (
maSach INT AUTO_INCREMENT PRIMARY KEY,       
tenSach VARCHAR(255) NOT NULL,
tacGia VARCHAR(100) NOT NULL,
moTa TEXT NULL,                              
soLuong INT NOT NULL DEFAULT 0,              
CONSTRAINT chk_soLuong CHECK (soLuong >= 0)
);

CREATE TABLE TheMuonSach (
maMuonSach VARCHAR(10) PRIMARY KEY,        
maSach INT NOT NULL,
maHocSinh INT NOT NULL,
trangThai BOOLEAN NOT NULL,                 
ngayMuon DATE NOT NULL,
ngayTra DATE NOT NULL,
CONSTRAINT fk_themuonsach_sach FOREIGN KEY (maSach) REFERENCES Sach(maSach)
ON DELETE RESTRICT ON UPDATE CASCADE,   
CONSTRAINT fk_themuonsach_hocsinh FOREIGN KEY (maHocSinh) REFERENCES HocSinh(maHocSinh)
ON DELETE RESTRICT ON UPDATE CASCADE,    
CONSTRAINT chk_ngayTra CHECK (ngayTra >= ngayMuon)
);

INSERT INTO HocSinh (hoTen, lop) VALUES
('Nguyễn Văn An', '10A1'),
('Trần Thị Bình', '11B2'),
('Lê Văn Cường', '12C3'),
('Phạm Thị Dung', '10A2');

INSERT INTO Sach (tenSach, tacGia, moTa, soLuong) VALUES
('Lập trình Java Web Back-End', 'CodeGym', 'Giáo trình Module 4 - Java Web Back-End Development', 15),
('Cấu trúc dữ liệu và giải thuật', 'Mr. A', 'Các cấu trúc dữ liệu cơ bản và thuật toán sắp xếp, tìm kiếm.', 8),
('Toán rời rạc', 'Bộ Giáo Dục', 'Lý thuyết đồ thị, tổ hợp, logic mệnh đề.', 5),
('Vật lý đại cương I', 'Lương Duyên Bình', 'Cơ học, Nhiệt học.', 0),
('Tiếng Anh Chuyên Ngành CNTT', 'Ms. B', 'Thuật ngữ tiếng Anh trong Công nghệ thông tin.', 12);

INSERT INTO TheMuonSach (maMuonSach, maSach, maHocSinh, trangThai, ngayMuon, ngayTra) VALUES
('MS-0011', 1, 2, TRUE, '2025-03-25', '2025-04-10'),
('MS-0012', 3, 1, TRUE, '2025-03-28', '2025-04-15'),
('MS-0014', 5, 3, TRUE, '2025-04-01', '2025-04-20');



SELECT * FROM HocSinh;
SELECT * FROM Sach;
SELECT * FROM TheMuonSach;