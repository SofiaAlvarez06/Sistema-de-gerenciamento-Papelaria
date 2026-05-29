package controller;

import java.util.ArrayList;
import model.Produto;

public class ProdutoController {

    private ArrayList<Produto> listaProdutos;

    public ProdutoController() {

        listaProdutos = new ArrayList<>();

    }

    public void adicionarProduto(
            Produto produto) {

        listaProdutos.add(produto);

    }

    public ArrayList<Produto> listarProdutos() {

        return listaProdutos;

    }

    public void removerProduto(int index) {

        if(index >= 0 &&
                index < listaProdutos.size()) {

            listaProdutos.remove(index);

        }

    }

}