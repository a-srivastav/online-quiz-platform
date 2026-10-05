(() => {
  const display = document.getElementById('timer');
  const form = document.getElementById('quiz-form');
  if (!display || !form) return;
  let remaining = Number(document.querySelector('.timer').dataset.minutes) * 60;
  const render = () => {
    const minutes = Math.floor(remaining / 60).toString().padStart(2, '0');
    const seconds = (remaining % 60).toString().padStart(2, '0');
    display.textContent = `${minutes}:${seconds}`;
    if (remaining <= 0) { form.requestSubmit(); return; }
    remaining -= 1;
    window.setTimeout(render, 1000);
  };
  render();
})();
