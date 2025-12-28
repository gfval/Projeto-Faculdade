# Projeto: Sistema de Vendas (Java - POO)

Este projeto exemplifica a **análise e planejamento orientados a objetos** solicitados: identificação de entidades do domínio, responsabilidade única (Single Responsibility Principle), abstração usando interfaces, e modelagem (UML). É um projeto Java simples (Maven) em modo console com arquitetura em camadas: `model`, `repository`, `service`, `app`.

## Estrutura do projeto

```
projeto-vendas/
├── pom.xml
└── src
    ├── main
    │   └── java
    │       └── com.example.vendas
    │           ├── app
    │           │   └── Main.java
    │           ├── model
    │           │   ├── Cliente.java
    │           │   ├── Produto.java
    │           │   ├── Pedido.java
    │           │   └── PedidoItem.java
    │           ├── repository
    │           │   ├── IRepository.java
    │           │   ├── ClienteRepositoryInMemory.java
    │           │   ├── ProdutoRepositoryInMemory.java
    │           │   └── PedidoRepositoryInMemory.java
    │           └── service
    │               ├── ClienteService.java
    │               ├── ProdutoService.java
    │               └── PedidoService.java
    └── test
        └── java
            └── com.example.vendas
                └── service
                    └── PedidoServiceTest.java
```

---

## pom.xml

```
xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>projeto-vendas</artifactId>
    <version>1.0-SNAPSHOT</version>
    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    <dependencies>
        <!-- JUnit para testes -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.9.3</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.10.1</version>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.0.0-M7</version>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## Código-fonte

### src/main/java/com/example/vendas/model/Cliente.java
```java
package com.example.vendas.model;

import java.util.Objects;

public class Cliente {
    private final String id;
    private String nome;
    private String email;

    public Cliente(String id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Cliente{" + "id='" + id + '\'' + ", nome='" + nome + '\'' + ", email='" + email + '\'' + '}';
    }
}
```

---

### src/main/java/com/example/vendas/model/Produto.java
```java
package com.example.vendas.model;

import java.util.Objects;

public class Produto {
    private final String sku;
    private String nome;
    private double preco;

    public Produto(String sku, String nome, double preco) {
        this.sku = sku;
        this.nome = nome;
        this.preco = preco;
    }

    public String getSku() { return sku; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Objects.equals(sku, produto.sku);
    }

    @Override
    public int hashCode() { return Objects.hash(sku); }

    @Override
    public String toString() {
        return "Produto{" + "sku='" + sku + '\'' + ", nome='" + nome + '\'' + ", preco=" + preco + '}';
    }
}
```

---

### src/main/java/com/example/vendas/model/PedidoItem.java
```java
package com.example.vendas.model;

public class PedidoItem {
    private final Produto produto;
    private final int quantidade;

