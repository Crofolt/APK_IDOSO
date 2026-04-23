package com.mesawa.cuidarproximo.cadastros

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mesawa.cuidarproximo.R

class CadastroContaFragment : Fragment() {

    private lateinit var nomeResponsavel: EditText
    private lateinit var telefone: EditText
    private lateinit var email: EditText
    private lateinit var senha: EditText
    private lateinit var confirmSenha: EditText
    private lateinit var btnContinuar: Button
    private lateinit var txtEmailStatus: TextView
    private lateinit var txtSenhaStatus: TextView

    private lateinit var viewModel: CadastroContaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_cadastro_conta, container, false)

        viewModel = ViewModelProvider(requireActivity())[CadastroContaViewModel::class.java]

        nomeResponsavel = view.findViewById(R.id.editTextNomeResponsavel)
        telefone = view.findViewById(R.id.editTextTelefone)
        email = view.findViewById(R.id.editTextEmail)
        senha = view.findViewById(R.id.editTextPassword)
        confirmSenha = view.findViewById(R.id.editTextConfirmPassword)
        btnContinuar = view.findViewById(R.id.buttonContinuar)
        txtEmailStatus = view.findViewById(R.id.txtEmailStatus)
        txtSenhaStatus = view.findViewById(R.id.txtSenhaStatus)

        // Observando o status do cadastro
        viewModel.cadastroStatus.observe(viewLifecycleOwner, Observer { status ->
            when (status) {
                "sucesso" -> {
                    // Navegar para o próximo fragmento
                    (activity as CadastroActivity).navegarPara(CadastroIdosoFragment())
                }
                "erro" -> {
                    showToast("Erro desconhecido!")
                }
                "erro_campos_vazios" -> {
                    showToast("Preencha todos os campos!")
                }
                "erro_senhas_diferentes" -> {
                    showToast("As senhas não coincidem!")
                }
                "erro_senha_fraca" -> {
                    showToast("A senha deve ter no mínimo 6 caracteres!")
                }
                "erro_email_invalido" -> {
                    showToast("Email inválido!")
                }
                "erro_firestone" -> {
                    showToast("Erro ao salvar dados no Firestore!")
                }
            }
        })

        btnContinuar.setOnClickListener {
            viewModel.nomeResponsavel = nomeResponsavel.text.toString()
            viewModel.telefone = telefone.text.toString()
            viewModel.email = email.text.toString()
            viewModel.senha = senha.text.toString()
            viewModel.confirmSenha = confirmSenha.text.toString()

            // Chama o método no ViewModel para validar e salvar
            viewModel.finalizarCadastro()
        }

        return view
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}