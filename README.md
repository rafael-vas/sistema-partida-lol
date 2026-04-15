# 🚀 Plataforma de Rastreamento de Qualidade (QA Track)

Sistema de rastreamento de bugs e demandas inspirado em ferramentas como Jira, Azure Boards e GitLab Issues.

---

## 📌 Visão Geral

A **Plataforma de Rastreamento de Qualidade** é um sistema focado no ciclo de vida de desenvolvimento de software e no processo de **Quality Assurance (QA)**.

O objetivo é resolver um problema real enfrentado por equipes de desenvolvimento: o controle eficiente de demandas com regras claras de responsabilidade (*ownership*), fluxo de status e auditoria.

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

### 🗂️ Estruturas de Dados

* Uso de **vetores de objetos (arrays)**:

  * `Ticket[]`
  * `String[]` (histórico)

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
* **Manipulação de Arrays** → Estrutura de dados principal

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