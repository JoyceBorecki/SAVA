<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="${empty turma.id ? 'Nova Turma' : 'Editar Turma'}" />
<c:set var="currentNav" value="admin_turmas" />

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <jsp:include page="/WEB-INF/views/includes/_head.jspf" />

    <style>
        .dual-listbox {
            display: flex;
            gap: 1.5rem;
            align-items: center;
            margin-top: .5rem;
        }

        .dual-listbox select {
            width: 250px;
            height: 160px;
            padding: 0.5rem;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: .9rem;
            background: #fafafa;
        }

        .dual-listbox .buttons {
            display: flex;
            flex-direction: column;
            gap: .6rem;
        }

        .dual-listbox button {
            padding: .45rem .75rem;
            border: none;
            background: #0069d9;
            color: white;
            font-size: .85rem;
            border-radius: 4px;
            cursor: pointer;
            transition: 0.2s;
        }

        .dual-listbox button:hover {
            background: #0053b3;
        }

        .dual-header {
            display: flex;
            gap: 1.5rem;
            margin-bottom: .3rem;
            font-size: .85rem;
            font-weight: 600;
            color: #555;
        }

        .dual-header div {
            width: 250px;
        }

        .dual-header .center {
            width: 60px;
            text-align: center;
        }
    </style>
</head>

<body>

<jsp:include page="/WEB-INF/views/includes/_header.jspf" />

<main class="page-wrap">
    <div class="container">

        <section class="mb-4">
            <h1 class="page-title"><c:out value="${pageTitle}" /></h1>
            <p class="page-subtitle">Preencha os dados da turma.</p>
        </section>

        <section class="card card-padding">
            <form class="form-grid" action="turmas" method="post" id="formTurma" novalidate>
                <input type="hidden" name="id" value="${turma.id}"/>
                <div class="field">
                    <label class="label">Disciplina</label>
                    <select class="select" name="disciplinaId" required>
                        <option value="" disabled
                            <c:if test="${empty turma.disciplina}">selected</c:if>>
                            Selecione a disciplina
                        </option>
                        <c:forEach var="disciplina" items="${disciplinas}">
                            <option value="${disciplina.id}"
                                    <c:if test="${not empty turma.disciplina and disciplina.id == turma.disciplina.id}">
                                        selected
                                    </c:if>>
                                    ${disciplina.nome}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="field">
                    <label class="label">Semestre</label>
                    <input class="input" type="text" name="semestre" value="${turma.semestre}" placeholder="Ex.: 2025/2" required />
                </div>

                <div class="field">
                    <div class="dual-header">
                        <div>Alunos disponíveis (<span id="countAlunosDisp"></span>)</div>
                        <div class="center">Ações</div>
                        <div>Alunos matriculados (<span id="countAlunosSel"></span>)</div>
                    </div>

                    <div class="dual-listbox">
                        <select id="alunosDisponiveis" multiple>
                            <c:forEach var="a" items="${alunos}">
                                <c:if test="${empty turma.alunos || !turma.alunos.contains(a)}">
                                    <option value="${a.id}">${a.nome}</option>
                                </c:if>
                            </c:forEach>
                        </select>

                        <div class="buttons">
                            <button type="button" onclick="mover('alunosDisponiveis','alunosSelecionados')">&gt;&gt;</button>
                            <button type="button" onclick="mover('alunosSelecionados','alunosDisponiveis')">&lt;&lt;</button>
                        </div>

                        <select id="alunosSelecionados" name="alunos" multiple>
                            <c:forEach var="a" items="${alunos}">
                                <c:if test="${not empty turma.alunos && turma.alunos.contains(a)}">
                                    <option value="${a.id}">${a.nome}</option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </div>

                    <span class="help-text">Use os botões para matricular ou desmatricular alunos.</span>
                </div>

                <div class="field">
                    <div class="dual-header">
                        <div>Professores disponíveis (<span id="countProfDisp"></span>)</div>
                        <div class="center">Ações</div>
                        <div>Professores designados (<span id="countProfSel"></span>)</div>
                    </div>

                    <div class="dual-listbox">
                        <select id="profDisponiveis" multiple>
                            <c:forEach var="p" items="${professores}">
                                <c:if test="${empty turma.professores || !turma.professores.contains(p)}">
                                    <option value="${p.id}">${p.nome}</option>
                                </c:if>
                            </c:forEach>
                        </select>

                        <div class="buttons">
                            <button type="button" onclick="mover('profDisponiveis','profSelecionados')">&gt;&gt;</button>
                            <button type="button" onclick="mover('profSelecionados','profDisponiveis')">&lt;&lt;</button>
                        </div>

                        <select id="profSelecionados" name="professores" multiple>
                            <c:forEach var="p" items="${professores}">
                                <c:if test="${not empty turma.professores && turma.professores.contains(p)}">
                                    <option value="${p.id}">${p.nome}</option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </div>

                    <span class="help-text">Professores à direita serão responsáveis pela turma.</span>
                </div>

                <div class="form-footer">
                    <a href="turmas?action=listar" class="btn btn-secondary btn-sm">Cancelar</a>
                    <button type="submit" class="btn btn-primary btn-sm">Salvar</button>
                </div>

            </form>
        </section>
    </div>
</main>
<script src="${pageContext.request.contextPath}/assets/js/turma-form.js"></script>
</body>
</html>