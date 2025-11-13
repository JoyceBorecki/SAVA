<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="${empty usuario.id ? 'Novo Usuário' : 'Editar Usuário'}" />
<c:set var="currentNav" value="admin_usuarios" />

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <jsp:include page="/WEB-INF/views/includes/_head.jspf" />
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght@400" rel="stylesheet" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/usuario-form.css">
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

            <form class="form-grid" action="usuarios" method="post" id="formUsuario" novalidate>
                <input type="hidden" name="id" value="${usuario.id != null ? usuario.id : ''}" />

                <div class="field">
                    <label class="label" for="nome">Nome completo *</label>
                    <input id="nome" class="input" type="text" name="nome" value="${usuario.nome}" required/>
                    <span class="help-text">Somente letras, sem números.</span>
                </div>

                <div class="field">
                    <label class="label" for="email">E-mail *</label>
                    <input id="email" class="input" type="email" name="email" value="${usuario.email}" required/>
                    <span class="help-text">Este e-mail será utilizado para acessar o sistema.</span>
                </div>

                <div class="field">
                    <label class="label" for="perfilId">Perfil *</label>
                    <select id="perfilId" class="select" name="perfilId" required>
                        <option value="">Selecione</option>
                        <c:forEach var="perfil" items="${perfis}">
                            <option value="${perfil.id}"
                                ${usuario.perfil != null && perfil.id == usuario.perfil.id ? 'selected' : ''}>
                                    ${perfil.nome}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="field">
                    <label class="label" for="senha">Senha *</label>
                    <div class="password-wrapper">
                        <input id="senha" class="input" type="password" name="senha" minlength="8" maxlength="50"
                            <c:if test="${empty usuario.id}">required</c:if>/>
                        <span class="material-symbols-outlined toggle-password" data-target="senha">
                            visibility
                        </span>
                    </div>
                    <span class="help-text">Mínimo 8 caracteres, máximo 50.</span>
                </div>

                <div class="field">
                    <label class="label" for="confirmarSenha">Confirmar senha *</label>
                    <div class="password-wrapper">
                        <input id="confirmarSenha" class="input" type="password" name="confirmarSenha"
                            <c:if test="${empty usuario.id}">required</c:if> />
                        <span class="material-symbols-outlined toggle-password" data-target="confirmarSenha">
                            visibility
                        </span>
                    </div>

                    <span id="erroConfirmacao" class="error-confirmacao">
                        As senhas não conferem.
                    </span>

                    <span class="help-text">Repita a senha para confirmar.</span>
                </div>

                <div class="form-footer">
                    <a href="usuarios?action=listar" class="btn btn-secondary btn-sm">Cancelar</a>
                    <button type="submit" class="btn btn-primary btn-sm">Salvar</button>
                </div>
            </form>
        </section>
    </div>
</main>
<script src="${pageContext.request.contextPath}/assets/js/usuario-form.js"></script>
</body>
</html>