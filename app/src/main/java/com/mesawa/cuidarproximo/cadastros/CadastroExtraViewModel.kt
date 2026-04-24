package com.mesawa.cuidarproximo.cadastros

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class CadastroExtraViewModel : ViewModel() {

    var cpfCuidador: String = ""
    var aceitouTermos: Boolean = false
    var aceitouEmail: Boolean = false

    // Status do cadastro
    val cadastroStatus: MutableLiveData<String> = MutableLiveData()

    // Função para finalizar o cadastro extra
    fun finalizarCadastro() {
        Log.d("CADASTRO", "Iniciando cadastro extra no Firestore")

        // Preparando os dados para salvar no Firestore
        val db = FirebaseFirestore.getInstance()
        val dados = hashMapOf(
            "cpf_cuidador" to cpfCuidador,
            "aceitou_termos" to aceitouTermos,
            "aceitou_email" to aceitouEmail,
            "timestamp" to System.currentTimeMillis()
        )

        // Salvando os dados no Firestore
        db.collection("usuarios")
            .add(dados)
            .addOnSuccessListener {
                Log.d("CADASTRO", "Cadastro realizado com sucesso!")
                cadastroStatus.value = "sucesso"
            }
            .addOnFailureListener {
                Log.e("CADASTRO", "Erro ao cadastrar no Firestore", it)
                cadastroStatus.value = "erro"
            }
    }
}