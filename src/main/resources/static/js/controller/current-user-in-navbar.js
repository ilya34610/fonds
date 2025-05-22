document.addEventListener("DOMContentLoaded", () => {
  fetch('/api/currentUser')
    .then(res => {
      if (res.status === 200) return res.json();
      throw new Error('Not authenticated');
    })
    .then(user => {
      const el = document.getElementById('navbar-username');
      if (el) el.textContent = user.login;
    })
    .catch(() => {
      const el = document.getElementById('navbar-login');
      if (el) el.style.display = 'block';
    });
});
