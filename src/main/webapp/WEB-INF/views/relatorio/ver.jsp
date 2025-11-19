<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Relatório Detalhado" />
<c:set var="currentNav" value="admin_relatorios" />

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <jsp:include page="/WEB-INF/views/includes/_head.jspf" />
    <style>
        .stats-card { margin-bottom: 30px; border-bottom: 1px solid #eee; padding-bottom: 20px; }
        .stats-card:last-child { border-bottom: none; margin-bottom: 0; } 
        .bar-container { background: #f0f0f0; border-radius: 4px; height: 24px; width: 100%; position: relative; margin-top: 5px; }
        .bar-fill { background: #0C4DB2; height: 100%; border-radius: 4px; transition: width 0.5s; }
        .bar-text { position: absolute; right: 8px; top: 2px; font-size: 12px; font-weight: bold; color: #333; }
        .alt-row { display: grid; grid-template-columns: 2fr 1fr; align-items: center; gap: 10px; margin-bottom: 8px; }
        .resp-box { background: #F9FAFB; padding: 10px; border-left: 4px solid #0C4DB2; margin-top: 8px; }
        .resp-author { font-weight: bold; font-size: 12px; color: #666; margin-bottom: 4px; }
        
        /* Espaço e alinhamento do rodapé */
        .page-footer-space {
            margin-top: 30px;
            padding-bottom: 60px;
            display: flex;
            justify-content: flex-end; /* Alinha o botão à direita */
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/views/includes/_header.jspf" />
    <main class="page-wrap">
        <div class="container">
            
            <%-- Cabeçalho sem botão de voltar --%>
            <div class="mb-4">
                <h1 class="page-title" style="margin-top:10px;">Relatório: <c:out value="${formulario.titulo}" /></h1>
                <p class="page-subtitle">
                    Total de Avaliações Processadas
                    <c:if test="${formulario.anonimo}"><span class="badge badge-warning">Modo Anônimo</span></c:if>
                </p>
            </div>

            <section class="card card-padding">
                <c:if test="${empty estatisticas}">
                    <div style="padding: 20px; text-align: center; color: #666;">
                        <p>Ainda não há respostas computadas para este formulário.</p>
                    </div>
                </c:if>

                <c:forEach var="est" items="${estatisticas}" varStatus="loop">
                    <div class="stats-card">
                        <h3><span style="color:#0C4DB2;">Q${loop.count}.</span> <c:out value="${est.enunciado}" /></h3>
                        
                        <%-- QUESTÃO FECHADA (GRÁFICO) --%>
                        <c:if test="${est.tipo != 'ABERTA'}">
                            <div style="margin-top:15px;">
                                <c:forEach var="entry" items="${est.contagemAlternativas}">
                                    <c:set var="pct" value="${est.getPorcentagem(entry.key)}" />
                                    <div class="alt-row">
                                        <div><c:out value="${entry.key}" /></div>
                                        <div class="bar-container">
                                            <div class="bar-fill" style="width: ${pct}%;"></div>
                                            <span class="bar-text">${entry.value} votos (${pct}%)</span>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:if>

                        <%-- QUESTÃO ABERTA (LISTA) --%>
                        <c:if test="${est.tipo == 'ABERTA'}">
                            <div style="margin-top:15px;">
                                <strong>Respostas Textuais:</strong>
                                <c:forEach var="entry" items="${est.respostasAbertas}">
                                    <div class="resp-box">
                                        <div class="resp-author">
                                            <c:out value="${entry.key}" /> disse:
                                        </div>
                                        <div><c:out value="${entry.value}" /></div>
                                    </div>
                                </c:forEach>
                                <c:if test="${empty est.respostasAbertas}">
                                    <p class="text-muted" style="font-size: 14px; margin-top: 5px;">Nenhuma resposta textual registrada.</p>
                                </c:if>
                            </div>
                        </c:if>
                    </div>
                </c:forEach>
            </section>

            <%-- Botão ÚNICO de Voltar no final --%>
            <div class="page-footer-space">
                <a href="relatorios" class="btn btn-secondary btn-sm">Voltar para Lista</a>
            </div>

        </div>
    </main>
</body>
</html>