    public PedidoItem(Produto produto, int quantidade) {
        if (quantidade <= 0) throw new IllegalArgumentException("Quantidade deve ser > 0");
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public Produto getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }
    public double getSubtotal() { return produto.getPreco() * quantidade; }
}
```

---

### src/main/java/com/example/vendas/model/Pedido.java
```java
package com.example.vendas.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Pedido {
    private final String id;
    private final Cliente cliente;
    private final LocalDateTime criadoEm;
    private final List<PedidoItem> itens = new ArrayList<>();

    public Pedido(String id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
        this.criadoEm = LocalDateTime.now();
    }

    public String getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public List<PedidoItem> getItens() { return Collections.unmodifiableList(itens); }

    // Responsabilidade da classe: representar o pedido e permitir adicionar itens
    public void adicionarItem(PedidoItem item) {
        itens.add(item);
    }

    public double calcularTotal() {
        return itens.stream().mapToDouble(PedidoItem::getSubtotal).sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return Objects.equals(id, pedido.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Pedido{" + "id='" + id + '\'' + ", cliente=" + cliente + ", total=" + calcularTotal() + '}';
    }
}
```

---

## Repositórios (abstração + implementação in-memory)

### src/main/java/com/example/vendas/repository/IRepository.java
```java
package com.example.vendas.repository;

import java.util.Optional;
import java.util.List;

public interface IRepository<T, ID> {
    T save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void deleteById(ID id);
}
```

---

### src/main/java/com/example/vendas/repository/ClienteRepositoryInMemory.java
```java
package com.example.vendas.repository;

import com.example.vendas.model.Cliente;

import java.util.*;

public class ClienteRepositoryInMemory implements IRepository<Cliente, String> {
    private final Map<String, Cliente> storage = new LinkedHashMap<>();

    @Override
    public Cliente save(Cliente entity) {
        storage.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<Cliente> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Cliente> findAll() { return new ArrayList<>(storage.values()); }

    @Override
    public void deleteById(String id) { storage.remove(id); }
}
```

---

### src/main/java/com/example/vendas/repository/ProdutoRepositoryInMemory.java
```java
package com.example.vendas.repository;

import com.example.vendas.model.Produto;

import java.util.*;

public class ProdutoRepositoryInMemory implements IRepository<Produto, String> {
    private final Map<String, Produto> storage = new LinkedHashMap<>();

    @Override
    public Produto save(Produto entity) {
        storage.put(entity.getSku(), entity);
        return entity;
    }

    @Override
    public Optional<Produto> findById(String sku) { return Optional.ofNullable(storage.get(sku)); }

    @Override
    public List<Produto> findAll() { return new ArrayList<>(storage.values()); }

    @Override
    public void deleteById(String sku) { storage.remove(sku); }
}
```

---

### src/main/java/com/example/vendas/repository/PedidoRepositoryInMemory.java
```java
package com.example.vendas.repository;

import com.example.vendas.model.Pedido;

import java.util.*;

public class PedidoRepositoryInMemory implements IRepository<Pedido, String> {
    private final Map<String, Pedido> storage = new LinkedHashMap<>();

    @Override
    public Pedido save(Pedido entity) {
        storage.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<Pedido> findById(String id) { return Optional.ofNullable(storage.get(id)); }

    @Override
    public List<Pedido> findAll() { return new ArrayList<>(storage.values()); }

    @Override
    public void deleteById(String id) { storage.remove(id); }
}
```

---

## Serviços (responsabilidade: regras de negócio)

### src/main/java/com/example/vendas/service/ClienteService.java
```java
package com.example.vendas.service;

import com.example.vendas.model.Cliente;
import com.example.vendas.repository.IRepository;

import java.util.List;
import java.util.Optional;

public class ClienteService {
    private final IRepository<Cliente, String> clienteRepo;

    public ClienteService(IRepository<Cliente, String> clienteRepo) {
        this.clienteRepo = clienteRepo;
    }

    public Cliente criarCliente(Cliente cliente) {
        // Validações simples
        if (cliente.getNome() == null || cliente.getNome().isBlank())
            throw new IllegalArgumentException("Nome do cliente é obrigatório");
        return clienteRepo.save(cliente);
    }

    public Optional<Cliente> buscarPorId(String id) { return clienteRepo.findById(id); }
    public List<Cliente> listarTodos() { return clienteRepo.findAll(); }
}
```

---

### src/main/java/com/example/vendas/service/ProdutoService.java
```java
package com.example.vendas.service;

import com.example.vendas.model.Produto;
import com.example.vendas.repository.IRepository;

import java.util.List;
import java.util.Optional;

public class ProdutoService {
    private final IRepository<Produto, String> produtoRepo;

    public ProdutoService(IRepository<Produto, String> produtoRepo) { this.produtoRepo = produtoRepo; }

    public Produto criarProduto(Produto p) {
        if (p.getPreco() < 0) throw new IllegalArgumentException("Preço inválido");
        return produtoRepo.save(p);
    }

    public Optional<Produto> buscarPorSku(String sku) { return produtoRepo.findById(sku); }
    public List<Produto> listarTodos() { return produtoRepo.findAll(); }
}
```

---

### src/main/java/com/example/vendas/service/PedidoService.java
```java
package com.example.vendas.service;

import com.example.vendas.model.Cliente;
import com.example.vendas.model.Pedido;
import com.example.vendas.model.PedidoItem;
import com.example.vendas.model.Produto;
import com.example.vendas.repository.IRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PedidoService {
    private final IRepository<Pedido, String> pedidoRepo;
    private final IRepository<Cliente, String> clienteRepo;
    private final IRepository<Produto, String> produtoRepo;

    public PedidoService(IRepository<Pedido, String> pedidoRepo,
                         IRepository<Cliente, String> clienteRepo,
                         IRepository<Produto, String> produtoRepo) {
        this.pedidoRepo = pedidoRepo;
        this.clienteRepo = clienteRepo;
        this.produtoRepo = produtoRepo;
    }

    public Pedido criarPedido(String clienteId) {
        Cliente cliente = clienteRepo.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        Pedido pedido = new Pedido(UUID.randomUUID().toString(), cliente);
        return pedidoRepo.save(pedido);
    }

    public Pedido adicionarItemAoPedido(String pedidoId, String sku, int quantidade) {
        Pedido pedido = pedidoRepo.findById(pedidoId).orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
        Produto produto = produtoRepo.findById(sku).orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        PedidoItem item = new PedidoItem(produto, quantidade);
        pedido.adicionarItem(item);
        pedidoRepo.save(pedido); // atualizar
        return pedido;
    }

    public Optional<Pedido> buscarPorId(String id) { return pedidoRepo.findById(id); }
    public List<Pedido> listarTodos() { return pedidoRepo.findAll(); }
}
```

---

## Aplicação principal (exemplo de uso)

### src/main/java/com/example/vendas/app/Main.java
```java
package com.example.vendas.app;

import com.example.vendas.model.Cliente;
import com.example.vendas.model.Produto;
import com.example.vendas.repository.*;
import com.example.vendas.service.*;

public class Main {
    public static void main(String[] args) {
        // Instanciar repositórios in-memory
        ClienteRepositoryInMemory clienteRepo = new ClienteRepositoryInMemory();
        ProdutoRepositoryInMemory produtoRepo = new ProdutoRepositoryInMemory();
        PedidoRepositoryInMemory pedidoRepo = new PedidoRepositoryInMemory();

        // Injetar nas services
        ClienteService clienteService = new ClienteService(clienteRepo);
        ProdutoService produtoService = new ProdutoService(produtoRepo);
        PedidoService pedidoService = new PedidoService(pedidoRepo, clienteRepo, produtoRepo);

        // Criar dados de exemplo
        Cliente c1 = new Cliente("cli-001", "João Silva", "joao@example.com");
        clienteService.criarCliente(c1);

        Produto p1 = new Produto("sku-001", "Cafeteira", 199.90);
        Produto p2 = new Produto("sku-002", "Xícara", 9.90);
        produtoService.criarProduto(p1);
        produtoService.criarProduto(p2);

        // Criar pedido
        var pedido = pedidoService.criarPedido("cli-001");
        pedidoService.adicionarItemAoPedido(pedido.getId(), "sku-001", 1);
        pedidoService.adicionarItemAoPedido(pedido.getId(), "sku-002", 4);

        System.out.println("Pedido criado: " + pedido);
        System.out.println("Itens:");
        pedido.getItens().forEach(i -> System.out.printf(" - %s x%d => subtotal: %.2f\n", i.getProduto().getNome(), i.getQuantidade(), i.getSubtotal()));
        System.out.printf("Total: %.2f\n", pedido.calcularTotal());
    }
}
```

---

## Teste unitário de exemplo

### src/test/java/com/example/vendas/service/PedidoServiceTest.java
```java
package com.example.vendas.service;

import com.example.vendas.model.Cliente;
import com.example.vendas.model.Produto;
import com.example.vendas.repository.*;
import com.example.vendas.model.Pedido;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PedidoServiceTest {
    private ClienteRepositoryInMemory clienteRepo;
    private ProdutoRepositoryInMemory produtoRepo;
    private PedidoRepositoryInMemory pedidoRepo;
    private PedidoService pedidoService;

    @BeforeEach
    public void setup() {
        clienteRepo = new ClienteRepositoryInMemory();
        produtoRepo = new ProdutoRepositoryInMemory();
        pedidoRepo = new PedidoRepositoryInMemory();
        pedidoService = new PedidoService(pedidoRepo, clienteRepo, produtoRepo);

        clienteRepo.save(new Cliente("cli-001", "Teste", "t@t.com"));
        produtoRepo.save(new Produto("sku-001", "Produto Teste", 10.0));
    }

    @Test
    public void criarPedidoEAdicionarItem_deveCalcularTotalCorreto() {
        Pedido pedido = pedidoService.criarPedido("cli-001");
        pedidoService.adicionarItemAoPedido(pedido.getId(), "sku-001", 3);
        Assertions.assertEquals(30.0, pedido.calcularTotal(), 0.0001);
    }
}
```

---

## UML (PlantUML) — diagrama de classes

```plantuml
@startuml
package model {
  class Cliente { - id: String\n- nome: String\n- email: String }
  class Produto { - sku: String\n- nome: String\n- preco: double }
  class Pedido { - id: String\n- cliente: Cliente\n- itens: List<PedidoItem>\n+ adicionarItem(item)\n+ calcularTotal(): double }
  class PedidoItem { - produto: Produto\n- quantidade: int\n+ getSubtotal(): double }
}

package repository {
  interface IRepository<T,ID>
  class ClienteRepositoryInMemory
  class ProdutoRepositoryInMemory
  class PedidoRepositoryInMemory
}

package service {
  class ClienteService
  class ProdutoService
  class PedidoService
}

Cliente "1" -- "*" Pedido : faz
Pedido "1" o-- "*" PedidoItem : possui
PedidoItem --> Produto
ClienteRepositoryInMemory ..|> IRepository
ProdutoRepositoryInMemory ..|> IRepository
PedidoRepositoryInMemory ..|> IRepository
@enduml

'''
## Observações de projeto e princípios aplicados

- **Identificação de entidades**: `Cliente`, `Produto`, `Pedido`, `PedidoItem` — cada uma modelando conceitos do domínio de vendas.
- **Single Responsibility Principle (SRP)**: cada classe tem responsabilidade única:
  - Models: apenas representam dados e comportamento direto do domínio (ex.: `Pedido.calcularTotal`).
  - Repositórios: responsabilidade de armazenamento (in-memory neste exemplo).
  - Services: regras de negócio e validações.
- **Abstração**: `IRepository` define contrato genérico; implementações in-memory seguem esse contrato. Serviços dependem da abstração (`IRepository`) — facilita substituição por repositórios reais (ex.: JPA).
- **Encapsulamento**: atributos privados/imutáveis quando apropriado; acesso via getters/setters controlados.
- **Polimorfismo**: a interface `IRepository` permite múltiplas implementações (in-memory, JDBC, JPA).

---

## Como executar
1. Tenha Java 17+ e Maven instalados.
2. Na raiz do projeto, execute:

```bash
mvn clean package
java -cp target/projeto-vendas-1.0-SNAPSHOT.jar com.example.vendas.app.Main
```

(Como alternativa, importe o projeto num IDE como IntelliJ ou VS Code e execute `Main`.)
'''

