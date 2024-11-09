window.onload = function() {
    // Add schedule button click event
    document.getElementById("add-schedule").addEventListener("click", function() {
        const title = document.getElementById("title").value;
        const startDate = document.getElementById("start-date").value;
        const endDate = document.getElementById("end-date").value;
        const location = document.getElementById("location").value;
        const memo = document.getElementById("memo").value;
        const sharedWith = document.getElementById("shared-with").value;
        const item = {
            title: title,
            startDate: startDate,
            endDate: endDate,
            location: location,
            memo: memo,
            sharedWith: sharedWith
        };

        // Checklist items
        const checklistItems = [];
        document.querySelectorAll("#checklist-items input[type='checkbox']").forEach(function(checkbox) {
            if (checkbox.checked) {
                checklistItems.push(checkbox.parentElement.textContent.trim());
            }
        });

        // Add new schedule
        const scheduleList = document.querySelector(".schedule-list");
        const newSchedule = document.createElement("div");
        newSchedule.classList.add("schedule-item");

        newSchedule.innerHTML = `
            <div class="schedule-item">
                <div class="schedule-header">
                    <span class="schedule-title">${title}</span>
                    <button class="toggle-button">▼</button>
                    <button class="edit-button" data-item=${JSON.stringify(item)} onclick="openModal(this, 'edit-modal');">✏️</button>
                </div>
                <div class="schedule-details">
                    <p><strong>날짜:</strong> ${startDate} ~ ${endDate}</p>
                    <p><strong>위치:</strong> ${location}</p>
                    <p><strong>메모:</strong> ${memo}</p>
                    <p><strong>공유 대상:</strong> ${sharedWith}</p>
                    <ul>${checklistItems.map(item => `<li>${item}</li>`).join('')}</ul>
                </div>
            </div>
        `;

        scheduleList.appendChild(newSchedule);
        document.querySelector(".add-schedule-form").reset(); // Reset the form after adding
    });

    // Toggle schedule details (show/hide)
    document.addEventListener("click", function(event) {
        if (event.target.classList.contains("toggle-button")) {
            const details = event.target.closest(".schedule-item").querySelector(".schedule-details");
            details.style.display = details.style.display === "none" ? "block" : "none";
            event.target.textContent = details.style.display === "none" ? "▼" : "▲";
        }
    });

    // Checklist item click event for background color change
    document.querySelectorAll(".checklist-item").forEach(function(button) {
        button.addEventListener("click", function() {
            this.classList.toggle("selected");
        });
    });

    // Add new checklist item
    document.getElementById("add-checklist-item").addEventListener("click", function() {
        const newItem = document.getElementById("new-checklist-item").value.trim();
        if (newItem) {
            const newButton = document.createElement("button");
            newButton.classList.add("checklist-item");
            newButton.textContent = newItem;
            newButton.addEventListener("click", function() {
                this.classList.toggle("selected");
            });
            document.getElementById("checklist-items").appendChild(newButton);
            document.getElementById("new-checklist-item").value = ""; // Clear input
            modal.style.display = "none"; // Close modal
        } else {
            alert("항목을 입력해주세요.");
        }
    });

    // Modal open and close functionality
    window.onclick = function(event) {
        const modals = document.querySelectorAll('.modal');
        modals.forEach(function(modal) {
            if (event.target === modal) {
                closeModal(modal.id);
            }
        });
    };

    // Function to open modal for editing
    function openModal(obj, modalId) {
        const item = JSON.parse(obj.dataset.item);
        const { title, startDate, endDate, location, memo, sharedWith } = item;

        document.getElementById('edit-title').value = title || "";
        document.getElementById('edit-start-date').value = startDate || "";
        document.getElementById('edit-end-date').value = endDate || "";
        document.getElementById('edit-location').value = location || "";
        document.getElementById('edit-memo').value = memo || "";
        document.getElementById('edit-shared-with').value = sharedWith || "";

        const modal = document.getElementById(modalId);
        modal.style.display = "block";
    }

    // Close modal function
    function closeModal(modalId) {
        const modal = document.getElementById(modalId);
        modal.style.display = "none";
    }

    // Edit schedule form submit
    document.getElementById("edit-schedule-form").addEventListener("submit", function(event) {
        event.preventDefault(); // Prevent default form submit
        const updatedItem = {
            title: document.getElementById("edit-title").value,
            startDate: document.getElementById("edit-start-date").value,
            endDate: document.getElementById("edit-end-date").value,
            location: document.getElementById("edit-location").value,
            memo: document.getElementById("edit-memo").value,
            sharedWith: document.getElementById("edit-shared-with").value,
        };

        // Update schedule in list
        updateScheduleInList(event.target.dataset.scheduleId, updatedItem);

        // Close modal
        document.getElementById("edit-modal").style.display = "none";
    });

    // Update the schedule in list
    function updateScheduleInList(scheduleId, updatedItem) {
        const scheduleItem = document.querySelector(`[data-id="${scheduleId}"]`);
        if (scheduleItem) {
            scheduleItem.querySelector(".schedule-title").textContent = updatedItem.title;
            scheduleItem.querySelector(".schedule-details p:nth-child(1)").textContent = `날짜: ${updatedItem.startDate} ~ ${updatedItem.endDate}`;
            scheduleItem.querySelector(".schedule-details p:nth-child(2)").textContent = `위치: ${updatedItem.location}`;
            scheduleItem.querySelector(".schedule-details p:nth-child(3)").textContent = `메모: ${updatedItem.memo}`;
            scheduleItem.querySelector(".schedule-details p:nth-child(4)").textContent = `공유 대상: ${updatedItem.sharedWith}`;
        }
    }
};
