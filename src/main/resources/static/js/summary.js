document.addEventListener("DOMContentLoaded", async () => {
    const userId = localStorage.getItem("userId");
    if (!userId) {
        alert("Please log in first.");
        window.location.href = "login.html";
        return;
    }

    try {
        const res = await fetch(`http://localhost:8080/users/${userId}/tasks`);
        if (!res.ok) throw new Error("Failed to fetch tasks");
        const allTasks = await res.json();

        updateSummary(allTasks);
        drawLineChart(allTasks);
    } catch (error) {
        alert("Error loading tasks.");
    }
});

function updateSummary(tasks) {
    const total = tasks.length;
    const pending = tasks.filter(task => task.status === "PENDING").length;
    const completed = tasks.filter(task => task.status === "COMPLETED").length;
    const completionRate = total > 0 ? Math.round((completed / total) * 100) : 0;

    document.getElementById("totalCount").textContent = total;
    document.getElementById("pendingCount").textContent = pending;
    document.getElementById("completedCount").textContent = completed;
    document.getElementById("completionRate").textContent = `${completionRate}%`;
}

function drawLineChart(tasks) {
    const pendingTasks = tasks.filter(t => t.status === "PENDING");

    // Count pending tasks by due date
    const countByDate = {};
    pendingTasks.forEach(task => {
        const dueDate = task.dueDate;
        countByDate[dueDate] = (countByDate[dueDate] || 0) + 1;
    });

    const sortedDates = Object.keys(countByDate).sort();
    const counts = sortedDates.map(date => countByDate[date]);

    const ctx = document.getElementById("lineChart").getContext("2d");
    new Chart(ctx, {
        type: 'line',
        data: {
            labels: sortedDates,
            datasets: [{
                label: 'Pending Tasks',
                data: counts,
                fill: false,
                borderColor: 'rgb(75, 192, 192)',
                tension: 0.1,
                pointBackgroundColor: 'rgb(75, 192, 192)',
                pointRadius: 5,
                pointHoverRadius: 7
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    display: true,
                    position: 'top'
                },
                tooltip: {
                    mode: 'index',
                    intersect: false
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        stepSize: 1
                    },
                    title: {
                        display: true,
                        text: 'Number of Tasks'
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: 'Due Date'
                    }
                }
            }
        }
    });
}

function logout() {
    localStorage.clear();
    window.location.href = "login.html";
}