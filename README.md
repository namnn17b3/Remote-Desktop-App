# REMOTE DESKTOP APP
## Giới thiệu:
•	Ứng dụng Remote Desktop là phần mềm cho phép người dùng kết nối với máy tính ở một vị trí khác, tương tác như thể họ được kết nối cục bộ

•	Ứng dụng cũng có thể điều khiển màn hình phía server. Việc cho phép máy client sử dụng các tính năng khác nhau của phần mềm sẽ nằm trong tay máy chủ server.
## Thông tin thành viên:
• Trịnh Minh Tuấn - B20DCCN037

• Lại Ngọc Sơn - B20DCCN575

• Nguyễn Ngọc Nam - B20DCCN455
## Các công nghệ sử dụng

•	TCP Socket

•	Java Swing

•	Java AWT (Abstract Window Toolkit)

• JDBC  (Java Database Connectivity)
## Mô tả chức năng
• Ứng dụng cho phép người dùng truy cập máy tính khác, có thể là máy tính cá nhân, máy chủ hoặc máy tính khác mà họ muốn kiểm soát từ xa

•	Chia sẻ màn hình: Màn hình của người dùng sẽ được truyền đi và hiển thị trên màn hình của người nhận. Điều này cho phép người nhận xem và điều khiển màn hình từ xa, như là việc điều khiển một máy tính từ xa.

• Điều khiển các thao tác chuột và bàn phím của máy khác

•	Chuyển tập tin: Ứng dụng cung cấp khả năng chuyển tập tin giữa người dùng gửi và người dùng nhận. Người dùng có thể chọn tập tin từ máy tính của mình và gửi nó cho người nhận thông qua ứng dụng. Người nhận sẽ nhận được tập tin và có thể lưu nó vào máy tính của mình. Chức năng này hữu ích trong việc chia sẻ và truyền tải các tệp tin quan trọng hoặc tài liệu giữa các người dùng từ xa.

• Ghi nhật kí lịch sử kết nối vào file log và database

• Chia sẻ clipboard giữa hai máy ( Tính năng đang được phát triển ): Cho phép việc copy paste giữa máy client với server và ngược lại
## Preview giao diện
• Giao diện trang chủ:
![Giao diện sẵn sàng kết nối](https://github.com/jnp2018/g3_proj-037575455/assets/94033356/72133b92-4c50-4e32-a545-4975bd39ce5d)

• Giao diện trang xem lịch sử kết nối
![image](https://github.com/jnp2018/g3_proj-037575455/assets/94033356/4c7a2de2-668d-4c52-9c8c-e257b6af6c85)

• File log được lưu thông tin lịch sử kết nối
![image](https://github.com/jnp2018/g3_proj-037575455/assets/94033356/e598ce6f-c17d-4f24-a595-62007dd34545)

• Giao diện chia sẻ màn hình khi máy client kết nối với máy server
![Giao diện kết nối client](https://github.com/jnp2018/g3_proj-037575455/assets/94033356/cd3d509d-ed5d-4973-8b20-052b2c9c1da2)

• Giao diện Menu nhận file được gửi ở máy server

![Giao diện menu nhận file server](https://github.com/jnp2018/g3_proj-037575455/assets/94033356/b473a365-b789-4ff4-a2dc-126b365f490c)


## Cài đặt môi trường
• Java Development Kit (JDK): JDK 8 trở lên

## Triển khai
• Chạy file remotedesktop.exe để chạy chương trình

• Sử dụng thư viện launch4j để tích hợp vào quy trình xây dựng Maven, cho tạo tệp thực thi .exe khi bạn xây dựng dự án của mình.
