<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Aplicar Formulário a uma Turma" />
<c:set var="currentNav" value="admin_avaliacoes" />

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
          <h1 class="page-title">Aplicar Formulário</h1>
          <p class="page-subtitle">Selecione o formulário e a turma que deve respondê-lo.</p>
        </section>

        <section class="card card-padding">
          <form class="form-grid" action="avaliacoes" method="post">
            <div class="field">
              <label class="label" for="formularioId">1. Selecione o Formulário</label>
              <select id="formularioId" class="select" name="formularioId" required>
                <option value="" disabled selected>Selecione um formulário</option>
                <c:forEach var="formulario" items="${formularios}">
                  <option value="${formulario.id}">
                    <c:out value="${formulario.titulo}" />
                  </option>
                </c:forEach>
              </select>
              <span class="help-text">O modelo de avaliação que será usado.</span>
            </div>

            <div class="field">
              <label class="label" for="turmaId">2. Selecione a Turma</label>
              <select id="turmaId" class="select" name="turmaId" required>
                <option value="" disabled selected>Selecione uma turma</option>
                <c:forEach var="turma" items="${turmas}">
                  <option value="${turma.id}">
                    <c:out value="${turma.disciplina.nome}" /> (<c:out value="${turma.semestre}" />)
                  </option>
                </c:forEach>
              </select>
              <span class="help-text">A turma que receberá este formulário.</span>
            </div>

            <div class="form-footer">
              <a href="avaliacoes?action=listar" class="btn btn-secondary btn-sm">Cancelar</a>
              <button type="submit" class="btn btn-primary btn-sm">Aplicar</button>
            </div>
          </form>
        </section>
      </div>
    </main>
  </body>
</html>