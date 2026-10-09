// Controls the countdown shown while a participant is taking a timed quiz.
(() => {
  // These elements connect the timer to the visible countdown and answer form.
  const display = document.getElementById('timer');
  const form = document.getElementById('quiz-form');
  // The script is also included on pages without a quiz; stop if its elements are absent.
  if (!display || !form) return;
  // Read the allotted minutes from the timer element and convert them to seconds.
  let remaining = Number(document.querySelector('.timer').dataset.minutes) * 60;
  // Updates the display, submits the quiz at zero, and otherwise schedules the next tick.
  const render = () => {
    const minutes = Math.floor(remaining / 60).toString().padStart(2, '0');
    const seconds = (remaining % 60).toString().padStart(2, '0');
    display.textContent = `${minutes}:${seconds}`;
    // Submit the existing form once the countdown has reached zero or below.
    if (remaining <= 0) { form.requestSubmit(); return; }
    // Decrease by one second, then update again after a one-second delay.
    remaining -= 1;
    window.setTimeout(render, 1000);
  };
  // Render immediately so the participant sees the initial time without a delay.
  render();
})();
