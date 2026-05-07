# 🚀 Plataforma de Rastreamento de Qualidade (QA Track)

Sistema de rastreamento de bugs e demandas inspirado em ferramentas como Jira, Azure Boards e GitLab Issues.

---

## 📌 Visão Geral

A **Plataforma de Rastreamento de Qualidade** é um sistema focado no ciclo de vida de desenvolvimento de software e no processo de **Quality Assurance (QA)**.

O objetivo é resolver um problema real enfrentado por equipes de desenvolvimento: o controle eficiente de demandas com regras claras de responsabilidade (*ownership*), fluxo de status e auditoria.

O projeto foi organizado em classes separadas para modelagem, regras de negócio e execução, usando herança, encapsulamento, polimorfismo e coleções no domínio de tickets.

---

## ❗ Problema que o sistema resolve

* Perda de controle de bugs e melhorias quando informações ficam espalhadas
* Falta de regras de transição de status, gerando retrabalho
* Ausência de histórico, dificultando auditoria e análise de incidentes

---

## 🎯 Objetivo

Criar uma plataforma simples e eficiente para:

* Registrar e organizar tickets
* Controlar o fluxo de desenvolvimento
* Garantir regras claras de transição
* Manter histórico completo das alterações

---

## 🧱 Modelagem Orientada a Objetos

### 🎫 Ticket (Classe Abstrata)

Atributos:

* id
* título
* descrição
* prioridade
* status
* criador
* responsável
* histórico
* datas

### Tipos de Ticket

* 🐞 **Bug**

  * passos para reproduzir
  * ambiente

* ✨ **Feature**

  * valor de negócio

* 🔧 **Melhoria**

  * área impactada

---

### 👤 Usuário (Classe Abstrata)

Atributos:

* id
* nome

Subclasses:

* **Gestor**
* **Desenvolvedor**
* **QA**

Cada classe sobrescreve o método:

```java
getTipo()
```

---

### ⚙️ Serviço Principal

#### `SistemaTickets`

Responsável por:

* Criar tickets
* Atribuir responsáveis
* Controlar transições de status
* Validar regras de negócio

---

## 🗂️ Estrutura do Projeto

* `src/sistema/model` → entidades do sistema, como `Ticket`, `Bug`, `Feature`, `Melhoria` e os tipos de usuário
* `src/sistema/service` → regras de negócio e operações do sistema
* `src/sistema/app` → ponto de entrada da aplicação

---

### 🗂️ Estruturas de Dados

* Uso de **coleções**:

  * `List<Ticket>` para tickets
  * `List<String>` para histórico

* Limites fixos na versão atual:

  * até 100 tickets cadastrados
  * até 100 eventos por histórico de ticket

---

## 📏 Regras de Negócio (MVP)

* Todo ticket inicia com status **ABERTO**
* Apenas **GESTOR** pode atribuir responsáveis
* Responsável deve ser um **DESENVOLVEDOR**

### 🔄 Fluxo de Status

* `ABERTO / REABERTO → EM_DESENVOLVIMENTO` *(somente DEV)*
* `EM_DESENVOLVIMENTO → EM_TESTE` *(somente DEV)*
* `EM_TESTE → RESOLVIDO / REABERTO` *(somente QA)*

---

### 🧾 Histórico

Toda mudança:

* Registra autor
* Novo status
* Observação

---

### ⚠️ Tratamento de Erros

* Sem exceções customizadas
* A entrada inválida no menu é tratada com `try-catch`
* Operações inválidas retornam:

  * `false` ou `null`

---

## 🧠 Conceitos de POO Aplicados

* **Abstração** → Classe base `Ticket`
* **Encapsulamento** → Regras controladas no serviço
* **Polimorfismo** → Tratamento uniforme dos tipos de ticket
* **Herança** → Especialização de `Bug`, `Feature`, `Melhoria`
* **Coesão** → Separação entre entidades e regras de negócio
* **Sobrecarga** → Métodos com diferentes assinaturas
* **Sobreescrita** → `toString()` e `getTipo()`
* **Manipulação de Coleções** → Estrutura de dados principal

---

## 🧪 Cenário de Uso (MVP)

1. Gestor cria um bug crítico
2. Gestor atribui para um desenvolvedor
3. Desenvolvedor corrige e envia para teste
4. QA reprova e reabre o ticket
5. Desenvolvedor corrige novamente
6. QA valida e resolve

📌 Resultado: histórico completo garantindo rastreabilidade do incidente

---

## ▶️ Como Obter e Executar o Projeto


1. Clone o repositório:

```bash
git clone https://github.com/rafael-vas/qa-track.git
```

2. Entre na pasta do projeto:

```bash
cd qa-track
```

3. Abra o projeto no VS Code ou em outra IDE Java.

4. Execute a classe principal `sistema.app.Main`.

Ao iniciar, o sistema pede o cadastro inicial de um Gestor, um Desenvolvedor e um QA. Depois disso, o menu da versão atual libera as funcoes basicas da entrega inicial: criar bug, listar tickets, atribuir responsavel, alterar status e consultar historico.

O fluxo da aplicação é totalmente em console: entrada do usuário, processamento das regras de negócio no serviço e saída com o resultado na tela.

As classes `Feature` e `Melhoria` já existem na modelagem e no serviço, mas ainda nao estao expostas no menu de console.

Também é possível executar pelo terminal, se o JDK estiver instalado, compilando apenas os arquivos usados pela aplicação principal:

```bash
mkdir out
javac -d out src/sistema/model/*.java src/sistema/service/SistemaTickets.java src/sistema/app/Main.java
java -cp out sistema.app.Main
```

Se a IDE pedir um ponto de entrada, use `sistema.app.Main` como classe principal.

## ✅ O que esta implementado nesta versao

* Uso de `ArrayList` para tickets e histórico
* Menu funcional com tratamento de entrada inválida
* Criacao de bugs com prioridade, passos para reproduzir e ambiente
* Listagem de tickets cadastrados
* Atribuicao de responsavel pelo gestor
* Transicao de status com validacao de perfil e fluxo
* Consulta de historico do ticket
* Registro de data de criacao, ultima atualizacao e eventos do historico

---

## 🔮 Melhorias Futuras

* Persistência de dados (H2 ou SQLite)
* Filtros por status, prioridade e responsável
* SLA por prioridade
* Relatórios de produtividade:

  * Lead time
  * Taxa de reabertura
  * Taxa de aprovação
* API REST com Spring Boot
* Interface web

---

## 👨‍💻 Integrantes

* Thiago Felipe
* Luiz Gustavo
* Rafael Vasconcelos

---

## 📄 Licença

Projeto acadêmico desenvolvido para fins educacionais.