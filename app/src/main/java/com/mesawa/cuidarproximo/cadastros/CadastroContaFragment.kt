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
import androidx.lifecycle.ViewModelProvider
import com.mesawa.cuidarproximo.R

class CadastroContaFragment : Fragment() {

    private lateinit var nomeResponsavel: EditText
    private lateinit var telefone: EditText
    private lateinit var email: EditText
    private lateinit var senha: EditText
    private lateinit var confirmSenha: EditText

    private lateinit var txtEmailStatus: TextView
    private lateinit var txtSenhaStatus: TextView

    private lateinit var btnContinuar: Button

    private lateinit var viewModel: CadastroViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_cadastro_conta, container, false)

        viewModel = ViewModelProvider(requireActivity())[CadastroViewModel::class.java]

        nomeResponsavel = view.findViewById(R.id.editTextNomeResponsavel)
        telefone = view.findViewById(R.id.editTextTelefone)
        email = view.findViewById(R.id.editTextEmail)
        senha = view.findViewById(R.id.editTextPassword)
        confirmSenha = view.findViewById(R.id.editTextConfirmPassword)

        txtEmailStatus = view.findViewById(R.id.txtEmailStatus)
        txtSenhaStatus = view.findViewById(R.id.txtSenhaStatus)

        btnContinuar = view.findViewById(R.id.buttonContinuar)

        // 📞 MÁSCARA TELEFONE
        telefone.addTextChangedListener(MascaraTelefone(telefone))

        // 📧 VALIDAÇÃO EMAIL
        email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val emailTexto = s.toString()

                if (Patterns.EMAIL_ADDRESS.matcher(emailTexto).matches()) {
                    txtEmailStatus.text = "Email válido ✔"
                    txtEmailStatus.setTextColor(resources.getColor(android.R.color.holo_green_dark))
                } else {
                    txtEmailStatus.text = "Email inválido"
                    txtEmailStatus.setTextColor(resources.getColor(android.R.color.holo_red_dark))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // 🔐 VALIDAÇÃO SENHA
        confirmSenha.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                val senhaTexto = senha.text.toString()
                val confirmTexto = confirmSenha.text.toString()

                if (senhaTexto.length < 6) {
                    txtSenhaStatus.text = "Senha fraca (mín 6 caracteres)"
                    txtSenhaStatus.setTextColor(resources.getColor(android.R.color.holo_red_dark))
                    return
                }

                if (senhaTexto == confirmTexto) {
                    txtSenhaStatus.text = "Senha correta ✔"
                    txtSenhaStatus.setTextColor(resources.getColor(android.R.color.holo_green_dark))
                } else {
                    txtSenhaStatus.text = "Senhas não coincidem"
                    txtSenhaStatus.setTextColor(resources.getColor(android.R.color.holo_red_dark))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // 🚀 BOTÃO
        btnContinuar.setOnClickListener {

            if (!validarCampos()) return@setOnClickListener

            viewModel.nomeResponsavel = nomeResponsavel.text.toString()
            viewModel.telefone = telefone.text.toString()
            viewModel.email = email.text.toString()
            viewModel.senha = senha.text.toString()

            (activity as CadastroActivity).navegarPara(CadastroIdosoFragment())
        }

        return view
    }

    private fun validarCampos(): Boolean {

        if (nomeResponsavel.text.isEmpty() ||
            telefone.text.isEmpty() ||
            email.text.isEmpty() ||
            senha.text.isEmpty() ||
            confirmSenha.text.isEmpty()
        ) {
            Toast.makeText(context, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            Toast.makeText(context, "Email inválido!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (senha.text.toString().length < 6) {
            Toast.makeText(context, "Senha muito fraca!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (senha.text.toString() != confirmSenha.text.toString()) {
            Toast.makeText(context, "Senhas não coincidem!", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    // 📞 MÁSCARA TELEFONE
    class MascaraTelefone(private val telefone: EditText) : TextWatcher {

        private var isUpdating = false

        override fun afterTextChanged(s: Editable?) {
            if (s == null) return
            if (isUpdating) return

            isUpdating = true

            val str = s.toString().replace("[^\\d]".toRegex(), "")
            val formatted = StringBuilder()

            for (i in str.indices) {
                formatted.append(str[i])

                if (i == 1) formatted.append(") ")
                if (i == 6) formatted.append("-")
            }

            val result = if (str.length >= 2) {
                "(" + formatted.toString()
            } else {
                str
            }

            telefone.setText(result)
            telefone.setSelection(result.length)

            isUpdating = false
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
}