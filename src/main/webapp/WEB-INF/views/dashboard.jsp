<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="pageTitle" value="Minhas Avaliações — Sistema de Avaliação Acadêmica" />
<c:set var="currentNav" value="avaliacoes" />
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <jsp:include page="/WEB-INF/views/includes/_head.jspf" />
</head>
<body>
<jsp:include page="/WEB-INF/views/includes/_header.jspf" />
<main class="page-wrap">
    <div class="container">
        <section class="mb-4">
            <h1 class="page-title">Bem-vindo(a), <c:out value="${usuarioLogado.nome}"/></h1>
            <p class="page-subtitle">
                Você tem ${fn:length(pendentes)} avaliações pendentes.
            </p>
        </section>

        <section class="grid-cards">
            <c:forEach var="avaliacao" items="${pendentes}">
                <article class="card card-padding card-course">
                    <div class="top">
                        <div class="icon-doc" aria-hidden="true">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                                <rect x="5" y="3" width="12" height="18" rx="2" ry="2" stroke="#0C4DB2" stroke-width="1.5" fill="#fff"/>
                                <path d="M7.5 8.5h7M7.5 12h7M7.5 15.5h7" stroke="#0C4DB2" stroke-width="1.5" stroke-linecap="round"/>
                            </svg>
                        </div>

                        <div>
                            <h2 class="title">
                                <c:choose>
                                    <c:when test="${not empty avaliacao.turma && not empty avaliacao.turma.disciplina}">
                                        <c:out value="${avaliacao.turma.disciplina.nome}"/>
                                    </c:when>
                                    <c:otherwise>
                                        (Disciplina não definida)
                                    </c:otherwise>
                                </c:choose>
                            </h2>

                            <c:set var="professorPrincipal" value="" />
                            <c:forEach var="prof" items="${avaliacao.turma.professores}" varStatus="st">
                                <c:if test="${st.first}">
                                    <c:set var="professorPrincipal" value="${prof.nome}" />
                                </c:if>
                            </c:forEach>

                            <div class="meta">
                                Professor(a):
                                <c:choose>
                                    <c:when test="${not empty professorPrincipal}">
                                        <c:out value="${professorPrincipal}"/>
                                    </c:when>
                                    <c:otherwise>
                                        Não definido
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                <div>
                <span class="badge badge-warning">
                  <span class="icon-dot" aria-hidden="true" style="background: #FFE0B2;">
                    <svg width="10" height="10" viewBox="0 0 24 24" fill="none">
                      <circle cx="12" cy="12" r="10" fill="#E68A00"/>
                    </svg>
                  </span>
                  Não respondido
                </span>
                    </div>
                    <div class="actions">
                        <a href="responder?id=${avaliacao.id}" class="btn btn-primary btn-sm">
                            Responder Avaliação
                        </a>
                    </div>
                </article>
            </c:forEach>

            <c:forEach var="avaliacao" items="${respondidas}">
                <article class="card card-padding card-course">
                    <div class="top">
                        <div class="icon-doc" aria-hidden="true">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                                <rect x="5" y="3" width="12" height="18" rx="2" ry="2" stroke="#0C4DB2" stroke-width="1.5" fill="#fff"/>
                                <path d="M7.5 8.5h7M7.5 12h7M7.5 15.5h7" stroke="#0C4DB2" stroke-width="1.5" stroke-linecap="round"/>
                            </svg>
                        </div>

                        <div>
                            <h2 class="title">
                                <c:choose>
                                    <c:when test="${not empty avaliacao.turma && not empty avaliacao.turma.disciplina}">
                                        <c:out value="${avaliacao.turma.disciplina.nome}"/>
                                    </c:when>
                                    <c:otherwise>
                                        (Disciplina não definida)
                                    </c:otherwise>
                                </c:choose>
                            </h2>

                            <c:set var="professorPrincipal" value="" />
                            <c:forEach var="prof" items="${avaliacao.turma.professores}" varStatus="st">
                                <c:if test="${st.first}">
                                    <c:set var="professorPrincipal" value="${prof.nome}" />
                                </c:if>
                            </c:forEach>

                            <div class="meta">
                                Professor(a):
                                <c:choose>
                                    <c:when test="${not empty professorPrincipal}">
                                        <c:out value="${professorPrincipal}"/>
                                    </c:when>
                                    <c:otherwise>
                                        Não definido
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>

                    <div>
                <span class="badge badge-success">
                  <span class="icon-dot" aria-hidden="true" style="background: #C9F0DA;">
                    <svg width="10" height="10" viewBox="0 0 24 24" fill="none">
                      <path d="M9.5 12.8l-2-2 -1.5 1.5 3.5 3.5 7-7L14 7.4 9.5 12.8Z" fill="#22A06B"/>
                    </svg>
                  </span>
                  Respondido
                </span>
                    </div>
                    <div class="actions">
                        <button class="btn btn-primary btn-sm disabled" type="button" disabled>
                            Visualizar Resposta
                        </button>
                    </div>
                </article>
            </c:forEach>
        </section>
    </div>
</main>
</body>
</html>
