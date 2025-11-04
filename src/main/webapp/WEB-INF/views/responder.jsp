<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Avaliação da Disciplina — Sistema de Avaliação Acadêmica" />
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
      
        <form action="responder" method="post" novalidate>
          <%-- ID da Avaliação (o "O quê" + "Para quem") --%>
          <input type="hidden" name="avaliacaoId" value="${avaliacao.id}" />

          <section class="mb-4">
            <h1 class="page-title"><c:out value="${formulario.titulo}"/></h1>
            <p class="page-subtitle">
              <c:out value="${avaliacao.turma.disciplina.nome}"/>
              <%-- Lista de Professores --%>
              <c:forEach var="prof" items="${avaliacao.turma.professores}">
                — Professor(a): <c:out value="${prof.nome}"/>
              </c:forEach>
            </p>
          </section>

          <%-- Loop principal sobre as Questões --%>
          <c:forEach var="questao" items="${questoes}" varStatus="loop">
            <%-- ID da Questão (para o servlet saber qual é qual) --%>
            <input type="hidden" name="questaoId" value="${questao.id}" />
            
            <section class="card card-padding section">
              <h3>
                Questão ${loop.count} 
                <c:if test="${questao.obrigatoria}"> (Obrigatória)</c:if>
              </h3>
              <p style="font-size: 16px; color: var(--text-heading); margin-top: 4px;">
                <c:out value="${questao.enunciado}"/>
              </p>
              
              <div class="form-grid" style="margin-top: 20px;">
                
                <%-- Renderiza o tipo de input correto --%>
                <c:choose>
                  
                  <%-- TIPO: ABERTA (RF09) [cite: 98-99] --%>
                  <c:when test="${questao.tipo == 'ABERTA'}">
                    <div class="field">
                      <textarea id="resposta_q_${questao.id}" 
                                name="resposta_q_${questao.id}" 
                                class="textarea" 
                                placeholder="Digite sua resposta aqui"
                                ${questao.obrigatoria ? 'required' : ''}></textarea>
                    </div>
                  </c:when>

                  <%-- TIPO: RESPOSTA ÚNICA (RF08) [cite: 89-93] --%>
                  <c:when test="${questao.tipo == 'UNICA'}">
                    <div class="field">
                      <div class="choice-group" role="radiogroup" aria-label="Questão ${loop.count}">
                        <c:forEach var="alt" items="${questao.alternativas}">
                          <label class="option-control">
                            <input type="radio" 
                                   name="resposta_q_${questao.id}" 
                                   value="${alt.id}" 
                                   ${questao.obrigatoria ? 'required' : ''} />
                            <span class="option-label"><c:out value="${alt.texto}"/></span>
                          </label>
                        </c:forEach>
                      </div>
                    </div>
                  </c:when>
                  
                  <%-- TIPO: MÚLTIPLA ESCOLHA (RF08) [cite: 94-97] --%>
                  <c:when test="${questao.tipo == 'MULTIPLA'}">
                    <div class="field">
                      <div class="choice-group" role="group" aria-label="Questão ${loop.count}">
                        <c:forEach var="alt" items="${questao.alternativas}">
                          <label class="option-control checkbox">
                            <input type="checkbox" 
                                   name="resposta_q_${questao.id}" 
                                   value="${alt.id}" />
                            <span class="option-label"><c:out value="${alt.texto}"/></span>
                          </label>
                        </c:forEach>
                      </div>
                    </div>
                  </c:when>
                </c:choose>
                
              </div>
            </section>
          </c:forEach>
          
          <%-- Botões de envio --%>
          <section class="form-footer" style="padding: 24px 0;">
            <a href="dashboard" class="btn btn-secondary btn-sm">Cancelar e Voltar</a>
            <%-- <button type="submit" class="btn btn-outline btn-sm">Salvar Rascunho</button> --%>
            <button type="submit" class="btn btn-primary btn-sm">Enviar Avaliação</button>
          </section>
        
        </form>
      </div>
    </main>
  </body>
</html>