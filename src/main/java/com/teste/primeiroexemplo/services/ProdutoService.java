package com.teste.primeiroexemplo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.teste.primeiroexemplo.model.Produto;
import com.teste.primeiroexemplo.model.exception.ResourceNotFoundException;
import com.teste.primeiroexemplo.repository.ProdutoRepository;
import com.teste.primeiroexemplo.shared.ProdutoDTO;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    /**
     * Método para retornar uma lista de produtos
     * 
     * @return Lista de produtos.
     */
    public List<ProdutoDTO> obterTodos() {
        // Colocar regra caso tenha

        // Retorna uma lista de produto model.
        List<Produto> produtos = produtoRepository.findAll();

        return produtos.stream()
                .map(produto -> new ModelMapper().map(produto, ProdutoDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Metodo que retorna o produto encontrado pelo seu Id.
     * 
     * @param id do produto que será localizado.
     * @return Retorna um produto caso seja encontrado.
     */
    public Optional<ProdutoDTO> obterPorId(Integer id) {

        // Obtendo produto pelo id
        Optional<Produto> produto = produtoRepository.findById(id);

        // Se não econtrar, lança excpetion
        if (produto.isEmpty()) {
            throw new ResourceNotFoundException("Produto com id: " + id + " não encontrado");
        }

        // Convertendo o meu optional de produto em um produtoDTO
        ProdutoDTO dto = new ModelMapper().map(produto.get(), ProdutoDTO.class);

        // Criando e retornando um optional de produtoDTO
        return Optional.of(dto);
    }

    /**
     * Metodo para adicionar produto na lista.
     * 
     * @param produto que será adicionado.
     * @return Retorna o produto que foi adicionado na lista.
     */
    public ProdutoDTO adicionar(ProdutoDTO produtoDTO) {
        // Poderia ter alguma regra de negócio aqui para validar o produto.

        // Removendo o id para conseguir fazer o cadastro
        produtoDTO.setId(null);

        // Criar um objeto de mapeamento.
        ModelMapper mapper = new ModelMapper();

        // Converter o nosso produtoDTO em um produto.
        Produto produto = mapper.map(produtoDTO, Produto.class);

        // Salvar o Produto no banco.
        produto = produtoRepository.save(produto);

        produtoDTO.setId(produto.getId());

        // Retornar o ProdutoDTO atualizado.
        return produtoDTO;

    }

    /**
     * Metodo para deletar o produto por id.
     * 
     * @param id do produto a ser deletado.
     */
    public void deletar(Integer id) {

        // Verificar se o produto existe
        Optional<Produto> produto = produtoRepository.findById(id);

        // Se não existir lança uma exception
        if (produto.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Não foi possível deletar o produto com o id: " + id + " - Produto não existe");
        }

        // Deleta o produto pelo id
        produtoRepository.deleteById(id);
    }

    /**
     * Metodo para atualizar o produto na lista.
     * 
     * @param produto que será atualizado.
     * @param id      do produto.
     * @return Retorna o produto após atualizar na lista.
     */
    public ProdutoDTO atualizar(Integer id, ProdutoDTO produtoDTO) {

        // Passar o id para o produtoDTO
        produtoDTO.setId(id);

        // Criar um objeto de mapeamento
        ModelMapper mapper = new ModelMapper();

        // Converter o produtoDTO em um Produto
        Produto produto = mapper.map(produtoDTO, Produto.class);

        // Atualizar o produto no Banco de dados
        produtoRepository.save(produto);

        // Retornar o produtoDTO atualizado
        return produtoDTO;

    }

}
