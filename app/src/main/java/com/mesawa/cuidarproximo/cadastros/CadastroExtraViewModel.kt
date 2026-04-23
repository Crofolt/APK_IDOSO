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

    // Função para finalizar o cadastro extra (cadastra os dados no Firestore)
    fun finalizarCadastro() {
        Log.d("CADASTRO", "Iniciando cadastro extra no Firestore")

        // Verificando se o CPF do cuidador está preenchido corretamente
        if (cpfCuidador.isEmpty()) {
            cadastroStatus.value = "erro"
            Log.d("CADASTRO", "Erro: CPF do cuidador não informado")
            return
        }

        // Preparando os dados para salvar no Firestore
        val db = FirebaseFirestore.getInstance()
        val dados = hashMapOf(
            "cpf_cuidador" to cpfCuidador,
            "aceitou_termos" to aceitouTermos,
            "aceitou_email" to aceitouEmail,
            "timestamp" to System.currentTimeMillis()
        )

        // Logando os dados que serão enviados para o Firestore
        Log.d("CADASTRO", "Enviando dados para o Firestore: CPF Cuidador = $cpfCuidador, Aceitou Termos = $aceitouTermos, Aceitou Email = $aceitouEmail")

        // Salvando os dados no Firestore
        db.collection("usuarios")
            .add(dados)
            .addOnSuccessListener { documentReference ->
                Log.d("CADASTRO", "🔥 Cadastro realizado com sucesso! ID: ${documentReference.id}")
                cadastroStatus.value = "sucesso" // Cadastro realizado com sucesso
            }
            .addOnFailureListener { exception ->
                Log.e("CADASTRO", "🔥 Erro ao cadastrar no Firestore: ${exception.message}", exception)
                cadastroStatus.value = "erro" // Erro ao tentar salvar os dados
            }
    }
}