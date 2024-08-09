// Modal functionality
const modal = document.getElementById("myModal");
const btn = document.getElementById("openModal");
const span = document.getElementsByClassName("close")[0];

btn.onclick = function() {
    modal.style.display = "block";
}

span.onclick = function() {
    modal.style.display = "none";
}

window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

// Form steps functionality
const nextButton = document.querySelector('.btn-next');
const prevButton = document.querySelector('.btn-prev');
const steps = document.querySelectorAll('.step');
const form_steps = document.querySelectorAll('.form-step');
const submitButton = document.querySelector('.btn-submit');
let active = 1;

nextButton.addEventListener('click', () => {
    active++;
    if (active > steps.length) {
        active = steps.length;
    }
    updateProgress();
});

prevButton.addEventListener('click', () => {
    active--;
    if (active < 1) {
        active = 1;
    }
    updateProgress();
});

const updateProgress = () => {
    console.log('steps.length => ' + steps.length);
    console.log('active => ' + active);

    steps.forEach((step, i) => {
        if (i === (active - 1)) {
            step.classList.add('active');
            form_steps[i].classList.add('active');
            console.log('i => ' + i);
        } else {
            step.classList.remove('active');
            form_steps[i].classList.remove('active');
        }
    });

    if (active === 1) {
        prevButton.disabled = true;
        nextButton.style.display = 'block';
        submitButton.style.display = 'none';
    } else if (active === steps.length) {
        nextButton.style.display = 'none';
        submitButton.style.display = 'block';
        prevButton.disabled = false;
    } else {
        prevButton.disabled = false;
        nextButton.style.display = 'block';
        submitButton.style.display = 'none';
    }
};

// Initialize progress on page load
updateProgress();
