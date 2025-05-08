let allTasks = [];

document.addEventListener("DOMContentLoaded", () => {
    const userId = localStorage.getItem("userId");
    if (!userId) {
        alert("Please log in first.");
        window.location.href = "login.html";
        return;
    }

    loadTasks(userId);

    document.getElementById("addTaskForm").addEventListener("submit", async (e) => {
        e.preventDefault();

        const task = {
            title: document.getElementById("title").value,
            description: document.getElementById("description").value,
            dueDate: document.getElementById("dueDate").value,
        };

        const res = await fetch(`http://localhost:8080/users/${userId}/tasks`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(task),
        });

        if (res.ok) {
            alert("Task added!");
            await loadTasks(userId);
            document.getElementById("addTaskForm").reset();
            closeModal();
        } else {
            alert("Error adding task.");
        }
    });

    document.getElementById("statusFilter").addEventListener("change", filterTasks);
    document.getElementById("closeModal").addEventListener("click", closeModal);
});

function toggleTaskModal() {
    document.getElementById("taskModal").classList.toggle("hidden");
}

function closeModal() {
    document.getElementById("taskModal").classList.add("hidden");
}

function logout() {
    localStorage.clear();
    window.location.href = "login.html";
}

async function loadTasks(userId) {
    try {
        const res = await fetch(`http://localhost:8080/users/${userId}/tasks`);
        if (!res.ok) throw new Error("Failed to fetch tasks");
        allTasks = await res.json();
        filterTasks();
    } catch (error) {
        alert("Error loading tasks.");
    }
}

function filterTasks() {
    const selectedStatus = document.getElementById("statusFilter").value;
    const filtered = selectedStatus === "ALL"
        ? allTasks
        : allTasks.filter(task => task.status === selectedStatus);
    renderTasks(filtered);
}

function renderTasks(tasksToRender) {
    const taskList = document.getElementById("taskList");
    taskList.innerHTML = "";

    if (tasksToRender.length === 0) {
        taskList.innerHTML = "<p>No tasks found.</p>";
        return;
    }

    tasksToRender.forEach(task => {
        const taskElement = document.createElement("div");
        taskElement.classList.add("task");
        taskElement.setAttribute("data-status", task.status);

        taskElement.innerHTML = `
            <div class="task-header">
                <h3 class="task-title">${task.title}</h3>
                <span class="task-status ${task.status.toLowerCase()}">${task.status}</span>
            </div>
            <p class="task-description">${task.description}</p>
            <div class="task-footer">
                <span class="task-due">Due: ${formatDate(task.dueDate)}</span>
                ${
            task.status === "PENDING"
                ? `<button onclick="markCompleted(${task.id})" class="complete-btn">Mark Completed</button>`
                : ""
        }
                <button onclick="deleteTask(${task.id})" class="delete-btn">Delete</button>
            </div>
        `;
        taskList.appendChild(taskElement);
    });
}

async function markCompleted(taskId) {
    const res = await fetch(`http://localhost:8080/tasks/${taskId}/status?status=COMPLETED`, {
        method: "PUT"
    });

    if (res.ok) {
        const userId = localStorage.getItem("userId");
        await loadTasks(userId);
    } else {
        alert("Failed to update task.");
    }
}

async function deleteTask(taskId) {
    if (!confirm("Are you sure you want to delete this task?")) return;

    const res = await fetch(`http://localhost:8080/tasks/${taskId}`, {
        method: "DELETE"
    });

    if (res.ok) {
        const userId = localStorage.getItem("userId");
        await loadTasks(userId);
    } else {
        alert("Failed to delete task.");
    }
}

function formatDate(dateString) {
    if (!dateString) return "No due date";
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    return new Date(dateString).toLocaleDateString(undefined, options);
}