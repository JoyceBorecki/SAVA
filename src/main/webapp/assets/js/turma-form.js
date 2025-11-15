function mover(origemId, destinoId) {
    const origem = document.getElementById(origemId);
    const destino = document.getElementById(destinoId);

    Array.from(origem.selectedOptions).forEach(option => {
        option.selected = false;
        destino.appendChild(option);
    });

    atualizarContadores();
}

function atualizarContadores() {
    const count = (id) => document.getElementById(id).options.length;

    document.getElementById("countAlunosDisp").innerText = count("alunosDisponiveis");
    document.getElementById("countAlunosSel").innerText = count("alunosSelecionados");
    document.getElementById("countProfDisp").innerText = count("profDisponiveis");
    document.getElementById("countProfSel").innerText = count("profSelecionados");
}

document.addEventListener("DOMContentLoaded", () => {
    atualizarContadores();

    document.getElementById('formTurma').addEventListener('submit', () => {
        document.querySelectorAll('#alunosSelecionados option').forEach(o => o.selected = true);
        document.querySelectorAll('#profSelecionados option').forEach(o => o.selected = true);
    });
});
