# Todo List App - SRT GROUP Intern Test

## Mô tả
Ứng dụng Todo List quản lý công việc: tạo, sửa, xóa, đánh dấu hoàn thành, và lọc theo trạng thái. Hiện tại cung cấp dưới dạng REST API (chưa có giao diện web, xem "Ghi chú" bên dưới).

## Công nghệ sử dụng
- Java 21, Spring Boot 4.1
- Spring Data JPA, H2 Database
- Lombok, validation, Mockito, AssertJ

## Cấu trúc project
src/
├── main/
│   ├── java/com/srt/
│   │   ├── dto/          # Requests / Responses
│   │   ├── exception/    # Xử lý exception tập trung
│   │   └── todolist/
│   │       ├── controller  # REST endpoints
│   │       ├── entity      # JPA entity
│   │       ├── repository  # Spring Data JPA repository
│   │       └── service     # Business logic
└── test/java/com/srt/todolist/service   # Unit test tầng service

## Cách chạy dự án

### Yêu cầu
- Java 21+

### Chạy local
\`\`\`bash
./gradlew bootRun
\`\`\`
Mở trình duyệt tại: http://localhost:8080

### Chạy test
\`\`\`bash
./gradlew test
\`\`\`

## API Endpoints
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | /api/tasks | Trả về danh sách công việc; có thể lọc theo trạng thái qua query param `completed` (true/false) |
| POST | /api/tasks | Tạo mới công việc |
| PUT | /api/tasks/{id} | Chỉnh sửa công việc |
| DELETE | /api/tasks/{id} | Xóa công việc |

## Test thử API (chưa có giao diện web)
Dùng Postman hoặc curl để gọi các endpoint ở trên. Ví dụ:
\`\`\`bash
curl -X POST http://localhost:8080/api/tasks -H "Content-Type: application/json" -d "{\"title\":\"Học Spring Boot\"}"
\`\`\`

H2 Console (xem dữ liệu trực tiếp): http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:todolistdb
- Username: sa, Password: (để trống)

## Ghi chú / Giới hạn hiện tại
- Chưa có phân trang (dữ liệu test ít, chưa cần thiết ở scope này)
- Chưa deploy online