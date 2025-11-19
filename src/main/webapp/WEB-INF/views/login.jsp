<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Login — Sistema de Avaliação Acadêmica" />

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <jsp:include page="/WEB-INF/views/includes/_head.jspf" />
    <style>
        .login-card {
            max-width: 420px;
            margin: 0 auto;
        }

        .error-message {
            background: #FFF5F5;
            color: #E53E3E;
            border: 1px solid #FED7D7;
            padding: 12px 16px;
            border-radius: 10px;
            font-size: 14px;
            font-weight: 500;
            margin-bottom: 16px;
        }
    </style>
</head>
<body>
<main class="login-wrap">
    <div class="login-card">
        <div class="card login-card card-padding">

            <div class="login-header">
                <div class="login-logo" aria-hidden="true">
                    <svg width="34" height="34" viewBox="0 0 64 64" fill="none">
                        <circle cx="32" cy="32" r="30" fill="#0C4DB2" opacity="0.12"/>
                        <path d="M8 24l24-10 24 10-24 10L8 24z" fill="#0C4DB2"/>
                        <path d="M20 30v8c0 2.21 5.37 6 12 6s12-3.79 12-6v-8" stroke="#0C4DB2" stroke-width="2" fill="none"/>
                        <circle cx="32" cy="22" r="2" fill="#0C4DB2"/>
                    </svg>
                </div>
                <h1 class="login-title">Sistema de Avaliação Acadêmica</h1>
                <div class="login-subtitle">Acesse sua conta</div>
            </div>

            <c:if test="${not empty erro}">
                <div class="error-message" role="alert">
                    <c:out value="${erro}"/>
                </div>
            </c:if>

            <form class="form-grid" action="login" method="post">
                <div class="field">
                    <label class="label" for="email">E-mail institucional</label>
                    <input id="email" class="input" type="email" name="email" placeholder="email@ufpr.br" required value="<c:out value='${param.email}' />" />
                </div>

                <div class="field">
                    <label class="label" for="senha">Senha</label>
                    <input id="senha" class="input" type="password" name="senha" placeholder="Digite sua senha" required />
                </div>
                <button class="btn btn-primary w-full" type="submit">Entrar</button>
            </form>
        </div>
    </div>
</main>
</body>
</html>