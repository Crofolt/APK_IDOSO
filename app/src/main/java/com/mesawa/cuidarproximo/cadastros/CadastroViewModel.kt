package com.mesawa.cuidarproximo.cadastros

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class CadastroViewModel : ViewModel() {

    val cadastroStatus: MutableLiveData<String> = MutableLiveData()

    // 🔥 DADOS DO RESPONSÁVEL
    var nomeResponsavel: String = ""
    var telefone: String = ""
    var email: String = ""
    var senha: String = ""

    // 🔥 DADOS DO IDOSO
    var nomeIdoso: String = ""
    var cpfIdoso: String = ""
    var dataNascimento: String = ""
    var genero: String = ""
    var cidade: String = ""
    var condicao: String = ""
    var dependencia: String = ""

    // 🔥 DADOS FINAIS
    var cpfCuidador: String = ""
    var aceitouTermos: Boolean = false
    var aceitouEmail: Boolean = false

    // 🔥 FUNÇÃO FINAL DE CADASTRO
    fun finalizarCadastro() {
        val db = FirebaseFirestore.getInstance()

        if (nomeResponsavel.isNotEmpty() &&
            email.isNotEmpty() &&
            senha.isNotEmpty() &&
            nomeIdoso.isNotEmpty() &&
            cpfIdoso.isNotEmpty() &&
            aceitouTermos
        ) {

            val dados = hashMapOf(
                // RESPONSÁVEL
                "nome_responsavel" to nomeResponsavel,
                "telefone" to telefone,
                "email" to email,
                "cpf_cuidador" to cpfCuidador,

                // IDOSO
                "nome_idoso" to nomeIdoso,
                "cpf_idoso" to cpfIdoso,
                "data_nascimento" to dataNascimento,
                "genero" to genero,
                "cidade" to cidade,
                "condicao" to condicao,
                "dependencia" to dependencia,

                // CONFIG
                "aceita_email" to aceitouEmail,
                "timestamp" to System.currentTimeMillis()
            )

            db.collection("usuarios")
                .add(dados)
                .addOnSuccessListener {
                    cadastroStatus.value = "sucesso"
                }
                .addOnFailureListener {
                    cadastroStatus.value = "erro"
                }

        } else {
            cadastroStatus.value = "erro"
        }
    }
}