function check() {
    if (document.getElementById('pwd').value ===
            document.getElementById('controlpwd').value) {
        document.getElementById('checked').innerHTML ="Passwords are matching!"
        document.getElementById('submit').disabled = false
    } else {
        document.getElementById('checked').innerHTML="Passwords do not match, please try again!"
        document.getElementById('submit').disabled = true
    }
}
