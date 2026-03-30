package com.mesawa.cuidarproximo.cadastros

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mesawa.cuidarproximo.R

class CadastroExtraFragment : Fragment() {

    private lateinit var cpfCuidador: EditText
    private lateinit var txtCpfStatus: TextView

    private lateinit var checkBoxTerms: CheckBox
    private lateinit var checkBoxEmail: CheckBox
    private lateinit var btnFinalizar: Button
    private lateinit var btnVerTermos: Button

    private lateinit var viewModel: CadastroViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_cadastro_extra, container, false)

        viewModel = ViewModelProvider(requireActivity())[CadastroViewModel::class.java]

        cpfCuidador = view.findViewById(R.id.editTextCpfCuidador)
        txtCpfStatus = view.findViewById(R.id.txtCpfStatus)

        checkBoxTerms = view.findViewById(R.id.checkBoxTerms)
        checkBoxEmail = view.findViewById(R.id.checkBoxEmail)
        btnFinalizar = view.findViewById(R.id.buttonFinalizar)
        btnVerTermos = view.findViewById(R.id.btnVerTermos)

        // 🔥 OBSERVER (FORA do botão, como deve ser)
        viewModel.cadastroStatus.observe(viewLifecycleOwner) {
            Log.d("CADASTRO", "Status recebido: $it")

            if (it == "erro") {
                Toast.makeText(context, "Erro no cadastro!", Toast.LENGTH_SHORT).show()
                btnFinalizar.isEnabled = true
            }
        }

        // 🔥 MÁSCARA + VALIDAÇÃO
        cpfCuidador.addTextChangedListener(MascaraCPF(cpfCuidador))

        cpfCuidador.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val cpf = s.toString()

                if (isCPFValido(cpf)) {
                    txtCpfStatus.text = "CPF válido ✔"
                    txtCpfStatus.setTextColor(resources.getColor(android.R.color.holo_green_dark))
                } else {
                    txtCpfStatus.text = "CPF inválido"
                    txtCpfStatus.setTextColor(resources.getColor(android.R.color.holo_red_dark))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // 📜 TERMOS (mantido igual)
        btnVerTermos.setOnClickListener {
            // Criar o AlertDialog para mostrar os termos
            val builder = android.app.AlertDialog.Builder(requireContext())
            builder.setTitle("Termos e Condições")
            builder.setMessage(
                "TERMOS DE USO – CUIDAR PRÓXIMO\n\n" +

                        "1. OBJETIVO DA PLATAFORMA\n" +
                        "A plataforma Cuidar Próximo tem como objetivo conectar cuidadores a pessoas idosas que necessitam de assistência, facilitando a organização de informações e o acompanhamento de cuidados.\n\n" +

                        "2. RESPONSABILIDADE DAS INFORMAÇÕES\n" +
                        "O usuário declara que todas as informações fornecidas são verdadeiras, completas e atualizadas. A plataforma não se responsabiliza por dados incorretos inseridos.\n\n" +

                        "3. NATUREZA DO SERVIÇO\n" +
                        "A plataforma atua apenas como intermediadora, não sendo responsável pela execução direta dos serviços prestados pelos cuidadores.\n\n" +

                        "4. CONDIÇÕES DE SAÚDE\n" +
                        "As informações sobre saúde possuem caráter informativo e não substituem avaliação médica profissional.\n\n" +

                        "5. PRIVACIDADE E DADOS\n" +
                        "Os dados fornecidos serão armazenados com segurança e utilizados apenas para funcionamento da plataforma, conforme a Lei Geral de Proteção de Dados (LGPD).\n\n" +

                        "6. SEGURANÇA DA CONTA\n" +
                        "O usuário é responsável por manter a confidencialidade de suas credenciais de acesso.\n\n" +

                        "7. LIMITAÇÃO DE RESPONSABILIDADE\n" +
                        "A plataforma não se responsabiliza por danos decorrentes do uso dos serviços.\n\n" +

                        "8. USO ADEQUADO\n" +
                        "É proibido utilizar a plataforma para fins ilegais ou fraudulentos.\n\n" +

                        "9. CANCELAMENTO\n" +
                        "O usuário pode solicitar o encerramento da conta a qualquer momento.\n\n" +

                        "10. ALTERAÇÕES\n" +
                        "Os termos podem ser atualizados a qualquer momento, sendo responsabilidade do usuário revisá-los.\n\n" +

                        "Ao utilizar a plataforma, você concorda com todos os termos acima."
            )

            builder.setPositiveButton("Fechar") { dialog, _ ->
                dialog.dismiss()  // Fechar o dialog ao clicar em "Fechar"
            }

            builder.setCancelable(true)  // Permitir que o dialog seja fechado tocando fora

            builder.create().show()
        }

        // 🚀 FINALIZAR
        btnFinalizar.setOnClickListener {

            Log.d("CADASTRO", "Botão FINALIZAR clicado")

            if (!validarCampos()) {
                Log.d("CADASTRO", "Validação falhou")
                return@setOnClickListener
            }

            // 🔥 salva ANTES (CORRETO)
            viewModel.cpfCuidador = cpfCuidador.text.toString()
            viewModel.aceitouTermos = checkBoxTerms.isChecked
            viewModel.aceitouEmail = checkBoxEmail.isChecked

            Log.d("CADASTRO", "Dados salvos no ViewModel")

            // 🔥 trava botão
            btnFinalizar.isEnabled = false

            // 🔥 chama Firebase (UMA VEZ SÓ)
            viewModel.finalizarCadastro()
        }

        return view
    }

    private fun isCPFValido(cpf: String): Boolean {
        val cleanCpf = cpf.replace("[^\\d]".toRegex(), "")

        if (cleanCpf.length != 11 || cleanCpf.all { it == cleanCpf[0] }) return false

        return try {
            val numbers = cleanCpf.map { it.toString().toInt() }

            val sum1 = (0..8).sumOf { (10 - it) * numbers[it] }
            val digit1 = ((sum1 * 10) % 11).let { if (it == 10) 0 else it }

            val sum2 = (0..9).sumOf { (11 - it) * numbers[it] }
            val digit2 = ((sum2 * 10) % 11).let { if (it == 10) 0 else it }

            digit1 == numbers[9] && digit2 == numbers[10]

        } catch (e: Exception) {
            false
        }
    }

    private fun validarCampos(): Boolean {

        if (cpfCuidador.text.isEmpty()) {
            Toast.makeText(context, "Informe o CPF!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!isCPFValido(cpfCuidador.text.toString())) {
            Toast.makeText(context, "CPF inválido!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!checkBoxTerms.isChecked) {
            Toast.makeText(context, "Aceite os termos!", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    class MascaraCPF(private val editText: EditText) : TextWatcher {

        private var isUpdating = false

        override fun afterTextChanged(s: Editable?) {
            if (isUpdating) return

            isUpdating = true

            val str = s.toString().replace("[^\\d]".toRegex(), "")
            val formatted = StringBuilder()

            for (i in str.indices) {
                formatted.append(str[i])
                if (i == 2 || i == 5) formatted.append(".")
                if (i == 8) formatted.append("-")
            }

            editText.setText(formatted.toString())
            editText.setSelection(formatted.length)

            isUpdating = false
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
}