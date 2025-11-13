document.addEventListener("DOMContentLoaded", function () {

    const campoSenha = document.getElementById("senha");
    const campoConfirmar = document.getElementById("confirmarSenha");
    const erroConfirmacao = document.getElementById("erroConfirmacao");

    erroConfirmacao.style.display = "none";

    document.querySelectorAll(".toggle-password").forEach(icon => {
        icon.addEventListener("click", () => {
            const target = document.getElementById(icon.dataset.target);

            if (target.type === "password") {
                target.type = "text";
                icon.textContent = "visibility_off";
            } else {
                target.type = "password";
                icon.textContent = "visibility";
            }
        });
    });

    const nome = document.getElementById("nome");

    nome.addEventListener("input", () => {
        if (!/^[A-Za-zÀ-ÖØ-öø-ÿ ]+$/.test(nome.value)) {
            nome.classList.add("invalid");
        } else {
            nome.classList.remove("invalid");
        }
    });

    function validarConfirmacao() {
        if (campoConfirmar.value === "") {
            erroConfirmacao.style.display = "none";
            campoConfirmar.classList.remove("invalid");
            return;
        }

        if (campoSenha.value !== campoConfirmar.value) {
            erroConfirmacao.style.display = "block";
            campoConfirmar.classList.add("invalid");
        } else {
            erroConfirmacao.style.display = "none";
            campoConfirmar.classList.remove("invalid");
        }
    }

    campoSenha.addEventListener("input", validarConfirmacao);
    campoConfirmar.addEventListener("input", validarConfirmacao);
});
