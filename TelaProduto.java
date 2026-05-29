package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TelaProduto extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textPesquisar;

	private JTable table;
	private DefaultTableModel modeloTabela;

	private JLabel lblContador;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				TelaProduto frame = new TelaProduto();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public TelaProduto() {

		setTitle("Sistema de Papelaria");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 500);

		contentPane = new JPanel();
		contentPane.setBackground(new Color(245,245,245));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// TÍTULO
		JLabel lblTitulo = new JLabel("SISTEMA DE PAPELARIA");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
		lblTitulo.setBounds(220, 10, 350, 30);
		contentPane.add(lblTitulo);

		// ID
		JLabel txtId = new JLabel("ID:");
		txtId.setBounds(47, 70, 46, 14);
		contentPane.add(txtId);

		textField = new JTextField();
		textField.setBounds(101, 67, 100, 25);
		contentPane.add(textField);

		textField.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				if(!Character.isDigit(e.getKeyChar())) {
					e.consume();
				}
			}
		});

		// NOME
		JLabel txtNome = new JLabel("Nome:");
		txtNome.setBounds(47, 110, 46, 14);
		contentPane.add(txtNome);

		textField_1 = new JTextField();
		textField_1.setBounds(101, 107, 100, 25);
		contentPane.add(textField_1);

		// PREÇO
		JLabel txtPreco = new JLabel("Preço:");
		txtPreco.setBounds(250, 70, 46, 14);
		contentPane.add(txtPreco);

		textField_2 = new JTextField();
		textField_2.setBounds(320, 67, 100, 25);
		contentPane.add(textField_2);

		// ESTOQUE
		JLabel txtEstoque = new JLabel("Estoque:");
		txtEstoque.setBounds(250, 110, 60, 14);
		contentPane.add(txtEstoque);

		textField_3 = new JTextField();
		textField_3.setBounds(320, 107, 100, 25);
		contentPane.add(textField_3);

		// ================= CADASTRAR =================
		JButton btnCadastrar = new JButton("Cadastrar");
		btnCadastrar.setBackground(new Color(144,238,144));

		btnCadastrar.addActionListener(e -> {

			if(textField.getText().isEmpty()
					|| textField_1.getText().isEmpty()
					|| textField_2.getText().isEmpty()
					|| textField_3.getText().isEmpty()) {

				JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
				return;
			}

			for(int i = 0; i < table.getRowCount(); i++) {
				if(table.getValueAt(i, 0).toString().equals(textField.getText())) {
					JOptionPane.showMessageDialog(null, "ID já cadastrado!");
					return;
				}
			}

			int estoque = Integer.parseInt(textField_3.getText());

			DecimalFormat df = new DecimalFormat("R$ #,##0.00");
			String precoFormatado = df.format(Double.parseDouble(textField_2.getText()));

			modeloTabela.addRow(new Object[]{
					textField.getText(),
					textField_1.getText(),
					precoFormatado,
					textField_3.getText()
			});

			if(estoque < 5) {
				JOptionPane.showMessageDialog(null, "Estoque baixo!");
			}

			atualizarContador();
			limparCampos();
		});

		btnCadastrar.setBounds(47, 160, 120, 30);
		contentPane.add(btnCadastrar);

		// ================= EDITAR =================
		JButton btnEditar = new JButton("Editar");
		btnEditar.setBackground(new Color(173,216,230));

		btnEditar.addActionListener(e -> {

			int linha = table.getSelectedRow();

			if(linha != -1) {

				DecimalFormat df = new DecimalFormat("R$ #,##0.00");

				modeloTabela.setValueAt(textField.getText(), linha, 0);
				modeloTabela.setValueAt(textField_1.getText(), linha, 1);
				modeloTabela.setValueAt(df.format(Double.parseDouble(textField_2.getText())), linha, 2);
				modeloTabela.setValueAt(textField_3.getText(), linha, 3);

				JOptionPane.showMessageDialog(null, "Produto editado!");

			} else {
				JOptionPane.showMessageDialog(null, "Selecione uma linha!");
			}
		});

		btnEditar.setBounds(180, 160, 120, 30);
		contentPane.add(btnEditar);

		// ================= EXCLUIR =================
		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.setBackground(new Color(255,182,193));

		btnExcluir.addActionListener(e -> {

			int linha = table.getSelectedRow();

			if(linha != -1) {
				modeloTabela.removeRow(linha);
				atualizarContador();
			} else {
				JOptionPane.showMessageDialog(null, "Selecione uma linha!");
			}
		});

		btnExcluir.setBounds(313, 160, 120, 30);
		contentPane.add(btnExcluir);

		// ================= LIMPAR =================
		JButton btnLimpar = new JButton("Limpar");
		btnLimpar.addActionListener(e -> limparCampos());

		btnLimpar.setBounds(446, 160, 120, 30);
		contentPane.add(btnLimpar);

		// ================= ORDENAR (CORRIGIDO) =================
		JButton btnOrdenar = new JButton("Ordenar");

		btnOrdenar.addActionListener(e -> {

			ArrayList<Object[]> lista = new ArrayList<>();

			for(int i = 0; i < table.getRowCount(); i++) {
				Object[] row = new Object[4];
				for(int j = 0; j < 4; j++) {
					row[j] = table.getValueAt(i, j);
				}
				lista.add(row);
			}

			lista.sort(Comparator.comparing(o -> o[1].toString().toLowerCase()));

			modeloTabela.setRowCount(0);

			for(Object[] row : lista) {
				modeloTabela.addRow(row);
			}

			JOptionPane.showMessageDialog(null, "Tabela ordenada!");
		});

		btnOrdenar.setBounds(580, 160, 120, 30);
		contentPane.add(btnOrdenar);

		// ================= TABELA =================
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(47, 210, 650, 120);
		contentPane.add(scrollPane);

		table = new JTable();

		modeloTabela = new DefaultTableModel();
		modeloTabela.addColumn("ID");
		modeloTabela.addColumn("Nome");
		modeloTabela.addColumn("Preço");
		modeloTabela.addColumn("Estoque");

		table.setModel(modeloTabela);
		scrollPane.setViewportView(table);

		// ================= PESQUISA =================
		textPesquisar = new JTextField();
		textPesquisar.setBounds(47, 360, 150, 25);
		contentPane.add(textPesquisar);

		JButton btnPesquisar = new JButton("Pesquisar");

		btnPesquisar.addActionListener(e -> {

			String pesquisa = textPesquisar.getText().toLowerCase();

			for(int i = 0; i < table.getRowCount(); i++) {

				String nome = table.getValueAt(i, 1).toString().toLowerCase();

				if(nome.contains(pesquisa)) {
					table.setRowSelectionInterval(i, i);
					break;
				}
			}
		});

		btnPesquisar.setBounds(220, 360, 120, 25);
		contentPane.add(btnPesquisar);

		// ================= TOTAL =================
		JButton btnTotal = new JButton("Total");

		btnTotal.addActionListener(e -> {
			JOptionPane.showMessageDialog(null,
					"Total de produtos: " + table.getRowCount());
		});

		btnTotal.setBounds(360, 360, 120, 25);
		contentPane.add(btnTotal);

		// ================= SOMAR ESTOQUE =================
		JButton btnSomar = new JButton("Somar Estoque");

		btnSomar.addActionListener(e -> {

			double soma = 0;

			for(int i = 0; i < table.getRowCount(); i++) {

				String precoTexto = table.getValueAt(i, 2).toString()
						.replace("R$", "").replace(",", ".").trim();

				double preco = Double.parseDouble(precoTexto);
				int estoque = Integer.parseInt(table.getValueAt(i, 3).toString());

				soma += preco * estoque;
			}

			JOptionPane.showMessageDialog(null,
					"Valor total do estoque: R$ " + soma);
		});

		btnSomar.setBounds(500, 360, 160, 25);
		contentPane.add(btnSomar);

		// ================= CONTADOR =================
		lblContador = new JLabel("Produtos cadastrados: 0");
		lblContador.setBounds(47, 410, 250, 20);
		contentPane.add(lblContador);
	}

	// LIMPAR
	public void limparCampos() {
		textField.setText("");
		textField_1.setText("");
		textField_2.setText("");
		textField_3.setText("");
	}

	// CONTADOR
	public void atualizarContador() {
		lblContador.setText("Produtos cadastrados: " + table.getRowCount());
	}
}