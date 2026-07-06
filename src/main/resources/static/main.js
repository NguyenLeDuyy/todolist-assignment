const API_URL = 'http://localhost:8080/api/tasks';

// 1. Khởi tạo & Lắng nghe sự kiện
document.addEventListener('DOMContentLoaded', () => {
    fetchTasks(); // Tải danh sách ngay khi mở trang

    // Lắng nghe sự kiện submit form thêm mới
    document.getElementById('addForm').addEventListener('submit', function (e) {
        e.preventDefault(); // Ngăn trang bị reload
        addTask();
    });

    // Lắng nghe sự kiện thay đổi bộ lọc
    document.getElementById('filterSelect').addEventListener('change', function (e) {
        fetchTasks(e.target.value);
    });
});

// 2. Hàm Lấy Danh Sách (GET)
async function fetchTasks(filter = 'all') {
    let url = API_URL;
    if (filter !== 'all') {
        url += `?completed=${filter}`;
    }

    try {
        const response = await fetch(url);
        const tasks = await response.json();
        renderTasks(tasks);
    } catch (error) {
        console.error('Lỗi khi tải dữ liệu:', error);
        alert('Không thể kết nối đến máy chủ!');
    }
}

// 3. Hàm Hiển Thị Dữ Liệu Ra Màn Hình
function renderTasks(tasks) {
    const taskList = document.getElementById('taskList');
    taskList.innerHTML = ''; // Xóa sạch danh sách cũ

    document.getElementById('taskCount').innerText = `Tổng số: ${tasks.length} công việc`;

    if (tasks.length === 0) {
        taskList.innerHTML = `<li class="list-group-item text-center text-muted">Không có công việc nào!</li>`;
        return;
    }

    tasks.forEach(task => {
        const li = document.createElement('li');
        li.className = 'list-group-item d-flex justify-content-between align-items-center';

        // Giao diện cho từng công việc
        li.innerHTML = `
            <div class="d-flex align-items-center gap-3">
                <input class="form-check-input mt-0" type="checkbox" style="transform: scale(1.5);"
                       ${task.completed ? 'checked' : ''} 
                       onchange="toggleStatus(${task.id}, '${task.title}', ${task.completed})">
                <span class="${task.completed ? 'text-decoration-line-through text-muted' : 'fw-medium'}">
                    ${task.title}
                </span>
            </div>
            <button class="btn btn-outline-danger btn-sm" onclick="deleteTask(${task.id})">
                <i class="bi bi-trash"></i> Xóa
            </button>
        `;
        taskList.appendChild(li);
    });
}

// 4. Hàm Thêm Công Việc (POST)
async function addTask() {
    const input = document.getElementById('taskInput');
    const title = input.value.trim();

    if (!title) return;

    try {
        const response = await fetch(API_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ title: title, completed: false }) // Gửi theo chuẩn DTO
        });

        if (response.ok) {
            input.value = ''; // Xóa ô nhập liệu
            fetchTasks(document.getElementById('filterSelect').value); // Tải lại danh sách
        } else {
            const error = await response.json();
            alert(error.message || 'Lỗi khi thêm công việc!');
        }
    } catch (error) {
        console.error(error);
    }
}

// 5. Hàm Cập Nhật Trạng Thái Hoàn Thành (PUT)
async function toggleStatus(id, currentTitle, currentStatus) {
    try {
        await fetch(`${API_URL}/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                title: currentTitle,
                completed: !currentStatus // Đảo ngược trạng thái hiện tại
            })
        });
        fetchTasks(document.getElementById('filterSelect').value);
    } catch (error) {
        console.error(error);
    }
}

// 6. Hàm Xóa Công Việc (DELETE)
async function deleteTask(id) {
    if (!confirm('Bạn có chắc chắn muốn xóa công việc này?')) return;

    try {
        await fetch(`${API_URL}/${id}`, {
            method: 'DELETE'
        });
        fetchTasks(document.getElementById('filterSelect').value);
    } catch (error) {
        console.error(error);
    }
}