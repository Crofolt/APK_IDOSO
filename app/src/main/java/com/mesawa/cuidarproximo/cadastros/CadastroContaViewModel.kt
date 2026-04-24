package com.mesawa.cuidarproximo.cadastros

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.mindrot.jbcrypt.BCrypt

class CadastroContaViewModel : ViewModel() {

    var nomeResponsavel: String = ""
    var telefone: String = ""
    var email: String = ""
    var senha: String = ""
    var confirmSenha: String = ""

    // Status do cadastro
    val cadastroStatus: MutableLiveData<String> = MutableLiveData()

    // Função para validar os campos
    fun finalizarCadastro() {
        if (validarCampos()) {
            cadastroStatus.value = "sucesso"
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

    // Função para salvar no Firestore
    fun salvarNoFirestore() {
        val hashedPassword = hashPassword(senha)

        // Código para salvar no Firestore
        // Firestore.saveData(nomeResponsavel, telefone, email, hashedPassword)
    }

    // Função para fazer o hash da senha usando Bcrypt
    private fun hashPassword(password: String): String {
        val salt = BCrypt.gensalt()
        return BCrypt.hashpw(password, salt)
    }
}