function validateLogin() {
  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;
  const errorMessage = document.getElementById("error-message");

  // Reset the error message before validating
  errorMessage.textContent = "";
  errorMessage.style.color = "black";

  // Validate the fields
  if (username === "" || password === "") {
    errorMessage.textContent = "Please fill in all fields.";
    errorMessage.style.color = "red";
  } else if (username === "student123" && password === "password123") {
    errorMessage.style.color = "green";
    errorMessage.textContent = "Login successful!";
  } else {
    errorMessage.style.color = "red";
    errorMessage.textContent = "Invalid username or password.";
  }
}
