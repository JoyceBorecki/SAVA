<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="${empty formulario.id ? 'Novo Formulário' : 'Editar Formulário'}" />
<c:set var="currentNav" value="admin_formularios" />

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
          <p class="page-subtitle">Preencha os dados do modelo de avaliação.</p>
        </section>

        <section class="card card-padding">
          <form class="form-grid" action="formularios" method="post" novalidate>
            
            <input type="hidden" name="id" value="<c:out value='${formulario.id}' />" />

            <%-- Campo Título --%>
            <div class="field">
              <label class="label" for="titulo">Título do Formulário</label>
              <input id="titulo" class="input" type="text" name="titulo"
                     placeholder="Ex.: Avaliação Docente" 
                     value="<c:out value='${formulario.titulo}' />" 
                     required />
              <span class="help-text">Nome de identificação do formulário.</span>
            </div>

            <%-- Campo Processo Avaliativo (Dropdown) --%>
            <div class="field">
              <label class="label" for="processoId">Processo Avaliativo</label>
              <select id="processoId" class="select" name="processoId" required>
                <option value="" disabled ${empty formulario.processoAvaliativo ? 'selected' : ''}>
                  Selecione o processo
                </option>
                <c:forEach var="processo" items="${processos}">
                  <option value="${processo.id}" 
                          ${processo.id == formulario.processoAvaliativo.id ? 'selected' : ''}>
                    <c:out value="${processo.nome}" />
                  </option>
                </c:forEach>
              </select>
              <span class="help-text">Define a qual período este formulário pertence.</span>
            </div>

            <%-- Campo Anônimo (Checkbox) --%>
            <div class="field">
              <label class="label">Configuração de Anonimato (RF11)</label>
              <div class="choice-group">
                <label class="option-control checkbox" style="margin-top: 8px;">
                  <input type="checkbox" name="anonimo" 
                         ${formulario.anonimo ? 'checked' : ''} />
                  <span class="option-label">Formulário Anônimo</span>
                </label>
              </div>
              <span class="help-text">Se marcado, as respostas não serão vinculadas ao nome do aluno.</span>
            </div>
            
            <%-- Campo Perfis Destinados (Checkboxes) (RF07) --%>
            <div class="field">
              <label class="label">Perfis Destinados (RF07)</label>
              <div class="choice-group">
                <%-- Função JSTL para verificar se um perfil está na lista --%>
                <c:forEach var="perfil" items="${perfis}">
                  <c:set var="perfilSelecionado" value="false" />
                  <c:forEach var="perfilForm" items="${formulario.perfisDestinados}">
                    <c:if test="${perfil.id == perfilForm.id}">
                      <c:set var="perfilSelecionado" value="true" />
                    </c:if>
                  </c:forEach>
                  
                  <label class="option-control checkbox">
                    <input type="checkbox" name="perfisDestinados" value="${perfil.id}"
                           ${perfilSelecionado ? 'checked' : ''} />
                    <span class="option-label"><c:out value="${perfil.nome}" /></span>
                  </label>
                </c:forEach>
              </div>
              <span class="help-text">Selecione quais perfis poderão responder este formulário.</span>
            </div>

              <div class="field">
                  <label class="label">Turmas que receberão este formulário</label>
                  <div class="choice-group">
                      <c:forEach var="turma" items="${turmas}">
                          <c:set var="selecionada" value="false" />
                          <c:forEach var="av" items="${formulario.avaliacoes}">
                              <c:if test="${av.turma.id == turma.id}">
                                  <c:set var="selecionada" value="true" />
                              </c:if>
                          </c:forEach>
                          <label class="option-control checkbox">
                              <input type="checkbox" name="turmasAplicadas" value="${turma.id}"${selecionada ? "checked" : ""}/>
                              <span class="option-label">
                                ${turma.disciplina.nome} — ${turma.semestre}
                              </span>
                          </label>
                      </c:forEach>
                  </div>
                  <span class="help-text">Selecione as turmas que receberão este formulário.</span>
              </div>
            
            <%-- Botões de Ação --%>
            <div class="form-footer">
              <a href="formularios?action=listar" class="btn btn-secondary btn-sm">Cancelar</a>
              <button type="submit" class="btn btn-primary btn-sm">Salvar</button>
            </div>
            
          </form>
        </section>
      </div>
    </main>
  </body>
</html>