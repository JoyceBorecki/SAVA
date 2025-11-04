<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="${empty turma.id ? 'Nova Turma' : 'Editar Turma'}" />
<c:set var="currentNav" value="admin_turmas" />

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
          <p class="page-subtitle">Preencha os dados da turma.</p>
        </section>

        <section class="card card-padding">
          <form class="form-grid" action="turmas" method="post" novalidate>
            
            <input type="hidden" name="id" value="<c:out value='${turma.id}' />" />

            <%-- Campo Disciplina (Dropdown) --%>
            <div class="field">
              <label class="label" for="disciplinaId">Disciplina</label>
              <select id="disciplinaId" class="select" name="disciplinaId" required>
                <option value="" disabled ${empty turma.disciplina ? 'selected' : ''}>
                  Selecione a disciplina
                </option>
                <c:forEach var="disciplina" items="${disciplinas}">
                  <option value="${disciplina.id}" 
                          ${disciplina.id == turma.disciplina.id ? 'selected' : ''}>
                    <c:out value="${disciplina.nome}" />
                  </option>
                </c:forEach>
              </select>
              <span class="help-text">A unidade curricular desta turma.</span>
            </div>

            <%-- Campo Semestre --%>
            <div class="field">
              <label class="label" for="semestre">Semestre</label>
              <input id="semestre" class="input" type="text" name="semestre"
                     placeholder="Ex.: 2025/2" 
                     value="<c:out value='${turma.semestre}' />" 
                     required />
              <span class="help-text">O semestre de oferta da turma.</span>
            </div>
            
            <%-- Botões de Ação --%>
            <div class="form-footer">
              <a href="turmas?action=listar" class="btn btn-secondary btn-sm">Cancelar</a>
              <button type="submit" class="btn btn-primary btn-sm">Salvar</button>
            </div>
            
          </form>
        </section>
      </div>
    </main>
  </body>
</html>