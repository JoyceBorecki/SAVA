<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="${empty disciplina.id ? 'Nova Disciplina' : 'Editar Disciplina'}" />
<c:set var="currentNav" value="admin_disciplinas" />

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
          <h1 class="page-title"><c:out value="${pageTitle}" /></h1>
          <p class="page-subtitle">Preencha os dados da unidade curricular.</p>
        </section>

        <section class="card card-padding">
          <%-- O formulário aponta para o nosso servlet --%>
          <form class="form-grid" action="disciplinas" method="post" novalidate>
            
            <%-- Campo oculto para o ID (essencial para edição) --%>
            <input type="hidden" name="id" value="<c:out value='${disciplina.id}' />" />

            <%-- Campo Nome da Disciplina (baseado no "Nome do curso" [cite: 110, 111]) --%>
            <div class="field">
              <label class="label" for="nome">Nome da Disciplina</label>
              <input id="nome" class="input" type="text" name="nome"
                     placeholder="Ex.: Desenvolvimento para Dispositivos Móveis" 
                     value="<c:out value='${disciplina.nome}' />" 
                     required />
              <span class="help-text">Nome completo como consta no currículo.</span>
            </div>

            <%-- Campo Semestre (novo, mas usando o estilo de input [cite: 111, 112]) --%>
            <div class="field">
              <label class="label" for="semestre">Semestre</label>
              <input id="semestre" class="input" type="text" name="semestre"
                     placeholder="Ex.: 2025/2" 
                     value="<c:out value='${disciplina.semestre}' />" 
                     required />
              <span class="help-text">Semestre em que a disciplina é ofertada.</span>
            </div>
            
            <%-- Campo Curso (Dropdown, baseado no "Tipo" <select> [cite: 113, 114]) --%>
            <div class="field">
              <label class="label" for="cursoId">Curso</label>
              <select id="cursoId" class="select" name="cursoId" required>
                <option value="" disabled ${empty disciplina.curso ? 'selected' : ''}>
                  Selecione o curso
                </option>
                <%-- Loop para carregar os cursos do banco --%>
                <c:forEach var="curso" items="${cursos}">
                  <option value="${curso.id}" 
                          ${curso.id == disciplina.curso.id ? 'selected' : ''}>
                    <c:out value="${curso.nome}" />
                  </option>
                </c:forEach>
              </select>
              <span class="help-text">Define a qual curso esta disciplina pertence.</span>
            </div>
            
            <%-- Botões de Ação (baseados no seu template [cite: 115]) --%>
            <div class="form-footer">
              <a href="disciplinas?action=listar" class="btn btn-secondary btn-sm">Cancelar</a>
              <button type="submit" class="btn btn-primary btn-sm">Salvar</button>
            </div>
            
          </form>
        </section>
      </div>
    </main>
  </body>
</html>