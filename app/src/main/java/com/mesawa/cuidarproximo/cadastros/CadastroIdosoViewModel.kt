package com.mesawa.cuidarproximo.cadastros

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class CadastroIdosoViewModel : ViewModel() {

    var nomeIdoso: String = ""
    var cpfIdoso: String = ""
    var dataNascimento: String = ""
    var genero: String = ""
    var cidade: String = ""
    var condicao: String = ""
    var dependencia: String = ""

    // Status do cadastro
    val cadastroStatus: MutableLiveData<String> = MutableLiveData()

    // Função para finalizar o cadastro do idoso
    fun finalizarCadastro() {
        Log.d("CADASTRO", "Iniciando cadastro do idoso no Firestore")

        // Verificando se os campos obrigatórios estão preenchidos
        if (nomeIdoso.isEmpty() || cpfIdoso.isEmpty() || dataNascimento.isEmpty() || cidade.isEmpty()) {
            cadastroStatus.value = "erro"
            Log.d("CADASTRO", "Erro: Campos obrigatórios não preenchidos")
            return
        }

        // Preparando os dados para salvar no Firestore
        val db = FirebaseFirestore.getInstance()
        val dados = hashMapOf(
            "nome_idoso" to nomeIdoso,
            "cpf_idoso" to cpfIdoso,
            "data_nascimento" to dataNascimento,
            "genero" to genero,
            "cidade" to cidade,
            "condicao" to condicao,
            "dependencia" to dependencia,
            "timestamp" to System.currentTimeMillis()
        )

        // Salvando os dados no Firestore
        db.collection("usuarios")
            .add(dados)
            .addOnSuccessListener { documentReference ->
                Log.d("CADASTRO", "🔥 Cadastro do idoso realizado com sucesso! ID: ${documentReference.id}")
                cadastroStatus.value = "sucesso" // Cadastro realizado com sucesso
            }
            .addOnFailureListener { exception ->
                Log.e("CADASTRO", "🔥 Erro ao cadastrar no Firestore", exception)
                cadastroStatus.value = "erro" // Erro ao tentar salvar os dados
            }
    }
}