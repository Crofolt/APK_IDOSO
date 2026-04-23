package com.mesawa.cuidarproximo.cadastros

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore


import org.mindrot.jbcrypt.BCrypt


class CadastroContaViewModel : ViewModel() {

    var nomeResponsavel: String = ""
    var telefone: String = ""
    var email: String = ""
    var senha: String = ""
    var confirmSenha: String = ""

    // Status do cadastro
    val cadastroStatus: MutableLiveData<String> = MutableLiveData()

    // Função para finalizar o cadastro da conta e salvar no Firestore
    fun finalizarCadastro() {
        if (validarCampos()) {
            salvarNoFirestore()
        } else {
            cadastroStatus.value = "erro"
        }
    }

    private fun validarCampos(): Boolean {
        // Validação básica
        if (nomeResponsavel.isEmpty() || telefone.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmSenha.isEmpty()) {
            cadastroStatus.value = "erro_campos_vazios"
            return false
        }

        if (senha != confirmSenha) {
            cadastroStatus.value = "erro_senhas_diferentes"
            return false
        }

        if (senha.length < 6) {
            cadastroStatus.value = "erro_senha_fraca"
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            cadastroStatus.value = "erro_email_invalido"
            return false
        }

        return true
    }

    private fun salvarNoFirestore() {
        // Gerar o hash da senha usando Bcrypt
        val hashedPassword = hashPassword(senha)

        val db = FirebaseFirestore.getInstance()

        val user = hashMapOf(
            "nome_responsavel" to nomeResponsavel,
            "telefone" to telefone,
            "email" to email,
            "senha" to hashedPassword // Salvando a senha hasheada
        )

        db.collection("usuarios")
            .add(user)
            .addOnSuccessListener {
                cadastroStatus.value = "sucesso"
            }
            .addOnFailureListener {
                cadastroStatus.value = "erro_firestone"
            }
    }

    // Função para fazer o hash da senha usando Bcrypt
    private fun hashPassword(password: String): String {
        // Gera o salt
        val salt = BCrypt.gensalt()

        // Gera o hash da senha usando o salt
        return BCrypt.hashpw(password, salt)
    }
}