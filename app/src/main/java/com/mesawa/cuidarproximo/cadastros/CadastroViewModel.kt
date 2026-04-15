package com.mesawa.cuidarproximo.cadastros

import android.util.Log
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

        Log.d("CADASTRO", "Entrou no finalizarCadastro")

        if (nomeResponsavel.isNotEmpty() &&
            email.isNotEmpty() &&
            senha.isNotEmpty() &&
            nomeIdoso.isNotEmpty() &&
            cpfIdoso.isNotEmpty() &&
            aceitouTermos
        ) {

            Log.d("CADASTRO", "Passou na validação")

            val dados = hashMapOf(
                "nome_responsavel" to nomeResponsavel,
                "telefone" to telefone,
                "email" to email,
                "cpf_cuidador" to cpfCuidador,
                "nome_idoso" to nomeIdoso,
                "cpf_idoso" to cpfIdoso,
                "data_nascimento" to dataNascimento,
                "genero" to genero,
                "cidade" to cidade,
                "condicao" to condicao,
                "dependencia" to dependencia,
                "aceita_email" to aceitouEmail,
                "timestamp" to System.currentTimeMillis()
            )

            db.collection("usuarios")
                .add(dados)
                .addOnSuccessListener {
                    Log.d("CADASTRO", "🔥 SUCESSO FIREBASE")
                    cadastroStatus.value = "sucesso"
                }
                .addOnFailureListener {
                    Log.e("CADASTRO", "🔥 ERRO FIREBASE", it)
                    cadastroStatus.value = "erro"
                }

        } else {
            Log.e("CADASTRO", "❌ FALHOU NA VALIDAÇÃO")
            cadastroStatus.value = "erro"
        }
    }
}