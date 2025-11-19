# SAVA – Sistema de Avaliação Acadêmica

Sistema web para avaliação institucional de disciplinas, onde alunos respondem formulários vinculados às turmas em que estão matriculados. O sistema gerencia todo o processo avaliativo, permite avaliações anônimas ou identificadas, suporta múltiplos perfis de usuário e gera relatórios.

---

## Objetivo

Este projeto foi desenvolvido como trabalho prático da disciplina **DS142 – Linguagem de Programação Orientada a Objetos II**, com os seguintes objetivos:

- Aplicar Servlets, JSP e Hibernate
- Desenvolver um fluxo completo de avaliação acadêmica
- Implementar autenticação, autorização e controle de acesso
- Criar formulários dinâmicos com questões abertas e fechadas
- Tratar avaliações identificadas e anônimas
- Gerar relatórios estatísticos
- Utilizar Docker e Tomcat na implantação e execução

---

## Integrantes do Grupo

- **Joyce Adriana Borecki**
- **Victoria Isabele Corbolin Monte**

---

## Como Executar

O projeto utiliza Docker, MySQL, Maven e Tomcat.

1. Clone o repositório
2. Compile o projeto com Maven
3. Suba o banco de dados com Docker
4. Inicie o Tomcat
5. Acesse o sistema no navegador

As configurações de login e senha do banco de dados estão definidas nos arquivos de configuração do projeto.

O sistema popula automaticamente o banco com dados iniciais, como perfis, usuários, cursos, disciplinas, turmas e processos avaliativos. A execução inicial já cria registros suficientes para testar o sistema.

---

## Tecnologias Utilizadas

- Java 17
- Jakarta Servlet
- JSP
- Hibernate ORM
- MySQL
- Docker
- Tomcat
- JUnit

---

## Funcionalidades Principais

### Gestão de Usuários e Acesso
- Perfis de aluno, professor, coordenador e administrador
- Autenticação e controle de acesso
- Registro de quem respondeu cada avaliação

### Formulários de Avaliação
- Questões de múltipla escolha (resposta única ou múltipla)
- Questões abertas
- Configuração de obrigatoriedade
- Definição de formulário identificado ou anônimo
- Associação ao processo avaliativo

### Aplicação das Avaliações
- Acesso restrito às turmas em que o aluno está matriculado
- Uma única resposta por aluno, com edição permitida em formulários identificados
- Registro de participação anônima sem vincular identidade às respostas

### Relatórios e Estatísticas
- Relatórios por curso, disciplina, turma e professor
- Estatísticas automáticas de questões fechadas
- Score por proposição
- Respostas abertas visíveis apenas ao avaliado e à coordenação do curso