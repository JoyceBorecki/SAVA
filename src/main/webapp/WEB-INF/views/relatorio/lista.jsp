<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Resultados das Avaliações" />
<c:set var="currentNav" value="admin_relatorios" />

<!DOCTYPE html>
<html lang="pt-BR">
  <head>
    <jsp:include page="/WEB-INF/views/includes/_head.jspf" />

    <style>
      .table-wrapper {
        width: 100%;
        overflow-x: auto;
      }
      .data-table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 16px;
      }
      .data-table th, .data-table td {
        padding: 12px 16px;
        text-align: left;
        border-bottom: 1px solid var(--divider);
      }
      .data-table th {
        color: var(--text-heading);
        font-size: 13px;
        font-weight: 600;
        text-transform: uppercase;
        background: var(--app-bg);
      }
      .data-table td {
        font-size: 14px;
      }
      .data-table .actions {
        display: flex;
        gap: 16px;
      }
      .data-table .actions a {
        color: var(--brand);
        font-weight: 600;
      }
      .data-table .actions a:hover {
        color: var(--brand-hover);
      }
      .page-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        gap: 16px;
        flex-wrap: wrap;
      }
    </style>
  </head>
  <body>
    <jsp:include page="/WEB-INF/views/includes/_header.jspf" />
    
    <main class="page-wrap">
      <div class="container">
        
        <div class="page-header">
          <section class="mb-4">
            <h1 class="page-title">Resultados das Avaliações</h1>
            <p class="page-subtitle">Selecione um formulário para visualizar os relatórios.</p>
          </section>
          <div class="btn-row"></div>
        </div>

        <section class="card card-padding">
          <div class="table-wrapper">
            <table class="data-table">
              <thead>
                <tr>
                  <th>Formulário</th>
                  <th>Processo Avaliativo</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                <c:if test="${empty formularios}">
                    <tr>
                        <td colspan="3" style="color: var(--text-subtle); padding: 24px; text-align: center;">
                            Nenhum formulário disponível.
                        </td>
                    </tr>
                </c:if>
                
                <c:forEach var="form" items="${formularios}">
                  <tr>
                    <td>
                        <c:out value="${form.titulo}" />
                        <c:if test="${form.anonimo}">
                            <span style="font-size: 11px; color: #E68A00; background: #FFF3E0; padding: 2px 6px; border-radius: 4px; margin-left: 8px;">Anônimo</span>
                        </c:if>
                    </td>
                    <td><c:out value="${form.processoAvaliativo.nome}" /></td>
                    <td class="actions">
                        <a href="relatorios?action=ver&id=${form.id}">
                            Ver Resultados
                        </a>
                    </td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
        </section>
      </div>
    </main>
  </body>
</html>