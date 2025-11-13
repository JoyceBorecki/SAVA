<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="${empty usuario.id ? 'Novo Usuário' : 'Editar Usuário'}" />
<c:set var="currentNav" value="admin_usuarios" />

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <jsp:include page="/WEB-INF/views/includes/_head.jspf" />

    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght@400" rel="stylesheet" />

    <style>
        .password-wrapper {
            position: relative;
        }
        .toggle-password {
            position: absolute;
            right: 10px;
            top: 50%;
            transform: translateY(-50%);
            cursor: pointer;
            color: #666;
            font-size: 22px;
            user-select: none;
        }
        .alert-error {
            padding: 14px 18px;
            background: #ffe6e6;
            border-left: 5px solid #d20000;
            border-radius: 6px;
            color: #7a0000;
            margin-bottom: 22px;
            font-size: 15px;
        }
    </style>
</head>

<body>

<jsp:include page="/WEB-INF/views/includes/_header.jspf" />

<main class="page-wrap">
    <div class="container">

        <section class="mb-4">
            <h1 class="page-title">${pageTitle}</h1>
            <p class="page-subtitle">Preencha os dados para criar ou atualizar um usuário.</p>
        </section>

        <c:if test="${not empty erro}">
            <div class="alert-error">
                <strong>Atenção:</strong> ${erro}
            </div>
        </c:if>

        <section class="card card-padding">

            <form class="form-grid" action="usuarios" method="post" novalidate>
                <input type="hidden" name="id" value="${usuario.id != null ? usuario.id : ''}" />

                <div class="field">
                    <label class="label" for="nome">Nome completo *</label>
                    <input id="nome" class="input" type="text" name="nome" value="${usuario.nome}" required />
                </div>

                <div class="field">
                    <label class="label" for="email">E-mail *</label>
                    <input id="email" class="input" type="email" name="email" value="${usuario.email}" required />
                    <span class="help-text">Este será utilizado para login.</span>
                </div>

                <div class="field">
                    <label class="label" for="perfilId">Perfil *</label>
                    <select id="perfilId" class="select" name="perfilId" required>
                        <option value="">Selecione</option>
                        <c:forEach var="perfil" items="${perfis}">
                            <option value="${perfil.id}"${usuario.perfil != null && perfil.id == usuario.perfil.id ? 'selected' : ''}>${perfil.nome}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="field">
                    <label class="label" for="senha">Senha *</label>

                    <div class="password-wrapper">
                        <input id="senha" class="input" type="password" name="senha"
                               <c:if test="${empty usuario.id}">required</c:if>/>
                        <span class="material-symbols-outlined toggle-password" id="toggleSenha">
                            visibility
                        </span>
                    </div>

                    <span class="help-text">
                        <c:if test="${empty usuario.id}">
                            Senha obrigatória.
                        </c:if>
                        <c:if test="${not empty usuario.id}">
                            Deixe em branco para manter a senha atual.
                        </c:if>
                    </span>
                </div>

                <div class="form-footer">
                    <a href="usuarios?action=listar" class="btn btn-secondary btn-sm">Cancelar</a>
                    <button type="submit" class="btn btn-primary btn-sm">Salvar</button>
                </div>
            </form>
        </section>
    </div>
</main>

<script>
    const campo = document.getElementById("senha");
    const botao = document.getElementById("toggleSenha");

    botao.addEventListener("click", () => {
        const mostrando = campo.type === "text";

        campo.type = mostrando ? "password" : "text";
        botao.textContent = mostrando ? "visibility" : "visibility_off";
    });
</script>
</body>
</html>