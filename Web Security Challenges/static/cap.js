document.addEventListener('DOMContentLoaded', function() {
    const selectElement = document.getElementById('capacitySelect');
    const updateButton = document.getElementById('updateButton');
    const defaultCapacity = selectElement.value;  // Store the initial default value

    // Show the update button when the capacity is changed
    selectElement.addEventListener('change', function() {
        if (selectElement.value !== defaultCapacity) {
            updateButton.style.display = 'inline-block';
        } else {
            updateButton.style.display = 'none';
        }
    });

    // This is a general comment
    // and some details about the DOMContentLoaded event.
    const Msg = "msg param"; //Error Messages will appear in message get parameter
    
    // More about the function below
});

function updateCapacity() {
    const selectElement = document.getElementById('capacitySelect');
    const selectedCapacity = selectElement.value;
    const courseCode = selectElement.getAttribute('data-course-code');

    const form = document.createElement('form');
    form.method = 'POST';
    form.action = `/admin/course/${courseCode}/update`;

    const input = document.createElement('input');
    input.type = 'hidden';
    input.name = 'capacity';
    input.value = selectedCapacity;
    form.appendChild(input);

    document.body.appendChild(form);
    form.submit();
}
